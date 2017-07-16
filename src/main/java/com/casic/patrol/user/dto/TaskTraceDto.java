package com.casic.patrol.user.dto;

import com.casic.patrol.user.domain.User;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 2016/4/20.
 */
public class TaskTraceDto {

    private Long id;
    private String taskId;
    private String taskName;
    private Long confirmUser_id;
    private String confirmUser_name;
    private String startTime;
    private String endTime;
    private String btnTrace = "<a href='#' class='btn mini blue'><i class='icon-edit'></i>查看</a>";

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getBtnTrace() {
        return btnTrace;
    }

    public void setBtnTrace(String btnTrace) {
        this.btnTrace = btnTrace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getConfirmUser_id() {
        return confirmUser_id;
    }

    public void setConfirmUser_id(Long confirmUser_id) {
        this.confirmUser_id = confirmUser_id;
    }

    public String getConfirmUser_name() {
        return confirmUser_name;
    }

    public void setConfirmUser_name(String confirmUser_name) {
        this.confirmUser_name = confirmUser_name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
