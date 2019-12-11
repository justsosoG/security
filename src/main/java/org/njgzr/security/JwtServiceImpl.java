package org.njgzr.security;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.njgzr.security.base.Contance;
import org.njgzr.security.event.interfaces.ConfigGetService;
import org.njgzr.security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;



/**
*@author Mr Gu
*@description （一句话描述作用）
*@version 2019年8月7日下午4:20:05
*/
@Service
public class JwtServiceImpl implements JwtService {
	
	long getExpireTime(int terminal) {
		switch (terminal) {
			case 1:
				return Contance.webExpireTime;
			case 2:
				return Contance.appExpireTime;
			case 3:
				return Contance.pcExpireTime;
			default:
				return Contance.webExpireTime;
		}
	}
	
	@Override
	public String createToken(String username,int teminalType) {
		String key = UUID.randomUUID().toString();
		String salt = cachePutIfAbsent(buildSaltKey(username, key, teminalType), UUID.randomUUID().toString());
		String token = JWTUtil.sign(username, salt, key, getExpireTime(teminalType)+Contance.gracePeriod,teminalType);
		return token;
	}
	
	@Override
	public String refreshToken(String token,String username) {
		String key = JWTUtil.getKey(token);
		int terminal = JWTUtil.getTerminal(token);
		String salt = cacheGet(buildSaltKey(username,key ,terminal));
		if(salt==null)
			return null;

		String newToken = JWTUtil.sign(username, salt, key, getExpireTime(terminal)+Contance.gracePeriod,terminal); 
		return cachePutIfAbsent(tokenGraceCacheKey(token),newToken,Contance.gracePeriod,TimeUnit.MILLISECONDS);		
	}
	
	@Override
	public boolean validateToken(String token, String username) {
		String salt = cacheGet(buildSaltKey(username,JWTUtil.getKey(token), JWTUtil.getTerminal(token)));
		return JWTUtil.verify(token, username, salt);
	}

	@Override
	public boolean shouldRefreshToken(String token) {
		Date issueTime = JWTUtil.getIssuedAt(token);
		int terminal = JWTUtil.getTerminal(token);
		return issueTime.getTime()+getExpireTime(terminal)<=System.currentTimeMillis()
				&&issueTime.getTime()+getExpireTime(terminal)+Contance.gracePeriod>System.currentTimeMillis();
	}

	@Override
	public void clearToken(String token) {
		
	}
	
	private String buildSaltKey(String username, String key, int teminalType) {
		return "jwt:salt:"+appId+":"+username+":"+teminalType+":"+key;
	}
	
	private String tokenGraceCacheKey(String token) {
		return "jwt:token:grace:"+token;
	}
	
	private String appId;
	
	@PostConstruct
	public void getAppId() {
		this.appId = configGetService.getAppId();
	}
	
	private StringRedisTemplate redisTemplate;
	
	@PostConstruct
	public void gerRedisService() {
		this.redisTemplate = configGetService.getStringRedisTemplate();
	}
	
	@Autowired
	private ConfigGetService configGetService;

	protected String cachePutIfAbsent(String key, String value) {
		if(redisTemplate.opsForValue().setIfAbsent(key, value))
			return value;
		return redisTemplate.opsForValue().get(key);
	}
	
	protected String cachePutIfAbsent(String key, String value, long timeOut, TimeUnit uint) {
		if(redisTemplate.opsForValue().setIfAbsent(key, value)) {
			redisTemplate.expire(key, timeOut, uint);
			return value;
		}
		return redisTemplate.opsForValue().get(key);
	}
	
	protected String cacheGet(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
}
