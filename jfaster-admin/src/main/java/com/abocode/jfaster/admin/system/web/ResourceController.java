package com.abocode.jfaster.admin.system.web;

import com.abocode.jfaster.admin.system.dto.FileUploadDto;
import com.abocode.jfaster.core.common.model.json.AjaxJson;
import com.abocode.jfaster.core.common.model.json.AjaxJsonBuilder;
import com.abocode.jfaster.core.common.model.json.DataGrid;
import com.abocode.jfaster.admin.system.dto.FileImportDto;
import com.abocode.jfaster.core.common.util.*;
import com.abocode.jfaster.core.platform.view.ReflectHelper;
import com.abocode.jfaster.admin.system.repository.ResourceRepository;
import com.abocode.jfaster.admin.system.repository.SystemRepository;
import com.abocode.jfaster.core.persistence.hibernate.qbc.CriteriaQuery;
import com.abocode.jfaster.core.platform.view.widgets.easyui.TagUtil;
import com.abocode.jfaster.system.entity.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/1/27.
 */
@Controller
@RequestMapping("/resourceController")
public class ResourceController {
    @Autowired
    private SystemRepository systemService;
    @Autowired
    private ResourceRepository resourceService;
      /**
     * 生成XML文件
     *
     * @return
     */
    @RequestMapping(params = "createxml")
    public void createxml(HttpServletRequest request, HttpServletResponse response) {
        String field = request.getParameter("field");
        String entityname = request.getParameter("entityname");
        FileImportDto fileImportDto = new FileImportDto(request, response);
        fileImportDto.setField(field);
        fileImportDto.setEntityName(entityname);
        fileImportDto.setFileName(entityname + ".bak");
        fileImportDto.setEntityClass(ClassLoaderUtils.getClassByScn(entityname));
        resourceService.createXml(fileImportDto);
    }

    /**
     * 生成XML文件parserXml
     *
     * @return
     */
    @RequestMapping(params = "parserXml")
    @ResponseBody
    public AjaxJson parserXml(HttpServletRequest request) {
        String ctxPath = request.getSession().getServletContext().getRealPath("");
        FileUploadDto uploadFile = new FileUploadDto(request);
        resourceService.readAndParserXml(ctxPath,uploadFile);
        return AjaxJsonBuilder.success();
    }

    /**
     * 文件上传通用跳转
     * @param req
     * @return
     */
    @RequestMapping(params = "commonUpload")
    public ModelAndView commonUpload(HttpServletRequest req) {
        return new ModelAndView("common/upload/uploadView");
    }

    /**
     * 保存文件
     *
     * @param attachment
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "saveFiles", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveFiles(HttpServletRequest request, FileUpload attachment) {
        AjaxJson j = new AjaxJson();
        String fileKey = ConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
        String documentTitle = ConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题

        Boolean  multi= ConvertUtils.getBoolean(request.getParameter("multi"));
        String  sessionKey=request.getParameter("sessionKey");

        if(!multi){
            List<FileUpload> attachments=this.systemService.findAllByProperty(FileUpload.class,"sessionKey",sessionKey);
            if(attachments.size()>0){
                Map<String, Object> attributes= buildAttributes(attachments.get(0));
                j.setAttributes(attributes);
                j.setMsg("你只能上传一个附件");
                j.setSuccess(false);
                return  j;
            }
        }

        if (StringUtils.isNotEmpty(fileKey)) {
            attachment.setId(fileKey);
            attachment = systemService.findEntity(FileUpload.class, fileKey);
            attachment.setName(documentTitle);
        }
        attachment.setSessionKey(sessionKey);
        attachment.setCreateDate(DateUtils.getTimestamp());
        FileUploadDto uploadFile = new FileUploadDto(request, attachment);
        String fileType=request.getParameter("fileType");
        if(StringUtils.isEmpty(fileType)){
            fileType="files";
        }
        uploadFile.setFolderPath(fileType);
        attachment = resourceService.uploadFile(uploadFile);
        j.setMsg("文件添加成功");
        Map<String, Object> attributes= buildAttributes(attachment);
        j.setAttributes(attributes);
        return j;
    }


    /**
     * 封装map
     * 该map的值不能随便改动
     * @param attachment
     * @return
     */
    private Map<String, Object> buildAttributes(FileUpload attachment) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("fileKey", attachment.getId());
        attributes.put("url", attachment.getPath());
        attributes.put("name", attachment.getName());
        attributes.put("viewhref", "resourceController.do?viewFile&fileKey=" + attachment.getId());
        attributes.put("delurl", "resourceController.do?deleteFile&fileKey=" + attachment.getId());
        return attributes;
    }


    /**
     * 删除继承于TSUploadFile附件的公共方法
     * @param request
     * @return
     */
    @RequestMapping(params = "deleteFile")
    @ResponseBody
    public AjaxJson deleteFile(HttpServletRequest request) {
        String fileKey = ConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
        Assert.isTrue(StringUtils.isNotEmpty(fileKey),"文件已经不存在了");
        FileUpload attachment = systemService.findEntity(FileUpload.class,fileKey);
        ResourceUtils.delete(ResourceUtils.getResourceLocalPath()+"/"+attachment.getPath());
        systemService.delete(attachment);
        String message = attachment.getName() + "删除成功";
        return AjaxJsonBuilder.success(message);
    }


    /**
     * 附件预览页面打开链接
     *
     * @return
     */
    @RequestMapping(params = "viewFile")
    public ModelAndView viewFile(HttpServletRequest request) {
        String fileKey = ConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
        String subclassname = ConvertUtils.getString(request.getParameter("subclassname"), "UploadFile");
        String contentfield = ConvertUtils.getString(request.getParameter("contentfield"));
        Class fileClass = ClassLoaderUtils.getClassByScn(subclassname);// 附件的实际类
        Object fileobj = systemService.findEntity(fileClass, fileKey);
        ReflectHelper reflectHelper = new ReflectHelper(fileobj);
        String extend = ConvertUtils.getString(reflectHelper.getMethodValue("extend"));
        if ("dwg".equals(extend)) {
            String realpath = ConvertUtils.getString(reflectHelper.getMethodValue("path"));
            request.setAttribute("realpath", realpath);
            return new ModelAndView("common/upload/dwgView");
        } else if (FileUtils.isPicture(extend)) {
            String realpath = ConvertUtils.getString(reflectHelper.getMethodValue("path"));
            request.setAttribute("realpath",realpath);
            request.setAttribute("fileid", fileKey);
            request.setAttribute("subclassname", subclassname);
            request.setAttribute("contentfield", contentfield);
            return new ModelAndView("common/upload/imageView");
        } else {
            String swfpath = ConvertUtils.getString(reflectHelper.getMethodValue("swfpath"));
            request.setAttribute("swfpath", swfpath);
            return new ModelAndView("common/upload/swfView");
        }

    }


    /**
     * 下载附件     *
     * @RETURN
     */
    @RequestMapping(params = "downloadFile")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileid = ConvertUtils.getString(request.getParameter("fileKey"));
        String subclassname = ConvertUtils.getString(request.getParameter("subclassname"), "UploadFile");
        Class fileClass = ClassLoaderUtils.getClassByScn(subclassname);// 附件的实际类
        Object fileobj = systemService.findEntity(fileClass, fileid);
        ReflectHelper reflectHelper = new ReflectHelper(fileobj);
        FileUploadDto uploadFile = new FileUploadDto(request, response);
        String contentfield = ConvertUtils.getString(request.getParameter("contentfield"), uploadFile.getByteField());
        byte[] content = (byte[]) reflectHelper.getMethodValue(contentfield);
        String path = ConvertUtils.getString(reflectHelper.getMethodValue("path"));
        String extend = ConvertUtils.getString(reflectHelper.getMethodValue("extend"));
        String attachmenttitle = ConvertUtils.getString(reflectHelper.getMethodValue("title"));
        uploadFile.setExtend(extend);
        uploadFile.setTitleField(attachmenttitle);
        uploadFile.setRealPath(path);
        uploadFile.setContent(content);
        //uploadFile.setView(true);
        resourceService.viewOrDownloadFile(uploadFile);
    }



    /**
     * 继承于TSUploadFile附件公共列表数据
     */
    @RequestMapping(params = "objfileGrid")
    public void objfileGrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String businessKey = ConvertUtils.getString(request.getParameter("businessKey"));
        String subclassname = ConvertUtils.getString(request.getParameter("subclassname"));// 子类类名
        String type = ConvertUtils.getString(request.getParameter("typename"));
        String code = ConvertUtils.getString(request.getParameter("typecode"));
        String filekey = ConvertUtils.getString(request.getParameter("filekey"));
        CriteriaQuery cq = new CriteriaQuery(ClassLoaderUtils.getClassByScn(subclassname), dataGrid);
        cq.eq("businessKey", businessKey);
        if (StringUtils.isNotEmpty(type)) {
            cq.createAlias("TBInfotype", "TBInfotype");
            cq.eq("TBInfotype.typeName", type);
        }
        if (StringUtils.isNotEmpty(filekey)) {
            cq.eq("id", filekey);
        }
        if (StringUtils.isNotEmpty(code)) {
            cq.createAlias("TBInfotype", "TBInfotype");
            cq.eq("TBInfotype.typeCode", code);
        }
        cq.add();
        this.systemService.findDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

}
