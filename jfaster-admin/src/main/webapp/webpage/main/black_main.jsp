<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
	<title><t:language langKey="system.title"/></title>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<link rel="shortcut icon" href="images/favicon.ico">
<style type="text/css">
a {
	color: Black;
	text-decoration: none;
}

a:hover {
	color: black;
	text-decoration: none;
}
.tree-node-selected{
    background: #eaf2ff;
}
</style>
<SCRIPT type="text/javascript">

	$(function() {
		$('#layout_jeecg_onlineDatagrid').datagrid({
			url : 'systemController.do?datagridOnline&field=ip,logindatetime,user.username,',
			title : '',
			iconCls : '',
			fit : true,
			fitColumns : true,
			pagination : true,
			pageSize : 10,
			pageList : [ 10 ],
			nowarp : false,
			border : false,
			idField : 'id',
			sortName : 'logindatetime',
			sortOrder : 'desc',
			frozenColumns : [ [ {
				title : '<t:language langKey="common.code"/>',
				field : 'id',
				width : 150,
				hidden : true
			} ] ],
			columns : [ [ {
				title : '<t:language langKey="common.login.name"/>',
				field : 'user.username',
				width : 100,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : 'IP',
				field : 'ip',
				width : 150,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : '<t:language langKey="common.login.time"/>',
				field : 'logindatetime',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				},
				hidden : true
			} ] ],
			onClickRow : function(rowIndex, rowData) {
			},
			onLoadSuccess : function(data) {
				$('#layout_jeecg_onlinePanel').panel('setTitle', '( ' + data.total + ' )' + ' <t:language langKey="lang.user.online"/>');
			},
			onLoadError : function(data) {
			}
		}).datagrid('getPager').pagination({
			showPageList : false,
			showRefresh : false,
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : ''
		});		
		
		$('#layout_jeecg_onlinePanel').panel({
			tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
				}
			} ]
		});
		
		$('#layout_east_calendar').calendar({
			fit : true,
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', new Date());
			}
		});
		$(".layout-expand").click(function(){
			$('#layout_east_calendar').css("width","auto");
			$('#layout_east_calendar').parent().css("width","auto");
			$("#layout_jeecg_onlinePanel").find(".datagrid-view").css("max-height","200px");
			$("#layout_jeecg_onlinePanel .datagrid-view .datagrid-view2 .datagrid-body").css("max-height","180px").css("overflow-y","auto");
		});
	});
	var onlineInterval;
	
	function easyPanelCollapase(){
		window.clearTimeout(onlineInterval);
	}
	function easyPanelExpand(){
		onlineInterval = window.setInterval(function() {
			$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
		}, 1000 * 20);
	}
</SCRIPT>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<!-- 顶部-->
<div region="north" border="false" title="<t:language langKey="system.name"/>" style="BACKGROUND: #E6E6FA; height: 85px; padding: 1px; overflow: hidden;">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td align="left" style="vertical-align: text-bottom;"><img src="plug-in/login/images/head.png;"></td>
    <td align="right" nowrap>
        <table>
            <tr>
                <td valign="top" height="50">
                    <span style="color: #CC33FF"><t:language langKey="common.user"/>:</span>
                    <span style="color: #666633">${username }</span>
                    <span style="color: #CC33FF"><t:language langKey="current.org"/>:</span>
                    <span style="color: #666633">${currentOrgName }</span>
                    <span style="color: #CC33FF"><t:language langKey="common.role"/>:</span>
                    <span style="color: #666633">${roleName }</span>
                </td>
            </tr>
            <tr>
                <div style="position: absolute; right: 0px; bottom: 0px;">
                    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">
                        <t:language langKey="common.control.panel"/>
                    </a>
                    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">
                        <t:language langKey="common.logout"/>
                    </a>
                </div>
                <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
                    <div onclick="openwindow('<t:language langKey="common.profile"/>','userController.do?userinfo')">
                        <t:language langKey="common.profile"/>
                    </div>
                    <div class="menu-sep"></div>
                    <div onclick="add('<t:language langKey="common.change.password"/>','userController.do?changepassword')">
                        <t:language langKey="common.change.password"/>
                    </div>
                </div>
                <div id="layout_north_zxMenu" style="width: 100px; display: none;">
                    <div class="menu-sep"></div>
                    <div onclick="exit('loginController.do?logout','<t:language langKey="common.exit.confirm"/>',1);"><t:language langKey="common.exit"/></div>
                </div>
            </tr>
        </table>
    </td>
    <td align="right">&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<!-- 左侧-->
<div region="west" split="true" href="loginController.do?left" title="<t:language langKey="common.navegation"/>" style="width: 200px; padding: 1px;"></div>
<!-- 中间-->
<div id="mainPanle" region="center" style="overflow: hidden;">
    <div id="maintabs" class="easyui-tabs" fit="true" border="false">
    <div class="easyui-tab" title="<t:language langKey="common.dash_board"/>" href="loginController.do?home" style="padding: 2px; overflow: hidden;"></div>
        <c:if test="${map=='1'}">
            <div class="easyui-tab" title="<t:language langKey="common.map"/>" style="padding: 1px; overflow: hidden;">
                <iframe name="myMap" id="myMap" scrolling="no" frameborder="0" src="mapController.do?map" style="width: 100%; height: 99.5%;"></iframe>
            </div>
        </c:if>
    </div>
</div>
<!-- 右侧 -->
<div collapsed="true" region="east" iconCls="icon-reload" title="<t:language langKey="common.assist.tools"/>" split="true" style="width: 190px;"
	data-options="onCollapse:function(){easyPanelCollapase()},onExpand:function(){easyPanelExpand()}">
    <div id="tabs" class="easyui-tabs" border="false" style="height: 240px">
        <div title="<t:language langKey="common.calendar"/>" style="padding: 0px; overflow: hidden; color: red;">
            <div id="layout_east_calendar"></div>
        </div>
    </div>
    <div id="layout_jeecg_onlinePanel" data-options="fit:true,border:false" title=<t:language langKey="common.online.user"/>>
        <table id="layout_jeecg_onlineDatagrid"></table>
    </div>
</div>
<!-- 底部 -->
<div region="south" border="false" style="height: 25px; overflow: hidden;">
    <div align="center" style="color: #CC99FF; padding-top: 2px">&copy;
        <t:language langKey="common.copyright"/>
        <span class="tip">
            <a href="http://www.hulasou.com" title="<t:language langKey="system.name"/>"><t:language langKey="system.name"/> <t:language langKey="system.version"/></a>
        </span>
    </div>
</div>
<div id="mm" class="easyui-menu" style="width: 150px;">
    <div id="mm-tabupdate"><t:language langKey="common.refresh"/></div>
    <div id="mm-tabclose"><t:language langKey="common.close"/></div>
    <div id="mm-tabcloseall"><t:language langKey="common.close.all"/></div>
    <div id="mm-tabcloseother"><t:language langKey="common.close.all.but.this"/></div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright"><t:language langKey="common.close.all.right"/></div>
    <div id="mm-tabcloseleft"><t:language langKey="common.close.all.left"/></div>
</div>

</body>
</html>