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

            <form action="synUserInfo" method="post" class="form-horizontal">
            <%-- <form action="${synUserInfo}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="synUserInfoUri" value="${synUserInfo}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">grant_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="grantType" readonly="readonly"
                                   class="form-control" ng-model="grant_type"/>

                            <p class="help-block">固定值 'client_credentials'<font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientId"
                                   class="form-control" ng-model="client_id"/>
                        </div>
                        <p class="help-block" style="margin-left: 170px">客户端从Oauth Server申请的client_id<font color="red">(必传)</font></p>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">access_token</label>

                        <div class="col-sm-10">
                            <input type="text" name="access_token"
                                   class="form-control" ng-model="access_token"/>
                        </div>
                        <p class="help-block"  style="margin-left: 170px">客户端从Oauth Server 获得访问的token<font color="red">(必传)</font></p>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">scope</label>

                        <div class="col-sm-10">
                            <select name="scope" ng-model="scope" class="form-control" >
                                <option value="read">read</option>
                                <option value="write">write</option>
                                <option value="read,write">read,write</option>
                            </select>
                           
                        </div>
                         <p class="help-block"  style="margin-left: 170px">权限（read，write） <font color="red">(必传)</font></p>
                    </div>                    
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">source_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="sourceId" class="form-control"
                                   size="50" ng-model="source_id"/>

                            <p class="help-block">原业务系统用户id  <font color="red">(必传)</font></p>
                        </div>
                    </div>
					
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">phone</label>

                        <div class="col-sm-10">
                            <input type="text" name="phone" size="50" class="form-control"
                                   ng-model="phone"/>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">email</label>

                        <div class="col-sm-10">
                            <input type="text" name="email" size="50" class="form-control"
                                   ng-model="email"/>

                            <p class="help-block">邮箱</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">whetherBind</label>

                        <div class="col-sm-10">
                            <select name="whetherBind" ng-model="whetherBind" id="whetherBind"class="form-control">
                                <option value="0">绑定手机号</option>
                                <option value="1">绑定邮箱</option>
                            </select>
                            <p class="help-block">是否绑定</p>
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
                <button type="submit" class="btn btn-primary" >调用用户信息同步接口</button>
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
        $scope.synUserInfo = '${synUserInfo}';
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
     function btnSubmit(){
				var clientId=$("input[name='clientId']").val();
				var accessToken=$("input[name='access_token']").val();
	   			var scope=$("select[name='scope']").val();
	   			var synUserInfoUri=$("input[name='synUserInfoUri']").val();
	   			var sourceId=$("input[name='sourceId']").val();
	   			var phone=$("input[name='phone']").val();
	   			var email=$("input[name='email']").val();
	   			var grantType=$("input[name='grantType']").val();
				if(clientId==''){
				    alert("请输入clientId");
					return;
				}if(accessToken==''){
				    alert("请输入accessToken");
					return;
				}
				if(sourceId==''){
				    alert("请输入sourceId");
					return;
				}
				if(phone==''){
				    alert("请输入phone");
					return;
				}
				if(email==''){
				    alert("请输入email");
					return;
				}
				if(grantType==''){
				    alert("请输入grantType");
					return;
				}if(scope==''){
				    alert("请输入scope");
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