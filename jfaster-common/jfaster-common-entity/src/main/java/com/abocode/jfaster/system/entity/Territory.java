package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *地域管理表
 */
@Entity
@Table(name = "t_s_territory")
@Data
public class Territory extends AbstractIdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	//父地域
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "territory_Id")
	private Territory parentTerritory;
	//地域名称
	@Column(name = "territory_name",nullable = false, length = 50)
	private String territoryName;
	//等级
	@Column(name = "territory_level",nullable = false,length = 1)
	private Short territoryLevel;
	//同区域中的显示顺序
	@Column(name = "territory_sort",nullable = false,length = 3)
	private String territorySort;
	//区域码
	@Column(name = "territory_code",nullable = false,length = 10)
	private String territoryCode;
	//区域名称拼音
	@Column(name = "territory_zh",length = 40)
	private String territoryZh;
	//wgs84格式经度(mapabc 的坐标系)
	@Column(nullable = false,length = 40)
	private double longitude;
	//wgs84格式纬度(mapabc 的坐标系)
	@Column(nullable = false,length = 40)
	private double latitude;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentTerritory")
	private List<Territory> territories = new ArrayList<>();
}