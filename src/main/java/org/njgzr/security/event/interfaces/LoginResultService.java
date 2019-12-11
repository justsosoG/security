package org.njgzr.security.event.interfaces;
/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 9, 2019 , 3:26:25 PM
*/
public interface LoginResultService {
	
	/**
	 * Login successful callback interface
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:35:22 PM
	 * @param userId
	 * @param loginName
	 * @param terminal
	 * @param addr
	 * @param ip
	 */
	void loginSuccess(Long userId,String loginName,String terminal,String addr,String ip);
	
	/**
	 * Login fail callback interface
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:35:50 PM
	 * @param userId
	 * @param loginName
	 * @param e
	 */
	void loginFail(Long userId,String loginName,Exception e);
	
}
