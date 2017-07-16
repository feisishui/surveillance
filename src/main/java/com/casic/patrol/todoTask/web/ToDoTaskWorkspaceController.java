package com.casic.patrol.todoTask.web;


import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskConstants;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.export.Exportor;
import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.core.spring.MessageHelper;
import com.casic.patrol.humantask.persistence.domain.TaskInfo;
import com.casic.patrol.humantask.persistence.manager.TaskInfoManager;
import com.casic.patrol.util.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("todotask")
public class ToDoTaskWorkspaceController {
    private static Logger logger = LoggerFactory
            .getLogger(ToDoTaskWorkspaceController.class);
    private HumanTaskConnector humanTaskConnector;
    private TenantHolder tenantHolder;



    /**
     * 待指派任务.
     */
    @RequestMapping("workspace-toBeAssignedTasks")
    public String toBeAssignedTasks(@ModelAttribute Page page,
                               @RequestParam Map<String, Object> parameterMap, Model model)
    {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findPersonalTasksByName(userId, tenantId,
                page.getPageNo(), page.getPageSize(), "指派核实");
        model.addAttribute("secondMenu", "agency");
        model.addAttribute("thirdMenu", "to_assign");
        model.addAttribute("page", page);

        return "bpm/workspace-toDoTasks";
    }

    /**
     * 待立案任务.
     */
    @RequestMapping("workspace-toBeRecordedTasks")
    public String toBeRecordedTasks(@ModelAttribute Page page,
                                    @RequestParam Map<String, Object> parameterMap, Model model)
    {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findPersonalTasksByName(userId, tenantId,
                page.getPageNo(), page.getPageSize(),"事故立案");
        model.addAttribute("secondMenu", "agency");
        model.addAttribute("thirdMenu", "to_record");
        model.addAttribute("page", page);

        return "bpm/workspace-toDoTasks";
    }

    /**
     * 待结案任务.
     */
    @RequestMapping("workspace-toBeFinishedTasks")
    public String toBeFinishedTasks(@ModelAttribute Page page,
                                    @RequestParam Map<String, Object> parameterMap, Model model)
    {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findPersonalTasksByName(userId, tenantId,
                page.getPageNo(), page.getPageSize(),"事故结案");
        model.addAttribute("secondMenu", "agency");
        model.addAttribute("thirdMenu", "to_finish");
        model.addAttribute("page", page);

        return "bpm/workspace-toDoTasks";
    }

    @RequestMapping("workspace-toDoTasks")
    public String toDoTasks(@ModelAttribute Page page, Model model)
    {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        page = humanTaskConnector.findPersonalTasksByName(userId, tenantId,
                page.getPageNo(), page.getPageSize(), null);
        model.addAttribute("page", page);
        model.addAttribute("secondMenu", "myTasks");

        return "bpm/workspace-toDoTasks";
    }

    /**
     * 领取.
     */
    @Autowired
    private HttpSession session;

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }

    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
