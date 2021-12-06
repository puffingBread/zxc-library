package org.humor.zxc.library.commons.util.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {

	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 如果Long变NULL,或者小于0l,则取缺省值;
	 * @param val 原值
	 * @param defaultVal 缺省值
	 * @return
	 */
	public static Long toLong(Long val, Long defaultVal){
		if(null == val || val < 0L){
			return defaultVal;
		}
		return val;
	}
	
	/**
	 * 分割字符串
	 * 
	 * @param line
	 *            原始字符串
	 * @param seperator
	 *            分隔符
	 * @return 分割结果
	 */
	public static String[] split(String line, String seperator) {
		if (line == null || seperator == null || seperator.length() == 0)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		int pos1 = 0;
		int pos2;
		for (;;) {
			pos2 = line.indexOf(seperator, pos1);
			if (pos2 < 0) {
				list.add(line.substring(pos1));
				break;
			}
			list.add(line.substring(pos1, pos2));
			pos1 = pos2 + seperator.length();
		}
		// 去掉末尾的空串，和String.split行为保持一致
		for (int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i) {
			list.remove(i);
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 获取字符串的长度，中文占一个字符,英文数字占半个字符
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static double lengthChinese(String value) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}
		}
		// 进位取整
		return Math.ceil(valueLength);
	}

	public static String escapeJavascriptParam(final String str) {
		if (str == null) {
			return null;
		}
		// 替换时先判断是否存在需要替换的字符,提高性能
		if (str.indexOf('"') == -1 && str.indexOf('\'') == -1) {
			return str;
		}

		int stringLength = str.length();
		// StringBuilder buf = new StringBuilder();
		StringBuilder buf = new StringBuilder((int) (stringLength * 1.1));
		for (int i = 0; i < stringLength; ++i) {
			char c = str.charAt(i);

			switch (c) {
			case '\'':
				buf.append("\\'");
				break;
			case '"':
				buf.append("&quot;");
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public static String urlEncode(String str) {
		if (str == null) {
			return null;
		}
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String urlDecode(String str) {
		if (str == null) {
			return null;
		}
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 转换&#123;这种编码为正常字符<br/>
	 * 有些手机会将中文转换成&#123;这种编码,这个函数主要用来转换成正常字符.
	 * 
	 * @param str
	 * @return String
	 */
	public static String decodeNetUnicode(String str) {
		if (str == null)
			return null;

		String pStr = "&#(\\d+);";
		Pattern p = Pattern.compile(pStr);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String mcStr = m.group(1);
			int charValue = StringUtil.convertInt(mcStr, -1);
			String s = charValue > 0 ? (char) charValue + "" : "";
			m.appendReplacement(sb, Matcher.quoteReplacement(s));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 获取字符型参数，若输入字符串为null，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return 字符串参数
	 */
	public static String convertString(String str, String defaults) {
		if (str == null) {
			return defaults;
		} else {
			return str;
		}
	}

	/**
	 * 获取int参数，若输入字符串为null或不能转为int，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return int参数
	 */
	public static int convertInt(String str, int defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	/**
	 * 获取long型参数，若输入字符串为null或不能转为long，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return long参数
	 */
	public static long convertLong(String str, long defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	/**
	 * 获取double型参数，若输入字符串为null或不能转为double，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return double型参数
	 */
	public static double convertDouble(String str, double defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	/**
	 * 获取short型参数，若输入字符串为null或不能转为short，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return short型参数
	 */
	public static short convertShort(String str, short defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Short.parseShort(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	/**
	 * 获取float型参数，若输入字符串为null或不能转为float，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return float型参数
	 */
	public static float convertFloat(String str, float defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	/**
	 * 获取boolean型参数，若输入字符串为null或不能转为boolean，则返回设定的默认值
	 * 
	 * @param str
	 *            输入字符串
	 * @param defaults
	 *            默认值
	 * @return boolean型参数
	 */
	public static boolean convertBoolean(String str, boolean defaults) {
		if (str == null) {
			return defaults;
		}
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	public static String replaceAll(String str, String pattern, String to) {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		StringBuffer sb = new StringBuffer();
		Matcher m = null;
		for (m = p.matcher(str); m.find();) {
			m.appendReplacement(sb, to);
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 将传进来的字符串的html标签删除
	 * 
	 * @param str
	 * @return
	 */
	public static String stripTags(String str) {
		try {
			// str = str.replaceAll("<\\p{Alnum}+?>", "");
			str = str.replaceAll("<[^>]*>", "");
			return str;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 将传进来的字符串的换行符替成 <br/>
	 * 
	 * @param str
	 * @return
	 */
	public static String nl2br(String str) {
		try {
			return str.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
		} catch (Exception e) {
			return str;
		}
	}

	public static String br2nl(String str) {
		try {
			return replaceAll(str, "<br\\s*[^>]*>", "\n");
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}

	public static boolean containCNWords(String body) {
		if (body == null) {
			return false;
		}
		for (int i = 0; i < body.length(); i++) {
			if (body.charAt(i) > 255) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串查找
	 * 
	 * @param str
	 * @param s1
	 * @return
	 */
	public static final int find(String str, String s1) {
		int count = 0;
		int fromindex = -1;
		while ((fromindex = str.indexOf(s1, fromindex + 1)) > -1) {
			count++;
		}
		return count;
	}

	/**
	 * 得到某字符串的md5哈希
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return EncryptUtils.getMD5(str);
	}

	/**
	 * 转换html特殊字符为html码
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlSpecialChars(String str) {
		try {
			if (str.trim() == null) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
			char ch = ' ';
			for (int i = 0; i < str.length(); i++) {
				ch = str.charAt(i);
				if (ch == '&') {
					sb.append("&amp;");
				} else if (ch == '<') {
					sb.append("&lt;");
				} else if (ch == '>') {
					sb.append("&gt;");
				} else if (ch == '"') {
					sb.append("&quot;");
				} else if (ch == '\'') {
					sb.append("&#039;");
				} else if (ch == '(') {
					sb.append("&#040;");
				} else if (ch == ')') {
					sb.append("&#041;");
				} else {
					sb.append(ch);
				}
			}
			if (sb.toString().replaceAll("&nbsp;", "").replaceAll("　", "")
					.trim().length() == 0) {
				return "";
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 转换特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String changeChar(String str) {
		try {
			if (str.trim() == null) {
				return "";
			}
			str = "_" + str;
			StringBuffer sb = new StringBuffer();
			char ch = ' ';
			for (int i = 0; i < str.length(); i++) {
				ch = str.charAt(i);
				if (ch == '#' && str.charAt(i - 1) == '-'
						&& str.charAt(i + 1) == 'i') {
					sb.append("\\#");
				} else {
					sb.append(ch);
				}
			}
			return sb.toString().substring(1);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 删除字符串中的所有空格和换行
	 * 
	 * @param str
	 * @return
	 */
	public static String stripSpace(String str) {
		try {
			str = str.replaceAll("&nbsp;", "");
			str = str.replaceAll(" ", "");
			str = str.replaceAll("\r", "");
			str = str.replaceAll("\n", "");
			return str;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 在长数字前补零
	 * 
	 * @param num
	 *            数字
	 * @param length
	 *            输出位数
	 */
	public static String addzero(long num, int length) {
		String str = "";
		if (num < Math.pow(10, length - 1)) {
			for (int i = 0; i < (length - (num + "").length()); i++) {
				str += "0";
			}
		}
		str = str + num;
		return str;
	}

	/**
	 * 判断字符串是否一个数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] < 48 || cs[i] > 57) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查关键字
	 * 
	 * @param words
	 * @param str
	 * @return
	 */
	public static boolean haveWord(String[] words, String str) {
		for (int i = 0; i < words.length; i++) {
			if (str.indexOf(words[i]) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * s中的s1替换成s2
	 * 
	 * @param s
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final String replace(String s, String s1, String s2) {
		if (s == null)
			return null;
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0) {
			char ac[] = s.toCharArray();
			char ac1[] = s2.toCharArray();
			int j = s1.length();
			StringBuffer sb = new StringBuffer(ac.length);
			sb.append(ac, 0, i).append(ac1);
			i += j;
			int k;
			for (k = i; (i = s.indexOf(s1, i)) > 0; k = i) {
				sb.append(ac, k, i - k).append(ac1);
				i += j;
			}
			sb.append(ac, k, ac.length - k);
			return sb.toString();
		} else {
			return s;
		}
	}

	/**
	 * 替换第一个出现的词语
	 * 
	 * @param s
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static final String replaceFirst(String s, String s1, String s2) {
		int firstposition = s.indexOf(s1);
		if (firstposition == -1) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(s.substring(0, firstposition));
		sb.append(s2);
		sb.append(s.substring(firstposition + s1.length()));
		return sb.toString();
	}

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNull(String str) {
		if (str != null && str.length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 得到后缀名
	 * 
	 * @param filename
	 * @return
	 */
	public static final String getExt(String filename) {
		int last_point_position = filename.lastIndexOf(".");
		int last_slash_position = filename.lastIndexOf("/");
		if (last_point_position > -1) {
			if ((last_slash_position > -1 && last_point_position > last_slash_position)
					|| last_slash_position == -1) {
				String ext = filename.substring(last_point_position)
						.toLowerCase();
				int q_pos = ext.indexOf("?");
				if (q_pos > 0) {
					return ext.substring(0, q_pos);
				}
				return ext;
			} else {
				return ".tmp";
			}
		}
		return ".tmp";
	}

	/**
	 * URL编码
	 * 
	 * @param str
	 * @return
	 */
	public static String URLEncode(String str) {
		try {
			return URLEncoder.encode(str, "GBK");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * URL解码
	 * 
	 * @param str
	 * @return
	 */
	public static String URLDecode(String str) {
		try {
			return URLDecoder.decode(str, "GBK");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 通用编码
	 * 
	 * @param s
	 * @return
	 */
	public static String Encode(String s) {
		try {
			s = URLEncoder.encode(s, "UTF-8");
			s = StringUtil.replace(s, "%", "_");
			return s;
		} catch (Exception e) {
			return s;
		}
	}

	/**
	 * 通用解码
	 * 
	 * @param s
	 * @return
	 */
	public static String Decode(String s) {
		try {
			s = StringUtil.replace(s, "_", "%");
			s = URLDecoder.decode(s, "UTF-8");
			return s;
		} catch (Exception e) {
			return s;
		}
	}

	/**
	 * 转换字符串成boolean值
	 * 
	 * @param s
	 * @return
	 */
	public static boolean toBoolean(String s) {
		if (s == null || s.length() == 0 || s.equals("false") || s.equals("0")) {
			return false;
		}
		return true;
	}

	/**
	 * 转换数字成boolean值
	 * 
	 * @param i
	 * @return
	 */
	public static boolean toBoolean(int i) {
		if (i <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 截取中文字符串
	 * 
	 * @param str
	 * @param start
	 * @param length
	 * @return
	 */
	public static String subStringGBK(String str, int start, int length) {
		try {
			if (str.getBytes("GBK").length <= length * 2) {
				return str;
			}
			if (length <= 0) {
				return "";
			}
			byte[] bytes = str.getBytes("GBK");
			int check = 1;
			for (int i = 0; i < start * 2; i++) {
				check = check * bytes[i];
				if (check > 1000) {
					check = 1;
				}
				if (check < -1000) {
					check = -1;
				}
			}
			if (check < 0 && bytes[start * 2] < 0) {
				start--;
			}
			byte[] newbytes = new byte[length * 2];
			check = 1;
			for (int i = 0; i < newbytes.length; i++) {
				newbytes[i] = bytes[start + i];
				check = check * bytes[start + i];
				if (check > 1000) {
					check = 1;
				}
				if (check < -1000) {
					check = -1;
				}
			}
			if (check < 0 && newbytes[newbytes.length - 1] < 0) {
				newbytes[newbytes.length - 1] = 32;
			}
			return new String(newbytes, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static final String subStringGBK(String str, int length) {
		try {
			byte[] bs = str.getBytes("GBK");
			if (bs.length < length * 2) {
				return str;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int sign = 0;
			for (int i = 0; i < bs.length && i < length * 2; i++) {
				if (bs[i] < 0) {
					sign++;
				}
				if (i == length * 2 - 1) {
					if (sign % 2 == 0) {
						baos.write(bs[i]);
					}
				} else {
					baos.write(bs[i]);
				}
			}
			return new String(baos.toByteArray(), "GBK");
		} catch (Exception e) {
			return str;
		}
	}

	public static String subString(String str, int len) {
		if (str == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < 'ÿ') {
				counter++;
			} else {
				counter += 2;
			}
			if (counter > len) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static final String substring(String str, int start, int end) {
		if (str.getBytes().length < end) {
			return str;
		}
		return str.substring(start, end);
	}

	/**
	 * 
	 * 得到全文搜索字符串
	 * 
	 */
	public static String getIntString(String str) {
		byte[] bytes = str.getBytes();
		StringBuffer sb = new StringBuffer();
		int iscn = 0;
		for (int i = 0; i < bytes.length; i++) {
			int j = bytes[i];
			if (bytes[i] < 0) {
				j = j * (-1);
				if (j < 10) {
					sb.append('0');
				}
				sb.append(j);
				iscn++;
				if (iscn == 2) {
					sb.append(' ');
					iscn = 0;
				}
			} else {
				sb.append(new String(new byte[] { bytes[i] }));
				if (i <= bytes.length - 2 && bytes[i + 1] < 0) {
					sb.append(' ');
				}
			}
		}
		return sb.toString();
	}

	public static String ruship(String ip) {
		try {
			return ip.substring(0, ip.lastIndexOf(".")) + ".*";
		} catch (Exception e) {
			return "";
		}
	}

	/*
	 * 限制字符串长度，html码除外
	 */
	static Pattern pcut = Pattern
			.compile(">([^<]*)<", Pattern.CASE_INSENSITIVE);

	public static String cutString(String str, int length) {
		try {
			if (str.getBytes("GBK").length < length * 2) {
				return str;
			}
			if (str.indexOf("<") < 0 || str.indexOf(">") < 0) {
				// System.out.println(length);
				return subString(str, length);
			}
			StringBuffer sb = new StringBuffer();
			Matcher m = pcut.matcher(str);
			int dylen = 0;
			while (m.find()) {
				// System.out.println("dylen:"+dylen);
				// System.out.println("length-dylen:"+(length-dylen));
				String s = m.group(1);
				m.appendReplacement(sb, ">" + subString(s, length - dylen)
						+ "<");
				int thelen = s.getBytes("GBK").length;
				if (thelen % 2 == 1) {
					thelen = ((thelen + 1) / 2);
				} else {
					thelen = thelen / 2;
				}
				dylen += thelen;
			}
			m.appendTail(sb);
			str = sb.toString();
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	public static String[] getImgUrls(String body) {
		Pattern PwgetImgs = Pattern.compile(
				"<img.*?src\\=[\"' ]*([^\"' ]*)[\"' ]*.*?>",
				Pattern.CASE_INSENSITIVE);
		String aa = "";
		boolean isfind = false;
		try {
			Matcher m = PwgetImgs.matcher(body);
			while (m.find()) {
				isfind = true;
				aa += replace(m.group(1), "&amp;", "&") + "; ;";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { Matcher m = PwgetAs.matcher(body); while (m.find()) { String
		 * abc = m.group(2); if (abc.toLowerCase().endsWith(".jpg") ||
		 * abc.toLowerCase().endsWith(".gif") ||
		 * abc.toLowerCase().endsWith(".png") ||
		 * abc.toLowerCase().endsWith(".img")) { aa += abc + "; ;"; } } } catch
		 * (Exception e) { e.printStackTrace(); } if (aa.indexOf("; ;") < 0) {
		 * return null; }
		 */
		if (isfind) {
			return aa.split("; ;");
		}
		return null;
	}

	public static int toInt(String str) {
		return Integer.valueOf(str);
	}

	private static Pattern Pcheck = Pattern.compile("<([^>]*)>",
			Pattern.CASE_INSENSITIVE);

	private static Pattern PcheckScript = Pattern.compile(
			"<script(.*?)</script\\s*>", Pattern.CASE_INSENSITIVE);

	public static String w3cRepair(String str) {
		str = str.replaceAll("\r\n", "[.ln.]").replaceAll("\n", "[.ln.]");
		try {
			StringBuffer sb = new StringBuffer();
			Matcher m = PcheckScript.matcher(str);
			while (m.find()) {
				m.appendReplacement(sb,
						"<SCRIPT" + m.group(1).replaceAll("<", "[script_tag]")
								+ "</SCRIPT>");
			}
			m.appendTail(sb);
			str = sb.toString();
		} catch (Exception e) {
		}
		try {
			StringBuffer sb = new StringBuffer();
			Matcher m = Pcheck.matcher(str);
			while (m.find()) {
				String g1 = "";
				String[] g1s = m.group(1).replaceAll("\"", "").split(" ");
				for (int i = 0; i < g1s.length; i++) {
					g1 += " ";
					if (g1s[i].indexOf("=") > 0) {
						g1 += g1s[i].substring(0, g1s[i].indexOf("="));
						g1 += "=";
						g1 += "\"";
						g1 += g1s[i].substring(g1s[i].indexOf("=") + 1,
								g1s[i].length());
						g1 += "\"";
					} else {
						g1 += g1s[i];
					}
				}
				m.appendReplacement(sb, "<" + g1.trim() + ">");
			}
			m.appendTail(sb);
			str = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		str = str.replaceAll("<(br|BR)\\s*>", "<BR />");
		str = str.replaceAll("<(img|IMG)([^>]*)>", "<IMG$2 />");
		str = str.replaceAll("\\[.ln.\\]", "\n");
		str = str.replaceAll("\\[script_tag\\]", "<");
		return str;
	}

	/**
	 * 过滤`字符
	 * 
	 * @param str
	 * @return
	 */
	public static final String rejectSQLinjection(String str) {
		return str.replace('`', '\'').replace(' ', '\'');
	}

	public static String strip63(String str) {
		try {
			byte[] bs = str.getBytes();
			java.nio.ByteBuffer bb = java.nio.ByteBuffer
					.allocate(bs.length * 4);
			for (int ii = 0; ii < bs.length; ii++) {
				if (bs[ii] == 63) {
					bb.putInt(32);
					continue;
				}
				bb.putInt(bs[ii]);
			}
			byte[] bbbs = bb.array();
			for (int ii = 0; ii < bs.length; ii++) {
				bs[ii] = bbbs[ii * 4 + 3];
			}
			return new String(bs);
		} catch (Exception e) {
			return str;
		}
	}

	public static final boolean greatThen(String a, String b) {
		int alen = a.length();
		int blen = b.length();
		int minlen = Math.min(alen, blen);
		char[] achars = a.toCharArray();
		char[] bchars = b.toCharArray();
		for (int i = 0; i < minlen; i++) {
			if (achars[i] > bchars[i]) {
				return true;
			}
		}
		return false;
	}

	public static final boolean lessThen(String a, String b) {
		int alen = a.length();
		int blen = b.length();
		int minlen = Math.min(alen, blen);
		char[] achars = a.toCharArray();
		char[] bchars = b.toCharArray();
		for (int i = 0; i < minlen; i++) {
			if (achars[i] < bchars[i]) {
				return true;
			}
		}
		return false;
	}

	public static String fcDecode(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
    
	/**
	 * 翻转字符串
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
		if (str == null)
			return null;
		char[] cs = str.toCharArray();
		int i = 0, k = cs.length - 1;
		while(i < k){
			char c = cs[i];
			cs[i ++] = cs[k];
			cs[k --] = c;
		}
		return String.valueOf(cs);
	}

	public static String grepA(String str, String s, int lines) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new CharArrayReader(str.toCharArray()));
			String line = "";
			StringBuffer sb = new StringBuffer();
			boolean start = false;
			int i = 0;
			while ((line = reader.readLine()) != null && i < lines) {
				if (start) {
					sb.append(line);
					sb.append("\n");
					i += 1;
				}
				if (line.indexOf(s) > -1) {
					start = true;
				}
			}
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			return "";
		} finally {
			try {
				reader.close();
				reader = null;
			} catch (Exception e2) {
			}
		}
	}

	public static String grep(String str, String s) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new CharArrayReader(str.toCharArray()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(s) > -1)
					return line;
			}
			return "";
		} catch (Exception e) {
			return "";
		} finally {
			try {
				reader.close();
				reader = null;
			} catch (Exception e2) {
			}
		}
	}

	public static String m2g(long l) {
		String str = String.valueOf(l);
		if (str.length() < 9) {
			for (int i = 0; i <= 9 - str.length();) {
				str = "0" + str;

			}
		}

		return str;

	}

	/**
	 * 使HTML的标签失去作用*
	 * 
	 * @param input
	 *            被操作的字符串
	 * @return String
	 */
	public static final String escapeHTMLTag(String input) {
		if (input == null)
			return "";
		input = input.trim().replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("\n", "<br>");
		input = input.replaceAll("'", "&#39;");
		input = input.replaceAll("\"", "&quot;");
		input = input.replaceAll("\\\\", "&#92;");
		return input;
	}

	/**
	 * 还原html标签
	 * 
	 * @param input
	 * @return
	 */
	public static final String unEscapeHTMLTag(String input) {
		if (input == null)
			return "";
		input = input.trim().replaceAll("&amp;", "&");
		input = input.replaceAll("&lt;", "<");
		input = input.replaceAll("&gt;", ">");
		input = input.replaceAll("<br>", "\n");
		input = input.replaceAll("&#39;", "'");
		input = input.replaceAll("&quot;", "\"");
		input = input.replaceAll("&#92;", "\\\\");
		return input;
	}

	/**
	 * 把字符型数字转换成整型.
	 * 
	 * @param str
	 *            字符型数字
	 * @return int 返回整型值。如果不能转换则返回默认值defaultValue.
	 */
	public static int getInt(String str, int defaultValue) {
//		if (str == null)
//			return defaultValue;
//		if (isInt(str)) {
//			return Integer.parseInt(str);
//		} else {
//			return defaultValue;
//		}
		if (str == null || str.length() == 0){
			return defaultValue;
		}
		
		try{
			return Integer.parseInt(str);
		}catch(Exception ex){
			return defaultValue;
		}
	}

	/** 取整数，默认值-1 */
	public static int getInt(String str) {
		return getInt(str, -1);
	}
	
	public static String getNotNullString(String str){
		return str == null?"":str;
	}

	public static long getLong(String str, long defaultValue) {
		if (str == null)
			return defaultValue;
		if (isInt(str)) {
			return Long.valueOf(str);
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * 判断一个字符串是否为数字
	 */
	public static boolean isInt(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("\\d+");
	}

	/**
	 * 字符串截断，这里对中文处理为2个字符,并且省略号算1个中文站位 如：substringCN("niai我是谁",4) 结果为：niai我…
	 * 
	 * @param str
	 *            字符串
	 * @param maxLength
	 *            最大中文长度，也就意味着是双倍的英文字符
	 * @return 截断的字符
	 */
	public static final String substringCN(String str, int maxLength) {
		if (str == null)
			return "";
		if (maxLength < 2) {
			return str;
		}
		if (str != null && str.getBytes().length <= maxLength * 2)
			return str;
		try {
			return mySubString(str, (maxLength - 1) * 2) + "…";
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	// 中英文组合截字
	public static final String mySubString(String str, int length)
			throws UnsupportedEncodingException {
		if (length < 2)
			return str;
		Pattern p = Pattern.compile("^[^\\x00-\\xff]$");
		int i = 0, j = 0;
		for (char c : str.toCharArray()) {
			Matcher m = p.matcher(String.valueOf(c));
			i += m.find() ? 2 : 1;
			++j;
			if (i == length) {
				break;
			}
			if (i > length) {
				--j;
				break;
			}
		}
		return str.substring(0, j);
	}

	/**
	 * 除去字符串头尾部的空白，如果结果字符串是空字符串<code>""</code>，则返回<code>null</code>。
	 * <p/>
	 * 
	 * <pre>
	 * StringUtil.trimToNull(null)          = null
	 * StringUtil.trimToNull("")            = null
	 * StringUtil.trimToNull("     ")       = null
	 * StringUtil.trimToNull("abc")         = "abc"
	 * StringUtil.trimToNull("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str
	 *            要处理的字符串
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimToNull(String str) {
		if (str == null) {
			return null;
		}

		String result = str.trim();

		if (result == null || result.length() == 0) {
			return null;
		}

		return result;
	}

	/**
	 * 在数字前补零
	 *
	 * @param num
	 *            数字
	 * @param length
	 *            输出位数
	 */
	public static String addzero(int num, int length) {
		String str = "";
		if (num < Math.pow(10.0D, length - 1)) {
			for (int i = 0; i < (length - (num + "").length()); i++) {
				str += "0";
			}
		}
		str = str + num;
		return str;
	}

	/**
	 * 把数组合成字符串
	 *
	 * @param str
	 * @param seperator
	 * @return
	 */
	public static String toString(String[] str, String seperator) {
		if (str == null || str.length == 0)
			return "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0, n = str.length; i < n; i++) {
			if (i != 0)
				buf.append(seperator);
			buf.append(str[i]);
		}
		return buf.toString();
	}

	public static String escapeSQLParam(final String param) {
		int stringLength = param.length();
		StringBuilder buf = new StringBuilder((int) (stringLength * 1.1));
		for (int i = 0; i < stringLength; ++i) {
			char c = param.charAt(i);
			switch (c) {
			case 0: /* Must be escaped for 'mysql' */
				buf.append('\\');
				buf.append('0');
				break;
			case '\n': /* Must be escaped for logs */
				buf.append('\\');
				buf.append('n');
				break;
			case '\r':
				buf.append('\\');
				buf.append('r');
				break;
			case '\\':
				buf.append('\\');
				buf.append('\\');
				break;
			case '\'':
				buf.append('\\');
				buf.append('\'');
				break;
			case '"': /* Better safe than sorry */
				buf.append('\\');
				buf.append('"');
				break;
			case '\032': /* This gives problems on Win32 */
				buf.append('\\');
				buf.append('Z');
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}

	public static final String escapeHTMLTags(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	public static final String clearHTMLTags(String str) {
		if (str == null) {
			return null;
		}
		str = str.replaceAll("<.*?>", "");
		return str;
	}

	public static String clearScript(String str) {
		if (str == null) {
			return null;
		}

		String pstr = "onclick=\"javascript:window.open\\(this.src\\);\" ";
		pstr = pstr
				+ "style=\"CURSOR: pointer\" onmousewheel=\"return bbimg\\(this\\)\" ";
		pstr = pstr + "onload=\"javascript:if\\(this.width>screen.width-500\\)";
		pstr = pstr + "this.style.width=screen.width\\-500;\"";
		str = str.replaceAll(pstr, "border=\"0\"");

		pstr = "<script.*?</script.*?>";
		str = replaceScript(str, pstr, "");

		pstr = "<iframe.*?>";
		str = replaceScript(str, pstr, "");
		return str;
	}

	public static String replaceScript(String content, String regex,
			String replacement) {
		int flags = 34;
		Pattern p = Pattern.compile(regex, flags);
		return p.matcher(content).replaceAll(replacement);
	}

	public static String escapePatternBackSlash(String content) {
		if (content == null) {
			return null;
		}
		content = content.replaceAll("\\\\", "\\\\\\\\");
		content = content.replaceAll("\\?", "\\\\?");
		content = content.replaceAll("\\+", "\\\\+");
		content = content.replaceAll("\\*", "\\\\*");
		content = content.replaceAll("\\[", "\\\\[");
		content = content.replaceAll("\\]", "\\\\]");
		content = content.replaceAll("\\{", "\\\\{");
		content = content.replaceAll("\\}", "\\\\}");
		content = content.replaceAll("\\(", "\\\\(");
		content = content.replaceAll("\\)", "\\\\)");
		content = content.replaceAll("\\-", "\\\\-");
		content = content.replaceAll("\\$", "\\\\\\$");
		return content;
	}

	public static String escapePatternCharacter(final String content) {
		if (content == null) {
			return null;
		}
		int stringLength = content.length();
		StringBuilder buf = new StringBuilder((int) (stringLength * 1.1));
		for (int i = 0; i < stringLength; ++i) {
			char c = content.charAt(i);
			if (c == '\\' || c == '?' || c == '+' || c == '*' || c == '['
					|| c == ']' || c == '{' || c == '}' || c == '(' || c == ')'
					|| c == '-' || c == '$' || c == '|') {
				buf.append('\\');
			}
			buf.append(c);
		}
		return buf.toString();
	}

	public static String fixed(int number, int minlen) {
		String result = String.valueOf(number);
		while (result.length() < minlen) {
			result = "0" + result;
		}
		return result;
	}

	public static String formatProperty(String title) {
		if (title == null) {
			return null;
		}
		title = title.replaceAll("\"", "%22").replaceAll("'", "%27");
		return title;
	}

	public static String getHTMLTitle(String title, int num) {
		if (title == null) {
			return "";
		}
		title = subString(title, num);

		if (title == null) {
			return null;
		}
		title = title.replaceAll("<", "&lt;");
		title = title.replaceAll(">", "&gt;");
		return title;
	}

	public String getSimpleString(String string) {
		if (string == null) {
			return "";
		}
		String regex = "[a-zA-Z0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		if (m.find()) {
			return m.group();
		}
		return "";
	}

	public static double parseDouble(String str) {
		double re = 0.0D;
		try {
			re = Double.parseDouble(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	public static int parseInt(double num) {
		int result = 0;
		String str = num + "";
		int index = str.indexOf(".");
		if (index >= 0) {
			result = parseInt(str.substring(0, index));
		} else {
			result = parseInt(str);
		}
		return result;
	}

	public static int parseInt(String str) {
		return parseInt(str, 0);
	}

	public static int parseInt(String str, int custom) {
		return parseInt(str, custom, custom);
	}

	public static int parseInt(String str, int min, int custom) {
		int result = custom;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			result = custom;
		}

		if (result < min) {
			result = min;
		}
		return result;
	}

	public static long parseLong(String str) {
		long re = 0L;
		try {
			re = Long.parseLong(str);
		} catch (Exception localException) {
		}
		return re;
	}

	public static String toIn(String[] strs) {
		try {
			String strout = "";
			for (int i = 0; i < strs.length; i++) {
				if ((strs[i] != null) && (strs[i].length() > 0)) {
					strout = strout + "'" + strs[i] + "',";
				}
			}
			if (strout.length() > 0) {
				strout = strout.substring(0, strout.length() - 1);
			}
			return strout;
		} catch (Exception e) {
		}
		return "";
	}



	public static String[] patchMa(String content, String coptem) {
		StringBuffer contentBatch = new StringBuffer();
		Pattern pa = Pattern.compile(coptem);
		Matcher ma = pa.matcher(content);
		int pos = 0;

		while (ma.find(pos)) {
			contentBatch.append(ma.group(1));
			contentBatch.append("#!#");
			pos = ma.end();
		}
		return contentBatch.toString().split("#!#");
	}
	
	/**
	 * 效验字符串,过滤掉含有的mysql关键字  
	 * @param str
	 * @return
	 */
    public static String sqlstrValidate(String str) {
    	if(null == str || str.trim().length() == 0){
    		return "";
    	}
    	if(str.contains("*")){
    		return "";
    	}
    	String str1 = str.trim().toLowerCase();//统一转为小写 
        String badStr = "'| ' |' | and |null|exec|execute|insert|select|delete|update|count|drop|*|%|chr| mid | master | truncate |" +  
                " char |declare|sitename|net user|xp_cmdshell|;| or |-|+|,| like | and |exec|execute|insert|create| drop |" +  
                "table| from | grant | use |group_concat| column_name |" +  
                "information_schema.columns|table_schema| union | where |select| delete | update | order | by | count | * |" +  
                " chr | mid | master | truncate | char | declare |or| ; |-|--| + |,| like '|//|/|//%|#";//过滤掉的sql关键字，可以手动添加  
        String[] badStrs = badStr.split("\\|");
        int n = 0;
        for (int i = 0; i < badStrs.length; i++) {
            if (str1.indexOf(badStrs[i]) >= 0) {
            	str1 = str1.replaceAll(badStrs[i], "");
            	n = 1;
            }  
        }  
        if(n == 1){
        	return str1;
        }else if(n == 0){
        	return str;	
        }  
        return str;
    }
     
    /**
     * 判断给定的字符串,是否符合指定的正则表达式
     * @param str 待验证字符串
     * @param regExp 正则表达式
     * @return true:符合   false:不符号
     */
    public static boolean checkRegExp(String str, String regExp){
    	//  ^[a-z0-9_-]{3,15}$
    	if(null == str || str.length() == 0){
    		return false;
    	}
    	if(null == regExp || regExp.length() == 0){
    		return false;
    	}
    	Pattern p = Pattern.compile(regExp);
    	Matcher m = p.matcher(str);
    	return m.matches();
    }
    	
    /**
     * 校验邮箱
     * @param email
     * @return
     */
   /* public static boolean isEmail(String email) {   
        String regex = "\\w+\\.\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";   
        return Pattern.matches(regex, email);   
    } */
    
    public static boolean isEmail(String email){     
     // String str = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";		 
        
        Pattern p = Pattern.compile(str);     
           Matcher m = p.matcher(email);     
           return m.matches();     
      } 
    
    public static void main(String[] args){
		System.out.println(isEmail("9.98r_de_r-ry@vip.com"));
		System.out.println(isEmail("26583463@qq.com"));

	}

	/**
	 * 将unicode编码的字符串转成utf-8编码的字符串
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len; ) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx int value = 0;
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed \\uxxxx encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

}
