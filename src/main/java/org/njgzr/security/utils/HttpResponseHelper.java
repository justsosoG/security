/* 
 * Description TODO(用一句话描述该文件做什么) 
 * All rights Reserved, Designed By Jincloud.com Copyright(C) 2017
 * author      rocky 
 * version      V1.0  
 * Createdate:   2017-05-18 
 * Date         Author        Version        Discription    
 * 2017年5月18日       rocky       1.0           <修改原因描述>  
 */
package org.njgzr.security.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.njgzr.security.base.Contance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @ClassName     HttpResponseHelper
 * @Description   TODO(这里用一句话描述这个类的作用)  
 * @author        rocky
 * @date          2017年5月18日 下午4:17:49 
 *
 */
public class HttpResponseHelper {

	private static final Logger logger = LoggerFactory.getLogger(HttpResponseHelper.class);
	
	private HttpServletResponse resp = null;
	
	public HttpResponseHelper(ServletResponse response){
		resp = WebUtils.toHttp(response);
	}
	
	public void resposeJwtToken(String jwtToken) {
		if(jwtToken!=null) {
			resp.setHeader(Contance.HEADER, jwtToken);
			Cookie cookie = new Cookie(Contance.HEADER, jwtToken);
    		resp.addCookie(cookie);
    		resp.setHeader("Pragma","no-cache");
			resp.setHeader("Cache-Control","no-cache");
			resp.setHeader("Access-Control-Expose-Headers", "token");
		}
	}
	public boolean responseJson(String content){
		return responseJson(content,null);
	}
	public boolean responseJson(String content,Integer code){
		if(code!=null)
			try {
				resp.sendError(code);
			} catch (IOException e) {
				// ignore
			}
		resp.setCharacterEncoding("UTF-8");  
		resp.setContentType("application/json; charset=utf-8");  
	    return writeContent(content); 
	}

	private boolean writeContent(String content) {
		try( PrintWriter out = resp.getWriter()) {   
	        out.append(content);  
	        out.flush();
	        return true;
	    } catch (IOException e) {  
	        logger.error("response json error:",e);
	        return false;
	    }
	}

}
