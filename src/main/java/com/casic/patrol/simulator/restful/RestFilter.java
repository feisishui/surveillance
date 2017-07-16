package com.casic.patrol.simulator.restful;


import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.core.spring.ApplicationContextHelper;
import com.casic.patrol.core.spring.DateConverter;
import com.casic.patrol.core.util.IoUtils;
import com.casic.patrol.internal.store.rs.StoreResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

public class RestFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(RestFilter.class);
    private JsonMapper jsonMapper = new JsonMapper();
    private DateConverter dateConverter = new DateConverter();

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    private static final String REQUEST_ATTRIBUTES_ATTRIBUTE =
            RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";
    private void Post(HttpServletRequest request) {
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
        LocaleContextHolder.setLocale(request.getLocale());
        RequestContextHolder.setRequestAttributes(attributes);
    }
    private void After(HttpServletRequest request) {
        ServletRequestAttributes attributes = null;
        Object reqAttr = request.getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
        if (reqAttr instanceof ServletRequestAttributes) {
            attributes = (ServletRequestAttributes) reqAttr;
        }
        RequestAttributes threadAttributes = RequestContextHolder.getRequestAttributes();
        if (threadAttributes != null) {
            LocaleContextHolder.resetLocaleContext();
            RequestContextHolder.resetRequestAttributes();
            if (attributes == null && threadAttributes instanceof ServletRequestAttributes) {
                attributes = (ServletRequestAttributes) threadAttributes;
            }
        }
        if (attributes != null) {
            attributes.requestCompleted();
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        Post(request);

        ApplicationContext ctx = ApplicationContextHelper
                .getApplicationContext();
        Map<String, Object> map = ctx.getBeansWithAnnotation(Path.class);

        for (Object object : map.values()) {
            Class clz = object.getClass();

            for (Method method : clz.getDeclaredMethods()) {
                if (this.matches(request, method)) {
                    Map<String, String> parameters = parseBody(request
                            .getInputStream());
                    this.invokeMethod(request, response, object, method,
                            parameters);

                    return;
                }
            }
        }
        After(request);

        filterChain.doFilter(req, res);
    }

    public Map<String, String> parseBody(InputStream inputStream)
            throws IOException {
        String body = IoUtils.readString(inputStream);
        // body = URLDecoder.decode(body, "UTF-8");
        logger.debug("body : {}", body);

        Map<String, String> parameters = new HashMap<String, String>();

        for (String text : body.split("&")) {
            logger.debug("text : {}", text);

            if (text.indexOf("=") == -1) {
                continue;
            }

            int index = text.indexOf("=");
            String key = text.substring(0, index);
            String value = text.substring(index + 1);

            if (value != null) {
                value = URLDecoder.decode(value, "UTF-8");
            }

            parameters.put(key, value);
        }

        return parameters;
    }

    public Map<String, String> getMetaData(Annotation[] annotations) {
        Map<String, String> metaData = new HashMap<String, String>();

        for (Annotation annotation : annotations) {
            String name = null;
            String type = null;
            String value = null;
            String defaultValue = null;

            if (annotation instanceof PathParam) {
                name = ((PathParam) annotation).value();
                type = "path";
                metaData.put("name", name);
                metaData.put("type", type);

                // value = this.getPathParam(request, name, method);
            } else if (annotation instanceof QueryParam) {
                name = ((QueryParam) annotation).value();
                type = "query";
                metaData.put("name", name);
                metaData.put("type", type);

                // value = request.getParameter(name);
            } else if (annotation instanceof FormParam) {
                name = ((FormParam) annotation).value();
                type = "form";
                metaData.put("name", name);
                metaData.put("type", type);

                // value = parameters.get(name);
            } else if (annotation instanceof HeaderParam) {
                name = ((HeaderParam) annotation).value();
                type = "header";
                metaData.put("name", name);
                metaData.put("type", type);

                // value = request.getHeader(name);
            } else if (annotation instanceof DefaultValue) {
                defaultValue = ((DefaultValue) annotation).value();
                metaData.put("defaultValue", defaultValue);
            }
        }

        return metaData;
    }

    public void invokeMethod(HttpServletRequest request,
            HttpServletResponse response, Object object, Method method,
            Map<String, String> parameters) {
        try {
            List arguments = new ArrayList();

            Annotation[][] annotationArray = method.getParameterAnnotations();
            Class[] parameterTypeArray = method.getParameterTypes();

            for (int i = 0; i < annotationArray.length; i++) {
                if (parameterTypeArray[i]
                        .isAssignableFrom(HttpServletRequest.class)) {
                    arguments.add(request);

                    continue;
                }

                if (parameterTypeArray[i]
                        .isAssignableFrom(HttpServletResponse.class)) {
                    arguments.add(response);

                    continue;
                }

                Annotation[] annotations = annotationArray[i];
                Map<String, String> metaData = this.getMetaData(annotations);

                String name = metaData.get("name");
                String value = null;

                if ("path".equals(metaData.get("type"))) {
                    value = this.getPathParam(request, name, method);
                } else if ("query".equals(metaData.get("type"))) {
                    value = request.getParameter(name);
                } else if ("form".equals(metaData.get("type"))) {
                    value = parameters.get(name);
                } else if ("header".equals(metaData.get("type"))) {
                    value = request.getHeader(name);
                }

                if ((value == null) && metaData.containsKey("defaultValue")) {
                    value = metaData.get("defaultValue");
                }

                if (value != null) {
                    if (parameterTypeArray[i] == String.class) {
                        arguments.add(value);
                    } else if ((parameterTypeArray[i] == Boolean.class)
                            || (parameterTypeArray[i] == boolean.class)) {
                        if (value.length() == 0) {
                            arguments.add(false);
                        } else {
                            arguments.add(Boolean.valueOf(value));
                        }
                    } else if ((parameterTypeArray[i] == Long.class)
                            || (parameterTypeArray[i] == long.class)) {
                        if (value.length() == 0) {
                            arguments.add(0L);
                        } else {
                            arguments.add(Long.parseLong(value));
                        }
                    } else if ((parameterTypeArray[i] == Integer.class)
                            || (parameterTypeArray[i] == int.class)) {
                        if (value.length() == 0) {
                            arguments.add(0);
                        } else {
                            arguments.add(Integer.parseInt(value));
                        }
                    } else if (parameterTypeArray[i] == Date.class) {
                        if (value.length() == 0) {
                            arguments.add(null);
                        } else {
                            arguments.add(dateConverter.convert(value));
                        }
                    } else {
                        throw new IllegalArgumentException("unsupport type : "
                                + parameterTypeArray[i]);
                    }
                } else {
                    if (parameterTypeArray[i] == boolean.class) {
                        arguments.add(false);
                    } else if (parameterTypeArray[i] == char.class) {
                        arguments.add((char) 0);
                    } else if (parameterTypeArray[i] == byte.class) {
                        arguments.add((byte) 0);
                    } else if (parameterTypeArray[i] == short.class) {
                        arguments.add((short) 0);
                    } else if (parameterTypeArray[i] == int.class) {
                        arguments.add(0);
                    } else if (parameterTypeArray[i] == long.class) {
                        arguments.add(0L);
                    } else if (parameterTypeArray[i] == float.class) {
                        arguments.add(0F);
                    } else if (parameterTypeArray[i] == double.class) {
                        arguments.add(0D);
                    } else if (parameterTypeArray[i] == String.class) {
                        arguments.add(null);
                    } else {
                        String message = "cannot process method argument, index is : "
                                + i + ", name : " + name + ", value is null";
                        throw new IllegalArgumentException(message);
                    }
                }
            }

            logger.debug("{}, {}, {}", object, method, arguments);

            Object result = method.invoke(object, arguments.toArray());

            if (result == null) {
                if (!checkPass(method, object)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                return;
            }

            if (result instanceof String) {
                response.setContentType(MediaType.TEXT_HTML);
                response.getOutputStream().write(
                        ((String) result).getBytes("UTF-8"));
            } else if (result instanceof InputStream) {
                Produces produces = method.getAnnotation(Produces.class);
                String contentType = MediaType.APPLICATION_OCTET_STREAM;

                if (produces != null) {
                    String[] values = produces.value();

                    if ((values != null) && (values.length > 0)) {
                        contentType = values[0];
                    }
                }

                response.setContentType(contentType);
                IoUtils.copyStream((InputStream) result,
                        response.getOutputStream());
            } else {
                response.setContentType(MediaType.APPLICATION_JSON);
                response.getOutputStream().write(
                        jsonMapper.toJson(result).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private boolean checkPass(Method method, Object o) {
        if (o.getClass().equals(StoreResource.class)) {
            return true;
        }
        return false;
    }

    public boolean matches(HttpServletRequest request, Method method) {
        if ("GET".equalsIgnoreCase(request.getMethod())
                && (method.getAnnotation(GET.class) == null)) {
            return false;
        }

        if ("POST".equalsIgnoreCase(request.getMethod())
                && (method.getAnnotation(POST.class) == null)) {
            return false;
        }

        if ("PUT".equalsIgnoreCase(request.getMethod())
                && (method.getAnnotation(PUT.class) == null)) {
            return false;
        }

        if ("DELETE".equalsIgnoreCase(request.getMethod())
                && (method.getAnnotation(DELETE.class) == null)) {
            return false;
        }

        logger.debug("{}", request.getRequestURI());

        String prefix = request.getContextPath() + "/rs";
        String requestPath = request.getRequestURI().substring(prefix.length());
        logger.debug("{}", requestPath);

        String path = "/";

        if (method.getDeclaringClass().getAnnotation(Path.class) != null) {
            path += method.getDeclaringClass().getAnnotation(Path.class)
                    .value();
        }

        if (method.getAnnotation(Path.class) != null) {
            path += ("/" + method.getAnnotation(Path.class).value());
        }

        logger.debug("{}, {}", method, path);

        String[] src = requestPath.split("/");
        String[] dest = path.split("/");

        if (src.length != dest.length) {
            return false;
        }

        for (int i = 0; i < src.length; i++) {
            if (!this.matchPath(src[i], dest[i])) {
                return false;
            }
        }

        return true;
    }

    public boolean matchPath(String src, String dest) {
        if (src.equals(dest)) {
            return true;
        }

        if (dest.startsWith("{") && dest.endsWith("}")) {
            return true;
        }

        return false;
    }

    public String getPathParam(HttpServletRequest request, String name,
            Method method) {
        String prefix = request.getContextPath() + "/rs";
        String requestPath = request.getRequestURI().substring(prefix.length());

        String path = "/";

        if (method.getDeclaringClass().getAnnotation(Path.class) != null) {
            path += method.getDeclaringClass().getAnnotation(Path.class)
                    .value();
        }

        if (method.getAnnotation(Path.class) != null) {
            path += ("/" + method.getAnnotation(Path.class).value());
        }

        String[] src = requestPath.split("/");
        String[] dest = path.split("/");

        for (int i = 0; i < dest.length; i++) {
            if (dest[i].equals("{" + name + "}")) {
                return src[i];
            }
        }

        return null;
    }
}
