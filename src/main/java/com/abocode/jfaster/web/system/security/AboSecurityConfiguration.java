package com.abocode.jfaster.web.system.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Franky Guan on 2017/4/2.
 */
@Configuration
@EnableWebSecurity
public class AboSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("user")
                .password("password")
                .roles("USER");*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.requestMatchers()
                .antMatchers("/loginController.do*//**","/repairController.do*//**","/userController.do*//**")
                .and()
                .authorizeRequests()
                .antMatchers("*//**").hasRole("USER").and()
                .httpBasic();*/
    }
    }
