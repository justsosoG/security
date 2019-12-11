package org.njgzr.security;

/**
*@author Mr Gu
*@description （一句话描述作用）
*@version 2019年8月7日下午4:19:31
*/
public interface JwtService {
	
	String createToken(String username,int teminalType);
	
	String refreshToken(String token, String username);
	
	boolean validateToken(String token,String username);
	
	boolean shouldRefreshToken(String token);
	
	void clearToken(String token);
	
}
