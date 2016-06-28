package cn.com.open.openpaas.userservice.app.domain.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import cn.com.open.openpaas.userservice.app.web.WebUtils;


public class FileUpload  extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public FileUpload() {  
        super();  
    }  
  
	@Override
    public void destroy() {  
        super.destroy();   
    }  
    
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){  
  
    }  
    
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){  
        String realDir = request.getSession().getServletContext().getRealPath("");  
        String contextpath = request.getContextPath(); 
        //获取当前项目网络地址
        String basePath = request.getScheme() + "://"  
        + request.getServerName() + ":" + request.getServerPort()  
        + contextpath + "/";  
        try {  
	        String filePath = "uploadfiles";  ;
	        String realPath = realDir+File.separator+filePath;  
	        //判断路径是否存在，不存在则创建  
	        File dir = new File(realPath);  
	        if(!dir.isDirectory()){
	        	dir.mkdir();
	        }
	        if(ServletFileUpload.isMultipartContent(request)){  
	            DiskFileItemFactory dff = new DiskFileItemFactory();  
	            dff.setRepository(dir);  
	            dff.setSizeThreshold(1024000);  
	            ServletFileUpload sfu = new ServletFileUpload(dff);  
	            FileItemIterator fii = null;  
	            fii = sfu.getItemIterator(request);  
	            String title = "";   //图片标题  
	            String url = "";    //图片地址  
	            String fileName = "";  
	            String state="SUCCESS";  
	            String realFileName="";  
	            while(fii.hasNext()){  
	                FileItemStream fis = fii.next();  
	  
	                try{  
	                    if(!fis.isFormField() && fis.getName().length()>0){  
	                        fileName = fis.getName();  
	                        Pattern reg=Pattern.compile("[.]jpg|png|jpeg|gif$");  
	                        Matcher matcher=reg.matcher(fileName);  
	                        if(!matcher.find()) {  
	                            state = "只能上传图片文件！";  
	                            break;  
	                        }  
	                        realFileName = new Date().getTime()+fileName.substring(fileName.lastIndexOf("."),fileName.length());  
	                        url = realPath+File.separator+realFileName;  
	  
	                        BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流  
	                        FileOutputStream a = new FileOutputStream(new File(url));  
	                        BufferedOutputStream output = new BufferedOutputStream(a);  
	                        Streams.copy(in, output, true);//开始把文件写到你指定的上传文件夹  
	                    }else{  
	                        String fname = fis.getFieldName();  
	  
	                        if(fname.indexOf("pictitle")!=-1){  
	                            BufferedInputStream in = new BufferedInputStream(fis.openStream());  
	                            byte c [] = new byte[10];  
	                            int n = 0;  
	                            while((n=in.read(c))!=-1){  
	                                title = new String(c,0,n);  
	                                break;  
	                            }  
	                        }  
	                    }  
	                }catch(Exception e){  
	                    e.printStackTrace();  
	                }  
	            }  
	            Map<String,Object> map = new HashMap<String, Object>();
	            map.put("webPath",basePath+filePath+"/"+realFileName);
	            map.put("message",state);
	            WebUtils.writeJson(response,JSONObject.fromObject(map));
				String header = request.getHeader("User-agent");
				//针对ie8、9无法接收json对象的处理
				if(
						header.indexOf("MSIE 8.0")>0
						|| header.indexOf("MSIE 9.0")>0
						){
					response.setContentType("text/html; charset=UTF-8");
				}
	        }
        }catch(Exception ee) {  
            ee.printStackTrace();  
        }  
          
    }  
	
	@Override
    public void init() {  
        // Put your code here  
    }  
}
