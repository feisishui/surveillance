package com.casic.patrol.overtime.scheduler;

import com.casic.patrol.overtime.persistence.domain.OvertimeLevel;
import com.casic.patrol.overtime.persistence.domain.OvertimeInfo;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Component
public class OvertimeInfoJob {
    private static Logger logger = LoggerFactory
            .getLogger(OvertimeInfoJob.class);

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void execute() throws Exception {
        logger.debug("start");

        List<TaskOvertimeInfo> list = taskOvertimeInfoManager.getUnfinishedInfos();
        Date now = new Date();
        for (TaskOvertimeInfo info : list) {
            OvertimeLevel overtimeLevel = OvertimeLevel.valueOf(info.getLevelCode());
            if (OvertimeLevel.UNKNOWN != overtimeLevel) {
                if (null != info.getOvertimeInfo()) {
                    OvertimeInfo temp = info.getOvertimeInfo();
                    long alarmtime = (long)(temp.getAlarmTime() * 60 * 60 * 1000);
                    long warntime = (long)(temp.getWarnTime() * 60 * 60 * 1000);
                    Date starttime = info.getStartTime();
                    info.setWarnTime(new Date(starttime.getTime() + warntime));
                    info.setOverTime(new Date(starttime.getTime() + alarmtime));
                    if (isOvertime(info.getStartTime(), alarmtime, now)) {
                        info.setLevelCode(OvertimeLevel.ALARM.name());
                    } else if (isOvertime(info.getStartTime(), warntime, now)) {
                        info.setLevelCode(OvertimeLevel.WARN.name());
                    } else {
                        info.setLevelCode(OvertimeLevel.NORMAL.name());
                    }
                } else {
                    info.setLevelCode(OvertimeLevel.UNKNOWN.name());
                    info.setWarnTime(null);
                    info.setOverTime(null);
                }
                taskOvertimeInfoManager.save(info);
            }
        }

        logger.debug("end");
    }

    private boolean isOvertime(Date start, long duration, Date now) {
        return now.getTime() - start.getTime() > duration;
    }
}
