package com.casic.patrol.log.dto;

/**
 * Created by yxw on 2015/7/13.
 */

import com.casic.patrol.log.domain.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LogDTO {
    private Long dbId;
    private String handle;
    private String logType;
    private String staff;
    private String time;

    private String humanTaskId;
    private String processid;
    private long userId;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHumanTaskId() {
        return humanTaskId;
    }

    public void setHumanTaskId(String humanTaskId) {
        this.humanTaskId = humanTaskId;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static LogDTO convertToDTO(Log log) {
        LogDTO logDTO=new LogDTO();
        logDTO.setDbId(log.getDbId());
        logDTO.setHandle(log.getHandle());
        logDTO.setLogType(log.getLogType());
        logDTO.setProcessid(log.getProcessid());
        logDTO.setStaff(log.getStaff());
        logDTO.setHumanTaskId(log.getHumanTaskId());
        logDTO.setUserId(log.getUserId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logDTO.setTime(sdf.format(log.getTime()));

        return logDTO;
    }

    public static List<LogDTO> convertToDTOs(List<Log> apps) {
        List<LogDTO> appDTOs = new ArrayList<LogDTO>();
        for (Log app : apps) {
            appDTOs.add(LogDTO.convertToDTO(app));
        }
        return appDTOs;
    }
}
