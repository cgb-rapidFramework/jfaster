package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.admin.system.dto.FileImportDto;
import com.abocode.jfaster.admin.system.dto.FileUploadDto;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.system.entity.Org;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ResourceRepository extends CommonRepository {
    /**
     * 文件上传
     *
     * @param uploadFile
     */
    <T> T uploadFile(FileUploadDto uploadFile);

    /***
     * 预览及下载文件
     * @param uploadFile
     * @return
     */
    HttpServletResponse viewOrDownloadFile(FileUploadDto uploadFile) throws UnsupportedEncodingException;

    /**
     * 生成XML文件
     *
     * @param fileName XML全路径
     */
    HttpServletResponse createXml(FileImportDto fileName);

    /**
     * 解析XML文件
     *
     * @param fileName XML全路径
     */
    void parserXml(String fileName);

    /**
     * 根据模型生成ComboTree JSON
     *
     * @param all       全部对象
     * @param comboTree 模型
     * @return List<ComboTree>
     */
    List<ComboTree> comTree(List<Org> all, ComboTree comboTree);

    /**
     * 根据模型生成JSON
     *
     * @param all       全部对象
     * @param in        已拥有的对象
     * @param recursive 是否递归加载所有子节点
     * @return List<ComboTree>
     */
    List<ComboTree> buildComboTree(List<?> all, ComboTreeModel comboTreeModel,
                                   List<?> in, boolean recursive);


    /**
     * 构建树形数据表
     *
     * @param all
     * @param treeGridModel
     * @return
     */
    List<TreeGrid> getTreeGrid(List<?> all, TreeGridModel treeGridModel);


    void readAndParserXml(String ctxPath, FileUploadDto uploadFile);

    List<ComboTree> findComboTree(String orgId);
}
