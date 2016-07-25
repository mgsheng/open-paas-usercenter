<%--
 * 
 * @wangshuai
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>synUserInfo(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="getUserId" method="post" class="form-horizontal">
            <%-- <form action="${synUserInfo}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="verifyPayUserUri" value="${verifyPayUserUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">app_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId"
                                   class="form-control" ng-model="app_id"/>
                        </div>
                        <p class="help-block" style="margin-left: 170px">app_id<font color="red">(必传)</font></p>
                    </div>
                                        
                  
                                    
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">source_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceId" class="form-control"
                                   size="50" ng-model="source_id"/>

                            <p class="help-block">原业务系统用户id  <font color="red">(必传)</font></p>
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
                <button type="submit" class="btn btn-primary" >调用获取用户ID接口</button>
                  <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
                <%--
                <span class="text-muted">将重定向到 'spring-oauth-server' 的注册页面</span>
            --%>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.verifyPayUserUri = '${verifyPayUserUri}';
        $scope.app_id = '23';
        $scope.source_id='2';
        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
     function btnSubmit(){
				var appId=$("input[name='appId']").val();
		
	   			var sourceId=$("input[name='sourceId']").val();
				if(appId==''){
				    alert("请输入appId");
					return;
				}
				if(sourceId==''){
				    alert("请输入sourceId");
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
	   					    var regUri=synUserInfoUri+"?client_id="+clientId+"&grant_type="+grantType+"&scope="+scope+"&access_token="+accessToken+"&source_id="+sourceId+"&phone="+phone+"&email="+email+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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