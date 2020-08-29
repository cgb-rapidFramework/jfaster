<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>地域信息</title>
<t:base type="jquery,easyui,tools"></t:base>
<script type="text/javascript">
		
	$(function() {
		$('#cc').combotree({
			url : 'territoryController.do?setPTerritory',
			panelHeight:'auto',
			onClick: function(node){
				$("#territoryId").val(node.id);
			}
		});
		
		if($('#territoryLevel').val()=='1'){
			$('#pfun').show();
		}else{
			$('#pfun').hide();
		}
		
		
		$('#territoryLevel').change(function(){
			if($(this).val()=='1'){
				$('#pfun').show();
				var t = $('#cc').combotree('tree');
				var nodes = t.tree('getRoots');
				if(nodes.length>0){
					$('#cc').combotree('setValue', nodes[0].id);
					$("#territoryId").val(nodes[0].id);
				}
			}else{
				var t = $('#cc').combotree('tree');
				var node = t.tree('getSelected');
				if(node){
					$('#cc').combotree('setValue', null);
				}
				$('#pfun').hide();
			}
		});
	});
</script>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" layout="div" dialog="true" refresh="true" action="territoryController.do?saveTerritory">
	<input name="id" type="hidden" value="${territoryView.id}">
	<fieldset class="step">
        <div class="form">
            <label class="Validform_label"> <t:language langKey="area.name"/>: </label>
            <input name="territoryName" class="inputxt" value="${territoryView.territoryName}" datatype="s2-15">
            <span class="Validform_checktip"><t:language langKey="areaname.rang2to15"/></span>
        </div>
        <div class="form">
            <label class="Validform_label"> <t:language langKey="area.level"/>: </label>
            <select name="territoryLevel" id="territoryLevel" datatype="*">
                <option value="0" <c:if test="${territoryView.territoryLevel eq 0}">selected="selected"</c:if>><t:language langKey="main.area"/></option>
                <option value="1" <c:if test="${territoryView.territoryLevel>0}"> selected="selected"</c:if>><t:language langKey="sub.area"/></option>
            </select>
            <span class="Validform_checktip"></span>
        </div>
        <div class="form" id="pfun">
            <label class="Validform_label"> <t:language langKey="parent.area"/>: </label>
            <input id="cc"
                <c:if test="${territoryView.TSTerritory.territoryLevel eq 0}"> value="${territoryView.TSTerritory.id}"</c:if>
                <c:if test="${territoryView.TSTerritory.territoryLevel > 0}"> value="${territoryView.TSTerritory.territoryName}"</c:if>>
            <input id="territoryId" name="Territory.id" style="display: none;" value="${territoryView.TSTerritory.id}">
        </div>
        <div class="form" id="codediv">
            <label class="Validform_label"> <t:language langKey="area.code"/>: </label>
            <input name="territoryCode" class="inputxt" value="${territoryView.territoryCode}">
        </div>
        <div class="form" id="orderdiv">
            <label class="Validform_label"> <t:language langKey="display.order"/>: </label>
            <input name="territorySort" class="inputxt" value="${territoryView.territorySort}">
        </div>
	</fieldset>
</t:formvalid>
</body>
</html>
