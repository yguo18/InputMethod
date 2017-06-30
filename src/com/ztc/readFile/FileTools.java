package com.ztc.readFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileTools {
	private final static String TGA = FileTools.class.getSimpleName();
	
	public static void readFile(List<String> data, String filePath){
		if(data == null || Tools.isEmptyString(filePath)){
			return;
		}
		
		File file = new File(filePath);
		BufferedReader bufferedReader = null;
		try{
			bufferedReader = new BufferedReader(new FileReader(file)); //wuzhongwen suoyi busheji bianma wenti
			String line = null;
			while((line = bufferedReader.readLine())!= null){
				if(!Tools.isEmptyString(line)){
					data.add(line);
				}
			}
			bufferedReader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch(IOException e){
					e.printStackTrace();
				}		
			}
		}
	}
}
