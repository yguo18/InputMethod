package com.ztc.train;

import java.io.File;
import java.util.ArrayList;

import com.ztc.util.Utils;
/**
 * 提取文章中的句子
 * @input /article下的文章
 * @output sentences.txt
 * @author yguo
 *
 */
public class ProcessArticle {
	private static final String ARTICLE_DIR = "./dateFile/input/article";
	private static final String SENTENCES_FILE = "./dateFile/input/sources/sentences.txt";
	
	private static ArrayList<String> extractChineseSentences(String filePath){
		String content = Utils.readFileOneTime(filePath);
		content = content.replace(" ", "");
		content = content.replace("\t", "");
		String str = "";
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> sentencesFinally = new ArrayList<String>();
		for(int i = 0; i < content.length(); i++){
			if(Utils.isChinese(content.charAt(i))){
				str += content.charAt(i);
			}else{
				sentences.add(str);
				str = "";
			}
		}
		sentences.add(str);
		
		for(String string : sentences){
			if(string.trim().length() > 0)
				sentencesFinally.add(string.trim());
		}
		return sentencesFinally;
	}
	
	private static void generateSentence(){
		ArrayList<String> allFileNames = Utils.getDirFileNames(ARTICLE_DIR);
		ArrayList<String> sentences = null;
		StringBuilder builder = new StringBuilder("");
		for(String fileName : allFileNames){
			Utils.println("processing "+fileName);
			sentences = extractChineseSentences(fileName);
			for(String string : sentences){
				builder.append(string);
				builder.append("\n");
			}
		}
		String str = new String(builder);
		Utils.writeStringToFile(str, SENTENCES_FILE);
		Utils.println("finish sentences.txt");
	}
	private static void init(){
		File file = new File(SENTENCES_FILE);
		if(file.exists()){
			file.delete();
		}
	}
	public static void main(String[] args){
		init();
		generateSentence();
	}
}
