package com.casic.patrol.indication.support;

import com.casic.patrol.party.persistence.domain.PartyEntity;

/**
 * Created by lenovo on 2017/5/24.
 */
public class IndicationResult {

    /** 公众上报数 */
    private Double reportNum;

    /** 指派数 */
    private Double assignNum;

    /** 派遣数 */
    private Double dispatchNum;

    /** 应派遣数 */
    private Double dispatchShouldNum;

    /** 按期派遣数 */
    private Double dispatchOntimeNum;

    /** 超期派遣数 */
    private Double dispatchOverdueNum;

    /** 派遣任务超期数，超期派遣数与超期未派遣数集合 */
    private Double undispatchOverdueNum;

    public Double getReportNum() {
        return reportNum;
    }

    public void setReportNum(Double reportNum) {
        this.reportNum = reportNum;
    }

    public Double getAssignNum() {
        return assignNum;
    }

    public void setAssignNum(Double assignNum) {
        this.assignNum = assignNum;
    }

    public Double getDispatchNum() {
        return dispatchNum;
    }

    public void setDispatchNum(Double dispatchNum) {
        this.dispatchNum = dispatchNum;
    }

    public Double getDispatchShouldNum() {
        return dispatchShouldNum;
    }

    public void setDispatchShouldNum(Double dispatchShouldNum) {
        this.dispatchShouldNum = dispatchShouldNum;
    }

    public Double getDispatchOntimeNum() {
        return dispatchOntimeNum;
    }

    public void setDispatchOntimeNum(Double dispatchOntimeNum) {
        this.dispatchOntimeNum = dispatchOntimeNum;
    }

    public Double getDispatchOverdueNum() {
        return dispatchOverdueNum;
    }

    public void setDispatchOverdueNum(Double dispatchOverdueNum) {
        this.dispatchOverdueNum = dispatchOverdueNum;
    }

    public Double getUndispatchOverdueNum() {
        return undispatchOverdueNum;
    }

    public void setUndispatchOverdueNum(Double undispatchOverdueNum) {
        this.undispatchOverdueNum = undispatchOverdueNum;
    }

    /** 核实数数 */
    private Double verifyNum;

    /** 应核实数 */
    private Double verifyShouldNum;

    /** 按期核实数 */
    private Double verifyOntimeNum;

    /** 超期核实数 */
    private Double verifyOverdueNum;

    /** 超期未核实数 */
    private Double unverifyOverdueNum;

    /** 漏报数 */
    private Double missingReportNum;

    public Double getVerifyNum() {
        return verifyNum;
    }

    public void setVerifyNum(Double verifyNum) {
        this.verifyNum = verifyNum;
    }

    public Double getVerifyShouldNum() {
        return verifyShouldNum;
    }

    public void setVerifyShouldNum(Double verifyShouldNum) {
        this.verifyShouldNum = verifyShouldNum;
    }

    public Double getVerifyOntimeNum() {
        return verifyOntimeNum;
    }

    public void setVerifyOntimeNum(Double verifyOntimeNum) {
        this.verifyOntimeNum = verifyOntimeNum;
    }

    public Double getVerifyOverdueNum() {
        return verifyOverdueNum;
    }

    public void setVerifyOverdueNum(Double verifyOverdueNum) {
        this.verifyOverdueNum = verifyOverdueNum;
    }

    public Double getUnverifyOverdueNum() {
        return unverifyOverdueNum;
    }

    public void setUnverifyOverdueNum(Double unverifyOverdueNum) {
        this.unverifyOverdueNum = unverifyOverdueNum;
    }

    public Double getMissingReportNum() {
        return missingReportNum;
    }

    public void setMissingReportNum(Double missingReportNum) {
        this.missingReportNum = missingReportNum;
    }

    /** 处置数 */
    private Double disposeNum;

    /** 应处置数 */
    private Double disposeShouldNum;

    /** 按期处置数 */
    private Double disposeOntimeNum;

    /** 超期处置数 */
    private Double disposeOverdueNum;

    /** 超期处置数 */
    private Double undisposeOverdueNum;

    /** 返工数 */
    private Double reworkNum;

    public Double getDisposeNum() {
        return disposeNum;
    }

    public void setDisposeNum(Double disposeNum) {
        this.disposeNum = disposeNum;
    }

    public Double getDisposeShouldNum() {
        return disposeShouldNum;
    }

    public void setDisposeShouldNum(Double disposeShouldNum) {
        this.disposeShouldNum = disposeShouldNum;
    }

    public Double getDisposeOntimeNum() {
        return disposeOntimeNum;
    }

    public void setDisposeOntimeNum(Double disposeOntimeNum) {
        this.disposeOntimeNum = disposeOntimeNum;
    }

    public Double getDisposeOverdueNum() {
        return disposeOverdueNum;
    }

    public void setDisposeOverdueNum(Double disposeOverdueNum) {
        this.disposeOverdueNum = disposeOverdueNum;
    }

    public Double getUndisposeOverdueNum() {
        return undisposeOverdueNum;
    }

    public void setUndisposeOverdueNum(Double undisposeOverdueNum) {
        this.undisposeOverdueNum = undisposeOverdueNum;
    }

    public Double getReworkNum() {
        return reworkNum;
    }

    public void setReworkNum(Double reworkNum) {
        this.reworkNum = reworkNum;
    }

    private PartyEntity person;

    private String department;

    public PartyEntity getPerson() {
        return person;
    }

    public void setPerson(PartyEntity person) {
        this.person = person;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
