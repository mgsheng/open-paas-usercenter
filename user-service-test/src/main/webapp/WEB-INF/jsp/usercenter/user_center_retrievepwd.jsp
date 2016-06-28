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

            <form action="userCenterRetrievePwd" method="post" class="form-horizontal">
                <input type="hidden" name="userCenterRetrievePwdUri" value="${userCenterRetrievePwdUri}"/>

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
                                <input type="text" class="form-control" name="username" required="required"
                                       ng-model="username"/>

                                <p class="help-block">登录帐号</p>
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
                            <label class="col-sm-2 control-label">guid</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="guid" required="required"
                                       ng-model="guid"/>

                                <p class="help-block">guid</p>
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
                <button type="submit" class="btn btn-primary">去调用找回密码接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterModiPwdUri = '${userCenterRetrievePwdUri}';
        $scope.clientId='client-unity';
        $scope.accessToken = 'e23b86ea-1d4d-427c-a3da-c13a56c87192';
        $scope.account = 'xiaoli';

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
	   			var guid=$("input[name='guid']").val();
	   			var username=$("input[name='username']").val();
	   			var newPwd=$("input[name='new_pwd']").val();
	   			var userCenterRetrievePwdUri=$("input[name='userCenterRetrievePwdUri']").val();
	   			var isValidate=$("#isValidate").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(guid==''){
				    alert("请输入guid");
					return;
				}
				if(username==''){
				    alert("请输入username");
					return;
				}
				if(newPwd==''){
				    alert("请输入password");
					return;
				}if(isValidate==''){
				    alert("请选择isValidate");
					return;
				}
				$.post("${contextPath}/userCenterReg/getSignature",
					{
					clientId:clientId,
					accessToken:accessToken,
					},
	   				function(data){
	   					if(data.flag){
	   					    var signature=data.signature;
	   					    var timestamp=data.timestamp;
	   					    var signatureNonce=data.signatureNonce;
	   					    var regUri=userCenterRetrievePwdUri+"?&client_id="+clientId+"&access_token="+accessToken+"&username="+username+"&new_pwd="+newPwd+"&guid="+guid+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce+"&isValidate="+isValidate;
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