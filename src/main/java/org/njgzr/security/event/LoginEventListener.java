package org.njgzr.security.event;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.njgzr.security.base.AuthorizedUser;
import org.njgzr.security.base.Contance;
import org.njgzr.security.cache.LoginCache;
import org.njgzr.security.event.interfaces.ConfigGetService;
import org.njgzr.security.event.interfaces.LoginResultService;
import org.njgzr.security.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author Mr Gu<admin@njgzr.org>
 * @version Dec 9, 2019 , 3:41:51 PM
 * Description
 */
@Service
@Slf4j
public class LoginEventListener {
	
	String getTerminal(int terminal) {
		switch (terminal) {
			case 1:
				return "浏览器";
			case 2:
				return "手机端";
			case 3:
				return "电脑端";
			default:
				return "默认终端";
		}
	}

	@EventListener
	@Transactional
	@Async
	public void loginSuccessed(LoginSuccessEvent event){
		
		AuthorizedUser user = event.getUser();
		HttpServletRequest resuest = event.getHttp();
		Map<String, String> map = IpUtil.getCityInfo(resuest);
    	String addr = map.get("city");
    	String ip = map.get("ip");
    	int terminal = NumberUtils.toInt(resuest.getHeader(Contance.TERMINAL), 1);
    	
    	loginCache.saveAndKitOutSession(event.getToken(),user.getLoginName()+"-"+appId+"-"+terminal, ip+"-"+addr,terminal,user.getLoginName());
    	log.info(user.getLoginName()+"使用"+getTerminal(terminal)+"登录成功");
    	loginResultService.loginSuccess(user.getId(), user.getLoginName(), event.getTerminal(), addr, ip);
		
	}
	
	@EventListener
	@Transactional
	@Async
	public void loginFailed(LoginFailEvent event){
		AuthorizedUser user = event.getUser();
		loginResultService.loginFail(user.getId(), user.getLoginName(), event.getException());
	}	
	
	
	@Autowired
	private LoginCache loginCache;
	
	@Autowired
	private LoginResultService loginResultService;
	
	private String appId;
	
	@PostConstruct
	public void getAppId() {
		this.appId = configGetService.getAppId();
	}
	
	@Autowired
	private ConfigGetService configGetService;
	
}
