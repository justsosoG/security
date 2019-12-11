package org.njgzr.security.config;

import java.util.List;

import org.njgzr.security.event.interfaces.ConfigGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.Data;

/**
*@author Mr Gu<admin@njgzr.org>
*@version Dec 9, 2019 , 4:29:02 PM
*/
@Service
@Data
public class AnonListConfig {
	
	@Autowired
	private ConfigGetService configGetService;
	
	public List<String> getAnon(){
		String[] as = configGetService.anons().split(",");
		List<String> res = Lists.newArrayList();
		for(String str: as) {
			res.add(str);
		}
		return res;
	}
	
}
