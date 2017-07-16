package com.casic.patrol.bpm.support;

import org.activiti.engine.history.HistoricVariableInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/8/12.
 */
public class ActivitiVariableDTO {

    private String name;
    private String varType;
    private Object value;
    private String id;
    private String processInstanceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public static ActivitiVariableDTO convertToDTO(HistoricVariableInstance dto) {
        ActivitiVariableDTO variable = new ActivitiVariableDTO();
        variable.setName(dto.getVariableName());
        variable.setVarType(dto.getVariableTypeName());
        variable.setValue(dto.getValue());
        variable.setId(dto.getId());
        variable.setProcessInstanceId(dto.getProcessInstanceId());
        return variable;
    }

    public static Map<String, ActivitiVariableDTO> convertToDTOs(List<HistoricVariableInstance> dtos) {
        Map<String, ActivitiVariableDTO> result = new HashMap<String, ActivitiVariableDTO>();
        for (HistoricVariableInstance dto : dtos) {
            ActivitiVariableDTO temp = ActivitiVariableDTO.convertToDTO(dto);
            result.put(temp.getName(), temp);
        }
        return result;
    }
}
