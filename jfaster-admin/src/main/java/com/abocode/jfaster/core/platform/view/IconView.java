package com.abocode.jfaster.core.platform.view;
import lombok.Data;

/**
 * TIcon entity.
 *  @author  guanxf
 */
@Data
public class IconView  implements java.io.Serializable {
	private String id;
	private String iconName;
	private Short iconType;
	private String iconPath;
	private byte[] iconContent;
	private String iconClazz;
	private String iconExtend;

}