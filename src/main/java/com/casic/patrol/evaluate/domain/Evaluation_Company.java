package com.casic.patrol.evaluate.domain;

import com.casic.patrol.party.persistence.domain.PartyEntity;

import javax.persistence.*;
import java.util.Date;

/**角色信息
 * Created by lenovo on 2016/5/16.
 */
@Entity
@Table(name="CD_EVALUATION_COMPANY")
@SequenceGenerator(name = "CD_EVALUATION_COMPANYIS",sequenceName = "CD_EVALUATION_COMPANYIS",allocationSize = 1,initialValue = 1)
public class Evaluation_Company {
    private Long id;
    private Double dispose_num;//处置数
    private Double should_dispose_num;//应处置数
    private Double inTime_dispose_num;//按期处置数
    private Double overTime_dispose_num;//超期未处置数
    private Double rework_num;//返工数
    private PartyEntity partyEntity;
    private Integer evaluateType;
    private Date evaluateYear;
    private Date evaluateMonth;
    private String evaluateQuarter;
    private Date evaluateTime;
    private String score;
    private String memo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_EVALUATION_COMPANYIS")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_ENTITY_ID", nullable = false)
    public PartyEntity getPartyEntity() {
        return partyEntity;
    }

    public void setPartyEntity(PartyEntity partyEntity) {
        this.partyEntity = partyEntity;
    }

    @Column(name = "EVALUATE_TIME")
    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    @Column(name = "MEMO")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "SCORE")
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Column(name = "DISPOSE_NUM")
    public Double getDispose_num() {
        return dispose_num;
    }

    public void setDispose_num(Double dispose_num) {
        this.dispose_num = dispose_num;
    }

    @Column(name = "SHOULD_DISPOSE_NUM")

    public Double getShould_dispose_num() {
        return should_dispose_num;
    }

    public void setShould_dispose_num(Double should_dispose_num) {
        this.should_dispose_num = should_dispose_num;
    }

    @Column(name = "INTIME_DISPOSE_NUM")
    public Double getInTime_dispose_num() {
        return inTime_dispose_num;
    }

    public void setInTime_dispose_num(Double inTime_dispose_num) {
        this.inTime_dispose_num = inTime_dispose_num;
    }

    @Column(name = "OVERTIME_DISPOSE_NUM")
    public Double getOverTime_dispose_num() {
        return overTime_dispose_num;
    }

    public void setOverTime_dispose_num(Double overTime_dispose_num) {
        this.overTime_dispose_num = overTime_dispose_num;
    }

    @Column(name = "REWORK_NUM")
    public Double getRework_num() {
        return rework_num;
    }

    public void setRework_num(Double rework_num) {
        this.rework_num = rework_num;
    }

    @Column(name = "EVALUATETYPE")
    public Integer getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(Integer evaluateType) {
        this.evaluateType = evaluateType;
    }

    @Column(name = "EVALUATEYEAR")
    public Date getEvaluateYear() {
        return evaluateYear;
    }

    public void setEvaluateYear(Date evaluateYear) {
        this.evaluateYear = evaluateYear;
    }

    @Column(name = "EVALUATEQUARTER")
    public String getEvaluateQuarter() {
        return evaluateQuarter;
    }

    public void setEvaluateQuarter(String evaluateQuarter) {
        this.evaluateQuarter = evaluateQuarter;
    }

    @Column(name = "EVALUATEMONTH")
    public Date getEvaluateMonth() {
        return evaluateMonth;
    }

    public void setEvaluateMonth(Date evaluateMonth) {
        this.evaluateMonth = evaluateMonth;
    }
}
