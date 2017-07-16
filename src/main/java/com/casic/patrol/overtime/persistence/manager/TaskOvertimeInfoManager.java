package com.casic.patrol.overtime.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.overtime.persistence.domain.OvertimeLevel;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.util.DateUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricVariableInstance;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TaskOvertimeInfoManager extends
        HibernateEntityDao<TaskOvertimeInfo> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProcessEngine processEngine;

    /**
     * 根据条件查询未完成的计时数据
     * @param processDefinedId
     * @param processInstanceId
     * @param businessKey
     * @param endTaskCode
     * @return
     */
    public List<TaskOvertimeInfo> getUnfinishedInfoBy(
            String processDefinedId,
            String processInstanceId,
            String businessKey,
            String endTaskCode) {
        Criteria criteria = this.createCriteria(TaskOvertimeInfo.class);
        criteria.add(Restrictions.eq("processDefinitionId", processDefinedId));
        criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
        criteria.add(Restrictions.eq("businessKey", businessKey));
        criteria.add(Restrictions.eq("endTaskCode", endTaskCode));
        criteria.add(Restrictions.eq("finished", false));
        return criteria.list();
    }

    /**
     * 根据条件查询
     * @param processInstanceId
     * @param startTaskID
     * @param endTaskID
     * @return
     */
    public TaskOvertimeInfo getInfoBy(
            String processInstanceId,
            String startTaskID,
            String endTaskID
    ) {
        Criteria criteria = this.createCriteria(TaskOvertimeInfo.class);
        criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
        criteria.add(Restrictions.eq("startTaskId", startTaskID));
        criteria.add(Restrictions.eq("endTaskId", endTaskID));
        criteria.add(Restrictions.eq("finished", true));
        criteria.addOrder(Order.desc("endTime"));
        return (TaskOvertimeInfo)criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * 获取所有未完成的计时数据
     * @return
     */
    public List<TaskOvertimeInfo> getUnfinishedInfos() {
        Criteria criteria = this.createCriteria(TaskOvertimeInfo.class);
        criteria.add(Restrictions.eq("finished", false));
        return criteria.list();
    }

    /**
     * 查询某流程实例中是否存在非
     * {@link com.casic.patrol.overtime.persistence.domain.OvertimeLevel#NORMAL}
     * 且未完成的计时数据
     * @param processInstanceId
     * @param businessKey
     * @return
     */
    public List<TaskOvertimeInfo> getUnfinishedBy(String processInstanceId, String businessKey) {
        Criteria criteria = this.createCriteria(TaskOvertimeInfo.class);
        criteria.add(Restrictions.eq("processInstanceId", processInstanceId));
        criteria.add(Restrictions.eq("businessKey", businessKey));
        criteria.add(Restrictions.eq("finished", false));
        return criteria.list();
    }

    /**
     * 便利工具，在TaskOvertimeInfo list中找到最严重的信息
     * @param list
     * @return
     */
    public static TaskOvertimeInfo getMostSeriousInfo(List<TaskOvertimeInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        TaskOvertimeInfo most = list.get(0);
        for (int i = 1; i < list.size(); i ++) {
            TaskOvertimeInfo temp = list.get(i);
            if (!OvertimeLevel.valueOf(most.getLevelCode()).biggerThan(OvertimeLevel.valueOf(temp.getLevelCode()))) {
                most = temp;
            }
        }
        return most;
    }

    /**
     * 获取在某段时间内超时且未处理的任务超时信息
     * 此处的时间如果为某天凌晨，则视为以天为基本单位进行时间判断
     * @param start
     * @param end
     * @param taskCode
     * @return
     */
    public List<TaskOvertimeInfo> getUnfinishedOvertimeTaskBetween(
            Date start, Date end, String taskCode) {
        try {
            Criteria criteria = getSession().
                    createCriteria(TaskOvertimeInfo.class);
            criteria.add(Restrictions.eq("startTaskCode", taskCode));
            criteria.add(Restrictions.eq("endTaskCode", taskCode));
//            criteria.add(Restrictions.ge("overTime", start));
            if (DateUtils.isWeeHours(end)) {
                Date tempEnd = DateUtils.getWeeHoursNextDay(end);
                criteria.add(Restrictions.lt("overTime", tempEnd));
                criteria.add(Restrictions.or(
                        Restrictions.eq("finished", false),
                        Restrictions.ge("endTime", tempEnd)
                ));
            } else {
                criteria.add(Restrictions.le("overTime", end));
                criteria.add(Restrictions.or(
                        Restrictions.eq("finished", false),
                        Restrictions.gt("endTime", end)
                ));
            }
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TaskOvertimeInfo>();
        }
    }

    /**
     * 获取某段时间内超期且未完成任务的数据
     * @param start 起始时间
     * @param end   终止时间
     * @param taskCode  查询关联的任务code
     * @param args  关联的人员参数key
     * @return map key为人员标示，value为对应的数据
     */
    public Map<String, Integer> getUnfinishedOvertimeTaskBy(
            Date start, Date end, String taskCode, String args
    ) {
        List<TaskOvertimeInfo> infos =
                getUnfinishedOvertimeTaskBetween(start, end, taskCode);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (TaskOvertimeInfo info : infos) {
            try {
                HistoricVariableInstance instance = processEngine
                        .getHistoryService().createHistoricVariableInstanceQuery()
                        .processInstanceId(info.getProcessInstanceId())
                        .variableName(args).singleResult();
                List<String> refs = (List<String>)instance.getValue();
                for (String ref : refs) {
                    if (map.containsKey(ref)) {
                        map.put(ref, map.get(ref) + 1);
                    } else {
                        map.put(ref, 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Chinese Log : " +
                                "查询获取某时间区间[{},{}]内" +
                                "超时且未处理的任务[{}]失败[{}]," +
                                "查询流程ID为[{}],关联人员参数为[{}].",
                        DateUtils.sdf2.format(start),
                        DateUtils.sdf2.format(end),
                        taskCode, e.getMessage(),
                        info.getProcessInstanceId(), args);
            }
        }
        return map;
    }
}
