#BMS框架说明

     本框架由个人发起忙，只用于技术交流，不用于做商业用途，如果有侵犯你的权益，请和个人联系：guanxf_m@126.com

##协议说明

    本开源框架采用GPL协议，大家可以免费用于各种商业项目.

##平台说明

    平台采用maven进行代码管理，平台在jeecg的基础上进行代码大量的抽起及重构，并且在真实环境中进行过校验，保证系统的安全性及稳定性。现在将平台分为三大基本模块，bms-core模块、bms-platform及bms-project模块。

##模块说明

### bms-core说明

     该模块主要是封装jdbc操作及Hibernate集成及cq集成，采用新模式find及query查询。如果是Hibernate相关操作采用find方法，如果是纯jdbc操作则使用query查询。（注：此部分在jeecg基础上进行过大量的修正）。

### bms-paltform说明

    该部分封装了easyui相关的组件，封装的组件在前台进行直接使用。但是需要引入bms-core，因为返回的数据集是easyui相关的组建对象。

### bms-project说明

    该部分为业务代码的实现，拥有jeecg强大的代码生成器及系统管理功能。和jeecg的区别如下：

    1、代码结构清晰（经过大量的重构）
    2、bug较少（解决若干jeecg的bug,如session共享;显示特殊字符；上传附件等，这儿不在详细描述）
    3、无冗余代码，可以直接使用于项目


###备注：

      bms-core，bms-paltform的最新jar在lib目录下

###加入我们：

    长期招收有意于bms开发的技术人才，需要2年工作经验以上。联系方式：qq:54314720,昵称franky

