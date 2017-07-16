package com.casic.patrol.humantask.rs;


import com.casic.patrol.api.humantask.HumanTaskConnector;
import com.casic.patrol.api.humantask.HumanTaskDTO;
import com.casic.patrol.api.tenant.TenantHolder;
import com.casic.patrol.core.auth.CurrentUserHolder;
import com.casic.patrol.core.auth.MockCurrentUserHolder;
import com.casic.patrol.core.page.Page;
import com.casic.patrol.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Path("humantask/widget")
public class HumanTaskWidgetResource {
    private HumanTaskConnector humanTaskConnector;
    private TenantHolder tenantHolder;

    @Autowired
    private HttpSession session;
    @GET
    @Path("personalTasks")
    @Produces(MediaType.TEXT_HTML)
    public String personalTasks() {
        CurrentUserHolder currentUserHolder = new MockCurrentUserHolder();
        currentUserHolder.setUserId(session.getAttribute(StringUtils.USERID).toString());
        currentUserHolder.setUsername(session.getAttribute(StringUtils.USERNAME).toString());

        String userId = currentUserHolder.getUserId();
        String tenantId = tenantHolder.getTenantId();
        Page page = humanTaskConnector.findPersonalTasks(userId, tenantId, 1,
                10);
        List<HumanTaskDTO> humanTaskDtos = (List<HumanTaskDTO>) page
                .getResult();

        StringBuilder buff = new StringBuilder();
        buff.append("<table class='table table-hover'>");
        buff.append("  <thead>");
        buff.append("    <tr>");
        buff.append("      <th>名称</th>");
        buff.append("      <th width='20%'>&nbsp;</th>");
        buff.append("    </tr>");
        buff.append("  </thead>");
        buff.append("  <tbody>");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (HumanTaskDTO humanTaskDto : humanTaskDtos) {
            buff.append("    <tr>");
            buff.append("      <td>" + humanTaskDto.getPresentationSubject()
                    + "</td>");
            buff.append("      <td>");
            buff.append("        <a href='" + ".."
                    + "/operation/task-operation-viewTaskForm.do?humanTaskId="
                    + humanTaskDto.getId()
                    + "' class='btn btn-xs btn-primary'>处理</a>");
            buff.append("      </td>");
            buff.append("    </tr>");
        }

        buff.append("  </tbody>");
        buff.append("</table>");

        return buff.toString();
    }

    @Resource
    public void setHumanTaskConnector(HumanTaskConnector humanTaskConnector) {
        this.humanTaskConnector = humanTaskConnector;
    }



    @Resource
    public void setTenantHolder(TenantHolder tenantHolder) {
        this.tenantHolder = tenantHolder;
    }
}
