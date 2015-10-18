<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="userInfoList" title="有聊用户信息" actionUrl="userInfoController.do?datagrid" idField="id" fit="true">
   <t:dgCol title="编号" field="id" hidden="true"></t:dgCol>
   <t:dgCol title="个性签名的类型。0 文字签名；1 语音签名文件相对路径" field="signaturetype" ></t:dgCol>
   <t:dgCol title="头像缩略图地址" field="headPhotoS" ></t:dgCol>
   <t:dgCol title="用户权限列表，位或值，Long型，最大48位。
0：没有任何权限；
1：打免费电话权限；
2：12530拨打权限；
4：副号呼叫权限；
281474976710655：所有权限。" field="permissions" ></t:dgCol>
   <t:dgCol title="用户资料审核状态。0 待审核；1 审核通过" field="auditstatus" ></t:dgCol>
   <t:dgCol title="省份名称，包含省字的完整名称，如：四川省" field="province" ></t:dgCol>
   <t:dgCol title="城市名称，包含市字的完整名称，如：成都市" field="city" ></t:dgCol>
   <t:dgCol title="星座。0 未知；1 白羊座；2 金牛座；3 双子座；4 巨蟹座；5 狮子座；6 处女座；7 天秤座；8 天蝎座；9 射手座；10 摩羯座；11 水瓶座；12 双鱼座" field="constellation" ></t:dgCol>
   <t:dgCol title="自我介绍" field="selfsay" ></t:dgCol>
   <t:dgCol title="情感状态。0 未知；1 保密；2 单身；3 恋爱中；" field="affective" ></t:dgCol>
   <t:dgCol title="性取向。0 未知；1 保密；2 异性；3 同性；4 双性；" field="sexual" ></t:dgCol>
   <t:dgCol title="uId" field="uId" ></t:dgCol>
   <t:dgCol title="用户昵称" field="nickName" ></t:dgCol>
   <t:dgCol title="用户手机号码" field="msisdn" ></t:dgCol>
   <t:dgCol title="用户密文密码" field="pwd" ></t:dgCol>
   <t:dgCol title="用户电台长号码" field="radioNumber" ></t:dgCol>
   <t:dgCol title="头像地址" field="headPhoto" ></t:dgCol>
   <t:dgCol title="个性签名/交友宣言" field="signature" ></t:dgCol>
   <t:dgCol title="身份证号码" field="idCardNo" ></t:dgCol>
   <t:dgCol title="用户性别。0 未知；1 男；2 女" field="sex" ></t:dgCol>
   <t:dgCol title="出生日期" field="dateOfBirth" formatter="yyyy-MM-dd"></t:dgCol>
   <t:dgCol title="用户状态。0 禁用；1 正常；" field="status" ></t:dgCol>
   <t:dgCol title="用户注册时间" field="createTime" formatter="yyyy-MM-dd"></t:dgCol>
   <t:dgCol title="资料更新的时间" field="lastUpdateTime" formatter="yyyy-MM-dd"></t:dgCol>
   <t:dgCol title="用户是否开通副号功能。0 未开通；1 已开通" field="uiViceOnoff" ></t:dgCol>
   <t:dgCol title="用户当天使用刷新功能，刷新副号的次数" field="uiViceRefresh" ></t:dgCol>
   <t:dgCol title="用户等级" field="levelL" ></t:dgCol>
   <t:dgCol title="注册时的客户端版本号" field="uiAppVersion" ></t:dgCol>
   <t:dgCol title="注册版本的渠道来源" field="uiMarketName" ></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="userInfoController.do?del&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="userInfoController.do?addorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="userInfoController.do?addorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="userInfoController.do?addorupdate" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>