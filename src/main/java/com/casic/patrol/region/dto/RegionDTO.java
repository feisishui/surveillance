package com.casic.patrol.region.dto;

import com.casic.patrol.region.domain.Region;
import com.casic.patrol.region.domain.RegionPoint;

import java.util.*;

/**
 * wp
 */
public class RegionDTO {

    private Long dbId;
    private String regionName;
    private String regionCode;
    private String description;
    private Integer status;
    private List<RegionPointDTO> points;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<RegionPointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<RegionPointDTO> points) {
        this.points = points;
    }

    public static RegionDTO ConvertDTO(Region region) {
        RegionDTO regionDTO=new RegionDTO();
        regionDTO.setDbId(region.getDbId());
        regionDTO.setRegionName(region.getName());
        regionDTO.setRegionCode(region.getCode());
        regionDTO.setStatus(region.getStatus());

        List<RegionPointDTO> list = RegionPointDTO.ConvertDTOs(region.getPoints());
        Collections.sort(list, new Comparator<RegionPointDTO>() {
            @Override
            public int compare(RegionPointDTO o1, RegionPointDTO o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
        regionDTO.setPoints(list);
        if(region.getDescription() == null) {
            regionDTO.setDescription("无");
        } else {
            regionDTO.setDescription(region.getDescription());
        }
        return regionDTO;
    }

    public static List<RegionDTO> ConvertDTOs(List<Region> regions) {
        List<RegionDTO> regionDTOs=new ArrayList<RegionDTO>();
        for(Region region : regions) {
            if (region.getPoints() == null || region.getPoints().size() < 3) continue;
            regionDTOs.add(RegionDTO.ConvertDTO(region));
        }
        return regionDTOs;
    }

}
