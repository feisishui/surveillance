package com.casic.patrol.evaluate.web;

import com.casic.patrol.evaluate.domain.Evaluation_CommandCenter;
import com.casic.patrol.evaluate.domain.Evaluation_Company;
import com.casic.patrol.evaluate.domain.Evaluation_Patrol;
import com.casic.patrol.evaluate.dto.EvaluateTypeEnum;
import com.casic.patrol.evaluate.dto.Evaluation_CommandCenterDto;
import com.casic.patrol.evaluate.dto.Evaluation_CompanyDto;
import com.casic.patrol.evaluate.dto.Evaluation_PatrolDto;
import com.casic.patrol.evaluate.manager.EvaluationCommandCenterManager;
import com.casic.patrol.evaluate.manager.EvaluationCompanyManager;
import com.casic.patrol.evaluate.manager.EvaluationPatrolManager;
import com.casic.patrol.indication.support.IndicationResult;
import com.casic.patrol.indication.web.IndicationStatistics;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.party.persistence.manager.PartyEntityManager;
import com.casic.patrol.party.persistence.manager.PartyStructManager;
import com.casic.patrol.party.support.PartyEntityConverter;
import com.casic.patrol.party.support.PartyEntityDTO;
import com.casic.patrol.util.DataTable;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("evaluation")
public class EvaluationController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private EvaluationCommandCenterManager evaluationCommandCenterManager;
    @Resource
    private EvaluationPatrolManager evaluationPatrolManager;
    @Resource
    private EvaluationCompanyManager evaluationCompanyManager;
    @Resource
    private PartyEntityManager partyEntityManager;
    @Resource
    private PartyStructManager partyStructManager;

    @Resource
    IndicationStatistics indicationStatistics;


    @RequestMapping("evaluation-info")
    public String evaluationInfo(@RequestParam(value = "department", required = true) String department,
                                     @RequestParam(value = "type", required = true) String type,
                                     Model model) {
        String returnJSP = "";
        if (department ==null||type==null)
            return null;
        if (department.equals("指挥中心"))
        {
            if (type.equals(EvaluateTypeEnum.YEAR.getName()))
            {
                returnJSP = "evaluate/evaluation-command-year-list";
            }
            else if (type.equals(EvaluateTypeEnum.MONTH.getName()))
            {
                returnJSP = "evaluate/evaluation-command-month-list";
            }
            else if (type.equals(EvaluateTypeEnum.QUARTER.getName()))
            {
                returnJSP = "evaluate/evaluation-command-quarter-list";
            }
            else if (type.equals(EvaluateTypeEnum.any.getName()))
            {
                returnJSP = "evaluate/evaluation-command-any-list";
            }

        }
        else if(department.equals("巡查部门"))
        {
            if (type.equals(EvaluateTypeEnum.YEAR.getName()))
            {
                returnJSP = "evaluate/evaluation-patrol-year-list";
            }
            else if (type.equals(EvaluateTypeEnum.MONTH.getName()))
            {
                returnJSP = "evaluate/evaluation-patrol-month-list";
            }
            else if (type.equals(EvaluateTypeEnum.QUARTER.getName()))
            {
                returnJSP = "evaluate/evaluation-patrol-quarter-list";
            }
            else if (type.equals(EvaluateTypeEnum.any.getName()))
            {
                returnJSP = "evaluate/evaluation-patrol-any-list";
            }
        }
        else if(department.equals("专业公司"))
        {
            PartyEntity partyEntity = partyEntityManager.getPartyEntityByName("专业公司");
            List<PartyEntity> companyPartyEntities = partyStructManager.getChilePartiesByParent(partyEntity);
            PartyEntityConverter partyEntityConverter = new PartyEntityConverter();
            List<PartyEntityDTO> companyPartyEntityDTOs = partyEntityConverter.createPartyEntityDtos(companyPartyEntities);
            model.addAttribute("companyParties", companyPartyEntityDTOs);
            if (type.equals(EvaluateTypeEnum.YEAR.getName()))
            {
                returnJSP = "evaluate/evaluation-company-year-list";
            }
            else if (type.equals(EvaluateTypeEnum.MONTH.getName()))
            {
                returnJSP = "evaluate/evaluation-company-month-list";
            }
            else if (type.equals(EvaluateTypeEnum.QUARTER.getName()))
            {
                returnJSP = "evaluate/evaluation-company-quarter-list";
            }
            else if (type.equals(EvaluateTypeEnum.any.getName()))
            {
                returnJSP = "evaluate/evaluation-company-any-list";
            }
        }
        return returnJSP;
    }
    @POST
    @RequestMapping("evaluation-info-list")
    public void evaluation_info_list(String jsonParam, HttpServletResponse response,
                     @RequestParam(value = "day1", required = false) String day1,
                     @RequestParam(value = "day2", required = false) String day2,
                     @RequestParam(value = "department", required = false) String department,
                     @RequestParam(value = "type", required = false) String type,
                     @RequestParam(value = "objectIds", required = false) Long[] entityIds) {
        try {
            Gson gson = new Gson();
            String json = "";

            List<PartyEntity> partyEntities = new ArrayList<PartyEntity>();
            if (entityIds!=null&&entityIds.length>0)
            {
                partyEntities = partyEntityManager.getPartyEntitiesByIds(entityIds);
            }

            if (department.equals("指挥中心"))
            {
                DataTable<Evaluation_CommandCenterDto> evaluationDtoDataTable = evaluationCommandCenterManager.queryPage(jsonParam, department, type,day1, day2, partyEntities);
                json = gson.toJson(evaluationDtoDataTable);
            }
            else if(department.equals("巡查部门"))
            {
                DataTable<Evaluation_PatrolDto> evaluationDtoDataTable = evaluationPatrolManager.queryPage(jsonParam,department, type,day1, day2, partyEntities);
                json = gson.toJson(evaluationDtoDataTable);
            }
            else if(department.equals("专业公司"))
            {
                DataTable<Evaluation_CompanyDto> evaluationDtoDataTable = evaluationCompanyManager.queryPage(jsonParam,department, type,day1, day2, partyEntities);
                json = gson.toJson(evaluationDtoDataTable);
            }

            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //测试
    @RequestMapping("command_year_save")
    public void saveCommandYearEvaluate(HttpServletResponse response) {
        try {
            PartyEntity partyEntity = partyEntityManager.getPartyEntityByName("许娟");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));

            calendar.add(Calendar.YEAR, -1);
            Date startDate = calendar.getTime();
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.SECOND,-1);
            Date endDate = calendar.getTime();

            IndicationResult indicationResult = indicationStatistics.getBy(partyEntity,"指挥中心",startDate,endDate);

            Evaluation_CommandCenter evaluation_commandCenter = new Evaluation_CommandCenter();
            evaluation_commandCenter.setAccreditation_num(indicationResult.getDispatchShouldNum());
            evaluation_commandCenter.setDispatch_confirm_num(indicationResult.getAssignNum());
            evaluation_commandCenter.setDispatch_num(indicationResult.getDispatchNum());
            evaluation_commandCenter.setShould_dispatch_num(indicationResult.getDispatchShouldNum());
            evaluation_commandCenter.setInTime_dispatch_num(indicationResult.getDispatchOntimeNum());
            evaluation_commandCenter.setOverTime_dispatch_num(indicationResult.getUndispatchOverdueNum());
            evaluation_commandCenter.setReport_num(indicationResult.getReportNum());
            evaluation_commandCenter.setValid_report_num((double)0);

            evaluation_commandCenter.setEvaluateMonth(null);
            evaluation_commandCenter.setEvaluateQuarter("");
            evaluation_commandCenter.setEvaluateYear(startDate);

            evaluation_commandCenter.setEvaluateTime(new Date());
            evaluation_commandCenter.setEvaluateType(EvaluateTypeEnum.YEAR.getIndex());
            evaluation_commandCenter.setPartyEntity(partyEntity);
            evaluationCommandCenterManager.save(evaluation_commandCenter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("command_quarter_save")
    public void saveCommandQuarterEvaluate(HttpServletResponse response) {
        try {
            Evaluation_CommandCenter evaluation_commandCenter = new Evaluation_CommandCenter();
            evaluation_commandCenter.setAccreditation_num((double) 1);
            evaluation_commandCenter.setDispatch_confirm_num((double) 1);
            evaluation_commandCenter.setDispatch_num((double) 1);
            evaluation_commandCenter.setShould_dispatch_num((double)1);
            evaluation_commandCenter.setInTime_dispatch_num((double) 1);
            evaluation_commandCenter.setOverTime_dispatch_num((double)1);
            evaluation_commandCenter.setReport_num((double)1);
            evaluation_commandCenter.setValid_report_num((double)1);

            evaluation_commandCenter.setEvaluateMonth(null);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH)+1;
            String time = sdf.format(new Date());
            calendar.setTime(sdf.parse(time));

            if(4<=month&&month<=6)
            {
                evaluation_commandCenter.setEvaluateQuarter("一季度");
                evaluation_commandCenter.setEvaluateYear(calendar.getTime());
            }
            else if(7<=month&&month<=9)
            {
                evaluation_commandCenter.setEvaluateQuarter("二季度");
                evaluation_commandCenter.setEvaluateYear(calendar.getTime());
            }
            else if(10<=month&&month<=12)
            {
                evaluation_commandCenter.setEvaluateQuarter("三季度");
                evaluation_commandCenter.setEvaluateYear(calendar.getTime());
            }
            else if(1<=month&&month<=3)
            {
                evaluation_commandCenter.setEvaluateQuarter("四季度");
                calendar.add(Calendar.YEAR, -1);
                evaluation_commandCenter.setEvaluateYear(calendar.getTime());
            }
            else
            {
                evaluation_commandCenter.setEvaluateQuarter("");
                evaluation_commandCenter.setEvaluateYear(null);
            }

            evaluation_commandCenter.setEvaluateTime(new Date());
            evaluation_commandCenter.setEvaluateType(EvaluateTypeEnum.QUARTER.getIndex());
            evaluation_commandCenter.setPartyEntity(partyEntityManager.getPartyEntityByName("许娟"));
            evaluationCommandCenterManager.save(evaluation_commandCenter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("command_month_save")
    public void saveCommandMonthEvaluate(HttpServletResponse response) {
        try {
            Evaluation_CommandCenter evaluation_commandCenter = new Evaluation_CommandCenter();
            evaluation_commandCenter.setAccreditation_num((double) 1);
            evaluation_commandCenter.setDispatch_confirm_num((double) 1);
            evaluation_commandCenter.setDispatch_num((double) 1);
            evaluation_commandCenter.setShould_dispatch_num((double)1);
            evaluation_commandCenter.setInTime_dispatch_num((double) 1);
            evaluation_commandCenter.setOverTime_dispatch_num((double)1);
            evaluation_commandCenter.setReport_num((double)1);
            evaluation_commandCenter.setValid_report_num((double)1);

            evaluation_commandCenter.setEvaluateYear(null);
            evaluation_commandCenter.setEvaluateQuarter("");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MONTH, -1);

            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH)+1;
            if(month == 12)
                calendar.add(Calendar.YEAR, -1);
            evaluation_commandCenter.setEvaluateMonth(calendar.getTime());

            evaluation_commandCenter.setEvaluateTime(new Date());
            evaluation_commandCenter.setEvaluateType(EvaluateTypeEnum.MONTH.getIndex());
            evaluation_commandCenter.setPartyEntity(partyEntityManager.getPartyEntityByName("许娟"));
            evaluationCommandCenterManager.save(evaluation_commandCenter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("patrol_year_save")
    public void savePatrolYearEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Patrol evaluation_patrol = new Evaluation_Patrol();
            evaluation_patrol.setConfirm_num((double) 1);
            evaluation_patrol.setShould_confirm_num((double) 1);
            evaluation_patrol.setInTime_confirm_num((double) 1);
            evaluation_patrol.setOverTime_confirm_num((double) 0);
            evaluation_patrol.setMis_report_num((double) 1);

            evaluation_patrol.setEvaluateMonth(null);
            evaluation_patrol.setEvaluateQuarter("");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.YEAR, -1);
            evaluation_patrol.setEvaluateYear(calendar.getTime());

            evaluation_patrol.setEvaluateTime(new Date());
            evaluation_patrol.setEvaluateType(EvaluateTypeEnum.YEAR.getIndex());
            evaluation_patrol.setPartyEntity(partyEntityManager.getPartyEntityByName("曾锐"));
            evaluationPatrolManager.save(evaluation_patrol);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("patrol_quarter_save")
    public void savePatrolQuarterEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Patrol evaluation_patrol = new Evaluation_Patrol();
            evaluation_patrol.setConfirm_num((double) 1);
            evaluation_patrol.setShould_confirm_num((double) 1);
            evaluation_patrol.setInTime_confirm_num((double) 1);
            evaluation_patrol.setOverTime_confirm_num((double) 0);
            evaluation_patrol.setMis_report_num((double) 1);

            evaluation_patrol.setEvaluateMonth(null);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH)+1;
            String time = sdf.format(new Date());
            calendar.setTime(sdf.parse(time));

            if(4<=month&&month<=6)
            {
                evaluation_patrol.setEvaluateQuarter("一季度");
                evaluation_patrol.setEvaluateYear(calendar.getTime());
            }
            else if(7<=month&&month<=9)
            {
                evaluation_patrol.setEvaluateQuarter("二季度");
                evaluation_patrol.setEvaluateYear(calendar.getTime());
            }
            else if(10<=month&&month<=12)
            {
                evaluation_patrol.setEvaluateQuarter("三季度");
                evaluation_patrol.setEvaluateYear(calendar.getTime());
            }
            else if(1<=month&&month<=3)
            {
                evaluation_patrol.setEvaluateQuarter("四季度");
                calendar.add(Calendar.YEAR, -1);
                evaluation_patrol.setEvaluateYear(calendar.getTime());
            }
            else
            {
                evaluation_patrol.setEvaluateQuarter("");
                evaluation_patrol.setEvaluateYear(null);
            }

            evaluation_patrol.setEvaluateTime(new Date());
            evaluation_patrol.setEvaluateType(EvaluateTypeEnum.QUARTER.getIndex());
            evaluation_patrol.setPartyEntity(partyEntityManager.getPartyEntityByName("曾锐"));
            evaluationPatrolManager.save(evaluation_patrol);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("patrol_month_save")
    public void savePatrolMonthEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Patrol evaluation_patrol = new Evaluation_Patrol();
            evaluation_patrol.setConfirm_num((double) 1);
            evaluation_patrol.setShould_confirm_num((double) 1);
            evaluation_patrol.setInTime_confirm_num((double) 1);
            evaluation_patrol.setOverTime_confirm_num((double) 0);
            evaluation_patrol.setMis_report_num((double) 1);

            evaluation_patrol.setEvaluateYear(null);
            evaluation_patrol.setEvaluateQuarter("");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MONTH, -1);

            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH)+1;
            if(month == 12)
                calendar.add(Calendar.YEAR, -1);
            evaluation_patrol.setEvaluateMonth(calendar.getTime());

            evaluation_patrol.setEvaluateTime(new Date());
            evaluation_patrol.setEvaluateType(EvaluateTypeEnum.MONTH.getIndex());
            evaluation_patrol.setPartyEntity(partyEntityManager.getPartyEntityByName("曾锐"));
            evaluationPatrolManager.save(evaluation_patrol);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("company_year_save")
    public void saveCompanyYearEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Company evaluation_company = new Evaluation_Company();
            evaluation_company.setDispose_num((double) 1);
            evaluation_company.setShould_dispose_num((double) 1);
            evaluation_company.setInTime_dispose_num((double) 1);
            evaluation_company.setOverTime_dispose_num((double) 0);
            evaluation_company.setRework_num((double) 1);

            evaluation_company.setEvaluateMonth(null);
            evaluation_company.setEvaluateQuarter("");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.YEAR, -1);
            evaluation_company.setEvaluateYear(calendar.getTime());

            evaluation_company.setEvaluateTime(new Date());
            evaluation_company.setEvaluateType(EvaluateTypeEnum.YEAR.getIndex());
            evaluation_company.setPartyEntity(partyEntityManager.getPartyEntityByName("黄可"));
            evaluationCompanyManager.save(evaluation_company);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("company_quarter_save")
    public void saveCompanyQuarterEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Company evaluation_company = new Evaluation_Company();
            evaluation_company.setDispose_num((double) 1);
            evaluation_company.setShould_dispose_num((double) 1);
            evaluation_company.setInTime_dispose_num((double) 1);
            evaluation_company.setOverTime_dispose_num((double) 0);
            evaluation_company.setRework_num((double) 1);

            evaluation_company.setEvaluateMonth(null);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH)+1;
            String time = sdf.format(new Date());
            calendar.setTime(sdf.parse(time));

            if(4<=month&&month<=6)
            {
                evaluation_company.setEvaluateQuarter("一季度");
                evaluation_company.setEvaluateYear(calendar.getTime());
            }
            else if(7<=month&&month<=9)
            {
                evaluation_company.setEvaluateQuarter("二季度");
                evaluation_company.setEvaluateYear(calendar.getTime());
            }
            else if(10<=month&&month<=12)
            {
                evaluation_company.setEvaluateQuarter("三季度");
                evaluation_company.setEvaluateYear(calendar.getTime());
            }
            else if(1<=month&&month<=3)
            {
                evaluation_company.setEvaluateQuarter("四季度");
                calendar.add(Calendar.YEAR, -1);
                evaluation_company.setEvaluateYear(calendar.getTime());
            }
            else
            {
                evaluation_company.setEvaluateQuarter("");
                evaluation_company.setEvaluateYear(null);
            }

            evaluation_company.setEvaluateTime(new Date());
            evaluation_company.setEvaluateType(EvaluateTypeEnum.QUARTER.getIndex());
            evaluation_company.setPartyEntity(partyEntityManager.getPartyEntityByName("黄可"));
            evaluationCompanyManager.save(evaluation_company);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("company_month_save")
    public void saveCompanyMonthEvaluate(HttpServletResponse response) {
        try {
            Evaluation_Company evaluation_company = new Evaluation_Company();
            evaluation_company.setDispose_num((double) 1);
            evaluation_company.setShould_dispose_num((double) 1);
            evaluation_company.setInTime_dispose_num((double) 1);
            evaluation_company.setOverTime_dispose_num((double) 0);
            evaluation_company.setRework_num((double) 1);

            evaluation_company.setEvaluateYear(null);
            evaluation_company.setEvaluateQuarter("");

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MONTH, -1);

            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH)+1;
            if(month == 12)
                calendar.add(Calendar.YEAR, -1);
            evaluation_company.setEvaluateMonth(calendar.getTime());

            evaluation_company.setEvaluateTime(new Date());
            evaluation_company.setEvaluateType(EvaluateTypeEnum.MONTH.getIndex());
            evaluation_company.setPartyEntity(partyEntityManager.getPartyEntityByName("黄可"));
            evaluationCompanyManager.save(evaluation_company);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("dateTest")
    public void dateTest(HttpServletResponse response) {
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
            String time = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(time));

            calendar.add(Calendar.YEAR, -1);
            Date startDate = calendar.getTime();
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.SECOND,-1);
            Date endDate = calendar.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
