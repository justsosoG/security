package org.njgzr.security.base;
/**
 * @author Justsoso丶G
 * @date 2019年5月26日 下午9:03:38
 * Description:
 */
public class Contance {
	
	/**
	 * 令牌的有效时间
	 */
	public static final long TOKEN_EXPIRE_TIME = 1*60*1000;
	
	/**
	 * 验证码有效时间
	 */
	public static final long SMS_EXPIRE_TIME = 5*60*1000;
	
	/**
	 * 每个手机号的发送验证码限制
	 */
//	public static final long SMS_LIMIT_TIME = 1*60*1000;
	public static final long SMS_LIMIT_TIME = 1;
	
	/**
	 * 注册验证码的key
	 */
	public static final String CODE_REGISTER_KEY = "CODE_REGISTER_";
	
	public static final String MOBILE_KEY = "MOBILE_";
	
	public static final String MOBILE_COUNT_KEY = "MOBILE_COUNT_";
	
	/**
	 * 手机号每天发送验证码的最大次数限制
	 */
	public static final Integer MOBILE_COUNT_LIMIT_KEY = 10;
	
	public static final String IP_KEY = "IP_";
	
	public static final String UTF = "UTF-8";
	
	public static final String CONTENTTYPE = "application/json; charset=utf-8";
	
	/**
	 * 签名头
	 */
	public static final String HEADER = "token";
	
	public static final String TERMINAL = "terminal";
	
	/**
	 * token篡改验证秘钥
	 */
	public static final String KEY = "secret";
	
	/**
	 * token 过期豁免时长 默认60S
	 */
	public static final long gracePeriod = 60*1000L;
	
	/**
	 * 默认30分钟
	 */
	public static final long webExpireTime =      	   30 * 60 * 1000L;
	
	/**
	 * 一个月
	 */
	public static final long appExpireTime = 30 * 24 * 60 * 60 * 1000L;
	
	/**
	 * 一天
	 */
	public static final long pcExpireTime = 	  24 * 60 * 60 * 1000L;
	
	public static final String SESSION_KEY = "idong:session:";
	
}
