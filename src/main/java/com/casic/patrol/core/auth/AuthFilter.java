package com.casic.patrol.core.auth;

import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.manager.UserManager;
import com.casic.patrol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 2015/3/3.
 */
public class AuthFilter implements Filter

{

    @Resource
    private UserManager userManager;

    public final static Set<String> VISITOR_ACCESS = new HashSet<String>(Arrays.asList(
            "/reg/region-list.do", "/visitor.do",
            "/startProcessInstance.do", "/confirmStartProcess.do"
    ));
    public final static String VISITOR_USERNAME = "游客";

    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (VISITOR_ACCESS.contains(request.getServletPath())) {
            HttpSession visitorsession = request.getSession();
            if (visitorsession.getAttribute(StringUtils.SYS_USER) == null) {
                User user = userManager.getUserByName(VISITOR_USERNAME);
                if (user != null) {
                    visitorsession.setAttribute(StringUtils.SYS_USER, user);
                    visitorsession.setAttribute(StringUtils.USERID, user.getId());
                    visitorsession.setAttribute(StringUtils.USERNAME, user.getUserName());
                }
            }
            if (visitorsession.getAttribute(StringUtils.SYS_USER) != null) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        if (session == null || session.getAttribute(StringUtils.SYS_USER) == null
                || session.getAttribute(StringUtils.USERNAME).equals(VISITOR_USERNAME)) {
            logger.info(request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
