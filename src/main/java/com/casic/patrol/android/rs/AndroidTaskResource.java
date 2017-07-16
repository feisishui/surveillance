package com.casic.patrol.android.rs;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.keyvalue.FormParameter;
import com.casic.patrol.api.keyvalue.KeyValueConnector;
import com.casic.patrol.api.keyvalue.Record;
import com.casic.patrol.api.keyvalue.RecordBuilder;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.bpm.support.ActivitiVariableDTO;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.operation.service.OperationService;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("android/task")
public class AndroidTaskResource {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidTaskResource.class);
    public static final int STATUS_DRAFT_PROCESS = 0;
    public static final int STATUS_DRAFT_TASK = 1;
    public static final int STATUS_RUNNING = 2;
    private JsonMapper jsonMapper = new JsonMapper();
    private ProcessEngine processEngine;
    private ProcessConnector processConnector;
    private BpmProcessManager bpmProcessManager;
    private TenantHolder tenantHolder;
    private HumanTaskConnector humanTaskConnector;
    private OperationService operationService;
    private KeyValueConnector keyValueConnector;
    private StoreConnector storeConnector;
    private UserConnector userConnector;

    /**
     * 需要调用
     * @param userId
     * @return
     * @throws Exception
     */
    @POST
    @Path("tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO tasks(@FormParam("userId") String userId,
                         @FormParam("lastId") String lastId)
            throws Exception {
        String tenantId = "1";
        List<HumanTaskDTO> humanTaskDtos= humanTaskConnector.findPersonalTasks(userId, tenantId, lastId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("processInstanceId",
                    humanTaskDto.getProcessInstanceId());
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        BaseDTO result = null;
        try {
            String json = jsonMapper.toJson(list);
            result = new BaseDTO();
            result.setCode(200);
            result.setData(json);
            logger.info("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @POST
    @Path("get-task-number")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO getTaskNumber(@FormParam("userId") String userId)
            throws Exception {
        String tenantId = "1";
        String number= humanTaskConnector.getTaskNumber(userId, tenantId);
        BaseDTO result = null;
        try {
            result = new BaseDTO();
            result.setCode(200);
            result.setData(number);
            logger.info("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @POST
    @Path("finishedTasks")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO finishedTasks(@FormParam("userId") String userId,
                         @FormParam("lastId") String lastId)
            throws Exception {
        String tenantId = "1";
        List<HumanTaskDTO> humanTaskDtos= humanTaskConnector.findFinishedPersonalTasks(userId, tenantId, lastId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("completeTime",
                    dateFormat.format(humanTaskDto.getCompleteTime()));
            map.put("processInstanceId",
                    humanTaskDto.getProcessInstanceId());
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        BaseDTO result = null;
        try {
            String json = jsonMapper.toJson(list);
            result = new BaseDTO();
            result.setCode(200);
            result.setData(json);
            logger.info("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @POST
    @Path("tasksPersonal")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO tasksPersonal(@FormParam("userId") String userId)
            throws Exception {
        logger.info("start");
        String tenantId = "1";
        Page page = humanTaskConnector.findPersonalTasks(userId, tenantId, 1,
                10);
        List<HumanTaskDTO> humanTaskDtos = (List<HumanTaskDTO>) page
                .getResult();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        String json = jsonMapper.toJson(list);
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(json);
        logger.info("end");

        return result;
    }

    @POST
    @Path("tasksGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO tasksGroup(@FormParam("userId") String userId)
            throws Exception {
        logger.info("start");
        String tenantId = "1";
        Page page = humanTaskConnector.findGroupTasks(userId, tenantId, 1, 10);
        List<HumanTaskDTO> humanTaskDtos = (List<HumanTaskDTO>) page
                .getResult();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        String json = jsonMapper.toJson(list);
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(json);
        logger.info("end");

        return result;
    }


    @POST
    @Path("tasksComplete")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO tasksComplete(@FormParam("userId") String userId)
            throws Exception {
        logger.info("start");

        String tenantId = "1";
        Page page = humanTaskConnector.findFinishedTasks(userId, tenantId, 1,
                10);
        List<HumanTaskDTO> humanTaskDtos = (List<HumanTaskDTO>) page
                .getResult();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        String json = jsonMapper.toJson(list);
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(json);
        logger.info("end");

        return result;
    }

    @POST
    @Path("tasksDelegate")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO tasksDelegate(@FormParam("userId") String userId)
            throws Exception {
        logger.info("start");
        String tenantId = "1";
        Page page = humanTaskConnector.findDelegateTasks(userId, tenantId, 1,
                10);
        List<HumanTaskDTO> humanTaskDtos = (List<HumanTaskDTO>) page
                .getResult();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", humanTaskDto.getId());
            map.put("name", humanTaskDto.getName());
            map.put("presentationSubject",
                    humanTaskDto.getPresentationSubject());
            map.put("createTime",
                    dateFormat.format(humanTaskDto.getCreateTime()));
            map.put("assignee", humanTaskDto.getAssignee());
            map.put("assigneeDisplayName", userConnector.findById(userId)
                    .getDisplayName());
            list.add(map);
        }

        String json = jsonMapper.toJson(list);
        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(json);
        logger.info("end");

        return result;
    }

    /***
     * 完成任务的操作
     * @param userId
     * @param taskId
     * @param data
     * @return
     * @throws Exception
     */
    @POST
    @Path("completeTask")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO completeTask(@FormParam("userId") String userId,
            @FormParam("taskId") String taskId, @FormParam("data") String data)
            throws Exception {
        logger.info("start : {} {}", taskId, data);

        String humanTaskId = taskId;

        String tenantId = "1";

        Map<String, Object> map = jsonMapper.fromJson(data, Map.class);
        map.put("taskId", humanTaskId);

        Record record = null;
        FormParameter formParameter = null;
        HumanTaskDTO humanTaskDto = null;
        FormDTO formDto = null;

        formParameter = doSaveRecord(map, userId, tenantId);

        humanTaskId = formParameter.getHumanTaskId();
        operationService.saveDraft(userId, tenantId, formParameter);

        formDto = humanTaskConnector.findTaskForm(humanTaskId);

        humanTaskDto = humanTaskConnector.findHumanTask(humanTaskId);

        String processInstanceId = humanTaskDto.getProcessInstanceId();
        record = keyValueConnector.findByRef(processInstanceId);

        record = new RecordBuilder().build(record,
                formParameter.getMultiValueMap(), tenantId);

        keyValueConnector.save(record);

        Xform xform = new XformBuilder().setStoreConnector(storeConnector).setUserConnector(userConnector)
                .setContent(formDto.getContent()).setRecord(record).build();
        Map<String, Object> taskParameters = xform.getMapData();
        logger.info("taskParameters : {}", taskParameters);

        String comment = "";
        String action = "";

        try {
            humanTaskConnector.completeTask(humanTaskId, userId, action,
                    comment, taskParameters);
        } catch (IllegalStateException ex) {
            logger.error(ex.getMessage(), ex);

            return null;
        }

        if (record == null) {
            record = new Record();
        }

        record = new RecordBuilder().build(record, STATUS_RUNNING,
                humanTaskDto.getProcessInstanceId());
        keyValueConnector.save(record);

        BaseDTO result = new BaseDTO();
        result.setCode(200);
        result.setData(data);
        logger.info("end");

        return result;
    }

    /**
     * 把数据先保存到keyvalue里.
     */
    public FormParameter doSaveRecord(Map<String, Object> map, String userId,
            String tenantId) throws Exception {
        FormParameter formParameter = new FormParameter();
        MultiValueMap multiValueMap = new LinkedMultiValueMap();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }

        formParameter.setMultiValueMap(multiValueMap);
        formParameter.setHumanTaskId((String) map.get("taskId"));

        String businessKey = operationService.saveDraft(userId, tenantId,
                formParameter);

        if ((formParameter.getBusinessKey() == null)
                || "".equals(formParameter.getBusinessKey().trim())) {
            formParameter.setBusinessKey(businessKey);
        }

        Record record = keyValueConnector.findByCode(businessKey);

        record = new RecordBuilder().build(record, multiValueMap, tenantId);

        keyValueConnector.save(record);

        return formParameter;
    }

    @POST
    @Path("get-location")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO getProcessLocation(
            @FormParam("processInstanceId") String processInstanceId)
            throws Exception {
        BaseDTO result = null;
        String value = null;
        try {

            HistoryService historyService = processEngine.getHistoryService();
            List<HistoricVariableInstance> historicVariableInstances = historyService
                    .createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            for (HistoricVariableInstance historicVariableInstance:historicVariableInstances)
            {
                if(historicVariableInstance.getVariableName().equalsIgnoreCase("position"))
                {
                    value = historicVariableInstance.getValue().toString();
                    break;
                }
            }
            result = new BaseDTO();
            result.setCode(200);
            result.setData(value);
            logger.info("end");
        } catch (Exception e) {
            result.setCode(400);
            result.setMessage("获取位置失败");
            e.printStackTrace();
        }
        return result;
    }


    // ~ ======================================================================
    @Resource
    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Resource
    public void setKeyValueConnector(KeyValueConnector keyValueConnector) {
        this.keyValueConnector = keyValueConnector;
    }
    @Resource
    public void setProcessConnector(ProcessConnector processConnector) {
        this.processConnector = processConnector;
    }

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }
}
