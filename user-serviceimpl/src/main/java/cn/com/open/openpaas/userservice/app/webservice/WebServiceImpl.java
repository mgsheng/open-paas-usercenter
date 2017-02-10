package cn.com.open.openpaas.userservice.app.webservice;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.userservice.app.tools.DBHelp;
import cn.com.open.openpaas.userservice.app.tools.DBUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;


/**
 * @author ws
 * 
 */
@WebService
public class WebServiceImpl   implements WebServiceI {
	private static final Logger log = LoggerFactory.getLogger(WebServiceImpl.class);

    public String getpwd(String name,String sourceId) {
    	 String pwd="";
 	     String id="";
    	  if(name!=null&&!"".equals(name)){
    		  name=name.toLowerCase();
    		  log.info("用户："+name+" sourceid:"+sourceId+" 调用时间："+DateTools.getNow()+"调用接口");
     	     String sql="select id as id,des_password as desPassword from user_account where  user_name='"+name+"'";
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
//         	    	 int newSourceid=Integer.parseInt(sourceId)-1;
//         	    	 String newappUserSql="select source_id as sourceid from app_user where user_id="+id+" and source_id='"+String.valueOf(newSourceid)+"'" ;
//         	    	 List<Map<String, Object>> userdatas2 = read(newappUserSql); 
//         	    	 if(userdatas2!=null&&userdatas2.size()>0){
         	    	pwd=userdatas.get(0).get("desPassword").toString();
//             	     }
         	     }
     	     }
     	     
    	  }else{
    		  
    	  }
    	  log.info("接口调用成功密码为："+pwd);
        return pwd;
    }

    public String save(String name, String pwd) {
        System.out.println("WebService save "+name+"密码"+pwd);
        return "save Success";
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

   //通过数据库元数据获得服务器信息
   public  void DbMeta() {
   Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            DatabaseMetaData dbma = conn.getMetaData();
            System.out.println("----------数据库信息------------");
            System.out.println("数据库名称: " + dbma.getDatabaseProductName());
            System.out.println("驱动版本: " + dbma.getDriverVersion());
        } catch (Exception e) {
            throw new RuntimeException();
        }
   }
   /**
    * 
    * 判断3区取出的数据是否在一区库中存在根据设备ID
    * 
    * @param EquipmentId
    * @return 1:存在，2：不存在
    */
   public int isExistInOne(String id) {
       DBHelp db = null;
       Connection con = null;
       ResultSet rs = null;
       try {
           db = new DBHelp("one");
           con = db.getConnection();
           if (null != con) {
               String sqlOne = "select * from user_account where id =" + id;
               rs = db.executeQuery(con, sqlOne);
               if (rs == null) {
                   return -98;
               }
               if (rs.next()) {
                   return 1;
               } else {
                   return 2;
               }
           } else {
               System.out.println("isExistInOne-------数据库连接失败-----");
               return -97;
           }
       } catch (Exception e) {
    	   System.out.println("----isExistInOne----从一区查询数据失败"+e);
           return -99;
       } finally {
           if (db != null) {
               db.closeConnection(con);
           }
       }
   }

}