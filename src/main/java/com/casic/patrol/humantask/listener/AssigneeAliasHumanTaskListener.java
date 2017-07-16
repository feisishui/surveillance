package com.casic.patrol.humantask.listener;


import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.domain.TaskParticipant;
import com.casic.patrol.humantask.persistence.manager.TaskParticipantManager;
import com.casic.patrol.humantask.rule.*;
import com.casic.patrol.spi.process.InternalProcessConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;

/**
 * 处理负责人配置的别名.
 */
public class AssigneeAliasHumanTaskListener implements HumanTaskListener {
    private static Logger logger = LoggerFactory
            .getLogger(AssigneeAliasHumanTaskListener.class);
    private InternalProcessConnector internalProcessConnector;
    private Map<RuleMatcher, AssigneeRule> assigneeRuleMap = new HashMap<RuleMatcher, AssigneeRule>();
    @Resource
    private TaskParticipantManager taskParticipantManager;

    public AssigneeAliasHumanTaskListener() {
        SuperiorAssigneeRule superiorAssigneeRule = new SuperiorAssigneeRule();
        PositionAssigneeRule positionAssigneeRule = new PositionAssigneeRule();
        InitiatorAssigneeRule initiatorAssigneeRule = new InitiatorAssigneeRule();
        ActivityAssigneeRule activityAssigneeRule = new ActivityAssigneeRule();
        assigneeRuleMap.put(new EqualsRuleMatcher("常用语:直接上级"),
                superiorAssigneeRule);
        assigneeRuleMap.put(new EqualsRuleMatcher("常用语:流程发起人"),
                initiatorAssigneeRule);
        assigneeRuleMap.put(new PrefixRuleMatcher("岗位"), positionAssigneeRule);
        assigneeRuleMap.put(new PrefixRuleMatcher("环节处理人"),
                activityAssigneeRule);
    }

    @Override
    public void onCreate(TaskInfo taskInfo) throws Exception {
        String assignee = taskInfo.getAssignee();
        logger.debug("assignee : {}", assignee);

        if (assignee == null) {
            return;
        }

        if (assignee.startsWith("${")) {
            //fix the bug list failed to be converted to string
            List<String> assignees =(List<String>)internalProcessConnector.executeExpression(taskInfo.getTaskId(), assignee);
            if (assignees.size() != 0) {
                if (assignees.size() == 1) {
                    taskInfo.setAssignee(assignees.get(0));
                } else {
                    taskInfo.setAssignee(null);
                    storeAssigneeAsCandidate(assignees, taskInfo);
                }
                return;
            }
        }

        for (Map.Entry<RuleMatcher, AssigneeRule> entry : assigneeRuleMap
                .entrySet()) {
            RuleMatcher ruleMatcher = entry.getKey();

            if (!ruleMatcher.matches(assignee)) {
                continue;
            }

            String value = ruleMatcher.getValue(assignee);
            AssigneeRule assigneeRule = entry.getValue();
            logger.debug("value : {}", value);
            logger.debug("assigneeRule : {}", assigneeRule);

            if (assigneeRule instanceof SuperiorAssigneeRule) {
                this.processSuperior(taskInfo, assigneeRule, value);
            } else if (assigneeRule instanceof InitiatorAssigneeRule) {
                this.processInitiator(taskInfo, assigneeRule, value);
            } else if (assigneeRule instanceof ActivityAssigneeRule) {
                this.processActivityAssignee(taskInfo, assigneeRule, value);
            } else if (assigneeRule instanceof PositionAssigneeRule) {
                this.processPosition(taskInfo, assigneeRule, value);
            }
        }
    }

    @Override
    public void onComplete(TaskInfo taskInfo) throws Exception {
    }

    public void processSuperior(TaskInfo taskInfo, AssigneeRule assigneeRule,
            String value) {
        String processInstanceId = taskInfo.getProcessInstanceId();
        String startUserId = internalProcessConnector
                .findInitiator(processInstanceId);
        String userId = assigneeRule.process(startUserId);
        logger.debug("userId : {}", userId);
        taskInfo.setAssignee(userId);
    }

    public void processInitiator(TaskInfo taskInfo, AssigneeRule assigneeRule,
            String value) {
        String processInstanceId = taskInfo.getProcessInstanceId();
        String startUserId = internalProcessConnector
                .findInitiator(processInstanceId);
        String userId = assigneeRule.process(startUserId);
        logger.debug("userId : {}", userId);
        taskInfo.setAssignee(userId);
    }

    public void processActivityAssignee(TaskInfo taskInfo,
            AssigneeRule assigneeRule, String value) {
        String processInstanceId = taskInfo.getProcessInstanceId();
        List<String> userIds = assigneeRule.process(value, processInstanceId);
        logger.debug("userIds : {}", userIds);

        if (!userIds.isEmpty()) {
            taskInfo.setAssignee(userIds.get(0));
        }
    }

    public void processPosition(TaskInfo taskInfo, AssigneeRule assigneeRule,
            String value) {
        String processInstanceId = taskInfo.getProcessInstanceId();
        String startUserId = internalProcessConnector
                .findInitiator(processInstanceId);
        List<String> userIds = assigneeRule.process(value, startUserId);
        logger.debug("userIds : {}", userIds);

        if (!userIds.isEmpty()) {
            taskInfo.setAssignee(userIds.get(0));
        }
    }

    private void storeAssigneeAsCandidate(List<String> assignees, TaskInfo taskInfo) {
        Set<TaskParticipant> set = taskInfo.getTaskParticipants();
        if (set == null) set = new HashSet<TaskParticipant>();
        for (String userid : assignees) {
            if (!taskParticipantManager.exist(userid, taskInfo.getId())) {
                TaskParticipant taskParticipant = new TaskParticipant();
                taskParticipant.setCategory("candidate");
                taskParticipant.setRef(userid);
                taskParticipant.setType("user");
                taskParticipant.setTaskInfo(taskInfo);
                taskParticipantManager.save(taskParticipant);
                set.add(taskParticipant);
            }
        }
        taskInfo.setTaskParticipants(set);
    }

    @Resource
    public void setInternalProcessConnector(
            InternalProcessConnector internalProcessConnector) {
        this.internalProcessConnector = internalProcessConnector;
    }
}
