package org.jeecgframework.web.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.ComboBox;
import org.jeecgframework.core.util.ConvertUtils;
import org.jeecgframework.platform.common.tag.easyui.TagUtil;
import org.jeecgframework.web.system.entity.base.TSFunction;
import org.jeecgframework.web.system.entity.base.TSRole;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author  张代浩
 *
 */
public class SystemJsonUtils {

	/**
	 * 手工拼接JSON
	 */
	public static String getComboBoxJson(List<TSRole> list, List<TSRole> roles) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (TSRole node : list) {
			if (roles.size() > 0) {
				buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"");
				for (TSRole node1 : roles) {
					if (node.getId() == node1.getId()) {
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
	public static List<ComboBox> getComboBox(List<TSRole> list, List<TSRole> roles) {
		StringBuffer buffer = new StringBuffer();
		List<ComboBox> comboxBoxs = new ArrayList<ComboBox>();
		buffer.append("[");
		for (TSRole node : list) {
			ComboBox box = new ComboBox();
			box.setId(node.getId().toString());
			box.setText(node.getRoleName());
			if (roles.size() > 0) {
				for (TSRole node1 : roles) {
					if (node.getId() == node1.getId()) {
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
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			strList.add(perStr + "_" +sufStr);
		}
		return StringUtils.join(strList, ",");
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
	static void iterGet(List<TSFunction> list, String pid, StringBuffer buffer) {
		for (TSFunction node : list) {

			// 查找所有父节点为pid的所有对象，然后拼接为json格式的数据
			if (node.getTSFunction() != null) {
				if (pid.equals(ConvertUtils.getString(node.getTSFunction().getId()))) {
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
