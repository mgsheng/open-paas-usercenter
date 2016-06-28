package com.andaily.forLoad.web;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.andaily.forLoad.util.ComFileUtil;
import com.andaily.forLoad.util.DBHelp;
import com.andaily.forLoad.util.DBUtil;




import net.sf.json.JSONObject;

 

public class PrintTest implements Runnable 

{

   private final int sleepTime; // random sleep time for thread
   private final  Map<String, String> parameters;
   private final static Random generator = new Random();

   public PrintTest( Map<String, String> datas)
   {
	   parameters=datas;
      sleepTime = generator.nextInt(2000); 
   } 
   public void run()

	{
		String reqUrl = "http://localhost:8080/spring-oauth-server/user/userCenterPassword";
		String res = "";
		String username = "";
		try {
			res = HttpTools.doPost(reqUrl, parameters, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res != "") {
			JSONObject jsonObject = JSONObject.fromObject(res);
			String Status = "";
			int a = 0;
			Status = jsonObject.getString("status");
			a = Integer.parseInt(Status);
			if (a > 0) {
				username=parameters.get("username");
				updateOne(username);
			} else {
				username=parameters.get("username");
				updateTwo(username);
			}
		} else {
			username=parameters.get("username");
			updateTwo(username);
		}
	/*	try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	} // end method run
/**
 * 更新一条数据
 * 
 * @param eb
 * @return
 */
public void updateOne(String  username) {
    DBHelp db = null;
    Connection con = null;
    try {
        db = new DBHelp("one");
        con = db.getConnection();
        if (null != con) {
            String updateOnesql = "update bak_log2 e set e. p2 =1 where e.name= '"+username+"'" ;
            db.executeUpdate(con, updateOnesql);
        } else {
            System.out.println("updateOne-------数据库连接失败-----");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (db != null) {
            db.closeConnection(con);
        }
    }
}
/**
 * 更新一条数据
 * 
 * @param eb
 * @return
 */
public void updateTwo(String  username) {
    DBHelp db = null;
    Connection con = null;
    try {
        db = new DBHelp("one");
        con = db.getConnection();
        if (null != con) {
            String updateOnesql = "update bak_log2 e set e. p2 =2 where e.name= '"+username+"'" ;
            db.executeUpdate(con, updateOnesql);
        } else {
            System.out.println("updateOne-------数据库连接失败-----");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (db != null) {
            db.closeConnection(con);
        }
    }
}
} // end class PrintTask

