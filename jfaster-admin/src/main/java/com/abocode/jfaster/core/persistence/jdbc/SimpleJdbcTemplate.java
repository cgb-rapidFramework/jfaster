package com.abocode.jfaster.core.persistence.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleJdbcTemplate {
	public static final String SQL_ERROR = "SQL_ERROR";
	protected JdbcTemplate jdbcTemplate;
	public SimpleJdbcTemplate(DataSource dataSource){
		jdbcTemplate=new JdbcTemplate(dataSource);
	}
	
	/**
	 * 根据sql语句，返回对象集合
	 * @param sql
	 *  sql语句(参数用冒号加参数名，例如select * from tb where id=:id)
	 * @param clazz
	 * @param parameters
	 * 参数集合(key为参数名，value为参数值)
	 * @return bean对象集合
	 */
	public List find(final String sql,Class clazz,Map parameters){
		Assert.hasText(sql, SQL_ERROR);
		Assert.notNull(clazz,"集合中对象类型不能为空!");
		if(parameters!=null){
			return jdbcTemplate.queryForList(sql,  parameters);
		}else{
			return  jdbcTemplate.queryForList(sql,clazz);
		}
	}
	
	/**
	 * 根据sql语句，返回对象
	 * @param sql
	 *  语句(参数用冒号加参数名，例如select * from tb where id=:id)
	 * @param clazz
	 *  类型
	 * @param parameters
	 * 参数集合(key为参数名，value为参数值)
	 * @return bean对象
	 */
	public Object findForObject(final String sql,Class clazz,Map parameters){
		try{
			Assert.hasText(sql,SQL_ERROR);
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			if(parameters!=null){

				return jdbcTemplate.queryForList(sql,  parameters);
			}else{
				return  jdbcTemplate.queryForList(sql,clazz);
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据sql语句，返回数值型返回结果
	 * @param sql
	 * 语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
	 * @param parameters
	 * 参数集合(key为参数名，value为参数值)
	 * @return bean对象
	 */
	public long findForLong(final String sql,Map parameters){
		try{
			Assert.hasText(sql,SQL_ERROR);
			if(parameters!=null){
				return jdbcTemplate.queryForObject(sql,Long.class, parameters);
			}else{
				return jdbcTemplate.queryForObject(sql,Long.class);
			}
		}catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 根据sql语句，返回Map对象,对于某些项目来说，没有准备Bean对象，则可以使用Map代替Key为字段名,value为值
	 * @param sql
	 * 语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
	 * @param parameters
	 * 参数集合(key为参数名，value为参数值)
	 * @return bean对象
	 */
	public Map findForMap(final String sql,Map parameters){
		try{
			Assert.hasText(sql,SQL_ERROR);
			if(parameters!=null){
				return jdbcTemplate.queryForMap(sql, parameters);
			}else{
				return jdbcTemplate.queryForMap(sql);
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 根据sql语句，返回Map对象集合
	 * @param sql
	 * 语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
	 * @param parameters
	 * 参数集合(key为参数名，value为参数值)
	 * @return bean对象
	 */
	public List<Map<String,Object>> findForListMap(final String sql,Map parameters){
		Assert.hasText(sql,SQL_ERROR);
		if(parameters!=null){
			return jdbcTemplate.queryForList(sql, parameters);
		}else{
			return jdbcTemplate.queryForList(sql);
		}
	}
	
	/**
	 * 执行insert，update，delete等操作<br>
	 * 例如insert into users (name,login_name,password) values(:name,:loginName,:password)<br>
	 * 参数用冒号,参数为bean的属性名
	 * @param sql
	 * @param bean
	 */
	public int executeForObject(final String sql,Object bean){
		Assert.hasText(sql,SQL_ERROR);
		if(bean!=null){
			return jdbcTemplate.update(sql, paramBeanMapper(bean));
		}else{
			return jdbcTemplate.update(sql);
		}
	}

	/**
	 * 执行insert，update，delete等操作<br>
	 * 例如insert into users (name,login_name,password) values(:name,:login_name,:password)<br>
	 * 参数用冒号,参数为Map的key名
	 * @param sql
	 * @param parameters
	 */
	public int executeForMap(final String sql,Map parameters){
		Assert.hasText(sql,SQL_ERROR);
		if(parameters!=null){
			return jdbcTemplate.update(sql, parameters);
		}else{
			return jdbcTemplate.update(sql);
		}
	}
	
	public long executeForObjectReturnPk(final String sql,Object bean){
		Assert.hasText(sql,SQL_ERROR);
		if(bean!=null){
			return jdbcTemplate.update(sql, paramBeanMapper(bean));
		}else{
			return jdbcTemplate.update(sql);
		}
	}
	
	/*
	 * 批量处理操作
	 * 例如：update t_actor set first_name = :firstName, last_name = :lastName where id = :id
	 * 参数用冒号
	 */
	public int[] batchUpdate(final String sql,List<Object[]> batch ){
        int[] updateCounts = jdbcTemplate.batchUpdate(sql,batch);
        return updateCounts;
	}

	
	protected BeanPropertySqlParameterSource paramBeanMapper(Object object) {
		return new BeanPropertySqlParameterSource(object);
	}
}
