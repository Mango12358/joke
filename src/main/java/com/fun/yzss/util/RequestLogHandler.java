package com.fun.yzss.util;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fanqq on 2016/7/13.
 */

public class RequestLogHandler extends HandlerWrapper {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RequestLogHandler() {
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        logger.info("[RequestLog] uri: " + uri);
        try {
            super.handle(target, baseRequest, request, response);
        }finally {
//            requestLog.log(baseRequest,(Response)response);
        }
    }
}
