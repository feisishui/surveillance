package com.casic.patrol.operation.web;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.keyvalue.*;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.process.ProcessDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.persistence.domain.BpmProcess;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.button.ButtonDTO;
import com.casic.patrol.button.ButtonHelper;
import com.casic.patrol.core.MultipartHandler;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.operation.service.OperationService;
import com.casic.patrol.util.StringUtils;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 流程操作.
 * 
 * @author Lingo
 */
@Controller
@RequestMapping("/")
public class VisitorProcessOperationController {
    private static Logger logger = LoggerFactory
            .getLogger(VisitorProcessOperationController.class);
    public final static String BPM_CODE = "surveillance";
    private OperationService operationService;
    private KeyValueConnector keyValueConnector;
    private ProcessConnector processConnector;
    private HumanTaskConnector humanTaskConnector;
    private MultipartResolver multipartResolver;
    private StoreConnector storeConnector;
    private ButtonHelper buttonHelper = new ButtonHelper();
    private FormConnector formConnector;
    private JsonMapper jsonMapper = new JsonMapper();
    private TenantHolder tenantHolder;
    private UserConnector userConnector;
    @Resource
    private BpmProcessManager bpmProcessManager;

    /**
     * 显示启动流程的表单 for 游客.
     */
    @RequestMapping("visitor")
    public String viewStartFormForVisitor(Model model) throws Exception {
        String bpmProcessId = getBpmProcessId(BPM_CODE);
        String tenantId = tenantHolder.getTenantId();
        FormParameter formParameter = new FormParameter();
        formParameter.setBpmProcessId(bpmProcessId);

        ProcessDTO processDto = processConnector.findProcess(bpmProcessId);
        String processDefinitionId = processDto.getProcessDefinitionId();
        FormDTO formDto = this.processConnector
                .findStartForm(processDefinitionId);
        formParameter.setFormDto(formDto);

        if (formDto.isExists()) {
            // 如果找到了form，就显示表单
            if (processDto.isConfigTask()) {
                throw new RuntimeException("不支持配置负责人");
            } else {
                formParameter.setNextStep("confirmStartProcess");
            }
            return this.doViewStartForm(formParameter, model, tenantId);
        }
        throw new RuntimeException("未找到流程[" +
                processDefinitionId + "]开始对应的表单");
    }

    private String getBpmProcessId(String code) {
        String hql = "from BpmProcess where code=? order by priority";
        List<BpmProcess> bpmProcesses = bpmProcessManager.find(hql, code);
        if (bpmProcesses.size() > 0) {
            return bpmProcesses.get(0).getId().toString();
        }
        return new Integer(-1).toString();
    }

    /**
     * 确认发起流程.
     */
    @RequestMapping("confirmStartProcess")
    public String confirmStartProcess(HttpServletRequest request, Model model)
            throws Exception {
        FormParameter formParameter = this.doSaveRecord(request);
        formParameter.setNextStep("startProcessInstance");
        this.doConfirmStartProcess(formParameter, model);

        return "visitor/confirmStartProcess";
    }

    /**
     * 发起流程.
     */
    @RequestMapping("startProcessInstance")
    public String startProcessInstance(HttpServletRequest request, Model model) {
        try {
            FormParameter formParameter = this.doSaveRecord(request);
            this.doConfirmStartProcess(formParameter, model);

            Record record = keyValueConnector.findByCode(formParameter
                    .getBusinessKey());
            ProcessDTO processDto = processConnector.findProcess(formParameter
                    .getBpmProcessId());
            String processDefinitionId = processDto.getProcessDefinitionId();

            // 获得form的信息
            FormDTO formDto = processConnector.findStartForm(processDefinitionId);

            Xform xform = new XformBuilder().setStoreConnector(storeConnector)
                    .setUserConnector(userConnector)
                    .setContent(formDto.getContent()).setRecord(record).build();
            Map<String, Object> processParameters = xform.getMapData();
            logger.info("processParameters : {}", processParameters);

            // String processInstanceId = processConnector.startProcess(
            // currentUserHolder.getUserId(), formParameter.getBusinessKey(),
            // processDefinitionId, processParameters);
            // record = new RecordBuilder().build(record, STATUS_RUNNING,
            // processInstanceId);
            // keyValueConnector.save(record);
            CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
            currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
            currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

            String userId = currentUserHolder.getUserId();
            String businessKey = formParameter.getBusinessKey();
            this.operationService.startProcessInstance(userId, businessKey,
                    processDefinitionId, processParameters, record);
            model.addAttribute("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
        }
        return "visitor/startProcessInstance";
    }

    // ~ ======================================================================
    /**
     * 通过multipart请求构建formParameter.
     */
    public FormParameter buildFormParameter(MultipartHandler multipartHandler) {
        FormParameter formParameter = new FormParameter();
        formParameter.setMultiValueMap(multipartHandler.getMultiValueMap());
        formParameter.setMultiFileMap(multipartHandler.getMultiFileMap());
        formParameter.setBusinessKey(multipartHandler.getMultiValueMap()
                .getFirst("businessKey"));
        formParameter.setBpmProcessId(multipartHandler.getMultiValueMap()
                .getFirst("bpmProcessId"));
        formParameter.setHumanTaskId(multipartHandler.getMultiValueMap()
                .getFirst("humanTaskId"));

        return formParameter;
    }

    /**
     * 把数据先保存到keyvalue里.
     */
    @Autowired
    private HttpSession session;
    public FormParameter doSaveRecord(HttpServletRequest request)
            throws Exception {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();

        MultipartHandler multipartHandler = new MultipartHandler(
                multipartResolver);
        FormParameter formParameter = null;

        try {
            multipartHandler.handle(request);
            logger.debug("multiValueMap : {}",
                    multipartHandler.getMultiValueMap());
            logger.debug("multiFileMap : {}",
                    multipartHandler.getMultiFileMap());

            formParameter = this.buildFormParameter(multipartHandler);

            String businessKey = operationService.saveDraft(userId, tenantId,
                    formParameter);

            if ((formParameter.getBusinessKey() == null)
                    || "".equals(formParameter.getBusinessKey().trim())) {
                formParameter.setBusinessKey(businessKey);
            }

            Record record = keyValueConnector.findByCode(businessKey);

            record = new RecordBuilder().build(record, multipartHandler,
                    storeConnector, tenantId);

            keyValueConnector.save(record);
        } finally {
            multipartHandler.clear();
        }

        return formParameter;
    }

    /**
     * 实际确认发起流程.
     */
    public String doConfirmStartProcess(FormParameter formParameter, Model model) {
        humanTaskConnector.configTaskDefinitions(
                formParameter.getBusinessKey(),
                formParameter.getList("taskDefinitionKeys"),
                formParameter.getList("taskAssignees"));

        model.addAttribute("businessKey", formParameter.getBusinessKey());
        model.addAttribute("nextStep", formParameter.getNextStep());
        model.addAttribute("bpmProcessId", formParameter.getBpmProcessId());

        return "operation/process-operation-confirmStartProcess";
    }

    /**
     * 实际显示开始表单.
     */
    public String doViewStartForm(FormParameter formParameter, Model model,
            String tenantId) throws Exception {
        model.addAttribute("formDto", formParameter.getFormDto());
        model.addAttribute("bpmProcessId", formParameter.getBpmProcessId());
        model.addAttribute("businessKey", formParameter.getBusinessKey());
        model.addAttribute("nextStep", formParameter.getNextStep());

        List<ButtonDTO> buttons = new ArrayList<ButtonDTO>();
        buttons.add(buttonHelper.findButton(formParameter.getNextStep()));
        model.addAttribute("buttons", buttons);

        String json = this.findStartFormData(formParameter.getBusinessKey());
        if (json != null) {
            model.addAttribute("json", json);
        }

        Record record = keyValueConnector.findByCode(formParameter
                .getBusinessKey());
        FormDTO formDto = formConnector.findForm(formParameter.getFormDto()
                .getCode(), tenantId);
        model.addAttribute("eventName", formDto.getName());

        if (record != null) {
            Xform xform = new XformBuilder().setStoreConnector(storeConnector)
                    .setUserConnector(userConnector)
                    .setContent(formDto.getContent()).setRecord(record).build();
            model.addAttribute("xform", xform);
        } else {
            Xform xform = new XformBuilder().setStoreConnector(storeConnector)
                    .setUserConnector(userConnector)
                    .setContent(formDto.getContent()).build();
            model.addAttribute("xform", xform);
        }

        return "visitor/viewStartForm";
    }

    /**
     * 读取草稿箱中的表单数据，转换成json.
     */
    public String findStartFormData(String businessKey) throws Exception {
        Record record = keyValueConnector.findByCode(businessKey);

        if (record == null) {
            return null;
        }

        Map map = new HashMap();

        for (Prop prop : record.getProps().values()) {
            map.put(prop.getCode(), prop.getValue());
        }

        String json = jsonMapper.toJson(map);

        return json;
    }

    // ~ ======================================================================
    @Resource
    public void setKeyValueConnector(KeyValueConnector keyValueConnector) {
        this.keyValueConnector = keyValueConnector;
    }

    @Resource
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Resource
    public void setProcessConnector(ProcessConnector processConnector) {
        this.processConnector = processConnector;
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setMultipartResolver(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    @Resource
    public void setStoreConnector(StoreConnector storeConnector) {
        this.storeConnector = storeConnector;
    }

    @Resource
    public void setFormConnector(FormConnector formConnector) {
        this.formConnector = formConnector;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }

    @Resource
    public void setUserConnector(UserConnector userConnector) {
        this.userConnector = userConnector;
    }
}
