/*
 * Copyright 2011-2016 ZXC.com All right reserved. This software is the confidential and proprietary information of
 * ZXC.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with ZXC.com.
 */
package com.ms.commons.summer.web.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.ms.commons.log.ExpandLogger;
import com.ms.commons.log.LoggerFactoryWrapper;
import com.ms.commons.summer.web.servlet.mvc.ComponentMethodController;
import com.ms.commons.summer.web.view.velocity.SummerVelocityLayoutView;

/**
 * @author zxc Apr 12, 2013 4:12:17 PM
 */
public class ComponentMappingExceptionResolver extends SimpleMappingExceptionResolver {

    protected static ExpandLogger logger = LoggerFactoryWrapper.getLogger(ComponentMappingExceptionResolver.class);

    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate.
     * <p>
     * May be overridden in subclasses, in order to apply specific exception checks. Note that this template method will
     * be invoked <i>after</i> checking whether this resolved applies ("mappedHandlers" etc), so an implementation may
     * simply proceed with its actual exception handling.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for
     * example, if multipart resolution failed)
     * @param ex the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     */
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception ex) {

        // Log exception, both at debug log level and at warn level, if desired.
        if (logger.isDebugEnabled()) {
            logger.debug("Resolving exception from handler [" + handler + "]: " + ex);
        }
        logException(ex, request);

        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode.intValue());
            }
            viewName = buildName(viewName, handler);
            return getModelAndView(viewName, ex, request);
        } else {
            return null;
        }
    }

    private String buildName(String name, Object handler) {
        if (name == null || name.length() == 0) {
            return null;
        }
        // handler为空，去掉后缀
        if (handler == null) {
            int index = name.indexOf(".");
            if (index != -1) {
                name = name.substring(0, index);
            }
            return name;
        }
        if (handler instanceof ComponentMethodController) {
            ComponentMethodController cmc = (ComponentMethodController) handler;
            int index = name.indexOf(".");
            if (index != -1) {
                name = name.substring(0, index);
            }
            String nameSpace = cmc.getNameSpace();
            if (nameSpace != null && nameSpace.length() > 0) {
                nameSpace = "/" + nameSpace;
            } else {
                nameSpace = "";
            }
            return nameSpace + SummerVelocityLayoutView.DEFAULT_VIEW_DIRECTORY + name;
        }
        return name;
    }
}
