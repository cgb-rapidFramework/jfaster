# JFaster项目说明

JFaster 是一套基于SH（Spring + Hibernate）快速开发框架，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。
项目采用DDD设计思想对业务逻辑进行解耦，帮你快速实现高质量的业务系统。使用代码生成器快速生成所有代码，节约开发成本。

## 项目介绍

- 项目真实环境中经过大量的检验，系统的安全、稳定、可靠。
- 所有源代码均完全开源，无商业限制，开源协议为Apache v2 License。
- 技术框架选型为主流框架上手简单,文档齐全。
- 使用代码生成器快速生成功能模块。
- 代码结构清晰，便于维护,无冗余代码。
- 组件大量封装，基本操作只需要简单引用就可达到理想的效果。
- 多种模版选择，可以在后台模版管理中进行选择，满足同一套框架，不同风格适用不同的客户。
- 封装数据导入导出插件，数据转换方便快捷。
- 支持多语言，开发国际化项目非常方便。
- 前后端分离：前端可以独立调整样式风格，后端只需要关心后台业务逻辑的实现。
- 运行环境：常用的java web容器、jdk1.6+,浏览器为ie8+，主流数据库。

## 模块说明

### core核心域说明

对基础领域的封装
 
 - common 一些基础工具类，对异常、缓存、工具类的封装。
 - domain 对基础业务逻辑的封装。
 - interfaces 对接口层的封装，及定义。
 - persistence 对数据库持久化层的封装,规范查询方法。
 - platform 该部分对页面显示元素、数据集及大量基础工具进行封装。

### api层
接口请求参数定义

-constant  接口暴露的常量定义
-enums   接口枚举定义

### project
接口层定义
- common 项目需要的异常处理/拦截器等
- interfaces 接口层（Restful+dubbo+web）

### service
具体实现

- application 应用层,对业务代码逻辑的实现及组装
- domain 领域层，对领域业务的实现


## 系统功能介介绍

* 系统监控：
  * 数据库监控
* 系统管理
  * 用户管理
  * 数据库字典
  * 角色管理
  * 菜单管理
  * 语言管理
  * 图标管理
  * 地区管理
  * 部门管理
  * 模版管理
* 统计报表[用户分析]
  * 饼状图
  * 柱状图
     
### 使用说明

 -  数据库脚本初始化，需要执行脚本 doc/db/jfaster.sql
 -  开发环境 idea+jdk6+maven+mysql
 -  项目启动访问地址：http://localhost:8080/jfaster

#### 下载地址

 - gitee:[https://gitee.com/abocode-source/bms-project.git](https://gitee.com/abocode-source/bms-project)
 - github:[https://github.com/abocode/jfaster.git](https://github.com/abocode/jfaster.git)
 - 生成器路径:[https://github.com/abocode/code-maker](https://github.com/abocode/code-maker.git)
 - 在线文档：[http://www.abocode.com](http://www.abocode.com)
 
#### 文档

 - 文档路径:/docs
 - 数据库脚本(mysql)：JFaster.zip（其他数据库请使用hibernate自行初始化）



### 皮肤展示
- ACE UI:
 <img alt="ACE-UI" src="https://gitee.com/uploads/images/2018/0418/003928_2f87f596_78155.png">

- black UI
 <img alt="BLACK-UI" src="https://gitee.com/uploads/images/2018/0418/004331_21951b87_78155.png">

- bootstrap UI
 <img alt="BOOTSTRAP-UI" src="https://gitee.com/uploads/images/2018/0418/004432_6f865d82_78155.png">
- DEFAULT UI
 <img alt="DEFAULT-UI" src="https://gitee.com/uploads/images/2018/0418/004503_142347aa_78155.png">
- diy UI
 <img alt="dIY-UI" src="https://gitee.com/uploads/images/2018/0418/004548_8da42ab5_78155.png">


## 加入我们

   欢迎各位开发者提出建议、push代码,如有疑问联系，请使用如下联系方式。邮箱:guanxf@aliyun.com,昵称:糊涂,交流qq群:140586555。
