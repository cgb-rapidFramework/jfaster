package com.abocode.jfaster.admin.system.dto;

import com.abocode.jfaster.core.platform.poi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * Created by Franky on 2016/3/15.
 */
@Getter
@Setter
public class ExlUserDto {
    @Excel(exportName = "用户名", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Pattern(regexp = "([a-zA-Z0-9_]*[a-zA-Z_][a-zA-Z0-9_]*)")
    @Size(min =4, max = 30)
    private String username;// 用户名
    @Excel(exportName = "真实姓名", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 2, max = 30)
    private String realName;// 真实姓名

    @Excel(exportName = "组织机构", exportConvertSign = 0, exportFieldWidth = 18, importConvertSign = 0)
    @NotNull
    @Size(min = 3, max = 50)
    private String orgName;

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
    // 状态1：在线,2：离线,0：禁用
    private Short status=0;
}
