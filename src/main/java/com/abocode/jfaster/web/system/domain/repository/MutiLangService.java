package com.abocode.jfaster.web.system.domain.repository;

import com.abocode.jfaster.core.repository.service.CommonService;

public interface MutiLangService extends CommonService {

	public void initAllMutiLang();
	
	public String getLang(String lang_key);
	
	public String getLang(String lang_key, String args);
	
	public void refleshMutiLangCach();

}
