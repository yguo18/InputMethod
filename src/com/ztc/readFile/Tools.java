package com.ztc.readFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	public static void sleep(long millis){
		try{
			Thread.sleep(millis);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static boolean isEmptyString(String text){
		if(text == null || text.length() == 0){
			return true;
		}
		return false;
	}
	
	public static boolean isSub(String str, String re){
		Pattern pattern = Pattern.compile(re);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find(0)){
			return true;
		}
		return false;
	}
	public static String getSub(String str, String re){
		String name = "";
		Pattern pattern = Pattern.compile(re);
		Matcher match = pattern.matcher(str);
		if(match.find(0)){
			name = match.group();
		}
		return name;
	}
	
	public static boolean hasChinese(String text){
		//\u4e00-\u9fa5是所有汉字的unicode编码范围 
		String reg_charset = "[\u4e00-\u9fa5]*";
		Pattern p = Pattern.compile(reg_charset);
		Matcher matcher = p.matcher(text);
		while(matcher.find()){
			if(matcher.group().length()>0){
				return true;
			}
		}
		return false;
	}
	
	public static List<String> getOptionalPrefixSub(List<String> labels,String sub){
		if(labels == null || labels.size() == 0 || isEmptyString(sub)){
			return null;
		}
		
		List<String> dataList = new ArrayList<String>();
		for(int i = 0; i < labels.size(); i++){
			if(labels.get(i).startsWith(sub)){
				dataList.add(labels.get(i));
			}
		}
		return dataList;
	}
}
