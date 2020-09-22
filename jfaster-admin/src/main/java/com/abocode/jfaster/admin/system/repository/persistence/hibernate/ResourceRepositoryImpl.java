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
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ResourceRepositoryImpl extends CommonRepositoryImpl implements ResourceRepository {
    /**
     * 文件上传
     *
     * @param uploadFile
     * @throws Exception
     */
    public Object uploadFile(FileUploadDto uploadFile) {
        Object object = uploadFile.getObject();
        if (uploadFile.getFileKey() != null) {
            update(object);
        } else {
            try {
                uploadFile.getMultipartRequest().setCharacterEncoding("UTF-8");
                MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
                ReflectHelper reflectHelper = new ReflectHelper(uploadFile.getObject());
                String localDiskPath = FileUtils.getResourceLocalPath();


                String path = "";
                String realPath = "";
                String entityName = uploadFile.getObject().getClass().getSimpleName();
                // 设置文件上传路径
                if (entityName.equals("Template")) {
                    realPath = localDiskPath + "/" + FileUtils.getResourceTemplate() + "/";
                    path = FileUtils.getResourceTemplate()  + "/";
                } else if (entityName.equals("Icon")) {
                    realPath = localDiskPath + "/" + uploadFile.getFolderPath() + "/";
                    path = uploadFile.getFolderPath() + "/";
                } else {
                    path = FileUtils.getResourceFile() + "/" + uploadFile.getFolderPath() + "/" + FileUtils.getDateDir() + "/";
                    // 文件数据库保存路径
                    realPath = localDiskPath + "/" + path + "/";// 文件的硬盘真实路径
                }

                File file = new File(realPath);
                if (!file.exists()) {
                    file.mkdirs();// 创建根目录
                }
                file = new File(realPath);
                if (!file.exists()) {
                    file.mkdirs();// 创建文件自定义子目录
                } else {
                    file = new File(realPath);
                    if (!file.exists()) {
                        file.mkdir();// 创建文件时间子目录
                    }
                }

                Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

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
                    this.saveOrUpdate(object);
                    // 文件拷贝到指定硬盘目录
                    FileCopyUtils.copy(mf.getBytes(), savefile);

                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return object;
    }

    /**
     * 文件下载或预览
     *
     * @param uploadFile
     * @throws Exception
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public HttpServletResponse viewOrDownloadFile(FileUploadDto uploadFile) {
        uploadFile.getResponse().setContentType("UTF-8");
        uploadFile.getResponse().setCharacterEncoding("UTF-8");
        BufferedOutputStream bos = null;
        HttpServletResponse response = uploadFile.getResponse();
        HttpServletRequest request = uploadFile.getRequest();
        String ctxPath = request.getSession().getServletContext().getRealPath("/");
        String downLoadPath = "";
        long fileLength = 0;
        InputStream bis = null;
        try {
            if (uploadFile.getRealPath() != null && uploadFile.getContent() == null) {
                downLoadPath = ctxPath + uploadFile.getRealPath();
                fileLength = new File(downLoadPath).length();
                bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            } else {
                if (uploadFile.getContent() != null)
                    bis = new ByteArrayInputStream(uploadFile.getContent());
                fileLength = uploadFile.getContent().length;
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
                response.setHeader("Content-disposition", "attachment; filename=" + new String((uploadFile.getTitleField() + "." + uploadFile.getExtend()).getBytes("GBK"), "ISO8859-1"));
                response.setHeader("Content-Length", String.valueOf(fileLength));
            }
            if (bis != null) {
                bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
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
            document.setXMLEncoding("UTF-8");
            // 创建根节点
            String rootname = fileImportDto.getEntityName() + "s";
            Element rElement = document.addElement(rootname);
            Class entityClass = fileImportDto.getEntityClass();
            String[] fields = fileImportDto.getField().split(",");
            // 得到导出对象的集合
            List objList = findAll(entityClass);
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
            File fileWriter = new File(ctxPath + "/" + fileImportDto.getFileName());
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
            Class entityClass;
            // 读取文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputXml);
            Element employees = document.getRootElement();
            // 遍历根节点下的子节点
            for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                Element employee = (Element) i.next();
                // 有实体名反射得到实体类
                entityClass = ClassLoaderUtils.getClassByScn(employee.getName());
                // 得到实体属性
                Field[] fields = TagUtil.getFiled(entityClass);
                // 得到实体的ID
                String id = employee.attributeValue(fields[0].getName());
                // 判断实体是否已存在
                Object entity = find(entityClass, id);
                // 实体不存在new个实体
                if (entity == null) {
                    entity = entityClass.newInstance();
                }
                // 根据反射给实体属性赋值
                for (Iterator j = employee.elementIterator(); j.hasNext(); ) {
                    Element node = (Element) j.next();
                    for (int k = 0; k < fields.length; k++) {
                        if (node.getName().equals(fields[k].getName())) {
                            String fieldName = fields[k].getName();
                            String stringLetter = fieldName.substring(0, 1).toUpperCase();
                            String setName = "set" + stringLetter + fieldName.substring(1);
                            Method setMethod = entityClass.getMethod(setName, new Class[]{fields[k].getType()});
                            Object type = TagUtil.getColumnType(fieldName, fields,node);
                            setMethod.invoke(entity,type);
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
    @SuppressWarnings("unchecked")
    public ComboTree tree(Org depart, boolean recursive) {
        ComboTree tree = new ComboTree();
        tree.setId(ConvertUtils.getString(depart.getId()));
        tree.setText(depart.getOrgName());
        List<Org> departsList = findAllByProperty(Org.class, "parentOrg.id", depart.getId());
        if (departsList != null && departsList.size() > 0) {
            tree.setState("closed");
            tree.setChecked(false);
            if (recursive) {// 递归查询子节点
                List<Org> departList = new ArrayList<Org>(departsList);
                List<ComboTree> children = new ArrayList<ComboTree>();
                for (Org d : departList) {
                    ComboTree t = tree(d, true);
                    children.add(t);
                }
                tree.setChildren(children);
            }
        }
        return tree;
    }

    public List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in, boolean recursive) {
        List<ComboTree> trees = new ArrayList();
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
    private ComboTree comboTree(Object obj, ComboTreeModel comboTreeModel, List in, boolean recursive) {
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
        if (in == null) {
        } else {
            if (in.size() > 0) {
                for (Object inobj : in) {
                    ReflectHelper reflectHelper2 = new ReflectHelper(inobj);
                    String inId = ConvertUtils.getString(reflectHelper2.getMethodValue(comboTreeModel.getIdField()));
                    if (inId.equals(id)) {
                        tree.setChecked(true);
                    }
                }
            }
        }

        List curChildList = (List) reflectHelper.getMethodValue(comboTreeModel.getChildField());
        if (curChildList != null && curChildList.size() > 0) {
            tree.setState("closed");
            tree.setChecked(false);

            if (recursive) { // 递归查询子节点
                List<ComboTree> children = new ArrayList<ComboTree>();
                List nextChildList = new ArrayList(curChildList);
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
    public List<TreeGrid> treegrid(List all, TreeGridModel treeGridModel) {
        List<TreeGrid> treegrid = new ArrayList<TreeGrid>();
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
            List childList = (List) reflectHelper.getMethodValue(treeGridModel.getChildList());

            if (childList != null && childList.size() > 0) {
                tg.setState("closed");
            }
            if (treeGridModel.getRoleId() != null) {
                String[] opStrings = {};
                List<RoleFunction> roleFunctions = findAllByProperty(RoleFunction.class, "function.id", id);

                if (roleFunctions.size() > 0) {
                    for (RoleFunction tRoleFunction : roleFunctions) {
                        RoleFunction roleFunction = tRoleFunction;
                        if (roleFunction.getRole().getId().toString().equals(treeGridModel.getRoleId())) {
                            String bbString = roleFunction.getOperation();
                            if (bbString != null) {
                                opStrings = bbString.split(",");
                                break;
                            }
                        }
                    }
                }
                List<Operation> operateions = findAllByProperty(Operation.class, "function.id", id);
                StringBuffer attributes = new StringBuffer();
                if (operateions.size() > 0) {
                    for (Operation tOperation : operateions) {
                        if (opStrings.length < 1) {
                            attributes.append("<input type=checkbox name=operatons value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationName());
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append("<input type=checkbox name=operatons");
                            for (int i = 0; i < opStrings.length; i++) {
                                if (opStrings[i].equals(tOperation.getId().toString())) {
                                    sb.append(" checked=checked");
                                }
                            }
                            sb.append(" value=" + tOperation.getId() + "_" + id + ">" + tOperation.getOperationName());
                            attributes.append(sb.toString());
                        }
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
            treegrid.add(tg);
        }
        return treegrid;
    }


    @Override
    public List<ComboTree> comTree(List<Org> all, ComboTree comboTree) {
        List<ComboTree> trees = new ArrayList<ComboTree>();
        for (Org depart : all) {
            trees.add(tree(depart, true));
        }
        return trees;

    }

    @Override
    public String getUploadFileContent(FileUploadDto uploadFile) {
        StringBuffer content = new StringBuffer();
        try {
            uploadFile.getMultipartRequest().setCharacterEncoding("UTF-8");
            MultipartHttpServletRequest multipartRequest = uploadFile.getMultipartRequest();
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (fileMap.size() > 1) {
                throw new Exception("只支持单个文件上传");
            }
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile mf = entity.getValue();// 获取上传文件对象

                InputStream stream = mf.getInputStream();
                BufferedReader bufferedreader = null;
                try {
                    bufferedreader = new BufferedReader(new InputStreamReader(stream, "gb2312"));
                    String s = null;
                    while (null != (s = bufferedreader.readLine())) {
                        content.append(s).append(",");
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
        } catch (Exception e1) {
            return "";
        }
        return content.toString();
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
            String savePath = file.getPath() + "/" + fileName;
            File savefile = new File(savePath);
            try {
                FileCopyUtils.copy(mf.getBytes(), savefile);
            } catch (IOException e) {
                throw new BusinessException("解析xml失败", e);
            }
        }
        parserXml(ctxPath + "/" + fileName);
    }

    @Override
    public List<ComboTree> findComboTree(String orgId) {
        List<Role[]> orgRoleArrList = findByHql(
                        "from Role r, RoleOrg ro, Org o WHERE r.id=ro.role.id AND ro.parentOrg.id=o.id AND o.id=?",
                        new Object[]{orgId});
        List<Role> orgRoleList = new ArrayList<Role>();
        for (Object[] roleArr : orgRoleArrList) {
            orgRoleList.add((Role) roleArr[0]);
        }

        List<Role> allRoleList =findAll(Role.class);
        ComboTreeModel comboTreeModel = new ComboTreeModel("id", "roleName", "");
        List<ComboTree> comboTrees = ComboTree(allRoleList,
                comboTreeModel, orgRoleList, false);
        return  comboTrees;
    }

}
