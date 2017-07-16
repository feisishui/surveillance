package com.casic.patrol.bpm.service;


import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.bpm.support.EdgeDTO;
import com.casic.patrol.bpm.support.NodeDTO;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.manager.TaskInfoManager;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.domain.PartyStruct;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.manager.UserManager;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TraceService {
    private static Logger logger = LoggerFactory.getLogger(TraceService.class);
    private ProcessEngine processEngine;

    public List<NodeDTO> traceProcessInstance(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = processEngine
                .getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(
                historicProcessInstance.getProcessDefinitionId());
        BpmnModel bpmnModel = processEngine.getManagementService()
                .executeCommand(getBpmnModelCmd);

        Map<String, GraphicInfo> graphicInfoMap = bpmnModel.getLocationMap();

        List<NodeDTO> nodeDtos = new ArrayList<NodeDTO>();

        for (Map.Entry<String, GraphicInfo> entry : graphicInfoMap.entrySet()) {
            String key = entry.getKey();
            GraphicInfo graphicInfo = entry.getValue();
            nodeDtos.add(this.convertNodeDto(graphicInfo,
                    bpmnModel.getFlowElement(key), key, bpmnModel, processInstanceId));
        }

        return nodeDtos;
    }

    public NodeDTO convertNodeDto(GraphicInfo graphicInfo,
            FlowElement flowElement, String id, BpmnModel bpmnModel, String processInstanceId) {
        NodeDTO nodeDto = new NodeDTO();
        nodeDto.setX((int) graphicInfo.getX());
        nodeDto.setY((int) graphicInfo.getY());
        nodeDto.setWidth((int) graphicInfo.getWidth());
        nodeDto.setHeight((int) graphicInfo.getHeight());
        //
        nodeDto.setId(id);
        nodeDto.setName(flowElement.getName());

        if (flowElement instanceof UserTask) {
            nodeDto.setType("用户任务");

            UserTask userTask = (UserTask) flowElement;
            nodeDto.setAssignee(userTask.getAssignee());
            processUserTask(nodeDto, id, processInstanceId);
        } else if (flowElement instanceof StartEvent) {
            nodeDto.setType("开始事件");
        } else if (flowElement instanceof EndEvent) {
            nodeDto.setType("结束事件");
        } else if (flowElement instanceof ExclusiveGateway) {
            nodeDto.setType("选择网关");
        }

        if (flowElement instanceof FlowNode) {
            FlowNode flowNode = (FlowNode) flowElement;

            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                EdgeDTO edgeDto = new EdgeDTO();
                edgeDto.setId(sequenceFlow.getTargetRef());

                for (GraphicInfo flowGraphicInfo : bpmnModel
                        .getFlowLocationGraphicInfo(sequenceFlow.getId())) {
                    List<Integer> position = new ArrayList<Integer>();
                    position.add((int) flowGraphicInfo.getX()
                            - ((int) graphicInfo.getWidth() / 2));
                    position.add((int) flowGraphicInfo.getY()
                            - ((int) graphicInfo.getHeight() / 2));
                    edgeDto.getG().add(position);
                }

                edgeDto.getG().remove(0);
                edgeDto.getG().remove(edgeDto.getG().size() - 1);
                logger.debug("{}", edgeDto.getG());
                nodeDto.getOutgoings().add(edgeDto);
            }
        }

        return nodeDto;
    }

    @Resource
    private TaskInfoManager taskInfoManager;

    @Resource
    private UserManager userManager;

    @Resource
    private PartyEntityManager partyEntityManager;

    @Resource
    private TenantHolder tenantHolder;

    private void processUserTask(NodeDTO dto, String id, String processInstanceId) {
        List<TaskInfo> list = taskInfoManager.getTaskInfoForViewHistory(id, processInstanceId, tenantHolder.getTenantId());
        if (list != null && list.size() > 0) {
            dto.setHaveDone(true);
            StringBuilder sb = new StringBuilder();
            for (TaskInfo taskInfo : list) {
                User user = userManager.getUserById(Long.parseLong(taskInfo.getAssignee()));
                checkedPartyEntity.clear();
                PartyEntity partyEntity = partyEntityManager.getPartyEntityByRef(user.getId().toString(), tenantHolder.getTenantId());
                if (user != null && user.getIsValid() == 1) {
                    sb.append(user.getUserName());
                    String party = getPartyName(new HashSet<PartyEntity>(Arrays.asList(partyEntity)));
                    if (party != null) {
                        sb.append("[").append(party).append("]");
                    }
                    sb.append(",");
                }
            }
            if (sb.length() > 0) sb = new StringBuilder(sb.substring(0, sb.length() - 1));
            dto.setAssigneeName(sb.toString());
        }
    }

    /*防止无限递归*/
    private Set<Long> checkedPartyEntity = new HashSet<Long>();

    /**
     * 组织机构类型的类型，即{@link com.casic.patrol.party.persistence.domain.PartyType#type}
     * 该type在数据库中存储为0、1、2三个值，分别对应组织、岗位、人员（在前端显示）
     */
    public static final Integer PERSON_FOR_TYPE_OF_PARTY_TYPE = 2;

    private String getPartyName(Set<PartyEntity> entities) {
        if (entities == null || entities.size() < 1) return null;
        Set<PartyEntity> set = new HashSet<PartyEntity>();
        for (PartyEntity entity : entities) {
            if (entity == null) continue;
            checkedPartyEntity.add(entity.getId());
            if (entity.getPartyType().getType() != PERSON_FOR_TYPE_OF_PARTY_TYPE) {
                return entity.getName();
            } else {
                for (PartyStruct temp : entity.getParentStructs()) {
                    if (temp != null && temp.getParentEntity() != null &&
                            !checkedPartyEntity.contains(temp.getParentEntity().getId())) {
                        set.add(temp.getParentEntity());
                    }
                }
            }
        }
        return getPartyName(set);
    }

    @Resource
    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
}
