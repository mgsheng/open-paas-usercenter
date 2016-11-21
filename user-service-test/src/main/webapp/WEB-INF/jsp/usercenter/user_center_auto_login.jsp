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

            <form action="autoLogin" method="post" class="form-horizontal">
                <input type="hidden" name="autoLoginUri" value="${autoLoginUri}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                	<div class="form-group">
                        <label class="col-sm-2 control-label">app_id</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="app_id" required="required"
                                   ng-model="appId"/>

                            <p class="help-block">客户端从 Oauth Server 申请的app_id, 有的Oauth服务器中又叫 appKey</p>
                        </div>
                    </div>
                   	<div class="form-group">
                        <label class="col-sm-2 control-label">desApp_id</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="desApp_id" required="required"
                                   ng-model="desAppId"/>

                            <p class="help-block">客户端从 Oauth Server 申请的app_id, 有的Oauth服务器中又叫 appKey</p>
                        </div>
                    </div>
                   
                         <div class="form-group">
                            <label class="col-sm-2 control-label">secret</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="secret" required="required"
                                       ng-model="secret"/>

                                <p class="help-block"><font color="red">DES加密之后的验证信息 格式：sourceId+“#”+time+“#”+appKey+“#”+salt(6位随机数)+"#"+desAddress</font></p>
                            </div>
                        </div>
                          <div class="form-group">
                            <label class="col-sm-2 control-label">desAddress</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="desAddress" required="required"
                                       ng-model="desAddress"/>

                                <p class="help-block"><font color="red">跳转地址</font></p>
                            </div>
                        </div>

                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{autoLoginUri}}?app_id={{app_id}}&secret={{secret}}&desApp_id={{desApp_id}}&desAddress={{desAddress}}
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