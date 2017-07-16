package com.casic.patrol.bpm.web;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.keyvalue.FormParameter;
import com.casic.patrol.api.keyvalue.KeyValueConnector;
import com.casic.patrol.api.keyvalue.Record;
import com.casic.patrol.api.keyvalue.RecordBuilder;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.process.ProcessDTO;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.persistence.domain.BpmProcess;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.util.BaseDTO;
import com.casic.patrol.operation.service.OperationService;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.manager.UserManager;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("bpm")
public class AndroidBpm {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidBpm.class);
    public static final int STATUS_RUNNING = 2;
    private JsonMapper jsonMapper = new JsonMapper();
    private BpmProcessManager bpmProcessManager;
    private KeyValueConnector keyValueConnector;
    private ProcessConnector processConnector;
    private StoreConnector storeConnector;
    private UserConnector userConnector;
    private OperationService operationService;
    @Resource
    private UserManager userManager;
    @Resource
    private PartyEntityManager partyEntityManager;
    @Resource
    private PartyStructManager partyStructManager;

    @RequestMapping("startProcess")
    @ResponseBody
    public BaseDTO startProcess1(
                                 @RequestParam(value = "userId",required = false) String userId,
                                 @RequestParam(value = "processDefinitionId",required = false) String processDefinitionId,
                                 @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
                                 @RequestParam(value = "videoFile",required = false) MultipartFile videoFile,
                                 @RequestParam(value = "data",required = false) String data) throws Exception {
        logger.info("start : {} {}", processDefinitionId, data);

        String tenantId = "1";
        String hql = "from BpmProcess where bpmConfBase.processDefinitionId=? order by priority";
        BpmProcess bpmProcess = bpmProcessManager.findUnique(hql,
                processDefinitionId);

        Map<String, Object> map = jsonMapper.fromJson(data, Map.class);

        map.put("bpmProcessId", Long.toString(bpmProcess.getId()));

        FormParameter formParameter = this.doSaveRecord(map, userId, tenantId,imageFile,videoFile);

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
            String tenantId,MultipartFile imageFile,MultipartFile videoFile) throws Exception {
        FormParameter formParameter = new FormParameter();
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        MultiValueMap<String, MultipartFile> multiFileMap = new LinkedMultiValueMap<String, MultipartFile>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("uploadPic")||entry.getKey().equalsIgnoreCase("uploadMv"))
                continue;
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        if (imageFile!=null)
        {
            List<MultipartFile> imageFileList = new ArrayList<MultipartFile>();
            imageFileList.add(imageFile);
            multiFileMap.put("uploadPic", imageFileList);
        }
        if (videoFile!=null)
        {
            List<MultipartFile> videoFileList = new ArrayList<MultipartFile>();
            videoFileList.add(videoFile);
            multiFileMap.put("uploadMv",videoFileList);
        }
        formParameter.setMultiValueMap(multiValueMap);
        formParameter.setMultiFileMap(multiFileMap);

        formParameter.setBpmProcessId((String) map.get("bpmProcessId"));

        String businessKey = operationService.saveDraft(userId, tenantId,
                formParameter);

        if ((formParameter.getBusinessKey() == null)
                || "".equals(formParameter.getBusinessKey().trim())) {
            formParameter.setBusinessKey(businessKey);
        }

        Record record = keyValueConnector.findByCode(businessKey);

        record = new RecordBuilder().build(record, multiValueMap,multiFileMap,storeConnector, tenantId);

        keyValueConnector.save(record);

        return formParameter;
    }

    @RequestMapping("weiXin-startProcess")
    @ResponseBody
    public BaseDTO weiXinStartProcess(
            @RequestParam(value = "userId",required = false) String userId,
            @RequestParam(value = "processDefinitionId",required = false) String processDefinitionId,
            @RequestParam(value = "imageFile",required = false) MultipartFile imageFile,
            @RequestParam(value = "videoFile",required = false) MultipartFile videoFile,
            @RequestParam(value = "data",required = false) String data) throws Exception {
        logger.info("start : {} {}", processDefinitionId, data);

        String tenantId = "1";
        String hql = "from BpmProcess where bpmConfBase.processDefinitionId=? order by priority";
        BpmProcess bpmProcess = bpmProcessManager.findUnique(hql,
                processDefinitionId);

        Map<String, Object> map = jsonMapper.fromJson(data, Map.class);

        map.put("bpmProcessId", Long.toString(bpmProcess.getId()));

        map.put("commandCenter",getCommandCenterIds());
        FormParameter formParameter = this.doSaveRecord(map, userId, tenantId,imageFile,videoFile);

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

    public String getCommandCenterIds()
    {
        String ids = "";
        PartyEntity partyEntity = partyEntityManager.getPartyEntityByName("指挥中心坐席");
        List<PartyEntity> partyEntities = partyStructManager.getChilePartiesByParent(partyEntity);
//        List<User> users = userManager.getPatrolers(role);
        List<User> users = userManager.getUserByParties(partyEntities);
        for (User user : users) {
            ids=ids+user.getId().toString()+",";
        }
        ids = ids.substring(0,ids.length()-1);
        return ids;
    }

    @Resource
    public void setBpmProcessManager(BpmProcessManager bpmProcessManager) {
        this.bpmProcessManager = bpmProcessManager;
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
