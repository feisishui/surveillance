package com.casic.patrol.operation.web;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskDefinition;
import com.casic.patrol.api.keyvalue.*;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.process.ProcessDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.button.ButtonDTO;
import com.casic.patrol.button.ButtonHelper;
import com.casic.patrol.core.MultipartHandler;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.operation.service.OperationService;
import com.casic.patrol.util.StringUtils;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程操作.
 * 
 * @author Lingo
 */
@Controller
@RequestMapping("operation")
public class ProcessOperationController {
    private static Logger logger = LoggerFactory
            .getLogger(ProcessOperationController.class);
    public static final int STATUS_DRAFT_PROCESS = 0;
    public static final int STATUS_DRAFT_TASK = 1;
    public static final int STATUS_RUNNING = 2;
    private OperationService operationService;
    private KeyValueConnector keyValueConnector;
    private MessageHelper messageHelper;
    private ProcessConnector processConnector;
    private HumanTaskConnector humanTaskConnector;
    private MultipartResolver multipartResolver;
    private StoreConnector storeConnector;
    private ButtonHelper buttonHelper = new ButtonHelper();
    private FormConnector formConnector;
    private JsonMapper jsonMapper = new JsonMapper();
    private TenantHolder tenantHolder;
    private UserConnector userConnector;

    /**
     * 保存草稿.
     */
    @RequestMapping("process-operation-saveDraft")
    public String saveDraft(HttpServletRequest request) throws Exception {
        this.doSaveRecord(request);

        return "operation/process-operation-saveDraft";
    }

    /**
     * 列出所有草稿.
     */
    @RequestMapping("process-operation-listDrafts")
    public String listDrafts(@ModelAttribute Page page, Model model)
            throws Exception {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = keyValueConnector.pagedQuery(page, STATUS_DRAFT_PROCESS, userId,
                tenantId);
        model.addAttribute("page", page);

        return "operation/process-operation-listDrafts";
    }

    /**
     * 删除草稿.
     */
    @RequestMapping("process-operation-removeDraft")
    public String removeDraft(@RequestParam("code") String code) {
        keyValueConnector.removeByCode(code);

        return "redirect:/operation/process-operation-listDrafts.do";
    }

    /**
     * 显示启动流程的表单.
     */
    @RequestMapping("process-operation-viewStartForm")
    public String viewStartForm(
            HttpServletRequest request,
            @RequestParam("bpmProcessId") String bpmProcessId,
            @RequestParam(value = "businessKey", required = false) String businessKey,
            Model model) throws Exception {
        String tenantId = tenantHolder.getTenantId();
        FormParameter formParameter = new FormParameter();
        formParameter.setBpmProcessId(bpmProcessId);
        formParameter.setBusinessKey(businessKey);

        ProcessDTO processDto = processConnector.findProcess(bpmProcessId);

        String processDefinitionId = processDto.getProcessDefinitionId();

        FormDTO formDto = this.processConnector
                .findStartForm(processDefinitionId);
        formParameter.setFormDto(formDto);

        if (formDto.isExists()) {
            if (formDto.isRedirect()) {
                // 如果是外部表单，就直接跳转出去
                String redirectUrl = formDto.getUrl() + "?processDefinitionId="
                        + formDto.getProcessDefinitionId();

                return "redirect:" + redirectUrl;
            }

            // 如果找到了form，就显示表单
            if (processDto.isConfigTask()) {
                // 如果需要配置负责人
                formParameter.setNextStep("taskConf");
            } else {
                formParameter.setNextStep("confirmStartProcess");
            }

            return this.doViewStartForm(formParameter, model, tenantId);
        } else if (processDto.isConfigTask()) {
            formParameter.setProcessDefinitionId(processDefinitionId);

            // 如果没找到form，就判断是否配置负责人
            return this.doTaskConf(formParameter, model);
        } else {
            // 如果也不需要配置任务，就直接进入确认发起流程
            return this.doConfirmStartProcess(formParameter, model);
        }
    }

    /**
     * 配置每个任务的参与人.
     * 
     * 如果是执行taskConf，可能是填写表单，也可能是直接进入taskConf。
     */
    @RequestMapping("process-operation-taskConf")
    public String taskConf(HttpServletRequest request, Model model)
            throws Exception {
        FormParameter formParameter = this.doSaveRecord(request);

        ProcessDTO processDto = processConnector.findProcess(formParameter
                .getBpmProcessId());
        String processDefinitionId = processDto.getProcessDefinitionId();
        formParameter.setProcessDefinitionId(processDefinitionId);

        if (processDto.isConfigTask()) {
            // 如果需要配置负责人
            formParameter.setNextStep("confirmStartProcess");

            return this.doTaskConf(formParameter, model);
        } else {
            // 如果不需要配置负责人，就进入确认发起流程的页面
            return this.doConfirmStartProcess(formParameter, model);
        }
    }

    /**
     * 确认发起流程.
     */
    @RequestMapping("process-operation-confirmStartProcess")
    public String confirmStartProcess(HttpServletRequest request, Model model)
            throws Exception {
        FormParameter formParameter = this.doSaveRecord(request);
        formParameter.setNextStep("startProcessInstance");
        this.doConfirmStartProcess(formParameter, model);

        return "operation/process-operation-confirmStartProcess";
    }

    /**
     * 发起流程.
     */
    @RequestMapping("process-operation-startProcessInstance")
    public String startProcessInstance(HttpServletRequest request, Model model)
            throws Exception {
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
        return "operation/process-operation-startProcessInstance";
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
//        buttons.add(buttonHelper.findButton("saveDraft"));
        buttons.add(buttonHelper.findButton(formParameter.getNextStep()));
        model.addAttribute("buttons", buttons);

        model.addAttribute("formDto", formParameter.getFormDto());

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

        return "operation/process-operation-viewStartForm";
    }

    /**
     * 实际展示配置任务的配置.
     */
    public String doTaskConf(FormParameter formParameter, Model model) {
        model.addAttribute("bpmProcessId", formParameter.getBpmProcessId());

        model.addAttribute("businessKey", formParameter.getBusinessKey());
        model.addAttribute("nextStep", formParameter.getNextStep());

        List<HumanTaskDefinition> humanTaskDefinitions = humanTaskConnector
                .findHumanTaskDefinitions(formParameter
                        .getProcessDefinitionId());
        model.addAttribute("humanTaskDefinitions", humanTaskDefinitions);

        return "operation/process-operation-taskConf";
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
    public void setMessageHelper(MessageHelper messageHelper) {
        this.messageHelper = messageHelper;
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
