package com.casic.patrol.evaluate.domain;

import com.casic.patrol.party.persistence.domain.PartyEntity;

import javax.persistence.*;
import java.util.Date;

/**角色信息
 * Created by lenovo on 2016/5/16.
 */
@Entity
@Table(name="CD_EVALUATION_PATROL")
@SequenceGenerator(name = "CD_EVALUATION_PATROLIS",sequenceName = "CD_EVALUATION_PATROLIS",allocationSize = 1,initialValue = 1)
public class Evaluation_Patrol {
    private Long id;
    private Double confirm_num;//核实数
    private Double should_confirm_num;//应核实数
    private Double inTime_confirm_num;//按期核实数
    private Double overTime_confirm_num;//超期未核实数
    private Double mis_report_num;//漏报数
    private PartyEntity partyEntity;
    private Integer evaluateType;
    private Date evaluateYear;
    private Date evaluateMonth;
    private String evaluateQuarter;
    private Date evaluateTime;
    private String score;
    private String memo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_EVALUATION_PATROLIS")
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

    @Column(name = "CONFIRM_NUM")
    public Double getConfirm_num() {
        return confirm_num;
    }

    public void setConfirm_num(Double confirm_num) {
        this.confirm_num = confirm_num;
    }

    @Column(name = "SHOULD_CONFIRM_NUM")
    public Double getShould_confirm_num() {
        return should_confirm_num;
    }

    public void setShould_confirm_num(Double should_confirm_num) {
        this.should_confirm_num = should_confirm_num;
    }

    @Column(name = "INTIME_CONFIRM_NUM")
    public Double getInTime_confirm_num() {
        return inTime_confirm_num;
    }

    public void setInTime_confirm_num(Double inTime_confirm_num) {
        this.inTime_confirm_num = inTime_confirm_num;
    }

    @Column(name = "OVERTIME_CONFIRM_NUM")
    public Double getOverTime_confirm_num() {
        return overTime_confirm_num;
    }

    public void setOverTime_confirm_num(Double overTime_confirm_num) {
        this.overTime_confirm_num = overTime_confirm_num;
    }

    @Column(name = "MIS_REPORT_NUM")
    public Double getMis_report_num() {
        return mis_report_num;
    }

    public void setMis_report_num(Double mis_report_num) {
        this.mis_report_num = mis_report_num;
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
