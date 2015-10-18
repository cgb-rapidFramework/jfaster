package org.jeecgframework.web.cgform.service.impl.autolist;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.common.CommUtils;
import org.jeecgframework.web.cgform.entity.config.CgFormFieldEntity;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.service.autolist.CgTableServiceI;
import org.jeecgframework.web.cgform.service.build.DataBaseService;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.util.QueryParamUtil;
import org.jeecgframework.web.utils.JeecgDataAutorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @Title:CgTableServiceImpl
 * @description:动态表数据服务实现
 * @author 赵俊夫
 * @date Jul 5, 2013 9:34:51 PM
 * @version V1.0
 */
@Service("cgTableService")
@Transactional
public class CgTableServiceImpl extends CommonServiceImpl implements CgTableServiceI {
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private DataBaseService dataBaseService;
	
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;

	@SuppressWarnings("unchecked")
	
	public List<Map<String, Object>> querySingle(String table, String field, Map params,
			int page, int rows) {
		StringBuilder sqlB = new StringBuilder();
		dealQuerySql(table,field,params,sqlB);
		List<Map<String, Object>> result = commonService.queryForListMapByParameter("mysql",sqlB
				.toString(), page, rows);
		return result;
	}
	
	public List<Map<String, Object>> querySingle(String table, String field, Map params,
			String sort, String order, int page, int rows) {
		StringBuilder sqlB = new StringBuilder();
		dealQuerySql(table,field,params,sqlB);
		if(!StringUtil.isEmpty(sort)&& !StringUtil.isEmpty(order)){
			sqlB.append(" ORDER BY "+sort+" "+ order);
		}
		List<Map<String, Object>> result = commonService.queryForListMapByParameter("mysql",sqlB
				.toString(), page, rows);
		return result;
	}

	@SuppressWarnings("unchecked")
	
	public boolean delete(String table, Object id) {
		try{
			CgFormHeadEntity head = cgFormFieldService.getCgFormHeadByTableName(table);
			Map<String,Object> data  = dataBaseService.findOneForJdbc(table, id.toString());
			if(data!=null){
				//打印测试
			    Iterator it=data.entrySet().iterator();
			    while(it.hasNext()){
			    	Map.Entry entry=(Map.Entry)it.next();
			        Object ok=entry.getKey();
			        Object ov=entry.getValue()==null?"":entry.getValue();
			        org.jeecgframework.core.util.LogUtil.info("name:"+ok.toString()+";value:"+ov.toString());
			    }
				data = CommUtils.mapConvert(data);
				dataBaseService.executeSqlExtend(head.getId(), "delete", data);
			}
			//step.1 删除表
			StringBuilder deleteSql = new StringBuilder();
			deleteSql.append("DELETE FROM "+table+" WHERE id = ?");
			if(!QueryParamUtil.sql_inj(id.toString())){
				commonService.executeSql(deleteSql.toString(), id);
			}
			//step.2 判断是否有明细表,进行连带删除
			String[] subTables = head.getSubTableStr()==null?new String[0]:head.getSubTableStr().split(",");
			for(String subTable:subTables){
				Map<String, CgFormFieldEntity>  fields = cgFormFieldService.getAllCgFormFieldByTableName(subTable);
				String subFkField = null;
				Iterator it = fields.keySet().iterator();
				for(;it.hasNext();){
					String fieldName  = (String) it.next();
					CgFormFieldEntity fieldc = fields.get(fieldName);
					if(StringUtil.isNotEmpty(fieldc.getMainTable())){
						if(table.equalsIgnoreCase(fieldc.getMainTable())){
							subFkField = fieldName;
						}
					}
				}
				if(StringUtil.isNotEmpty(subFkField)){
					String dsql = "delete from "+subTable+" "+"where "+subFkField+" = ? ";
					this.executeSql(dsql,id);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	private void dealQuerySql(String table, String field, Map params,StringBuilder sqlB){
		sqlB.append(" SELECT ");
		for (String f : field.split(",")) {
			sqlB.append(f);
			sqlB.append(",");
		}
		sqlB.deleteCharAt(sqlB.length() - 1);
		sqlB.append(" FROM " + table);
		if (params.size() >= 1) {
			sqlB.append(" WHERE 1=1 ");
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());
				String value = String.valueOf(params.get(key));
				if (!StringUtil.isEmpty(value) && !"null".equals(value)) {
						sqlB.append(" AND ");
						sqlB.append(" " + key +  value );
				}
			}
		}
		//update-begin--Author:JueYue  Date:20140831 for：onlinecoding 数据权限
		Object dataRuleSql = JeecgDataAutorUtils.loadDataSearchConditonSQLString();//ContextHolderUtils.getRequest().getAttribute(Globals.MENU_DATA_AUTHOR_RULE_SQL);
		if(dataRuleSql != null && !dataRuleSql.equals("")){
			if(params.size() == 0) {
				sqlB.append(" WHERE 1=1 ");
			}
			sqlB.append(dataRuleSql);
		}
		//update-end--Author:JueYue  Date:20140831 for：onlinecoding 数据权限
	}


	@SuppressWarnings("unchecked")
	
	public Long getQuerySingleSize(String table, String field, Map params) {
		StringBuilder sqlB = new StringBuilder();
		dealQuerySql(table,"count(*) as query_size,",params,sqlB);
		List<Map<String, Object>> result = commonService.queryForListMap(sqlB.toString());
		if(result.size()>=1){
			return Long.parseLong(String.valueOf(result.get(0).get("query_size")));
		}else{
			return 0L;
		}
	}
	
	public boolean deleteBatch(String table, String[] ids) {
		try{
			for(String id:ids){
				delete(table, id);
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return true;
	}


	
}
