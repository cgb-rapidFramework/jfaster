package com.abocode.jfaster.api.system;

import lombok.Data;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/8
 */
@Data
public class UserDto {
    private String id;// 用户名
    private String username;// 用户名
    private String realName;// 真实姓名
    private String password;//用户密码

    private String signature;// 签名
    private Short status;// 状态1：在线,2：离线,0：禁用
    private String mobilePhone;// 手机
    private String officePhone;// 办公电话
    private String email;// 邮箱
}
