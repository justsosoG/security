package org.njgzr.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Mr Gu[admin@njgzr.org]
 * @version 2019-12-6 4:11:33PM
 */
@Configuration
@ComponentScan(basePackages = {"org.njgzr.security",
		"org.njgzr.security.base.filter",
		"org.njgzr.security.base.realm",
		"org.njgzr.security.base.token",
		"org.njgzr.security.event",
		"org.njgzr.security.event.interfaces",
		"org.njgzr.security.utils"})
public class LoginConfig {
	
	
	
}
