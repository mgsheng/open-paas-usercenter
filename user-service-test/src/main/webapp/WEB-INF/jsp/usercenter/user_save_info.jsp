<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>user-save-info(interface)</title>
</head>
<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <!-- <form action="userCenterReg" method="post" class="form-horizontal"> -->
            <form action="${saveUserInfo}" method="post" class="form-horizontal">
                <input type="hidden" name="saveUserInfo" value="${saveUserInfo}"/>

                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">grant_type</label>

                        <div class="col-sm-10">
                            <input type="text" name="grant_type" readonly="readonly"
                                   class="form-control" ng-model="grant_type"/>

                            <p class="help-block">固定值 'client_credentials'</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">client_id</label>

                        <div class="col-sm-10">
                            <input type="text" name="client_id"
                                   class="form-control" ng-model="client_id"/>
                        </div>
                        <p class="help-block">客户端从Oauth Server申请的client_id</p>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">access_token</label>

                        <div class="col-sm-10">
                            <input type="text" name="access_token"
                                   class="form-control" ng-model="access_token"/>
                        </div>
                        <p class="help-block">客户端从Oauth Server 获得访问的token</p>
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
                            <input type="text" name="source_id" class="form-control"
                                   size="50" ng-model="source_id"/>

                            <p class="help-block">业务系统用户id</p>
                        </div>
                    </div>

					<div class="form-group">
                        <label class="col-sm-2 control-label">username</label>

                        <div class="col-sm-10">
                            <input type="text" name="username" size="50" class="form-control"
                                   ng-model="username"/>

                            <p class="help-block">帐号</p>
                        </div>
                    </div>
                    <%--
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">password</label>

                        <div class="col-sm-10">
                            <input type="text" name="password" size="50" class="form-control"
                                   ng-model="password"/>

                            <p class="help-block">密码</p>
                        </div>
                    </div>

                    --%>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">nickname</label>

                        <div class="col-sm-10">
                            <input type="text" name="nickname" size="50" class="form-control"
                                   ng-model="nickname"/>

                            <p class="help-block">昵称</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">real_name</label>

                        <div class="col-sm-10">
                            <input type="text" name="real_name" size="50" class="form-control"
                                   ng-model="real_name"/>

                            <p class="help-block">真实姓名</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">head_picture</label>

                        <div class="col-sm-10">
                            <input type="text" name="head_picture" size="50" class="form-control"
                                   />

                            <p class="help-block">用户头像</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">identify_type</label>
                        <div class="col-sm-10">
                            <select name="identify_type" ng-model="identify_type" class="form-control">
                                <option value="1">身份证</option>
                                <option value="2">军官证</option>
                                <option value="3">港澳台证</option>
                                <option value="4">护照</option>
                                <option value="5">其他</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">identify_no</label>

                        <div class="col-sm-10">
                            <input type="text" name="identify_no" size="50" class="form-control"
                                   ng-model="identify_no"/>

                            <p class="help-block">身份唯一标识</p>
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
                        <label class="col-sm-2 control-label">user_type</label>

                        <div class="col-sm-10">
                            <select name="user_type" ng-model="user_type" class="form-control">
                                <option value="1">真实用户</option>
                                <option value="2">测试用户</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">sex</label>

                        <div class="col-sm-10">
                            <select name="sex" ng-model="sex" class="form-control">
                                <option value="1">女</option>
                                <option value="0">男</option>
                                <option value="3">其他</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">nation</label>

                        <div class="col-sm-10">
                            <input type="text" name="nation" size="50" class="form-control"
                                   ng-model="nation"/>

                            <p class="help-block">民族</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">politics</label>

                        <div class="col-sm-10">
                            <input type="text" name="politics" size="50" class="form-control"
                                   ng-model="politics"/>

                            <p class="help-block">政治面貌</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">birthday</label>

                        <div class="col-sm-10">
                            <input type="text" name="birthday" size="50" class="form-control"
                                   ng-model="birthday"/>

                            <p class="help-block">出生日期</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">age</label>

                        <div class="col-sm-10">
                            <input type="text" name="age" size="50" class="form-control"
                                   ng-model="age"/>

                            <p class="help-block">年龄</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">marriage</label>

                        <div class="col-sm-10">
                            <input type="text" name="marriage" size="50" class="form-control"
                                   ng-model="marriage"/>

                            <p class="help-block">婚姻状态</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">education</label>

                        <div class="col-sm-10">
                            <input type="text" name="education" size="50" class="form-control"
                                   ng-model="education"/>

                            <p class="help-block">最终学历</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">occupation</label>

                        <div class="col-sm-10">
                            <input type="text" name="occupation" size="50" class="form-control"
                                   ng-model="occupation"/>

                            <p class="help-block">职业</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">region</label>

                        <div class="col-sm-10">
                            <input type="text" name="region" size="50" class="form-control"
                                   ng-model="region"/>

                            <p class="help-block">区域</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">province</label>

                        <div class="col-sm-10">
                            <input type="text" name="province" size="50" class="form-control"
                                   ng-model="province"/>

                            <p class="help-block">省市</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">city</label>

                        <div class="col-sm-10">
                            <input type="text" name="city" size="50" class="form-control"
                                   ng-model="city"/>

                            <p class="help-block">城市</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">hukou</label>

                        <div class="col-sm-10">
                            <input type="text" name="hukou" size="50" class="form-control"
                                   ng-model="hukou"/>

                            <p class="help-block">户口</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">create_time</label>

                        <div class="col-sm-10">
                            <input type="text" name="createTime" size="50" class="form-control"
                                   ng-model="createTime"/>

                            <p class="help-block">注册时间</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">qq</label>

                        <div class="col-sm-10">
                            <input type="text" name="qq" size="50" class="form-control"
                                   ng-model="qq"/>

                            <p class="help-block">qq账号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">weixin</label>

                        <div class="col-sm-10">
                            <input type="text" name="weixin" size="50" class="form-control"
                                   ng-model="weixin"/>

                            <p class="help-block">微信账号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">weibo</label>

                        <div class="col-sm-10">
                            <input type="text" name="weibo" size="50" class="form-control"
                                   ng-model="weibo"/>

                            <p class="help-block">新浪微博账号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">blog</label>

                        <div class="col-sm-10">
                            <input type="text" name="blog" size="50" class="form-control"
                                   ng-model="blog"/>

                            <p class="help-block">腾讯微博账号</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">others</label>

                        <div class="col-sm-10">
                            <input type="text" name="others" size="50" class="form-control"
                                   ng-model="others"/>

                            <p class="help-block">其他社交信息</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">address</label>

                        <div class="col-sm-10">
                            <input type="text" name="address" size="50" class="form-control"
                                   ng-model="address"/>

                            <p class="help-block">学校地址</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">school</label>

                        <div class="col-sm-10">
                            <input type="text" name="school" size="50" class="form-control"
                                   ng-model="school"/>

                            <p class="help-block">学校</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">major</label>

                        <div class="col-sm-10">
                            <input type="text" name="major" size="50" class="form-control"
                                   ng-model="major"/>

                            <p class="help-block">专业</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">start_date</label>

                        <div class="col-sm-10">
                            <input type="text" name="start_date" size="50" class="form-control"
                                   ng-model="start_date"/>

                            <p class="help-block">学习开始时间，格式“yyyy-MM-dd”</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">end_date</label>

                        <div class="col-sm-10">
                            <input type="text" name="end_date" size="50" class="form-control"
                                   ng-model="end_date"/>

                            <p class="help-block">毕业时间，格式“yyyy-MM-dd”</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">study_description</label>

                        <div class="col-sm-10">
                            <input type="text" name="study_description" size="50" class="form-control"
                                   ng-model="study_description"/>

                            <p class="help-block">学习信息描述</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">study_level</label>

                        <div class="col-sm-10">
                            <input type="text" name="study_level" size="50" class="form-control"
                                   ng-model="study_level"/>

                            <p class="help-block">学历</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">home_address</label>

                        <div class="col-sm-10">
                            <input type="text" name="home_address" size="50" class="form-control"
                                   ng-model="home_address"/>

                            <p class="help-block">家庭地址</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">tel</label>

                        <div class="col-sm-10">
                            <input type="text" name="tel" size="50" class="form-control"
                                   ng-model="tel"/>

                            <p class="help-block">办公电话</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">fax</label>

                        <div class="col-sm-10">
                            <input type="text" name="fax" size="50" class="form-control"
                                   ng-model="fax"/>

                            <p class="help-block">传真</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">begin_date</label>

                        <div class="col-sm-10">
                            <input type="text" name="begin_date" size="50" class="form-control"
                                   ng-model="begin_date"/>

                            <p class="help-block">工作开始时间，格式“yyyy-MM-dd”</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">finish_date</label>

                        <div class="col-sm-10">
                            <input type="text" name="finish_date" size="50" class="form-control"
                                   ng-model="finish_date"/>

                            <p class="help-block">工作结束时间，格式“yyyy-MM-dd”</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">now_address</label>

                        <div class="col-sm-10">
                            <input type="text" name="now_address" size="50" class="form-control"
                                   ng-model="now_address"/>

                            <p class="help-block">现住址</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">company</label>

                        <div class="col-sm-10">
                            <input type="text" name="company" size="50" class="form-control"
                                   ng-model="company"/>

                            <p class="help-block">公司</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">description</label>

                        <div class="col-sm-10">
                            <input type="text" name="description" size="50" class="form-control"
                                   ng-model="description"/>

                            <p class="help-block">工作描述</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">position</label>

                        <div class="col-sm-10">
                            <input type="text" name="position" size="50" class="form-control"
                                   ng-model="position"/>

                            <p class="help-block">职位</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">responsibility</label>

                        <div class="col-sm-10">
                            <input type="text" name="responsibility" size="50" class="form-control"
                                   ng-model="responsibility"/>

                            <p class="help-block">岗位职责</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">work_content</label>

                        <div class="col-sm-10">
                            <input type="text" name="work_content" size="50" class="form-control"
                                   ng-model="work_content"/>

                            <p class="help-block">简历</p>
                        </div>
                    </div>

                    <div class="well well-sm">
                       <!--  <span class="text-muted">最终发给 spring-oauth-server的 URL:</span>
                        <br/> -->

                        <!-- <div class="text-primary">
                            {{userCenterRegUri}}?response_type={{responseType}}&scope={{scope}}&client_id={{clientId}}&redirect_uri={{redirectUri}}&state={{state}}
                        </div> -->
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用用户信息保存接口</button>
                <%--
                <span class="text-muted">将重定向到 'spring-oauth-server' 的注册页面</span>
            --%>
            </form>

        </div>
    </div>
</div>

<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
        $scope.saveUserInfo = '${saveUserInfo}';
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
</script>

</body>
</html>