package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author guanxf
 * @Title: Entity
 * @Description: 模版管理
 */
@Entity
@Table(name = "t_s_template")
@Data
public class Template extends AbstractIdEntity implements java.io.Serializable {
    /**
     * 模版编码
     */
    private String theme;
    /**
     * 模版名称
     */
    private String name;
    /**
     * 风格
     */
    private String style;
    /**
     * 模版图片
     */
    private String image;
    /**
     * 主页
     */
    @Column(name = "page_main")
    private String pageMain;
    /**
     * 登录页面
     */
    @Column(name = "page_login")
    private String pageLogin;
    /**
     * 状态0-未使用，1-使用
     */
    private Integer status;
    @Column(name = "update_by", nullable = false, length = 20)
    private String updateBy;
    @Column(name = "update_by_id", nullable = false, length = 32)
    private String updateById;
    @Column(name = "update_date", nullable = false, length = 32)
    private Date updateDate;
}
