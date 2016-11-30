package cn.com.open.user.platform.manager.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.kafka.KafkaConsumer;
import cn.com.open.user.platform.manager.user.service.UserAccountBalanceService;

@Component
public class InitKafkaMessage extends HttpServlet {
	@Autowired
	private UserAccountBalanceService userAccountBalanceService;
	@Autowired
	private UserManagerDev userManagerDev;
	/**
	 * Constructor of the object.
	 */
	public InitKafkaMessage() {
		super();
		System.out.println("~~~~~~~~~~ok~~~~~~~~~~~~");
//		Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
//		thread.run();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	/*public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
		thread.run();

	}*/

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
