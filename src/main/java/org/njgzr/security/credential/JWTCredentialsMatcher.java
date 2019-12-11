package org.njgzr.security.credential;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.njgzr.security.JwtService;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.token.JWTToken;

public class JWTCredentialsMatcher implements CredentialsMatcher {

	private JwtService jwtService;

	public JWTCredentialsMatcher(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
		JWTToken jwtToken = (JWTToken) authenticationToken;
		String token = jwtToken.getVal();
		
		AuthorizedUser user = (AuthorizedUser) authenticationInfo.getPrincipals().getPrimaryPrincipal();

		return jwtService.validateToken(token, user.getLoginName());

	}

}
