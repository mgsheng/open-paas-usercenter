<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>UserCenterLogin</title>
</head>
<body>
<a href="${contextPath}/">Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="userCenterLogin" method="post" class="form-horizontal">
                <input type="hidden" name="userCenterLoginUri" value="${userCenterLoginUri}"/>

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
                            <label class="col-sm-2 control-label">grant_type</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="grant_type" readonly="readonly"
                                       ng-model="grantType"/>

                                <p class="help-block">固定值 'client_credentials'</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">scope</label>

                            <div class="col-sm-10">
                                <select name="scope" ng-model="scope" class="form-control">
                                    <option value="read">read</option>
                                    <option value="write">write</option>
                                    <option value="read,write">read,write</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">source_id</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="source_id" required="required"
                                       ng-model="sourceId"/>

                                <p class="help-block">业务系统用户ID</p>
                            </div>
                        </div>
                        

                    <div class="well well-sm">
                        <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/>

                        <div class="text-primary">
                            {{userCenterLoginUri}}?client_id={{clientId}}&client_secret={{clientSecret}}&grant_type={{grantType}}&scope={{scope}}&source_id={{sourceId}}
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">去调用登录接口</button>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.userCenterLoginUri = '${userCenterLoginUri}';
        $scope.grantType = 'client_credentials';
        $scope.scope = 'read,write';

        $scope.clientId = 'b62c4d6b6d3b462d9af0194e241512dd';
        /*$scope.clientSecret='4c7a43395e9f4f3fa0b6639ca56bdf27';*/
        $scope.accessToken="3c712b7f-f948-427b-ac77-99ae4059d487";
        $scope.sourceId='1';

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>

</body>
</html>