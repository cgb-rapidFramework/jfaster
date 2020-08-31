package com.abocode.jfaster.core.persistence.hibernate.qbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.abocode.jfaster.core.common.util.ContextHolderUtils;
import com.abocode.jfaster.core.common.util.ConvertUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;


/**
 * 拼装Hibernate条件
 */
public class DetachedCriteriaUtil {


	
	/**
	 * @param pojoClazz
	 *            实体类名
	 * @param startChar
	 *            参数统一的开始字符
	 * @param alias
	 *            别名
	 * @param columnNames
	 *            作为select子句的属性名集合
	 * @return DetachedCriteria 组装好的查询条件
	 */
	public static DetachedCriteria createDetachedCriteria(Class<?> pojoClazz,
			String startChar,String alias,String[] columnNames) {
		return createDetachedCriteria(pojoClazz, startChar, alias, columnNames, null);
	}
	
	/**
	 * @param pojoClazz
	 *            实体类名
	 * @param startChar
	 *            参数统一的开始字符
	 * @param columnNames
	 *            作为select子句的属性名集合
	 * @param excludeParameters
	 *            不作为查询条件的参数
	 * @param alias
	 *            别名
	 * @return DetachedCriteria 组装好的查询条件
	 */
	public static DetachedCriteria createDetachedCriteria(Class<?> pojoClazz,
			String startChar,String alias,String[] columnNames,String[] excludeParameters) {
		DetachedCriteria criteria = DetachedCriteria.forClass(pojoClazz,alias);
		if(columnNames!=null && columnNames.length>0){
			selectColumn(criteria, columnNames, pojoClazz, false);
		}
		return criteria;
	}
	
	private static final String ALIAS_KEY_IN_REQUEST = "ALIAS_KEY_IN_REQUEST";
	private static final String HAS_JOIN_TABLE_KEY_IN_REQUEST = "HAS_JOIN_TABLE_KEY_IN_REQUEST";
	
	private static void setAliasToRequest(HttpServletRequest request,Set<String> aliases) {
		request.setAttribute(ALIAS_KEY_IN_REQUEST, aliases);
	}
	
	private static Set<String> getAliasesFromRequest(){
		Set<String> aliases = (Set<String>) ContextHolderUtils.getRequest().getAttribute(ALIAS_KEY_IN_REQUEST);
		if(aliases==null){
			aliases = new HashSet(5);
			setAliasToRequest(ContextHolderUtils.getRequest(), aliases);
		}
		return aliases;
	}
	
	private static boolean getHasJoinTableFromRequest(){
		Boolean hasJoin = (Boolean) ContextHolderUtils.getRequest().getAttribute(HAS_JOIN_TABLE_KEY_IN_REQUEST);
		return hasJoin==null?false:hasJoin;
	}
	


	
	/**
	 * @author
	 * @param columnNames
	 *            字符串数组，以数据的形式接收要查询的字段属性，如String[] column={"属性1","属性2","属性3"};
	 * @param pojoClass
	 *            实体类的Class,如Mobile.class;
	 *            为要查询的POJO对象指定一个别名
	 * @return DetachedCriteria 的一个对象，如果需要查询条件，在些对象后追加查询条件。
	 * 
	 * @param forJoinTable 是否多表连接查询
	 */
	public static void selectColumn(DetachedCriteria criteria, String[] columnNames,
			Class<?> pojoClass,boolean forJoinTable) {
		if (null == columnNames) {
			return;
		}
	
		//使用这个临时变量集合，是因为dinstinct关键字要放在最前面，而distinct关键字要在下面才决定放不放，
		List<Projection> tempProjectionList = new ArrayList<Projection>();
		
		Set<String> aliases = getAliasesFromRequest();
		boolean hasJoniTable = false;
		String rootAlias = criteria.getAlias();
		for (String property : columnNames) {
			if(property.contains("_")){
				String[] propertyChain = property.split("_");
				createAlias(criteria,rootAlias,aliases,propertyChain,0);
				tempProjectionList.add(Projections.property(ConvertUtils.replaceUnderLine(property)).as(ConvertUtils.replaceUnderLine(property)));
				hasJoniTable = true;
			}else{
				tempProjectionList.add(Projections.property(rootAlias + POINT + property).as(property));
			}
		}

		ProjectionList projectionList = Projections.projectionList();
		//这个一定要放在tempProjectionList的前面，因为distinct要在最前面
		if(hasJoniTable || forJoinTable ||  getHasJoinTableFromRequest()){
			projectionList.add(Projections.distinct(Projections.id()));
		}
		
		for (Projection proj : tempProjectionList) {
			projectionList.add(proj);
		}
		
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(pojoClass));

		/*if(!hasJoniTable){
			criteria.setResultTransformer(Transformers.aliasToBean(pojoClass));
		}else{//下面这个是自定义的结果转换器
			criteria.setResultTransformer(new AliasToBean(pojoClass));
		}*/
	}
	
	private static final String POINT = ".";
	
	/**
	 * 创建别名
	 * @author 苍鹰
	 * 2009-9-9
	 * @param criteria
	 * @param rootAlais
	 * @param aliases
	 * @param columns
	 * @param currentStep
	 */
	private static void createAlias(DetachedCriteria criteria, String rootAlais, Set<String> aliases,String[] columns,int currentStep){
		if(currentStep<columns.length-1){
			if(!aliases.contains(convertArrayToAlias(columns, currentStep))){
				if(currentStep>0){
					criteria.createAlias(convertArrayToAlias(columns, currentStep-1) + POINT +columns[currentStep], convertArrayToAlias(columns, currentStep)).setFetchMode(columns[currentStep], FetchMode.JOIN);
				}else{
					criteria.createAlias(rootAlais + POINT +columns[currentStep], convertArrayToAlias(columns, currentStep)).setFetchMode(columns[currentStep], FetchMode.JOIN);
				}
				aliases.add(convertArrayToAlias(columns, currentStep));
			}
			currentStep++;
			createAlias(criteria, rootAlais, aliases, columns, currentStep);
		}
	}
	

	
	/**
	 * 从数组中创建ALIAS
	 * @author 苍鹰
	 * 2009-9-10
	 * @param columns
	 * @param currentStep
	 * @return
	 */
	private static String convertArrayToAlias(String[] columns, int currentStep){
		StringBuilder alias = new StringBuilder();
		for (int i = 0; i <= currentStep; i++) {
			if(alias.length()>0){
				alias.append("_");
			}
			alias.append(columns[i]);
		}
		return alias.toString();
	}
	
	
	
	
}
