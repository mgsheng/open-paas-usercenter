<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>autoLogin</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="verifyAutoLogin" method="post" class="form-horizontal">
                <input type="hidden" name="verifyAutoLogin" value="${verifyAutoLogin}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
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
                            <input type="text" name="accessToken" id="access_token"
                                   class="form-control" ng-model="access_token"/>
                        </div>
                        <p class="help-block">客户端从Oauth Server 获得访问的token</p>
                    </div>
                         <div class="form-group">
                            <label class="col-sm-2 control-label">secret</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="secret" required="required"
                                       ng-model="secret"/>

                                <p class="help-block"><font color="red">DES加密之后的验证信息 格式：sourceId+“#”+time+“#”+appKey+“#”+salt(6位随机数)+"#"+desAddress</font></p>
                            </div>
                        </div>
                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{verifyAutoLogin}}?client_id={{client_id}}&access_token={{access_token}}&secret={{secret}}&desAddress={{desAddress}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用验证自动登录地址接口</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.autoLoginUri = '${autoLoginUri}';
        $scope.appId='1';
        $scope.desAppId='10026';
         $scope.secret='21212111889#20161109144001#91d921e029d0470b9eb41e39d895a0e0#123332#http://www.baidu.com';
        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>