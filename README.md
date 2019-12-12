# 项目介绍
        该项目帮助开发者快速，简单的，使用很少的配置，就可以集成shiro框架，免去了接口认证的代码。
# 快速开始
[![Maven Central](https://camo.githubusercontent.com/e7cacdfa1e3b28c8d69fe23418364c62c354ba48/68747470733a2f2f6d6176656e2d6261646765732e6865726f6b756170702e636f6d2f6d6176656e2d63656e7472616c2f636f6d2e6769746875622e686f7562622f73656e7369746976652f62616467652e737667 "Maven Central")](https://mvnrepository.com/artifact/org.njgzr/security)
## maven 导入
        这里以当下流行的spring boot作为例子
        <dependency>
            <groupId>org.njgzr</groupId>
            <artifactId>security</artifactId>
            <version>${latest.version}</version>
        </dependency>
	[最新版本号点这里](https://mvnrepository.com/artifact/org.njgzr/security)
	<a href="https://mvnrepository.com/artifact/org.njgzr/security">最新版本号点这里</a>
## 使用
   ### 第一步
        实现3个接口
        ConfigGetService（注入配置）
        LoginResultService（登陆结果的回调，用作记登陆日志等）
        SecurityService（校验账号密码正确性）
        
        
	
	
        
        
