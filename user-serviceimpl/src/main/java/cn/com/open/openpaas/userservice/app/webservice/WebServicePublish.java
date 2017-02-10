package cn.com.open.openpaas.userservice.app.webservice;

import javax.xml.ws.Endpoint;

/**
 * @author ws
 *
 * 发布Web Service
 */
public class WebServicePublish {

    public static void main(String[] args) {
    	 //定义WebService的发布地址，这个地址就是提供给外界访问Webervice的URL地址，URL地址格式为：http://ip:端口号/xxxx
    	 //String address = "http://192.168.1.100:8989/";这个WebService发布地址的写法是合法的
    	//String address = "http://192.168.1.100:8989/Webservice";这个WebService发布地址的是合法的
        String address = "http://10.96.5.242:8080/spring-oauth-server/Webservice";
        Endpoint.publish(address , new WebServiceImpl());
        System.out.println("webservice启动成功！");
    }
}