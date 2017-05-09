<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-center-saveInfo(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userSaveInfo" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="saveUserInfoUri" id="saveUserInfo" value="${saveUserInfo}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">grant_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="grantType" readonly="readonly" id="grant_type"
                                   class="form-control" ng-model="grant_type"/>

                            <p class="help-block">固定值 'client_credentials'</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientId" id="client_id"
                                   class="form-control" ng-model="client_id"/>
                        </div>
                        <p class="help-block">客户端从Oauth Server申请的client_id</p>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">access_token</label>

                        <div class="col-sm-10">
                            <input type="text" name="access_token" id="access_token"
                                   class="form-control" ng-model="access_token"/>
                        </div>
                        <p class="help-block">客户端从Oauth Server 获得访问的token</p>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">scope</label>

                        <div class="col-sm-10">
                            <select name="scope" ng-model="scope" id="scope"class="form-control">
                                <option value="read">read</option>
                                <option value="write">write</option>
                                <option value="read,write">read,write</option>
                            </select>
                        </div>
                    </div>                    
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">source_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceId" id="source_id" class="form-control"
                                   size="50" ng-model="source_id"/>

                            <p class="help-block">业务系统用户id</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">appUid</label>

                        <div class="col-sm-10">
                            <input type="text" name="appUid" id="appUid" class="form-control"
                                   size="50" ng-model="appUid"/>

                            <p class="help-block">业务系统用户id</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">guid</label>

                        <div class="col-sm-10">
                            <input type="text" name="guid" id="guid" class="form-control"
                                   size="50" ng-model="guid"/>

                            <p class="help-block">业务系统用户id</p>
                        </div>
                    </div>
                         <div class="form-group">
                        <label class="col-sm-2 control-label">id</label>

                        <div class="col-sm-10">
                            <input type="text" name="id" id="id" class="form-control"
                                   size="50" ng-model="id"/>

                            <p class="help-block">业务系统用户id</p>
                        </div>
                    </div>
					<div class="form-group">  
                        <label class="col-sm-2 control-label">username</label>

                        <div class="col-sm-10">
                            <input type="text" name="username" size="50" id="username" class="form-control"
                                   ng-model="username"/>

                            <p class="help-block">帐号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">password</label>

                        <div class="col-sm-10">
                            <input type="text" name="password" id="password" size="50" class="form-control"
                                   ng-model="password"/>

                            <p class="help-block">密码</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" id="phone"size="50" class="form-control"
                                   ng-model="phone"/>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">email</label>

                        <div class="col-sm-10">
                            <input type="text" name="email" id="email" size="50" class="form-control"
                                   ng-model="email"/>

                            <p class="help-block">邮箱</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">cardNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="cardNo" size="50" class="form-control"
                                   ng-model="card_no"/>

                            <p class="help-block">奥鹏卡号</p>
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
                    
                       <div class="form-group">
                        <label class="col-sm-2 control-label">methordName</label>

                        <div class="col-sm-10">
                            <select name="methordName" ng-model="methordName" id="methordName"class="form-control">
                                <option value="registerUser">注册</option>
                                <option value="sysUserInfo">同步</option>
                                <option value="bindUserInfo">绑定</option>
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
                <button type="submit" class="btn btn-primary">调用用户信息保存接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
    
        $scope.saveUserInfo = '${saveUserInfo}';
        $scope.grant_type = 'client_credentials';
        $scope.scope = 'read,write';
        $scope.client_id = 'b62c4d6b6d3b462d9af0194e241512dd';
        //$scope.clientSecret='4c7a43395e9f4f3fa0b6639ca56bdf27';
        $scope.access_token="e23b86ea-1d4d-427c-a3da-c13a56c87192";
        $scope.source_id='2';
        $scope.nickname="xiaogang";        
        $scope.real_name="小刚";
        $scope.identify_type="1";
        $scope.identify_no="130629199003150366";
        $scope.user_type="2";
        $scope.sex="1";  

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
				var clientId=$("input[name='clientId']").val();
				var accessToken=$("input[name='access_token']").val();
				var scope=$("#scope").val();
	   			var grantType=$("#grant_type").val();
	   			var sourceId=$("#source_id").val();
	   			var username=$("#username").val();
	   			var password=$("#password").val();
	   			var phone=$("#phone").val();
	   			var email=$("#email").val();
	   			var isValidate=$("#isValidate").val();
	   			userCenterRegUri=$("#saveUserInfo").val();
	   			//console.log(clientId);
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
				if(isValidate==''){
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
	   					    var regUri=userCenterRegUri+"?"+"grant_type="+grantType+"&scope="+scope+"&client_id="+clientId+"&access_token="+accessToken+"&source_id="+sourceId+"&username="+username+"&password="+password+"&phone="+phone+"&email="+email+"&isValidate="+isValidate+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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