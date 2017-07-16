package com.casic.patrol.evaluate.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.evaluate.domain.Evaluation_Company;
import com.casic.patrol.evaluate.dto.EvaluateTypeEnum;
import com.casic.patrol.evaluate.dto.Evaluation_CompanyDto;
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
@Service("EvaluationCompanyManager")
public class EvaluationCompanyManager extends HibernateEntityDao<Evaluation_Company> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    IndicationStatistics indicationStatistics;

    public DataTable<Evaluation_CompanyDto> queryPage(String params,String department, String type, String date1,String date2,List<PartyEntity> partyEntities) {
        DataTable<Evaluation_CompanyDto> result = new DataTable<Evaluation_CompanyDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = this.createCriteria(Evaluation_Company.class);
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
                List<Evaluation_Company> evaluation_companies= new ArrayList<Evaluation_Company>();
                if (StringUtils.isNotBlank(date1)||StringUtils.isNotBlank(date2))
                {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = sdf.parse(date1);
                    Date endDate = sdf.parse(date2);
                    List<IndicationResult> indicationResults = indicationStatistics.getBy(partyEntities,department,startDate,endDate);
                    for (IndicationResult indicationResult:indicationResults)
                    {
                        Evaluation_Company evaluation_company = new Evaluation_Company();
                        evaluation_company.setDispose_num(indicationResult.getDisposeNum());
                        evaluation_company.setShould_dispose_num(indicationResult.getDisposeShouldNum());
                        evaluation_company.setInTime_dispose_num(indicationResult.getDisposeOntimeNum());
                        evaluation_company.setOverTime_dispose_num(indicationResult.getDisposeOverdueNum());
                        evaluation_company.setRework_num(indicationResult.getReworkNum());

                        evaluation_company.setEvaluateTime(new Date());
                        evaluation_company.setPartyEntity(indicationResult.getPerson());
                        evaluation_company.setEvaluateType(EvaluateTypeEnum.any.getIndex());

                        evaluation_company.setEvaluateYear(null);
                        evaluation_company.setEvaluateMonth(null);
                        evaluation_company.setEvaluateQuarter("");
                        if (evaluation_company!=null)
                            evaluation_companies.add(evaluation_company);
                    }
                }

                List<Evaluation_CompanyDto> evaluation_companyDtos = Evaluation_CompanyDto.Converts(evaluation_companies);
                result.setAaData(evaluation_companyDtos);
                result.setiTotalDisplayRecords( evaluation_companyDtos.size());
                result.setiTotalRecords(evaluation_companyDtos.size());
                result.setsEcho(parameter.getsEcho());
                return result;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<Evaluation_CompanyDto> evaluation_commandCenterDtos = Evaluation_CompanyDto.Converts((List<Evaluation_Company>) page.getResult());
        result.setAaData(evaluation_commandCenterDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }


    public void saveEvaluation(Evaluation_Company evaluation, HttpSession session) {
        if (evaluation == null) {
            logger.error("UserInfoService->saveUserInfo ERROR,userInfo is null!");
            return;
        }

        this.getSession().saveOrUpdate(evaluation);
    }

}
