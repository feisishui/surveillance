package com.casic.patrol.overtime.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.overtime.dto.OvertimeInfoDto;
import com.casic.patrol.overtime.persistence.domain.OvertimeInfo;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import com.casic.patrol.util.ExecInfo;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OvertimeInfoManager extends HibernateEntityDao<OvertimeInfo> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询超时时间管理列表信息
     * @param params
     * @return
     */
    public DataTable<OvertimeInfoDto> pageInfo(String params) {
        DataTable<OvertimeInfoDto> result =
                new DataTable<OvertimeInfoDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.addOrder(Order.desc("id"));
        if (StringUtils.isNotBlank(parameter.getsSearch())) {
            criteria.add(Restrictions.like("regulationName", "%" + parameter.getsSearch() + "%"));
        }
        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<OvertimeInfoDto> overtimeInfoDtos = OvertimeInfoDto.Converts((List<OvertimeInfo>) page.getResult());
        result.setAaData(overtimeInfoDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }

    /**
     * 删除规则
     * @param id
     * @return
     */
    public ExecInfo deleteById(Long id) {
        if (id == null) {
            return ExecInfo.fail("删除规则需要规则ID信息");
        }
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.add(Restrictions.eq("id", id));
        if (CollectionUtils.isEmpty(criteria.list())) {
            return ExecInfo.fail("规则已被删除");
        }
        try {
            OvertimeInfo overtimeInfo =
                    (OvertimeInfo) criteria.list().get(0);
            remove(overtimeInfo);
        } catch (Exception e) {
            return ExecInfo.fail(e.getMessage());
        }
        return ExecInfo.succ();
    }

    /**
     * 根据ID获取dto
     * @param id
     * @return
     */
    public OvertimeInfoDto getDTOByID(Long id) {
        OvertimeInfo info = getById(id);
        return OvertimeInfoDto.Convert(info);
    }

    public OvertimeInfo getById(Long id) {
        if (id == null) {//参数不合法
            logger.error("ERROR,UserInfoManager's method -> getUserInfoById(Long userId) ,the parameter userId is null");
            return null;
        }
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.add(Restrictions.eq("id", id));
        OvertimeInfo info = (OvertimeInfo) criteria.uniqueResult();
        return info;
    }

    /**
     * 检查规则唯一性
     * @param overtimeInfo
     * @return
     */
    public ExecInfo checkInfoUnique(OvertimeInfo overtimeInfo) {
        if (overtimeInfo == null) return ExecInfo.fail("规则信息不能为空");
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.add(Restrictions.eq("emergencyLevel", overtimeInfo.getEmergencyLevel()));
        criteria.add(Restrictions.eq("endTaskCode", overtimeInfo.getEndTaskCode()));
        criteria.add(Restrictions.eq("eventType", overtimeInfo.getEventType()));
        criteria.add(Restrictions.eq("startTaskCode", overtimeInfo.getStartTaskCode()));
        criteria.add(Restrictions.eq("processDefinitionKey", overtimeInfo.getProcessDefinitionKey()));
        if (overtimeInfo.getId() != null) {
            criteria.add(Restrictions.not(
                    Restrictions.eq("id", overtimeInfo.getId())
            ));
        }
        List<OvertimeInfo> infos = criteria.list();
        if (!CollectionUtils.isEmpty(infos)) {
            return ExecInfo.fail("该设置与规则[" +
                    infos.get(0).getRegulationName() +
                    "]冲突");
        }
        return ExecInfo.succ();
    }

    /**
     * 检查规则名称唯一性
     * @param regulationName
     * @param id
     * @return
     */
    public ExecInfo checkRegulationNameUnique(String regulationName, Long id) {
        if (regulationName == null)
            return ExecInfo.fail("规则名称不能为空");
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.add(Restrictions.eq("regulationName", regulationName));
        if (null != id) {
            criteria.add(Restrictions.not(
                    Restrictions.eq("id", id)
            ));
        }
        List<OvertimeInfo> infos = criteria.list();
        if (!CollectionUtils.isEmpty(infos)) {
            return ExecInfo.fail("规则名称已存在");
        }
        return ExecInfo.succ();
    }

    /**
     * 根据开始任务code、流程定义key、事故种类、事故等级获取规则
     * @param startTaskCode
     * @param processDefinedId
     * @param emergencyLevel
     * @param eventType
     * @return
     */
    public List<OvertimeInfo> getInfoBy(String startTaskCode,
                                        String processDefinedId,
                                        String emergencyLevel,
                                        String eventType) {
        Criteria criteria = this.createCriteria(OvertimeInfo.class);
        criteria.add(Restrictions.eq("startTaskCode", startTaskCode));
        criteria.add(Restrictions.eq("processDefinitionKey", processDefinedId));
        criteria.add(Restrictions.eq("emergencyLevel", emergencyLevel));
        criteria.add(Restrictions.eq("eventType", eventType));
        return criteria.list();
    }
}
