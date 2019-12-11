package org.njgzr.security.base;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:聽聽聽聽聽Password
 * @Description:   TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜绫荤殑浣滅敤)聽 
 * @author         rocky
 * @date:聽聽        2017骞�4鏈�18鏃ヂ犱笅鍗�3:57:25聽
 *
 */
@Getter @Setter 
public class Password {
	/**
	 * 鐢ㄦ埛瀵嗙爜鍔犲瘑绛栫暐SHA-1
	 */
	public static final String HASH_ALGORITHM_SHA1 = "SHA-1";
	
	/**
	 * 鐢ㄦ埛瀵嗙爜鍔犲瘑绛栫暐MD5
	 */
	public static final String HASH_ALGORITHM_MD5 = "MD5";

	/**
	 * 鐢ㄦ埛瀵嗙爜鍔犲瘑鐩�(salt)闀垮害
	 */
	public static final int SALT_SIZE = 8;
	
	public static final int NO_SALT_MD5_LEN = 32;
	
	public static final int NO_SALT_SHA1_LEN = 40;

	public static final int SHA1_WITH_SALT_LEN = SALT_SIZE*2+NO_SALT_SHA1_LEN;
	
	/**
	 * 鐢ㄦ埛瀵嗙爜鍔犲瘑杩愮畻娆℃暟
	 */
	public static final int HASH_INTERATIONS = 1024;
	
	private String value;

	private String salt;
	
	private String type;
	
	public Password() {
	}
	
	public Password(String plainValue){
		if(plainValue==null) plainValue="";
		this.type = HASH_ALGORITHM_SHA1;
		byte[] salt = this.salt();
		this.salt = Encodes.encodeHex(salt);
		this.value = Encodes.encodeHex(Digests.sha1(plainValue.getBytes(), salt, HASH_INTERATIONS));	
	}
	
	public static Password sha1Password(String plainValue){
		return new Password(plainValue);
	}

	/**   
	 * @return      
	 */ 
	protected  byte[] salt() {
		return Digests.generateSalt(SALT_SIZE);
	}
	public static Password md5Password(String plainValue){
		if(plainValue==null) plainValue="";
		Password result = new Password();
		result.type = HASH_ALGORITHM_MD5;
		try {
			result.value = Encodes.encodeHex(Digests.md5(IOUtils.toInputStream(plainValue,"utf-8")));
		} catch (IOException e) {
			// 
		}	
		return result;
	}
	
	/**聽
	 * <p>Title聽toString</p>聽聽
	 * <p>Description聽</p>聽聽
	 * @return聽
	 * @see java.lang.Object#toString()聽聽
	 */
	@Override
	public String toString() {
		return StringUtils.trimToEmpty(this.salt)+this.value;
	}
	
	public static Password parse(String val){
		if(StringUtils.isBlank(val))
			return null;
		if(val.length()==NO_SALT_MD5_LEN){//MD5
			Password result = new Password();
			result.setType(HASH_ALGORITHM_MD5);
			result.setValue(val);
			return result;
		}else if(val.length()==SHA1_WITH_SALT_LEN){//SHA1
			Password result = new Password();
			int saltLen = Password.SALT_SIZE*2;
			result.setType(HASH_ALGORITHM_SHA1);
			result.setSalt(StringUtils.substring(val, 0, saltLen));
			result.setValue(StringUtils.substring(val, saltLen));
			return result;
		}
		return null;
	}
	
	public boolean match(String plainValue) {
		if(type==null)
			return false;
		if(type.equals(HASH_ALGORITHM_MD5)){
			try{
				return StringUtils.equals(value, Encodes.encodeHex(Digests.md5(IOUtils.toInputStream(plainValue,"utf-8"))));
			}catch (IOException e) {
				return false;
			}
		}
		if(type.equals(HASH_ALGORITHM_SHA1)){
			return StringUtils.equals(value,Encodes.encodeHex(Digests.sha1(plainValue.getBytes(), Encodes.decodeHex(this.salt), HASH_INTERATIONS)));		
		}
		return false;
	}
	
	
	
	public static void main(String[] args) {
		Password p = new Password("123456");
		System.out.println(p.toString());
		System.out.println(p.getSalt());
		System.out.println(Encodes.decodeHex(p.getSalt()));
	}

	
}
