package com.casic.patrol.evaluate.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.evaluate.domain.Evaluation_CommandCenter;
import com.casic.patrol.evaluate.dto.EvaluateTypeEnum;
import com.casic.patrol.evaluate.dto.Evaluation_CommandCenterDto;
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
@Service("EvaluationCommandCenterManager")
public class EvaluationCommandCenterManager extends HibernateEntityDao<Evaluation_CommandCenter> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IndicationStatistics indicationStatistics;


    public DataTable<Evaluation_CommandCenterDto> queryPage(String params,String department, String type, String date1,String date2,List<PartyEntity> partyEntities) {
        DataTable<Evaluation_CommandCenterDto> result = new DataTable<Evaluation_CommandCenterDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = this.createCriteria(Evaluation_CommandCenter.class);
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
                List<Evaluation_CommandCenter> evaluation_commandCenters= new ArrayList<Evaluation_CommandCenter>();
                if (StringUtils.isNotBlank(date1)||StringUtils.isNotBlank(date2))
                {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = sdf.parse(date1);
                    Date endDate = sdf.parse(date2);
                    List<IndicationResult> indicationResults = indicationStatistics.getBy(partyEntities,department,startDate,endDate);
                    for (IndicationResult indicationResult:indicationResults)
                    {
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
                        evaluation_commandCenter.setPartyEntity(indicationResult.getPerson());
                        evaluation_commandCenter.setEvaluateType(EvaluateTypeEnum.any.getIndex());
                        evaluation_commandCenter.setEvaluateYear(null);
                        evaluation_commandCenter.setEvaluateMonth(null);
                        evaluation_commandCenter.setEvaluateQuarter("");
                        if (evaluation_commandCenter!=null)
                            evaluation_commandCenters.add(evaluation_commandCenter);
                    }
                }

                List<Evaluation_CommandCenterDto> evaluation_commandCenterDtos = Evaluation_CommandCenterDto.Converts(evaluation_commandCenters);
                result.setAaData(evaluation_commandCenterDtos);
                result.setiTotalDisplayRecords( evaluation_commandCenterDtos.size());
                result.setiTotalRecords(evaluation_commandCenterDtos.size());
                result.setsEcho(parameter.getsEcho());
                return result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<Evaluation_CommandCenterDto> evaluation_commandCenterDtos = Evaluation_CommandCenterDto.Converts((List<Evaluation_CommandCenter>) page.getResult());
        result.setAaData(evaluation_commandCenterDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }


    public void saveEvaluation(Evaluation_CommandCenter evaluation, HttpSession session) {
        if (evaluation == null) {
            logger.error("UserInfoService->saveUserInfo ERROR,userInfo is null!");
            return;
        }

        this.getSession().saveOrUpdate(evaluation);
    }

}
