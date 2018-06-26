package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.domain.repository.CommonRepository;

public interface MutiLangService extends CommonRepository {

	public void initAllMutiLang();
	
	public String getLang(String lang_key);
	
	public String getLang(String lang_key, String args);
	
	public void refleshMutiLangCach();

}
