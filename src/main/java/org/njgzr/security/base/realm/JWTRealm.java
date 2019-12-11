package org.njgzr.security.base.realm;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.njgzr.security.JwtService;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.token.JWTToken;
import org.njgzr.security.credential.JWTCredentialsMatcher;
import org.njgzr.security.event.interfaces.SecurityService;
import org.njgzr.security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTRealm extends AuthorizingRealm {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private SecurityService securityService;
	
	/**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    	JWTToken jwtToken = (JWTToken) auth;
        String token = jwtToken.getVal();
        
        AuthorizedUser user = this.securityService.findByPrincipal(JWTUtil.getUsername(token));
        
        if(user == null)
            throw new AuthenticationException("token过期，请重新登录");

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, token, getName());
        return authenticationInfo;
    }
    
    
    @PostConstruct
    private void initCredentialsMatcher() {

        this.setCredentialsMatcher(new JWTCredentialsMatcher(jwtService));
    }
    
}
