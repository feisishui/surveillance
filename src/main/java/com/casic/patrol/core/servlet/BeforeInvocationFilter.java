package com.casic.patrol.core.servlet;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeforeInvocationFilter implements Filter {
    private List<Filter> filters = new ArrayList<Filter>();

    public void setFilters(List<Filter> filters) {
        this.filters = new ArrayList<Filter>(filters);
    }

    public void destroy() {
        for (int i = filters.size(); i-- > 0;) {
            Filter filter = filters.get(i);
            filter.destroy();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        for (Filter filter : filters) {
            filter.init(config);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        ProxyFilterChain proxyFilterChain = new ProxyFilterChain();

        for (Filter filter : filters) {
            proxyFilterChain.setInvokeNextFilter(false);
            filter.doFilter(request, response, proxyFilterChain);

            if (!proxyFilterChain.isInvokeNextFilter()) {
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private static final class ProxyFilterChain implements FilterChain {
        private boolean invokeNextFilter = false;

        public void doFilter(ServletRequest request, ServletResponse response) {
            this.invokeNextFilter = true;
        }

        public boolean isInvokeNextFilter() {
            return invokeNextFilter;
        }

        public void setInvokeNextFilter(boolean invokeNextFilter) {
            this.invokeNextFilter = invokeNextFilter;
        }
    }
}
