package org.njgzr.security.base;

import java.util.Date;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

	private static final int SUCCESS_CODE = 200;
	
	private static final int FAIL_CODE = 501;
	
	private boolean success = false;
	
	private static Date responseTime = new Date();
	
	private static String SUCCESS_DESC = "请求成功了哦，你很棒！";
	
	private static String FAIL_DESC = "请求失败了，没关系，继续加油吧！";
	
	private int code;

	private Object data;
	
	private Date time;
	
	private String desc;
	
	
	public static  Result success(){
		return new Result(true, SUCCESS_CODE, "",responseTime,SUCCESS_DESC);
	}
	
	public static  Result success(Object data,String desc){
		return new Result(true, SUCCESS_CODE, data,responseTime,desc);
	}
	
	public static  Result success(Object data){
		return new Result(true, SUCCESS_CODE, data,responseTime,SUCCESS_DESC);
	}
	
	public static  Result fail(int code,String msg){
		return new Result(false, code, msg,responseTime,FAIL_DESC);
	}
	
	public static  Result fail(String msg){
		return new Result(false, FAIL_CODE, msg,responseTime,FAIL_DESC);
	}
	
	public static  Result fail(int code,Exception e){
		return new Result(false, code, e.getMessage(),responseTime,FAIL_DESC);
	}
	
	public static  Result fail(Object data,int code){
		return new Result(false, code, data,responseTime,FAIL_DESC);
	}
	
	public String toJson(){
		return JSON.toJSONString(this);
	}
}
