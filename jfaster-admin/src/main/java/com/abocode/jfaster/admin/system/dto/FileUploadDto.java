package com.abocode.jfaster.admin.system.dto;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 上传下载模型类
 */
@Data
public class FileUploadDto {
	private String byteField = "content";// 二进制文件内容保存到数据库的对应实体类字段
	private String titleField = "name";// 文件名(标题)保存到数据库的对应实体类字段
	private String basePath = "resource/upload";// 文件保存目录根路径
	private String realPath = "path";// 文件保存在硬盘的全路径对应实体字段
	private String extend = "extend";// 扩展名
	private boolean view = false;// 是否是预览
	private boolean rename  =true;// 是否重命名
	private String swfPath;// 转换SWF
	private String folderPath;// 文件物理路径自定义子目录
	private byte[] content;// 预览或下载时传入的文件二进制内容
	private Object object;// 文件对应实体对象
	private String fileKey;// 上传文件ID
	private MultipartHttpServletRequest multipartRequest;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public FileUploadDto(HttpServletRequest request, Object object) {
		String fileKey = ConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		if (!StringUtils.isEmpty(fileKey)) {
			this.fileKey = fileKey;
			this.request = request;
		} else {
			this.multipartRequest = (MultipartHttpServletRequest) request;
		}
		this.object = object;
	}

	public FileUploadDto(HttpServletRequest request) {
		this.multipartRequest = (MultipartHttpServletRequest) request;

	}

	public FileUploadDto(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	public String get(String name) {
		return getMultipartRequest().getParameter(name);

	}
}
