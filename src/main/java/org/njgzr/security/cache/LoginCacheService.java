package org.njgzr.security.cache;

import javax.annotation.PostConstruct;

import org.njgzr.security.cache.impl.MemoryCache;
import org.njgzr.security.cache.impl.RedisCache;
import org.njgzr.security.event.interfaces.ConfigGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 11, 2019 , 10:16:59 AM
*/
@Service
public class LoginCacheService {
	
	private LoginCache realCache = null;
	
	@PostConstruct
	public void init(){
		if(redisTemplate!=null)
			realCache = new RedisCache();
		else
			realCache = new MemoryCache();
	}
	
	private StringRedisTemplate redisTemplate;
	
	public void getStringRedisTemplate() {
		this.redisTemplate = configGetService.getStringRedisTemplate();
	}
	
	@Autowired
    private ConfigGetService configGetService;
	
	
	public void saveSession(String key, String name) {
//		realCache.saveSession(key, name);
	}
	
	public void saveAndKitOutSession(String sessionId,String name, String key,int terminal,String uname) {
		realCache.saveAndKitOutSession(sessionId,name, key, terminal,uname);
	}
	
	public boolean checkSession(String key, String name) {
		return realCache.checkSession(key, name);
	}
	
	public boolean existSession(String key) {
		return realCache.existSession(key);
	}
	
	public void removeSession(String key) {
		realCache.removeSession(key);
	}

}
