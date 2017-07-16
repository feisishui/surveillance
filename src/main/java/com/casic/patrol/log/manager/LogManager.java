package com.casic.patrol.log.manager;

import com.casic.patrol.core.hibernate.HibernateEntityDao;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.util.DateUtils;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.manager.TaskInfoManager;
import com.casic.patrol.log.domain.Log;
import com.casic.patrol.log.dto.LogDTO;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import com.casic.patrol.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Created by wp on 2015/7/13.
 */
@Service
public class LogManager extends HibernateEntityDao<Log> {

    @Resource
    private TaskInfoManager taskInfoManager;

    /**
     * 普通日志记录
     * @param handle
     * @param logType
     * @return
     */
    public boolean saveLog(String handle, String logType) {
        Log sysLog = new Log();
        setUserForLog(sysLog);
        sysLog.setHandle(handle);
        sysLog.setLogType(logType);
        sysLog.setTime(new Date());
        save(sysLog);
        return true;
    }

    /**
     * 记录工作流关联操作日志
     * @param handle
     * @param logType
     * @return
     */
    public boolean saveLogRelatedWithTask(String handle, String logType, String humanTaskId) {
        Log sysLog = new Log();
        setUserForLog(sysLog);
        sysLog.setHumanTaskId(humanTaskId);
        sysLog.setHandle(handle);
        sysLog.setLogType(logType);
        sysLog.setTime(new Date());
        save(sysLog);
        return true;
    }

    /**
     * 记录工作流日志
     * @param humanTaskId
     * @return
     */
    public boolean saveLogBaseOnTask(String humanTaskId) {
        TaskInfo taskInfo = taskInfoManager.get(Long.parseLong(humanTaskId));
        Log sysLog = new Log();
        setUserForLog(sysLog);
        sysLog.setHumanTaskId(humanTaskId);
        if (taskInfo == null) {
            sysLog.setHandle("操作流程失败[提供错误任务相关编码:" + humanTaskId + "]");
            sysLog.setLogType(StringUtils.LOG_TASK_ERROR);
        } else {
            sysLog.setHandle(makeHandler(taskInfo));
            sysLog.setProcessid(taskInfo.getProcessInstanceId());
            sysLog.setLogType(StringUtils.LOG_TASK);
        }
        sysLog.setTime(new Date());
        save(sysLog);
        return true;
    }

    private String makeHandler(TaskInfo taskInfo) {
        if (taskInfo != null) {
            if ("start".equals(taskInfo.getCatalog())) {
                return "启动流程[" + taskInfo.getPresentationSubject() + "]";
            } else if ("normal".equals(taskInfo.getCatalog())) {
                return taskInfo.getAction() + "任务[" + taskInfo.getName() + "]";
            } else {
                return taskInfo.getAction() + "任务[" + taskInfo.getName() + "]";
            }
        }
        return null;
    }

    public DataTable<LogDTO> queryLogByPage(String jsonParam, String day1, String day2, Long userid) throws ParseException {
        DataTableParameter dataTableParam = DataTableUtils.getDataTableParameterByJsonParam(jsonParam);

        int start = dataTableParam.getiDisplayStart();
        int pageSize = dataTableParam.getiDisplayLength();
        int pageNo = (start / pageSize) + 1;

        Criteria criteria = getSession().createCriteria(Log.class);
        criteria.addOrder(Order.desc("dbId"));
        if (userid != null && userid > 0) {
            criteria.add(Restrictions.eq("userId", userid));
        }
        if(StringUtils.isNotBlank(dataTableParam.getsSearch())) {
            criteria.add(Restrictions.or(
                    Restrictions.like("staff", "%" + dataTableParam.getsSearch() + "%"),
                    Restrictions.like("handle", "%" + dataTableParam.getsSearch() + "%")
            ));
        }

        if(StringUtils.isNotBlank(day1)) {
            criteria.add(Restrictions.ge("time", DateUtils.sdf1.parse(day1)));
        }
        if(StringUtils.isNotBlank(day2)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.sdf1.parse(day2));
            calendar.add(Calendar.DATE, 1);
            criteria.add(Restrictions.le("time", calendar.getTime()));
        }

        Page page = pagedQuery(criteria, pageNo, pageSize);
        List<LogDTO> logDTOs = LogDTO.convertToDTOs((List<Log>) page.getResult());

        DataTable<LogDTO> dt = new DataTable<LogDTO>();
        dt.setAaData(logDTOs);
        dt.setiTotalDisplayRecords((int) page.getTotalCount());
        dt.setsEcho(dataTableParam.getsEcho());
        dt.setiTotalRecords((int) page.getTotalCount());
        return dt;
    }

    public LogDTO findLogById(Long id) {
        LogDTO logDTO = null;
        Log log = get(id);
        if (log != null) {
            logDTO = LogDTO.convertToDTO(log);
        }
        return logDTO;
    }

    private Log setUserForLog(Log log) {
        if (log == null) {
            log = new Log();
        }
        HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object userid = request.getSession().getAttribute(StringUtils.USERID);
        Object username = request.getSession().getAttribute(StringUtils.USERNAME);
        if (userid != null) {
            log.setUserId(Long.parseLong(userid.toString()));
        }
        if (username != null) {
            log.setStaff(username.toString());
        }
        return log;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

//    public Map<String, Object> expSysLogToExcel(String beginDay, String endDay, String path) throws ParseException, IOException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        StringBuilder sb = new StringBuilder(" from Log where 1=1 ");
//        if (StringUtils.isNotBlank(beginDay)) {
//            sb.append(" and time >= to_timestamp('").append(beginDay)
//                    .append(" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') ");
//        }
//        if (StringUtils.isNotBlank(endDay)) {
//            sb.append(" and time <= to_timestamp('").append(endDay)
//                    .append(" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') ");
//        }
//        Integer count = getCount("select count(*) " + sb.toString());
//        if (count > 300) {
//            map.put("success", false);
//            map.put("message", "请求数据量过大");
//            return map;
//        }
//        List<LogDTO> dtoList = LogDTO.convertToDTOs(
//                this.getSession().createQuery(sb.toString()).list()
//        );
//        String[] headers = {"编号","操作内容", "日志类型", "操作人员", "记录时间"};
//        OutputStream out = new FileOutputStream(path);
//        ExportExcel<LogDTO> ex = new ExportExcel<LogDTO>();
//        ex.exportExcel(headers, dtoList, out);
//        map.put("success", true);
//        return map;
//    }

}


