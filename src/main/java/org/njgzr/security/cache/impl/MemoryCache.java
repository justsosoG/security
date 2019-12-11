package org.njgzr.security.cache.impl;

import org.njgzr.security.cache.LoginCache;

/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 11, 2019 , 10:20:03 AM
*/
public class MemoryCache implements LoginCache {

	@Override
	public boolean checkSession(String key, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existSession(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeSession(String key) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void saveAndKitOutSession(String sessionId, String name, String key, int terminal, String uname) {
		// TODO Auto-generated method stub
		
	}

}
