package com.casic.patrol.api.workcal;

import javax.xml.datatype.Duration;
import java.util.Date;

public interface WorkCalendarConnector {
    Date processDate(Date date, String tenantId);

    Date add(Date date, Duration duration, String tenantId);
}
