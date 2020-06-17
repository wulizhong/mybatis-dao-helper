package com.wlz.mybatis.dao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @ClassName:  FieldNameUtils   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 吴立中 
 * @date:   2019年4月19日 上午10:03:45   
 *     
 *
 */
public class FieldNameUtils {
	/**
	 * 驼峰命名转换成下划线
	 * @param hump
	 * @return
	 */
	public static String humpToUnderline(String hump) {
		return humpToUnderline(hump, false);
	}

	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	/**
	 * 将第一个字母转换为大写
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 驼峰命名转换成下划线
	 * @param hump
	 * @param upperCase 是否转换为大写
	 * @return
	 */
	public static String humpToUnderline(String hump, boolean upperCase) {

		if (hump == null || "".equals(hump)) {
			return "";
		}
		hump = String.valueOf(hump.charAt(0)).toUpperCase().concat(hump.substring(1));
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher = pattern.matcher(hump);
		while (matcher.find()) {
			String word = matcher.group();
			if (upperCase) {
				sb.append(word.toUpperCase());
			} else {
				sb.append(word.toLowerCase());
			}

			sb.append(matcher.end() == hump.length() ? "" : "_");
		}
		return sb.toString();
	}

	/**
	 * 下划线转换成驼峰命名
	 * @param line
	 * @param lowerHump
	 * @return
	 */
	public static String underlineToHump(String line, boolean lowerHump) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(lowerHump && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
					: Character.toUpperCase(word.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}
}
