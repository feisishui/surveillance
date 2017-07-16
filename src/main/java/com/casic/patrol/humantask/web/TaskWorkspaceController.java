package com.casic.patrol.humantask.web;


import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskConstants;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.manager.TaskInfoManager;
import com.casic.patrol.util.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("humantask")
public class TaskWorkspaceController {
    private static Logger logger = LoggerFactory
            .getLogger(TaskWorkspaceController.class);
    private TaskInfoManager taskInfoManager;
    private Exportor exportor;
    private BeanMapper beanMapper = new BeanMapper();
    private JsonMapper jsonMapper = new JsonMapper();
    private MessageHelper messageHelper;
    private JdbcTemplate jdbcTemplate;
    private HumanTaskConnector humanTaskConnector;
    private TenantHolder tenantHolder;
    @Resource
    private ProcessEngine processEngine;

    /**
     * 待办任务.
     */
    @RequestMapping("workspace-personalTasks")
    public String personalTasks(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findPersonalTasks(userId, tenantId,
                page.getPageNo(), page.getPageSize());
        // List<PropertyFilter> propertyFilters = PropertyFilter
        // .buildFromMap(parameterMap);
        // propertyFilters.add(new PropertyFilter("EQS_status", "active"));
        // propertyFilters.add(new PropertyFilter("EQS_assignee", userId));
        // page = taskInfoManager.pagedQuery(page, propertyFilters);
        model.addAttribute("page", page);

        return "humantask/workspace-personalTasks";
    }

    /**
     * 待领任务.
     */
    @RequestMapping("workspace-groupTasks")
    public String groupTasks(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();

        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findGroupTasks(userId, tenantId,
                page.getPageNo(), page.getPageSize());
        model.addAttribute("page", page);

        return "humantask/workspace-groupTasks";
    }

    /**
     * 已办任务.
     */
    @RequestMapping("workspace-historyTasks")
    public String historyTasks(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model)
    {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findFinishedTasks(userId, tenantId,
                page.getPageNo(), page.getPageSize());
        model.addAttribute("page", page);

        return "bpm/workspace-historyTasks";
    }

    /**
     * 历史流程.
     */
    @RequestMapping("workspace-historyInstances")
    public String historyInstances(@ModelAttribute Page page, Model model) {
        String tenantId = tenantHolder.getTenantId();

        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).finished()
                .count();
        List<HistoricProcessInstance> historicProcessInstances = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).finished()
                .listPage((int) page.getStart(), page.getPageSize());

        page.setResult(historicProcessInstances);
        page.setTotalCount(count);

        model.addAttribute("page", page);
        return "bpm/workspace-historyInstances";
    }

    /**
     * 代理中的任务.
     */
    @RequestMapping("workspace-delegatedTasks")
    public String delegatedTasks(@ModelAttribute Page page,
            @RequestParam Map<String, Object> parameterMap, Model model) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findDelegateTasks(userId, tenantId,
                page.getPageNo(), page.getPageSize());
        model.addAttribute("page", page);

        return "humantask/workspace-delegatedTasks";
    }

    /**
     * 领取.
     */
    @Autowired
    private HttpSession session;

    @RequestMapping("workspace-claimTask")
    public String claimTask(@RequestParam("taskId") Long taskId) {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        TaskInfo taskInfo = taskInfoManager.get(taskId);
        taskInfo.setAssignee(userId);
        taskInfoManager.save(taskInfo);

        return "redirect:/humantask/workspace-personalTasks.do";
    }

    /**
     * 转发已结流程.
     */
    @RequestMapping("workspace-transferTask")
    public String transferTask(@RequestParam("humanTaskId") String humanTaskId,
            @RequestParam("assignee") String assignee) {
        String tenantId = tenantHolder.getTenantId();

        // 1. 找到任务
        HumanTaskDTO historyHumanTask = humanTaskConnector
                .findHumanTask(humanTaskId);

        // 2. 创建一个任务，设置为未读，转发状态
        HumanTaskDTO humanTaskDto = humanTaskConnector.createHumanTask();
        humanTaskDto.setProcessInstanceId(historyHumanTask
                .getProcessInstanceId());
        humanTaskDto.setPresentationSubject(historyHumanTask
                .getPresentationSubject());
        humanTaskDto.setAssignee(assignee);
        humanTaskDto.setTenantId(tenantId);
        humanTaskDto.setParentId(historyHumanTask.getId());
        // TODO: 还没有字段
        // humanTaskDto.setCopyStatus("unread");
        humanTaskDto.setCatalog(HumanTaskConstants.CATALOG_COPY);
        humanTaskDto.setAction("unread");
        humanTaskDto.setBusinessKey(historyHumanTask.getBusinessKey());
        humanTaskDto.setProcessDefinitionId(historyHumanTask
                .getProcessDefinitionId());

        try {
            // TODO: 等到流程支持viewFormKey，才能设置。目前做不到
            humanTaskDto.setForm(historyHumanTask.getForm());
            humanTaskDto.setName(historyHumanTask.getName());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        humanTaskConnector.saveHumanTask(humanTaskDto);

        // 3. 把任务分配给对应的人员
        return "redirect:/humantask/workspace-historyTasks.do#";
    }

    // ~ ======================================================================
    @Resource
    public void setTaskInfoManager(TaskInfoManager taskInfoManager) {
        this.taskInfoManager = taskInfoManager;
    }

    @Resource
    public void setExportor(Exportor exportor) {
        this.exportor = exportor;
    }

    @Resource
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }


    @Resource
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
