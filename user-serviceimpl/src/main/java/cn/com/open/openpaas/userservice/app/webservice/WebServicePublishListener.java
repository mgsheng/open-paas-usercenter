package cn.com.open.openpaas.userservice.app.webservice;

import javax.jws.WebService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.Endpoint;

import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;

/**
 * @author ws
 * 
 */
@WebService
public class WebServicePublishListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void contextInitialized(ServletContextEvent sce) {
    	//WebService的发布地址
    	String address = PropertiesTool.getAppPropertieByKey("app.localhost.url")+"/service/sayHi";
    	System.out.println(address);
      //发布WebService，WebServiceImpl类是WebServie接口的具体实现类
        Endpoint.publish(address , new WebServiceImpl());
        System.out.println("webservice 启动成功！");
    }
}