package com.casic.patrol.log.domain;

/**
 * Created by yxw on 2015/7/13.
 */
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CD_LOG")
@SequenceGenerator(name = "CD_LOGIS", sequenceName = "CD_LOGIS",allocationSize=1,initialValue=1)
public class Log implements Serializable{

    private long dbId;
    private String handle;
    private String humanTaskId;
    private String processid;
    private String logType;
    private long userId;
    private String staff;
    private Date time;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_LOGIS")
    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    @Column(name = "HANDLE")
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @Column(name = "LOGTYPE")
    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @Column(name = "USER_ID")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "STAFF")
    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    @Column(name = "TIME")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Column(name = "HUMAN_TASK_ID")
    public String getHumanTaskId() {
        return humanTaskId;
    }

    public void setHumanTaskId(String humanTaskId) {
        this.humanTaskId = humanTaskId;
    }

    @Column(name = "PROCESS_INSTANCE_ID")
    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }
}
