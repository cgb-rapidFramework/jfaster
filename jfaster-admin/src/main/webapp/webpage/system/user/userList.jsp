<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>

<script>
    $(function() {
        var datagrid = $("#userListtb");
        datagrid.find("div[name='searchColums']").append($("#tempSearchColums div[name='searchColums']").html());
    });
</script>
<div id="tempSearchColums" style="display: none">
    <div name="searchColums">
        <span style="display:-moz-inline-box;display:inline-block;">
            <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;" title="<t:mutiLang langKey="common.department"/>">
                <t:mutiLang langKey="common.department"/>：
            </span>
            <input id="orgIds" name="orgIds" type="hidden">
            <input readonly="true" type="text" name="departname" style="width: 100px" onclick="choose_297e201048183a730148183ad85c0001()"/>
            <%--<t:choose hiddenName="orgIds" hiddenid="id"--%>
                      <%--url="departController.do?departSelect" name="departList"--%>
                      <%--icon="icon-search" title="common.department.list"--%>
                      <%--textname="departname" isclear="true"></t:choose>--%>
        </span>
    </div>
</div>

<t:datagrid name="userList" title="common.operation" actionUrl="userController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="common.username" sortable="false" field="username" query="true" width="10"></t:dgCol>
	<t:dgCol title="common.department" field="userOrgList.tsDepart.departname" query="false" width="12"></t:dgCol>
	<t:dgCol title="common.real.name" field="realName" query="true" width="10"></t:dgCol>
	<t:dgCol title="common.role" field="roleUserList.TSRole.roleName" width="10"></t:dgCol>
	<t:dgCol title="common.status" sortable="true" field="status" replace="common.active_1,common.inactive_0,super.admin_-1" width="8"></t:dgCol>

    <%--<t:dgCol title="common.username" field="opt"></t:dgCol>--%>
    <%--<t:dgFunOpt funname="setSignature(id)" title="设置签名" />--%>
    <%--<t:dgDelOpt title="common.delete" url="userController.do?del&id={id}&username={username}" />--%>
	<t:dgToolBar title="common.add.param" langArg="common.user" icon="icon-add" url="userController.do?addorupdate" funname="add"></t:dgToolBar>
	<t:dgToolBar title="common.edit.param" langArg="common.user" icon="icon-edit" url="userController.do?addorupdate" funname="update"></t:dgToolBar>
	<t:dgToolBar title="common.password.reset" icon="icon-edit" url="userController.do?changepasswordforuser" funname="update"></t:dgToolBar>
	<t:dgToolBar title="common.lock.user" icon="icon-edit" url="userController.do?lock" funname="lockObj"></t:dgToolBar>
    <t:dgToolBar title="导入用户" icon="icon-add" url="userController.do?importUser" funname="add"></t:dgToolBar>
    <t:dgToolBar title="导出Excel" icon="icon-print" onclick="exportXls();" ></t:dgToolBar>

</t:datagrid>
<script type="text/javascript">

function lockObj(title,url, id) {

	gridname=id;
	var rowsData = $('#'+id).datagrid('getSelections');
	if (!rowsData || rowsData.length==0) {
		tip('<t:mutiLang langKey="please.select.lock.item"/>');
		return;
	}
		url += '&id='+rowsData[0].id;

	$.dialog.confirm('<t:mutiLang langKey="is.confirm"/>', function(){
		lockuploadify(url, '&id');
	}, function(){
	});
}


function lockuploadify(url, id) {
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		url : url,// 请求的action路径
		error : function() {// 请求失败处理函数
		
		
		},
		success : function(data) {
			var d =data;
			if (d.success) {
			var msg = d.msg;
				tip(msg);
				reloadTable();
			}
			
		}
	});
}
</script>

<%--<a href="#" class="easyui-linkbutton" plain="true" icon="icon-search" onClick="choose_297e201048183a730148183ad85c0001()">选择</a>--%>
<%--<a href="#" class="easyui-linkbutton" plain="true" icon="icon-redo" onClick="clearAll_297e201048183a730148183ad85c0001();">清空</a>--%>
<script type="text/javascript">
//    var windowapi = frameElement.api, W = windowapi.opener;
    function choose_297e201048183a730148183ad85c0001() {
        if (typeof(windowapi) == 'undefined') {
            $.dialog({content: 'url:departController.do?departSelect', zIndex: 2100, title: '<t:mutiLang langKey="common.department.list"/>', lock: true, width: 400, height: 350, left: '85%', top: '65%', opacity: 0.4, button: [
                {name: '<t:mutiLang langKey="common.confirm"/>', callback: clickcallback_297e201048183a730148183ad85c0001, focus: true},
                {name: '<t:mutiLang langKey="common.cancel"/>', callback: function () {
                }}
            ]});
        } else {
            $.dialog({content: 'url:departController.do?departSelect', zIndex: 2100, title: '<t:mutiLang langKey="common.department.list"/>', lock: true, parent: windowapi, width: 400, height: 350, left: '85%', top: '65%', opacity: 0.4, button: [
                {name: '<t:mutiLang langKey="common.confirm"/>', callback: clickcallback_297e201048183a730148183ad85c0001, focus: true},
                {name: '<t:mutiLang langKey="common.cancel"/>', callback: function () {
                }}
            ]});
        }
    }
    function clearAll_297e201048183a730148183ad85c0001() {
        if ($('#departname').length >= 1) {
            $('#departname').val('');
            $('#departname').blur();
        }
        if ($("input[name='departname']").length >= 1) {
            $("input[name='departname']").val('');
            $("input[name='departname']").blur();
        }
        $('#orgIds').val("");
    }
    function clickcallback_297e201048183a730148183ad85c0001() {
        iframe = this.iframe.contentWindow;
        var departname = iframe.getdepartListSelections('departname');
        if ($('#departname').length >= 1) {
            $('#departname').val(departname);
            $('#departname').blur();
        }
        if ($("input[name='departname']").length >= 1) {
            $("input[name='departname']").val(departname);
            $("input[name='departname']").blur();
        }
        var id = iframe.getdepartListSelections('id');
        if (id !== undefined && id != "") {
            $('#orgIds').val(id);
            $("input[name='orgIds']").val(id);
        }
    }
function setSignature(id) {
    createwindow('设置签名', 'userController.do?addsign&id=' + id);
}
</script>

<script type="text/javascript">
    /*
     *拼接查询参数
     */
    function exportXls() {
        var query = $("#userList").datagrid('options').queryParams;
        var queryParams;
        for ( var key in query) {
            if (key != 'searchColums' && query[key] != 'undefined') {
                queryParams = queryParams + key + '=' + query[key] + '&';
            }
        }
        window.location.href = "userController.do?exportUser&"+ queryParams;
    }
</script>