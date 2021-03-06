package com.casic.patrol.bpm.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NodeDTO {
    private int x;
    private int y;
    private int width;
    private int height;
    private String type;
    private String id;
    private String name;
    private String assignee;
    private boolean isTask = false;
    private boolean haveDone = false;
    private String assigneeName;
    private Date startTime;
    private Date endTime;
    private List<EdgeDTO> outgoings = new ArrayList<EdgeDTO>();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
        this.isTask = true;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<EdgeDTO> getOutgoings() {
        return outgoings;
    }

    public void setOutgoings(List<EdgeDTO> outgoings) {
        this.outgoings = outgoings;
    }

    public boolean getIsTask() {
        return isTask;
    }

    public void setIsTask(boolean isTask) {
        this.isTask = isTask;
    }

    public boolean isHaveDone() {
        return haveDone;
    }

    public void setHaveDone(boolean haveDone) {
        this.haveDone = haveDone;
    }
}
