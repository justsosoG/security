package org.njgzr.security.event.interfaces;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 10, 2019 , 4:18:28 PM
*/
public interface ConfigGetService {
	
	/**
	 * Maximum number of people logged in at the same time with the same account
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:32:11 PM
	 * @return
	 */
	Long maxSessionCount();
	
	/**
	 * Interface address that does not need to be verified
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:33:04 PM
	 * @return
	 */
	String anons();
	
	/**
	 * redis Connect config
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:33:54 PM
	 * @return
	 */
	StringRedisTemplate getStringRedisTemplate();
	
	/**
	 * System identification
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:34:34 PM
	 * @return
	 */
	String getAppId();
	
}
