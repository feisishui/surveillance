package com.casic.patrol.bpm.web;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.StoreDTO;
import com.casic.patrol.api.form.FormConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskConstants;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.keyvalue.KeyValueConnector;
import com.casic.patrol.api.keyvalue.Prop;
import com.casic.patrol.api.keyvalue.Record;
import com.casic.patrol.api.notification.NotificationConnector;
import com.casic.patrol.api.notification.NotificationDTO;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.cmd.*;
import com.casic.patrol.bpm.graph.Graph;
import com.casic.patrol.bpm.persistence.domain.BpmCategory;
import com.casic.patrol.bpm.persistence.domain.BpmProcess;
import com.casic.patrol.bpm.persistence.manager.BpmCategoryManager;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.bpm.service.TraceService;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.humantask.persistence.domain.TaskParticipant;
import com.casic.patrol.humantask.persistence.manager.TaskParticipantManager;
import com.casic.patrol.overtime.persistence.domain.EmergencyLevel;
import com.casic.patrol.overtime.persistence.domain.OvertimeLevel;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
import com.casic.patrol.spi.process.InternalProcessConnector;
import com.casic.patrol.todoTask.dto.ToDoTaskDTO;
import com.casic.patrol.util.StringUtils;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.activiti.engine.*;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

/**
 * 我的流程 待办流程 已办未结
 */
@Controller
@RequestMapping("bpm")
public class WorkspaceController {
    private static Logger logger = LoggerFactory
            .getLogger(WorkspaceController.class);
    private BpmCategoryManager bpmCategoryManager;
    private BpmProcessManager bpmProcessManager;
    private ProcessEngine processEngine;
    private UserConnector userConnector;
    private ProcessConnector processConnector;
    private TraceService traceService;
    private TenantHolder tenantHolder;
    private KeyValueConnector keyValueConnector;
    private JsonMapper jsonMapper = new JsonMapper();
    private HumanTaskConnector humanTaskConnector;
    private NotificationConnector notificationConnector;
    private InternalProcessConnector internalProcessConnector;
    private String baseUrl;
    private BeanMapper beanMapper = new BeanMapper();

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    @RequestMapping("home")
    public String home(Model model) {
        return "bpm/console-listProcessInstancesMap";
    }

    @RequestMapping("workspace-home")
    public String workspaceHome(Model model) {
        String tenantId = tenantHolder.getTenantId();
        String hql = "from BpmCategory where tenantId=? order by priority";
        List<BpmCategory> bpmCategories = bpmCategoryManager
                .find(hql, tenantId);
        model.addAttribute("bpmCategories", bpmCategories);

        return "bpm/workspace-home";
    }

    @RequestMapping("workspace-graphProcessDefinition")
    public void graphProcessDefinition(
            @RequestParam("bpmProcessId") Long bpmProcessId,
            HttpServletResponse response) throws Exception {
        BpmProcess bpmProcess = bpmProcessManager.get(bpmProcessId);
        String processDefinitionId = bpmProcess.getBpmConfBase()
                .getProcessDefinitionId();

        Command<InputStream> cmd = null;
        cmd = new ProcessDefinitionDiagramCmd(processDefinitionId);

        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        response.setContentType("image/png");

        IOUtils.copy(is, response.getOutputStream());
    }

    // ~ ======================================================================
    @RequestMapping("workspace-endProcessInstance")
    public String endProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId) {

        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        Authentication.setAuthenticatedUserId(currentUserHolder.getUserId());
        processEngine.getRuntimeService().deleteProcessInstance(
                processInstanceId, "人工终止");

        return "redirect:/bpm/workspace-listRunningProcessInstances.do";
    }

    @RequestMapping("workspace-copyProcessInstance")
    public String copyProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId)
            throws Exception {
        // 复制流程
        // 1. 从历史获取businessKey
        HistoricProcessInstance historicProcessInstance = processEngine
                .getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String businessKey = historicProcessInstance.getBusinessKey();
        String processDefinitionId = historicProcessInstance
                .getProcessDefinitionId();

        // 2. 从businessKey获取keyvalue
        Record original = keyValueConnector.findByCode(businessKey);

        // 3. 找到流程的第一个form
        FormDTO formDto = this.processConnector
                .findStartForm(processDefinitionId);

        List<String> fieldNames = new ArrayList<String>();

        if (formDto.isExists()) {
            String content = formDto.getContent();
            logger.debug("content : {}", content);

            Map<String, Object> formJson = jsonMapper.fromJson(
                    formDto.getContent(), Map.class);
            List<Map<String, Object>> sections = (List<Map<String, Object>>) formJson
                    .get("sections");

            for (Map<String, Object> section : sections) {
                if (!"grid".equals(section.get("type"))) {
                    continue;
                }

                List<Map<String, Object>> fields = (List<Map<String, Object>>) section
                        .get("fields");

                for (Map<String, Object> field : fields) {
                    logger.debug("field : {}", field);

                    String type = (String) field.get("type");
                    String name = (String) field.get("name");
                    String label = name;

                    if ("label".equals(type)) {
                        continue;
                    }

                    // if (formField != null) {
                    // continue;
                    // }
                    fieldNames.add(name);
                }
            }
        }

        logger.debug("fieldNames : {}", fieldNames);

        // 4. 使用第一个form复制数据，后续的审批意见数据之类的不要复制
        Record record = keyValueConnector.copyRecord(original, fieldNames);

        // 5. 跳转到草稿箱
        return "redirect:/operation/process-operation-listDrafts.do";
    }

    /**
     * 流程列表（所有的流程定义即流程模型）
     * 
     * @return
     */
    @RequestMapping("workspace-listProcessDefinitions")
    public String listProcessDefinitions(Model model) {
        String tenantId = tenantHolder.getTenantId();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        List<ProcessDefinition> processDefinitions = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionTenantId(tenantId).active().list();
        model.addAttribute("processDefinitions", processDefinitions);

        return "bpm/workspace-listProcessDefinitions";
    }

    @RequestMapping("workspace-listRunningProcessInstances")
    public String listRunningProcessInstances(@ModelAttribute Page page,
            Model model) {
        // TODO:确认用户有执行权限
        String tenantId = tenantHolder.getTenantId();
        List<HistoricProcessInstance> instances =
                processConnector.findAllRunningProcessInstances(tenantId);
        List<ToDoTaskDTO> dtos = convertToDoTaskDtos(instances);
        dtos = sort(dtos);
        slice(dtos, page);
        model.addAttribute("page", page);
        return "bpm/workspace-listRunningProcessInstances";
    }

    /**
     * 根据page信息截取对应数据list，存入result
     * @param dtos
     * @param page
     */
    private void slice(List<ToDoTaskDTO> dtos, Page page) {
        int start = (page.getPageNo() - 1) * page.getPageSize();
        if (start > dtos.size()) {
            start = 0;
            page.setPageNo(1);
        }
        int end = start + page.getPageSize() > dtos.size() ? dtos.size() : start + page.getPageSize();
        page.setTotalCount(dtos.size());
        page.setResult(dtos.subList(start, end));
    }

    /**
     * 根据要求排序，首先以超时级别排序，
     * 其次按照紧急级别排序，最后按照流程生成日期排序
     * @param dtos
     * @return
     */
    private List<ToDoTaskDTO> sort(List<ToDoTaskDTO> dtos) {
        Collections.sort(dtos, new Comparator<ToDoTaskDTO>() {
            @Override
            public int compare(ToDoTaskDTO o1, ToDoTaskDTO o2) {
                return o2.getEmergencyLevelNum() - o1.getEmergencyLevelNum();
            }
        });
        Collections.sort(dtos, new Comparator<ToDoTaskDTO>() {
            @Override
            public int compare(ToDoTaskDTO o1, ToDoTaskDTO o2) {
                return o2.getOvertimeLevelNum() - o1.getOvertimeLevelNum();
            }
        });
        return dtos;
    }

    private List<ToDoTaskDTO> convertToDoTaskDtos(Collection<HistoricProcessInstance> infos){
        List<ToDoTaskDTO> toDoTaskDTOs = new ArrayList<ToDoTaskDTO>();
        for (HistoricProcessInstance taskInfo : infos) {
            toDoTaskDTOs.add(convertToDoTaskDto(taskInfo));
        }
        return toDoTaskDTOs;
    }

    public ToDoTaskDTO convertToDoTaskDto(HistoricProcessInstance instance) {
        ToDoTaskDTO toDoTaskDTO = new ToDoTaskDTO();
        beanMapper.copy(instance, toDoTaskDTO);
        TaskOvertimeInfo info = TaskOvertimeInfoManager.getMostSeriousInfo(
                taskOvertimeInfoManager.getUnfinishedBy(instance.getId(), instance.getBusinessKey())
        );
        if (info != null) {
            toDoTaskDTO.setOvertimeLevel(OvertimeLevel.valueOf(info.getLevelCode()));
        } else {
            toDoTaskDTO.setOvertimeLevel(OvertimeLevel.UNKNOWN);
        }
        Record record = keyValueConnector.findByRef(instance.getId());
        if (record != null) {
            toDoTaskDTO.setEventTime(record.getCreateTime());
            Map<String, Prop> props =  record.getProps();
            if (props.containsKey("eventType")) {
                toDoTaskDTO.setEventType(props.get("eventType").getValue());
            }
            if (props.containsKey("emergencyLevel")) {
                toDoTaskDTO.setEmergencyLevel(props.get("emergencyLevel").getValue());
            }
        }
        toDoTaskDTO.setOvertimeLevelNum(
                toDoTaskDTO.getOvertimeLevel().getLevel()
        );
        toDoTaskDTO.setEmergencyLevelNum(
                EmergencyLevel.getByDesc(toDoTaskDTO.getEmergencyLevel()).getLevel()
        );
        return toDoTaskDTO;
    }

    /**
     * 已结流程.
     * 
     * @return
     */
    @RequestMapping("workspace-listCompletedProcessInstances")
    public String listCompletedProcessInstances(@ModelAttribute Page page,
            Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();

        page = processConnector.findCompletedProcessInstances(userId, tenantId,
                page);
        model.addAttribute("page", page);

        return "bpm/workspace-listCompletedProcessInstances";
    }

    /**
     * 用户参与的流程（包含已经完成和未完成）
     * 
     * @return
     */
    @RequestMapping("workspace-listInvolvedProcessInstances")
    public String listInvolvedProcessInstances(@ModelAttribute Page page,
            Model model) {
        // TODO: finished(), unfinished()
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = processConnector.findInvolvedProcessInstances(userId, tenantId,
                page);
        model.addAttribute("page", page);

        return "bpm/workspace-listInvolvedProcessInstances";
    }

    /**
     * 流程跟踪
     * 
     * @throws Exception
     */
    @RequestMapping("workspace-graphHistoryProcessInstance")
    public void graphHistoryProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId,
            HttpServletResponse response) throws Exception {
        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        response.setContentType("image/png");

        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 待办任务（个人任务）
     * 
     * @return
     */
    @RequestMapping("workspace-listPersonalTasks")
    public String listPersonalTasks(@ModelAttribute Page page, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = processConnector.findPersonalTasks(userId, tenantId, page);
        model.addAttribute("page", page);

        return "bpm/workspace-listPersonalTasks";
    }

    /**
     * 代领任务（组任务）
     * 
     * @return
     */
    @RequestMapping("workspace-listGroupTasks")
    public String listGroupTasks(@ModelAttribute Page page, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();

        page = processConnector.findGroupTasks(userId, tenantId, page);
        model.addAttribute("page", page);

        return "bpm/workspace-listGroupTasks";
    }

    /**
     * 已办任务（历史任务）
     * 
     * @return
     */
    @RequestMapping("workspace-listHistoryTasks")
    public String listHistoryTasks(@ModelAttribute Page page, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();

        page = processConnector.findHistoryTasks(userId, tenantId, page);
        model.addAttribute("page", page);

        return "bpm/workspace-listHistoryTasks";
    }

    /**
     * 代理中的任务（代理人还未完成该任务）
     * 
     * @return
     */
    @RequestMapping("workspace-listDelegatedTasks")
    public String listDelegatedTasks(@ModelAttribute Page page, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());
        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = processConnector.findGroupTasks(userId, tenantId, page);
        model.addAttribute("page", page);

        return "bpm/workspace-listDelegatedTasks";
    }

    /**
     * 同时返回已领取和未领取的任务.
     */
    @RequestMapping("workspace-listCandidateOrAssignedTasks")
    public String listCandidateOrAssignedTasks(@ModelAttribute Page page,
            Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = processConnector.findCandidateOrAssignedTasks(userId, tenantId,
                page);
        model.addAttribute("page", page);

        return "bpm/workspace-listCandidateOrAssignedTasks";
    }

    // ~ ======================================================================
    /**
     * 发起流程页面（启动一个流程实例）内置流程表单方式
     * 
     * @return
     */
    @RequestMapping("workspace-prepareStartProcessInstance")
    public String prepareStartProcessInstance(
            @RequestParam("processDefinitionId") String processDefinitionId,
            Model model) {
        FormService formService = processEngine.getFormService();
        StartFormData startFormData = formService
                .getStartFormData(processDefinitionId);
        model.addAttribute("startFormData", startFormData);

        return "bpm/workspace prepareStartProcessInstance";
    }

    // ~ ======================================================================
    /**
     * 完成任务页面
     * 
     * @return
     */
    @RequestMapping("workspace-prepareCompleteTask")
    public String prepareCompleteTask(@RequestParam("taskId") String taskId,
            Model model) {
        FormService formService = processEngine.getFormService();

        TaskFormData taskFormData = formService.getTaskFormData(taskId);

        model.addAttribute("taskFormData", taskFormData);

        return "bpm/workspace-prepareCompleteTask";
    }

    /**
     * 认领任务（对应的是在组任务，即从组任务中领取任务）
     *
     * @return
     */
    @Autowired
    private HttpSession session;
    @RequestMapping("workspace-claimTask")
    public String claimTask(@RequestParam("taskId") String taskId) {

        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();

        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);

        return "redirect:/bpm/workspace-listPersonalTasks.do";
    }

    /**
     * 任务代理页面
     * 
     * @return
     */
    @RequestMapping("workspace-prepareDelegateTask")
    public String prepareDelegateTask() {
        return "bpm/workspace-prepareDelegateTask";
    }

    /**
     * 任务代理
     * 
     * @return
     */
    @RequestMapping("workspace-delegateTask")
    public String delegateTask(@RequestParam("taskId") String taskId,
            @RequestParam("userId") String userId) {
        TaskService taskService = processEngine.getTaskService();
        taskService.delegateTask(taskId, userId);

        return "redirect:/bpm/workspace-listPersonalTasks.do";
    }

    /**
     * TODO 该方法有用到？
     * 
     * @return
     */
    @RequestMapping("workspace-resolveTask")
    public String resolveTask(@RequestParam("taskId") String taskId) {
        TaskService taskService = processEngine.getTaskService();
        taskService.resolveTask(taskId);

        return "redirect:/bpm/workspace-listPersonalTasks.do";
    }

    /**
     * 查看历史【包含流程跟踪、任务列表（完成和未完成）、流程变量】.
     */
    @RequestMapping("workspace-viewHistory")
    public String viewHistory(
            @RequestParam("processInstanceId") String processInstanceId,@RequestParam(value = "src",required = false) String src,
            Model model) {
        if(src!=null){
            model.addAttribute("src", src);
        }

        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        if (userId.equals(historicProcessInstance.getStartUserId())) {
            // startForm
        }

        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        // List<HistoricVariableInstance> historicVariableInstances = historyService
        // .createHistoricVariableInstanceQuery()
        // .processInstanceId(processInstanceId).list();
        model.addAttribute("historicTasks", historicTasks);

        // 获取流程对应的所有人工任务（目前还没有区分历史）
        List<HumanTaskDTO> humanTasks = humanTaskConnector
                .findHumanTasksByProcessInstanceId(processInstanceId);
        List<HumanTaskDTO> humanTaskDtos = new ArrayList<HumanTaskDTO>();

        for (HumanTaskDTO humanTaskDto : humanTasks) {
            if (humanTaskDto.getParentId() != null) {
                continue;
            }

            humanTaskDtos.add(humanTaskDto);
        }

        model.addAttribute("humanTasks", humanTaskDtos);
        // model.addAttribute("historicVariableInstances",
        // historicVariableInstances);
        model.addAttribute("nodeDtos",
                traceService.traceProcessInstance(processInstanceId));
        model.addAttribute("historyActivities", processEngine
                .getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list());

        if (historicProcessInstance.getEndTime() == null) {
            model.addAttribute("currentActivities", processEngine
                    .getRuntimeService()
                    .getActiveActivityIds(processInstanceId));
        } else {
            model.addAttribute("currentActivities", Collections
                    .singletonList(historicProcessInstance.getEndActivityId()));
        }

        Graph graph = processEngine.getManagementService().executeCommand(
                new FindHistoryGraphCmd(processInstanceId));
        model.addAttribute("graph", graph);
        model.addAttribute("historicProcessInstance", historicProcessInstance);

        //增加事件描述
        try {
            setXform(model, processInstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "bpm/workspace-viewHistory";
    }

    @Value("${view.history.detail.code}")
    private String code;
    @Resource
    private FormConnector formConnector;
    @Resource
    private StoreConnector storeConnector;

    private void setXform(Model model, String processInstanceId) throws Exception {
        Record record = keyValueConnector.findByRef(processInstanceId);
        FormDTO content = formConnector.findForm(code, record.getTenantId());

        Xform xform = new XformBuilder().setStoreConnector(storeConnector)
                .setUserConnector(userConnector)
                .setContent(content.getContent()).setRecord(record).build();
        model.addAttribute("xform", xform);
    }

    @RequestMapping("getStorePath")
    @ResponseBody
    public Map<String, Object> getStorePath(
            @RequestParam(value = "model") String model,
            @RequestParam(value = "key") String key) {
        Map<String,Object> map = new HashMap<String,Object>();
        String tenantId = tenantHolder.getTenantId();
        try {
            StoreDTO storeDto = storeConnector.getStore(model, key, tenantId);
            if (storeDto != null && null != storeDto.getAbsolutePath()) {
                map.put("success", true);
                map.put("path", storeDto.getAbsolutePath());
            } else {
                map.put("success", false);
                map.put("msg", "无法查询到该文件！");
            }
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @RequestMapping("getInstanceForm")
    @ResponseBody
    public Map<String, Object> getInstanceForm(
            @RequestParam(value = "processInstanceId")String processInstanceId) {
        Map<String,Object> map = new HashMap<String,Object>();
        Record record = keyValueConnector.findByRef(processInstanceId);
        FormDTO content = formConnector.findForm(code, record.getTenantId());
        try {
            Xform xform = new XformBuilder().setStoreConnector(storeConnector)
                    .setUserConnector(userConnector)
                    .setContent(content.getContent()).setRecord(record).build();
            map.put("success", true);
            map.put("content", xform.getContent());
            map.put("jsonData", xform.getJsonData());
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    // ~ ==================================国内特色流程====================================
    /**
     * 回退任务
     *
     * @return
     */
    @RequestMapping("workspace-rollback")
    public String rollback(@RequestParam("taskId") String taskId) {
        Command<Object> cmd = new RollbackTaskCmd(taskId, null);

        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/workspace-listPersonalTasks.do";
    }

    /**
     * 撤销任务
     * 
     * @return
     */

    /*
     * @RequestMapping("workspace-withdraw") public String withdraw(@RequestParam("taskId") String taskId) {
     * Command<Integer> cmd = new WithdrawTaskCmd(taskId);
     * 
     * processEngine.getManagementService().executeCommand(cmd);
     * 
     * return "redirect:/bpm/workspace-listPersonalTasks.do"; }
     */

    /**
     * 准备加减签.
     */
    @RequestMapping("workspace-changeCounterSign")
    public String changeCounterSign() {
        return "bpm/workspace-changeCounterSign";
    }

    /**
     * 进行加减签.
     */
    @RequestMapping("workspace-saveCounterSign")
    public String saveCounterSign(
            @RequestParam("operationType") String operationType,
            @RequestParam("userId") String userId,
            @RequestParam("taskId") String taskId) {
        CounterSignCmd cmd = new CounterSignCmd(operationType, userId, taskId);

        processEngine.getManagementService().executeCommand(cmd);

        return "redirect:/bpm/workspace-listPersonalTasks.do";
    }

    /**
     * 转发已结流程.
     */
    @RequestMapping("workspace-transferProcessInstance")
    public String transferProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId,
            @RequestParam("assignee") String assignee) {
        String tenantId = tenantHolder.getTenantId();

        // 1. 找到历史
        HistoricProcessInstance historicProcessInstance = processEngine
                .getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        // 2. 创建一个任务，设置为未读，转发状态
        HumanTaskDTO humanTaskDto = humanTaskConnector.createHumanTask();
        humanTaskDto.setProcessInstanceId(processInstanceId);
        humanTaskDto.setPresentationSubject(historicProcessInstance.getName());
        humanTaskDto.setAssignee(assignee);
        humanTaskDto.setTenantId(tenantId);
        // TODO: 还没有字段
        // humanTaskDto.setCopyStatus("unread");
        humanTaskDto.setCatalog(HumanTaskConstants.CATALOG_COPY);
        humanTaskDto.setAction("unread");
        humanTaskDto.setBusinessKey(historicProcessInstance.getBusinessKey());
        humanTaskDto.setProcessDefinitionId(historicProcessInstance
                .getProcessDefinitionId());

        try {
            // TODO: 等到流程支持viewFormKey，才能设置。目前做不到
            List<HistoricTaskInstance> historicTaskInstances = processEngine
                    .getHistoryService().createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            HistoricTaskInstance historicTaskInstance = historicTaskInstances
                    .get(0);
            humanTaskDto.setForm(historicTaskInstance.getFormKey());
            humanTaskDto.setName(historicTaskInstance.getName());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        humanTaskConnector.saveHumanTask(humanTaskDto);

        // 3. 把任务分配给对应的人员
        return "redirect:/bpm/workspace-listCompletedProcessInstances.do";
    }

    /**
     * 催办.
     */
    @RequestMapping("workspace-remind")
    public String remind(
            @RequestParam("processInstanceId") String processInstanceId,
            @RequestParam("userId") String userId,
            @RequestParam("comment") String comment,
            HttpServletRequest request) {
        List<HumanTaskDTO> humanTaskDtos = humanTaskConnector
                .findHumanTasksByProcessInstanceId(processInstanceId);
        logger.debug("processInstanceId : {}", processInstanceId);

        logger.debug("humanTaskDtos : {}", humanTaskDtos);

        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            if (humanTaskDto.getCompleteTime() != null) {
                continue;
            }

            String requestURI = request.getRequestURI();
            String requestURL = request.getRequestURL().toString();
            String url = "/operation/task-operation-viewTaskForm.do?humanTaskId="
                    + humanTaskDto.getId();
            String content = "请尽快办理 " + humanTaskDto.getPresentationSubject()
                    + "<p><a href='" + requestURI.substring(0, requestURI.indexOf(request.getPathInfo())) + url + "'>" +
                    requestURL.substring(0, requestURL.indexOf(request.getPathInfo())) + url + "</a></p>";

            List<String> assignees = getAssignees(humanTaskDto);
            for (String assignee : assignees) {
                logger.debug("remind {}", assignee);
                NotificationDTO notificationDto = new NotificationDTO();
                notificationDto.setSender(currentUserHolder.getUserId());
                notificationDto.setReceiver(assignee);
                notificationDto.setReceiverType("userid");
                notificationDto.getTypes().add("msg");
                notificationDto.getTypes().add("email");
                notificationDto.setSubject("请尽快办理 "
                        + humanTaskDto.getPresentationSubject());
                notificationDto.setContent(content);

                notificationConnector.send(notificationDto, "1");
            }
        }

        return "redirect:/bpm/workspace-listRunningProcessInstances.do";
    }

    @Resource
    private TaskParticipantManager taskParticipantManager;

    private List<String> getAssignees(HumanTaskDTO humanTaskDto) {
        List<String> result = new ArrayList<String>();
        String assignee = humanTaskDto.getAssignee();
        if (StringUtils.isBlank(assignee)) {
            List<TaskParticipant> candidates = taskParticipantManager.getByTaskInfoId(
                    Long.parseLong(humanTaskDto.getId()), "candidate"
            );
            for (TaskParticipant temp : candidates) {
                result.add(temp.getRef());
            }
        } else {
            result.add(assignee);
        }
        return result;
    }

    /**
     * 跳过.
     */
    @RequestMapping("workspace-skip")
    public String skip(
            @RequestParam("processInstanceId") String processInstanceId,
            @RequestParam("userId") String userId,
            @RequestParam("comment") String comment) {
        List<HumanTaskDTO> humanTaskDtos = humanTaskConnector
                .findHumanTasksByProcessInstanceId(processInstanceId);
        logger.debug("processInstanceId : {}", processInstanceId);

        logger.debug("humanTaskDtos : {}", humanTaskDtos);

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            if (humanTaskDto.getCompleteTime() != null) {
                continue;
            }

            CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
            currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
            currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

            String humanTaskId = humanTaskDto.getId();
            humanTaskConnector.skip(humanTaskId, currentUserHolder.getUserId(),
                    comment);
        }

        return "redirect:/bpm/workspace-listRunningProcessInstances.do";
    }

    /**
     * 撤销.
     */
    @RequestMapping("workspace-withdraw")
    public String withdraw(
            @RequestParam("processInstanceId") String processInstanceId) {
        logger.debug("processInstanceId : {}", processInstanceId);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String initiator = "";
        String firstUserTaskActivityId = internalProcessConnector
                .findFirstUserTaskActivityId(
                        processInstance.getProcessDefinitionId(), initiator);
        logger.debug("firstUserTaskActivityId : {}", firstUserTaskActivityId);

        List<HistoricTaskInstance> historicTaskInstances = processEngine
                .getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(firstUserTaskActivityId).list();
        HistoricTaskInstance historicTaskInstance = historicTaskInstances
                .get(0);
        String taskId = historicTaskInstance.getId();
        HumanTaskDTO humanTaskDto = humanTaskConnector
                .findHumanTaskByTaskId(taskId);
        String comment = "";
        humanTaskConnector.withdraw(humanTaskDto.getId(), comment);

        return "redirect:/bpm/workspace-listRunningProcessInstances.do";
    }

    // ~ ======================================================================
    @Resource
    public void setBpmCategoryManager(BpmCategoryManager bpmCategoryManager) {
        this.bpmCategoryManager = bpmCategoryManager;
    }

    @Resource
    public void setBpmProcessManager(BpmProcessManager bpmProcessManager) {
        this.bpmProcessManager = bpmProcessManager;
    }

    @Resource
    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Resource
    public void setProcessConnector(ProcessConnector processConnector) {
        this.processConnector = processConnector;
    }


    @Resource
    public void setTraceService(TraceService traceService) {
        this.traceService = traceService;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }

    @Resource
    public void setKeyValueConnector(KeyValueConnector keyValueConnector) {
        this.keyValueConnector = keyValueConnector;
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setNotificationConnector(
            NotificationConnector notificationConnector) {
        this.notificationConnector = notificationConnector;
    }

    @Resource
    public void setInternalProcessConnector(
            InternalProcessConnector internalProcessConnector) {
        this.internalProcessConnector = internalProcessConnector;
    }

    @Value("${application.baseUrl}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
