package cn.com.open.openpaas.userservice.web.api.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.DBUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;

/**
 * 查询用户密码
 */
@Controller
@RequestMapping("/user/")
public class UserCenterGetPwdController extends BaseControllerUtil{

@Autowired
private DefaultTokenServices tokenServices;
@Autowired
private RedisClientTemplate redisClient;

	private static final Logger log = LoggerFactory.getLogger(UserCenterGetPwdController.class);
		@RequestMapping("userCenterGetPassWord")
		public void getPassWord(HttpServletRequest request,HttpServletResponse response) {
	        Map<String, Object> map = new HashMap<String, Object>();
		//	String client_id = request.getParameter("client_id");
		//	String access_token = request.getParameter("access_token");
			String userName = request.getParameter("userName");
			String sourceId = request.getParameter("sourceId");
	    	 String pwd="";
	 	     String id="";
	 	     try{
	 	    //	log.info("client_id:"+client_id+"access_token:"+access_token+"userName:"+userName);
	 	    //	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
	 	    	 log.info("sourceId:"+sourceId+"userName:"+userName);
	 	    	 log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
	 	    	if (!paraMandatoryCheck(Arrays.asList(userName,sourceId))) {
	 				paraMandaChkAndReturn(3, response, "必传参数中有空值");
	 				return;
	 			}
	 			
	 		//	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+ client_id);
	 		//	map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
	 		//	if (map.get("status").equals("1")) {// client_id,access_token正确	 			
		    	  if(userName!=null&&!"".equals(userName)){
		    		  userName=userName.toLowerCase();
		    		  log.info("用户："+userName+" 调用时间："+DateTools.getNow()+"调用接口");
		     	     String sql="select id as id,des_password as desPassword from user_account where  user_name='"+userName+"'";
		     	     List<Map<String, Object>> userdatas = read(sql);
		     	    
		     	     if(userdatas!=null&&userdatas.size()>0){
		     	    	 id=userdatas.get(0).get("id").toString();
		     	     }
		     	     if(id!=null&&!"".equals(id)){
		     	     	 String appUserSql="select source_id as sourceid from app_user where user_id="+id+" and source_id='"+sourceId+"'" ;
		     	    	 List<Map<String, Object>> userdatas1 = read(appUserSql);
		     	    	 if(userdatas1!=null&&userdatas1.size()>0){
			         	    pwd=userdatas.get(0).get("desPassword").toString();
		         	     }else{
		         	    	pwd=userdatas.get(0).get("desPassword").toString();
		         	     }
		     	     }
		     	      map.clear();
		    		  map.put("status", "1");//接口返回状态：1-正确 0-错误
		    		  map.put("pwd",pwd);
		    	  } else{
		    		  paraMandaChkAndReturn(1, response, "用户不存在");
		    	  }
		    //	 }
			} catch (Exception e) {
				writeErrorJson(response, map);
			}

	 	    if (map.get("status") == "0") {
				writeErrorJson(response, map);
			} else {
				writeSuccessJson(response, map);
			}
	    	  log.info("接口调用查询密码为："+pwd);
	        return ;
	    }
	 

		  //通过结果集元数据封装List结果集
		    public static List<Map<String, Object>> read(String sql) {
		        Connection conn = null;
		        PreparedStatement ps = null;
		        ResultSet rs = null;

		        try {
		            conn = DBUtil.getConnection();
		            ps = conn.prepareStatement(sql);
		            rs = ps.executeQuery();
		            ResultSetMetaData rsmd = ps.getMetaData();
		            // 取得结果集列数
		            int columnCount = rsmd.getColumnCount();

		            // 构造泛型结果集
		            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		            Map<String, Object> data = null;

		            // 循环结果集
		            while (rs.next()) {
		                data = new HashMap<String, Object>();
		                // 每循环一条将列名和列值存入Map
		                for (int i = 1; i <= columnCount; i++) {
		                    data.put(rsmd.getColumnLabel(i), rs.getObject(rsmd
		                            .getColumnLabel(i)));
		                }
		                // 将整条数据的Map存入到List中
		                datas.add(data);
		            }
		            return datas;
		        } catch (Exception e) {
		            throw new RuntimeException();
		        } finally {
		            DBUtil.free(rs, ps, conn);
		        }
		   }
 

}