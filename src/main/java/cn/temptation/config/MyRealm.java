package cn.temptation.config;

import cn.temptation.bean.User;
import cn.temptation.dao.ResourceDao;
import cn.temptation.dao.RoleDao;
import cn.temptation.dao.UserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ResourceDao resourceDao;

    // 授权处理
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前登录获得认证的用户
        User user = (User) principalCollection.getPrimaryPrincipal();

        if (user != null) {
            // 给资源授权
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            // 根据获得认证的用户编号查询该用户具备的资源URL集合
            List<String> resourceurls = resourceDao.findUrlByUserid(user.getUserid());

            // 遍历集合，组装成满足授权过滤器过滤格式，并添加到资源信息中
            resourceurls.forEach(item -> info.addStringPermission(item));

            return info;
        }

        return null;
    }

    // 认证处理
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 编写Shiro判断逻辑，判断账号和密码
        // 1、判断账号
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        User user = userDao.findByUsername(token.getUsername());
        if (user == null) {
            // 账号错误，Shiro底层会抛出UnknownAccountException异常
            return null;
        }

        // 2、判断密码
        // 认证后做授权处理，需要将获得认证的用户对象赋值给principal，授权处理时会用到
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
    }
}