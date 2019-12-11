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

import javax.servlet.http.HttpServletRequest;

import org.njgzr.security.base.AuthorizedUser;
import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName     LoginSuccess
 * @Description   TODO(这里用一句话描述这个类的作用)  
 * @author        rocky
 * @date          2017年6月19日 上午11:28:40 
 *
 */
@Getter @Setter @ToString
public class LoginSuccessEvent extends ApplicationEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7897287570536260847L;
	
	private String terminal;
	private HttpServletRequest http;
	private String jwtToken;
	
	public LoginSuccessEvent(AuthorizedUser user,HttpServletRequest http,String jwtToken) {
		super(user);
		this.http = http;
		this.jwtToken = jwtToken;
	}

	public AuthorizedUser getUser(){
		return (AuthorizedUser)getSource();
	}
	
	public HttpServletRequest getHttp() {
		return http;
	}
	
	public String getToken() {
		return jwtToken;
	}
}
