package com.casic.patrol.user.dto;

import com.casic.patrol.user.domain.Position;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PositionDto implements Serializable
{
    private long dbId;
    private String longitude;
    private String latitude;
    private String user_id;
    private String locaTime;
    private String userName;

    public PositionDto(Position position) {
        this.setDbId(position.getId());
        this.setLatitude(String.valueOf(position.getLatitude()));
        this.setLongitude(String.valueOf(position.getLongitude()));
        if (position.getUser()!=null)
        {
            this.setUserName(position.getUser().getUserName());
            this.setUser_id(position.getUser().getId().toString());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date locaTime = position.getLocaTime();
        if(locaTime != null)
            this.setLocaTime(simpleDateFormat.format(locaTime));
        else
            this.setLocaTime(null);
    }

    public long getDbId() {
        return dbId;
    }
    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocaTime() {
        return locaTime;
    }

    public void setLocaTime(String locaTime) {
        this.locaTime = locaTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
