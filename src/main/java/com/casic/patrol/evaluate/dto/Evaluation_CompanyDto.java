package com.casic.patrol.evaluate.dto;

import com.casic.patrol.evaluate.domain.Evaluation_Company;
import com.casic.patrol.evaluate.domain.Evaluation_Patrol;
import com.casic.patrol.party.persistence.domain.PartyEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Evaluation_CompanyDto {
    private Long id;
    private Double dispose_num;//处置数
    private Double should_dispose_num;//应处置数
    private Double inTime_dispose_num;//按期处置数
    private Double overTime_dispose_num;//超期未处置数
    private Double rework_num;//返工数
    private String party_id;
    private String party_name;
    private String evaluateType;
    private String evaluateYear;
    private String evaluateMonth;
    private String evaluateQuarter;
    private String evaluateTime;

    private String dispose_score;
    private String inTime_dispose_score;
    private String overTime_dispose_score;
    private String rework_score;
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

    public Double getDispose_num() {
        return dispose_num;
    }

    public void setDispose_num(Double dispose_num) {
        this.dispose_num = dispose_num;
    }

    public Double getShould_dispose_num() {
        return should_dispose_num;
    }

    public void setShould_dispose_num(Double should_dispose_num) {
        this.should_dispose_num = should_dispose_num;
    }

    public Double getInTime_dispose_num() {
        return inTime_dispose_num;
    }

    public void setInTime_dispose_num(Double inTime_dispose_num) {
        this.inTime_dispose_num = inTime_dispose_num;
    }

    public Double getOverTime_dispose_num() {
        return overTime_dispose_num;
    }

    public void setOverTime_dispose_num(Double overTime_dispose_num) {
        this.overTime_dispose_num = overTime_dispose_num;
    }

    public Double getRework_num() {
        return rework_num;
    }

    public void setRework_num(Double rework_num) {
        this.rework_num = rework_num;
    }

    public String getDispose_score() {
        return dispose_score;
    }

    public void setDispose_score(String dispose_score) {
        this.dispose_score = dispose_score;
    }

    public String getInTime_dispose_score() {
        return inTime_dispose_score;
    }

    public void setInTime_dispose_score(String inTime_dispose_score) {
        this.inTime_dispose_score = inTime_dispose_score;
    }

    public String getOverTime_dispose_score() {
        return overTime_dispose_score;
    }

    public void setOverTime_dispose_score(String overTime_dispose_score) {
        this.overTime_dispose_score = overTime_dispose_score;
    }

    public String getRework_score() {
        return rework_score;
    }

    public void setRework_score(String rework_score) {
        this.rework_score = rework_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static Evaluation_CompanyDto Convert(Evaluation_Company evaluation)
    {
        if (evaluation == null) {
            return null;
        }
        Evaluation_CompanyDto evaluationDto=new Evaluation_CompanyDto();
        evaluationDto.setId(evaluation.getId());

        Double dispose_num = evaluation.getDispose_num();
        Double should_dispose_num = evaluation.getShould_dispose_num();
        Double inTime_dispose_num = evaluation.getInTime_dispose_num();
        Double overTime_dispose_num = evaluation.getOverTime_dispose_num();
        Double rework_num = evaluation.getRework_num();

        evaluationDto.setDispose_num(dispose_num);
        evaluationDto.setShould_dispose_num(should_dispose_num);
        evaluationDto.setInTime_dispose_num(inTime_dispose_num);
        evaluationDto.setOverTime_dispose_num(overTime_dispose_num);
        evaluationDto.setRework_num(rework_num);

        double dispose_rate;
        double inTime_dispose_rate;
        double overTime_dispose_rate;
        double rework_rate;
        if(should_dispose_num == 0)
        {
            if (dispose_num!=0)
                dispose_rate = 1;
            else
                dispose_rate = 0;
        }
        else
        {
            dispose_rate=dispose_num/should_dispose_num;
        }

        if(dispose_num == 0)
        {
            if (inTime_dispose_num!=0)
                inTime_dispose_rate = 1;
            else
                inTime_dispose_rate = 0;
        }
        else
        {
            inTime_dispose_rate=inTime_dispose_num/dispose_num;
        }

        if(should_dispose_num == 0)
        {
            if (overTime_dispose_num!=0)
                overTime_dispose_rate = 1;
            else
                overTime_dispose_rate = 0;
        }
        else
        {
            overTime_dispose_rate=overTime_dispose_num/should_dispose_num;
        }

        if(dispose_num == 0)
        {
            if (rework_num!=0)
                rework_rate = 1;
            else
                rework_rate = 0;
        }
        else
        {
            rework_rate=rework_num/dispose_num;
        }

        String dispose_score = String.format("%.2f", dispose_rate>1?40:dispose_rate*40);
        String inTime_dispose_score = String.format("%.2f", inTime_dispose_rate>1?40:inTime_dispose_rate*40);
        String overTime_dispose_score = String.format("%.2f",  overTime_dispose_rate>1?0:(10-overTime_dispose_rate*10));
        String rework_score = String.format("%.2f", rework_rate>1?0:(10-rework_rate*10));
        String totalScore = String.format("%.2f",Double.valueOf(dispose_score)+
                Double.valueOf(inTime_dispose_score)+Double.valueOf(overTime_dispose_score)+
                Double.valueOf(rework_score));

        evaluationDto.setDispose_score(dispose_score);
        evaluationDto.setInTime_dispose_score(inTime_dispose_score);
        evaluationDto.setOverTime_dispose_score(overTime_dispose_score);
        evaluationDto.setRework_score(rework_score);
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
    public static List<Evaluation_CompanyDto> Converts(List<Evaluation_Company> evaluations)
    {
        List<Evaluation_CompanyDto> evaluationDtos= new ArrayList<Evaluation_CompanyDto>();
        for (Evaluation_Company evaluation : evaluations)
        {
            evaluationDtos.add(Evaluation_CompanyDto.Convert(evaluation));
        }
        return evaluationDtos;
    }
}
