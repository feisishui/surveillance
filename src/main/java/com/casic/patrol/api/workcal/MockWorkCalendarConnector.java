package com.casic.patrol.api.workcal;

import javax.xml.datatype.Duration;
import java.util.Date;

public class MockWorkCalendarConnector implements WorkCalendarConnector {
    public Date processDate(Date date, String tenantId) {
        return date;
    }

    public Date add(Date date, Duration duration, String tenantId) {
        duration.addTo(date);

        return date;
    }
}
