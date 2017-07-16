package com.casic.patrol.region.dto;

import com.casic.patrol.region.domain.RegionPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * wp
 */
public class RegionPointDTO {

    private Long dbId;
    private String x;
    private String y;
    private Long regionId;
    private Integer index;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public static RegionPointDTO ConvertDTO(RegionPoint point) {
        RegionPointDTO pointDTO = new RegionPointDTO();
        pointDTO.setDbId(point.getDbId());
        pointDTO.setX(point.getX());
        pointDTO.setY(point.getY());
        pointDTO.setRegionId(point.getRegion().getDbId());
        pointDTO.setIndex(point.getIndex());
        return pointDTO;
    }

    public static List<RegionPointDTO> ConvertDTOs(List<RegionPoint> points) {
        List<RegionPointDTO> pointDTOs = new ArrayList<RegionPointDTO>();
        for(RegionPoint point : points) {
            pointDTOs.add(RegionPointDTO.ConvertDTO(point));
        }
        return pointDTOs;
    }

}
