package org.njgzr.security.cache.impl;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.njgzr.security.RedisService;
import org.njgzr.security.base.Contance;
import org.njgzr.security.cache.LoginCache;
import org.njgzr.security.event.interfaces.ConfigGetService;
import org.njgzr.security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 11, 2019 , 10:07:53 AM
*/
@Service
@Slf4j
public class RedisCache implements LoginCache {
	
	@Autowired
    private ConfigGetService configGetService;
	
	@Autowired
	private RedisService redisService;
	
	
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
	
	public void saveSession(String sessionId, String name,int terminal) {
		redisService.set(sessionId, name, getExpireTime(terminal)+Contance.gracePeriod);
	}
	
	@Override
	public void saveAndKitOutSession(String token,String key1, String value,int terminal,String uname) {
		String sessionId = Md5Crypt.md5Crypt(token.getBytes());
		saveSession(sessionId,key1,terminal);
		Long size = listSize(key1);
		if(size>=configGetService.maxSessionCount()) {
			String session = (String) redisService.rightPop(key1);
			removeSession(session.split(":")[1]);
			String key = JWTUtil.getKey(token);
			removeSession(buildSaltKey(uname, key, terminal));
		}
		redisService.leftPush(key1, sessionKey(sessionId));
	}

	private Long listSize(String name) {
		return redisService.getKeySize(name);
	}

	@Override
	public boolean checkSession(String key, String name) {
		String sessionString = redisService.get(key);
		if(StringUtils.isBlank(sessionString) || !sessionString.equals(name)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean existSession(String key) {
		return redisService.exists(key);
	}

	@Override
	public void removeSession(String key) {
		if(redisService.exists(key)) {
			redisService.removeKey(key);
			log.info("session:"+key+"已被移除");
		}
	}
	
	private String sessionKey(String key) {
		return "session:"+key;
	}
	
	private String buildSaltKey(String username, String key, int teminalType) {
		return "jwt:salt:"+configGetService.getAppId()+":"+username+":"+teminalType+":"+key;
	}
	
}
