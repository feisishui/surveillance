package com.casic.patrol.evaluate.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.evaluate.domain.Evaluation_Patrol;
import com.casic.patrol.evaluate.dto.EvaluateTypeEnum;
import com.casic.patrol.evaluate.dto.Evaluation_PatrolDto;
import com.casic.patrol.indication.support.IndicationResult;
import com.casic.patrol.indication.web.IndicationStatistics;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2016/4/20.
 */
@Service("EvaluationPatrolManager")
public class EvaluationPatrolManager extends HibernateEntityDao<Evaluation_Patrol> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IndicationStatistics indicationStatistics;

    public DataTable<Evaluation_PatrolDto> queryPage(String params,String department, String type, String date1,String date2,List<PartyEntity> partyEntities) {
        DataTable<Evaluation_PatrolDto> result = new DataTable<Evaluation_PatrolDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = this.createCriteria(Evaluation_Patrol.class);
        criteria.addOrder(Order.desc("id"));
        if (StringUtils.isNotBlank(parameter.getsSearch())) {
            criteria.createAlias("partyEntity","partyEntity");
            criteria.add(Restrictions.like("partyEntity.name", "%" + parameter.getsSearch() + "%"));
        }
        if(partyEntities!=null&&partyEntities.size()>0)
        {
            criteria.add(Restrictions.in("partyEntity", partyEntities));
        }

        try {
            if (type.equals(EvaluateTypeEnum.YEAR.getName()))
            {
                criteria.add(Restrictions.eq("evaluateType", EvaluateTypeEnum.YEAR.getIndex()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                if(StringUtils.isNotBlank(date1))
                {
                    criteria.add(Restrictions.ge("evaluateYear", sdf.parse(date1)));
                }
                if(StringUtils.isNotBlank(date2)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(date2));
//                    calendar.add(Calendar.YEAR, 1);
                    criteria.add(Restrictions.le("evaluateYear", calendar.getTime()));
                }
            }
            else if (type.equals(EvaluateTypeEnum.MONTH.getName()))
            {
                criteria.add(Restrictions.eq("evaluateType", EvaluateTypeEnum.MONTH.getIndex()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
                if(StringUtils.isNotBlank(date1))
                {
                    criteria.add(Restrictions.ge("evaluateMonth", sdf.parse(date1)));
                }
                if(StringUtils.isNotBlank(date2)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(date2));
//                    calendar.add(Calendar.MONTH, 1);
                    criteria.add(Restrictions.le("evaluateMonth", calendar.getTime()));
                }
            }
            else if (type.equals(EvaluateTypeEnum.QUARTER.getName()))
            {
                criteria.add(Restrictions.eq("evaluateType", EvaluateTypeEnum.QUARTER.getIndex()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
                if(StringUtils.isNotBlank(date1))
                {
                    criteria.add(Restrictions.eq("evaluateYear", sdf.parse(date1)));
                }
            }
            else if (type.equals(EvaluateTypeEnum.any.getName()))
            {
                List<Evaluation_Patrol> evaluation_patrols= new ArrayList<Evaluation_Patrol>();
                if (StringUtils.isNotBlank(date1)||StringUtils.isNotBlank(date2))
                {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = sdf.parse(date1);
                    Date endDate = sdf.parse(date2);
                    List<IndicationResult> indicationResults = indicationStatistics.getBy(partyEntities,department,startDate,endDate);
                    for (IndicationResult indicationResult:indicationResults)
                    {
                        Evaluation_Patrol evaluation_patrol = new Evaluation_Patrol();
                        evaluation_patrol.setConfirm_num(indicationResult.getVerifyNum());
                        evaluation_patrol.setShould_confirm_num(indicationResult.getVerifyShouldNum());
                        evaluation_patrol.setInTime_confirm_num(indicationResult.getVerifyOntimeNum());
                        evaluation_patrol.setOverTime_confirm_num(indicationResult.getVerifyOverdueNum());
                        evaluation_patrol.setMis_report_num(indicationResult.getMissingReportNum());

                        evaluation_patrol.setEvaluateTime(new Date());
                        evaluation_patrol.setPartyEntity(indicationResult.getPerson());
                        evaluation_patrol.setEvaluateType(EvaluateTypeEnum.any.getIndex());
                        evaluation_patrol.setEvaluateYear(null);
                        evaluation_patrol.setEvaluateMonth(null);
                        evaluation_patrol.setEvaluateQuarter("");
                        if (evaluation_patrol!=null)
                            evaluation_patrols.add(evaluation_patrol);
                    }
                }

                List<Evaluation_PatrolDto> evaluation_patrolDtos = Evaluation_PatrolDto.Converts(evaluation_patrols);
                result.setAaData(evaluation_patrolDtos);
                result.setiTotalDisplayRecords( evaluation_patrolDtos.size());
                result.setiTotalRecords(evaluation_patrolDtos.size());
                result.setsEcho(parameter.getsEcho());
                return result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<Evaluation_PatrolDto> evaluation_patrolDtos = Evaluation_PatrolDto.Converts((List<Evaluation_Patrol>) page.getResult());
        result.setAaData(evaluation_patrolDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }


    public void saveEvaluation(Evaluation_Patrol evaluation, HttpSession session) {
        if (evaluation == null) {
            logger.error("UserInfoService->saveUserInfo ERROR,userInfo is null!");
            return;
        }

        this.getSession().saveOrUpdate(evaluation);
    }

}
