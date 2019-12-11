package org.njgzr.security.base.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.njgzr.security.JwtService;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.Contance;
import org.njgzr.security.base.Result;
import org.njgzr.security.base.token.JWTToken;
import org.njgzr.security.cache.LoginCache;
import org.njgzr.security.utils.HttpResponseHelper;
import org.njgzr.security.utils.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

	/**
     * 判断用户是否想要登入�?
     * �?测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
    	String authorization = getAuthzHeader(request);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
    	 AuthenticationToken token = createToken(request, response);
         if (token == null) {
         	return onLoginFailure(token, new ExpiredCredentialsException("身份验证过期，请重新登录"), request, response);
         }
         
         String tokenString = ((JWTToken)token).getVal();
         String sessionId = Md5Crypt.md5Crypt(tokenString.getBytes());
         if(!loginCache.existSession(sessionId)) {
        	 return onLoginFailure(token, new ExpiredCredentialsException("该账号在其他地方登陆，您已被强制退出。"), request, response);
         }
         
         if(JWTUtil.isTokenExpired(tokenString)) {
         	return onLoginFailure(token, new ExpiredCredentialsException("用户会话超时"), request, response);
         }
         try {
             Subject subject = getSubject(request, response);
             subject.login(token);
             return onLoginSuccess(token, subject, request, response);
         } catch (AuthenticationException e) {
             return onLoginFailure(token, e, request, response);
         }
    }

    /**
     * 这里我们详细说明下为�?么最终返回的都是true，即允许访问
     * 例如我们提供�?个地�? GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东�?
     * �?以我们在这里返回true，Controller中可以�?�过 subject.isAuthenticated() 来判断用户是否登�?
     * 如果有些资源只有登入用户才能访问，我们只�?要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有�?个缺点，就是不能够对GET,POST等请求进行分别过滤鉴�?(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	 return false;
    }

    /**
     * 对跨域提供支�?
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "token,secret");
        // 跨域时会首先发�?�一个option请求，这里我们给option请求直接返回正常状�??
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    
    
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,ServletResponse response) throws Exception {
		if (token instanceof JWTToken) {
			JWTToken jwtToken = (JWTToken) token;
			AuthorizedUser user = (AuthorizedUser) subject.getPrincipal();
			log.info("user request success:"+user.getLoginName());
//			loginCache.saveSession(key, user.getLoginName());
			
			boolean shouldRefresh = jwtService.shouldRefreshToken(jwtToken.getVal());
			if (shouldRefresh) {
				log.info("user token refresh:"+user.getLoginName());
				HttpResponseHelper respHelper = new HttpResponseHelper(response);
//				loginCache.saveSession(keyString, user.getLoginName());
				respHelper.resposeJwtToken(
						jwtService.refreshToken(jwtToken.getVal(),user.getLoginName()));
			}
//			applicationContext.publishEvent(new LoginSuccessEvent(user,key,terminal,WebUtils.toHttp(request)));
		}
		return true;
	}
    
	@Override
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
		String jwtToken = getTokenFromRequest(servletRequest);
		if (StringUtils.isNotBlank(jwtToken) )
			return new JWTToken(jwtToken);
		return null;
	}

	private String getTokenFromRequest(ServletRequest servletRequest) {
		String jwtToken = getAuthzHeader(servletRequest);
		return jwtToken;
	}
	
	protected String getAuthzHeader(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		return httpRequest.getHeader(Contance.HEADER);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		String keyString = JWTUtil.getKey(getTokenFromRequest(request));
		loginCache.removeSession(keyString);
		Result result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
		new HttpResponseHelper(response).responseJson(result.toJson());
		return false;
	}

	public JWTFilter(JwtService jwtService,LoginCache loginCache) {
		super();
		this.jwtService = jwtService;
		this.loginCache = loginCache;
	}
    
	private JwtService jwtService;
	
	private LoginCache loginCache;
	
//	private ApplicationContext applicationContext;
	
	
//	applicationContext.publishEvent(new LoginSuccessEvent(user,key,terminal,addr,ip,danger, agent,terminal));
	
}
