package com.abocode.jfaster.core.persistence.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author  张代浩
 *
 */
@Repository("jdbcDao")
public class JdbcDao extends SimpleJdbcTemplate{

	/**
	 * 数据库类型
	 */
	public static final String DATABSE_TYPE_MYSQL ="mysql";
	public static final String DATABSE_TYPE_POSTGRE ="postgresql";
	public static final String DATABSE_TYPE_ORACLE ="oracle";
	public static final String DATABSE_TYPE_SQLSERVER ="sqlserver";
	/**
	 * 分页SQL
	 */
	public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
	public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";//postgresql
	public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; //oracle
	public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver


	@Autowired
	public JdbcDao(DataSource dataSource) {
		super(dataSource);
	}



	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.jdbcTemplate.queryForList(sql,objs);
	}



	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 *
	 * @param dbType
	 * @param sql
	 * @param objs
	 * @return
	 * @throws DataAccessException
	 */
	public  List<Map<String, Object>>  findForJdbcParam(String dbType,String  sql,  int page, int rows,Object... objs){
		//封装分页SQL
		sql = jeecgCreatePageSql(dbType,sql,page,rows);
		return jdbcTemplate.queryForList(sql,objs);
	}

	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		try{
			return this.jdbcTemplate.queryForMap(sql, objs);
		}catch (EmptyResultDataAccessException e) {
		    return null;
		}
	}

	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC
	 */
	public Long getCountForJdbc(String  sql) {
		return  jdbcTemplate.queryForObject(sql,Long.class);
	}
	/**
	 * 使用指定的检索标准检索数据并分页返回数据For JDBC-采用预处理方式
	 *
	 */
	public Long getCountForJdbcParam(String  sql,Object... objs) {
		return  jdbcTemplate.queryForObject(sql,Long.class, objs);
	}

	public Integer executeSql2(String sql,List<Object> param) {
		return this.jdbcTemplate.update(sql,param);
	}

	public Integer executeSql(String sql, Object... param) {
		return this.jdbcTemplate.update(sql,param);
	}

	public Integer countByJdbc(String sql, Object... param) {
		return this.jdbcTemplate.queryForObject(sql,Integer.class, param);
	}

	/**
	 * 按照数据库类型，封装SQL
	 */

	private static int getAfterSelectInsertPoint(String sql) {
	    int selectIndex = sql.toLowerCase().indexOf("select");
	    int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
	    return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }

	public static String jeecgCreatePageSql(String dbType,String sql, int page, int rows){
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum+"";
		sqlParam[2] = rows+"";
		if(dbType.indexOf(DATABSE_TYPE_MYSQL)!=-1){
			sql = MessageFormat.format(MYSQL_SQL, sqlParam);
		}else if(dbType.indexOf(DATABSE_TYPE_POSTGRE)!=-1){
			sql = MessageFormat.format(POSTGRE_SQL, sqlParam);
		}else {
			int beginIndex = (page-1)*rows;
			int endIndex = beginIndex+rows;
			sqlParam[2] = Integer.toString(beginIndex);
			sqlParam[1] = Integer.toString(endIndex);
			if(dbType.indexOf(DATABSE_TYPE_ORACLE)!=-1) {
				sql = MessageFormat.format(ORACLE_SQL, sqlParam);
			} else if(dbType.indexOf(DATABSE_TYPE_SQLSERVER)!=-1) {
				sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
				sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
			}
		}
		return sql;
	}
}
