package com.abocode.jfaster.system.entity;

import com.abocode.jfaster.core.AbstractIdEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author franky
 * 项目附件父表(其他附件表需继承该表)
 */
@Entity
@Table(name = "t_s_file_upload")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class FileUpload extends AbstractIdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    // 业务类主键
    @Column(name = "session_key",length = 32)
    private String sessionKey;
    // 附件名称
    @Column(length = 100)
    private String name;
    // 附件内容
    @Column(length = 3000)
    @Lob
    private byte[] content;
    // 附件物理路径
    @Column(length = 100)
    private String path;
    // 扩展名
    @Column(length = 32)
    private String extend;
    @Column(name = "create_date",length = 35)
    private Date createDate;


}