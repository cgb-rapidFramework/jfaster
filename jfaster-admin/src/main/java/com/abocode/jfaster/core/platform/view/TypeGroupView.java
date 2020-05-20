package com.abocode.jfaster.core.platform.view;
import com.abocode.jfaster.system.entity.Type;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * TTypegroup entity.
 *
 */
@Data
public class TypeGroupView  implements java.io.Serializable {
	public static Map<String, List<TypeView>> allTypes = new HashMap<String,List<TypeView>>();
	private String typeGroupName;
	private String typeGroupCode;
	private List<Type> types = new ArrayList<Type>();

}