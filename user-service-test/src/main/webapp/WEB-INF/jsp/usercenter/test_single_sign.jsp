<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>user_verify_session</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">Home</a>

<div class="panel panel-default">
    <div class="panel-body">
    	<div id="param">
    		<div class="form-group">
                <label class="col-sm-2 control-label">client_id</label>
                <div class="col-sm-10">
                    <input type="text" name="client_id" id="client_id" class="form-control" ng-model="client_id"/>
                	<p class="help-block">客户端从Oauth Server申请的client_id</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">access_token</label>
                <div class="col-sm-10">
                    <input type="text" name="access_token" id="access_token" class="form-control" ng-model="access_token"/>
                	<p class="help-block">客户端从Oauth Server 获得访问的token</p>
                </div>
            </div>    
           <br/>
           <button type="submit" onclick="verifySession()" class="btn btn-primary">验证session</button>
    	</div>
           <br/><br/>
    	<div id="userInfo">
    		<table style="width:80%;height:auto;">
    			<tr>
    				<td>guid</td><td>sourceId</td><td>userName</td><td>appId</td><td>phone</td><td>email</td>
    			</tr>
    		</table>
    	</div>
       	<div id="login">
       		<form>
       			<input id="userCenterPasswordUri" name="userCenterPasswordUri" type="hidden" value="${userCenterPasswordUri}" />
	    		<div class="form-group">
	            	<label class="col-sm-2 control-label">grant_type</label>
	               	<div class="col-sm-10">
	                    <input type="text" name="grantType" readonly="readonly" id="grant_type" class="form-control" value="client_credentials"/>
	                    <p class="help-block">固定值 'client_credentials'</p>
	                </div>
	            </div>                    
	            <div class="form-group">
	                <label class="col-sm-2 control-label">scope</label>
	                <div class="col-sm-10">
	                    <select name="scope" ng-model="scope" class="form-control">
	                        <option value="read">read</option>
	                        <option value="write">write</option>
	                        <option value="read,write">read,write</option>
	                    </select>
	                    <p class="help-block"></p>
	                </div>
	            </div>                
       			<div class="form-group">
                    <label class="col-sm-2 control-label">username</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="username" id="username" required="required" ng-model="username"/>
                        <p class="help-block">用户账号</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">password</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="password" id="password" required="required" ng-model="password"/>
                        <p class="help-block">用户密码</p>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" onclick="login()" class="btn btn-primary">登录</button>
       		</form>
       	</div>
        <span class="error" style="color:red"></span>
    </div>
</div>

<script>
    $("document").ready(function(){
    	$("#userInfo").add("#login").hide();
    	//verifySession();
    });
    function verifySession(){   
    	var verifySessionUri="${contextPath}/verifySession";
    	//var client_id="credentials-client";
    	//var access_token="4d6ea779-0d9e-4208-86a0-036010214c17";
    	var client_id=$("#client_id").val();
    	var access_token=$("#access_token").val();
    	$.post(verifySessionUri,
			{
			clientId:client_id,
			accessToken:access_token
			},
			function(data){
				if(data.status == 1){
					$("#login").hide();
					$("#param").hide();
					var tr="<tr><td>"+data.guid+"</td><td>"+data.sourceId+"</td><td>"+data.userName+"</td><td>"+data.appId+"</td><td>"+data.phone+"</td><td>"+data.email+"</td></tr>";
					$("#userInfo table").append(tr);
					$("#userInfo").show();
				}else if(data.error_code == 5){
				    $("#userInfo").hide();
				    $("#param button").hide();
				    $("#login").show();
				}else{
					$(".error").text(data.errMsg);
				}
			}
		);
    }
    function login(){
    	var userCenterPasswordUri="${contextPath}/userCenterPassword";
    	//var userCenterPasswordUriToServer="http://localhost:8080/spring-oauth-server/user/userCenterPassword";
    	//var client_id="credentials-client";
    	//var access_token="4d6ea779-0d9e-4208-86a0-036010214c17";
    	//var grant_type="client_credentials";
    	//var scope="read,write";
    	var userCenterPasswordUriToServer=$("#userCenterPasswordUri").val();
    	var client_id=$("#client_id").val();
    	var access_token=$("#access_token").val();
    	var grant_type=$("#grant_type").val();
    	var scope=$("#scope").val();
    	var username=$("#username").val();
    	var password=$("#password").val();
    	if(username == null || password == null){
    		alert("请输入账号密码");
    	}
    	$.post(userCenterPasswordUri,
    			{
    			client_id:client_id,
    			access_token:access_token,
    			grant_type:grant_type,
    			scope:scope,
    			userCenterPasswordUri:userCenterPasswordUriToServer,
    			username:username,
    			password:password
    			},
    			function(data){
    				if(data.status != 0){
    					$("#login").hide();
    					$("#param").hide();
    					var tr="<tr><td>"+data.guid+"</td><td>"+data.sourceId+"</td><td>"+data.userName+"</td><td>"+data.appId+"</td><td>"+data.phone+"</td><td>"+data.email+"</td></tr>";
    					$("#userInfo table").append(tr);
    					$("#userInfo").show();
    				}else{
    				    $(".error").text(data.errMsg);
    				}
    			}
    		);
    }
</script>

</body>
</html>