<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterVerify</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCenterCheckToken" method="post" class="form-horizontal">
                <input type="hidden" name="userCenterCheckTokenUri" value="${userCenterCheckTokenUri}"/>

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
                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>
                        <div class="text-primary" id="regUri">
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用检查access_token接口</button>
                 <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterCheckTokenUri = '${userCenterCheckTokenUri}';

        $scope.accessToken = 'e23b86ea-1d4d-427c-a3da-c13a56c87192';
        $scope.clientId='client-unity';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
     function btnSubmit(){
				var clientId=$("input[name='client_id']").val();
				var accessToken=$("input[name='access_token']").val();
	   			var userCenterCheckTokenUri=$("input[name='userCenterCheckTokenUri']").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
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
	   					    var regUri=userCenterCheckTokenUri+"?&client_id="+clientId+"&access_token="+accessToken+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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