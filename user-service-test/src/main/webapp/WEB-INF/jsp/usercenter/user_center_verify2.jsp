<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>verifyByUserNameAndPhone</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="verifyByUserNameAndPhone" method="post" class="form-horizontal">
                <input type="hidden" name="verifyByUserNameAndPhoneUri" value="${verifyByUserNameAndPhoneUri}"/>

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

                                <p class="help-block">用户账号 </p>
                            </div>
                        </div>   
                         <div class="form-group">
                            <label class="col-sm-2 control-label">phone</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="phone" required="required"
                                       ng-model="phone"/>

                                <p class="help-block">手机号 </p>
                            </div>
                        </div>   
                           <div class="form-group">
                        <label class="col-sm-2 control-label">登录类型</label>
                        <div class="col-sm-10">
                            <select name="accountType" ng-model="accountType" class="form-control">
                                <option value="1" >用户名格式</option>
                                <option value="2">手机号格式</option>
                                <option value="3">邮箱格式</option>
                                <option value="4">奥鹏卡号</option>
                            </select>
                               <p class="help-block">奥鹏卡号，学号不建议使用 </p>
                        </div>
                    </div>                   

                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{userCenterVerifyUri}}?access_token={{accessToken}}&account={{account}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用用户账号验证接口</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.verifyByUserNameAndPhoneUri = '${verifyByUserNameAndPhoneUri}';
		$scope.clientId = 'client-unity';
	     $scope.accountType="1";
        $scope.accessToken = 'e23b86ea-1d4d-427c-a3da-c13a56c87192';
        $scope.account='xiaoli';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>