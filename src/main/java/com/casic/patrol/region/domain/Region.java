package com.casic.patrol.region.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by lenovo on 2016/8/18.
 */
@Entity
@Table(name="REGIONS")
@SequenceGenerator(name = "CD_REGIONSIS",sequenceName = "CD_REGIONSIS", allocationSize = 1,initialValue = 1)
public class Region {
    private Long dbId;

    private String name;

    private String code;

    private String description;

    private Integer status = 1;

    private List<RegionPoint> points;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_REGIONSIS")
    @Column(name = "DBID")
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long id) {
        this.dbId = id;
    }

    @Column(name = "REGIONNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "REGIONCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name="DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="region")
    public List<RegionPoint> getPoints() {
        return points;
    }

    public void setPoints(List<RegionPoint> points) {
        this.points = points;
    }
}
