package cn.temptation.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.temptation.bean.Resource;
import cn.temptation.dao.ResourceDao;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    private ResourceDao resourceDao;

    // 创建ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 设置登录跳转页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        // 拦截器
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 从上向下顺序判断
        filterMap.put("/static/**", "anon");
        filterMap.put("/templates/**", "anon");
        filterMap.put("/biz/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/error/**", "anon");
        filterMap.put("/easyui/**", "anon");
        filterMap.put("/echarts/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/dologin", "anon");

        // 配置退出过滤器
//        filterMap.put("/logout", "logout");

        // 配置授权过滤器
        // 获取所有资源，并配置需要进行授权过滤的资源(这里)
        List<Resource> resources = resourceDao.findAll();
        resources.forEach(resource -> {
            if (!"".equals(resource.getResourceurl())){
                filterMap.put(resource.getResourceurl(), "perms[" + resource.getResourceurl() + "]");
            }
        });

        // 过滤链定义，从上向下顺序执行，一般将/*放在最下边
        filterMap.put("/**", "authc");

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");

        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/401");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    // 创建DefaultWebSecurityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myRealm") MyRealm myRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();

        // 关联Realm
        defaultWebSecurityManager.setRealm(myRealm);

        return defaultWebSecurityManager;
    }

    // 创建Realm
    @Bean(name = "myRealm")
    public MyRealm myRealm() {
        return new MyRealm();
    }

    // 配置ShiroDialect后，可以在页面使用Shiro标签
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }
}