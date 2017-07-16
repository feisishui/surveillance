package com.casic.patrol.humantask.persistence.manager;


import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.DateUtils;
import com.casic.patrol.core.util.StringUtils;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.user.dto.TaskTraceDto;
import com.casic.patrol.user.manager.UserManager;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TaskInfoManager extends HibernateEntityDao<TaskInfo> {
    private final int MAX_ITEMS = 5;

    @Resource
    private UserManager userManager;

    public List<TaskInfo>  pageQueryByLastId(String userId,String tenantId,String lastId)
    {
        String hql = "select t from TaskInfo t left join t.taskParticipants p where (t.assignee=? or p.ref=?) and t.tenantId=? and t.status='active' and t.id < ? order by t.id desc";
        List<TaskInfo> taskInfos = (List<TaskInfo>)find(hql, userId, userId, tenantId, Long.parseLong(lastId));

        if (taskInfos != null && taskInfos.size() > 5) {
            taskInfos = taskInfos.subList(0, MAX_ITEMS);
        }
        return taskInfos;
    }

    public int  getTaskNumber(String userId,String tenantId)
    {
        String hql = "select t from TaskInfo t left join t.taskParticipants p where (t.assignee=? or p.ref=?) and t.tenantId=? and t.status='active' order by t.id desc";
        List<TaskInfo> taskInfos = (List<TaskInfo>)find(hql, userId, userId, tenantId);

        if (taskInfos != null) {
            return taskInfos.size();
        }
        return 0;
    }

    public List<TaskInfo>  findFinishedTaskByLastId(String userId,String tenantId,String lastId)
    {
        String hql = "select t from TaskInfo t where t.assignee=? and t.tenantId=? and t.status='complete' and t.id < ? order by t.id desc";
        List<TaskInfo> taskInfos = (List<TaskInfo>)find(hql, userId,  tenantId, Long.parseLong(lastId));

        if (taskInfos != null && taskInfos.size() > 5) {
            taskInfos = taskInfos.subList(0, MAX_ITEMS);
        }
        return taskInfos;
    }

    public List<TaskInfo> getTaskInfoForViewHistory(String code, String processId, String tenantId) {
        String hql = "select t from TaskInfo t where t.status='complete' and t.code=? and t.processInstanceId=? and t.tenantId=? order by t.id asc";
        List<TaskInfo> taskInfos = (List<TaskInfo>)find(hql, code, processId, tenantId);
        return taskInfos;
    }


    public static final String POSITION_CHECK_TASK_ID = "taskuser-3";

    /**
     * 根据分页查询信息，该信息为已经核查的公众上报事件，提供核查任务的所有信息
     * 并按照任务结束时间排序显示
     * @param params
     * @return
     */
    public DataTable<TaskTraceDto> getTaskInfoForPosition(String params) {
        DataTable<TaskTraceDto> result =
                new DataTable<TaskTraceDto>();
        DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(params);
        int start = parameter.getiDisplayStart();
        int pageSize = parameter.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;
        Criteria criteria = getSession().createCriteria(TaskInfo.class);
        criteria.add(Restrictions.eq("status", "complete"));
        criteria.add(Restrictions.eq("code", POSITION_CHECK_TASK_ID));
        if (StringUtils.isNotBlank(parameter.getsSearch())) {
            criteria.add(Restrictions.like("businessKey", "%" + parameter.getsSearch() + "%"));
        }
        criteria.addOrder(Order.desc("completeTime"));
        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<TaskTraceDto> taskTraceDtos = this.Converts((List<TaskInfo>)page.getResult());
        result.setAaData(taskTraceDtos);
        result.setiTotalDisplayRecords((int) page.getTotalCount());
        result.setiTotalRecords((int) page.getTotalCount());
        result.setsEcho(parameter.getsEcho());
        return result;
    }

    public List<TaskTraceDto> Converts(List<TaskInfo> taskInfos) {
        if (CollectionUtils.isEmpty(taskInfos)) {
            return Collections.emptyList();
        }
        List<TaskTraceDto> taskTraceDtos = new ArrayList<TaskTraceDto>();
        for (TaskInfo taskInfo : taskInfos) {
            taskTraceDtos.add(this.Convert(taskInfo));
        }
        return taskTraceDtos;
    }

    public TaskTraceDto Convert(TaskInfo taskInfo) {
        if (taskInfo == null) {
            return null;
        }
        TaskTraceDto taskTraceDto = new TaskTraceDto();
        taskTraceDto.setId(taskInfo.getId());
        taskTraceDto.setTaskId(taskInfo.getBusinessKey());
        if (taskInfo.getAssignee()!=null&&!taskInfo.getAssignee().equals(""))
        {
            long userId = Long.parseLong(taskInfo.getAssignee());
            taskTraceDto.setConfirmUser_id(userId);
            taskTraceDto.setConfirmUser_name(userManager.getNameById(userId));
        }
        taskTraceDto.setEndTime(taskInfo.getCompleteTime() == null ? "" : DateUtils.sdf4.format(taskInfo.getCompleteTime()));
        taskTraceDto.setStartTime(taskInfo.getCreateTime() == null ? "" : DateUtils.sdf4.format(taskInfo.getCreateTime()));
        taskTraceDto.setTaskName(taskInfo.getPresentationSubject());

        return taskTraceDto;
    }
}
