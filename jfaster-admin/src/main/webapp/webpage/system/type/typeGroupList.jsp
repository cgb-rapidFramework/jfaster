<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div id="main_typegroup_list" class="easyui-layout" fit="true">
    <div region="center" style="padding: 1px;">
        <t:datagrid name="typeGridTree" title="common.data.dictionary" actionUrl="typeController.do?typeGroupGrid"
                    idField="id" treeGrid="false" pagination="false" onLoadSuccess="loadSuccess">
            <t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
            <t:dgCol title="dict.name" field="typegroupname" width="100" query="true"></t:dgCol>
            <t:dgCol title="dict.code" field="typegroupcode" width="100" treeField="code"></t:dgCol>
            <t:dgCol title="common.operation" field="opt" width="100"></t:dgCol>
            <t:dgDelOpt url="typeController.do?delTypeGroup&id={id}" title="common.delete"></t:dgDelOpt>
            <t:dgFunOpt function="queryTypeValue(id,typegroupname)" title="common.type.view"></t:dgFunOpt>
            <t:dgToolBar title="common.add.param" langArg="lang.dictionary.type" icon="icon-add" url="typeController.do?aouTypeGroup" function="add"></t:dgToolBar>
            <%--<t:dgToolBar title="common.add.param" langArg="lang.dictionary.value" icon="icon-add" function="typeGridTree_AddType"></t:dgToolBar>--%>
            <t:dgToolBar title="common.edit" langArg="lang.dictionary.type" icon="icon-edit" url="typeController.do?aouTypeGroup"  function="update"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>

<div data-options="region:'east',
	title:'mytitle',
	collapsed:true,
	split:true,
	border:false,
	onExpand : function(){
		li_east = 1;
	},
	onCollapse : function() {
	    li_east = 0;
	}"
     style="width: 400px; overflow: hidden;" id="eastPanel">
    <div class="easyui-panel" style="padding: 1px;" fit="true" border="false" id="userListpanel"></div>
</div>

<script type="text/javascript">
    $(function() {
        var li_east = 0;
    });

    function queryTypeValue(typegroupid, typegroupname){
        var title = '<t:language langKey="lang.dictionary.type"/>: ' + typegroupname;
        if(li_east == 0){
            $('#main_typegroup_list').layout('expand','east');
        }
        $('#main_typegroup_list').layout('panel','east').panel('setTitle', title);
        $('#userListpanel').panel("refresh", "typeController.do?goTypeGrid&typegroupid=" + typegroupid);
    }
    function loadSuccess() {
        $('#main_typegroup_list').layout('panel','east').panel('setTitle', "");
        $('#main_typegroup_list').layout('collapse','east');
        $('#userListpanel').empty();
    }
</script>