<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-sms-send(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>

<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="smsSend" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="userSmsSendUri" id="userSmsSendUri" value="${userSmsSendUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

               <div ng-show="visible">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientId" id="client_id"
                                   class="form-control" ng-model="client_id"/>
                                     <p class="help-block">客户端从Oauth Server申请的client_id</p>
                        </div>
                      
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">accessToken</label>

                        <div class="col-sm-10">
                            <input type="text" name="accessToken" id="accessToken"
                                   class="form-control" ng-model="accessToken"/>
                                   <p class="help-block">客户端从Oauth Server 获得访问的token</p>
                        </div>
                        
                    </div>
                    
					<div class="form-group">  
                        <label class="col-sm-2 control-label">userName</label>

                        <div class="col-sm-10">
                            <input type="text" name="userName" size="50" id="userName" class="form-control"
                                   ng-model="userName"/>

                            <p class="help-block">帐号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" id="phone"size="50" class="form-control"
                                   ng-model="phone"/>
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
                <button type="submit" class="btn btn-primary">调用验证码发送接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
    
        $scope.userSmsSendUri = '${userCenterRegUri}';
        $scope.client_id = 'b62c4d6b6d3b462d9af0194e241512dd';
        $scope.accessToken="e23b86ea-1d4d-427c-a3da-c13a56c87192";
        $scope.userName='test2';
        $scope.phone="15727398579";

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
	   			var userName=$("#userName").val();
	   			var phone=$("#phone").val();
	   			var userSmsSendUri=$("#userSmsSendUri").val();
	   			//console.log(clientId);
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(userName==''){
				    alert("请输入username");
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
	   					    var regUri=userSmsSendUri+"?"+"client_id="+clientId+"&access_token="+accessToken+"&userName="+userName+"&phone="+phone+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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