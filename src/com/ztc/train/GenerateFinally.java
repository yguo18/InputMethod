package com.ztc.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.ztc.util.Utils;

/**
 * 
 * input: source file
 * output: HMM file
 * @author yguo
 *
 */
public class GenerateFinally {
	private static final String PINYIN2HANZI_FILE = "./dateFile/input/temp/pinyin2hanzi.txt";
	private static final String BASE_START = "./dateFile/input/temp/base_start.txt";
	private static final String BASE_EMISSION = "./dateFile/input/temp/base_emission.txt";
	private static final String BASE_TRANSITION = "./dateFile/input/temp/base_transition.txt";
	
	private static final String DATA_PATH = "./dateFile/output";
	private static final String FINAL_PINYIN2HANZI_FILE = "./dateFile/output/hmm_pinyin2hanzi.txt";
	private static final String FINAL_START_FILE = "./dateFile/output/hmm_start.txt";
	private static final String FINAL_EMISSION_FILE = "./dateFile/output/hmm_emission.txt";
	private static final String FINAL_TRANSITION_FILE = "./dateFile/output/hmm_transition.txt";
	
	private static final Double PINYIN_NUM = 411.;
	private static final Double HANZI_NUM = 20903.;
	private static Map<String, String> pinyin2hanziMap = new HashMap<String, String>();
    // {"你" : 2, "号" : 1}
    private static HashMap<String, Double> start = new HashMap<String, Double>();
    // {"泥" : {"ni" : 1.0}}, }
    private static HashMap<String, HashMap<String, Double>> emission = new HashMap<String, HashMap<String, Double>>();
    // {"你": {"好" : 10, "们" : 2}}
    private static HashMap<String, HashMap<String, Double>> transition = new HashMap<String, HashMap<String, Double>>();
/**
 * 读取pinyin2Hanzi
 */
	private static void generatePinyin2Hanzi(){
		BufferedReader buf = null;
		String line = null;
		String[] lineSplitResult = null;
		String pinyin = null;
		String hanziList = null;
		try{
			buf = new BufferedReader(new FileReader(PINYIN2HANZI_FILE));
//			buf = new BufferedReader(new InputStreamReader(new FileInputStream(PINYIN2HANZI_FILE),"utf-8"));
			while((line = buf.readLine())!=null){
				line = line.trim();
				lineSplitResult = line.split("=");
				if(lineSplitResult.length != 2){
					try{
						throw new InvalidFormatException("invalid format");
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
				pinyin = lineSplitResult[0];     //获取拼音
				hanziList = lineSplitResult[1];  // 获取汉字
				pinyin = pinyin.trim();
				hanziList = hanziList.trim();
				
				if(pinyin.length() >0 && hanziList.length()>0){
					pinyin2hanziMap.put(pinyin, hanziList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(buf != null){
				try{
					buf.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 初始概率
	 */
	private static void generateStart() {
        BufferedReader buf = null;
        String line = null;
        String[] lineSplitResult = null;
        String hanzi = null;
        String numString = null;
        Double count = HANZI_NUM;
        Double num = 0.0;

        try {
            buf = new BufferedReader(new FileReader(BASE_START));
//        	buf = new BufferedReader(new InputStreamReader(new FileInputStream(BASE_START),"utf-8"));
            while ((line = buf.readLine()) != null) {
                line = line.trim();
                lineSplitResult = line.split("=");
                hanzi = lineSplitResult[0];
                numString = lineSplitResult[1];
                num = Double.valueOf(numString);
                count += num;
                start.put(hanzi, num);
            }
            for (Map.Entry<String, Double> entry : start.entrySet()) {
                start.put(entry.getKey(), entry.getValue() / count);
            }
            start.put("default", 1 / count);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	/**
	 * 发射概率
	 */
    private static void generateEmission() {
        BufferedReader buf = null;
        String line = null;
        String[] lineSplitResult = null;
        String hanzi = null;
        String pinyinNum = null;
        String[] pinyinNumSplitResult = null;
        String[] pinyinNumSplitSingleResult = null;
        HashMap<String, Double> pinyinNumMap = null;

        try {
            buf = new BufferedReader(new FileReader(BASE_EMISSION));
//        	buf = new BufferedReader(new InputStreamReader(new FileInputStream(BASE_EMISSION),"utf-8"));
            while ((line = buf.readLine()) != null) {
                Double numSum = 0.0;
                line = line.trim();
                lineSplitResult = line.split("=");
                hanzi = lineSplitResult[0];
                pinyinNum = lineSplitResult[1];
                pinyinNumSplitResult = pinyinNum.split(",");
                pinyinNumMap = new HashMap<String, Double>();
                for (String string : pinyinNumSplitResult) {
                    pinyinNumSplitSingleResult = string.split(":");
                    numSum += Double.valueOf(pinyinNumSplitSingleResult[1]);
                    pinyinNumMap.put(pinyinNumSplitSingleResult[0], Double.valueOf(pinyinNumSplitSingleResult[1]));
                }
                for (Map.Entry<String, Double> entry : pinyinNumMap.entrySet()) {
                    pinyinNumMap.put(entry.getKey(), entry.getValue() / numSum);
                }
                emission.put(hanzi, pinyinNumMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 转换概率
     */
    private static void generateTransition() {
        BufferedReader buf = null;
        String line = null;
        String[] lineSplitResult = null;
        String hanzi1 = null;
        String hanzi2String = null;
        String[] hanzi2StringSplitResult = null;
        String[] hanzi2StringSplitResultSingle = null;
        HashMap<String, Double> hanzi2NumMap = null;

        try {
        	
            buf = new BufferedReader(new FileReader(BASE_TRANSITION));
//        	buf = new BufferedReader(new InputStreamReader(new FileInputStream(BASE_TRANSITION),"utf-8"));
            while ((line = buf.readLine()) != null) {
                Double numSum = HANZI_NUM;
                line = line.trim();
                lineSplitResult = line.split("=");
                hanzi1 = lineSplitResult[0];
                hanzi2String = lineSplitResult[1];
                hanzi2StringSplitResult = hanzi2String.split(",");
                hanzi2NumMap = new HashMap<String, Double>();
                for (String string : hanzi2StringSplitResult) {
                    hanzi2StringSplitResultSingle = string.split(":");
                    numSum += Double.valueOf(hanzi2StringSplitResultSingle[1]);
                    hanzi2NumMap.put(hanzi2StringSplitResultSingle[0], Double.valueOf(hanzi2StringSplitResultSingle[1]));
                }
                for (Map.Entry<String, Double> entry : hanzi2NumMap.entrySet()) {
                    hanzi2NumMap.put(entry.getKey(), (entry.getValue() + 1) / numSum);
                }
                hanzi2NumMap.put("default", 1 / numSum);
                transition.put(hanzi1, hanzi2NumMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 写字符串到文件
     * @param string
     * @param filePath
     */
    public static void writeStringToFile(String string, String filePath) {
        File file = new File(filePath);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(string);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void writePinyin2HanziMapToFile() {
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, String> entry : pinyin2hanziMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        String str = new String(builder);
        writeStringToFile(str, FINAL_PINYIN2HANZI_FILE);
    }

    public static void writeStartToFile() {
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, Double> entry : start.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        String str = new String(builder);
        writeStringToFile(str, FINAL_START_FILE);
    }

    public static void writeEmissionToFile() {
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, HashMap<String, Double>> entry : emission.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            for (Map.Entry<String, Double> entry1 : entry.getValue().entrySet()) {
                builder.append(entry1.getKey());
                builder.append(":");
                builder.append(entry1.getValue());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        String str = new String(builder);
//        writeStringToFile(str, FINAL_EMISSION_FILE);
        Utils.writeStringToFile(str, FINAL_EMISSION_FILE);
    }
    
    public static void writeTransitionToFile() {
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, HashMap<String, Double>> entry : transition.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            for (Map.Entry<String, Double> entry1 : entry.getValue().entrySet()) {
                builder.append(entry1.getKey());
                builder.append(":");
                builder.append(entry1.getValue());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        String str = new String(builder);
        writeStringToFile(str, FINAL_TRANSITION_FILE);
    }

    private static void initDir() {
        createDir(DATA_PATH);
    }
    public static void createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    public static void main(String[] args) {
    	initDir();
        generatePinyin2Hanzi();
        generateStart();
        generateEmission();
        generateTransition();
        writePinyin2HanziMapToFile();
        writeStartToFile();
        writeEmissionToFile();
        writeTransitionToFile();
    }

}
