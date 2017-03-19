package com.abocode.jfaster.core.common.hibernate.dialect;

import org.hibernate.dialect.PostgreSQLDialect;

public class MyPostgreSQLDialect extends PostgreSQLDialect {

	
	public boolean useInputStreamToInsertBlob() {
		return true;
	}

}
