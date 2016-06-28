<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterModiPwd</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCenterModiPwd" method="post" class="form-horizontal">
                <input type="hidden" name="userCenterModiPwdUri" value="${userCenterModiPwdUri}"/>

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

                                <p class="help-block">客户端从 Oauth Server 获得访问的token</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">account</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="account" required="required"
                                       ng-model="account"/>

                                <p class="help-block">登录帐号</p>
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
                            <label class="col-sm-2 control-label">old_pwd</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="old_pwd" required="required"
                                       ng-model="oldPwd"/>

                                <p class="help-block">旧登录密码</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">new_pwd</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="new_pwd" required="required"
                                       ng-model="newPwd"/>

                                <p class="help-block">新登录密码</p>
                            </div>
                        </div>               
                      <div class="form-group">
                        <label class="col-sm-2 control-label">isValidate</label>

                        <div class="col-sm-10">
                            <select name="isValidate" ng-model="isValidate" id="isValidate"class="form-control">
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                            <p class="help-block">是否验证密码规则</p>
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
                <button type="submit" class="btn btn-primary">去调用修改密码接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterModiPwdUri = '${userCenterModiPwdUri}';
        $scope.clientId='client-unity';
        $scope.accessToken = 'e23b86ea-1d4d-427c-a3da-c13a56c87192';
        $scope.account = 'xiaoli';
		$scope.pwdtype = 'MD5';
        $scope.oldPwd = '111';
        $scope.newPwd='111111';
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
     function btnSubmit(){
				var clientId=$("input[name='client_id']").val();
				var accessToken=$("input[name='access_token']").val();
	   			var account=$("input[name='account']").val();
	   			var oldPwd=$("input[name='old_pwd']").val();
	   			var newPwd=$("input[name='new_pwd']").val();
	   			var userCenterModiPwdUri=$("input[name='userCenterModiPwdUri']").val();
	   			var pwdtype=$("select[name='pwdtype']").val();
	   			var isValidate=$("#isValidate").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(account==''){
				    alert("请输入account");
					return;
				}
				if(oldPwd==''){
				    alert("请选择oldPwd");
					return;
				}if(newPwd==''){
				    alert("请选择newPwd");
					return;
				}if(isValidate==''){
				    alert("请选择isValidate");
					return;
				}
				$.post("${contextPath}/userCenterReg/getSignature",
					{
					clientId:clientId,
					accessToken:accessToken,
					password:newPwd,
					oldPassword:oldPwd
					},
	   				function(data){
	   					if(data.flag){
	   					    var signature=data.signature;
	   					    var timestamp=data.timestamp;
	   					    var signatureNonce=data.signatureNonce;
	   					    var newPwd=data.password;
	   					    var oldPwd=data.oldPassword;
	   					    var regUri=userCenterModiPwdUri+"?&client_id="+clientId+"&access_token="+accessToken+"&account="+account+"&old_pwd="+oldPwd+"&new_pwd="+newPwd+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce+"&pwdtype="+pwdtype+"&isValidate="+isValidate;
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