package com.ztc.pinyinToChinese;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * read HMM file into hashMap
 * @author yguo
 *
 */
public class DefaultHmmParams implements HmmParamsInterface{
	private Map<String,  String> pinyinToChineseMap = new HashMap<String,String>();
	//{"ni":2, "hao":1}
	private HashMap<String, Double> start = new HashMap<String, Double>();
	//{"ni":{"ni":2}}
	private HashMap<String, HashMap<String, Double>> emission = new HashMap<String, HashMap<String,Double>>();
	private HashMap<String, HashMap<String, Double>> transition = new HashMap<String, HashMap<String,Double>>();
	
	//hanzi 个数 20903
	private static final Double HANZI_NUM = 20903.;
	
	//文件path
	private static final String FINAL_PINTOHANZI_FILE = "./dateFile/output/hmm_pinyin2hanzi.txt";
	private static final String FINAL_START_FILE = "./dateFile/output/hmm_start.txt";
	private static final String FINAL_EMISSION_FILE = "./dateFile/output/hmm_emission.txt";
	private static final String FINAL_TRANSITION_FILE = "./dateFile/output/hmm_transition.txt";
	String charset = "UTF-8";
	public DefaultHmmParams(){
		initPinyinToHanziMap();
		initStart();
		initEmission();
		initTransition();
	}
	/**
	 * input pinyin to hanzi file
	 */
	private void initPinyinToHanziMap(){
		BufferedReader buf = null;
		String line = null;
		String[] lineSplitResult = null;
		String pinyin = null;
		String hanziList = null;
		
		try {
			buf = new BufferedReader(new FileReader(FINAL_PINTOHANZI_FILE));
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(FINAL_PINTOHANZI_FILE), charset));
			while((line = buf.readLine())!=null){
				line = line.trim(); //去处空格
				lineSplitResult = line.split("=");
				pinyin = lineSplitResult[0].trim();
				hanziList = lineSplitResult[1].trim();
				if(pinyin.length()>0 && hanziList.length()>0){
					pinyinToChineseMap.put(pinyin, hanziList);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(buf != null){
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * initStart method
	 * deal with start.txt file
	 */
	private void initStart(){
		BufferedReader buf = null;
		String line = null;
		String[] lineSplitResult = null;
		String hanzi = null;
		Double num = null;
		try{
			buf = new BufferedReader(new FileReader(FINAL_START_FILE));
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(FINAL_START_FILE), charset));
			while((line = buf.readLine())!=null){
				line = line.trim();
				lineSplitResult = line.split("=");
				hanzi = lineSplitResult[0];
				num = Double.valueOf(lineSplitResult[1]);
				start.put(hanzi, num);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(buf != null){
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * initEmission
	 * deal with Emission.txt file
	 */
	private void initEmission(){
		BufferedReader buf = null;
		String line = null;
		String[] lineSplitResult = null;
		String hanzi = null;
		String pinyinNum = null;
		String[] pinyinNumSplitResult = null;
		String[] pinyinNumSplitSingleResult = null;
		HashMap<String, Double> pinyinNumMap = null;
		try{
			buf = new BufferedReader(new FileReader(FINAL_EMISSION_FILE));
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(FINAL_EMISSION_FILE),charset));
			while((line = buf.readLine())!=null){
				line = line.trim();
				lineSplitResult = line.split("=");
				hanzi = lineSplitResult[0];
				pinyinNum = lineSplitResult[1];
				pinyinNumSplitResult = pinyinNum.split(",");
				pinyinNumMap = new HashMap<String, Double>();
				for(String string : pinyinNumSplitResult){
					pinyinNumSplitSingleResult = string.split(":");
					pinyinNumMap.put(pinyinNumSplitSingleResult[0], Double.valueOf(pinyinNumSplitSingleResult[1]));
				}
				emission.put(hanzi, pinyinNumMap);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			if(buf != null){
				try {
					buf.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * initTransition method
	 * deal with transition.txt file
	 */
	
	private void initTransition(){
		BufferedReader buf = null;
		String line = null;
		String[] lineSplitResult = null;
		String hanzi = null;
		String hanzi2String = null;
		String[] hanzi2StringSplitResult = null;
		String[] hanzi2StringSllitSingleResult = null;
		HashMap<String, Double> hanzi2NumMap = null;
		try {
			buf = new BufferedReader(new FileReader(FINAL_TRANSITION_FILE));
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(FINAL_TRANSITION_FILE), charset));
			while((line = buf.readLine())!=null){
				line = line.trim();
				lineSplitResult = line.split("=");
				hanzi = lineSplitResult[0];
				hanzi2String = lineSplitResult[1];
				hanzi2StringSplitResult = hanzi2String.split(",");
				hanzi2NumMap = new HashMap<String,Double>();
				for(String string:hanzi2StringSplitResult){
					hanzi2StringSllitSingleResult = string.split(":");
					hanzi2NumMap.put(hanzi2StringSllitSingleResult[0], Double.valueOf(hanzi2StringSllitSingleResult[1]));
				}
				transition.put(hanzi, hanzi2NumMap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(buf != null){
				try {
					buf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public Double start(String state){
		if(start.get(state) == null){
			return start.get("default");
		}
		return start.get(state);
	}
	
	@Override
	public Double emission(String state, String observation){
		if(emission.get(state).get(observation)==null){
			return 1e-200;
		}
		return emission.get(state).get(observation);
	}
	
	
	@Override
	public Double transition(String state1, String state2){
		if(transition.get(state1) == null){
			return 1/HANZI_NUM;
			
		}
		if(transition.get(state1).get(state2) == null){
			return transition.get(state1).get("default");
			
		}
		return transition.get(state1).get(state2);
	}
	/**
	 * return： 一个拼音对应的不同汉字序列
	 */
	public ArrayList<String> getStates(String observation){
		String hanziListString = pinyinToChineseMap.get(observation);
		ArrayList<String> hanziList = new ArrayList<String>();
		for(int i =0; i < hanziListString.length(); i++){
			hanziList.add(hanziListString.charAt(i)+"");
		}
		return hanziList;
	}
//	public static void main(String[] args){
//		DefaultHmmParams hmmParams = new DefaultHmmParams();
//		ArrayList<String> observation = new ArrayList<String>();
//		observation.add("xi");
//		observation.add("an");
//		observation.add("dian");
//		observation.add("zi");
//		observation.add("ke");
//		observation.add("ji");
//		observation.add("da");
//		observation.add("xue");
//		Viterbi.compute(hmmParams, observation, 3, false);
//		
//	}
}
