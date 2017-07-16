package com.casic.patrol.indication.persistence.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.support.Indication;
import com.casic.patrol.util.DateUtils;
import com.casic.patrol.util.ExecInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndicationInfoManager extends HibernateEntityDao<IndicationInfo> {

    /**
     * 根据流程实例ID、任务ID、指标名称
     * 以及排除的执行人ID，获取列表，并删除
     *
     * @param instanceID
     * @param taskID
     * @param catalog
     * @param excludeRef
     * @return
     */
    public ExecInfo delIndicationsBy(String instanceID,
                                     String taskID,
                                     String catalog,
                                     String excludeRef) {
        try {
            Criteria criteria = getSession().createCriteria(IndicationInfo.class);
            criteria.add(Restrictions.eq("instanceId", instanceID));
            criteria.add(Restrictions.eq("taskId", taskID));
            criteria.add(Restrictions.eq("catalog", catalog));
            criteria.add(Restrictions.not(Restrictions.eq("ref", excludeRef)));
            List<IndicationInfo> infos = criteria.list();
            for (IndicationInfo info : infos) {
                remove(info);
            }
            return ExecInfo.succ();
        } catch (Exception e) {
            e.printStackTrace();
            return ExecInfo.fail(e.getMessage());
        }
    }

    /**
     * 根据指标种类、关联任务时间、关联人获取数量
     * 其中关联任务结束时间的节点判断主要依据end时间是否为凌晨，
     * 凌晨视为以“天”为最小单位
     *
     * @param catalog
     * @param ref
     * @param start
     * @param end
     * @return
     */
    public Integer getNumberBy(String catalog, String ref,
                               Date start, Date end) {
        try {
            Criteria criteria = getSession().createCriteria(IndicationInfo.class);
            criteria.add(Restrictions.eq("catalog", catalog));
            criteria.add(Restrictions.eq("ref", ref));
            criteria.add(Restrictions.ge("relatedTaskEndTime", start));
            if (DateUtils.isWeeHours(end)) {
                criteria.add(Restrictions.lt("relatedTaskEndTime",
                        DateUtils.getWeeHoursNextDay(end)));
            } else {
                criteria.add(Restrictions.le("relatedTaskEndTime", end));
            }
            return getCount(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 返工数计算存在特殊处理
     * @param ref
     * @param start
     * @param end
     * @param except
     * @return
     */
    public Integer getReworkNum(String ref, Date start,
                                Date end, String except) {
        try {
            Criteria criteria = getSession().createCriteria(IndicationInfo.class);
            criteria.add(Restrictions.eq("catalog", Indication.REWORK.name()));
            criteria.add(Restrictions.eq("ref", ref));
            criteria.add(Restrictions.not(Restrictions.eq("attr1", except)));
            criteria.add(Restrictions.ge("relatedTaskStartTime", start));
            if (DateUtils.isWeeHours(end)) {
                criteria.add(Restrictions.lt("relatedTaskEndTime",
                        DateUtils.getWeeHoursNextDay(end)));
            } else {
                criteria.add(Restrictions.le("relatedTaskEndTime", end));
            }
            return getCount(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取漏报数Map
     * @param start
     * @param end
     * @return map key标示地区 value标示漏报次数
     */
    public Map<String, Long> getMissingReportNum(Date start, Date end) {
        Map<String, Long> map = new HashMap<String, Long>();
        try {
            Criteria criteria = getSession().createCriteria(IndicationInfo.class);
            criteria.add(Restrictions.eq("catalog",
                    Indication.MISSING_REPORT.name()));
            criteria.add(Restrictions.ge("relatedTaskStartTime", start));
            if (DateUtils.isWeeHours(end)) {
                criteria.add(Restrictions.lt("relatedTaskEndTime",
                        DateUtils.getWeeHoursNextDay(end)));
            } else {
                criteria.add(Restrictions.le("relatedTaskEndTime", end));
            }
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.groupProperty("attr1"));
            projectionList.add(Projections.rowCount());
            criteria.setProjection(projectionList);
            List results = criteria.list();
            Iterator iterator = results.iterator();
            while(iterator.hasNext()) {
                Object[] o = (Object[]) iterator.next();
                map.put(o[0].toString(), (Long)o[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取在同一个流程中，某人的某个指标已经插入的次数
     * @param catalog
     * @param ref
     * @param instanceId
     * @return
     */
    public Integer getCountBy(String catalog, String ref, String instanceId) {
        try {
            Criteria criteria = getSession().createCriteria(IndicationInfo.class);
            criteria.add(Restrictions.eq("catalog", catalog));
            criteria.add(Restrictions.eq("ref", ref));
            criteria.add(Restrictions.eq("instanceId", instanceId));
            return getCount(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
