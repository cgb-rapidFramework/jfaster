package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;

public interface MutiLangRepository extends CommonRepository {

	public void initAllMutiLang();
	
	public String getLang(String lang_key);
	
	public String getLang(String lang_key, String args);
	
	public void refleshMutiLangCach();

}
