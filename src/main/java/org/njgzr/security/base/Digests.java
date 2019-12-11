package org.njgzr.security.base;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;


/**
 *@author Justsoso丶G
 *@description （一句话描述作用）
 *@version 2019年5月15日上午11:54:34
 */
public class Digests {

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	private static SecureRandom random = new SecureRandom();

	/**
	 * 瀵硅緭鍏ュ瓧绗︿覆杩涜sha1鏁ｅ垪.
	 */
	public static byte[] sha1(byte[] input) {
		return digest(input, SHA1, null, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt) {
		return digest(input, SHA1, salt, 1);
	}

	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	/**
	 * 瀵瑰瓧绗︿覆杩涜鏁ｅ垪, 鏀寔md5涓巗ha1绠楁硶.
	 */
	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 鐢熸垚闅忔満鐨凚yte[]浣滀负salt.
	 * 
	 * @param numBytes byte鏁扮粍鐨勫ぇ灏�
	 */
	public static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 瀵规枃浠惰繘琛宮d5鏁ｅ垪.
	 */
	public static byte[] md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 瀵规枃浠惰繘琛宻ha1鏁ｅ垪.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static byte[] digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

}

