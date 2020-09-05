<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>

<div class="easyui-layout" fit="true">
    <div region="center" style="padding: 1px;">
        <t:datagrid name="typeValueList" title="common.type.list"
                    actionUrl="typeController.do?typeGrid&typegroupid=${typegroupid}" idField="id"
                    queryMode="group">
            <t:dgCol title="common.code" field="id" hidden="true"></t:dgCol>
            <t:dgCol title="common.type.name" field="typename"></t:dgCol>
            <t:dgCol title="common.type.code" field="typecode"></t:dgCol>
            <t:dgCol title="common.operation" field="opt"></t:dgCol>
            <t:dgDelOpt url="typeController.do?delType&id={id}" title="common.delete"></t:dgDelOpt>
            <t:dgToolBar title="common.add.param" langArg="common.type" icon="icon-add" url="typeController.do?detailType&typegroupid=${typegroupid}" function="add"></t:dgToolBar>
            <t:dgToolBar title="common.edit.param" langArg="common.type" icon="icon-edit" url="typeController.do?detailType&typegroupid=${typegroupid}" function="update"></t:dgToolBar>
        </t:datagrid>
    </div>
</div>
<script>
    function addType(title,addurl,gname,width,height) {
        alert($("#id").val());
        add(title,addurl,gname,width,height);
    }
</script>

