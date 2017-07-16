package com.casic.patrol.bpm.calendar;

import org.activiti.engine.ActivitiException;

import java.util.Date;

/**
 * 截止日期.
 */
public class DueDateBusinessCalendar extends AdvancedBusinessCalendar {
    public Date resolveDuedate(String duedate, int maxIterations) {
        try {
            return new DurationUtil(duedate, this).getDateAfter();
        } catch (Exception e) {
            throw new ActivitiException("couldn't resolve duedate: "
                    + e.getMessage(), e);
        }
    }

    public String getName() {
        return "dueDate";
    }
}
