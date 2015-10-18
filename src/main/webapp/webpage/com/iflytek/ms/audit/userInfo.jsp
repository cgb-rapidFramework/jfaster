<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>有聊用户信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userInfoController.do?save">
			<input id="id" name="id" type="hidden" value="${userInfoPage.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							个性签名的类型。0 文字签名；1 语音签名文件相对路径:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="signaturetype" name="signaturetype" ignore="ignore"
							   value="${userInfoPage.signaturetype}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							头像缩略图地址:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="headPhotoS" name="headPhotoS" ignore="ignore"
							   value="${userInfoPage.headPhotoS}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户权限列表，位或值，Long型，最大48位。
0：没有任何权限；
1：打免费电话权限；
2：12530拨打权限；
4：副号呼叫权限；
281474976710655：所有权限。:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="permissions" name="permissions" 
							   value="${userInfoPage.permissions}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户资料审核状态。0 待审核；1 审核通过:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="auditstatus" name="auditstatus" 
							   value="${userInfoPage.auditstatus}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							省份名称，包含省字的完整名称，如：四川省:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="province" name="province" ignore="ignore"
							   value="${userInfoPage.province}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							城市名称，包含市字的完整名称，如：成都市:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="city" name="city" ignore="ignore"
							   value="${userInfoPage.city}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							星座。0 未知；1 白羊座；2 金牛座；3 双子座；4 巨蟹座；5 狮子座；6 处女座；7 天秤座；8 天蝎座；9 射手座；10 摩羯座；11 水瓶座；12 双鱼座:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="constellation" name="constellation" ignore="ignore"
							   value="${userInfoPage.constellation}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							自我介绍:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="selfsay" name="selfsay" ignore="ignore"
							   value="${userInfoPage.selfsay}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							情感状态。0 未知；1 保密；2 单身；3 恋爱中；:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="affective" name="affective" ignore="ignore"
							   value="${userInfoPage.affective}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							性取向。0 未知；1 保密；2 异性；3 同性；4 双性；:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="sexual" name="sexual" ignore="ignore"
							   value="${userInfoPage.sexual}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							uId:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="uId" name="uId" 
							   value="${userInfoPage.uId}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户昵称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="nickName" name="nickName" ignore="ignore"
							   value="${userInfoPage.nickName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户手机号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="msisdn" name="msisdn" 
							   value="${userInfoPage.msisdn}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户密文密码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="pwd" name="pwd" 
							   value="${userInfoPage.pwd}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户电台长号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="radioNumber" name="radioNumber" ignore="ignore"
							   value="${userInfoPage.radioNumber}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							头像地址:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="headPhoto" name="headPhoto" ignore="ignore"
							   value="${userInfoPage.headPhoto}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							个性签名/交友宣言:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="signature" name="signature" ignore="ignore"
							   value="${userInfoPage.signature}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							身份证号码:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="idCardNo" name="idCardNo" ignore="ignore"
							   value="${userInfoPage.idCardNo}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户性别。0 未知；1 男；2 女:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="sex" name="sex" ignore="ignore"
							   value="${userInfoPage.sex}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							出生日期:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="dateOfBirth" name="dateOfBirth" ignore="ignore"
							   value="<fmt:formatDate value='${userInfoPage.dateOfBirth}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户状态。0 禁用；1 正常；:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="status" name="status" 
							   value="${userInfoPage.status}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户注册时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="createTime" name="createTime" 
							   value="<fmt:formatDate value='${userInfoPage.createTime}' type="date" pattern="yyyy-MM-dd"/>" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							资料更新的时间:
						</label>
					</td>
					<td class="value">
						<input class="Wdate" onClick="WdatePicker()"  style="width: 150px" id="updateTime" name="updateTime" ignore="ignore"
							   value="<fmt:formatDate value='${userInfoPage.updateTime}' type="date" pattern="yyyy-MM-dd"/>">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户是否开通副号功能。0 未开通；1 已开通:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="uiViceOnoff" name="uiViceOnoff" 
							   value="${userInfoPage.uiViceOnoff}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户当天使用刷新功能，刷新副号的次数:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="uiViceRefresh" name="uiViceRefresh" 
							   value="${userInfoPage.uiViceRefresh}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							用户等级:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="levelL" name="levelL" 
							   value="${userInfoPage.levelL}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							注册时的客户端版本号:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="uiAppVersion" name="uiAppVersion" ignore="ignore"
							   value="${userInfoPage.uiAppVersion}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							注册版本的渠道来源:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="uiMarketName" name="uiMarketName" ignore="ignore"
							   value="${userInfoPage.uiMarketName}">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>