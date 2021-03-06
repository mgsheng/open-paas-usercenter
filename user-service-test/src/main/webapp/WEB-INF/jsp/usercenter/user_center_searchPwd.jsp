<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>userCentersearchPwd</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCentersearchPwd" method="post" class="form-horizontal">
                <input type="hidden" name="userCentersearchPwdUri" value="${userCentersearchPwdUri}"/>

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
                            <label class="col-sm-2 control-label">username</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="username" required="required"
                                       ng-model="username"/>

                                <p class="help-block">登录帐号</p>
                            </div>
                        </div>
                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{userCentersearchPwdUri}}?accessToken={{accessToken}}&username={{username}}&client_id={{client_id}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用找回密码接口</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCentersearchPwdUri = '${userCentersearchPwdUri}';
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
</script>

</body>
</html>