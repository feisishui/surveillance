package com.casic.patrol.evaluate.scheduler;

import com.casic.patrol.evaluate.domain.Evaluation_CommandCenter;
import com.casic.patrol.evaluate.domain.Evaluation_Company;
import com.casic.patrol.evaluate.domain.Evaluation_Patrol;
import com.casic.patrol.evaluate.dto.EvaluateTypeEnum;
import com.casic.patrol.evaluate.manager.EvaluationCommandCenterManager;
import com.casic.patrol.evaluate.manager.EvaluationCompanyManager;
import com.casic.patrol.evaluate.manager.EvaluationPatrolManager;
import com.casic.patrol.indication.support.IndicationResult;
import com.casic.patrol.indication.web.IndicationStatistics;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class EvaluateJob {
    private static Logger logger = LoggerFactory
            .getLogger(EvaluateJob.class);

    @Resource
    private EvaluationCommandCenterManager evaluationCommandCenterManager;
    @Resource
    private EvaluationPatrolManager evaluationPatrolManager;
    @Resource
    private EvaluationCompanyManager evaluationCompanyManager;

    @Resource
    private PartyEntityManager partyEntityManager;

    @Resource
    IndicationStatistics indicationStatistics;

    private List<PartyEntity> commandPartyEntities=new ArrayList<PartyEntity>();

    private List<PartyEntity> patrolPartyEntities=new ArrayList<PartyEntity>();

    private List<PartyEntity> companyPartyEntities=new ArrayList<PartyEntity>();

    @Scheduled(cron = "0 0 1 1 * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Transactional
    public void execute() throws Exception {
        logger.debug("start");

        commandPartyEntities = partyEntityManager.getPersonByDepartmentAndRegion("指挥中心",null);
        patrolPartyEntities = partyEntityManager.getPersonByDepartmentAndRegion("巡查部门",null);
        companyPartyEntities = partyEntityManager.getPersonByDepartmentAndRegion("专业公司",null);

        Calendar calendar = Calendar.getInstance();
//        int month = 1;
        int month = calendar.get(Calendar.MONTH)+1;
        switch (month)
        {
            case 1:
                evaluate_year();
                evaluate_quarter("四季度");
                break;
            case 4:
                evaluate_quarter("一季度");
                break;
            case 7:
                evaluate_quarter("二季度");
                break;
            case 10:
                evaluate_quarter("三季度");
                break;
        }
        evaluate_month(month);

        logger.debug("end");
    }

    private void evaluate_year() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
        String time = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));

        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.SECOND,-1);
        Date endDate = calendar.getTime();
        getCommandCenterEvaluate(startDate,endDate,"指挥中心",EvaluateTypeEnum.YEAR,"");
        getPatrolEvaluate(startDate, endDate, "巡查部门", EvaluateTypeEnum.YEAR, "");
        getCompanyEvaluate(startDate, endDate, "专业公司", EvaluateTypeEnum.YEAR, "");

    }

    private void evaluate_quarter(String quarter)  throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String time = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));

        calendar.add(Calendar.MONTH, -3);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        calendar.add(Calendar.SECOND,-1);
        Date endDate = calendar.getTime();

        getCommandCenterEvaluate(startDate,endDate,"指挥中心",EvaluateTypeEnum.QUARTER,quarter);
        getPatrolEvaluate(startDate,endDate,"巡查部门",EvaluateTypeEnum.QUARTER,quarter);
        getCompanyEvaluate(startDate,endDate,"专业公司",EvaluateTypeEnum.QUARTER,quarter);

    }

    private void evaluate_month(int month)  throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        String time = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(time));

        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND,-1);
        Date endDate = calendar.getTime();

        getCommandCenterEvaluate(startDate,endDate,"指挥中心",EvaluateTypeEnum.MONTH,"");
        getPatrolEvaluate(startDate,endDate,"巡查部门",EvaluateTypeEnum.MONTH,"");
        getCompanyEvaluate(startDate, endDate, "专业公司", EvaluateTypeEnum.MONTH, "");
    }

    private void getCommandCenterEvaluate(Date startDate,Date endDate,String department,
            EvaluateTypeEnum evaluateTypeEnum,String quarter)
    {
        if(commandPartyEntities != null)
        {
            List<Evaluation_CommandCenter> evaluation_commandCenters = new ArrayList<Evaluation_CommandCenter>();
            for (PartyEntity partyEntity:commandPartyEntities)
            {
                IndicationResult indicationResult = indicationStatistics.getBy(partyEntity,department,startDate,endDate);

                Evaluation_CommandCenter evaluation_commandCenter = new Evaluation_CommandCenter();
                evaluation_commandCenter.setAccreditation_num(indicationResult.getDispatchShouldNum());
                evaluation_commandCenter.setDispatch_confirm_num(indicationResult.getAssignNum());
                evaluation_commandCenter.setDispatch_num(indicationResult.getDispatchNum());
                evaluation_commandCenter.setShould_dispatch_num(indicationResult.getDispatchShouldNum());
                evaluation_commandCenter.setInTime_dispatch_num(indicationResult.getDispatchOntimeNum());
                evaluation_commandCenter.setOverTime_dispatch_num(indicationResult.getUndispatchOverdueNum());
                evaluation_commandCenter.setReport_num(indicationResult.getReportNum());
                evaluation_commandCenter.setValid_report_num((double)0);

                evaluation_commandCenter.setEvaluateTime(new Date());
                evaluation_commandCenter.setPartyEntity(partyEntity);
                evaluation_commandCenter.setEvaluateType(evaluateTypeEnum.getIndex());

                if (evaluateTypeEnum.getIndex() == 1)
                {
                    evaluation_commandCenter.setEvaluateYear(startDate);
                    evaluation_commandCenter.setEvaluateMonth(null);
                    evaluation_commandCenter.setEvaluateQuarter("");
                }
                else if (evaluateTypeEnum.getIndex() == 2)
                {
                    evaluation_commandCenter.setEvaluateYear(startDate);
                    evaluation_commandCenter.setEvaluateMonth(null);
                    evaluation_commandCenter.setEvaluateQuarter(quarter);
                }
                else if (evaluateTypeEnum.getIndex() == 3) {
                    evaluation_commandCenter.setEvaluateYear(null);
                    evaluation_commandCenter.setEvaluateMonth(startDate);
                    evaluation_commandCenter.setEvaluateQuarter("");
                }
                if (evaluation_commandCenter!=null)
                    evaluation_commandCenters.add(evaluation_commandCenter);
            }
            save_command_evaluate(evaluation_commandCenters);
        }
    }

    private void getPatrolEvaluate(Date startDate,Date endDate,String department,
                                          EvaluateTypeEnum evaluateTypeEnum,String quarter)
    {
        if(patrolPartyEntities != null)
        {
            List<Evaluation_Patrol> evaluation_patrols = new ArrayList<Evaluation_Patrol>();
            for (PartyEntity partyEntity:patrolPartyEntities)
            {
                IndicationResult indicationResult = indicationStatistics.getBy(partyEntity,department,startDate,endDate);

                Evaluation_Patrol evaluation_patrol = new Evaluation_Patrol();
                evaluation_patrol.setConfirm_num(indicationResult.getVerifyNum());
                evaluation_patrol.setShould_confirm_num(indicationResult.getVerifyShouldNum());
                evaluation_patrol.setInTime_confirm_num(indicationResult.getVerifyOntimeNum());
                evaluation_patrol.setOverTime_confirm_num(indicationResult.getVerifyOverdueNum());
                evaluation_patrol.setMis_report_num(indicationResult.getMissingReportNum());

                evaluation_patrol.setEvaluateTime(new Date());
                evaluation_patrol.setPartyEntity(partyEntity);
                evaluation_patrol.setEvaluateType(evaluateTypeEnum.getIndex());

                if (evaluateTypeEnum.getIndex() == 1)
                {
                    evaluation_patrol.setEvaluateYear(startDate);
                    evaluation_patrol.setEvaluateMonth(null);
                    evaluation_patrol.setEvaluateQuarter("");
                }
                else if (evaluateTypeEnum.getIndex() == 2)
                {
                    evaluation_patrol.setEvaluateYear(startDate);
                    evaluation_patrol.setEvaluateMonth(null);
                    evaluation_patrol.setEvaluateQuarter(quarter);
                }
                else if (evaluateTypeEnum.getIndex() == 3) {
                    evaluation_patrol.setEvaluateYear(null);
                    evaluation_patrol.setEvaluateMonth(startDate);
                    evaluation_patrol.setEvaluateQuarter("");
                }
                if (evaluation_patrol!=null)
                    evaluation_patrols.add(evaluation_patrol);
            }
            save_patrol_evaluate(evaluation_patrols);
        }
    }

    private void getCompanyEvaluate(Date startDate,Date endDate,String department,
                                          EvaluateTypeEnum evaluateTypeEnum,String quarter)
    {
        if(companyPartyEntities != null)
        {
            List<Evaluation_Company> evaluation_companies = new ArrayList<Evaluation_Company>();
            for (PartyEntity partyEntity:companyPartyEntities)
            {
                IndicationResult indicationResult = indicationStatistics.getBy(partyEntity,department,startDate,endDate);

                Evaluation_Company evaluation_company = new Evaluation_Company();
                evaluation_company.setDispose_num(indicationResult.getDisposeNum());
                evaluation_company.setShould_dispose_num(indicationResult.getDisposeShouldNum());
                evaluation_company.setInTime_dispose_num(indicationResult.getDisposeOntimeNum());
                evaluation_company.setOverTime_dispose_num(indicationResult.getDisposeOverdueNum());
                evaluation_company.setRework_num(indicationResult.getReworkNum());

                evaluation_company.setEvaluateTime(new Date());
                evaluation_company.setPartyEntity(partyEntity);
                evaluation_company.setEvaluateType(evaluateTypeEnum.getIndex());

                if (evaluateTypeEnum.getIndex() == 1)
                {
                    evaluation_company.setEvaluateYear(startDate);
                    evaluation_company.setEvaluateMonth(null);
                    evaluation_company.setEvaluateQuarter("");
                }
                else if (evaluateTypeEnum.getIndex() == 2)
                {
                    evaluation_company.setEvaluateYear(startDate);
                    evaluation_company.setEvaluateMonth(null);
                    evaluation_company.setEvaluateQuarter(quarter);
                }
                else if (evaluateTypeEnum.getIndex() == 3) {
                    evaluation_company.setEvaluateYear(null);
                    evaluation_company.setEvaluateMonth(startDate);
                    evaluation_company.setEvaluateQuarter("");
                }
                if (evaluation_company!=null)
                    evaluation_companies.add(evaluation_company);
            }
            save_company_evaluate(evaluation_companies);
        }
    }

    private void save_command_evaluate(List<Evaluation_CommandCenter> evaluation_commandCenters)
    {
        if(evaluation_commandCenters == null)
            return;
        for (Evaluation_CommandCenter evaluation:evaluation_commandCenters)
        {
            evaluationCommandCenterManager.save(evaluation);
        }
    }

    private void save_patrol_evaluate(List<Evaluation_Patrol> evaluation_patrols)
    {
        if(evaluation_patrols == null)
            return;
        for (Evaluation_Patrol evaluation:evaluation_patrols)
        {
            evaluationPatrolManager.save(evaluation);
        }
    }

    private void save_company_evaluate(List<Evaluation_Company> evaluation_companies)
    {
        if(evaluation_companies == null)
            return;
        for (Evaluation_Company evaluation:evaluation_companies)
        {
            evaluationCompanyManager.save(evaluation);
        }
    }

}
