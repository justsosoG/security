/* 
 * Description TODO(用一句话描述该文件做什么) 
 * All rights Reserved, Designed By Jincloud.com Copyright(C) 2017
 * author      rocky 
 * version      V1.0  
 * Createdate:   2017-06-19 
 * Date         Author        Version        Discription    
 * 2017年6月19日       rocky       1.0           <修改原因描述>  
 */
package org.njgzr.security.event;

import org.njgzr.security.base.AuthorizedUser;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 9, 2019 , 3:42:09 PM
 * Description
 */
@Getter @Setter @ToString
public class LoginFailEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1513785696778314381L;
	
	private Exception e;
	
	public LoginFailEvent(AuthorizedUser user, Exception e) {
		super(user);
		this.e = e;
	}

	public AuthorizedUser getUser(){
		return (AuthorizedUser)getSource();
	}
	public Exception getException() {
		return e;
	}
	
	
}
