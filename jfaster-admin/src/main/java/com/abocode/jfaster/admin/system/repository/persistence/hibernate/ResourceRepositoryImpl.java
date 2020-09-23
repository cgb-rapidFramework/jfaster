package com.abocode.jfaster.admin.system.repository.persistence.hibernate;

import com.abocode.jfaster.admin.system.dto.FileImportDto;
import com.abocode.jfaster.admin.system.dto.FileUploadDto;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.core.common.exception.BusinessException;
import com.abocode.jfaster.core.common.model.json.ComboTree;
import com.abocode.jfaster.core.common.model.json.TreeGrid;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.platform.view.ReflectHelper;
import com.abocode.jfaster.core.platform.view.interactions.easyui.ComboTreeModel;
import com.abocode.jfaster.core.platform.view.interactions.easyui.TreeGridModel;
import com.abocode.jfaster.core.repository.TagUtil;
import com.abocode.jfaster.core.repository.persistence.hibernate.CommonRepositoryImpl;
import com.abocode.jfaster.system.entity.Operation;
import com.abocode.jfaster.system.entity.Org;
import com.abocode.jfaster.system.entity.Role;
import com.abocode.jfaster.system.entity.RoleFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ResourceRepositoryImpl extends CommonRepositoryImpl implements ResourceRepository {

    public static final String FILE_SPARATOR = "/";
    public static final String UTF_8 = "UTF-8";
    public static final String CLOSED = "closed";

    /**
     * 文件上传
     *
     * @param uploadFile
     * @throws Exception
     */
    public Object uploadFile(FileUploadDto uploadFile) {
        if (uploadFile.getFileKey() != null) {
            update(uploadFile.getObject());
        } else {
            try {
                uploadFile.getMultipartRequest().setCharacterEncoding(UTF_8);
                MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
                Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
                doUpdate(fileMap,uploadFile);

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return uploadFile.getObject();
    }

    private void doUpdate(Map<String, MultipartFile> fileMap, FileUploadDto uploadFile) throws IOException {
        String path = "";
        String realPath = "";
        String localDiskPath = FileUtils.getResourceLocalPath();
        String entityName = uploadFile.getObject().getClass().getSimpleName();
        // 设置文件上传路径
        if (entityName.equals("Template")) {
            realPath = localDiskPath + FILE_SPARATOR + FileUtils.getResourceTemplate() + FILE_SPARATOR;
            path = FileUtils.getResourceTemplate() + FILE_SPARATOR;
        } else if (entityName.equals("Icon")) {
            realPath = localDiskPath + FILE_SPARATOR + uploadFile.getFolderPath() + FILE_SPARATOR;
            path = uploadFile.getFolderPath() + FILE_SPARATOR;
        } else {
            path = FileUtils.getResourceFile() + FILE_SPARATOR + uploadFile.getFolderPath() + FILE_SPARATOR + FileUtils.getDateDir() + FILE_SPARATOR;
            // 文件数据库保存路径
            realPath = localDiskPath + FILE_SPARATOR + path + FILE_SPARATOR;// 文件的硬盘真实路径
        }
        FileUtils.mkdirIfNotExists(realPath);
        FileUtils.mkdirIfNotExists(path);

        ReflectHelper reflectHelper = new ReflectHelper(uploadFile.getObject());
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();// 获取上传文件对象
            String originalFilename = mf.getOriginalFilename();// 获取文件名
            String extend = FileUtils.getExtend(originalFilename);// 获取文件扩展名
            String fileName = originalFilename;
            if (uploadFile.isRename()) {
                fileName = FileUtils.getFileName(originalFilename);
            }
            String savePath = realPath + fileName;// 文件保存全路径
            String fileprefixName = FileUtils.getFilePrefix(originalFilename);
            if (uploadFile.getTitleField() != null) {
                reflectHelper.setMethodValue(uploadFile.getTitleField(), fileprefixName);// 动态调用set方法给文件对象标题赋值
            }
            if (uploadFile.getExtend() != null) {
                // 动态调用 set方法给文件对象内容赋值
                reflectHelper.setMethodValue(uploadFile.getExtend(), extend);
            }
            if (uploadFile.getByteField() != null) {
                // 二进制文件保存在数据库中
                reflectHelper.setMethodValue(uploadFile.getByteField(), StreamUtils.inputStreamToByte(mf.getInputStream()));
            }
            File savefile = new File(savePath);
            if (uploadFile.getRealPath() != null) {
                // 设置文件数据库的物理路径
                reflectHelper.setMethodValue(uploadFile.getRealPath(), path + fileName);
            }
            this.saveOrUpdate(uploadFile.getObject());
            // 文件拷贝到指定硬盘目录
            FileCopyUtils.copy(mf.getBytes(), savefile);
        }

    }

    /**
     * 文件下载或预览
     *
     * @param uploadFile
     * @throws Exception
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HttpServletResponse viewOrDownloadFile(FileUploadDto uploadFile) throws UnsupportedEncodingException {
        uploadFile.getResponse().setContentType(UTF_8);
        uploadFile.getResponse().setCharacterEncoding(UTF_8);

        HttpServletResponse response = uploadFile.getResponse();
        HttpServletRequest request = uploadFile.getRequest();
        String ctxPath = request.getSession().getServletContext().getRealPath(FILE_SPARATOR);
        String downLoadPath = "";
        long fileLength = 0;
        InputStream bis = null;
        if (uploadFile.getRealPath() != null && uploadFile.getContent() == null) {
            downLoadPath = ctxPath + uploadFile.getRealPath();
            fileLength = new File(downLoadPath).length();
            try {
                bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            } catch (FileNotFoundException e) {
                throw new BusinessException("异常退出", e);
            }finally {
                IOUtils.closeQuietly(bis);
            }
        } else {
            if (uploadFile.getContent() != null){
                bis = new ByteArrayInputStream(uploadFile.getContent());
                fileLength = uploadFile.getContent().length;
            }
        }

        if (!uploadFile.isView() && uploadFile.getExtend() != null) {
            if (uploadFile.getExtend().equals("text")) {
                response.setContentType("text/plain;");
            } else if (uploadFile.getExtend().equals("doc")) {
                response.setContentType("application/msword;");
            } else if (uploadFile.getExtend().equals("xls")) {
                response.setContentType("application/ms-excel;");
            } else if (uploadFile.getExtend().equals("pdf")) {
                response.setContentType("application/pdf;");
            } else if (uploadFile.getExtend().equals("jpg") || uploadFile.getExtend().equals("jpeg")) {
                response.setContentType("image/jpeg;");
            } else {
                response.setContentType("application/x-msdownload;");
            }
            response.setHeader("Content-disposition", "attachment; filename=" + new String((uploadFile.getTitleField() + "." + uploadFile.getExtend()).getBytes("GBK"), StandardCharsets.ISO_8859_1));
            response.setHeader("Content-Length", String.valueOf(fileLength));
        }

        if (bis != null) {
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(response.getOutputStream());
            } catch (IOException e) {
                throw new BusinessException("异常退出", e);
            }
            byte[] buff = new byte[2048];
            int bytesRead = 0;
            while (true) {
                try {
                    if (-1 == bis.read(buff, 0, buff.length)) {
                        break;
                    }
                } catch (IOException e) {
                    throw new BusinessException("异常退出", e);
                }
                try {
                    bos.write(buff, 0, bytesRead);
                } catch (IOException e) {
                    throw new BusinessException("异常退出", e);
                }
            }
        }
        return response;
    }

    /**
     * 生成XML importFile 导出xml工具类
     */
    public HttpServletResponse createXml(FileImportDto fileImportDto) {
        HttpServletResponse response = fileImportDto.getResponse();
        HttpServletRequest request = fileImportDto.getRequest();
        try {
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            document.setXMLEncoding(UTF_8);
            // 创建根节点
            String rootname = fileImportDto.getEntityName() + "s";
            Element rElement = document.addElement(rootname);
            String[] fields = fileImportDto.getField().split(",");
            // 得到导出对象的集合
            List<?> objList = findAll(fileImportDto.getEntityClass());
            for (Object t : objList) {
                Element childElement = rElement.addElement(fileImportDto.getEntityName());
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = fields[i];
                    // 第一为实体的主键
                    if (i == 0) {
                        childElement.addAttribute(fieldName, String.valueOf(TagUtil.fieldNameToValues(fieldName, t)));
                    } else {
                        Element name = childElement.addElement(fieldName);
                        name.setText(String.valueOf(TagUtil.fieldNameToValues(fieldName, t)));
                    }
                }

            }
            String ctxPath = request.getSession().getServletContext().getRealPath("");
            File fileWriter = new File(ctxPath + FILE_SPARATOR + fileImportDto.getFileName());
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(fileWriter));

            xmlWriter.write(document);
            xmlWriter.close();
            // 下载生成的XML文件
            FileUploadDto uploadFile = new FileUploadDto(request, response);
            uploadFile.setRealPath(fileImportDto.getFileName());
            uploadFile.setTitleField(fileImportDto.getFileName());
            uploadFile.setExtend("bak");
            viewOrDownloadFile(uploadFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * 解析XML文件将数据导入数据库中
     */
    public void parserXml(String fileName) {
        try {
            File inputXml = new File(fileName);
            // 读取文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputXml);
            Element employees = document.getRootElement();
            // 遍历根节点下的子节点
            Iterator<?> i;
            for (i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                // 有实体名反射得到实体类
                Class<?> entityClass = ClassLoaderUtils.getClassByScn(employee.getName());
                // 得到实体属性
                Field[] fields = TagUtil.getFiled(entityClass);
                // 得到实体的ID
                String id = employee.attributeValue(fields[0].getName());
                // 判断实体是否已存在
                Object entity = find(entityClass, id);
                // 根据反射给实体属性赋值
                Iterator<?> j;
                for (j = employee.elementIterator(); j.hasNext(); ) {
                    Element node = (Element) j.next();
                    for (int k = 0; k < fields.length; k++) {
                        if (node.getName().equals(fields[k].getName())) {
                            String fieldName = fields[k].getName();
                            String stringLetter = fieldName.substring(0, 1).toUpperCase();
                            String setName = "set" + stringLetter + fieldName.substring(1);
                            Method setMethod = entityClass.getMethod(setName, fields[k].getType());
                            Object type = TagUtil.getColumnType(fieldName, fields, node);
                            setMethod.invoke(entity, type);
                        }
                    }
                }
                saveOrUpdate(entity);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 根据模型生成JSOn 全部对象
     *
     * @param depart    已拥有的对象
     * @param recursive 模型
     * @return
     */
    public ComboTree tree(Org depart, boolean recursive) {
        ComboTree tree = new ComboTree();
        tree.setId(ConvertUtils.getString(depart.getId()));
        tree.setText(depart.getOrgName());
        List<Org> departsList = findAllByProperty(Org.class, "parentOrg.id", depart.getId());
        if (!CollectionUtils.isEmpty(departsList)) {
            tree.setState(CLOSED);
            tree.setChecked(false);
            if (recursive) {// 递归查询子节点
                List<Org> departList = new ArrayList<>(departsList);
                List<ComboTree> children = new ArrayList<>();
                for (Org d : departList) {
                    ComboTree t = tree(d, true);
                    children.add(t);
                }
                tree.setChildren(children);
            }
        }
        return tree;
    }

    public List<ComboTree> buildComboTree(List<?> all, ComboTreeModel comboTreeModel, List<?> in, boolean recursive) {
        List<ComboTree> trees = new ArrayList<>();
        for (Object obj : all) {
            trees.add(comboTree(obj, comboTreeModel, in, recursive));
        }
        return trees;

    }

    /**
     * 构建ComboTree
     *
     * @param obj
     * @param comboTreeModel
     * @param in
     * @param recursive      是否递归子节点
     * @return
     */
    private ComboTree comboTree(Object obj, ComboTreeModel comboTreeModel, List<?> in, boolean recursive) {
        ComboTree tree = new ComboTree();
        Map<String, String> attributes = new HashMap<>();
        ReflectHelper reflectHelper = new ReflectHelper(obj);
        String id = ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getIdField()));
        tree.setId(id);
        tree.setText(ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getTextField())));
        if (comboTreeModel.getSrcField() != null) {
            attributes.put("href", ConvertUtils.getString(reflectHelper.getMethodValue(comboTreeModel.getSrcField())));
            tree.setAttributes(attributes);
        }
        if (in != null) {
            for (Object inobj : in) {
                ReflectHelper reflectHelper2 = new ReflectHelper(inobj);
                String inId = ConvertUtils.getString(reflectHelper2.getMethodValue(comboTreeModel.getIdField()));
                if (inId.equals(id)) {
                    tree.setChecked(true);
                }
            }
        }
        Object child = reflectHelper.getMethodValue(comboTreeModel.getChildField());
        Collection<?> curChildList = (Collection<?>) child;
        if (!CollectionUtils.isEmpty(curChildList)) {
            tree.setState(CLOSED);
            tree.setChecked(false);
            if (recursive) { // 递归查询子节点
                List<ComboTree> children = new ArrayList<>();
                List<Object> nextChildList = new ArrayList<>(curChildList);
                for (Object childObj : nextChildList) {
                    ComboTree t = comboTree(childObj, comboTreeModel, in, recursive);
                    children.add(t);
                }
                tree.setChildren(children);
            }
        }
        return tree;
    }

    /**
     * 构建树形数据表
     */
    public List<TreeGrid> getTreeGrid(List<?> all, TreeGridModel treeGridModel) {
        List<TreeGrid> treeGrids = new ArrayList<>();
        for (Object obj : all) {
            ReflectHelper reflectHelper = new ReflectHelper(obj);
            TreeGrid tg = new TreeGrid();
            String id = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getIdField()));
            String src = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getSrc()));
            String text = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getTextField()));
            if (!StrUtils.isEmpty(treeGridModel.getOrder())) {
                String order = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getOrder()));
                tg.setOrder(order);
            }
            tg.setId(id);
            if (treeGridModel.getIcon() != null) {
                String iconPath = TagUtil.fieldNameToValues(treeGridModel.getIcon(), obj).toString();
                tg.setCode(iconPath);
            }
            tg.setSrc(src);
            tg.setText(text);
            if (treeGridModel.getParentId() != null) {
                Object pid = TagUtil.fieldNameToValues(treeGridModel.getParentId(), obj);
                if (pid != null) {
                    tg.setParentId(pid.toString());
                } else {
                    tg.setParentId("");
                }
            }
            if (treeGridModel.getParentText() != null) {
                Object ptext = TagUtil.fieldNameToValues(treeGridModel.getTextField(), obj);
                if (ptext != null) {
                    tg.setParentText(ptext.toString());
                } else {
                    tg.setParentText("");
                }

            }
            List<?> childList = (List) reflectHelper.getMethodValue(treeGridModel.getChildList());

            if (!CollectionUtils.isEmpty(childList)) {
                tg.setState(CLOSED);
            }
            if (treeGridModel.getRoleId() != null) {
                String[] opStrings = {};
                List<RoleFunction> roleFunctions = findAllByProperty(RoleFunction.class, "function.id", id);
                for (RoleFunction tRoleFunction : roleFunctions) {
                    RoleFunction roleFunction = tRoleFunction;
                    if (roleFunction.getRole().getId().equals(treeGridModel.getRoleId())) {
                        String bbString = roleFunction.getOperation();
                        if (bbString != null) {
                            opStrings = bbString.split(",");
                            break;
                        }
                    }
                }
                List<Operation> operateions = findAllByProperty(Operation.class, "function.id", id);
                StringBuilder attributes = new StringBuilder();
                for (Operation tOperation : operateions) {
                    if (opStrings.length < 1) {
                        attributes.append("<input type=checkbox name=operatons value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationName());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("<input type=checkbox name=operatons");
                        for (int i = 0; i < opStrings.length; i++) {
                            if (opStrings[i].equals(tOperation.getId())) {
                                sb.append(" checked=checked");
                            }
                        }
                        sb.append(" value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationName());
                        attributes.append(sb.toString());
                    }
                }
                tg.setOperations(attributes.toString());
            }
            if (treeGridModel.getFieldMap() != null) {
                tg.setFieldMap(new HashMap<>());
                for (Map.Entry<String, String> entry : treeGridModel.getFieldMap().entrySet()) {
                    Object fieldValue = reflectHelper.getMethodValue(entry.getValue());
                    tg.getFieldMap().put(entry.getKey(), fieldValue.toString());
                }
            }
            if (treeGridModel.getFunctionType() != null) {
                String functionType = ConvertUtils.getString(reflectHelper.getMethodValue(treeGridModel.getFunctionType()));
                tg.setFunctionType(functionType);
            }
            treeGrids.add(tg);
        }
        return treeGrids;
    }


    @Override
    public List<ComboTree> comTree(List<Org> all, ComboTree comboTree) {
        List<ComboTree> trees = new ArrayList<>();
        for (Org depart : all) {
            trees.add(tree(depart, true));
        }
        return trees;

    }


    @Override
    public void readAndParserXml(String ctxPath, FileUploadDto uploadFile) {
        File file = new File(ctxPath);
        if (!file.exists()) {
            file.mkdir();// 创建文件根目录
        }
        MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String fileName = "";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();// 获取上传文件对象
            fileName = mf.getOriginalFilename();// 获取文件名
            String savePath = file.getPath() + FILE_SPARATOR + fileName;
            File savefile = new File(savePath);
            try {
                FileCopyUtils.copy(mf.getBytes(), savefile);
            } catch (IOException e) {
                throw new BusinessException("解析xml失败", e);
            }
        }
        parserXml(ctxPath + FILE_SPARATOR + fileName);
    }

    @Override
    public List<ComboTree> findComboTree(String orgId) {
        List<Role[]> orgRoleArrList = findByHql(
                "from Role r, RoleOrg ro, Org o WHERE r.id=ro.role.id AND ro.parentOrg.id=o.id AND o.id=?",
                new Object[]{orgId});
        List<Role> orgRoleList = new ArrayList<>();
        for (Object[] roleArr : orgRoleArrList) {
            orgRoleList.add((Role) roleArr[0]);
        }

        List<Role> allRoleList = findAll(Role.class);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "roleName", "");
        return buildComboTree(allRoleList,
                comboTreeModel, orgRoleList, false);
    }

}
