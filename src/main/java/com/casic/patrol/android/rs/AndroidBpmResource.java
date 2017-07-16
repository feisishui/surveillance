package com.casic.patrol.android.rs;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.keyvalue.FormParameter;
import com.casic.patrol.api.keyvalue.KeyValueConnector;
import com.casic.patrol.api.keyvalue.Record;
import com.casic.patrol.api.keyvalue.RecordBuilder;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.process.ProcessDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.persistence.domain.BpmProcess;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.operation.service.OperationService;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("android/bpm")
public class AndroidBpmResource {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidBpmResource.class);
    public static final int STATUS_DRAFT_PROCESS = 0;
    public static final int STATUS_DRAFT_TASK = 1;
    public static final int STATUS_RUNNING = 2;
    private JsonMapper jsonMapper = new JsonMapper();
    private ProcessEngine processEngine;
    private BpmProcessManager bpmProcessManager;
    private TenantHolder tenantHolder;
    private KeyValueConnector keyValueConnector;
    private ProcessConnector processConnector;
    private StoreConnector storeConnector;
    private UserConnector userConnector;
    private OperationService operationService;

    @POST
    @Path("processDefinitions")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO processDefinitions() throws Exception {
        logger.info("start");

        String hql = "from BpmProcess where tenantId=? order by priority";
        List<BpmProcess> bpmProcesses = bpmProcessManager.find(hql, "1");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (BpmProcess bpmProcess : bpmProcesses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", bpmProcess.getName());
            map.put("processDefinitionId", bpmProcess.getBpmConfBase()
                    .getProcessDefinitionId());
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
    @Path("processInstances")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO processInstances(@FormParam("userId") String userId)
            throws Exception {
        return this.processInstancesRunning(userId);
    }

    @POST
    @Path("processInstancesRunning")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO processInstancesRunning(
            @FormParam("userId") String userId) throws Exception {
        logger.info("start");

        String tenantId = "1";
        List<HistoricProcessInstance> historicProcessInstances = processEngine
                .getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).startedBy(userId)
                .unfinished().list();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", historicProcessInstance.getName());
            map.put("processInstanceId", historicProcessInstance.getId());
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
    @Path("processInstancesComplete")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO processInstancesComplete(
            @FormParam("userId") String userId) throws Exception {
        logger.info("start");

        String tenantId = "1";
        List<HistoricProcessInstance> historicProcessInstances = processEngine
                .getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceTenantId(tenantId).startedBy(userId).finished()
                .list();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", historicProcessInstance.getName());
            map.put("processInstanceId", historicProcessInstance.getId());
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
    @Path("processInstancesDraft")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO processInstancesDraft(
            @FormParam("userId") String userId) throws Exception {
        logger.info("start");
        String tenantId = "1";

        List<Record> records = keyValueConnector.findByStatus(
                STATUS_DRAFT_PROCESS, userId, tenantId);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (Record record : records) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", record.getName());
            map.put("formTemplateCode", record.getFormTemplateCode());
            map.put("code", record.getCode());
            map.put("category", record.getCategory());
            map.put("status", record.getStatus());
            map.put("ref", record.getRef());
            map.put("createTime", record.getCreateTime());
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
     * 发起流程
     * @param userId
     * @param processDefinitionId
     * @param data
     * @return
     * @throws Exception
     */
    @POST
    @Path("startProcess")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseDTO startProcess( @FormParam("userId") String userId,
            @FormParam("processDefinitionId") String processDefinitionId,
            @FormParam("data") String data) throws Exception {
        logger.info("start : {} {}", processDefinitionId, data);

        String tenantId = "1";
        String hql = "from BpmProcess where bpmConfBase.processDefinitionId=? order by priority";
        BpmProcess bpmProcess = bpmProcessManager.findUnique(hql,
                processDefinitionId);

        Map<String, Object> map = jsonMapper.fromJson(data, Map.class);
        map.put("bpmProcessId", Long.toString(bpmProcess.getId()));

        FormParameter formParameter = this.doSaveRecord(map, userId, tenantId);

        // doConfirmStartProcess(formParameter, model);
        Record record = keyValueConnector.findByCode(formParameter
                .getBusinessKey());
        ProcessDTO processDto = processConnector.findProcess(formParameter
                .getBpmProcessId());

        // String processDefinitionId = processDto.getProcessDefinitionId();

        // 获得form的信息
        FormDTO formDto = processConnector.findStartForm(processDefinitionId);

        Xform xform = new XformBuilder().setStoreConnector(storeConnector).setUserConnector(userConnector)
                .setContent(formDto.getContent()).setRecord(record).build();
        Map<String, Object> processParameters = xform.getMapData();
        logger.info("processParameters : {}", processParameters);

        String processInstanceId = processConnector.startProcess(userId,
                formParameter.getBusinessKey(), processDefinitionId,
                processParameters);

        record = new RecordBuilder().build(record, STATUS_RUNNING,
                processInstanceId);
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
        formParameter.setBpmProcessId((String) map.get("bpmProcessId"));

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

    // ~ ======================================================================
    @Resource
    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    @Resource
    public void setBpmProcessManager(BpmProcessManager bpmProcessManager) {
        this.bpmProcessManager = bpmProcessManager;
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

    @Resource
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }
}
