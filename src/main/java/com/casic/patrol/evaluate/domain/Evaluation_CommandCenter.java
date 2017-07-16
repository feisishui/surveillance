package com.casic.patrol.evaluate.domain;

import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.user.domain.User;

import javax.persistence.*;
import java.util.Date;

/**角色信息
 * Created by lenovo on 2016/5/16.
 */
@Entity
@Table(name="CD_EVALUATION_COMMANDCENTER")
@SequenceGenerator(name = "CD_EVALUATION_COMMANDCENTERIS",sequenceName = "CD_EVALUATION_COMMANDCENTERIS",allocationSize = 1,initialValue = 1)
public class Evaluation_CommandCenter {
    private Long id;
    private Double accreditation_num;//立案数
    private Double dispatch_confirm_num;//指派核实数
    private Double dispatch_num;//派遣数
    private Double should_dispatch_num;//应派遣数
    private Double inTime_dispatch_num;//按期派遣数
    private Double overTime_dispatch_num;//超期未派遣数
    private Double report_num;//公众上报数
    private Double valid_report_num;//有效公众上报数
    private PartyEntity partyEntity;
    private Integer evaluateType;
    private Date evaluateYear;
    private Date evaluateMonth;
    private String evaluateQuarter;
    private Date evaluateTime;
    private String score;
    private String memo;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CD_EVALUATION_COMMANDCENTERIS")
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

    @Column(name = "ACCREDITATION_NUM")
    public Double getAccreditation_num() {
        return accreditation_num;
    }

    public void setAccreditation_num(Double accreditation_num) {
        this.accreditation_num = accreditation_num;
    }


    @Column(name = "DISPATCH_CONFIRM_NUM")
    public Double getDispatch_confirm_num() {
        return dispatch_confirm_num;
    }

    public void setDispatch_confirm_num(Double dispatch_confirm_num) {
        this.dispatch_confirm_num = dispatch_confirm_num;
    }

    @Column(name = "DISPATCH_NUM")
    public Double getDispatch_num() {
        return dispatch_num;
    }

    public void setDispatch_num(Double dispatch_num) {
        this.dispatch_num = dispatch_num;
    }

    @Column(name = "SHOULD_DISPATCH_NUM")
    public Double getShould_dispatch_num() {
        return should_dispatch_num;
    }

    public void setShould_dispatch_num(Double should_dispatch_num) {
        this.should_dispatch_num = should_dispatch_num;
    }

    @Column(name = "INTIME_DISPATCH_NUM")
    public Double getInTime_dispatch_num() {
        return inTime_dispatch_num;
    }

    public void setInTime_dispatch_num(Double inTime_dispatch_num) {
        this.inTime_dispatch_num = inTime_dispatch_num;
    }

    @Column(name = "OVERTIME_DISPATCH_NUM")
    public Double getOverTime_dispatch_num() {
        return overTime_dispatch_num;
    }

    public void setOverTime_dispatch_num(Double overTime_dispatch_num) {
        this.overTime_dispatch_num = overTime_dispatch_num;
    }

    @Column(name = "REPORT_NUM")
    public Double getReport_num() {
        return report_num;
    }

    public void setReport_num(Double report_num) {
        this.report_num = report_num;
    }

    @Column(name = "VALID_REPORT_NUM")
    public Double getValid_report_num() {
        return valid_report_num;
    }

    public void setValid_report_num(Double valid_report_num) {
        this.valid_report_num = valid_report_num;
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
