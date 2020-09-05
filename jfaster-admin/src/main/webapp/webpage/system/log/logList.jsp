<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid title="log.manage" name="logList" actionUrl="logController.do?findDataGridData" idField="id" sortName="operationTime" sortOrder="desc" pageSize="1000">
	<t:dgCol title="log.level" field="loglevel" hidden="true"></t:dgCol>
	<t:dgCol title="common.id" field="id" hidden="true"></t:dgCol>
	<t:dgCol title="log.content" field="logContent" width="200"></t:dgCol>
	<t:dgCol title="operate.ip" field="note" width="200"></t:dgCol>
	<t:dgCol title="common.browser" field="broswer" width="100"></t:dgCol>
	<t:dgCol title="operate.time" field="operationTime" formatter="yyyy-MM-dd hh:mm:ss" width="100"></t:dgCol>
</t:datagrid>
<div id="logListtb" style="padding: 3px; height: 25px">
    <div name="searchColums" style="float: right; padding-right: 15px;">
        <t:language langKey="log.level"/>:
        <select name="loglevel" id="loglevel" onchange="logListsearch();">
            <option value="0"><t:language langKey="select.loglevel"/></option>
            <option value="1"><t:language langKey="common.login"/></option>
            <option value="2"><t:language langKey="common.logout"/></option>
            <option value="3"><t:language langKey="common.insert"/></option>
            <option value="4"><t:language langKey="common.delete"/></option>
            <option value="5"><t:language langKey="common.update"/></option>
            <option value="6"><t:language langKey="common.upload"/></option>
            <option value="7"><t:language langKey="common.other"/></option>
        </select>
        <span>
            <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;" title="操作时间 "><t:language langKey="operate.time"/>: </span>
            <input type="text" name="operatetime_begin" style="width: 100px; height: 24px;">~
            <input type="text" name="operatetime_end" style="width: 100px; height: 24px; margin-right: 20px;">
        </span>
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="logListsearch();"><t:language langKey="common.query"/></a>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        $("input[name='operatetime_begin']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
        $("input[name='operatetime_end']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});

        $("input").css("height", "24px");
    });
</script>
