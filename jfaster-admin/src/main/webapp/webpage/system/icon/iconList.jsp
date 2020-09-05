<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding: 1px;">
    <t:datagrid name="iconList" title="icon.list" actionUrl="iconController.do?findDataGridData" idField="id" queryMode="group">
        <t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
        <t:dgCol title="common.icon.name" query="true" field="iconName" width="10"></t:dgCol>
        <t:dgCol title="common.icon.style" field="iconClas" width="10"></t:dgCol>
        <t:dgCol title="common.icon.type" field="iconType" width="10" query="true" dictionary="icontype"></t:dgCol>
        <t:dgCol title="common.icon" field="iconPath"  width="10" image="true" imageSize="24,24"></t:dgCol>
        <t:dgCol title="common.icon.type" field="extend" width="10"></t:dgCol>
        <t:dgCol title="common.operation" field="opt" width="20"></t:dgCol>
        <t:dgDelOpt url="iconController.do?del&id={id}" title="common.delete"></t:dgDelOpt>
        <t:dgToolBar title="common.add.param" langArg="common.icon" icon="icon-add" url="iconController.do?detail" function="add"></t:dgToolBar>
        <t:dgToolBar title="common.edit.param" langArg="common.icon" icon="icon-edit" url="iconController.do?detail" function="update"></t:dgToolBar>
        <t:dgToolBar title="batch.generate.style" icon="icon-edit" url="iconController.do?repair" function="doSubmit"></t:dgToolBar>
    </t:datagrid>
</div>
</div>

<script type="text/javascript">
    function viewImage(title,id,url){
        createdetailwindow(title+"列表", url,600,520);
    }
</script>