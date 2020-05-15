package com.abocode.jfaster.core.common.util;

import com.abocode.jfaster.core.common.model.json.ComboBox;
import org.apache.commons.beanutils.PropertyUtils;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.system.entity.Function;
import com.abocode.jfaster.system.entity.Role;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franky on 2016/3/15.
 */
public class SystemJsonUtils {

	/**
	 * 手工拼接JSON
	 */
	public static String getComboBoxJson(List<Role> list, List<Role> roles) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (Role node : list) {
			if (roles.size() > 0) {
				buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"");
				for (Role node1 : roles) {
					if (node.getId().equals( node1.getId())) {
						buffer.append(",\"selected\":true");
					}
				}
				buffer.append("},");
			} else {
				buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"},");
			}

		}
		buffer.append("]");

		// 将,\n]替换成\n]

		String tmp = buffer.toString();
		tmp = tmp.replaceAll(",]", "]");
		return tmp;

	}
	/**
	 * 根据模型生成JSON
	 */
	public static List<ComboBox> getComboBox(List<Role> list, List<Role> roles) {
		StringBuffer buffer = new StringBuffer();
		List<ComboBox> comboxBoxs = new ArrayList<ComboBox>();
		buffer.append("[");
		for (Role node : list) {
			ComboBox box = new ComboBox();
			box.setId(node.getId().toString());
			box.setText(node.getRoleName());
			if (roles.size() > 0) {
				for (Role node1 : roles) {
					if (node.getId().equals(node1.getId())) {
						box.setSelected(true);
					}
				}
			}
			comboxBoxs.add(box);
		}
		return comboxBoxs;

	}
	
	/**
     * 方法描述:  值替换工具
     * 作    者： zym
     * @param objList
     * @param perFieldName
     * @param sufFieldName
     * @return 格式：old_new,old2_new2
     * 返回类型： String
     */
	public static String listToReplaceStr(List<?> objList, String perFieldName, String sufFieldName){
		List<String> strList = new ArrayList<String>();
		for (Object object : objList) {
			String perStr = null;
			String sufStr = null;
			try {
				perStr = (String)PropertyUtils.getProperty(object, perFieldName);
				sufStr = (String)PropertyUtils.getProperty(object, sufFieldName);
			} catch (InvocationTargetException e) {
				LogUtils.error(e.getMessage());
			} catch (NoSuchMethodException e) {
				LogUtils.error(e.getMessage());
			} catch (IllegalAccessException e) {
				LogUtils.error(e.getMessage());
			}
			strList.add(perStr + "_" +sufStr);
		}
		return StringUtils.join(strList, ',');
	}


	public static String listToJson(String[] fields, int total, List list) throws Exception {
		Object[] values = new Object[fields.length];
		String jsonTemp = "{\"total\":" + total + ",\"rows\":[";
		for (int j = 0; j < list.size(); j++) {
			jsonTemp = jsonTemp + "{\"state\":\"closed\",";
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].toString();
				values[i] = TagUtil.fieldNameToValues(fieldName, list.get(j));
				jsonTemp = jsonTemp + "\"" + fieldName + "\"" + ":\"" + values[i] + "\"";
				if (i != fields.length - 1) {
					jsonTemp = jsonTemp + ",";
				}
			}
			if (j != list.size() - 1) {
				jsonTemp = jsonTemp + "},";
			} else {
				jsonTemp = jsonTemp + "}";
			}
		}
		jsonTemp = jsonTemp + "]}";
		return jsonTemp;
	}


	public static String getJsonData(@SuppressWarnings("rawtypes") List list) {

		StringBuffer buffer = new StringBuffer();

		buffer.append("[");

		iterGet(list, "0", buffer);

		buffer.append("]");

		// 将,\n]替换成\n]

		String tmp = buffer.toString();

		tmp = tmp.replaceAll(",\n]", "\n]");

		return tmp;

	}

	static int count = 0;

	/**
	 * 递归生成json格式的数据{id:1,text:'',children:[]}
	 *
	 * @param buffer
	 */
	static void iterGet(List<Function> list, String pid, StringBuffer buffer) {
		for (Function node : list) {

			// 查找所有父节点为pid的所有对象，然后拼接为json格式的数据
			if (node.getParentFunction() != null) {
				if (pid.equals(ConvertUtils.getString(node.getParentFunction().getId()))) {
					count++;
					buffer.append("{\'id\':" + node.getId() + ",\'text\':\'"
							+ node.getFunctionName() + "\',\'children\':[");
					// 递归
					iterGet(list, node.getId(), buffer);
					buffer.append("]},\n");
					count--;

				}
			}
		}

	}
}
