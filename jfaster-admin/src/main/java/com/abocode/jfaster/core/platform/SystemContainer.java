package com.abocode.jfaster.core.platform;

import com.abocode.jfaster.core.platform.view.IconView;
import com.abocode.jfaster.core.platform.view.OperationView;
import com.abocode.jfaster.core.platform.view.TypeGroupView;
import com.abocode.jfaster.core.platform.view.TypeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemContainer {
    /**
     * 图标
     *
     * @author guanxf
     */
    public final static class IconContainer {
        private static Map<String, IconView> iconsMap = new HashMap<String, IconView>();

        public static Map<String, IconView> getIconsMap() {
            return iconsMap;
        }

        public static void setIconsMap(Map<String, IconView> iconsMap) {
            IconContainer.iconsMap = iconsMap;
        }
    }


    /***
     * 字典
     */
    public final static class TypeGroupContainer {
        private static Map<String, TypeGroupView> typeGroupMap = new HashMap<String, TypeGroupView>();
        private static Map<String, List<TypeView>> typeMap = new HashMap<String, List<TypeView>>();

        public static Map<String, TypeGroupView> getTypeGroupMap() {
            return typeGroupMap;
        }

        public static void setTypeGroupMap(Map<String, TypeGroupView> typeGroupMap) {
            TypeGroupContainer.typeGroupMap = typeGroupMap;
        }

        public static Map<String, List<TypeView>> getTypeMap() {
            return typeMap;
        }

        public static void setTypeMap(Map<String, List<TypeView>> typeMap) {
            TypeGroupContainer.typeMap= typeMap;
        }
    }

    /**
     * 操作
     *
     * @author guanxf
     */
    public final static class OperationContainer {
        private static Map<String, OperationView> operationMap = new HashMap<String, OperationView>();

        public static Map<String, OperationView> getOperationMap() {
            return operationMap;
        }

        public static void setOperationMap(Map<String, OperationView> operationMap) {
            OperationContainer.operationMap = operationMap;
        }
    }

    /**
     * 模版
     *
     * @author guanxf
     */
    public final static class TemplateContainer {
        private static Map<String, String> template = new HashMap<String, String>();

        public static String getTemplate() {
            return template.get("SYSTEM-TEMPLATE");
        }

        public static void putTemplate(String content) {
            template.put("SYSTEM-TEMPLATE", content);
        }
    }

}
