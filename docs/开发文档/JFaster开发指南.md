# 开发指南
## 1.前言
### 1.1.技术背景

 随着 WEB UI框架(EasyUI/Jquery UI/Ext/DWZ)等的逐渐成熟,系统界面逐渐实现统一化，代码生成器也可以生成统一规范的界面！
 代码生成+手工MERGE半智能开发将是新的趋势，生成的代码可节省50%工作量，快速提高开发效率！

### 1.2.平台介绍

 JFaster [J2EE CodeGeneration]是一款基于代码生成器的智能开发平台，采用代码生成+手工MERGE半智能开发模式，可以帮助解决Java项目60%的重复工作，让开发更多关注

业务逻辑。既能快速提高开发效率，帮助公司节省人力成本，同时又不失扩展性和灵活性。JFaster宗旨：简单功能由代码生成器生成使用;复杂业务采用表单自定义，业务流程使

用工作流来实现、扩展出任务接口，供开发编写业务逻辑。实现了流程任务节点和任务接口的灵活配置，既保证了公司流程的保密行，又减少了开发人员的工作量。
 JFaster采用SpringMVC+Hibernate+UI快速开发库作为基础架构，采用面向声明的开发模式，基于泛型方式编写极少代码即可实现复杂的数据展示、数据编辑、表单处理等功能，再配合代码生成器的使用将JavaEE的开发效率提高6倍以上，
可以将代码减少60%以上。
 JFaster V3.0版本四大技术点: 1.代码生成器 2.UI快速开发库 3.在线流程设计
 4.系统

日志记录。

 要求 JDK1.6+

### 1.3.平台优势
采用主流开源技术框架，容易上手;代码生成器依赖性低,很方便的扩展

能力，可完全实现二次开发;

开发效率很高,代码生成器支持多种数据模型：单表数据模型、单表自关

联模型和一对多(父子表)数据模型，代码生成功能直接使用；


查询 SQL过滤器，后台不需要写代码，页面追加查询字段，查询功能自

动实现

页面校验自动生成(必须输入、数字校验、金额校验、时间控件等);

基础的用户权限：菜单，按钮权限，角色

常用共通封装，各种工具类(定时任务,短信接口,邮件发送,Excel导出等),

基本满足 80%项目需求
集成简易报表工具，图像报表和数据导出非常方便，可极其方便的生成

pdf、excel、word等报表；
### 1.4.平台架构

架构技术： Spring MVC+Hibernate4+UI快速开发库+Spring JDBC+Highcharts图形报表+Jquery+Ehcache。

设计思想:零配置（约定大于配置）

各技术点说明

[1]代码生成器

代码生成器用于生成规范的后台代码+统一风格的前台页面+表单校验。

单表模型，单表自关联模型和一对多(父子表)数据模型，增删改查功能生成直接使用;

特点：

A.前台页面字段对应数据库字段生成;

 B.页面字段校验自动生成（数字类型\\必须项\\金额类型\\时间控件\\邮箱\\手机号\\QQ

号等等);

C.支持 Oracle/Mysql/Postgres数据库

注意：代码生成包括 JSP页面生成，代码无需修改，增删改查功能直接配置使用

[2].查询条件过滤器

页面加查询条件，后台不需要写任何逻辑判断，动态拼 HQL追加查询条件

[3].UI快速开发库

 UI快速开发库，针对 WEB UI进行标准封装，页面统一采用 UI标签实现功能：数据

datagrid,表单校验,Popup,Tab等，实现 JSP页面零 JS，开发维护非常高效

[4].智能工作流

在线流程定义，采用开源 Activiti流程引擎，实现在线画流程,自定义表单,表单挂接,业

务流转，流程监控，流程跟踪，流程委托等

[5].表单 Form校验组件

前台页面字段校验采用 Validform

[6].常用共通封装

表单检验组件

数据字典/邮件发送/定时任务/短信接口/Freemarker模板工具 /Jqu ery

[7].基础用户权限

权限功能：用户、角色、权限（菜单权限+按钮权限）

[8].Ehcache缓存机制

Ehcache缓存自定义标签（永久缓存/临时缓存）

[9].报表封装

Excel简易导出工具类+Highcharts图形报表

[10].Hibernate+Spring jdbc组合使用
  Hibernate+Spring jdbc组合使用（单表操作使用 Hibernate；复杂 SQL采用 SQL）,
 (1) SQL设计方案:DB SQL抽离出 Java代码，采用命名规范根据类名和方法名创建 SQL文件，存储 SQL;

(2)程序自动读取 SQL;

(3) SQL读取模式:开发模式和发布模式[SQL加载内存]。

[11].安全的事务回滚机制+安全的数据乐观锁机制

[12].系统日志记录，便于问题追踪

## 2. JFaster框架初探

2.1.演示系统

打开浏览器，输入 JFaster演示环境地址：http://localhost:8088/jfaster-admin/，可以看到如图

## 3. JFaster开发环境搭建

JFaster推荐的开发环境为 Myeclipse6.5/Eclipse3.7+JDK1.6+Tomcat6.0

### 3.1. JAVA环境配置

通过 Oracle的官方地址下载 JDK开发包：
#### 数据库初始化

在 mysql数据库中新建一编码为 UTF8的数据库 JFaster。

通过命令：

mysql -proot -uroot JFaster \< d:/projects/JFaster/doc/JFasterv3-init.sql

 将工程目录中的 doc / JFasterv3-init.sql脚本导入到新建的 JFaster数据库中。
 
 JFaster表结构设计		
表名称	实体名	描述
t_s_log	Log	系统用户操作日志表
t_s_config	Config	全局参数配置表
t_s_file_upload	FileUpload	项目附件父表(其他附件表需继承该表)
t_s_user	User	[用户权限]系统用户表
t_s_base_user	BaseUser	[用户权限]系统用户父类表
t_s_role	Role	[用户权限]角色
t_s_role_user	RoleUser	[用户权限]用户角色
t_s_org	Org	[用户权限]部门机构表
t_s_role_function	RoleFunction	[用户权限]角色权限表
t_s_operation	Operation	[用户权限]权限操作表
t_s_function	Function	[用户权限]菜单权限表
t_s_icon	Icon	图标


#### 项目数据源配置

对数据库进行初始化之后，需要相应地对项目中的数据源连接进行配置。

修改项目的数据源连接配置文件 resources/dbconfig.properties，如图 3-7所示。

图 3-7项目数据源配置

修改项目中的代码生成器数据源连接配置文件 resources/JFaster/JFaster_database.properties
另外，根据自己的需要对代码生成器的相关参数进行配置，配置文件为 resources/JFaster/JFaster_config.properties。

#### 项目启动测试

 配置完成之后，启动Tomcat，在浏览器地址栏中输入*http://localhost:8080/JFaster/*，打开
登陆:输入用户名密码 admin/123456，登陆进入主界面，


#### 运行项目

 在工程目录上面右键-\>Run As-\>7 Maven Build，在弹出的运行设置的Goals中填

写”tomcat:run”，如果在运行时，不需要跑单元测试程序，可以把Skip Test给勾选上，


#### 项目打包

 在工程上面右键-\>Run As-\>Maven Package，打包完成之后的war包位于target/JFaster.war


## 4.代码生成器

 code-maker是为JFaster提供的完善的代码生成器

    1.所有代码均开源，开源协议详见LICENSE
    2.使用方法，运行：com.abocode.codemaker.Run即可
    3.项目配置文件修改：contextConfig.properties
    4.数据库连接修改：database.properties
    5.模版修改：maker-config/template/*
    
#### 1.新加枚举类型：CodeType：
 
         public enum CodeType {
         controller("Controller"),
     
         service("Service"),
         serviceImpl("ServiceImpl"),
     
         entity("Entity"),
         repository("Repository"),
         repositoryImpl("RepositoryImpl"),
         newType("NewType");
         }   
 #### 2.添加枚举类型对应的代码生成路径：JFasterCodeFactory getCodePath()
   
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
     
        
#### 3.添加代码生成器中使用模版：JFasterCodeGenerate.java  generateToFile()
    
      codeFactory.invoke("repositoryImplTemplate.ftl", "repositoryImpl");
      codeFactory.invoke("repositoryTemplate.ftl", "repository");
    



### 4.1.数据表创建

现在有一张员工表 person，其建表 SQL为：

CREATE TABLE \`person\` (

\`ID\` varchar(32) NOT NULL default '' COMMENT '主键',

\`NAME\` varchar(32) default NULL COMMENT '用户名',

\`AGE\` int(11) default NULL COMMENT '年龄',

\`SALARY\` decimal(10,2) default NULL COMMENT '工资',

\`createDt\` datetime default NULL COMMENT '创建时间',

PRIMARY KEY

(\`ID\`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 注意：建表时，必须给每个字段加上注释，代码生成器会根据注释去生成页面字段对应的显示文本。

将建表 SQL在数据库管理器里面执行，完成对 person表的创建。

### 4.2.代码生成器配置

 代码生成器有两个配置文件：一个用于数据源的配置，一个用于代码生成器的参数配置。

这两个配置文件分别是 resources/JFaster目录的 JFaster_database.properties和JFaster_config.properties。

参数配置文件 JFaster_config.properties的各参数说明如表 4-1所示：

表 4-1代码生成器参数说明

参数

参数说明

默认值

取值范围

source_root_package

Source floders on build path (JAVA

src

文件的根目录)

webroot_package

bussi_package

WEB应用文件的根目录（例如：

jsp）

WebRoot

Demo

业务包（举例：比如 ERP中的一

个大的模块销售模块目录）

特点：支持多级目录例如[com.sys]

templatepath

代码生成器使用的模板文件目录

项目编码

JFaster/template

utf-8

system_encoding

JFaster_generate_table_id

自定义主键命名

id

目前表主键只

能命名 ID

JFaster_ui_search_filed_nu配置代码生成器生成的 JSP页面， 1

m

默认前几个字段生成查询条件

JFaster_filed_convert

数据库表字段转换为实体字段是

采用原生态，还是采用驼峰写法转

换

true

true/false

4.3.代码生成

打开代码生成器并输入相应的参数如图 4-1所示。

图 4-1员工信息维护的代码生成器

执行【生成】之后，可以在源代码目录 src中看到新生成的 java代码文件

### 建表规范

1.

表必须有唯一主键: ID（字符类型 32位）

备注:主键采用 UUID方式生成

主键支持自定义，修改
JFaster_config.properties的参数[JFaster_generate_table_id]即可;

表必需字段（创建人，创建时间等..）

表字段必须有注释

备注: JSP页面字段文本，是根据表字段注释来生成

主表和子表的外键字段名字，必须相同（除主键 OBID外）

子表引用主表主键 OBID作为外键，外键字段必须以 OBID结尾

注：请按照建表模板表 4-2来创建新表，模板表中原有的字段，生成器会过滤不在页面
生成。


#### 页面生成规则

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

### 4.6.一对多的代码生成

一对多代码生成器使用

 单表的代码生成器入口类是 test.JFasterOneGUI；一对多的代码生成器入口类是

test.JFasterOneToMainUtil。

一对多的代码生成器使用示例：

//第一步：设置主表

CodeParamEntity codeParamEntityIn = new CodeParamEntity();

codeParamEntityIn.setTableName("JFaster_order_main");// 主表[表名]

codeParamEntityIn.setEntityName("Demo4ManyKey"); //主表[实体名]

codeParamEntityIn.setEntityPackage("JFaster"); //主表[包名]

codeParamEntityIn.setFtlDescription("订单主数据"); //主表[描述]

//第二步：设置子表集合

List\<SubTableEntity\> subTabParamIn = new ArrayList\<SubTableEntity\>();

//[1].子表一

SubTableEntity po = new SubTableEntity();

po.setTableName("JFaster_order_custom"); //子表[表名]

po.setEntityName("DemoMany4CustomKey"); //子表[实体名]

po.setEntityPackage("JFaster");

//子表[包]

po.setFtlDescription("订单客户明细"); //子表[描述]

po.setForeignKeys(new
String[]{"GORDER_OBID","GO_ORDER_CODE"});//子表[外键:与主表关

联外键]

subTabParamIn.add(po);

//[2].子表二

SubTableEntity po2 = new SubTableEntity();

po2.setTableName("JFaster_order_product");

//子表[表名]

po2.setEntityName("DemoMany4ProductKey");

子表[实体名]

//

po2.setEntityPackage("JFaster");

po2.setFtlDescription("订单产品明细");

 //子表[包]

//子表[描述]

po2.setForeignKeys(new
String[]{"GORDER_OBID","GO_ORDER_CODE"});//子表[外键:与主表

关联外键]

subTabParamIn.add(po2);

codeParamEntityIn.setSubTabParam(subTabParamIn);

//第三步：一对多(父子表)数据模型,代码生成

CodeGenerateOneToMany.oneToManyCreate(subTabParamIn, codeParamEntityIn);

*www.JFaster.org*

QQ群: 106259349, 106838471, 289782002

29
#### 使用规范

1.

目前代码生成器默认的主键生成策略为 UUID

主表和子表的目录最好保持一致

2.子表和主表的外键规则如下：

a)主表和子表的外键字段名字，必须相同（除主键ID外）

b)子表引用主表主键ID作为外键，外键字段必须以_ID结尾


##  5.查询 HQL过滤器

### 5.1.数据过滤现状分析

项目开发的查询页面都会有很多查询条件，开发追加查询条件的工作繁琐又很浪费时间。

这块工作量主要在：页面加查询字段和后台代码逻辑判断，追加查询条件；

目前 JAVA持久层主流框架 Hibernate和 Ibatis实现方式分析:

[1].Hibatente技术实现：

A.页面追加查询字段;

B.后台代码需加逻辑判断，判断字段是否为空，手工拼 SQL追加查询条件;

[2].IBATIS技术实现：

A.页面追加查询字段;

 B.后台不需写代码，但是需在 XML文件中追加该字段非空判断和查询条件;

特点：常规功能的页面查询方式只能是"全匹配"和"模糊查询"，对于特殊的 "包含查询"

和"不匹配查询",

只能写特殊逻辑代码

### 5.2.查询条件 SQL生成器

 5.2.1.

实现原理

根据页面传递到后台的参数，动态判断字段是否为空，自动拼 SQL追加查询条件。

实现的功能：实现了"模糊查询" , "包含查询" , "不匹配查询"等 SQL匹配功能。

特点：页面仅仅追加一个查询字段，后台不需要写任何代码，查询功能自动实现。

5.2.2.

查询规则

 要求：页面查询字段，需跟后台 Action(或 Controller)中
 Page的字段对应一致，后台不

需写代码自动生成 HQL，追加查询条件;默认生成的查询条件是全匹配;

 查询匹配方式分类：

[1].全匹配查询：查询数据没有特殊格式，默认为全匹配查询

[2].模糊查询：

[3].包含查询：

查询数据格式需加星号[\*]

例如：{MD\*/\*MD\*/\*M\*D\*}

查询数据格式采用逗号分隔[,]例如： {01,03}(含义：in('01','03'))


[4].不匹配查询：查询数据格式需要加叹号前缀[!]例如：{!123}(含义：不等于 123)

特殊说明：查询不为 Null的方法=!null(大小写没关系);查询不为空字符串的方法=!(只有

一个叹号).

[5].时间范围范围查询

jsp页面中使用的 name：需要查询的日期类型字段名本身（什么都不加），表示查询

时查询等于该字段时间的数据

begin：需要查询的日期类型字段名（首字母大写），表示查询开始时间查询时查询大

于等于开始时间的数据

end:需要查询的日期类型字段名（首字母大写），表示查询结束时间查询时查询小于

等于结束时间的数据

使用举例：

字段名称 private Date birthday

查询开始时间 beginBirthday

查询结束时间 endBirthday

5.2.3.

具体实现

第一步：页面实现

说明：为 dategrid字段，追加属性 query="true"，自动加载出查询框，如图 5-1所示。

 图 5-1 JSP代码实现

第二步：controller层处理

图 5-2 Controller代码

5.3.查询过滤器高级特性

 dategrid中的查询过滤器默认是单条件查询，即在设置多个 dgCol的
 query=”true”之后，

查询条件中同时只能有一个条件被使用，生成的页面效果如图 5-3所示。

图 5-3默认查询过滤器效果

当然，可以通过 dategrid和 dgCol的参数设置来达到更高级的查询过滤功能，如组合查

询条件和值范围查询。

5.3.1.

组合条件查询

 设置\<t:dategrid\>标签的
 queryMode=”group”（该参数值默认为”single”，即单条件查询），

在页面生成时，会生成一个组合查询条件输入面板。生成的页面效果如图 5-4所示。

图 5-4组合查询过滤器效果

5.3.2.

字段范围查询

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

//查询条件组装器

org.JFasterframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
JFasterDemo);

String ctBegin = request.getParameter("createTime_begin");

String ctEnd = request.getParameter("createTime_end");

if(ctBegin!=null && ctEnd!=null){

 try {

 cq.ge("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(ctBegin));

 cq.le("createTime", new SimpleDateFormat("yyyy-MM-dd").parse(ctEnd));

 } catch (ParseException e) {

 e.printStackTrace();

 }

 cq.add();

}

 this.JFasterDemoService.getDataGridReturn(cq, true);

 TagUtil.datagrid(response, dataGrid);

}
```


 在控制器中用 request接收传递到后台的查询条件，或者直接在方法参数列表里填上，

springmvc会帮我们获得。

 然后将得到的范围查询条件添加到 CriteriaQuery对象中，最后调用 CriteriaQuery的
 add()

方法加载生成 hql。

至此，范围查询就完成了。

5.3.3.

查询字段添加日期控件

例如，要给创建日期的范围查询条件框添加日期控件，首先为创建日期添加范围查询：

\<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"

query="true" queryMode="group"\>\</t:dgCol\>

用 jquery为生成的 createTime_start和 createTime_end两个输入框添加日期控件。

\$(document).ready(function(){

\$("input[name='createTime_begin']").attr("class","easyui-datebox");

\$("input[name='createTime_end']").attr("class","easyui-datebox");

);


5.3.4.

日期字段的数据格式化

在 dategrid中，对于日期字段，可以通过设置\<d:dgCol\>的 formatter属性配置格式化方

式，实现对日期数据的格式化，如：

\<t:dgCol title="创建日期" field="createTime" formatter="yyyy-MM-dd hh:mm:ss"

query="true" queryMode="group"\>\</t:dgCol\>

 对于日期的格式化方式，可以参考 JDK参考手册中 SimpleDateFormat中对于日期和时

间模式的说明，如图 5-7所示。

图 5-7日期和时间模式

5.3.5.

数据列表合计功能

 进行数据的列表展示时，为数据显示合计数是一个很有用的功能，在 JFaster的
 datagrid

JFaster智能开发平台

v3开发指南

图 5-8列表数据合计效果图

该功能的实现，主要是通过在加载 datagrid的数据时，统计出所需的合计值，并放在

datagrid对象的 footer中。示例代码如下：
```

@RequestMapping(params = "datagrid")


**public void** datagrid(JFasterDemo JFasterDemo,HttpServletRequest request,

HttpServletResponse response, DataGrid dataGrid) {

CriteriaQuery cq = **new** CriteriaQuery(JFasterDemo.**class**, dataGrid);

//查询条件组装器

org.JFasterframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,

JFasterDemo);

String ctBegin = request.getParameter("createTime_begin");

String ctEnd = request.getParameter("createTime_end");

**if**(StringUtil.isNotEmpty(ctBegin)&&StringUtil.isNotEmpty(ctEnd)){

 **try** {

cq.ge("createTime", **new**

SimpleDateFormat("yyyy-MM-dd").parse(ctBegin));

cq.le("createTime", **new**

SimpleDateFormat("yyyy-MM-dd").parse(ctEnd));
} **catch** (ParseException e) {

e.printStackTrace();

}

cq.add();

}

this.JFasterDemoService.getDataGridReturn(cq, **true**);

//update-begin--Author:zhaojunfu Date:20130520 for：TASK \#109

datagrid标签没有封装合计功能

String total_salary =

String.valueOf(JFasterDemoService.findOneForJdbc("select sum(salary) as ssum

from JFaster_demo").get("ssum"));


\*说明：格式为字段名:值(可选，不写该值时为分页数据的合计)多个合计以 ,分割

dataGrid.setFooter("salary:"+total_salary+",age,email:合计");

//update-end--Author:zhaojunfu Date:20130520 for：TASK \#109

datagrid标签没有封装合计功能


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

## 6.数据字典

数据字典（标签\<t:dictSelect\>）为系统中可能用到的字典类型数据提供了使用的便利性

和可维护性。

应用示例：

\<t:dictSelect field="name" typeGroupCode="process"
title="流程类型"\>\</t:dictSelect\>

### 6.2.使用案例

通过利用数据字典来做性别下拉框的安全来详细说明数据字典的使用步骤。

步骤一：数据字典维护

首先，为类型分组新增一个“性别类型”的分组，填写分组编码以及分组名称，如图 6-1

所示。
然后在该分组中分别添加“男性”、“女性”两个类型，并分别设置其类型编码为“1”

和“0”，如图 6-2所示。

图 6-2为分组添加字典类型

步骤二：在页面中使用字典

首先，在 JSP页面中引入 JFaster_UI标签库：

\<%\@include file="/context/mytags.jsp"%\>

 然后就可以使用 dictSelect标签把性别按下拉框显示出来了。代码如下：

\<t:dictSelect field="sexField" typeGroupCode="sex"\>\</t:dictSelect\>

 其中，typeGroupCode就是我们在步骤一中定义的类型分组编码“sex”。页面显示结果


## 7.表单校验组件 ValidForm

### 7.1. Validform使用入门

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

### 7.2.绑定附加属性

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

## Validform对象 [方法支持链式调用 ]

如示例 var demo=\$(".formsub").Validform()，那么 demo对象会有以下属性和方法可以

调用：

**tipmsg**【**object**】

 如：demo.tipmsg.s="error! no message inputed.";

 通过该对象可以修改除 tit以外的其他提示文字，这样可以实现同一个页面的不同表单

使用不同的提示文字。

具体可修改的提示文字

\$.Tipmsg={//默认提示文字;

tit:"提示信息",

w:{

"\*":"不能为空！",

"\*6-16":"请填写 6到 16位任意字符！",

"n":"请填写数字！ ",

"n6-16":"请填写 6到 16位数字！",

"s":"不能输入特殊字符！ ",

"s6-18":"请填写 6到 18位字符！",

"p":"请填写邮政编码！",

"m":"请填写手机号码！",

"e":"邮箱地址格式不对！",

"url":"请填写网址！"
},

def:"请填写正确信息！",

undef:"datatype未定义！",

reck:"两次输入的内容不一致！",

r:"通过信息验证！",

c:"正在检测信息…",

s:"请填入信息！",

s_auto:"请{填写}{0}！",

v:"所填信息没有经过验证，请稍后…",

p:"正在提交数据…"

};

 要修改 tit（弹出框的标题文字）的话，可以这样：\$.Tipmsg.tit="Message
 Box"，则弹出

框的标题文字会换成"Message Box"。

注：5.3+

 \$.Tipmsg.w里，形如"\*6-16"的提示文字，里面的数字是会被替换的。如绑定

datatype="\*2-18"，那它默认的出错信息就会是"请填写 2到 18位任意字符！"，可以通过

\$.Tipmsg.w这个对象修改和扩展默认错误信息，如果你已经设置了"zh2-4"的提示信息是"2-4

位中文",那么"zh2-8"出错的信息就自动会是："2-8位中文"。对于自定义的
datatype，在扩展

默认信息时，注意错误信息的名字要跟 datatype名字一样，如上面示例是：

\$.Tipmsg.w["zh2-4"]="2-4位中文"。对于多页面或一个页面多表单有相同
datatype来说，在

\$.Tipmsg.w中扩展默认提示信息是个很好的选择。目前只能通过\$.Tipmsg.w扩展，还不能使

用 Validform对象的 tipmsg属性来扩展。

 \$.Tipmsg.s_auto是用来指定在没有绑定 nullmsg，且指定了标签
 Validform_label时的默

认提示信息。"{0}"是会被找到的
Validform_label里的文字替换掉的，"{填写}"里的文字在绑

定了"recheck"

具体示例请参见页。

**dataType**【**object**】

获取内置的一些正则：

{

"match":/\^(.+?)(\\d+)-(\\d+)\$/,

"\*":/[\\w\\W]+/,

"\*6-16":/\^[\\w\\W]{6,16}\$/,

"n":/\^\\d+\$/,

"n6-16":/\^\\d{6,16}\$/,

"s":/\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w\\.\\s]+\$/,

"s6-18":/\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w\\.\\s]{6,18}\$/,"p":/\^[0-9]{6}\$/,

 "m":/\^13[0-9]{9}\$\|14[0-9]{9}\|15[0-9]{9}\$\|18[0-9]{9}\$/,

 "e":/\^\\w+([-+.']\\w+)\*\@\\w+([-.]\\w+)\*\\.\\w+([-.]\\w+)\*\$/,

 "url":/\^(\\w+:\\/\\/)?\\w+(\\.\\w+)+.\*\$/

}

**addRule(rule)**【返回值：**Validform**】

 可以通过Validform

附加属性都可以通过这个方法绑定。

demo.addRule([

 {

 ele:"\#name",

 datatype:"s6-18",

ajaxurl:"valid.php",

nullmsg:"请输入昵称！",

errormsg:"昵称至少 6个字符,最多 18个字符！"

},

{

ele:"\#userpassword",

datatype:"\*6-16",

nullmsg:"请设置密码！",

errormsg:"密码范围在 6\~16位之间！"

},

{

ele:"\#userpassword2",

datatype:"\*",

recheck:"userpassword",

nullmsg:"请再输入一次密码！",

errormsg:"您两次输入的账号密码不一致！"

}

]);

其中 ele是指定要绑定规则的对象，会在 Validform对象下查找这些对象。

**eq(n)**【返回值：**Validform**】

获取 Validform对象的第 n个元素。

 如你页面上有多个 form的 class都是 formsub，执行上面的验证绑定，得到的 demo对

象就可以操作所有这些表单，如果你要对其中某个表单执行某些操作，那么就可以使用这个

方法。

如 demo.eq(0).resetForm()，重置第一个表单。

**ajaxPost(flag,sync,url)**【返回值：**Validform**】

 以 ajax方式提交表单。flag为 true时，跳过验证直接提交，sync为
 true时将以同步的

方式进行 ajax提交。

参数 url是 5.3版新增，传入了 url地址时，表单会提交到这个地址

如 demo.ajaxPost(true)，不做验证直接 ajax提交表单。

**abort()**【返回值：**Validform**】

终止 ajax的提交。

 如执行上面的
 ajaxPost()之后，发现某些项填写不对，想取消表单提交，那么就可以执

行这个操作：demo.abort()

**submitForm(flag,url)**【返回值：**Validform**】

以初始化时传入参数的设置方式提交表单，flag为 true时，跳过验证直接提交。

参数 url是 5.3版新增，传入了 url地址时，表单会提交到这个地址

如 demo.submitForm(true)，不做验证直接提交表单。

**resetForm()**【返回值：**Validform**】

重置表单。

如 demo.resetForm()，重置表单到初始状态。

**resetStatus()**【返回值：**Validform**】

 重置表单的提交状态。传入了 postonce参数的话，表单成功提交后状态会设置为

"posted"，重置提交状态可以让表单继续可以提交。

 如 demo.resetStatus()

**getStatus()**【返回值：**String**】

 获取表单的提交状态，normal：未提交，posting：正在提交，posted：已成功提交

过。

如 demo.getStatus()

**setStatus(status)**【返回值：**Validform**】

 设置表单的提交状态，可以设置 normal，posting，posted三种状态，不传参则设置状

态为 posting，这个状态表单可以验证，但不能提交。

 如 demo.setStatus("posted")

**ignore(selector)**【返回值：**Validform**】

忽略对所选择对象的验证，不传入 selector则忽略所有表单元素。

 如 demo.ignore("select,textarea,\#name")，忽略 Validform对象下所有
 select，textarea及

一个 id为"name"元素的验证。

**unignore(selector)**【返回值：**Validform**】

 将 ignore方法所忽略验证的对象重新获取验证效果，不传入
 selector则恢复验证所有表

单元素。

 如 demo.unignore("select,textarea,\#name")，恢复 Validform对象下所有
 select，textarea

及一个 id为"name"元素的验证。

**check(bool,selector)**【返回值：**Boolean**】

bool为 true时则只验证不显示提示信息

 对指定对象进行验证(默认验证当前整个表单)，通过返回 true，否则返回
 false（绑定实

时验证的对象，格式符合要求时返回 true，而不会等 ajax的返回结果）

 如 demo.check()，验证当前整个表单，且只验证但不显示对错信息。

 **config(setup) 5.3+**【返回值：**Validform**】

setup参数是一个对象。

如:

demo.config({

 url:"这里指定提交地址",

 ajaxpost:{

 //可以传入\$.ajax()能使用的，除 dataType外的所有参数;

},

ajaxurl:{

//可以传入\$.ajax()能使用的，除 dataType外的所有参数;

}

})

可用参数：

url：指定表单的提交路径，这里指定的路径会覆盖表单 action属性所指定的路径

ajaxpost：表单以 ajax提交时，可以在这里配置 ajax的参数

ajaxurl：配置实时验证 ajax的参数

(1)执行config可以动态设置、添加参数，如：

demo.config({

 url:"http://validform.rjboy.cn"

});

\$(".save").click(function(){

demo.config({

 ajaxpost:{

 timeout:1000

}

 });

});

那么在点击 save按钮后，demo所对应的表单的配置为:

config={

 url:"http://validform.rjboy.cn",

 ajaxpost:{

 timeout:1000

}

}

 (2)参数 url的优先级：form表单的 action所指定的提交地址会被 config.url覆盖，

config.url会被 config.ajaxpost.url覆盖，config.ajaxpost.url会被
Validform对象的方法

submitForm(flag,url)和 ajaxPost(flag,sync,url)里的 url覆盖。

 如果表单里没有指定 action提交地址，那么就会提交到 config.url设定的地址。

 (3)考虑到整个验证框架的逻辑，传入 dataType参数不会起作用，不会被覆盖，ajax

必须返回含有 status值的 json数据。

另外注意的是：传入的 success和 error方法里，能多获取到一个参数，如：

demo.config={

 ajaxpost:{

 url:"",

 timeout:1000,

 ...,

 success:function(data,obj){

 //data是返回的 json数据;

 //obj是当前表单的 jquery对象;

},

error:function(data,obj){

//data是{ status:\*\*, statusText:\*\*, readyState:\*\*, responseText:\*\* };

//obj是当前表单的 jquery对象;

 }

},

ajaxurl:{

 success:function(data,obj){

 //data是返回的 json数据;

 //obj是当前正做实时验证表单元素的 jquery对象;

 //注意：5.3版中，实时验证的返回数据须是含有 status值的 json数据！

 //跟 callback里的 ajax返回数据格式统一，建议不再返回字符串"y"或"n"。
目前这两种格式的数据都兼容。

 }

 }

}

## 7.5.调用外部插件

**d**

**h**

**- datePicker**

7.6. Validform的公用对象

\$.Datatype

可以通过\$.Datatype对象来扩展 datatype类型。

如\$.Datatype.zh=/\^[\\u4E00-\\u9FA5\\uf900-\\ufa2d]{1,}\$/

\$.Tipmsg

 可以通过\$.Tipmsg对象来修改默认提示文字。具体可修改的提示文字请查看

Validform对象的 tipmsg属性。

 如果 Validform对象的 tipmsg属性没有找到相关的提示信息，那么就会到\$.Tipmsg中

查找对应提示文字。

如\$.Tipmsg.tit="msg box"; //设置默认弹出框的标题文字。

\$.Showmsg(msg)

调用 Validform自定义的弹出框。

*www.JFaster.org*

QQ群: 106259349, 106838471, 289782002

55

![](media/467d4345f25f8510e5173ebc5c8c1148.jpg)

JFaster智能开发平台

v3开发指南

 参数 msg是要显示的提示文字。

 如\$.Showmsg("这是提示文字"); //如果不传入信息则不会有弹出框出现，像

\$.Showmsg()这样是不会弹出提示框的。

 \$.Hidemsg()

关闭 Validform自定义的弹出框。

如\$.Hidemsg()

## 8.基础用户权限

### 8.1.权限设计

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

### 8.2.权限设计目标

权限设计及权限管理的目标包括：

对用户授予相应的角色

对角色授予不同的菜单

对角色授予不同的操作按钮权限

进行数据级别的权限控制（行级别、列级别）

目前已经实现前两项的权限设计目标，后两项的权限控制正在开发中。

### 权限设计

8.3.1.

数据表

数据表

实体类

说明

t_s_user

JFaster.system.pojo.base.TSUsr

JFaster.system.pojo.base.TSBaseUser

[用户权限]系统用户表

t_s_base_user

[用户权限]系统用户父类

表

t_s_role

JFaster.system.pojo.base.TSRole

JFaster.system.pojo.base.TSRoleUser

JFaster.system.pojo.base.TSDepart

[用户权限]角色

t_s_role_user

t_s_depart

[用户权限]用户角色

[用户权限]部门机构表

t_s_role_function

t_s_operation

t_s_function

JFaster.system.pojo.base.TSRoleFunction [用户权限]角色权限表

JFaster.system.pojo.base.TSOperation

JFaster.system.pojo.base.TSFunction

[用户权限]操作权限表

[用户权限]菜单权限表


图 8-4按钮权限分配

开发说明

在 JFaster系统中，可以通过系统的全局变量配置来决定是否启用按钮权限。如下：

/resources/sysConfig.properti es

true(开启按钮权限 )

false(关闭按钮权限 )

DateGridTag中根据系统的配置进行按钮权限的控制：

（1）系统开启按钮权限并且 DateGridTag里面相关的按钮操作有配置则根据配置做按

钮权限的控制；

（2）系统开启按钮权限但是 DateGridTag里面相关的按钮操作没有配置则不作按钮权

限的控制；

（3）系统关闭按钮权限则所有的按钮不做按钮权限的控制；

（4）admin用户按钮权限不做限制。

8.3.4.

自定义按钮权限

 JFaster中，目前按钮权限设置，是通过对平台自己封装的按钮标签（\<t:dgFunOpt等）

进行设置。而在开发的过程中，有一些按钮标签是普通的\<a
href\>或\<button\>形式的。对于

这种普通开发者自定义按钮的权限设置，目前 JFaster也可以支持了。具体设置方法如下：

1.给页面上的自定义按钮增加 id或 class。

小提示：对于具有相同权限的多个按钮，可以设定一个共同的 class，将会更加便捷。

2.将自定义按钮的 id或 class设置到操作按钮中。

方式一:

ID设置

方式二:

Class设置

3．在角色列表中，进行正常的权限设置就可以了


## JFaster注意规则

1.

列表页面，datagrid的 name属性不允许存在重复的，否则页面显示白板：

\<t:dategrid name="JFasterDemoList" title="开发 DEMO列表"

actionUrl="JFasterDemoController.do?datagrid" idField="id" fit="true"\>

表单验证采用 Validform

时间控件采用 my97，不要使用 easyui的时间控件，因为加载效率慢

上传文件使用规则

流程配置表单后，业务申请必须重新创建

jsp代码注释规范，采用隐式注释不能用显式注释，不然标签还是能读到：

隐式注释：\<%-- --%\>

显式注释：\<!-- --\>

7.

表单布局两种风格：1.table 2.div

1.table

2.div

例如：JFaster/demo/JFasterDemo/JFasterDemo.jsp

例如：webpage/system/role/role.jsp

8.

postgres数据库建表规范

字段名字大小写有区别，请注意

9.

菜单采用 frame方式打开方法

dataSourceController.do?goDruid&isIframe

10.页面组件 ID命名规范

[1].dategrid组件 name

\<t:dategrid name="userMe"

[2].组合查询 DIV

\<div id="userMetb"

[3].查询按钮对应的 js方法

\<a href="\#" class="easyui-linkbutton" iconCls="icon-search"
onclick="userMesearch()"\>查询\</a\>

## 10.项目编码规范

10.1.项目编码规范

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

## 12.附录

12.1.UI库常用控件参考示例

序号

控件

解决方案

参考示例


datagrid数据列 \<t:dgCol title="状态" sortable="true"

WebRoot/webpage/system/user/

表，字段采用数据 field="status" replace="正常_1,禁用_0,超级 userList.jsp

字典显示文本

管理员_-1"\>\</t:dgCol\>



树列表展现

参考示例[菜单管理 ]：

WebRoot/webpage/system/funct

ion/functionList.jsp

POPUP实现

\<t:choose hiddenName="roleid" hiddenid="id" /WebRoot/webpage/system/user

url="userController.do?roles"

name="roleList" icon="icon-choose" title="

角色列表" textname="roleName"

isclear="true"\>\</t:choose\>

/user.jsp



下拉菜单实现

radi o控件

WebRoot/webpage/system/user/

user.jsp

WebRoot/webpage/system/user/

user.jsp

数据列表展示

WebRoot/webpage/system/user/

userList.jsp

常用组件 DEMO

上传/表单验证 /Excel导入/Excel导出/

页面不同弹出方式 /树界面展示 /自动补全 /一对多

示例/tabs切换

/WebRoot/webpage/demo /\*

地址


下拉菜单多级联动


一对多明细行加

下拉项

datagrid数据列

表，时间字段格式化

数据行全选

重复校验

### 12.2.开发技巧：采用 IFrame打开页面

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

### 12.3.开发技巧：组合查询实现方法

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

### 12.4.

Formvalid新增属性 tiptype的使用

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


### 12.5.使用 toolbar自定义 js参数规则

第一步：定义按钮

\<t:dgToolBar title="JS增强" icon="icon-edit"

url="cgFormHeadController.do?jsPlugin"

funname="jsPlugin"\>\</t:dgToolBar\>

第二步：定义 js方法

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