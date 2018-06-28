# JFaster项目说明

JFaster 是一套SH（Spring + Hibernate）快速开发框架，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。
项目采用DDD设计思想对业务逻辑进行解耦，帮你快速实现高质量的业务系统。使用代码生成器快速生成所有代码，是技术选型最理想的选择。

## 项目介绍

- 项目的所有代码均开源共享，并且在真实环境中经过大量的检验，系统的安全、稳定、可靠。
- 所有源代码均完全开源，无商业限制，开源协议为BSD。
- 框架为主流框架上手简单,文档齐全,支持主流数据库。
- 完善的代码封装，使用代码生成器根据数据库表分分钟完美呈现对表的CRUD功能。
- 代码结构清晰，便于维护,无冗余代码，可直接在实际项目中使用。
- 组件大量封装，基本操作只需要简单引用就可达到你想要的效果。
- 多种模版选择，可以在后台模版管理中进行选择，满足同一套框架，不同风格适用不同的客户。
- 封装POI导入导出插件，读写Excel简单快捷。
- 支持多语言，开发国际化项目非常方便。
- 主流框架选型，经历过大并发正式环境积累，正式环境无忧。
- 前后端分离：前端可以独立调整样式风格及显示和后端只需要关心后台业务逻辑的实现。
- 项目使用框架：spring MVC+Hibernate+bootstrap+easyui+poi。
- 运行环境：所有web服务器及jdk1.6+,浏览器为ie8+。

## 模块说明

### core核心域说明

对基础领域的封装
 
 - common 一些基础工具类，对异常、缓存、工具类的实现
 - domain 对基础业务逻辑的封装，如部分持久化方法
 - interfaces 对基础接口层的封装，及定义
 - persistence 持久化方法，对数据库持久化操作的定义,对数据库的操作，统一查询方法命名Hibernate采用findXXX()，纯jdbc操作则使用queryXXX()
 - platform 该部分对页面显示元素、数据集及大量基础工具进行封装，在项目开发中可以直接进行使用。

### web业务模块说明
- application 应用层,对业务代码逻辑的实现及封装，同时也是领域的调用者
- constant 全局的常量，一下全局的枚举，常量
- domain 领域层，对领域功能的实现
- interfaces 接口层（Restful+dubbo+web）

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

## 系统功能介介绍

     1.用户、菜单、角色、权限完全拥有。
     2.作为管理系统框架：角色可配置权限，用户可配置权限,可以精确到具体的按钮，够吗？
     3.管理系统其他功能：你还想要啥？

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