<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.abocode.jfaster.core.platform.utils.SysThemesUtils"%>
<%@ page import="com.abocode.jfaster.core.platform.view.TemplateView" %>
<%@ page import="com.abocode.jfaster.core.common.util.BrowserUtils" %>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<%
    String lang = BrowserUtils.getBrowserLanguage();
    String langurl = "plug-in/mutiLang/" + lang +".js";
    TemplateView sysTheme = SysThemesUtils.getSysTheme(request);
    String lhgdialogTheme = SysThemesUtils.getLhgdialogTheme(sysTheme);
%>

<html>
<head>
    <title><t:mutiLang langKey="system.title"/></title>
    <link rel="shortcut icon" href="resources/fc/images/icon/favicon.ico">
    <script src=<%=langurl%> type="text/javascript"></script>
    <!--[if lt IE 9]>
    <script src="plug-in/login/js/html5.js"></script>
    <![endif]-->
    <!--[if lt IE 7]>
    <script src="plug-in/login/js/iepng.js" type="text/javascript"></script>
    <script type="text/javascript">
        EvPNG.fix('div, ul, img, li, input'); //EvPNG.fix('包含透明PNG图片的标签'); 多个标签之间用英文逗号隔开。
    </script>
    <![endif]-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/teemlink/css/login.css" 	type="text/css" />
</head>
<body >

<form action="loginController.do?login" check="loginController.do?checkuser" method="post">
<div align="center" class="container loginBg">
	<div class="language">
        <img src="plug-in/login/images/head.png" />
    </div>


    <div class="login">
    	<ul class="titleName">
        	<li class="english"></li>
            <li class="chinese"><t:mutiLang langKey="system.name"/></li>
        </ul>
        <ul class="loginInfo">
            <li>
	            <span>用户名：</span>
	            <input type="text"  class="username" value="admin" name="username" id="username"/>
            </li>
            <li>
            	<span>密 &nbsp;码：</span>
            	<input type="password" name="password" class="password"  value="123456" name="password"  id="password"/>
            </li>
			<li>
            	<span>验证码：</span>

                <div style="width:50px;float:left">
                    <input  name="randCode"  style="width: 90px;height: 26px;" type="text" id="randCode" title="" value="" nullmsg="" />

                </div>
                <div style="float: right; height: 26px;">
                    <img id="randCodeImage" src="randCodeImage"  />
                </div>
            </li>
           	
				<li >
                    <div id="alertMessage"></div>
                    <div id="successLogin"></div>
				</li>
			
            <li class="keepInfo"><input type="checkbox" id="on_off"  name="remember" value="yes"/>记住用户名</li>
            <li class="tipsArea"><div id="tipsArea">&nbsp;</div></li>           
            <li class="btnOk"><input type="button"  id="but_login" value="<t:mutiLang langKey="common.login"/>" class="loginBtn loginBg"  /></li>
        </ul>
    </div>

    <div class="copyright ">
    	<span>
            &copy; <t:mutiLang langKey="common.copyright" /> 版权所有 <span class="tip"><a href="#" title="<t:mutiLang langKey="system.right"/>"><t:mutiLang langKey="system.right"/></a> (推荐使用IE8+,谷歌浏览器) 技术支持：:<a href="#" title="<t:mutiLang langKey="system.support"/>"><t:mutiLang langKey="system.support"/></a>
        </span>
        </span>
    </div>
</div>
</form>



<!-- Link JScript-->
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="plug-in/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="plug-in/login/js/jquery-jrumble.js"></script>
<script type="text/javascript" src="plug-in/login/js/jquery.tipsy.js"></script>
<script type="text/javascript" src="plug-in/login/js/iphone.check.js"></script>
<script type="text/javascript" src="plug-in/login/js/login.js"></script>
<script type="text/javascript">


</script>
<!--    <script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js"></script> -->
<%=lhgdialogTheme %>

</body>
</html>

