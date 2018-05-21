package com.abocode.jfaster.web.system.security;

import com.abocode.jfaster.web.system.domain.entity.Role;
import com.abocode.jfaster.web.system.domain.entity.RoleFunction;
import com.abocode.jfaster.web.system.domain.repository.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Franky Guan on 2017/5/10.
 */
@Service
public class SecurityMetadataSource implements
        FilterInvocationSecurityMetadataSource {
    private static final Logger logger = LoggerFactory
            .getLogger(SecurityMetadataSource.class);
    @Autowired
    private SystemService systemService;
    // 所有的资源和权限的映射就存在这里
    private HashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();
    private Collection<ConfigAttribute> allAttribute = new HashSet<ConfigAttribute>();

    /**
     * 初始化所有的资源,这个会在容器运行的时候的构造方法里调用
     */
    private void initResources() {
        logger.debug("init SecurityMetadataSource load all resources");
        // 读取所有的资源,和资源相关联的的权限
        // 读取所有权限点
        Collection<Role> roles = this.systemService.findAll(Role.class);
        logger.debug("start to convert AUthortiyEntity to SercurityConfig");
        for (Role role : roles) {
            String authString = role.getRoleCode();
            logger.debug("add authroity named:[" + authString + "]");
            SecurityConfig attrConfig = new SecurityConfig(authString);
            allAttribute.add(attrConfig);

        }

        // 读取所有角色 所有的菜单
        List<RoleFunction> roleFunctionList = systemService.findAll(RoleFunction.class);
        for (RoleFunction roleFunction:roleFunctionList){
            String resourceContent = roleFunction.getTSFunction().getFunctionUrl();
            // 把url资源转化为一个spring的工具类,请求匹配器类
            logger.debug("add new requestmatcher with [" + resourceContent+ "]");
            RequestMatcher matcher = new AntPathRequestMatcher(resourceContent);
            // 循环权限 定义一个权限的集合,和此资源对应起来,添加到HashMap里
            List<RoleFunction> rolesFunction= systemService.findAllByProperty(RoleFunction.class, "TSFunction.id", roleFunction.getTSFunction().getId());
            Collection<ConfigAttribute> array = new ArrayList<ConfigAttribute>(rolesFunction.size());
            for (RoleFunction role:rolesFunction){
                SecurityConfig securityConfig = new SecurityConfig(role.getTSRole().getRoleCode());
                array.add(securityConfig);
            }
            requestMap.put(matcher, array);
        }


    }

    /**
     * 根据资源获取需要的权限名称
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
            throws IllegalArgumentException {
        logger.debug("get resource " + object + " authority");
        // 把对象转化为请求
        final HttpServletRequest request = ((FilterInvocation) object)
                .getRequest();
        // 循环整个Map 看看有没有可以匹配的,如果有匹配的就立刻返回
        Collection<ConfigAttribute> attrHashMap = new HashSet<ConfigAttribute>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                logger.debug("request matches :" + request.getRequestURL());
                attrHashMap.addAll(entry.getValue());
            }
        }
        if (attrHashMap.size() > 0) {
            // 如果有匹配的就转成ArrayList,然后返回list
            Collection<ConfigAttribute> attr = new ArrayList<ConfigAttribute>(
                    attrHashMap);
            return attr;
        }
        logger.debug("request no matches");
        return Collections.emptyList();
    }

    /**
     * 获取所有权限点
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return this.allAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
