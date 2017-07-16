package com.casic.patrol.evaluate.dto;

import com.casic.patrol.evaluate.domain.Evaluation_Patrol;
import com.casic.patrol.party.persistence.domain.PartyEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Evaluation_PatrolDto {
    private Long id;
    private Double confirm_num;//核实数
    private Double should_confirm_num;//应核实数
    private Double inTime_confirm_num;//按期核实数
    private Double overTime_confirm_num;//超期未核实数
    private Double mis_report_num;//漏报数
    private String party_id;
    private String party_name;
    private String evaluateType;
    private String evaluateYear;
    private String evaluateMonth;
    private String evaluateQuarter;
    private String evaluateTime;

    private String confirm_score;
    private String inTime_confirm_score;
    private String overTime_mis_confirm_score;
    private String mis_report_score;
    private String score;
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(String evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getEvaluateYear() {
        return evaluateYear;
    }

    public void setEvaluateYear(String evaluateYear) {
        this.evaluateYear = evaluateYear;
    }

    public String getEvaluateMonth() {
        return evaluateMonth;
    }

    public void setEvaluateMonth(String evaluateMonth) {
        this.evaluateMonth = evaluateMonth;
    }

    public String getEvaluateQuarter() {
        return evaluateQuarter;
    }

    public void setEvaluateQuarter(String evaluateQuarter) {
        this.evaluateQuarter = evaluateQuarter;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Double getConfirm_num() {
        return confirm_num;
    }

    public void setConfirm_num(Double confirm_num) {
        this.confirm_num = confirm_num;
    }

    public Double getShould_confirm_num() {
        return should_confirm_num;
    }

    public void setShould_confirm_num(Double should_confirm_num) {
        this.should_confirm_num = should_confirm_num;
    }

    public Double getInTime_confirm_num() {
        return inTime_confirm_num;
    }

    public void setInTime_confirm_num(Double inTime_confirm_num) {
        this.inTime_confirm_num = inTime_confirm_num;
    }

    public Double getOverTime_confirm_num() {
        return overTime_confirm_num;
    }

    public void setOverTime_confirm_num(Double overTime_confirm_num) {
        this.overTime_confirm_num = overTime_confirm_num;
    }

    public Double getMis_report_num() {
        return mis_report_num;
    }

    public void setMis_report_num(Double mis_report_num) {
        this.mis_report_num = mis_report_num;
    }

    public String getConfirm_score() {
        return confirm_score;
    }

    public void setConfirm_score(String confirm_score) {
        this.confirm_score = confirm_score;
    }

    public String getInTime_confirm_score() {
        return inTime_confirm_score;
    }

    public void setInTime_confirm_score(String inTime_confirm_score) {
        this.inTime_confirm_score = inTime_confirm_score;
    }

    public String getOverTime_mis_confirm_score() {
        return overTime_mis_confirm_score;
    }

    public void setOverTime_mis_confirm_score(String overTime_mis_confirm_score) {
        this.overTime_mis_confirm_score = overTime_mis_confirm_score;
    }

    public String getMis_report_score() {
        return mis_report_score;
    }

    public void setMis_report_score(String mis_report_score) {
        this.mis_report_score = mis_report_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static Evaluation_PatrolDto Convert(Evaluation_Patrol evaluation)
    {
        if (evaluation == null) {
            return null;
        }
        Evaluation_PatrolDto evaluationDto=new Evaluation_PatrolDto();
        evaluationDto.setId(evaluation.getId());

        Double confirm_num = evaluation.getConfirm_num();
        Double should_confirm_num = evaluation.getShould_confirm_num();
        Double inTime_confirm_num = evaluation.getInTime_confirm_num();
        Double overTime_confirm_num = evaluation.getOverTime_confirm_num();
        Double mis_report_num = evaluation.getMis_report_num();

        evaluationDto.setConfirm_num(confirm_num);
        evaluationDto.setShould_confirm_num(should_confirm_num);
        evaluationDto.setInTime_confirm_num(inTime_confirm_num);
        evaluationDto.setOverTime_confirm_num(overTime_confirm_num);
        evaluationDto.setMis_report_num(mis_report_num);

        double confirm_rate;
        double inTime_confirm_rate;
        double overTime_mis_confirm_rate;
        if(should_confirm_num == 0)
        {
            if (confirm_num!=0)
                confirm_rate = 1;
            else
                confirm_rate = 0;
        }
        else
        {
            confirm_rate=confirm_num/should_confirm_num;
        }
        if(confirm_num == 0)
        {
            if (inTime_confirm_num!=0)
                inTime_confirm_rate = 1;
            else
                inTime_confirm_rate = 0;
        }
        else
        {
            inTime_confirm_rate=inTime_confirm_num/confirm_num;
        }
        if(should_confirm_num == 0)
        {
            if (overTime_confirm_num!=0)
                overTime_mis_confirm_rate = 1;
            else
                overTime_mis_confirm_rate = 0;
        }
        else
        {
            overTime_mis_confirm_rate=overTime_confirm_num/should_confirm_num;
        }
        String confirm_score = String.format("%.2f", confirm_rate>1?40:confirm_rate*40);
        String inTime_confirm_score = String.format("%.2f", inTime_confirm_rate>1?40:inTime_confirm_rate*40);
        String overTime_mis_confirm_score = String.format("%.2f",  overTime_mis_confirm_rate>1?0:(10-overTime_mis_confirm_rate*10));
        String mis_report_score = String.format("%.2f", mis_report_num>10?0:(10-mis_report_num));
        String totalScore = String.format("%.2f",Double.valueOf(confirm_score)+
                Double.valueOf(inTime_confirm_score)+Double.valueOf(overTime_mis_confirm_score)+
                Double.valueOf(mis_report_score));

        evaluationDto.setConfirm_score(confirm_score);
        evaluationDto.setInTime_confirm_score(inTime_confirm_score);
        evaluationDto.setOverTime_mis_confirm_score(overTime_mis_confirm_score);
        evaluationDto.setMis_report_score(mis_report_score);
        evaluationDto.setScore(totalScore);

        String typeIndex = String.valueOf(evaluation.getEvaluateType());
        evaluationDto.setEvaluateType(EvaluateTypeEnum.getByIndex(typeIndex).getName());
        if (evaluation.getEvaluateType() == Integer.valueOf(EvaluateTypeEnum.YEAR.getIndex()))
        {
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
            evaluationDto.setEvaluateYear(evaluation.getEvaluateYear() == null ? "" : sdf1.format(evaluation.getEvaluateYear()));
        }
        else if(evaluation.getEvaluateType() == Integer.valueOf(EvaluateTypeEnum.MONTH.getIndex()))
        {
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
            evaluationDto.setEvaluateMonth(evaluation.getEvaluateMonth() == null ? "" : sdf1.format(evaluation.getEvaluateMonth()));
        }
        else if(evaluation.getEvaluateType() == Integer.valueOf(EvaluateTypeEnum.QUARTER.getIndex()))
        {
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
            evaluationDto.setEvaluateYear(evaluation.getEvaluateYear() == null ? "" : sdf1.format(evaluation.getEvaluateYear()));
            evaluationDto.setEvaluateQuarter(evaluation.getEvaluateQuarter());
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        evaluationDto.setEvaluateTime(evaluation.getEvaluateTime()==null?"":sdf.format(evaluation.getEvaluateTime()));

        PartyEntity partyEntity = evaluation.getPartyEntity();
        if (partyEntity!=null)
        {
            evaluationDto.setParty_id(String.valueOf(partyEntity.getId()));
            evaluationDto.setParty_name(partyEntity.getName());
        }
        evaluationDto.setMemo(evaluation.getMemo()==null?"":evaluation.getMemo());
        return evaluationDto;
    }
    public static List<Evaluation_PatrolDto> Converts(List<Evaluation_Patrol> evaluations)
    {
        List<Evaluation_PatrolDto> evaluationDtos= new ArrayList<Evaluation_PatrolDto>();
        for (Evaluation_Patrol evaluation : evaluations)
        {
            evaluationDtos.add(Evaluation_PatrolDto.Convert(evaluation));
        }
        return evaluationDtos;
    }
}
