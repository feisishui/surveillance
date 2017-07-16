package com.casic.patrol.evaluate.dto;

import com.casic.patrol.evaluate.domain.Evaluation_CommandCenter;
import com.casic.patrol.party.persistence.domain.PartyEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Evaluation_CommandCenterDto {
    private Long id;
    private Double accreditation_num;//立案数
    private Double dispatch_confirm_num;//指派核实数
    private Double dispatch_num;//派遣数
    private Double should_dispatch_num;//应派遣数
    private Double inTime_dispatch_num;//按期派遣数
    private Double overTime_dispatch_num;//超期为派遣数
    private Double report_num;//公众上报数
    private Double valid_report_num;//有效公众上报数
    private String party_id;
    private String party_name;
    private String evaluateType;
    private String evaluateYear;
    private String evaluateMonth;
    private String evaluateQuarter;
    private String evaluateTime;

    private String dispatch_confirm_score;
    private String dispatch_dispose_score;
    private String inTime_dispatch_score;
    private String overTime_mis_dispatch_score;
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

    public Double getAccreditation_num() {
        return accreditation_num;
    }

    public void setAccreditation_num(Double accreditation_num) {
        this.accreditation_num = accreditation_num;
    }

    public Double getDispatch_confirm_num() {
        return dispatch_confirm_num;
    }

    public void setDispatch_confirm_num(Double dispatch_confirm_num) {
        this.dispatch_confirm_num = dispatch_confirm_num;
    }

    public Double getDispatch_num() {
        return dispatch_num;
    }

    public void setDispatch_num(Double dispatch_num) {
        this.dispatch_num = dispatch_num;
    }

    public Double getShould_dispatch_num() {
        return should_dispatch_num;
    }

    public void setShould_dispatch_num(Double should_dispatch_num) {
        this.should_dispatch_num = should_dispatch_num;
    }

    public Double getInTime_dispatch_num() {
        return inTime_dispatch_num;
    }

    public void setInTime_dispatch_num(Double inTime_dispatch_num) {
        this.inTime_dispatch_num = inTime_dispatch_num;
    }

    public Double getOverTime_dispatch_num() {
        return overTime_dispatch_num;
    }

    public void setOverTime_dispatch_num(Double overTime_dispatch_num) {
        this.overTime_dispatch_num = overTime_dispatch_num;
    }

    public Double getReport_num() {
        return report_num;
    }

    public void setReport_num(Double report_num) {
        this.report_num = report_num;
    }

    public Double getValid_report_num() {
        return valid_report_num;
    }

    public void setValid_report_num(Double valid_report_num) {
        this.valid_report_num = valid_report_num;
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

    public String getDispatch_confirm_score() {
        return dispatch_confirm_score;
    }

    public void setDispatch_confirm_score(String dispatch_confirm_score) {
        this.dispatch_confirm_score = dispatch_confirm_score;
    }

    public String getDispatch_dispose_score() {
        return dispatch_dispose_score;
    }

    public void setDispatch_dispose_score(String dispatch_dispose_score) {
        this.dispatch_dispose_score = dispatch_dispose_score;
    }

    public String getInTime_dispatch_score() {
        return inTime_dispatch_score;
    }

    public void setInTime_dispatch_score(String inTime_dispatch_score) {
        this.inTime_dispatch_score = inTime_dispatch_score;
    }

    public String getOverTime_mis_dispatch_score() {
        return overTime_mis_dispatch_score;
    }

    public void setOverTime_mis_dispatch_score(String overTime_mis_dispatch_score) {
        this.overTime_mis_dispatch_score = overTime_mis_dispatch_score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static Evaluation_CommandCenterDto Convert(Evaluation_CommandCenter evaluation)
    {
        if (evaluation == null) {
            return null;
        }
        Evaluation_CommandCenterDto evaluationDto=new Evaluation_CommandCenterDto();
        evaluationDto.setId(evaluation.getId());
        Double accreditation_num = evaluation.getAccreditation_num();
        Double dispatch_confirm_num = evaluation.getDispatch_confirm_num();
        Double dispatch_num = evaluation.getDispatch_num();
        Double should_dispatch_num = evaluation.getShould_dispatch_num();
        Double inTime_dispatch_num = evaluation.getInTime_dispatch_num();
        Double overTime_dispatch_num = evaluation.getOverTime_dispatch_num();
        Double report_num = evaluation.getReport_num();
        Double valid_report_num = evaluation.getValid_report_num();

        evaluationDto.setAccreditation_num(accreditation_num);
        evaluationDto.setDispatch_confirm_num(dispatch_confirm_num);
        evaluationDto.setDispatch_num(dispatch_num);
        evaluationDto.setShould_dispatch_num(should_dispatch_num);
        evaluationDto.setInTime_dispatch_num(inTime_dispatch_num);
        evaluationDto.setOverTime_dispatch_num(overTime_dispatch_num);
        evaluationDto.setReport_num(report_num);
        evaluationDto.setValid_report_num(valid_report_num);

        double dispatch_confirm_rate;
        double dispatch_dispose_rate;
        double inTime_dispatch_rate;
        double overTime_mis_dispatch_rate;
        if(report_num == 0)
        {
            if (dispatch_confirm_num!=0)
                dispatch_confirm_rate = 1;
            else
                dispatch_confirm_rate = 0;
        }
        else
        {
            dispatch_confirm_rate=dispatch_confirm_num/report_num;
        }
        if(accreditation_num == 0)
        {
            if (dispatch_num!=0)
                dispatch_dispose_rate = 1;
            else
                dispatch_dispose_rate = 0;
        }
        else
        {
            dispatch_dispose_rate=dispatch_num/accreditation_num;
        }

        if(dispatch_num == 0)
        {
            if (inTime_dispatch_num!=0)
                inTime_dispatch_rate = 1;
            else
                inTime_dispatch_rate = 0;
        }
        else
        {
            inTime_dispatch_rate=inTime_dispatch_num/dispatch_num;
        }
        if(should_dispatch_num == 0)
        {
            if (overTime_dispatch_num!=0)
                overTime_mis_dispatch_rate = 1;
            else
                overTime_mis_dispatch_rate = 0;
        }
        else
        {
            overTime_mis_dispatch_rate=overTime_dispatch_num/should_dispatch_num;
        }
        String dispatch_confirm_score = String.format("%.2f", dispatch_confirm_rate>1?30:dispatch_confirm_rate*30);
        String dispatch_dispose_score = String.format("%.2f", dispatch_dispose_rate>1?40:dispatch_dispose_rate*40);
        String inTime_dispatch_score = String.format("%.2f", inTime_dispatch_rate>1?20:inTime_dispatch_rate*20);
        String overTime_mis_dispatch_score = String.format("%.2f", overTime_mis_dispatch_rate>1?0:(10-overTime_mis_dispatch_rate*10));
        String totalScore = String.format("%.2f", Double.valueOf(dispatch_confirm_score) +
                Double.valueOf(dispatch_dispose_score) + Double.valueOf(inTime_dispatch_score) +
                Double.valueOf(overTime_mis_dispatch_score));

        evaluationDto.setDispatch_confirm_score(dispatch_confirm_score);
        evaluationDto.setDispatch_dispose_score(dispatch_dispose_score);
        evaluationDto.setInTime_dispatch_score(inTime_dispatch_score);
        evaluationDto.setOverTime_mis_dispatch_score(overTime_mis_dispatch_score);
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
    public static List<Evaluation_CommandCenterDto> Converts(List<Evaluation_CommandCenter> evaluations)
    {
        List<Evaluation_CommandCenterDto> evaluationDtos= new ArrayList<Evaluation_CommandCenterDto>();
        for (Evaluation_CommandCenter evaluation : evaluations)
        {
            evaluationDtos.add(Evaluation_CommandCenterDto.Convert(evaluation));
        }
        return evaluationDtos;
    }
}
