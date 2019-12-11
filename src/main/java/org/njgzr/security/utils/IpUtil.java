package org.njgzr.security.utils;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 *@author Justsoso丶G
 *@description （一句话描述作用）
 *@version 2018年10月16日下午4:27:08
 */
@Slf4j
public class IpUtil {
	
	/**
	 * IpUtils工具类方法
	 * 获取真实的ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
	    if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
	         //多次反向代理后会有多个ip值，第一个ip才是真实ip
	    	int index = ip.indexOf(",");
	        if(index != -1){
	            return ip.substring(0,index);
	        }else{
	            return ip;
	        }
	    }
	    ip = request.getHeader("X-Real-IP");
	    if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
	       return ip;
	    }
	    return request.getRemoteAddr();
	}
	
	public static Map<String,String> getCityInfo(HttpServletRequest request) {
		Map<String,String> map = Maps.newHashMap();
		String ip = getIpAddr(request);
		map.put("ip", ip);
		log.info("IP是："+ip);
		try {
			//根据ip进行位置信息搜索
	        DbConfig config = new DbConfig();

	        //获取ip库的位置（放在src下）（直接通过测试类获取文件Ip2RegionTest为测试类）
	        
	        String dbPath = IpUtil.class.getResource("/ip2region.db").getPath();
	        
            File file = new File(dbPath);
            if (file.exists() == false) {

                String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
                dbPath = tmpDir + "ip.db";
                file = new File(dbPath);
                FileUtils.copyInputStreamToFile(IpUtil.class.getClassLoader().getResourceAsStream("classpath:ip2region.db"), file);

            }

	        DbSearcher searcher = new DbSearcher(config, dbPath);

	        //采用Btree搜索
	        DataBlock block = searcher.btreeSearch(ip);
	        //打印位置信息（格式：国家|大区|省份|城市|运营商）
	        String addr = block.getRegion().replace("|", ";");
	        String[] as = addr.split(";");
	        String ad = as[2]+as[3];
	        map.put("city", ad.replace("0", "").trim());
	        return map;
		} catch (Exception e) {
			log.error("根据IP获取城市异常："+e);
		}
		return map;
	}
	
}
