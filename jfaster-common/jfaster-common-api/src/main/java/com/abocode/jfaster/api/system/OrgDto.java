package com.abocode.jfaster.api.system;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/9/8
 */
@Data
public class OrgDto {
    private String id;
    private OrgDto parentOrg;
    //部门名称
    private String orgName;
    //部门描述
    private String description;
    //机构编码
    private String orgCode;
    //机构编码
    private String orgType;
    //下属部门
    private List<OrgDto> orgs;
}
