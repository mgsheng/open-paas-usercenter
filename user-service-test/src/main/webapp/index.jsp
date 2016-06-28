<%--
 * 
 * @author Shengzhao Li
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h2>Spring Security&Oauth2 Client is work!</h2>


<div>
    操作说明:
    <ol>
        <li>
            <p>
                spring-oauth-client 的实现没有使用开源项目 <a
                    href="https://github.com/spring-projects/spring-security-oauth/tree/master/spring-security-oauth2"
                    target="_blank">spring-security-oauth2</a> 中提供的代码与配置, 如:<code>&lt;oauth:client
                id="oauth2ClientFilter" /&gt;</code>
            </p>
        </li>
        <li>
            <p>
                按照Oauth2支持的grant_type依次去实现. 共5类.
                <br/>
            <ul>
                <li>authorization_code</li>
                <li>password</li>
                <li>client_credentials</li>
                <li>implicit</li>
                <li>refresh_token</li>
            </ul>
        </li>
        <li>
            <p>
                <em>
                    在开始使用之前, 请确保 <a href="http://git.oschina.net/shengzhao/spring-oauth-server" target="_blank">spring-oauth-server</a>
                    项目已正确运行, 且 spring-oauth-client.properties (位于项目的src/main/resources目录) 配置正确
                </em>
            </p>
        </li>
    </ol>
</div>
<br/>

<p>
    &Delta; 注意: 项目前端使用了 Angular-JS 来处理动态数据展现.
</p>
<hr/>

<div>
    <strong>菜单</strong>
    <ul>
        <li>
            <p><a href="client_credentials">client_credentials</a> <br/>客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向'服务端'获取资源)</p>
        </li>
        <li>
            <p><a href="refresh_token">refresh_token</a> <br/>刷新access_token</p>
        </li>
        
        <li>
            <p><a href="userCenterReg">userCenterReg</a> <br/>用户中心注册接口</p>
        </li>
        <li>
            <p><a href="userCenterPassword">userCenterPassword</a> <br/>用户中心登录接口（通过用户名密码）</p>
        </li>
        <li>
            <p><a href="userCenterVerify">userCenterVerify</a> <br/>用户中心用户账号验证接口</p>
        </li>
        <li>
            <p><a href="userCenterModiPwd">userCenterModiPwd</a> <br/>用户中心修改账号密码接口</p>
        </li>
        <li>
            <p><a href="userCenterCheckToken">userCenterCheckToken</a> <br/>用户中心检查access_token</p>
        </li>
       <!--  <li>
            <p><a href="userGuidInfo">userGuidInfo</a> <br/>用户guid查询接口</p>
        </li> -->
        <li>
            <p><a href="userCenterRetrievePwd">userCenterRetrievePwd</a> <br/>用户找回密码接口接口</p>
        </li>
         <li>
            <p><a href="userCenterVerifyPassWord">userCenterVerifyPassWord</a> <br/>用户密码是否符合规则查询接口</p>
        </li>
        <li>
            <p><a href="userCentergetValidationRule">userCentergetValidationRule</a> <br/>用户获取密码规则接口</p>
        </li>
         <li>
            <p><a href="validateLogin">validateLogin</a> <br/>验证用户自动登录地址</p>
        </li>
         <li>
            <p><a href="synUserInfo">synUserInfo</a> <br/>手机号绑定解绑</p>
        </li>
        <li>
            <p><a href="bindUserInfo">bindUserInfo</a> <br/>用户信息绑定接口</p>
        </li>
         <li>
            <p><a href="unBindUser">unBindUser</a> <br/>用户信息解除绑定接口</p>
        </li>
        <li>
            <p><a href="userSaveInfo">userSaveInfo</a> <br/>用户信息保存接口</p>
        </li>

    </ul>
</div>
</body>
</html>