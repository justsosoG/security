package org.njgzr.security.cache;
/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 11, 2019 , 10:00:00 AM
*/
public interface LoginCache {
	
	void saveAndKitOutSession(String sessionId,String name, String key,int terminal,String uname);
	
	boolean checkSession(String key, String name);
	
	boolean existSession(String key);
	
	void removeSession(String key);
	
}
