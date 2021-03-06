<%--
 *
 * @wangshuai
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>saveRedis(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>
<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="saveRedis" method="post" class="form-horizontal">
                <input type="hidden" name="saveRedisUri" value="${saveRedisUri}"/>
                <div>${data}</div>

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
                        <label class="col-sm-2 control-label">service_name</label>

                        <div class="col-sm-10">
                            <input type="text" name="serviceName" class="form-control"
                                   size="50" ng-model="serviceName"/>

                            <p class="help-block">服务名称（用户服务固定值为userService）<font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">redis_key</label>

                        <div class="col-sm-10">
                            <input type="text" name="redisKey" class="form-control"
                                   size="50" ng-model="redisKey"/>

                            <p class="help-block">key值  (单点登录key值为jsessionid)<font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">redis_value</label>

                        <div class="col-sm-10">
                            <input type="text" name="redisValue" class="form-control"
                                   size="50" ng-model="redisValue"/>

                            <p class="help-block">客户端从Oauth Server 获得访问的redis_value<font color="red">(必传)</font></p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">session_time</label>

                        <div class="col-sm-10">
                            <input type="text" name="sessionTime" class="form-control"
                                   size="50" ng-model="sessionTime"/>

                            <p class="help-block">有效登录时间<font color="red">(可选)</font></p>
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
                <button type="submit" class="btn btn-primary" >调用用户Session Redis 保存接口</button>
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
        var saveRedisUri=$("input[name='saveRedisUri']").val();
        var serviceName=$("input[name='serviceName']").val();
        var redisKey=$("input[name='redisKey']").val();
        var redisValue=$("input[name='redisValue']").val();
        var sessionTime=$("input[name='sessionTime']").val();
        if(clientId==''){
            alert("请输入clientId");
            return;
        }
        if(accessToken==''){
            alert("请输入accessToken");
            return;
        }
        if(saveRedisUri==''){
            alert("请输入saveRedisUri");
            return;
        }
        if(serviceName==''){
            alert("请输入serviceName");
            return;
        }
        if(redisKey==''){
            alert("请输入redisKey");
            return;
        }
        if(redisValue==''){
            alert("请输入redisValue");
            return;
        }
        $.post("${contextPath}/userCenterReg/getSignature",
            {
                clientId:clientId,
                accessToken:accessToken,
                serviceName:serviceName,
                redisKey:redisKey,
                sessionTime:sessionTime,
                redisValue:redisValue
            },
            function(data){
                if(data.flag){
                    var signature=data.signature;
                    var timestamp=data.timestamp;
                    var signatureNonce=data.signatureNonce;
                    var regUri=saveRedisUri+"?client_id="+clientId+"&access_token="+accessToken+"&service_name="+serviceName+"&redis_key="+redisKey+"&redis_value="+redisValue+"&session_time="+sessionTime+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
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