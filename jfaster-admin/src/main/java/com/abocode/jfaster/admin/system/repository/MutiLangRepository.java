package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;

public interface MutiLangRepository extends CommonRepository {

	 void initAllMutiLang();
	
	 String getLang(String lang_key);
	
	 String getLang(String lang_key, String args);
	
	 void refleshMutiLangCach();

}
