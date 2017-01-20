<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterPassword</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCenterPassword" method="post" class="form-horizontal">
                <input type="hidden" name="userCenterPasswordUri" value="${userCenterPasswordUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                            <label class="col-sm-2 control-label">client_id</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="client_id" required="required"
                                       ng-model="clientId"/>

                                <p class="help-block">客户端从 Oauth Server 申请的client_id, 有的Oauth服务器中又叫 appKey</p>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">access_token</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="access_token" required="required"
                                       ng-model="accessToken"/>

                                <p class="help-block">客户端从Oauth Server 获得访问的token</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">grant_type</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="grant_type" readonly="readonly"
                                       ng-model="grantType"/>

                                <p class="help-block">固定值 'client_credentials'</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">scope</label>

                            <div class="col-sm-10">
                                <select name="scope" id="scope" ng-model="scope" class="form-control">
                                    <option value="read">read</option>
                                    <option value="write">write</option>
                                    <option value="read,write">read,write</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">username</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="username" required="required"
                                       ng-model="username"/>

                                <p class="help-block">用户中心用户账号</p>
                            </div>
                        </div>
                       <div class="form-group">
                        <label class="col-sm-2 control-label">pwdtype</label>

                        <div class="col-sm-10">
                            <select name="pwdtype" ng-model="pwdtype" id="pwdtype"class="form-control">
                                <option value="MD5">MD5</option>
                                <option value="SHA1">SHA1</option>
                            </select>
                            <p class="help-block">用户密码类型</p>
                        </div>
                    </div>  
                        <div class="form-group">
                            <label class="col-sm-2 control-label">password</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="password" required="required"
                                       ng-model="password"/>

                                <p class="help-block">用户中心用户密码</p>
                            </div>
                        </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">session_time</label>

                        <div class="col-sm-10">
                            <input type="text" name="sessionTime" class="form-control"
                                   size="50" ng-model="sessionTime"/>

                            <p class="help-block">有效登录时间<font color="red">(可选)</font></p>
                        </div>
                    </div>
                        

                    <div class="well well-sm">
                         <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="regUri"> 
                        </div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用登录接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterPasswordUri = '${userCenterPasswordUri}';
        $scope.grantType = 'client_credentials';
        $scope.scope = 'read,write';
        $scope.pwdtype = 'MD5';
        $scope.clientId = 'b62c4d6b6d3b462d9af0194e241512dd';
        $scope.clientSecret='4c7a43395e9f4f3fa0b6639ca56bdf27';
        $scope.username='xiaoli';
        $scope.password='111';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
    function btnSubmit(){
				var clientId=$("input[name='client_id']").val();
				var accessToken=$("input[name='access_token']").val();
				var scope=$("#scope").val();
	   			var grantType=$("input[name='grant_type']").val();
	   			var username=$("input[name='username']").val();
	   			var password=$("input[name='password']").val();
	   			var userCenterPasswordUri=$("input[name='userCenterPasswordUri']").val();
	   			var pwdtype=$("#pwdtype").val();
                var sessionTime=$("input[name='sessionTime']").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(scope==''){
				    alert("请输入sourceId");
					return;
				}
				if(username==''){
				    alert("请输入username");
					return;
				}
				if(password==''){
				    alert("请输入password");
					return;
				}
				$.post("${contextPath}/userCenterReg/getSignature",
					{
					clientId:clientId,
					accessToken:accessToken,
					password:password
					},
	   				function(data){
	   					if(data.flag){
	   					    var signature=data.signature;
	   					    var timestamp=data.timestamp;
	   					    var signatureNonce=data.signatureNonce;
	   					    var pwd=data.password;
	   					    var regUri=userCenterPasswordUri+"?"+"grant_type="+grantType+"&scope="+scope+"&client_id="+clientId+"&access_token="+accessToken+"&username="+username+"&password="+pwd+"&session_time="+sessionTime+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce+"&pwdtype="+pwdtype;
	   						$("#regUri").html(regUri);
	   					}
	   					else{
	   					    jQuery("#regUri").html('无效数据，请重新申请');
	   					}
	   				}
   				);
			}
</script>

</body>
</html>