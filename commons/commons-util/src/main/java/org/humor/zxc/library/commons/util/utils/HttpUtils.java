package org.humor.zxc.library.commons.util.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
	/**
	 * 获取输入流
	 */
	public static String getBufferStream(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		
		try {
			InputStream in = request.getInputStream();
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			BufferedReader buffer = new BufferedReader(reader);
			String str;
			
			while ((str = buffer.readLine()) != null) {
				sb.append(str);
			}
			
			buffer.close();
			reader.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
