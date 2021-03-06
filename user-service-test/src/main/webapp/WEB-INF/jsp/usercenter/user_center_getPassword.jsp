<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>userCenterVerifyPassWord</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action=userCenterGetPwd method="post" class="form-horizontal">
                <input type="hidden" name="GetPassWordUri" value="${GetPassWordUri}"/>

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
                            <label class="col-sm-2 control-label">userName</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="userName" required="required"
                                       ng-model="userName"/>

                                <p class="help-block">用户名称</p>
                            </div>
                        </div>
                         <div class="form-group">
                            <label class="col-sm-2 control-label">source_id</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="source_id" required="required"
                                       ng-model="source_id"/>

                                <p class="help-block">source_id</p>
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
                <button type="submit" class="btn btn-primary">去调用获取密码规则接口</button>
                 <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.GetPassWordUri = '${GetPassWordUri}';
        $scope.clientId='337e8524bfd74f03916512bd7104567f';
        $scope.accessToken = 'c235bae1-666b-47d1-bacb-a7ce26e7b93e';
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
				var userName=$("input[name='userName']").val();
	   			var GetPassWordUri=$("input[name='GetPassWordUri']").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
		    	}
				if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(userName==''){
				    alert("请输入userName");
					return;
				} 
				if(source_id==''){
				    alert("请输入source_id");
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
	   					    var regUri=GetPassWordUri+"?&client_id="+clientId+"&source_id="+source_id+"&access_token="+accessToken+"&userName="+userName+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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