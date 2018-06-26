package com.abocode.jfaster.web.system.application.service;

import com.abocode.jfaster.core.common.util.StringUtils;
import com.abocode.jfaster.core.persistence.jdbc.JdbcDao;
import com.abocode.jfaster.web.system.application.ISystemService;
import com.abocode.jfaster.web.system.application.dto.bean.DuplicateBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemApplicationService  implements ISystemService {
    @Resource
    private JdbcDao jdbcDao;

    @Override
    public Long findCountByTable(DuplicateBean duplicateCheckPage) {
        long num=0;
        if(StringUtils.isNotEmpty(duplicateCheckPage.getRowObid())){
            //[2].编辑页面校验
            String sql = "SELECT count(*) FROM "+duplicateCheckPage.getTableName()
                    +" WHERE "+duplicateCheckPage.getFieldName() +" =? and id != ?";
            num = jdbcDao.getCountForJdbcParam(sql, new Object[]{duplicateCheckPage.getFieldVlaue(),duplicateCheckPage.getRowObid()});
        }else{
            //[1].添加页面校验
            String sql = "SELECT count(*) FROM "+duplicateCheckPage.getTableName()
                    +" WHERE "+duplicateCheckPage.getFieldName() +" =?";
            num = jdbcDao.getCountForJdbcParam(sql, new Object[]{duplicateCheckPage.getFieldVlaue()});
        }
        return  num;
    }
}
