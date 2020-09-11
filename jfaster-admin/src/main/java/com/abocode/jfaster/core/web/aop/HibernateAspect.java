package com.abocode.jfaster.core.web.aop;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.web.manager.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Slf4j
public class HibernateAspect extends EmptyInterceptor {
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state,
                          String[] propertyNames, Type[] types) {
        //添加数据
        for (int index = 0; index < propertyNames.length; index++) {
            /*找到名为"创建时间"的属性*/
            if (DataBaseConstant.CREATE_DATE.equals(propertyNames[index])
                    || DataBaseConstant.CREATE_TIME.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建时间"属性赋上值*/
                state[index] = new Date();
            }
            /*找到名为"创建人"的属性*/
            else if (DataBaseConstant.CREATE_BY.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建人"属性赋上值*/
                state[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
            }
            /*找到名为"创建人名称"的属性*/
            else if (DataBaseConstant.CREATE_NAME.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建人名称"属性赋上值*/
                state[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_USER_NAME);
            }
            /*找到名为"创建人名称"的属性*/
            else if (DataBaseConstant.SYS_USER_CODE.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建人名称"属性赋上值*/
                state[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
            }
            /*找到名为"创建人部门"的属性*/
            else if (DataBaseConstant.SYS_ORG_CODE.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建人部门"属性赋上值*/
                state[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_ORG_CODE);
            }
            /*找到名为"创建人部门"的属性*/
            else if (DataBaseConstant.SYS_COMPANY_CODE.equals(propertyNames[index]) && ConvertUtils.isEmpty(state[index])) {
                /*使用拦截器将对象的"创建人部门"属性赋上值*/
                state[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_COMPANY_CODE);
            }
        }
        return true;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        //添加数据
        for (int index = 0; index < propertyNames.length; index++) {
            /*找到名为"修改时间"的属性*/
            if (DataBaseConstant.UPDATE_DATE.equals(propertyNames[index])
                    || DataBaseConstant.UPDATE_TIME.equals(propertyNames[index])) {
                /*使用拦截器将对象的"修改时间"属性赋上值*/
                currentState[index] = new Date();
            }
            /*找到名为"修改人"的属性*/
            else if (DataBaseConstant.UPDATE_BY.equals(propertyNames[index])) {
                /*使用拦截器将对象的"修改人"属性赋上值*/
                currentState[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
            }
            /*找到名为"修改人名称"的属性*/
            else if (DataBaseConstant.UPDATE_NAME.equals(propertyNames[index])) {
                /*使用拦截器将对象的"修改人名称"属性赋上值*/
                currentState[index] = SessionHolder.getUserSystemData(DataBaseConstant.SYS_USER_NAME);
            }
        }
        return true;
    }
}
