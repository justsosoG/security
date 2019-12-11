package org.njgzr.security.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;


/**
 *@author Justsoso丶G
 *@description （一句话描述作用）
 *@version 2019年5月15日上午11:59:51
 */
@SuppressWarnings("deprecation")
public class Encodes {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Hex缂栫爜.
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}
	
	/**
	 * Hex瑙ｇ爜.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Base64缂栫爜.
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * Base64缂栫爜, URL瀹夊叏(灏咮ase64涓殑URL闈炴硶瀛楃'+'鍜�'/'杞负'-'鍜�'_', 瑙丷FC3548).
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64瑙ｇ爜.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * Base62缂栫爜銆�
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[(input[i] & 0xFF) % BASE62.length];
		}
		return new String(chars);
	}

	/**
	 * Html 杞爜.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 瑙ｇ爜.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 杞爜.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml11(xml);
	}

	/**
	 * Xml 瑙ｇ爜.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 缂栫爜, Encode榛樿涓篣TF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * URL 瑙ｇ爜, Encode榛樿涓篣TF-8.
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}

