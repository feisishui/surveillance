package com.casic.patrol.core.whitelist;

import com.casic.patrol.core.servlet.UrlPatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class WhitelistFilter implements Filter {
    private static Logger logger = LoggerFactory
            .getLogger(WhitelistFilter.class);
    private String value;
    private Map<UrlPatternMatcher, List<String>> map = new HashMap<UrlPatternMatcher, List<String>>();

    public void destroy() {
    }

    public void init(FilterConfig config) throws ServletException {
        this.init();
    }

    public void init() {
        for (String line : value.split("\n")) {
            String[] array = line.split("=");
            UrlPatternMatcher urlPatternMatcher = UrlPatternMatcher
                    .create(array[0]);
            List<String> ips = new ArrayList<String>();
            ips.addAll(Arrays.asList(array[1].split(",")));

            map.put(urlPatternMatcher, ips);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String contextPath = req.getContextPath();
        String requestUri = req.getRequestURI();
        String path = requestUri.substring(contextPath.length());

        for (Map.Entry<UrlPatternMatcher, List<String>> entry : map.entrySet()) {
            UrlPatternMatcher urlPatternMatcher = entry.getKey();

            if (urlPatternMatcher.matches(path)) {
                List<String> ips = entry.getValue();

                if (!ips.contains(req.getRemoteAddr())) {
                    logger.info(req.getRemoteAddr() + " access denied");
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);

                    return;
                } else {
                    logger.info(req.getRemoteAddr() + " access "
                            + urlPatternMatcher);
                }
            }
        }

        chain.doFilter(req, res);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
