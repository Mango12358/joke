package com.fun.yzss.server;

import com.fun.yzss.resources.ResourcePackage;
import com.fun.yzss.util.RequestLogHandler;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.DispatcherType;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumSet;

/**
 * Created by fanqq on 2016/7/13.
 */
public class YzssServer extends AbstractServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Server server;

    public YzssServer() throws Exception {

    }

    @Override
    protected void init() throws Exception {
        //GetConfig
        DynamicIntProperty serverPort = DynamicPropertyFactory.getInstance().getIntProperty("server.port", 8766);
        DynamicIntProperty sessionTimeout = DynamicPropertyFactory.getInstance().getIntProperty("server.session.timeout", 60 * 5);
        DynamicIntProperty maxThreads = DynamicPropertyFactory.getInstance().getIntProperty("server.max.threads", 300);
        DynamicIntProperty minThreads = DynamicPropertyFactory.getInstance().getIntProperty("server.min.threads", 50);
        DynamicStringProperty springContextFile = DynamicPropertyFactory.getInstance().getStringProperty("server.spring.context-file", "spring-context.xml");


        //Config Jersey
        ResourceConfig config = new ResourceConfig();
//        ResourceConfig config = new YzssResourceConfig();
        config.packages(ResourcePackage.class.getPackage().getName());

        //Create and Config Jetty Request Handler
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.getSessionManager().setMaxInactiveInterval(sessionTimeout.get());
        handler.setSessionHandler(sessionHandler);

        //Add Default Servlet
        handler.setResourceBase("/src/main/www");
        handler.setWelcomeFiles(new String[]{"index.htm", "index.html", "main.jsp", "index.jsp"});
        DefaultServlet staticServlet = new DefaultServlet();
        ServletHolder staticServletHolder = new ServletHolder(staticServlet);

        //Support jsp
        supportJsp(handler);

        //Support Spring
        System.out.println(springContextFile.get());
        handler.setInitParameter("contextConfigLocation", "classpath*:" + springContextFile.get()); //+ ",classpath*:spring-context-security.xml");
        ContextLoaderListener sprintContextListener = new ContextLoaderListener();
        handler.addEventListener(sprintContextListener);

        //Support Jersey
        ServletContainer jerseyServletContainer = new ServletContainer(config);
        ServletHolder jerseyServletHolder = new ServletHolder(jerseyServletContainer);

        //Support GZip
        handler.addFilter(GzipFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST))
                .setInitParameter("mimeTypes", "application/json, application/xml,text/xml, text/html");

        //Config Servlet
        handler.addServlet(jerseyServletHolder, "/api/*");
        handler.addServlet(staticServletHolder, "/");

        // Set Statistics Handler for graceful shutdown handling
        StatisticsHandler statsHandler = new StatisticsHandler();
        statsHandler.setHandler(handler);

//        Request Log
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setHandler(statsHandler);

        //Create Jetty Server
        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads.get(), minThreads.get());
        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(serverPort.get());
        server.setConnectors(new Connector[]{connector});
        server.setHandler(requestLogHandler);
        server.setStopTimeout(30000L);
    }

    @Override
    protected void doStart() throws Exception {
        server.start();
    }

    @Override
    protected void doClose() throws Exception {
        server.stop();
    }

     private void supportJsp(ServletContextHandler handler) {
        DynamicStringProperty tempDir = DynamicPropertyFactory.getInstance().getStringProperty("server.temp-dir", ".");

        //Support jsp
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        handler.setClassLoader(jspClassLoader);
        File f = new File(tempDir.get());
        if (!f.exists()) {
            f.mkdirs();
        }
        handler.setAttribute("javax.servlet.context.tempdir", f);
        ServletHolder jspServletHolder = new ServletHolder("jsp", JspServlet.class);
        jspServletHolder.setInitOrder(0);

        handler.addServlet(jspServletHolder, "*.jsp");
    }
}
