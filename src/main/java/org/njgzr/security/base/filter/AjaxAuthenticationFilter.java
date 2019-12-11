/* 
 * Description 支持ajax post json方式的登录拦截器 
 * All rights Reserved, Designed By Jincloud.com Copyright(C) 2018
 * author      rocky 
 * version      V1.0  
 * Createdate:   2018-04-18 
 * Date         Author        Version        Discription    
 * 2018年4月18日       rocky       1.0           <修改原因描述>  
 */
package org.njgzr.security.base.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.njgzr.security.JwtService;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.Contance;
import org.njgzr.security.base.Result;
import org.njgzr.security.cache.LoginCache;
import org.njgzr.security.event.LoginSuccessEvent;
import org.njgzr.security.utils.HttpResponseHelper;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;

import lombok.Getter;

/**
 * @ClassName     AjaxAuthenticationFilter
 * @Description   支持ajax post json方式的登录拦截器
 * @author        rocky
 * @date          2018年4月18日 上午11:27:13 
 *
 */
@Getter
public class AjaxAuthenticationFilter extends AuthenticatingFilter  {
	
	public static final String DEFAULT_USERNAME_PARAM = "username";
    public static final String DEFAULT_PASSWORD_PARAM = "password";
    public static final String DEFAULT_TERMINAL_TYPE_PARAM = "teminal";
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		org.apache.shiro.authc.UsernamePasswordToken token = createToken(request, response);
		return executeLogin(request, response,token);
	}
	
	protected UsernamePasswordToken createToken(ServletRequest request, ServletResponse response) throws Exception {
    	String contentType = request.getContentType();
    	if(contentType.contains("application/x-www-form-urlencoded")){

			String username = WebUtils.getCleanParam(request, DEFAULT_USERNAME_PARAM);
			String password =  WebUtils.getCleanParam(request, DEFAULT_PASSWORD_PARAM);
			String host = getHost(request);
			return new UsernamePasswordToken(username,password,host);
    	}else if(contentType.contains("application/json")){
			String reqContent = IOUtils.toString(request.getInputStream(),"utf-8");
			String username = JSON.parseObject(reqContent).getString(DEFAULT_USERNAME_PARAM);
			String password = JSON.parseObject(reqContent).getString(DEFAULT_PASSWORD_PARAM);
			String host = getHost(request);
			return new UsernamePasswordToken(username,password,host);
    	}
    	throw new RuntimeException("不支持的请求方式，Content-type:"+contentType);
    }

	protected boolean executeLogin(ServletRequest request, ServletResponse response,UsernamePasswordToken token) throws Exception {
		if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
	}
	
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,ServletResponse response) throws Exception {
		AuthorizedUser user = (AuthorizedUser)subject.getPrincipal();
		int terminal = NumberUtils.toInt(WebUtils.toHttp(request).getHeader(Contance.TERMINAL), 1);
		String jwtToken = jwtService.createToken(user.getLoginName(),terminal);
		applicationContext.publishEvent(new LoginSuccessEvent(user, WebUtils.toHttp(request),jwtToken));
		HttpResponseHelper respHelper = new HttpResponseHelper(response);
		respHelper.resposeJwtToken(jwtToken);
		respHelper.responseJson(Result.success(user).toJson());
		return false;
	}
	public AjaxAuthenticationFilter(JwtService jwtService,LoginCache loginCache,ApplicationContext applicationContext) {
		super();
		this.jwtService = jwtService;
		this.loginCache = loginCache;
		this.applicationContext = applicationContext;
	}
    
	private JwtService jwtService;
	
	private LoginCache loginCache;
	
	private ApplicationContext applicationContext;
	
}
