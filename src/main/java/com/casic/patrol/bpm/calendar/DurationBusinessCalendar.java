package com.casic.patrol.bpm.calendar;

import org.activiti.engine.ActivitiException;

import java.util.Date;

/**
 * 时间段.
 */
public class DurationBusinessCalendar extends AdvancedBusinessCalendar {
    public Date resolveDuedate(String duedate, int maxIterations) {
        try {
            DurationUtil durationUtil = new DurationUtil(duedate, this);

            return durationUtil.getDateAfter();
        } catch (Exception e) {
            throw new ActivitiException("couldn't resolve duedate: "
                    + e.getMessage(), e);
        }
    }

    public String getName() {
        return "duration";
    }
}
