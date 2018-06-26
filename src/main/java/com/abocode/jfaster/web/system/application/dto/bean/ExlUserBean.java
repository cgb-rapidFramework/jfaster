package com.abocode.jfaster.web.system.application.dto.bean;

import com.abocode.jfaster.core.platform.poi.excel.annotation.Excel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * Created by Franky on 2016/3/15.
 */
public class ExlUserBean {
    @Excel(exportName = "用户名", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Pattern(regexp = "([a-zA-Z0-9_]*[a-zA-Z_][a-zA-Z0-9_]*)")
    @Size(min =4, max = 30)
    private String userName;// 用户名
    @Excel(exportName = "真实姓名", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 2, max = 30)
    private String realName;// 真实姓名

    @Excel(exportName = "组织机构", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 3, max = 50)
    private String  departName;

    @Excel(exportName = "角色", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 3, max = 18)
    private String  roleName;
    @Excel(exportName = "用户密码", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 6, max = 18)
    private String password;

    @Excel(exportName = "手机号码", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Pattern(regexp = "^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\d{8}$")
    private String mobilePhone;// 手机
    @Excel(exportName = "电话", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 2, max = 50)
    private String officePhone;// 办公电话
    @Excel(exportName = "邮箱", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 2, max = 50)
    private String email;// 邮箱

    private Short status=0;// 状态1：在线,2：离线,0：禁用

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /*private String browser;// 用户使用浏览器类型
    private Short activitiSync;//是否同步工作流引擎
    private byte[] signature;// 签名文件
    private TSDepart currentDepart = new TSDepart();// 当前部门
    private String userKey;// 用户验证唯一标示

    private String signatureFile;// 签名文件*/
}
