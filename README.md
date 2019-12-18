# 项目介绍
        该项目帮助开发者快速，简单的，使用很少的配置，就可以集成shiro框架，免去了接口认证的代码。
# 特性
## 1、自定义登录接口地址
## 2、自定义登录提交的参数名称
## 3、自定义免认证接口（不需要登录就可以访问的接口）
## 4、是否开启验证码
## 5、5种验证码格式（除中文不能用，3种可选）png，gif，算术题，字体为系统随机，暂不支持字体的自定义。
## 6、可以选择每次登陆都校验验证码，也可以在该账号连续登录失败多少次后自动开启验证。
## 7、登录成功后采用发放令牌的方式，每次调用被保护的接口时只需在header中携带票据即可。（header中的票据字段支持自定义，默认为token）
## 8、登录事件均有回调（成功、失败、登出）
## 9、同一账号同时登陆的个数可控

# 快速开始
[![Maven Central](https://camo.githubusercontent.com/e7cacdfa1e3b28c8d69fe23418364c62c354ba48/68747470733a2f2f6d6176656e2d6261646765732e6865726f6b756170702e636f6d2f6d6176656e2d63656e7472616c2f636f6d2e6769746875622e686f7562622f73656e7369746976652f62616467652e737667 "Maven Central")](https://mvnrepository.com/artifact/org.njgzr/security)
## maven 导入（目前不要下载github中的源码自行编译，因为当前代码不是最新的）
        这里以当下流行的spring boot作为例子，以下示例代码以0.0.6版本为例
```xml
<dependency>
	<groupId>org.njgzr</groupId>
	<artifactId>security</artifactId>
	<version>${latest.version}</version>
</dependency>
```
[最新版本号点这里](https://mvnrepository.com/artifact/org.njgzr/security)
## 使用
### 第一步
        新建配置类并添加注解，如：
```java
@EnableEasyShiro
@Configuration
public class LoginConfig {
	
}
```
		
### 第二步
        实现4个接口
        ConfigGetService（注入配置）
        LoginResultService（登陆结果的回调，用作记登陆日志等）
        SecurityService（校验账号密码正确性）
        AuthorizedUser（登录成功后可以使用AuthorizedUser user = SecurityUtil.getCurrentUser();获得当前登录的用户信息。）
		
		注意：1、存在数据库的密码加密方式必须为：new Password(要加密的字符串).toString()
			 这里采用的盐的加密方式，相同的密码加密的结果都不一样，所以安全性大家放心。
		例子：
```java
@Service
public class ServiceImpl implements ConfigGetService,LoginResultService,SecurityService{
	
	
	/**
	 * principal是登录名
	 */
	@Override
	public AuthorizedUser findByPrincipal(Object principal) {
		// 开发时，这里应该从数据库读取，参数principal为登录名
		if (principal instanceof String) {
			
			User user = userService.findByLoginName((String) principal);// 本地账号
			if (user == null)
				return null;

			AuthorizedUserInfo info = new AuthorizedUserInfo();
			Long orgid = user.getOrganizationId();
			info.setUploadUrl(user.getUploadUrl());
			info.setDisplayName(user.getDisplayName());
			info.setId(user.getId());
			info.setLoginName(user.getLoginName());
			info.setMobile(user.getMobile());
			info.setType(user.getType());
			info.setOrganizationId(orgid);
			info.setDefaultCauseId(user.getDefaultCauseId());
			info.setAddress(user.getAddress());
			log.info(info.toString());
			return info;
		}
		return null;
	}

	@Override
	public Password findPassword(AuthorizedUser user) {
		// 开发时，这里应该从数据库读取，使用user中的id获取数据库中该用户记录，并返回密码字段，注意这里应返回Password类型
		User user = userService.findById(authorizedUser.getId());
		return user == null ? null : user.getPassword();
	}
	
	/**
	 * 登录成功后的回调
	 */
	@Override
	public void loginSuccess(Long userId, String loginName, String terminal, String addr, String ip,String os,String browser) {
		System.err.println(loginName+"登陆成功");
	}
	
	/**
	 * 设置同一个账号同时登陆的个数
	 */
	@Override
	public Long maxSessionCount() {
		return 2L;
	}
	
	
	/**
	 * 免认证的接口
	 */
	@Override
	public List<String> anons() {
		List<String> dList = Lists.newArrayList();
		return dList;
	}
	
	
	@Override
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 系统标识符
	 */
	@Override
	public String getAppId() {
		return "Idong";
	}
	
	/**
	 * web应用的令牌有效时间，返回null则默认为30分钟
	 */
	@Override
	public Long webExpireTime() {
		return 30 * 60 * 1000L;
	}
	
	/**
	 * 手机应用的令牌有效时间，返回null则默认为1个月
	 */
	@Override
	public Long appExpireTime() {
		return 30 * 60 * 1000L;
	}
	
	/**
	 * pc应用的令牌有效时间，返回null则默认为1天
	 */
	@Override
	public Long pcExpireTime() {
		return 30 * 60 * 1000L;
	}
	
	/**
	 * 登录请求中用户名的字段，返回null则默认为username
	 */
	@Override
	public String loginUserNameParam() {
		return null;
	}
	
	/**
	 * 登录请求中密码的字段，返回null则默认为password
	 */
	@Override
	public String loginPasswordParam() {
		return null;
	}
	
	/**
	 * 请求头中的令牌字段，返回null则默认为 token
	 */
	@Override
	public String headerToken() {
		return "myToken";
	}
	
	/**
	 * 登录接口的地址，返回null则默认是 /login
	 */
	@Override
	public String loginUrl() {
		return null;
	}
	
	/**
	 * 登出回调
	 */
	@Override
	public void logout(Long userId, String loginName) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 登录失败的回调
	 */
	@Override
	public void loginFail(String loginName, Exception e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 登录请求中验证码的字段，返回null则默认为captchaCode
	 */
	@Override
	public String captchaParam() {
		return null;
	}
	
	/**
	 * 最大的错误次数，如果超过该数字或者返回0，系统将校验验证码
	 */
	@Override
	public int maxClfCount() {
		return 2;
	}
	
	/**
	 * 验证码的长度，最长6位，最短4位，如为算术验证码，则默认是两个数字的算术题
	 */
	@Override
	public int captchaLenth() {
		return 4;
	}
	
	/**
	 * 验证码图片的大小，格式为 宽,高 ，逗号为英文状态的逗号，返回null则默认 110,40
	 */
	@Override
	public String captchaSize() {
		return "120,60";
	}
	
	/**
	 * 是否开启验证码
	 */
	@Override
	public boolean enableCaptcha() {
		return true;
	}
	
	/**
	 * 验证码类型，目前可选的为 specCaptcha,gifCaptcha,chineseCaptcha,chineseGifCaptcha,arithmeticCaptcha，
	 * 如果返回null，则默认随机，中文验证码存在问题，暂时不推荐使用
	 * 可选值参考org.njgzr.security.enums.CaptchaType
	 * 若返回随意字符串，则默认specCaptcha
	 */
	@Override
	public String captchaType() {
		return null;
	}
	
}
```
```java
@Setter @ToString @Getter
public class AuthorizedUserInfo implements AuthorizedUser {
	
	private Long id;
	
	private String loginName;
	
	private String displayName;
	
	private Date expireTime;
	
	private String mobile;
	
	private String ip;
	
	private boolean isLocked;

	private boolean isDisable;
	
	private Long organizationId;
	
	private String roles;
	
	private Set<String> Permissions;

	@Override
	public Set<String> getStringRoles() {
		return null;
	}

	@Override
	public Set<String> getStringRoleNames() {
		return null;
	}

	@Override
	public Set<String> getStringPermissions() {
		return null;
	}
	
}
```
### 第三步
到这里，你就可以调用/login接口进行登陆操作，
参数为username和password两个，header中可以添加teminal，来标识你是浏览器、app应用、pc端应用，该字段影响下发token的有效时间
提交方式为post，contentType为application/json或application/x-www-form-urlencoded两种方式。
请求成功后会在boday中返回登陆信息，response headers中返回token值，同时会把token写入cookie中。

### 第四步
调用你的其他接口怎么办？
每次请求时，将登陆返回的token值放在header中即可
token过期了怎么办？
token过期后一分钟内，会得到1分钟的豁免时间，也就是在过期后一分钟内，该token还是可以用的，用来保证，还没有完成的请求能够正常返回，
同时，在这一分钟内的请求返回中，会在response headers中返回新的token，并把token写入cookie中。实际开发过程中，只要发现response headers中有返回token值，就将以前的token覆盖即可。
你也可以写一个定时器，定时访问某个接口，用来保证你的token是最新的。




	
		
