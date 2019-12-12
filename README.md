# 项目介绍
        该项目帮助开发者快速，简单的，使用很少的配置，就可以集成shiro框架，免去了接口认证的代码。
# 快速开始
[![Maven Central](https://camo.githubusercontent.com/e7cacdfa1e3b28c8d69fe23418364c62c354ba48/68747470733a2f2f6d6176656e2d6261646765732e6865726f6b756170702e636f6d2f6d6176656e2d63656e7472616c2f636f6d2e6769746875622e686f7562622f73656e7369746976652f62616467652e737667 "Maven Central")](https://mvnrepository.com/artifact/org.njgzr/security)
## maven 导入
        这里以当下流行的spring boot作为例子
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
@EnableNjgzrSecurity
@Configuration
public class LoginConfig {
	
}
```
		
### 第二步
        实现4个接口
        ConfigGetService（注入配置）
        LoginResultService（登陆结果的回调，用作记登陆日志等）
        SecurityService（校验账号密码正确性）
        AuthorizedUser（可以是跟你数据库用户表映射的类）
		
		注意：存在数据库的密码加密方式必须为：new Password(要加密的字符串).toString()
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
		AuthorizedUserInfo user = new AuthorizedUserInfo();
		user.setLoginName("1");
		return user;
	}

	@Override
	public Password findPassword(AuthorizedUser user) {
		return new Password("1");
	}
	
	/**
	 * 登录成功后的回调（可以用来记登陆日志，如果不需要，可以不管）
	 */
	@Override
	public void loginSuccess(Long userId, String loginName, String terminal, String addr, String ip) {
		System.err.println(loginName+"登陆成功");
	}
	
	/**
	 * 登录失败的回调（可以用来记登陆日志，如果不需要，可以不管）
	 */
	@Override
	public void loginFail(Long userId, String loginName, Exception e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置同一个账号同时登陆的个数（超过的话，第一个人就会被强制T出去）
	 */
	@Override
	public Long maxSessionCount() {
		return 2L;
	}
	
	
	/**
	 * 免认证的接口（不需要登录就可以调用的接口）
	 */
	@Override
	public List<String> anons() {
		List<String> dList = Lists.newArrayList();
		// 像这样
		dList.add("/findLoginway");
		return dList;
	}
	
	/**
	 * 目前系统必须依赖redis，后期会支持基于内存的，如果不想依赖redis，这里之间返回null，但目前的版本不支持
	 */
	@Override
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 系统标识符（取一个英文名）
	 */
	@Override
	public String getAppId() {
		return "Idong";
	}
	
	/**
	 * 浏览器登录的token过期时间,返回null则默认30分钟
	 */
	@Override
	public Long webExpireTime() {
		return 30 * 60 * 1000L;
	}
	
	/**
	 * app登录的token过期时间,返回null则默认30天
	 */
	@Override
	public Long appExpireTime() {
		return 30 * 24 * 60 * 60 * 1000L;
	}
	
	/**
	 * pc应用登录的token过期时间,返回null则默认1天
	 */
	@Override
	public Long pcExpireTime() {
		return 24 * 60 * 60 * 1000L;
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

# 后期会加的功能
## 目前系统必须依赖redis，后期会支持基于内存的




	
		
