package com.mrkj.ygl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MrksUtils {

	/**
	 * 根据 ans_id 获取答案
	 * @param ans_id
	 * @return
	 */
	public static int statistics (String ...ans_id){		//String...为动态参数,等同于数组
		//String与StringBuffer都可以存储和操作字符串，StringBuffer操作字符串要比String性能好
		StringBuffer sb = new StringBuffer("");
		for (String id : ans_id){
			sb.append(id);						//使用StringBuffer拼接字符串
		}
		char[] cArr = sb.toString().toCharArray();		//把字符串转换为char数组
		int result = 0;			//记录字符串总和	
		for (char c : cArr){
			result += c+0;				//求char数组总和
		}
		return result;			//返回求得的char数组总和
	}

	//转换时间格式为：yyyy-MM-dd hh:mm:sss
	public static String TrasformGetimeToString (Long date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		if (date != null){
			return sdf.format(new Date(date));
		}else{
			return null;
		}
	}
}
