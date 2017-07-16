package com.casic.patrol.humantask.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 获得流程发起人.
 * 
 */
public class InitiatorAssigneeRule implements AssigneeRule {
    private static Logger logger = LoggerFactory
            .getLogger(InitiatorAssigneeRule.class);

    public String process(String initiator) {
        return initiator;
    }

    public List<String> process(String value, String initiator) {
        return Collections.singletonList(initiator);
    }
}
