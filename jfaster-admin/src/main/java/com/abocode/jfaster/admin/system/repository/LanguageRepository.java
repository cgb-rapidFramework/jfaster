package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;

public interface LanguageRepository extends CommonRepository {

	 void initLanguage();
	
	 String getLang(String lang_key);
	
	 String getLang(String lang_key, String args);
	
	 void refreshLanguageCache();

}
