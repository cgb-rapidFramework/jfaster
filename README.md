#JFaster项目说明

###Jfaster做最快最稳定的java快速开发框架
- 该项目由个人发起忙，只用于技术交流，不用于做商业用途。
- 项目的所有代码均开源共享，框架在jeecg的思想上发展而来，并且在真实环境中经过大量的检验，系统的安全、稳定、可靠。
- 项目框架为：spring MVC+Hibernate+bootstrap+easyui+poi。
- 运行环境：所有web服务器及jdk1.6+,浏览器为ie8+。

##项目优势

- 所有源代码均完全开源，无商业限制，开源协议为BSD。
-  框架为主流框架上手简单,文档齐全,支持主流数据库。
- 完善的代码封装，使用代码生成器根据数据库表分分钟完美呈现对表的CRUD功能。
- 代码结构清晰，便于维护,无冗余代码，可直接在实际项目中使用。
- 组件大量封装，基本操作只需要简单引用就可达到你想要的效果。
- 多种模版选择，可以在后台模版管理中进行选择，满足同一套框架，不同风格适用不同的客户。
- 封装POI导入导出插件，读写Excel简单快捷。
- 支持多语言，开发国际化项目非常方便。
- 主流框架选型，经历过大并发正式环境积累，正式环境无忧。
- 前后端分离：前端可以独立调整样式风格及显示和后端只需要关心后台业务逻辑的实现。
- 真正节省时间的框架，快而稳是我们追求的目标.


##模块说明

### core说明

 该模块封装了Spring JdbcTemplate和Hibernate对数据库的操作，统一查询方法命名Hibernate采用findXXX()，纯jdbc操作则使用queryXXX()。

### platform说明

该部分对页面显示元素、数据集及大量基础工具进行封装，在项目开发中可以直接进行使用。

### project说明

该部分为业务代码的实现，使用代码生成器快速生成所有代码，是一套小而巧的框架，技术选型最优的选择。

### 使用说明

 - JFaster.zip为mysql的数据库脚本（其他数据库请使用hibernate自行初始化，修改hibernate的生成策略即可）。
 - 代码生成器路径:[http://git.oschina.net/abocode-source/code-maker](http://git.oschina.net/abocode-source/code-maker)

## 系统功能介

     1.用户、菜单、角色、权限完全拥有。
     2.作为管理系统框架：角色可配置权限，用户可配置权限,可以精确到具体的按钮，够吗？
     3.管理系统其他功能：你还想要啥？

###皮肤展示
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


##加入我们：

   欢迎各位开发者提出建议、push代码,如有疑问联系，请使用如下联系方式。邮箱:guanxf@aliyun.com,昵称:糊涂,交流qq群:140586555。