package org.njgzr.security.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.njgzr.security.JwtServiceImpl;
import org.njgzr.security.base.filter.AjaxAuthenticationFilter;
import org.njgzr.security.base.filter.JWTFilter;
import org.njgzr.security.base.realm.DbRealm;
import org.njgzr.security.base.realm.JWTRealm;
import org.njgzr.security.cache.LoginCache;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;



/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 11, 2019 , 3:38:47 PM
 * Description
 */
@Configuration
public class ShiroConfig {
	
    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(JWTRealm userRealm,DbRealm dbRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm);
        realms.add(dbRealm);
        defaultWebSecurityManager.setRealms(realms);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
    		JwtServiceImpl jwtService,LoginCache loginCache,ApplicationContext applicationContext,AnonListConfig anonListConfig) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new HashMap<>(16);
        filterMap.put("jwt", new JWTFilter(jwtService,loginCache));
        filterMap.put("auth", new AjaxAuthenticationFilter(jwtService,loginCache,applicationContext));
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/login");
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(16);
        List<String> as = anonListConfig.getAnon();
        for(String str : as) {
        	filterChainDefinitionMap.put(str, "anon");
        }
        filterChainDefinitionMap.put("/login", "noSessionCreation,auth");
        filterChainDefinitionMap.put("/logout", "anon");
        
        filterChainDefinitionMap.put("/**", "noSessionCreation,jwt");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    
    
}
