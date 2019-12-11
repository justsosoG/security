/** 
 * FileName:     AuthorizedUser.java 
 * @Description: TODO(用一句话描述该文件做什么) 
 * All rights Reserved, Designed By Jincloud.com  
 * Copyright:    Copyright(C) 2016-2021  
 * Company       Jincloud LTD.  
 * @author:      rocky 
 * @version      V1.0  
 * Createdate:   2017-04-18  
 * Modification  History: 
 * Date         Author        Version        Discription    
 * 2017年4月18日       rocky       1.0          Why & What is modified: <修改原因描述>  
 */
package org.njgzr.security.base;

import java.util.Date;
import java.util.Set;

/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 9, 2019 , 3:00:04 PM
 * Description 经过安全接口认证的用户接口
 */
public interface AuthorizedUser {
	
	/**
	 * 唯一标识
	 * @return
	 */
	Long getId();
	/**
	 * 所属组织机构/部门编号
	 * @return
	 */
	Long getOrganizationId();
	
	/**
	 * 登录名
	 * @return
	 */
	String getLoginName();
	
	String getPwd();
	
	/**
	 * 名称
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * 联系方式  
	 * @return
	 */
	String getMobile();
	
	/**
	 * 是否被锁定
	 * @return
	 */
	boolean isLocked();
	/**
	 * 是否已（过期）失效
	 * @return
	 */
	boolean isDisable();
	/**
	 * 账号过期时间
	 * @return
	 */
	Date getExpireTime();
	
	/**
	 * 角色
	 * @return
	 */
	Set<String> getStringRoles();
	
	/**
	 * 角色名称
	 * @return
	 */
	Set<String> getStringRoleNames();
	
	/**
	 * 权限
	 * @return
	 */
	Set<String> getStringPermissions();
	
}

