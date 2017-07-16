package com.casic.patrol.bpm.calendar;

import org.activiti.engine.impl.calendar.BusinessCalendar;
import org.activiti.engine.impl.calendar.BusinessCalendarManager;

import java.util.HashMap;
import java.util.Map;

public class AdvancedBusinessCalendarManager implements BusinessCalendarManager {
    private Map<String, BusinessCalendar> businessCalendarMap;

    public AdvancedBusinessCalendarManager() {
        businessCalendarMap = new HashMap<String, BusinessCalendar>();
        this.addBusinessCalendar(new DueDateBusinessCalendar());
        this.addBusinessCalendar(new DurationBusinessCalendar());
        this.addBusinessCalendar(new CycleBusinessCalendar());
    }

    public void addBusinessCalendar(AdvancedBusinessCalendar businessCalendar) {
        businessCalendarMap.put(businessCalendar.getName(), businessCalendar);
    }

    public BusinessCalendar getBusinessCalendar(String businessCalendarRef) {
        return businessCalendarMap.get(businessCalendarRef);
    }
}
