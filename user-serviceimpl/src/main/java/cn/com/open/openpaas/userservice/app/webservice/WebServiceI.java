package cn.com.open.openpaas.userservice.app.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author ws
 * 定义SEI(WebService EndPoint Interface(终端))
 */
//使用@WebService注解标注WebServiceI接口
@WebService
public interface WebServiceI {

    //使用@WebMethod注解标注WebServiceI接口中的方法
    @WebMethod
    String getpwd(String name,String sourceId);
    
    @WebMethod
    String save(String name,String pwd);
     int isExistInOne(String id );
    
}