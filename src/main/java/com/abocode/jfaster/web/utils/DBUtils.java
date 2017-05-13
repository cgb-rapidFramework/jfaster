package com.abocode.jfaster.web.utils;

/**
 * Created by Franky on 2016/3/15.
 */
public class DBUtils {
	/*private static Logger log = Logger.getLogger(DBUtils.class);
	*//**
	 * 获取数据库类型
	 * @return
	 *//*
	public static String getDBType(){
		String retStr="";
		ApplicationContext ctx = OnlineListener.getCtx();
		if (ctx==null) {
			 return retStr;//如果ctx为空，则服务器异常了
		}else{
			org.springframework.orm.hibernate4.LocalSessionFactoryBean sf = (org.springframework.orm.hibernate4.LocalSessionFactoryBean)ctx.getBean("&sessionFactory");
			String dbdialect = sf.getHibernateProperties().getProperty("hibernate.dialect");
			log.debug(dbdialect);
			if (dbdialect.equals("org.hibernate.dialect.MySQLDialect")) {
				retStr="mysql";
			}else if (dbdialect.contains("Oracle")) {//oracle有多个版本的方言
				retStr = "oracle";
			}else if (dbdialect.equals("org.hibernate.dialect.SQLServerDialect")) {
				retStr = "sqlserver";
			}else if (dbdialect.equals("org.hibernate.dialect.PostgreSQLDialect")) {
				retStr = "postgres";
			}
			return retStr;
		}
	}

	private static final Logger logger = Logger.getLogger(DBUtils.class);

	private static BasicDataSource getDataSource(final DynamicDataSourceEntity dynamicSourceEntity) {
		BasicDataSource dataSource = new BasicDataSource();

		String driverClassName = dynamicSourceEntity.getDriverClass();
		String url = dynamicSourceEntity.getUrl();
		String dbUser = dynamicSourceEntity.getDbUser();
		String dbPassword = dynamicSourceEntity.getDbPassword();

		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}

	private static JdbcTemplate getJdbcTemplate(String dbKey) {
		DynamicDataSourceEntity dynamicSourceEntity = DynamicDataSourceEntity.DynamicDataSourceMap.get(dbKey);

		BasicDataSource dataSource = getDataSource(dynamicSourceEntity);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	*//**
	 * 该方法只是方便用于main方法测试调用
	 * @param dynamicSourceEntity
	 * @return JdbcTemplate
	 *//*
	private static JdbcTemplate getJdbcTemplate(DynamicDataSourceEntity dynamicSourceEntity) {
		BasicDataSource dataSource = getDataSource(dynamicSourceEntity);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	*//**
	 * Executes the SQL statement in this <code>PreparedStatement</code> object,
	 * which must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
	 * <code>DELETE</code>; or an SQL statement that returns nothing,
	 * such as a DDL statement.
	 *//*
	public static int update(final String dbKey, String sql, Object... param)
	{
		int effectCount = 0;
		JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

		if (org.springframework.util.StringUtils.isEmpty(param)) {
			effectCount = jdbcTemplate.update(sql);
		} else {
			effectCount = jdbcTemplate.update(sql, param);
		}

		return effectCount;
	}

	public static Object findOne(final String dbKey, String sql, Object... param) {
		List<Map<String, Object>> list;
		JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

		if (org.springframework.util.StringUtils.isEmpty(param)) {
			list = jdbcTemplate.queryForList(sql);
		} else {
			list = jdbcTemplate.queryForList(sql, param);
		}

		if(StringUtils.isEmpty(list))
		{
			logger.error("Except one, but not find actually");
		}

		if(list.size() > 1)
		{
			logger.error("Except one, but more than one actually");
		}

		return list.get(0);
	}

	public static List<Map<String, Object>> findList(final String dbKey, String sql, Object... param) {
		List<Map<String, Object>> list;
		JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

		if (org.springframework.util.StringUtils.isEmpty(param)) {
			list = jdbcTemplate.queryForList(sql);
		} else {
			list = jdbcTemplate.queryForList(sql, param);
		}
		return list;
	}

	public static void main(String[] args) {
		DynamicDataSourceEntity dynamicSourceEntity = new DynamicDataSourceEntity();

		String dbKey = "SAP_DB";
		String driverClassName = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@10.10.0.59:1521:mid";
		String dbUser = "CRM";
		String dbPassword = "CRM2013";

		dynamicSourceEntity.setDbKey(dbKey);
		dynamicSourceEntity.setDriverClass(driverClassName);
		dynamicSourceEntity.setUrl(url);
		dynamicSourceEntity.setDbUser(dbUser);
		dynamicSourceEntity.setDbPassword(dbPassword);

		JdbcTemplate jdbcTemplate = getJdbcTemplate(dynamicSourceEntity);

		String sql = "select ak.VKBUR, ak.KUNNR, ak.BSTNK, ak.VBELN, ak.MAHDT, ak.BSTDK from VBAK ak where ak.VKORG = '6002'";
		//List<Map<String, Object>> list = DynamicDBUtil.getList(jdbcTemplate, sql);
		//System.out.println(list.size());
	}*/
}
