<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>新建任务</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="taskController.do?save">
			<input id="id" name="id" type="hidden" value="${taskPage.id}">
			<input  class="inputxt" id="totalNumber" name="totalNumber" type="hidden" ignore="ignore"   value="${taskPage.totalNumber}">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="left">
						<label class="Validform_label">
							任务名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="taskName" name="taskName" required="required"   datatype="s2-50" errormsg=""
							   value="${taskPage.taskName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				
				<tr>
					<td align="left">
						<label class="Validform_label">
							导入用户号码:
						</label>
					</td>
					<td class="value">
				     <t:upload name="instruction" buttonText="文件上传" dialog="false"
						multi="false"  extend="*.txt"
						view="false" auto="true"
						uploader="resourceController.do?saveFiles"
						onUploadSuccess="uploadSuccess" id="instruction"
						formData="documentTitle" ></t:upload>
					<div id="fileShow" > </div>
					
					<input class="inputxt" id="userTelNo" name="userTelNo" hidden="hidden"   datatype="*1-5000" errormsg="请导入用户号码！ ">
					<span class="Validform_checktip"></span>
					</td>
				</tr>
				
				<tr>
					<td align="left">
						<label class="Validform_label">
							每次刷新人数:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="topNumber" name="topNumber"  datatype="n1-5"
							   value="${taskPage.topNumber}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="left">
						<label class="Validform_label">
							每次刷新间隔时间（分钟）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="topSpaceTime" name="topSpaceTime"  datatype="n1-5"
							   value="${taskPage.topSpaceTime}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="left">
						<label class="Validform_label">
							求聊发布有效时间（分钟）:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="limitTime" name="limitTime"  datatype="n1-5"
							   value="${taskPage.limitTime}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="left">
						<label class="Validform_label">
							开始时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-{%d}'})"  style="width: 150px" id="beginTime" name="beginTime" 
							   value="<fmt:formatDate value='${taskPage.beginTime}' type="date"   pattern="yyyy-MM-dd hh:mm:ss"/>"  datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="left">
						<label class="Validform_label">
							结束时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\',{d:0});}',maxDate:'#F{$dp.$D(\'beginTime\',{d:90});}'})"  style="width: 150px" id="endTime" name="endTime"
							   value="<fmt:formatDate value='${taskPage.endTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
		<b>说明：</b><br/>
1、用户号码文件格式为txt ，每行一个<br/>
2、任务按每次刷新人数和刷新间隔时间对导入的号码按顺序分批次置顶<br/>
3、导入的用户号码在开始时间和结束时间内循环刷新<br/>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
 
 <script type="text/javascript">
        function uploadSuccess(d,file,response){
                $("#userTelNo").val(d.attributes.content);
                $("#totalNumber").val(d.attributes.totalNumber);
                var html="";
                html += "成功导入<font color='red'>"+d.attributes.totalNumber+"</font>个用户号码";
                $("#fileShow").html(html);
        }
</script>