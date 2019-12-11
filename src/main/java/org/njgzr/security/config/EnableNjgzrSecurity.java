package org.njgzr.security.config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
* 
*@author Mr Gu<admin@njgzr.org>
*@version Dec 9, 2019 , 4:08:07 PM
*/
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Import({LoginConfig.class})
public @interface EnableNjgzrSecurity {
	
	
	
}
