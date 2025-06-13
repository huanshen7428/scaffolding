package com.huanshen.scaffolding.security.config;

import com.huanshen.scaffolding.security.realms.ShiroRealm;
import com.huanshen.scaffolding.security.filter.AuthorizationFilter;
import com.huanshen.scaffolding.security.filter.SystemLogoutFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2022/4/24.
 *
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({TokenConfigProperties.class, ShiroFilterProperties.class})
public class ShiroAutoConfiguration {

    @Bean
    @ConditionalOnProperty("token.secretKey")
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        //关闭shiro自带session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    @ConditionalOnProperty("token.secretKey")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager, TokenConfigProperties tokenConfigProperties) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        // 添加jwt过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new AuthorizationFilter(tokenConfigProperties));
        filterMap.put("logout", new SystemLogoutFilter());
        shiroFilter.setFilters(filterMap);

        //动态配置拦截器注入
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        List<Map<String, String>> perms = this.getShiroFilterProperties().getPerms();
        if (perms != null) {
            perms.forEach(perm -> filterRuleMap.put(perm.get("key"), perm.get("value")));
        }
        //swagger接口权限 开放
        //doc不开放 由配置文件开放
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        filterRuleMap.put("/v2/**", "anon");
        filterRuleMap.put("/v3/**", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");
        filterRuleMap.put("/service-node/event-request", "anon");
        filterRuleMap.put("/api/v1/gen/user/**", "anon");
        filterRuleMap.put("/**", "jwt");

        shiroFilter.setFilterChainDefinitionMap(filterRuleMap);

        return shiroFilter;
    }


    @Bean
    public ShiroFilterProperties getShiroFilterProperties() {
        return new ShiroFilterProperties();
    }

    /**
     * 开启aop注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    @ConditionalOnProperty("token.secretKey")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
