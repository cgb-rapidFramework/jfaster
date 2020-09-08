package com.abocode.jfaster.admin.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上传下载模型类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileImportDto {
    private Object object;// 导出实体对象
    private String fileName;// 文件保存在硬盘的全路径对应实体字段
    private String entityName;//导出实体名
    private String field;
    private Class entityClass;
    private MultipartHttpServletRequest multipartRequest;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public FileImportDto(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
