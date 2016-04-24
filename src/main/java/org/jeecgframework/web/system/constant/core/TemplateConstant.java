package org.jeecgframework.web.system.constant.core;

import org.jeecgframework.platform.constant.SysThemesEnum;
import org.jeecgframework.platform.util.StringUtils;
import org.jeecgframework.web.system.manager.ClientManager;

import javax.servlet.http.Cookie;

/**
 * Created by guanxf on 2016/3/20.
 */
public class TemplateConstant {
    public static final int TEMPLATE_STATUS_IS_AVAILABLE=1; //模版的状态可用
    public static final int TEMPLATE_STATUS_IS_UNAVAILABLE=0; //模版的状态可用

    public static void setDefault(SysThemesEnum sysTheme, String code) {
        if(code.equals(SysThemesEnum.ACE_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.ACE_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.ACE_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.ACE_STYLE.getThemes());
        }
        else if(code.equals(SysThemesEnum.DEFAULT_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.DEFAULT_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.DEFAULT_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.DEFAULT_STYLE.getThemes());
        }
        else if(code.equals(SysThemesEnum.SHORTCUT_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.SHORTCUT_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.SHORTCUT_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.SHORTCUT_STYLE.getThemes());
        }
        else if(code.equals(SysThemesEnum.SLIDING_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.SLIDING_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.SLIDING_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.SLIDING_STYLE.getThemes());
        }else if(code.equals(SysThemesEnum.BOOTSTRAP_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.BOOTSTRAP_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.BOOTSTRAP_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.BOOTSTRAP_STYLE.getThemes());
        }else  if(code.equals(SysThemesEnum.BLACK_STYLE.getStyle())){
            sysTheme.setStyle(SysThemesEnum.BLACK_STYLE.getStyle());
            sysTheme.setIndexPath(SysThemesEnum.BLACK_STYLE.getIndexPath());
            sysTheme.setThemes(SysThemesEnum.BLACK_STYLE.getThemes());
        }else{
            sysTheme.setStyle("flat");
            sysTheme.setIndexPath("main/flat_main");
            sysTheme.setThemes("blue");
            sysTheme.setDesc("扁平化风格");
        }
    }


}
