<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>人员信息批量导入</title>
	<t:base type="jquery,easyui,tools"></t:base>
	<script type="text/javascript">
		function upload(curform){
			//tip("上传处理中...");
			return true;
		}

		function callback(data){
			tip("处理完成!");
			var win = frameElement.api.opener;
			if (data.success == true) {
				frameElement.api.close();
				win.tip(data.msg);
			} else {
				if (data.responseText == ''
						|| data.responseText == undefined) {
					$.messager.alert('错误', data.msg);
				} else {
					try {
						var emsg = data.responseText.substring(
								data.responseText.indexOf('错误描述'),
								data.responseText.indexOf('错误信息'));
						$.messager.alert('错误', emsg);
					} catch (ex) {
						$.messager.alert('错误', data.responseText + "");
					}
				}
				return false;
			}
			win.reloadTable();
		}
	</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" beforeSubmit="upload" ><!-- callback="callback@Override" -->
<fieldset class="step">
	<div class="form">
		<t:upload name="fiels" buttonText="选择要导入的文件" multi="false"
				  uploader="userController.do?saveImportUser" extend="*.xls" id="file_upload" formData="documentTitle"></t:upload>
		<div><font color="red">注意点：</font><br/>
			文件目前仅支持Excel文档,请下载导入模板<br/>
			<font color="red">[帐号 只能是字母+数字或者字母] </font><br/>
			<font color="red">[组织结构 到组织结构中查找，多个用逗号','隔开] </font><br/>
			<font color="red">[角色 角色列表中查找，多个用逗号','隔开]</font><br/>
			<a href="userController.do?downloadUserTemplate" ><font color="blue">导入模板下载</font></a><br/>
            <span id="documentTitle" name="documentTitle" style="display:none"></span>
		</div>
	</div>
	<div class="form" id="filediv" style="height:50px">
	</div>
</fieldset>
</t:formvalid>
</body>
</html>
