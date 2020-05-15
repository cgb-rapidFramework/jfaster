package com.abocode.jfaster.system.entity;
import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *地域管理表
 * @author  张代浩
 */
@Entity
@Table(name = "t_s_territory")
@Data
public class Territory extends AbstractIdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	//父地域
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "territoryParentId")
	private Territory parentTerritory;
	//地域名称
	@Column(name = "territoryname", nullable = false, length = 50)
	private String territoryName;
	//等级
	@Column(name = "territorylevel",nullable = false,length = 1)
	private Short territoryLevel;
	//同区域中的显示顺序
	@Column(name = "territorysort",nullable = false,length = 3)
	private String territorySort;
	//区域码
	@Column(name = "territorycode",nullable = false,length = 10)
	private String territoryCode;
	//区域名称拼音
	@Column(name = "territory_pinyin",nullable = true,length = 40)
	private String territoryPinyin;
	//wgs84格式经度(mapabc 的坐标系)
	@Column(name = "x_wgs84",nullable = false,length = 40)
	private double xwgs84;
	//wgs84格式纬度(mapabc 的坐标系)
	@Column(name = "y_wgs84",nullable = false,length = 40)
	private double ywgs84;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentTerritory")
	private List<Territory> territories = new ArrayList<Territory>();
}