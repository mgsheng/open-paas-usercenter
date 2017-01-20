<%--
 * 
 * @wangshuai
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>destorySession(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="destorySession" method="post" class="form-horizontal">
                <input type="hidden" name="destorySessionUri" value="${destorySessionUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientId"
                                   class="form-control" ng-model="client_id"/>
                        </div>
                        <p class="help-block" style="margin-left: 170px">客户端从 Oauth Server 申请的client_id<font color="red">(必传)</font></p>
                    </div>
                                        
                  
                                    
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">access_token</label>

                        <div class="col-sm-10">
                            <input type="text" name="accessToken" class="form-control"
                                   size="50" ng-model="access_token"/>

                            <p class="help-block">客户端从Oauth Server 获得访问的token <font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">session_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="sessionId" class="form-control"
                                   size="50" ng-model="sessionId"/>

                            <p class="help-block">客户端从Oauth Server 获得访问的session_id<font color="red">(必传)</font></p>
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
                <button type="submit" class="btn btn-primary" >调用注销Session用户接口</button>
                  <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
               
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.verifySessionUri = '${verifySessionUri}';
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
     function btnSubmit(){
				var clientId=$("input[name='clientId']").val();
	   			var accessToken=$("input[name='accessToken']").val();
                var sessionId=$("input[name='sessionId']").val();
	   			var destorySessionUri=$("input[name='destorySessionUri']").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}
				if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
                 if(sessionId==''){
                     alert("请输入sessionId");
                     return;
                 }
				$.post("${contextPath}/userCenterReg/getSignature",
					{
					clientId:clientId,
					accessToken:accessToken,
                        sessionId:sessionId,
					},
	   				function(data){
	   					if(data.flag){
	   					    var signature=data.signature;
	   					    var timestamp=data.timestamp;
	   					    var signatureNonce=data.signatureNonce;
	   					    var regUri=destorySessionUri+"?client_id="+clientId+"&access_token="+accessToken+"&jsessionId="+sessionId+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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