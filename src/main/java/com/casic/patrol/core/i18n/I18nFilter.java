package com.casic.patrol.core.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class I18nFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(I18nFilter.class);
    private LocaleResolver localeResolver;

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if ("GET".equalsIgnoreCase(req.getMethod())) {
            String requestedLocale = request.getParameter("locale");

            if (requestedLocale != null) {
                Locale locale;
                String[] array = requestedLocale.split("_");

                if (array.length == 2) {
                    locale = new Locale(array[0], array[1]);
                } else {
                    locale = new Locale(array[0]);
                }

                localeResolver.setLocale(req, res, locale);
                logger.debug("update locale : {}", locale);
            }
        }

        Locale locale = localeResolver.resolveLocale(req);
        logger.debug("resolve locale : {}", locale);

        if (locale == null) {
            locale = req.getLocale();
            logger.debug("req locale : {}", req.getLocale());
        }

        try {
            LocaleContextHolder.setLocale(locale);
            req.setAttribute("locale", locale);
            req.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
                    localeResolver);
            filterChain.doFilter(request, response);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    @Resource
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }
}
