# 开发指南
## 项目说明
JFaster 是一套SH（Spring + Hibernate）快速开发框架，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。帮你快速构建高质量的业务系统，是技术选型最理想的选择。

## 快速使用

#### 项目数据源配置

修改项目的数据源连接配置文件 resources/dbconfig.properties

#### 项目启动测试

 配置完成之后，启动Tomcat，在浏览器地址栏中输入*http://localhost:8080/JFaster/*，打开
登陆:输入用户名密码 admin/123456，登陆进入主界面，


#### 运行项目

 在工程目录上面右键-\>Run As-\>7 Maven Build，在弹出的运行设置的Goals中填

写”tomcat:run”，如果在运行时，不需要跑单元测试程序，可以把Skip Test给勾选上，


#### 项目打包

 在工程上面右键-\>Run As-\>Maven Package，打包完成之后的war包位于target/JFaster.war
 
 
 

###  代码生成器的使用

#### 数据表创建

现在有一张员工表 person，其建表 SQL为：
```
CREATE TABLE \`person\` (

\`ID\` varchar(32) NOT NULL default '' COMMENT '主键',

\`NAME\` varchar(32) default NULL COMMENT '用户名',

\`AGE\` int(11) default NULL COMMENT '年龄',

\`SALARY\` decimal(10,2) default NULL COMMENT '工资',

\`createDt\` datetime default NULL COMMENT '创建时间',

PRIMARY KEY
(\`ID\`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```


> 注意：建表时，建议给每个字段加上注释，代码生成器会根据注释去生成页面字段对应的显示文本。

#### 代码生成器配置

 代码生成器有两个配置文件：一个用于数据源的配置，一个用于代码生成器的参数配置。

这两个配置文件分别是 resources/JFaster目录的 contextConfig.properties和database.properties

* database.properties
```
#mysql
diver_name=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/jfaster?useUnicode=true&characterEncoding=UTF-8
username=root
password=123456
database_name=jfaster
```

* 
```properties
project_path=E\:\\WorkSpace\\abocode\\jfaster\\
#bussi_package[User defined]
biz_package=com.abocode.jfaster.biz

#maven code path
source_root_package=src.main.java
webroot_package=src.main.webapp

#ftl resource url
templatepath=maker-config/template
system_encoding=utf-8

#Table key [User defined] 
generate_table_id=id
#Search Param num [User defined]
ui_search_filed_num=1

#convert flag[true/false]
filed_convert=true
#字段过滤
ui_filter_fields=create_date,create_datetime,create_by,create_key,create_name,create_realname,create_departmentid,create_departmentname,update_date,update_datetime,update_by,update_key,update_name,update_realname,update_departmentid,update_departmentname
```
####  使用

运行：com.abocode.codemaker.Run，根据提示进行配置




## 高级功能

### 架构技术
 Spring MVC+Hibernate4+UI快速开发库+Spring JDBC，数据库支持 Oracle/Mysql/Postgres数据库等主流数据库
### 主要功能说明
* 代码生成器
代码生成包括 JSP页面生成，代码无需修改，增删改查功能直接配置使用，代码生成器用于生成规范的后台代码+统一风格的前台页面+表单校验。
 A.前台页面字段对应数据库字段生成;
 B.页面字段校验自动生成（数字类型\\必须项\\金额类型\\时间控件\\邮箱\\手机号\\QQ号等等);

*  查询条件过滤器

页面加查询条件，后台不需要写任何逻辑判断，动态拼查询条件

*   UI快速开发库

 UI快速开发库，针对 WEB UI进行标准封装，页面统一采用 UI标签实现功能：数据datagrid,表单校验,Popup,Tab等，实现 JSP页面零JS，开发维护非常高效

*   表单Form校验

前台页面字段校验采用 Validform

*   常用共通封装

表单检验组件,数据字典/邮件发送，封装POI导入导出，读写Excel。
*  基础用户权限

权限功能：用户、角色、权限（菜单权限+按钮权限）

*  报表封装

Excel简易导出工具类+Highcharts图形报表


### 查询

#### 设置查询字段

具体实现

第一步：页面实现

说明：为 dategrid字段，追加属性 query="true"，自动加载出查询框

第二步：controller层处理


5.3.查询过滤器高级特性

 dategrid中的查询过滤器默认是单条件查询，即在设置多个 dgCol的
 query=”true”之后，

查询条件中同时只能有一个条件被使用，生成的页面效果

图 5-3默认查询过滤器效果

当然，可以通过 dategrid和 dgCol的参数设置来达到更高级的查询过滤功能，如组合查

询条件和值范围查询。
#### 组合条件查询

 设置\<t:dategrid\>标签的
 queryMode=”group”（该参数值默认为”single”，即单条件查询），

在页面生成时，会生成一个组合查询条件输入面板。生成的页面效果

#### 字段范围查询

 设置\<t:dgCol\>标签的
 queryMode=”group”，在页面生成时，会生成一个范围输入框。生


 字段范围查询会为该字段生成两个输入框，name分别为“字段名_begin”和“字段名

\_end”，具体的查询功能需要在后台接收这两个输入框的内容，并把查询条件加入到 HQL

中。示例如下：
```

@RequestMapping(params = "datagrid")
public void datagrid(JFasterDemo JFasterDemo,HttpServletRequest request,
HttpServletResponse response, DataGrid dataGrid) {
CriteriaQuery cq = new CriteriaQuery(JFasterDemo.class, dataGrid);
org.JFasterframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
JFasterDemo);
String ctBegin = request.getParameter("createTime_begin");
String ctEnd = request.getParameter("createTime_end");
if(ctBegin!=null && ctEnd!=null){
 cq.ge("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(ctBegin));
 cq.le("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(ctEnd));
 cq.add();
}
 this.JFasterDemoService.getDataGridReturn(cq, true);
 TagUtil.datagrid(response, dataGrid);
}
```


 在控制器中用 request接收传递到后台的查询条件，或者直接在方法参数列表里填上，springmvc会帮我们获得。
 然后将得到的范围查询条件添加到 CriteriaQuery对象中，最后调用 CriteriaQuery的add()方法加载生成 hql。
至此，范围查询就完成了。

#### 查询字段添加日期控件

例如，要给创建日期的范围查询条件框添加日期控件，首先为创建日期添加范围查询：

```
<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"

query="true" queryMode="group"\>\</t:dgCol\>

用 jquery为生成的 createTime_start和 createTime_end两个输入框添加日期控件。

\$(document).ready(function(){

\$("input[name='createTime_begin']").attr("class","easyui-datebox");

\$("input[name='createTime_end']").attr("class","easyui-datebox");

);
```



#### 日期字段的数据格式化

在 dategrid中，对于日期字段，可以通过设置\<d:dgCol\>的 formatter属性配置格式化方

式，实现对日期数据的格式化，如：
```
<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"
query="true" queryMode="group"\>\</t:dgCol\>
```

 对于日期的格式化方式，可以参考 JDK参考手册中 SimpleDateFormat中对于日期和时间模式的说明

#### 数据列表合计功能

 进行数据的列表展示时，为数据显示合计数是一个很有用的功能，在 JFaster的datagrid

该功能的实现，主要是通过在加载 datagrid的数据时，统计出所需的合计值，并放在datagrid对象的 footer中。示例代码如下：
```
@RequestMapping(params = "datagrid")、
**public void** datagrid(JFasterDemo JFasterDemo,HttpServletRequest request,
HttpServletResponse response, DataGrid dataGrid) {
CriteriaQuery cq = **new** CriteriaQuery(JFasterDemo.**class**, dataGrid);
//查询条件组装器
org.JFasterframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,JFasterDemo);
String ctBegin = request.getParameter("createTime_begin");
String ctEnd = request.getParameter("createTime_end");
if(StringUtil.isNotEmpty(ctBegin)&&StringUtil.isNotEmpty(ctEnd)){
cq.ge("createTime", **new**
SimpleDateFormat("yyyy-MM-dd").parse(ctBegin));
cq.le("createTime", **new**
SimpleDateFormat("yyyy-MM-dd").parse(ctEnd));
cq.add();
this.JFasterDemoService.getDataGridReturn(cq, **true**);
String total_salary =String.valueOf(JFasterDemoService.findOneForJdbc("select sum(salary) as ssum from JFaster_demo").get("ssum"));
dataGrid.setFooter("salary:"+total_salary+",age,email:合计");
TagUtil.datagrid(response, dataGrid);
}
```

在该示例代码中，需要重点注意的是这里的第 23行：
dataGrid.setFooter("salary:"+total_salary+",age,email:合计");
setFooter()方法接收一个字符串，其格式为格式：字段名[:值]，其中值为选填项，填了
则使用给定的值，没填则自动统计分页合计，示例：
salary:35.00,age,email:合计
 这里将 salary的合计值通过查询数据库得出，而 age则通过当前分页数据自动合计，
email给定一个值“合计”，其作用是在 datagrid对应于 email列的下方显示一个说明信息。

### 表单校验
详细参考 Validform官网

####  Validform使用入门

1、引入 css

 请查看下载文件中的 style.css，把里面 Validform必须部分复制到你的
 css中（文件里这

个注释 "/\*==========以下部分是
Validform必须的===========\*/"之后的部分是必须的）。

(之前发现有部分网友把整个 style.css都引用在了页面里，然后发现样式冲突了)

 2、引入 js（jquery 1.4.2以上版本都可以）

3、给需要验证的表单元素绑定附加属性

4、初始化，就这么简单

注：

 1、Validform有非压缩、压缩和 NCR三个版本提供下载，NCR是通用版，当你页面因编

码问题，提示文字出现乱码时可以使用这个版本；

 2、Validform没有限定必须使用 table结构，它可以适用于任何结构，需要在
 tiptype中

定义好位置关系。

#### 绑定附加属性

 凡要验证格式的元素均需绑定 datatype属性，datatype可选值内置有 10类，用来指定

不同的验证格式。

 如果还不能满足您的验证需求，可以传入自定义 datatype，自定义
 datatype是一个非常

强大的功能，通过它可以满足你的任何需求。

 可以绑定的附加属性有：datatype、nullmsg、sucmsg、errormsg、ignore、recheck、tip、

altercss、ajaxurl和 plugin等。

绑定方法如下所示：
说明：

内置基本的 datatype类型有： \* \| \*6-16 \| n \| n6-16 \| s \| s6-18 \| p \| m
\| e \| url

\*：检测是否有输入，可以输入任何字符，不留空即可通过验证；

\*6-16：检测是否为 6到 16位任意字符；

n：数字类型；

n6-16：6到 16位数字；

s：字符串类型；

s6-18：6到 18位字符串；

p：验证是否为邮政编码；

m：手机号码格式；

e：email格式；

url：验证字符串是否为网址。

 自定义 datatype的名称，可以由字母、数字、下划线、中划线和\*号组成。

 形如"\*6-16"的
 datatype，Validform会自动扩展，可以指定任意的数值范围。如内置基

本类型有"\*6-16"，那么你绑定 datatype="\*4-12"就表示 4到
12位任意字符。如果你自定义

了一个 datatype="zh2-4"，表示 2到 4位中文字符，那么 datatype="zh2-6"就表示 2到 6

位中文字符。
 5.2版本之后，datatype支持规则累加或单选。用","分隔表示规则累加；用"\|"分隔表示

规则多选一，即只要符合其中一个规则就可以通过验证，绑定的规则会依次验证，只要验证

通过，后面的规则就会忽略不再比较。如绑定
datatype="m\|e"，表示既可以填写手机号码，

也能填写邮箱地址，如果知道填入的是手机号码，那么就不会再检测他是不是邮箱地址；

datatype="zh,s2-4"，表示要符合自定义类型"zh"，也要符合规则"s2-4"。

 注：

5.2.1版本之后，datatype支持：

 直接绑定正则：如可用这样写 datatype="/\\w{3,6}/i"，要求是 3到
 6位的字母，不区分

大小写；

支持简单的逻辑运算：如

datatype="m \| e, \*4-18 \| /\\w{3,6}/i \| /\^validform\\.rjboy\\.cn\$/"，

 这个表达式的意思是：可以是手机号码；或者是邮箱地址，但字符长度必须在 4到 18

位；或者是 3到 6位的字母，不区分大小写；或者输入
validform.rjboy.cn，区分大小写。

这里","分隔相当于逻辑运算里的"&&"；
"\|"分隔相当于逻辑运算里的"\|\|"；不支持括号运算。

 **nullmsg**

当表单元素值为空时的提示信息，不绑定，默认提示"请填入信息！"。

如：nullmsg="请填写用户名！"

 5.3版开始，对于没有绑定 nullmsg的对象，会自动查找 class为
 Validform_label下的

文字作为提示文字:

如这样的 html结构：

 \<span class="Validform_label"\>\*用户名：\</span\>\<input type="text"
 val=""

datatype="s" /\>

 当这个文本框里没有输入时的出错信息就会是："请输入用户名！"

 这里 Validform_label跟 input之间的位置关系，不一定是要同级关系，同级里没有找

到的话，它还会在同级的子级、父级的同级、父级的同级的子级里查找。

 **sucmsg** 5.3+

当表单元素通过验证时的提示信息，不绑定，默认提示"通过信息验证！"。

如：sucmsg="用户名还未被使用，可以注册！"

 5.3版开始，也可以在实时验证返回的 json数据里返回成功的提示文字，请查看附加属

性 ajaxurl的介绍。
**errormsg**

输入内容不能通过验证时的提示信息，默认提示"请输入正确信息！"。

如：errormsg="用户名必须是 2到 4位中文字符！"

5.3版开始，Validform可以根据你绑定的datatype智能的输出相应出错信息，具体介

绍请查看*tipmsg*

**ignore**

绑定了 ignore="ignore"的表单元素，在有输入时，会验证所填数据是否符合 datatype

所指定数据类型，

没有填写内容时则会忽略对它的验证；

**recheck**

表单里面经常需要检查两次密码输入是否一致，recheck就是用来指定需要比较的另外

一个表单元素。

如：recheck="password1"，那么它就会拿当前元素的值跟该表单下，name为"password1"

的元素比较。

**tip**

表单里经常有些文本框需要默认就显示一个灰色的提示文字，当获得焦点时提示文字消

失，失去焦点时提示文字显示。tip属性就是用来实现这个效果。它通常和 altercss搭配使

用。

 如\<input type="text" value="默认提示文字" class="gray intxt"
 tip="默认提示文字"

altercss="gray" /\>

**altercss**

 它需要和 tip属性配合使用，altercss指定的样式名，会在文本框获得焦点时被删除，

没有输入内容而失去焦点时重新加上。

**ajaxurl**

指定 ajax实时验证的后台文件的地址。

 后台页面如 valid.php文件中可以用 \$_POST["param"]接收到值，Ajax中会 POST过

来变量 param和 name。param是文本框的值，name是文本框的 name属性。

 5.2版本开始，可以在 ajaxurl指定的 url后绑定参数，如：

ajaxurl="valid.php?myparam1=value1&myparam2=value2"；


 5.3之前的版本中，该文件输出的字符会作为错误信息显示在页面上，如果验证通过需

输出小写字母"y"。

 在 5.3版中，实时验证的返回数据做了调整，须是含有 status值的 json数据！跟
 callback

里的
ajax返回数据格式统一，建议不再返回字符串"y"或"n"。目前这两种格式的数据都兼容。

 注：

 如果 ajax校验通过，会在该元素上绑定 validform_valid值为
 true。可以通过设置该值

来控制验证能不能通过，如验证码的验证，第一次验证通过后，不小心右点击了下验证码图

片，验证码换了，但是仍然指示这个对象已经通过了验证，这时可以手动调整该值：

\$("\#name")[0].validform_valid="false"。

怎样设置ajax的参数，具体可以查看Validform对象的*config*方法。

**plugin**

指定需要使用的插件。

 5.3版开始，对于日期、swfupload和密码强度检测这三个插件，绑定了 plugin属性即

可以初始化对应的插件，可以不用在 validform初始化时传入空的 usePlugin了。

 如，你要使用日期插件，5.3之前版本需要这样初始化：

\$(".demoform").Validform({

 usePlugin:{

 datepicker:{}

}

});

5.3版开始，不需要传入这些空对象了，只需在表单元素上绑定 plugin="datepicker"就

可以，初始化直接这样：

\$(".demoform").Validform();

7.3.初始化参数说明

所有可用的参数如下：
```
\$(".demoform").Validform({

 btnSubmit:"\#btn_sub",

 btnReset:".btn_reset",

 tiptype:1,

ignoreHidden:false,

 dragonfly:false,

tipSweep:true,

showAllError:false,

postonce:true,

ajaxPost:true,

datatype:{

"\*6-20": /\^[\^\\s]{6,20}\$/,

 "z2-4" : /\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d]{2,4}\$/,

 "username":function(ge ts,obj,curform,regxp){

 //参数
 gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp

为内置的一些正则表达式的引用;

var reg1=/\^[\\w\\.]{4,16}\$/,

reg2=/\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d]{2,8}\$/;

if(reg1.test(gets)){return true;}

if(reg2.test(gets)){return true;}

return false;

//注意 return可以返回 true或
false或字符串文字，true表示验证通过，返回字符串表示验

证失败，字符串作为错误提示显示，返回 false则用 errmsg或默认的错误提示;

},

"phone":function(){

// 5.0版本之后，要实现二选一的验证效果，datatype的名称不需要以 "option_"开头;

}

},

usePlugin:{

swfupload:{},

datepicker:{},

passwordstrength:{},

jqtransform:{

selector:"select,input"

}

},

beforeCheck:function(curform){

 //在表单提交执行验证之前执行的函数，curform参数是当前表单对象。

 //这里明确 return false的话将不会继续执行验证操作;

},

beforeSubmit:function(curform){

//在验证成功后，表单提交前执行的函数，curform参数是当前表单对象。

//这里明确 return false的话表单将不会提交;

},

callback:function(data){

//返回数据 data是 json格式，{"info":"demo info","status":"y"}

//info:输出提示信息;

 //status:返回提交数据的状态,是否提交成功。如可以用"y"表示提交成功，"n"表示提交失败，在

ajax_post.php文件返回数据里自定字符，主要用在
callback函数里根据该值执行相应的回调操作;
 //ajax遇到服务端错误时也会执行回调，这时的 data是{ status:\*\*,
 statusText:\*\*, readyState:\*\*,

responseText:\*\* }；

//这里执行回调操作;

//注意：如果不是 ajax方式提交表单，传入 callback，这时
data参数是当前表单对象，回调函数会

在表单验证全部通过后执行，然后判断是否提交表单，如果 callback里明确 return
false，则表单不会提交，

如果 return true或没有 return，则会提交表单。

}

});

```
参数说明：【所有参数均为可选项】

 必须是表单对象执行 Validform方法，示例中".demoform"就是绑定在 form元素上的

class名称；

**btnSubmit**

 指定当前表单下的哪一个按钮触发表单提交事件，如果表单下有 submit按钮时可以省

略该参数。示例中".btn_sub"是该表单下要绑定点击提交表单事件的按钮；

 **btnReset**

".btn_reset"是该表单下要绑定点击重置表单事件的按钮;

**tiptype**

 可用的值有：1、2、3、4和 function函数，默认 tiptype为 1。( 3、4是
 5.2.1版本新

增)

1=\>自定义弹出框提示；

 2=\>侧边提示(会在当前元素的父级的 next对象的子级查找显示提示信息的对象，表单

以 ajax提交时会弹出自定义提示框显示表单提交状态)；

 3=\>侧边提示(会在当前元素的 siblings对象中查找显示提示信息的对象，表单以
 ajax

提交时会弹出自定义提示框显示表单提交状态)；

 4=\>侧边提示(会在当前元素的父级的 next对象下查找显示提示信息的对象，表单以

ajax提交时不显示表单的提交状态)；

 如果上面的 4种提示方式不是你需要的，你可以给 tiptype传入自定义函数。通过自定

义函数，可以实现你想要的任何提示效果：

tiptype:function(msg,o,cssctl){

//msg：提示信息;

//o:{obj:\*,type:\*,curform:\*},
 //obj指向的是当前验证的表单元素（或表单对象，验证全部验证通过，提交表单

时 o.obj为该表单对象），

 //type指示提示的状态，值为 1、2、3、4， 1：正在检测/提交数据，2：通过验

证，3：验证失败，4：提示 ignore状态,

//curform为当前 form对象;

//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的

对象和当前提示的状态（既形参 o中的 type）;

}

*demo*

tiptype不为 1时，Validform会查找 class为"Validform_checktip"的标签显示提示信息。

tiptype=1时，会自动创建弹出框显示提示信息。

Validform_checktip和表单元素之间的位置关系，会根据 tiptype的值有对应的结构，

上面已经做了说明。

5.3版本开始，如果页面里没有显示出错信息的标签，会根据 tiptype值自动创建

Validform_checktip对象。

**ignoreHidden**

可用值： true \| false。

默认为 false，当为 true时对:hidden的表单元素将不做验证;

**dragonfly**

可用值： true \| false。

默认 false，当为 true时，值为空时不做验证；

**tipSweep**

可用值： true \| false。

 默认为 false， 5.3版中做了修正，在各种 tiptype下，为
 true时提示信息将只会在表

单提交时触发显示，各表单元素 blur时不会触发信息提示；

 **showAllError**

可用值： true \| false。

 默认为
 false，true：提交表单时所有错误提示信息都会显示；false：一碰到验证不通

过的对象就会停止检测后面的元素，只显示该元素的错误信息；

 **postonce**
可用值： true \| false。

默认为 false，指定是否开启二次提交防御，true开启，不指定则默认关闭；

为 true时，在数据成功提交后，表单将不能再继续提交。

**ajaxPost**

可用值： true \| false。

默认为 false，使用 ajax方式提交表单数据，将会把数据 POST到 config方法或表单

action属性里设定的地址；

**datatype**

传入自定义 datatype类型，可以是正则，也可以是函数。

datatyp:{

"zh2-4":/\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d]{2,4}\$/,

"phone":function(gets,obj,curform,regxp){

 //参数 gets是获取到的表单元素值，

//obj为当前表单元素，

//curform为当前验证的表单，

 //regxp为内置的一些正则表达式的引用。

 //return false表示验证出错，没有 return或者 return true表示验证通过。

}

}

*demo*

**usePlugin**

 目前已整合swfupload、datepicker、passwordstrength和jqtransform四个插件，在这

里传入这些插件使用时需要传入的参数。datepicker在 Validform内调用时另外扩展了几个

比较实用的参数，具体请参考 demo页；

 **beforeCheck(curform)**

 在表单提交执行验证之前执行的函数，curform参数获取到的是当前表单对象。

函数 return false的话将不会继续执行验证操作;

 **beforeSubmit(curform)**

 在表单验证通过，提交表单数据之前执行的函数，data参数是当前表单对象。

函数 return false的话表单将不会提交;

**callback**

 在使用 ajax提交表单数据时，数据提交后的回调函数。返回数据 data是 Json格式：

{"info":"demo info","status":"y"}

info:输出提示信息，

 status:返回提交数据的状态,是否提交成功，"y"表示提交成功，"n"表示提交失败，在

ajax_post.php文件返回数据里自定字符，主要用在 callback函数里根据该值执行相应的回

调操作。你也可以在 ajax_post.php文件返回更多信息在这里获取，进行相应操作；

 如果不是 ajax方式提交表单，传入 callback，这时
 data参数是当前表单对象，回调函

数会在表单验证全部通过后执行，然后判断是否提交表单，如果 callback里 return
false，

则表单不会提交，如果 return true或没有 return，则会提交表单。

 5.3版开始，ajax遇到服务端错误时也会执行回调，这时的 data是{ status:\*\*,

statusText:\*\*, readyState:\*\*, responseText:\*\* }

### 代码生成器定制化

 code-maker是为JFaster提供的完善的代码生成器

* 项目配置文件修改：contextConfig.properties
* 数据库连接修改：database.properties
* 模版修改：maker-config/template/*
*  使用方法，运行：com.abocode.codemaker.Run即可
    
#### 枚举类型

新加枚举类型：CodeType

```
 public enum CodeType {
 controller("Controller"),

 service("Service"),
 serviceImpl("ServiceImpl"),

 entity("Entity"),
 repository("Repository"),
 repositoryImpl("RepositoryImpl"),
 newType("NewType");
 }   
```


#### 枚举类型路径
 
 添加枚举类型对应的代码生成路径：JFasterCodeFactory getCodePath()
 
```
 if("Controller".equalsIgnoreCase(codeType)) {
                    str.append(StringUtils.lowerCase("interfaces/web"));
    } else if("ServiceImpl".equalsIgnoreCase(codeType)) {
        str.append(StringUtils.lowerCase("application/service"));
    } else if("Service".equalsIgnoreCase(codeType)) {
        str.append(StringUtils.lowerCase("application"));
    } else if("RepositoryImpl".equalsIgnoreCase(codeType)) {
        str.append(StringUtils.lowerCase("domain/repository/persistence/hibernate"));
    } else if("Repository".equalsIgnoreCase(codeType)) {
        str.append(StringUtils.lowerCase("domain/repository"));
    } 
```  
        
#### 类型对应模版

添加代码生成器中使用模版：JFasterCodeGenerate.java  generateToFile()

``` 
codeFactory.invoke("repositoryImplTemplate.ftl", "repositoryImpl");
 codeFactory.invoke("repositoryTemplate.ftl", "repository");
```
     
    

### 使用规范

#### 数据库规范

* 表必须有唯一主键: ID（字符类型 32位）
* 主键支持自定义，修改JFaster_config.properties的参数[JFaster_generate_table_id]即可;
* 表必需字段（创建人，创建时间等..）
*表字段必须有注释

> 说明：

目前代码生成器默认的主键生成策略为 UUID
主表和子表的目录最好保持一致
2.子表和主表的外键规则如下：
a)主表和子表的外键字段名字，必须相同（除主键ID外）
b)子表引用主表主键ID作为外键，外键字段必须以_ID结尾




#### 编码规范

项目编码格式为 UTF-8(包括:java,jsp,css,js)

sevice接口命名：\*Service

service实现命名：\*ServiceImpl

entity命名：\*Entity

page页面 form命名：\*Page

action命名：\*Controller

项目没有 DAO, SQL写在 Service层

代码层次目录按照自动生成目录

 SQL文件目录和命名规范

3.
(1).所有 SQL必须大写，不允许用\*,全部替换为字段

(2).SQL文件根目录为:sql跟接口目录 Service是一个目录;

 例如:src\\sun\\sql,子目录跟 service必须保持一致

(3).SQL文件命名：[service名字]_[方法名字].sql

 数据库表设计规范

(1).主键字段为 id

(2).每个字段必须加备注

action中的方法

配置菜单的方法：以 go开头（其他方法不允许以 go开头）

触发业务逻辑的方法：以 do开头

页面跳转的：

以 to开头

6. Entity和数据库自定命名规范

 采用驼峰写法（每个单词首字母小写、其他字母小写的写法）转成中画线写法（所有

字母小写，单词与单词之间以中画线隔开）

10.2.详细说明


[1].SQL层讲解

 A.项目没有 DAO SQL写在 Service层，数据库取数和 DB操作通过 service层来实现

 B.如果使用硬代码 SQL,一个方法对应一个 SQL的话，可以采用框架封装的方式来存储SQL文件

（表示采用命名规范来存储 SQL）

存储方式：

(1).所有 SQL必须小写，不允许用\*,全部替换为字段

(2).SQL文件根目录为:src\\sun\\sql,子目录跟 service必须保持一致

(3).SQL文件命名：[service名字]_[方法名字].sql

读取方式：String sql = SqlUtil.getMethodSql(SqlUtil.getMethodUrl());

SQL定位方法：ctrl+shift+r参数：方法名，前面加\*

[2].Controller层页面数据封装

1.页面列表数据方法：datagrid

2.查询条件在 ACTION层 datagrid(pram)方法执行前加

10.3.举例讲解命名规范

例如：表名：JFaster_sys_demo

第一部分：代码文件命名规则如下：

首先：表名采用驼峰写法转换为 Java代码使用单词 JFaster_sys_demo =\>
JFasterSysDemo

[1].control命名 :JFasterSysDemoControl

[2].Servlice命名:JFasterSysDemoServiceI/JFasterSysDemoServiceImpl

[3].JSP命名

：JFaster-sys-demo.jsp(表单页面)

JFaster-sys-demo-list.jsp（列表页面）

JFaster-sys-demo-\*.jsp(新增表单页面例如：detail)

[4].control中方法命名：

 页面触发业务方法以 do\*开头

页面跳转方法以 go\*开头

（方法标签注释需和方法名保持一致）

[5].page/entity字段定义必须是对象类型

 int --\> Integer



#### 页面规范

* 列表页面，datagrid的 name属性不允许存在重复的，否则页面显示白板：
```
<t:dategrid name="JFasterDemoList" title="开发 DEMO列表" actionUrl="JFasterDemoController.do?datagrid" idField="id" fit="true"\>
```


* 表单验证采用 Validform

* 时间控件采用 my97，不要使用 easyui的时间控件，因为加载效率慢

* 上传文件使用规则

* 流程配置表单后，业务申请必须重新创建

* jsp代码注释规范，采用隐式注释不能用显式注释，不然标签还是能读到：
```
隐式注释：<%-- --%\>

显式注释：<!-- --\>

```

* 表单布局两种风格：1.table 2.div
* 菜单采用 frame方式打开方法
dataSourceController.do?goDruid&isIframe

* 页面组件 ID命名规范

[1].dategrid组件 name
```
<t:dategrid name="userMe">
```


[2].组合查询 DIV
```
<div id="userMetb">
```

[3].查询按钮对应的 js方法
```
<a href="\#" class="easyui-linkbutton" iconCls="icon-search"
onclick="userMesearch()"\>查询\</a\>
```


#### 页面数据规则

 说明：JSP页面字段的文本内容，取表字段的注释前 6位字符(如果建表字段注释为空，

则页面字段文本会为空)

 A.默认生成的
 JSP页面前五个字段为必须项，其他字段为非必须输入（需要自己手工加）

 B.数据库字段类型为：datetime
 --\>对应页面字段，会自动追加[年月日-时分秒]时间控件

 C.数据库字段类型为：date --\>对应页面会字段，自动追加[年月日]时间控件

 D.数据库字段类型为：Int/Number--\>对应页面字段，会自动追加数字校验（不允许输入

小数）

 E.数据库字段类型为：float/double/decimal--\>对应页面页面字段，会自动追加数字校验

（允许输入小数）

 F.如果表字段为字符类型，并且设置了长度，页面输入框会自动设置 maxlength对应表

字段长度

### 权限设计

基本概念

 权限管理模块涉及到的实体有：用户、角色和系统资源(包括系统菜单、页面按钮等
 )。

用户可以拥有多个角色，角色可以被分配给多个用户。而权限的意思就是对某个资源的某个

操作．一般通用的权限管理模块规定：所谓资源即应用系统中提供的要进行鉴权才能访问的

资源(比如各类数据,系统菜单)；所谓操作即增加、修改、删除、查询等操作。

权限模型

 用户权限模型，指的是用来表达用户信息及用户权限信息的数据模型。即能证明“你是

谁？”、“你能访问哪些受保护资源？”。

 用户与角色之间构成多对多关系。表示同一个用户可以拥有多个角色，一个角色可以被

多个用户所拥有。

 角色与资源之间构成多对多关系。表示同一个资源可以被多个角色访问，一个角色可以

访问多个资源。

#### 设计目标

权限设计及权限管理的目标包括：

对用户授予相应的角色

对角色授予不同的菜单

对角色授予不同的操作按钮权限

进行数据级别的权限控制（行级别、列级别）

目前已经实现前两项的权限设计目标，后两项的权限控制正在开发中。

#### 权限使用

开发说明

在 JFaster系统中，可以通过系统的全局变量配置来决定是否启用按钮权限。如下：

/resources/sysConfig.properties,true(开启按钮权限 )false(关闭按钮权限 )

DateGridTag中根据系统的配置进行按钮权限的控制：

（1）系统开启按钮权限并且 DateGridTag里面相关的按钮操作有配置则根据配置做按

钮权限的控制；

（2）系统开启按钮权限但是 DateGridTag里面相关的按钮操作没有配置则不作按钮权

限的控制；

（3）系统关闭按钮权限则所有的按钮不做按钮权限的控制；

（4）admin用户按钮权限不做限制。

#### 自定义按钮权限

 JFaster中，目前按钮权限设置，是通过对平台自己封装的按钮标签（\<t:dgFunOpt等）

进行设置。而在开发的过程中，有一些按钮标签是普通的\<a
href\>或\<button\>形式的。对于

这种普通开发者自定义按钮的权限设置，目前 JFaster也可以支持了。具体设置方法如下：

* 1.给页面上的自定义按钮增加 id或 class。

小提示：对于具有相同权限的多个按钮，可以设定一个共同的 class，将会更加便捷。

2* .将自定义按钮的 id或 class设置到操作按钮中。

方式一:

ID设置

方式二:

Class设置

* 3．在角色列表中，进行正常的权限设置就可以了

### 案例实践


#### 数据字典

数据字典（标签\<t:dictSelect\>）为系统中可能用到的字典类型数据提供了使用的便利性和可维护性。

应用示例：
```
<t:dictSelect field="name" typeGroupCode="process" title="流程类型"\>\</t:dictSelect\>
```
> 使用案例

通过利用数据字典来做性别下拉框的安全来详细说明数据字典的使用步骤。

* 步骤一：数据字典维护

首先，为类型分组新增一个“性别类型”的分组，填写分组编码以及分组名称，然后在该分组中分别添加“男性”、“女性”两个类型，并分别设置其类型编码为“1”
和“0”

* 步骤二：在页面中使用字典

首先，在 JSP页面中引入 JFaster_UI标签库：
```
<%\@include file="/context/mytags.jsp"%\>
```


 然后就可以使用 dictSelect标签把性别按下拉框显示出来了。代码如下：
```
<t:dictSelect field="sexField" typeGroupCode="sex"\>\</t:dictSelect\>
```

 其中，typeGroupCode就是我们在步骤一中定义的类型分组编码“sex”。页面显示结果

#### UI库常用控件

* 树列表展现

参考示例[菜单管理 ]：
```
WebRoot/webpage/system/function/functionList.jsp
```


* POPUP实现
```
<t:choose hiddenName="roleid" hiddenid="id" /WebRoot/webpage/system/user

url="userController.do?roles"

name="roleList" icon="icon-choose" title="角色列表" textname="roleName" isclear="true"\>\</t:choose\>
```


* 下拉菜单实现

radio控件
```
WebRoot/webpage/system/user/

user.jsp

WebRoot/webpage/system/user/

user.jsp
```

* 数据列表展示
```
WebRoot/webpage/system/user/

userList.jsp
```
* 常用组件 DEMO

上传/表单验证 /Excel导入/Excel导出/
页面不同弹出方式 /树界面展示 /自动补全 /一对多
示例/tabs切换
```
/WebRoot/webpage/demo /\*
```
* 地址


下拉菜单多级联动
一对多明细行加
下拉项
datagrid数据列
表，时间字段格式化
数据行全选
重复校验

#### IFrame打开页面

 目前在 JFaster开发平台中，为了提高 easyui的性能，tab的打开采用
 href方式，但是

href方式存在如下问题：

1.href只加载目标 URL的 html片段

 这个特性是由 jQuery封装的 ajax请求处理机制所决定的，所以目标 URL页面里不需

要有 html，

 head,body等标签，即使有这些元素，也会被忽略，所以放在 head标签里面的任何脚

本也不会被引入或者执行。

2.短暂的页面混乱：

 href链接的页面比较复杂的时候，easyui对其渲染往往需要一个较长的过程

当加载的页面布局较为复杂，或者有较多的 js脚本需要运行的时候，就不好处理了。

 所以，综合考虑，如果页面样式、js简单就采用系统默认的 href方式打开 tab页。

如果页面复杂，不好拆分，则采用 iframe方式打开 tab。采用 ifrme方式，需要在配置

菜单的时候，加上&isIframe标识，如下所示:

dataSourceController.do?goDruid&isIframe

#### 组合查询实现方法

简述：代码生成器默认生成的查询方式为单字段查询，如果想实现字段组合查询，需要

采用如下方式。

实现步骤：

第一步：设置 dategrid字段查询属性 query="true"

 第二步：对应 query="true"的 dategrid字段设置查询字段组件

\<input type="text" name="username" id="username" style="width: 80px"/\>

 第三步：设置查询按钮

\<a href="\#" class="easyui-linkbutton" iconCls="icon-search"
onclick="userListsearch()"\>查询

\</a\>

注意点：

1.这种写法 t:dgToolBar这个标签不能使用，不然会有冲突，查询 form显示不出来;

2.查询函数的名字规则"[dategrid组件 name]search()"

[1].dategrid组件 name

\<t:dategrid name="userMe"

 [2].组合查询 DIV

\<div id="userMetb"

[3].查询按钮对应的 js方法

\<a href="\#" class="easyui-linkbutton" iconCls="icon-search"
onclick="userMesearch()"\>查询

\</a\>

参考示例：/WebRoot/webpage/system/user/userList.jsp

#### 12.4. tiptype的使用

Formvalid中的 tiptype用来定义提示信息的显示方式，一共有 4种取值，在其官方的说

明中，不同取值的含义如下：

取值

含义


自定义弹出框提示；

侧边提示(会在当前元素的父级的 next对象的子级查找显示提示信息的对象，表单以

ajax提交时会弹出自定义提示框显示表单提交状态)；


侧边提示(会在当前元素的 siblings对象中查找显示提示信息的对象，表单以 ajax提

交时会弹出自定义提示框显示表单提交状态)；

侧边提示(会在当前元素的父级的 next对象下查找显示提示信息的对象，表单以 ajax

提交时不显示表单的提交状态)

在 JFaster中，tiptype的属性配置代码如下：

\<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table"

tiptype="1" action="JFasterOrderMainController.do?save"\>



 与官方的用法不同的是，JFaster中对取值为
 1时的样式以及校验方式进行了改造，官方

版是在提交时才给出提示，而 JFaster中是在
onblur的时候就会提示，当输入正确后，1秒中

后会自动消失。

 注：\<t:formvalid\>标签中不写 tiptype时默认为 4.即侧边显示。

 使用建议：单表可以不用给定 tiptype属性，即使用默认的侧边校验，主从表的数据校

验给定 tiptype="1"。


#### 12.5.使用 toolbar自定义 js参数规则

* 第一步：定义按钮
```properties
<t:dgToolBar title="JS增强" icon="icon-edit" url="cgFormHeadController.do?jsPlugin" funname="jsPlugin"></t:dgToolBar>
```


* 第二步：定义 js方法

三个参数说明：

1.三个参数缺一不可

2.三个参数顺序不能变

3.有且只有三个参数

4.id为datagrid的name属性
```
function jsPlugin(title,url,id){
var rowData = \$('\#'+id).datagrid('getSelected');
if (!rowData) {

tip('请选择编辑项目');

return;

}

url += '\&id='+rowData.id;

 \$.dialog({

content: "url:"+url,

lock : true,

title:"JS增强编辑["+rowData.tableName+"-"+rowData.content+"]",

opacity : 0.3,

width:900,

height:500,

cache:false,

 ok: function(){

 iframe = this.iframe.contentWindow;

 iframe.goForm();

return false;

 },
cancelVal: '关闭',

cancel: true /\*为true等价于function(){}\*/

});

}

```