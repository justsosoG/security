/** 
 * FileName:     SecurityService.java 
 * @Description: TODO(用一句话描述该文件做什么) 
 * All rights Reserved, Designed By Jincloud.com  
 * Copyright:    Copyright(C) 2016-2021  
 * Company       Jincloud LTD.  
 * @author:      rocky 
 * @version      V1.0  
 * Createdate:   2017-04-18  
 * Modification  History: 
 * Date         Author        Version        Discription    
 * 2017年4月18日       rocky       1.0          Why & What is modified: <修改原因描述>  
 */
package org.njgzr.security.event.interfaces;

import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.Password;

/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 9, 2019 , 3:48:24 PM
 * Description
 */
public interface SecurityService {
	
	/**
	 * 
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:36:10 PM
	 * @param principal
	 * @return
	 */
	AuthorizedUser findByPrincipal(Object principal);
	
	/**
	 * 
	 * @author: Mr Gu <admin@njgzr.org>
	 * @version Dec 11, 2019 , 3:36:23 PM
	 * @param user
	 * @return
	 */
	Password findPassword(AuthorizedUser user);
	
}
