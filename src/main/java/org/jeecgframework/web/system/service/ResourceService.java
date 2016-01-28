package org.jeecgframework.web.system.service;

import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.ImportFile;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.core.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.entity.base.TSDepart;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ResourceService {
	/**
	 * 文件上传
	 * 
	 * @param uploadFile
	 */
	public <T> T uploadFile(UploadFile uploadFile);
   /***
    * 预览及下载文件
    * @param uploadFile
    * @return
    */
	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile);

	/**
	 * 生成XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	public HttpServletResponse createXml(ImportFile fileName);

	/**
	 * 解析XML文件
	 * 
	 * @param fileName
	 *            XML全路径
	 */
	public void parserXml(String fileName);
	
	/**
     * 根据模型生成ComboTree JSON
     *
     * @param all 全部对象
     * @param comboTree 模型
     * @return List<ComboTree>
     */
	public List<ComboTree> comTree(List<TSDepart> all, ComboTree comboTree);

	/**
	 * 根据模型生成JSON
	 * 
	 * @param all
	 *            全部对象
	 * @param in
	 *            已拥有的对象
	 * @param recursive
	 *            是否递归加载所有子节点
	 * @return List<ComboTree>
	 */
	public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel,
			List in, boolean recursive);


	/**
	 * 构建树形数据表
	 * 
	 * @param all
	 * @param treeGridModel
	 * @return
	 */
	public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel);
	/**
	 * 读取上传文件的内容
	 * @param uploadFile
	 * @return
	 */
	public String getUploadFileContent(UploadFile uploadFile);

}
