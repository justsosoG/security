package org.njgzr.security.base.realm;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.Encodes;
import org.njgzr.security.base.Password;
import org.njgzr.security.event.interfaces.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 9, 2019 , 3:46:49 PM
*/
@Service
public class DbRealm extends AuthorizingRealm {
	
	/**
	 * 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
	
		AuthorizedUser user = this.securityService.findByPrincipal(authcToken.getPrincipal());
		
		if (user != null) {
			if(user.isLocked())
				throw new LockedAccountException();

			if(user.isDisable())
				throw new DisabledAccountException();			
			
			Password password =  this.securityService.findPassword(user);
			ByteSource salt = password.getSalt()==null?null: ByteSource.Util.bytes(Encodes.decodeHex(password.getSalt()));
			//构建AuthenticationInfo，采用DB中的用户数据中的密码数据，在父类AuthenticatingRealm中的assertCredentialsMatch
			//方法中，这个密码数据将会与authcToken中包含的密码数据进行比对
			return new SimpleAuthenticationInfo(user,password.getValue(),salt, getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		AuthorizedUser user = (AuthorizedUser)principals.getPrimaryPrincipal();
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(user.getStringRoles());
		info.addStringPermissions(user.getStringPermissions());
		return info;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Password.HASH_ALGORITHM_SHA1);
		matcher.setHashIterations(Password.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	@Autowired(required=false)
	private SecurityService securityService;
	
}
