package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Title: Entity
 * @Description: 模版管理
 * @author guanxf
 * @date 2016-05-15 23:43:02
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_s_template")
@Data
public class Template extends AbstractIdEntity implements java.io.Serializable {
	/**模版编码*/
	private java.lang.String theme;
	/**模版名称*/
	private java.lang.String name;
	/**风格*/
	private java.lang.String style;
	/**模版图片*/
	private java.lang.String image;
	/**主页*/
	private java.lang.String pageMain;
	/**登录页面*/
	private java.lang.String pageLogin;
	/**状态0-未使用，1-使用*/
	private java.lang.Integer status;
	/**更新日期*/
	private java.util.Date updateDate;
	/**更新人编号*/
	private java.lang.String updateBy;
	/**更新人姓名*/
	private java.lang.String updateName;
}
