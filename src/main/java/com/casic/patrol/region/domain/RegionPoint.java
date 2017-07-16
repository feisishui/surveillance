package com.casic.patrol.region.domain;

import javax.persistence.*;

/**
 * Created by lenovo on 2016/8/18.
 */
@Entity
@Table(name="REGIONSPOINT")
@SequenceGenerator(name = "CD_REGIONSPOINTIS",sequenceName = "CD_REGIONSPOINTIS", allocationSize = 1,initialValue = 1)
public class RegionPoint {
    private Long dbId;

    private String x;

    private String y;

    private Integer index;

    private Region region;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_REGIONSPOINTIS")
    @Column(name = "DBID")
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long id) {
        this.dbId = id;
    }

    @Column(name = "POINTX")
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Column(name = "POINTY")
    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Column(name = "POINTINDEX")
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REGION_ID")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
