# UI标签库帮助文档

##  BaseTag

标签用途： 样式表和JS引入标签

示例：

```
<t:base type="jquery,easyui,tools"></t:base>
```

参数

| 属性名 | 类型 |描述 |
|  ----  | ----  |----  |
|type	|string	|JS插件类型定义如果有多个以逗号隔开，必输字段|
|cssTheme	|string	|easyui theme（目录 默认default），非必须输入|

>JS插件类型标记说明

参数：

|插件名|	描述|
|  ----  | ----  |
|jquery	|引入版本jquery-1.8.3|
|easyui	|引入版本jquery.easyui.1.3.1及自定义扩展JS|
|DatePicker	|引入版本My97DatePicker4.8 Beta2|
|jqueryui	|引入版本jquery-ui-1.9.2|
|prohibit	|常用浏览器操作JS函数如禁用右键菜单，禁用回退，禁用F5|
|tools	|artDialog4.1.6弹出窗及常用CURD操作函数|
|ckeditor	|在需要加载t:ckeditor时引入ckeditor所需要的js|
|autocomplete	|Jquery 自动补全功能JS|
|jeasyuiextensions	|jQuery && jEasyUI 扩展功能集合|


## DataGrid

标签用途： 数据列表标签

示例：
```
<t:datagrid name="${typegroup.typegroupcode}List" title="类型列表" 
actionUrl="systemController.do?typeGrid&typegroupid=${typegroup.id}" idField="id" queryMode="group" sortOrder="desc" sortName="id">
```

参数:

| 属性名 | 类型|描述	| 是否必须| 	默认值| 
|  ----  | ----  |----|----|----|
| name|	string	|表格唯一标示|	是	|null|
| treegrid|	boolean	|是否是树形列表|	否	|FALSE|
| autoLoadData	|boolean|	数据列表是否自动加载|	否|	TRUE|
| queryMode|	string	|查询模式：组合查询= group，单查=single|	否	|single|
| actionUrl	|string	|从远程请求数据的地址|	是|	null|
| pagination|	boolean|	是否显示分页条|	否|	TRUE|
| title|string	|表格标题|	否|	null|
| idField|	string|	标识字段，或者说主键字段|	否	|null|
| width	|num|	表格宽度	|否|	auto|
| height|	num	|表格高度|	否	|auto|
| checkbox|	boolean	|是否显示复选框|	否|	FALSE|
| fit|	boolean|	是否允许表格自动缩放，以适应父容器|	否|	TRUE|
| sortName|	string|	定义的列进行排序	|否	|null|
| sortOrder|	string|	定义列的排序顺序，只能是"递增"或"降序（asc,desc）|	否	|asc|
| fitColumns|	boolean	|当为true时，自动展开/合同列的大小，以适应的宽度，防止横向滚动|	否	|TRUE|
| showPageList|	boolean|	是否显示分页条数下拉框|	否|	TRUE|
| showRefresh|	boolean	|是否显示刷新按钮|	否	|TRUE|
| showText|	boolean|	是否显示分页文本内容|	否	|TRUE|
| style	|string|	插件类型有easyui和datatable2种|	否	|easyui|
| pageSize|	num|	每页显示的记录数|	否|	10|
| openFirstNode|	boolean|	是不是展开第一个节点,在树形情况下,true展开,false不展开默认false|	否	|TRUE|
| entityName|	string	|对应的实体对象,如果entity和controller都是规则的可以不填,自动补全标签做关联|	否	|null|
| rowStyler|	string|	行 css函数 指定名称就可以,调用为 functionName(index,row)这样调用	|否	|null|
| extendParams	|string	|datagrid 的扩展字段,如果easyui上面有但是jeecg没有这个属性可以自己添加	|否|	null|
| queryBuilder|	boolean	|是否使用高级查询器|	否|	FALSE|
| isShowSearch|	boolean|	是否显示检索框收缩按钮|	是|	TRUE|
| treeField|	string|	树形列表展示列|	否|	null|
| singleSelect|	boolean	是否单选true,false|	否|	FALSE|
| btnCls|	string|	列表上方button样式class属性|	否	|null|
| nowrap|	boolean	列表内容是否可换行(false：可换行)|	否|	TRUE|
| isShowSubGrid|	boolean|	是否显示子表数据|	否|	FALSE|
| configId|	string|	主表表名	否|	null|

> 配置说明

主子表补充说明
  *  1.isShowSubGrid设置为true,configId设置为主表表名(对应online表单中表名字段); 
  *  2.子表中外键字段在online表单-页面属性-是否查询设置为是;


### Column

(列) 子标签

示例：
```
<t:dgCol title="用户名" sortable="false" field="userName" query="true"></t:dgCol>
```

参数：

| 属性名| 类型| 描述| 是否必须| 默认值|
| -------- | ----- | ---- |--- |--- |
| title| string| 列标题文字| 是| null|
| field| string| 列字段名称(操作列字段为opt)| 否| null|
| width| num| 列宽度| 否| auto|
| rowspan| num| 字段跨列| 否| auto|
| colspan| num| 字段跨行| 否| auto|
| queryMode| string| 字段范围查询queryMode="group"| 否| null|
| align| string| 数据对齐方式,可选值有:left,right,center| 否| left|
| sortable| boolean| 该列是否排序| 否| TRUE|
| checkbox| boolean| 是否显示复选框| 否| FALSE|
| formatter| string| 时间格式化: yyyy-MM-dd / yyyy-MM-dd hh:mm:ss| 否| null|
| formatterjs| string| 自定义函数名称(调用页面自定义js方法 参数为(value,row,index),优先级大于formatter属性)，使用示例：formatterjs=”test”//自定义js方法function test(value,row,index){ return value;}| 否| null|
| hidden| boolean| 是否隐藏该列| 否| FALSE|
| replace| string| 列值替换| 否| null|
| treefield| string| 树形数据表对应模型字段| 否| null|
| image| boolean| 该列是否是图片| 否| FALSE|
| frozenColumn| boolean| 是否冰冻列| 否| FALSE|
| query| boolean| 是否把该列作为查询字段| 否| FALSE|
| function| string| 链接的自定义函数例：fun(title,url)| 否| openwindow|
| url| string| 给该列加链接| 否| null|
| extend| string| 自定义查询字段的html属性（在extend中填写的内容使用json格式。在extend中自定义的属性会作为html属性添加到查询框input内）举例：extend="{style:'width:200px'}"| 否| Null|
| imageSize| string| Image参数的增强，可以设置显示图片的大小：imageSize(width,height)| 否| null|
| downloadName| string| 是否显示附件下载链接 例：downloadName(‘点击下载’)| 否| null|
| langArg| string| 多语言参数| 否| null|
| dictionary| string| 数据字典组编码 或 自定义字典(格式：表名,编码,显示文本)| 否| null|
| style| string| td CSS 属性| 否| null|
| autocomplete| boolean| 自动补全,true 自动补全,false不补全,默认false 填写当前字段| 否| FALSE|
| extendParams| string| datagrid field 的扩展字段,如果easyui上面有但是jeecg没有这个属性可以自己添加，添加规则和easyui的field一致| 否| null|
| popup| boolean| 是否启用popup模式| 否| FALSE|
| editor| string| 高级查询器用的字段编辑器| 否| null|
| showLen| string| 列表内容显示长度，超长截取...| 否| Null|
| defaultVal| string| 列表自动生成查询条件默认值| 否| Null|
| showMode| string| 列表查询字段生成模式：radio/checkbox/select| 否| select|
| newColumn| boolean| 是否另起一行| 否| FALSE|

### Operate

(操作)子标签

#### t:dgToolBar
 列表工具条标签 

示例：

<t:dgDefOpt url="systemController.do?viewFile&fileid={id}&subclassname={subclassname}" title="下载"></t:dgDefOpt>

参数：

|参数名|描述|
|--- |--- |
url|方法请求地址
id|控件ID
operationCode|权限操作码，对应按钮权限配置（不设置该字段表示不进行按钮权限控制）
icon|图标
title|标题
function|自定义函数
langArg|多语言参数
widht|弹出窗宽度(百分比)默认100%
height|弹出窗高度(百分比)默认100%
onclick|工具条选项onclick事件

####  t:dgFunOpt

自定义函数操作标签 

示例：

<t:dgFunOpt function="delCgForm(id,tableName)" title="删除" urlclass="ace_button" urlfont="fa-trash-o"></t:dgFunOpt>
参数：

|参数名|描述|
|--- |--- |
function|自定义函数可传出任意字段
title|操作标题
operationCode|权限操作码，对应按钮权限配置（不设置该字段表示不进行按钮权限控制）
exp|是否显示表操作的表达式 例: 字段名#表达式符号#字段值(name#eq#admin),表达式类型支持eq,ne,empty
langArg|多语言参数
urlclass|自定义链接风格
urlfont|自定义链接图标
urlStyle|自定义链接样式,直接写style里的内容; e.g: (background-color:#18a689;)

#### t:dgDelOpt

删除操作标签 

示例:

<t:dgDelOpt url="userController.do?delUser&id={id}" title="删除"></t:dgDelOpt>
参数：

|参数名|描述|
|--- |--- |
url|删除请求地址参数形式 id={id}
operationCode|权限操作码，对应按钮权限配置（不设置该字段表示不进行按钮权限控制）
title|操作标题
message|询问内容
exp|是否显示表操作的表达式 例: 字段名#表达式符号#字段值(name#eq#admin),表达式类型支持eq,ne,empty
function|自定义函数名称
langArg|多语言参数
urlclass|自定义链接风格
urlfont|自定义链接图标
urlStyle|自定义链接样式,直接写style里的内容; e.g: (background-color:#18a689;)

#### t:dgOpenOpt

弹出窗操作标签

示例:

<t:dgOpenOpt url="expertController.do?expert&id={id}" title="弹出窗"></t:dgOpenOpt>
参数：

|参数名|描述|
|--- |--- |
url|弹出页面地址
title|操作标题
operationCode|权限操作码，对应按钮权限配置（不设置该字段表示不进行按钮权限控制）
exp|是否显示表操作的表达式 例: 字段名#表达式符号#字段值(name#eq#admin),表达式类型支持eq,ne,empty
widht|弹出窗宽度(百分比)默认100%
height|弹出窗高度(百分比)默认100%
openModel|弹出方式[OpenWin/OpenTab]
urlclass|自定义链接风格
urlfont|自定义链接图标
urlStyle|自定义链接样式,直接写style里的内容; e.g: (background-color:#18a689;)

####  t:dgConfOpt

询问操作标签 |

示例:

<t:dgConfOpt title="激活" url="cgformFtlController.do?active&id={id}&formId=${formid}" exp="ftlStatus#eq#0" message="确认激活模板"/>
参数：

|参数名|描述|
|--- |--- |
url|弹出页面地址
title|操作标题
operationCode|权限操作码，对应按钮权限配置（不设置该字段表示不进行按钮权限控制）
message|询问内容
exp|是否显示该操作的表达式, 例: 字段名#表达式符号#字段值(name#eq#admin),表达式类型支持eq,ne,empty
urlclass|自定义链接风格
urlfont|自定义链接图标
urlStyle|自定义链接样式,直接写style里的内容; e.g: (background-color:#18a689;)

#### dgDefOpt

操作标签 | dgDefOpt(列表URL操作)

示例：

<t:dgDefOpt url="systemController.do?viewFile&fileid={id}&subclassname={subclassname}" title="下载"></t:dgDefOpt>

参数：

| 属性名| 类型| 描述| 是否必须|
| -------- | ----- | ---- |
| url| string| 列表操作URL容器的ID| 是|
| title| string| 操作标题| 是|
| exp| string| 是否显示改链接的表达式,例 字段名#表达式符号#字段值(name#eq#admin)表达式类型支持eq,ne,empty| 否|
| operationCode| string| 按钮操作Code| 否|
| urlclass| | 自定义链接样式| 否|
| urlfont| | 自定义链接图标样式| 否|
| urlStyle| | 链接样式,直接写style里的内容; e.g: (background-color:#18a689;)| 否|

#### exp

操作标签 | exp参数高级用法

[1]、支持exp有多个值判断，比如 state  in(1,4,5,6,8)
语法：status#eq#N,Y  多个以逗号隔开
[2]、判断空值判断
语法：status#empty#true  非空false



## formvalid

FormValidation(表单提交及验证标签)

示例(div)：
```
<t:formvalid formid="formobj" layout="div" dialog="true" action="roleController.do?saveRole">
 <fieldset class="step">
     <div class="form">
      <label class="Validform_label">字段标题:</label>
      <input name="roleName" class="inputxt" value="${role.roleName }" datatype="s2-8">
      <span class="Validform_checktip">字段说明</span>
     </div>
</fieldset>
</t:formvalid>

```


参数：

|属性名|类型|描述|是否必须|默认值|
| -------- | ----- | ---- |---- |---- |
action|string|表单提交路径|否|null
formid|string|表单唯一标示|是|formobj
refresh|boolean|dialog为true时是否刷新父页面|否|TRUE
callback,如果dialog="false"|string|表单提交完成后的回调函数,如果dialog="true"的话, callback="@Override functionName" 调用的是当前页面的方法 , callback="functionName" 调用的是父页面的方法, callback="functionName" 调用的是当前页面的方法|否|null
beforeSubmit|string|表单提交前的处理函数|否|null
btnsub|string|触发表单提交事件的按钮ID|否|btn_sub
btnreset|string|触发表单重置事件的按钮ID|否|btn_reset
layout|string|表单布局方式(div和table可选)|是|div
usePlugin|string|表单外调插件名称(可选插件,jqtransform:表单美化)|否|null
dialog|boolean|是否是弹出窗口模式|是|TRUE
tabtitle|string|表单布为div时多选项卡布局分组标题|否|null
tiptype|string|表单校验提示方式|否|4
styleClass|string|css class|否|null
cssTheme|string|formdiv.css主题目录|否|null

### tiptype 

表单校验提示方式(tiptype)

示例：
```
<t:formvalid formid="formobj" layout="table" dialog="true" action="roleController.do?save">
 <tr>
     <td align="right" width="10%" nowrap>
      <label class="Validform_label">
       字段标题:
      </label>
     </td>
     <td class="value" width="10%">
      <input id="realName" class="inputxt" name="realName" value="${user.realName }" datatype="s2-10">
      <span class="Validform_checktip">字段说明</span>
     </td>
</tr>
</t:formvalid>
```


参数：

|序号|功能描述|
|---- |---- |
1|自定义弹出框提示；onblur的时候就会提示，当输入正确后，1秒中后会自动消失。
2|侧边提示(会在当前元素的父级的next对象的子级查找显示提示信息的对象，表单以ajax提交时会弹出自定义提示框显示表单提交状态)；
3|侧边提示(会在当前元素的siblings对象中查找显示提示信息的对象，表单以ajax提交时会弹出自定义提示框显示表单提交状态)；
4|侧边提示(会在当前元素的父级的next对象下查找显示提示信息的对象，表单以ajax提交时不显示表单的提交状态)


## Upload

Upload(上传标签)

说明：
该上传标签是老实现方式，不好用，建议使用轻量级标签WebUploader标签。
示例：
```
<t:upload name="instruction" dialog="false" queueID="instructionfile" view="true" auto="true" uploader="systemController.do?save" extend="pic" id="instruction" formData="documentTitle">
</t:upload>
```

参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
id|string|上传控件唯一标示|是|null
name|string|控件name|是|null
formData|string|上传文件提交后台的其他表单参数取ID|否|null
uploader|string|上传提交路径|是|null
extend|string|上传文件扩展名(可选类型组1,pic[.jpg;,jpeg;.png;.gif;.bmp;.ico;.tif],2,office[.doc;.docx;.txt;.ppt;.xls;.xlsx;.html;*.htm ])|是|
buttonText|string|控件按钮显示文本|否|浏览
multi|boolean|是否允许选择多文件|否|TRUE
queueID|string|显示预上传文件列表的对象ID|否|TRUE
dialog|boolean|是否是对话框模式打开上传控件|否|TRUE
callback|string|所有文件上传完成后回调函数|否|null
auto|boolean|是否是自动上传|否|TRUE
onUploadSuccess|string|上传成功的处理函数|否|null
view|string|是否生成查看删除链接默认false,如为true需要在后台返回JSON中添加查看参数(viewhref)和删除参数(delurl)|否|null
formId|string|后台接受表单id, 不可与formData同时使用|否|null
fileSizeLimit|string|上传文件大小设置|否|15MB
outhtml|boolean|是否生成HTML（true/false）一个页面多个upload字段支持|否|TRUE
onUploadStart|string|上传之前处理函数,只传函数名,该函数有个参数为file对象|否|null

## Tabs
 
Tabs容器父标签参数

参数：


|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
id|string|控件唯一标示|是|null
width|string|选项卡宽度|否|auto
heigth|string|选项卡高度|否|auto
plain|boolean|简单模式|否|FALSE
fit|boolean|是否适应父容器|否|FALSE
border|boolean|是否显示边框|否|TRUE
scrollIncrement|string|滚动像素数|否|TRUE
scrollDuration|boolean|每个卷轴动画的毫秒数|否|100
tools|string|选项卡工具条|否|400
tabs|boolean|是否创建父容器|否|TRUE
iframe|string|是否是iframe方式创建|否|null
tabPosition|string|选项卡位置: 'top','bottom','left','right'|否|top

示例：
```
<t:tabs id="tt" iframe="false" tabPosition="bottom">
 <t:tab iframe="demoController.do?aut" icon="icon-search" title="自动上传" id="o"></t:tab>
 <t:tab iframe="demoController.do?" icon="icon-search" title="普通上传" id="t"></t:tab>
</t:tabs>
```

### Tab

Tab子标签参数
参数：
|属性名|类型|描述|是否必须|
|---- |---- |---- |---- |
id|string|控件唯一标示|是
href|string|Href方式请求地址|否
iframe|string|Iframe方式请求地址|否
title|string|选项卡标题|是
icon|string|选项卡图标|否
width|string|选项卡宽度|否
heigth|string|选项卡高度|否
closable|boolean|是否带关闭按钮|否


## Autocomplete

Autocomplete(自动补全标签)
示例：

<t:autocomplete selectfun="aa" closefun="close" valueField="id" searchField="userName" labelField="userName,realName" name="user" entityName="TSUser" datatype="*" nullmsg="请输入关键字" errormsg="数据不存在,请重新输入">  
</t:autocomplete>
参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
name|string|input 的ID/NAME|是|null
entityName|string|查询Hiber实体名|是|null
searchField|string|查询关键字字段|是|null
minLength|string|触发提示文字长度|否|1
defValue|string|默认显示值|否|null
datatype|string|数据验证类型|否|null
nullmsg|string|数据为空时验证|否|null
errormsg|string|数据格式不对时验证|否|null
parse|string|转换数据|否|null
formatItem|string|格式化要显示的数据|否|null
result|string|选择后回调方法|否|null
maxRows|string|最多显示条数|否|10
dataSource|string|数据源请求通用URL|否|systemController.do?getAutoList

## ComboTree

ComboTree (下拉树形选择框)
示例：

<t:comboTree url="jeecgUitagController.do?getComboTreeData" value="402880e447e99cf10147e9a03b320003" name=" depid " id="depid" width="200"></t:comboTree> 
参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
name|string|input 控件name|是|null
url|string|请求数据URL|是|null
id|string|input 控件id|是|null
width|string|控件宽度|否|140PX
value|string|默认赋值|否|null
multiple|boolean|是否多选|否|FALSE
onlyLeafCheck|boolean|是否只选择子节点|否|FALSE

## ComboBox 

 ComboBox (下拉选择框)

Menu(左侧菜单生成标签)
示例：

<t:menu parentFun="${parentFun}" childFun="${childFun} " ></t:menu>
参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
style|string|菜单样式|否|null
parentFun|string|一级菜单|是|null
childFun|string|二级菜单|是|null
menuFun|string|菜单信息|否|null

说明：
此标签不通用，建议开发者不要使用。


## Choose

Choose(弹出选择标签)
示例：

```
<input id="roleid" name="roleid" type="hidden" value="${id}"/>
<input name="roleName" id="roleName" class="inputxt" value="${roleName }" readonly="readonly" datatype="*" />
<t:choose hiddenName="roleid" hiddenid="id" textname="roleName" url="userController.do?roles" name="roleList" icon="icon-search" title="选中角色" isclear="true" isInit="true"></t:choose> 
```

参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
title|string|标题|否|null
hiddenName|string|隐藏域的ID|是|null
hiddenid|string|选中数据返回值|是|null
textname|string|选中数据返回显示文本|否|null
url|string|远程访问地址|是|null
name|string|弹出列表datagrid的ID名|是|null
isInit|boolean|是否传入初始化值|否|null
isclear|boolean|是否带清空按钮|否|FALSE
icon|string|选择按钮的图标|否|null
height|string|弹出框的高度|否|null
width|string|弹出框的宽度|否|null
left|string|弹出框的左间距|否|null
top|string|弹出框的上端间距|否|null
fun|string|自定义函数|否|null
langArg|string|多语言参数|否|null
inputTextname|string|显示域控件的ID|否|null

## Form

（form标签）
参数 属性名 类型 描述 是否必须 默认值
action string 表单提交地址 否
items string  循环集合值   是

* 用法
```
<t:form action=＂userAction＂  items =null></t:form>
```


## ComboTree

(下拉树形选择框)
   示例：
```
  <t:comboTree url="jeecgUitagController.do?getComboTreeData" value="402880e447e99cf10147e9a03b320003" name=" depid " id="depid" width="200"></t:comboTree> 
```

   参数：
   
|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
name|string|input 控件name|是|null
url|string|请求数据URL|是|null
id|string|input 控件id|是|null
width|string|控件宽度|否|140PX
value|string|默认赋值|否|null
multiple|boolean|是否多选|否|FALSE
onlyLeafCheck|boolean|是否只选择子节点|否|FALSE

# Colorchange
 
 (改变HTML控件颜色)G

# DictSelect 

(数据字典下拉选择框)
示例：

<t:dictSelect field="sex" typeGroupCode="sex" title="性别"></t:dictSelect> 
参数：

|属性名|类型|描述|是否必须|默认值|
|---- |---- |---- |---- |---- |
title|string|标题|否|null
field|string|控件字段名字|是|null
typeGroupCode|string|字典分组编码|是|null
type|string|控件类型select、radio、checkbox|否|select
id|string|唯一标识|否|null
defaultVal|string|默认值|否|null
divClass|string|DIV框默认样式|否|form
labelClass|string|LABEL默认样式|否|Validform_label
dictTable|string|自定义字典表|否|null
dictField|string|自定义字典表的匹配字段-字典的编码值|否|null
dictText|string|自定义字典表的显示文本-字典的显示值|否|null
dictCondition|string|自定义字典表的显示文本-字典查询条件|否|null
extendJson|string|扩展参数(json格式)|否|null
hasLabel|boolean|是否显示Label|否|FALSE
readonly|string|是否可编辑。当值”readonly”时不可编辑。|否|null
datatype|string|validform校验规则，一般必须输入：*|否|null