package com.abocode.jfaster.admin.system.service.impl;

import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.persistence.jdbc.JdbcDao;
import com.abocode.jfaster.admin.system.service.SystemService;
import com.abocode.jfaster.admin.system.dto.DuplicateBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private JdbcDao jdbcDao;

    @Override
    public Long findCountByTable(DuplicateBean duplicateCheckPage) {
        long num=0;
        if(StringUtils.isNotEmpty(duplicateCheckPage.getRowId())){
            //[2].编辑页面校验
            String sql = "SELECT count(*) FROM "+duplicateCheckPage.getTableName()
                    +" WHERE "+duplicateCheckPage.getFieldName() +" =? and id != ?";
            num = jdbcDao.getCountForJdbcParam(sql, new Object[]{duplicateCheckPage.getFieldValue(),duplicateCheckPage.getRowId()});
        }else{
            //[1].添加页面校验
            String sql = "SELECT count(*) FROM "+duplicateCheckPage.getTableName()
                    +" WHERE "+duplicateCheckPage.getFieldName() +" =?";
            num = jdbcDao.getCountForJdbcParam(sql, new Object[]{duplicateCheckPage.getFieldValue()});
        }
        return  num;
    }
}
