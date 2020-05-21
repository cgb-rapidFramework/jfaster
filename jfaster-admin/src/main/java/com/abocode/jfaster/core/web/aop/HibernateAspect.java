package com.abocode.jfaster.core.web.aop;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.common.util.LogUtils;
import com.abocode.jfaster.core.web.utils.SessionUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import com.abocode.jfaster.system.entity.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * Hiberate拦截器：实现创建人，创建时间，创建人名称自动注入;
 *                修改人,修改时间,修改人名自动注入;
 * @author  张代浩
 */
@Component
public class HibernateAspect extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;



public boolean onSave(Object entity, Serializable id, Object[] state,
		String[] propertyNames, Type[] types) {
	User currentUser = SessionUtils.getCurrentUser();
	if(currentUser==null){
		return true;
	}
	try {
		//添加数据
		 for (int index=0;index<propertyNames.length;index++)
		 {
		     /*找到名为"创建时间"的属性*/
		     if (DataBaseConstant.CREATE_DATE.equals(propertyNames[index])
		    		 ||DataBaseConstant.CREATE_TIME.equals(propertyNames[index]))
		     {
		         /*使用拦截器将对象的"创建时间"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		 state[index] = new Date();
		    	 }
		         continue;
		     }
		     /*找到名为"创建人"的属性*/
		     else if (DataBaseConstant.CREATE_BY.equals(propertyNames[index]))
		     {
		         /*使用拦截器将对象的"创建人"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		  state[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
		    	 }
		         continue;
		     }
		     /*找到名为"创建人名称"的属性*/
		     else if (DataBaseConstant.CREATE_NAME.equals(propertyNames[index]))
		     {
		         /*使用拦截器将对象的"创建人名称"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		 state[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_USER_NAME);
		    	 }
		         continue;
		     }
		     /*找到名为"创建人名称"的属性*/
		     else if (DataBaseConstant.SYS_USER_CODE.equals(propertyNames[index]))
		     {
		    	 /*使用拦截器将对象的"创建人名称"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		 state[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
		    	 }
		    	 continue;
		     }
		     /*找到名为"创建人部门"的属性*/
		     else if (DataBaseConstant.SYS_ORG_CODE.equals(propertyNames[index]))
		     {
		         /*使用拦截器将对象的"创建人部门"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		 state[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_ORG_CODE);
		    	 }
		         continue;
		     }
		     /*找到名为"创建人部门"的属性*/
		     else if (DataBaseConstant.SYS_COMPANY_CODE.equals(propertyNames[index]))
		     {
		         /*使用拦截器将对象的"创建人部门"属性赋上值*/
		    	 if(ConvertUtils.isEmpty(state[index])){
		    		 state[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_COMPANY_CODE);
		    	 }
		         continue;
		     }
		 }
	} catch (RuntimeException e) {
		LogUtils.error(e.getMessage());
	}
	 return true;
}


public boolean onFlushDirty(Object entity, Serializable id,
		Object[] currentState, Object[] previousState,
		String[] propertyNames, Type[] types) {
	User currentUser = SessionUtils.getCurrentUser();
	if(currentUser==null){
		return true;
	}
	//添加数据
     for (int index=0;index<propertyNames.length;index++)
     {
         /*找到名为"修改时间"的属性*/
         if (DataBaseConstant.UPDATE_DATE.equals(propertyNames[index])
        		 ||DataBaseConstant.UPDATE_TIME.equals(propertyNames[index]))
         {
             /*使用拦截器将对象的"修改时间"属性赋上值*/
        	 currentState[index] = new Date();
             continue;
         }
         /*找到名为"修改人"的属性*/
         else if (DataBaseConstant.UPDATE_BY.equals(propertyNames[index]))
         {
             /*使用拦截器将对象的"修改人"属性赋上值*/
        	 currentState[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_USER_CODE);
        	 continue;
         }
         /*找到名为"修改人名称"的属性*/
         else if (DataBaseConstant.UPDATE_NAME.equals(propertyNames[index]))
         {
             /*使用拦截器将对象的"修改人名称"属性赋上值*/
        	 currentState[index] = SessionUtils.getUserSystemData(DataBaseConstant.SYS_USER_NAME);
        	 continue;
         }
     }
	 return true;
}
}
