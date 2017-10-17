<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
		<meta charset="utf-8" />
		<title>找回密码</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style3.css" />
		<link href="${pageContext.request.contextPath}/images/open_logo.ico" rel="Shortcut Icon" />
	</head>
	<body class="page_setting pwd page_introduction">
		<%@ include file="/dev_head.jsp"%>
		<!--找回密码 star-->
		<div class="layout">
			<div class="w_1200">
				<div class="getbackpwd">
					<div class="stepbar">
						<div class="first curr"><span class="num">1</span>输入用户名</div>
						<div class="second"><span class="num">2</span>选择密码找回方式</div>
						<div class="four"><span class="num">3</span>验证身份</div>
						<div class="third"><span class="num">4</span>修改密码</div>
					</div>
					<div class="getbackchoice">
							<%--<div class="phone"><a href="${pageContext.request.contextPath}/findpwdphone"><span class="txtchoice">用绑定手机找回</span></a></div>
						<div class="email"><a href="${pageContext.request.contextPath}/findpwdemail"><span class="txtchoice">用绑定邮箱找回</span></a></div>
					    <div class="problem"><a href="${pageContext.request.contextPath}/findpwdproblem"><span class="txtchoice">用密保问题找回</span></a></div> --%>
					<div class="main">
						<table cellpadding="0" cellspacing="0" class="tbl-findpwd">
							<tr>
								<td colspan="2"><span class="fts-1">请输入登录的用户名</span></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="text" id="loginUserName" class="input-len" value="" placeholder="输入登录名" onchange="changeValue()"/>
									<div class="div_error" id="verify_username_error"></div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="button" class="btn-len" style="width:200px;" value="下一步" onclick="btnSubmit()" />
								</td>
							</tr>
						</table>
					</div>
					
					</div>
				</div>
			</div>
		</div>
		<!--找回密码 end-->
		<%@ include file="/dev_foot.jsp"%>
		<script type="text/javascript">
			jQuery(document).ready(function() {
				if('${flag}' == 'false'){
					var errorCode = '${errorCode}'
					if(errorCode == 'time_out'){
						alert('该激活链接已失效，请重新申请找回');
					}
					else if(errorCode == 'invalid_email' || errorCode == 'invalid_code'){
						alert('无效的请求，请重新申请找回')
					}
				}
			});
		   function btnSubmit(){
				jQuery('.input-len,.input-short').removeClass('frm_error');
				jQuery('.div_error').html('');
				
				var loginUserName=$('#loginUserName').val();
				if(jQuery.trim(loginUserName)==''){
					jQuery('#loginUserName').addClass('frm_error');
					jQuery('#verify_username_error').html('请输入用户名');
					return;
				}
				$.post("${pageContext.request.contextPath}/dev/user/activated_loginUserName",
					{loginUserName:loginUserName},
	   				function(data){
	   					if(data.flag){
	   						window.location.href="${pageContext.request.contextPath}/activated.html?guid="+data.guid;
	   					}
	   					else{
	   					    if(data.errorCode=='invalid_data'){
	   							jQuery('#loginUserName').addClass('frm_error');
	   							jQuery('#verify_username_error').html('请输入登录的用户名');
	   						}
	   						else if(data.errorCode=='invalid_username'){
	   							jQuery('#loginUserName').addClass('frm_error');
	   							jQuery('#verify_username_error').html('用户不存在');
	   						}
	   						else if(data.errorCode=='system_error'){
	   							jQuery('#loginUserName').addClass('frm_error');
	   							jQuery('#verify_username_error').html('系统异常');
	   						}
	   					}
	   				}
   				);
			}
		</script>
	</body>
</html>