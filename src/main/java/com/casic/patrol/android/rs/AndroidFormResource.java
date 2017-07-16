package com.casic.patrol.android.rs;


import com.casic.patrol.api.StoreConnector;
import com.casic.patrol.api.form.FormDTO;
import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.keyvalue.KeyValueConnector;
import com.casic.patrol.api.keyvalue.Record;
import com.casic.patrol.api.process.ProcessConnector;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.api.user.UserConnector;
import com.casic.patrol.bpm.persistence.manager.BpmProcessManager;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.model.support.FormField;
import com.casic.patrol.xform.Xform;
import com.casic.patrol.xform.XformBuilder;
import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Path("android/form")
public class AndroidFormResource {
    private static Logger logger = LoggerFactory
            .getLogger(AndroidFormResource.class);
    private JsonMapper jsonMapper = new JsonMapper();
    private ProcessEngine processEngine;
    private BpmProcessManager bpmProcessManager;
    private TenantHolder tenantHolder;
    private ProcessConnector processConnector;
    private HumanTaskConnector humanTaskConnector;
    private KeyValueConnector keyValueConnector;
    private StoreConnector storeConnector;
    private UserConnector userConnector;

    @POST
    @Path("viewStartForm")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewStartForm(
            @FormParam("processDefinitionId") String processDefinitionId)
            throws Exception {
        logger.info("start : {}", processDefinitionId);

        FormDTO formDto = processConnector.findStartForm(processDefinitionId);

        if (formDto.isRedirect()) {
            return "";
        }

        Map<String, Object> formJson = jsonMapper.fromJson(
                formDto.getContent(), Map.class);
        List<Map<String, Object>> sections = (List<Map<String, Object>>) formJson
                .get("sections");
        List<FormField> formFields = new ArrayList<FormField>();

        for (Map<String, Object> section : sections) {
            if (!"grid".equals(section.get("type"))) {
                continue;
            }

            List<Map<String, Object>> fields = (List<Map<String, Object>>) section
                    .get("fields");

            for (Map<String, Object> field : fields) {
                String type = (String) field.get("type");
                String name = (String) field.get("name");
                String items = (String) field.get("items");
                String label = name;
                Boolean readOnly = (Boolean) field.get("readOnly");
                Boolean required = (Boolean) field.get("required");

                if (readOnly == null) {
                    readOnly = false;
                }
                if (required == null) {
                    required = false;
                }

                if ("label".equals(type)) {
                    continue;
                }

                FormField formField = null;

                formField = new FormField();
                formField.setName(name);
                formField.setLabel(label);
                formField.setType(type);
                formField.setItems(items);
                formField.setReadOnly(readOnly);
                formField.setRequired(required);
                formFields.add(formField);
            }
        }

        StringBuilder buff = new StringBuilder();

        buff.append("<xmlgui>")
                .append("<form id='1' name='form' submitTo='http://192.168.1.106:8080/mossle-app-lemon/rs/android/bpm/startProcess' >");

        // .append("<field name='fname' label='First Name' type='text' required='Y' options=''/>")
        // .append("<field name='lname' label='Last Name' type='text' required='Y' options=''/>")
        // .append("<field name='gender' label='Gender' type='choice' required='Y' options='Male|Female'/>")
        // .append("<field name='age' label='Age on 15 Oct. 2010' type='numeric' required='N' options=''/>")
        for (FormField formField : formFields) {
            String type = "text";
            String options = "";

            if ("radio".equals(formField.getType())
                    || "checkbox".equals(formField.getType())
                    || "select".equals(formField.getType())) {
                type = "choice";
                options = formField.getItems().replaceAll(",", "|");
            }
            else if ("userpicker".equals(formField.getType())) {
                type = "userpicker";
            }
            else if ("fileupload".equals(formField.getType())) {
                type = "fileupload";
            }
            else if ("datepicker".equals(formField.getType())) {
                type = "datepicker";
            }

            buff.append("<field name='" + formField.getName() + "'");
            buff.append(" label='" + formField.getName() + "'");
            buff.append(" type='" + type + "'");
            buff.append(" required='" + (formField.isRequired() ? "Y" : "N")
                    + "'");
            buff.append(" readOnly='" + (formField.isReadOnly() ? "Y" : "N")
                    + "'");
            buff.append(" options='" + options + "'/>");
        }

        buff.append("</form>").append("</xmlgui>");

        return buff.toString();
    }

    @POST
    @Path("viewTaskForm")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewTaskForm(@FormParam("taskId") String taskId)
            throws Exception {
        logger.info("start : {}", taskId);

        HumanTaskDTO humanTaskDto = humanTaskConnector.findHumanTask(taskId);

        if (humanTaskDto == null) {
            return "";
        }

        FormDTO formDto = this.findTaskForm(humanTaskDto);

        if (formDto.isRedirect()) {
            return "";
        }

        Map<String, Object> formJson = jsonMapper.fromJson(
                formDto.getContent(), Map.class);
        List<Map<String, Object>> sections = (List<Map<String, Object>>) formJson
                .get("sections");
        List<FormField> formFields = new ArrayList<FormField>();

        for (Map<String, Object> section : sections) {
            if (!"grid".equals(section.get("type"))) {
                continue;
            }

            List<Map<String, Object>> fields = (List<Map<String, Object>>) section
                    .get("fields");

            for (Map<String, Object> field : fields) {
                String type = (String) field.get("type");
                String name = (String) field.get("name");
                String items = (String) field.get("items");
                String label = name;
                Boolean readOnly = (Boolean) field.get("readOnly");
                Boolean required = (Boolean) field.get("required");

                if (readOnly == null) {
                    readOnly = false;
                }

                if ("complete".equals(humanTaskDto.getStatus())) {
                    readOnly = true;
                }

                if ("label".equals(type)) {
                    continue;
                }

                FormField formField = null;

                formField = new FormField();
                formField.setName(name);
                formField.setLabel(label);
                formField.setType(type);
                formField.setItems(items);
                formField.setReadOnly(readOnly);
                formField.setRequired(required);
                formFields.add(formField);
            }
        }

        // 如果是任务草稿，直接通过processInstanceId获得record，更新数据
        // TODO: 分支肯定有问题
        String processInstanceId = humanTaskDto.getProcessInstanceId();

        Record record = keyValueConnector.findByRef(processInstanceId);

        Xform xform = new XformBuilder().setStoreConnector(storeConnector).setUserConnector(userConnector)
                .setContent(formDto.getContent()).setRecord(record).build();

        StringBuilder buff = new StringBuilder();

        buff.append("<xmlgui>")
                .append("<form id='1' name='form' submitTo='http://192.168.1.106:8080/mossle-app-lemon/rs/android/task/completeTask'")
                .append(" readOnly='")
                .append("complete".equals(humanTaskDto.getStatus()))
                .append("'").append(" >");

        // .append("<field name='fname' label='First Name' type='text' required='Y' options=''/>")
        // .append("<field name='lname' label='Last Name' type='text' required='Y' options=''/>")
        // .append("<field name='gender' label='Gender' type='choice' required='Y' options='Male|Female'/>")
        // .append("<field name='age' label='Age on 15 Oct. 2010' type='numeric' required='N' options=''/>")
        for (FormField formField : formFields) {
            String name = formField.getName();
            String type = "text";
            String options = "";
            String value = "";

            try {
                if (xform.findXformField(name).getValue() != null) {
                    value = xform.findXformField(name).getValue().toString();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if ("radio".equals(formField.getType())
                    || "checkbox".equals(formField.getType())) {
                type = "choice";
                options = formField.getItems().replaceAll(",", "|");
            }
            else if ("userpicker".equals(formField.getType())) {
                type = "userpicker";
            }
            else if ("fileupload".equals(formField.getType())&&name.equals("uploadPic")) {
                type = "pictureFileupload";
                if(value!=null&&!value.equals(""))
                    value = "casic.store/"+record.getTenantId()+"/form/"+value;
            }
            else if ("fileupload".equals(formField.getType())&&name.equals("uploadMv")) {
                type = "videoFileupload";
                if(value!=null&&!value.equals(""))
                    value = "casic.store/"+record.getTenantId()+"/form/"+value;
            }

            buff.append("<field name='" + formField.getName() + "'");
            buff.append(" label='" + formField.getName() + "'");
            buff.append(" type='" + type + "'");
            buff.append(" required='" + (formField.isRequired() ? "Y" : "N")
                    + "'");
            buff.append(" readOnly='" + (formField.isReadOnly() ? "Y" : "N")
                    + "'");
            buff.append(" options='" + options + "'");
            buff.append(" value='" + value + "'/>");
        }

        buff.append("</form>").append("</xmlgui>");
        logger.info("{}", buff);

        return buff.toString();
    }

    public FormDTO findTaskForm(HumanTaskDTO humanTaskDto) {
        FormDTO formDto = humanTaskConnector.findTaskForm(humanTaskDto.getId());

        return formDto;
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
    public void setProcessConnector(ProcessConnector processConnector) {
        this.processConnector = processConnector;
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setKeyValueConnector(KeyValueConnector keyValueConnector) {
        this.keyValueConnector = keyValueConnector;
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
