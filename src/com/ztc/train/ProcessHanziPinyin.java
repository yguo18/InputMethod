package com.ztc.train;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ztc.util.Utils;
/**
 * {@code return pinyin2hanzi file}
 * Æ´Òô=¶ÔÓ¦µÄ²»Í¬ºº×ÖÐòÁÐ
 * @author yguo
 *
 */
public class ProcessHanziPinyin {
	private static final String INPUT_PATH = "./dateFile/input";
    private static final String OUTPUT_PATH  = "./dateFile/output";
    private static final String HANZIPINYIN_FILE = "./dateFile/input/sources/hanzipinyin.txt";
    
    //Êä³ö
    private static final String ALL_STATES_FILE = "./dateFile/input/temp/all_states.txt"; // ºº×Ö£¨Òþ²Ø×´Ì¬£©
    private static final String ALL_OBSERVATIONS_FILE = "./dateFile/input/temp/all_observations.txt"; // Æ´Òô£¨¹Û²âÖµ£©
    private static final String PINYIN2HANZI_FILE = "./dateFile/input/temp/pinyin2hanzi.txt";

    private static Set<String> states = new HashSet<String>();
    private static Set<String> observations = new HashSet<String>();
    private static Map<String, HashSet<String>> pinyin2hanzi = new HashMap<String, HashSet<String>>();
    
    private static void generator() {
        BufferedReader buf = null;

        try {
//            buf = new BufferedReader(new FileReader(HANZIPINYIN_FILE));
        	buf = new BufferedReader(new InputStreamReader(new FileInputStream(HANZIPINYIN_FILE), "utf-8"));
            String line = null;
            String[] lineSplitResult = null;
            String hanzi = null;
            String pinyinList = null;
            String[] pinyinListSplit = null;
            String shengmu = null;

            while ((line = buf.readLine()) != null) {
                line = line.trim();
                lineSplitResult = line.split("=");
                hanzi = lineSplitResult[0];
                pinyinList = lineSplitResult[1];
                pinyinListSplit = pinyinList.split(",");
                for (int i = 0; i < pinyinListSplit.length; i++) {
                    pinyinListSplit[i] = Utils.simplifyPinyin(pinyinListSplit[i].trim());
                }
                states.add(hanzi);
                for (int i = 0; i < pinyinListSplit.length; i++) {
                    if (!pinyinListSplit[i].equals("null")) {
                        observations.add(pinyinListSplit[i]);
                    }
                    if (!pinyin2hanzi.containsKey(pinyinListSplit[i])) {
                        pinyin2hanzi.put(pinyinListSplit[i], new HashSet<String>());
                    }
                    pinyin2hanzi.get(pinyinListSplit[i]).add(hanzi);

                    shengmu = Utils.getShengmu(pinyinListSplit[i]);
                    if (shengmu != null) {
                        if (!pinyin2hanzi.containsKey(shengmu)) {
                            pinyin2hanzi.put(shengmu, new HashSet<>());
                        }
                        pinyin2hanzi.get(shengmu).add(hanzi);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void writeStateToFile() {
        StringBuilder builder = new StringBuilder("");
        for (String string : states) {
            builder.append(string);
        }
        String str = Utils.joinLineBreak(new String(builder));

        Utils.writeStringToFile(str, ALL_STATES_FILE);
        Utils.println("finish all_start.txt");
    }
    private static void writeObservationToFile() {
        StringBuilder builder = new StringBuilder("");
        for (String string : observations) {
            builder.append(string);
            builder.append("\n");
        }
        String str = new String(builder);
        str = str.substring(0, str.length() - 1);

        Utils.writeStringToFile(str, ALL_OBSERVATIONS_FILE);
        Utils.println("finish all_obeservation.txt");
    }
    private static void writePinyin2HanziToFile() {
        StringBuilder builder = new StringBuilder("");
        for (Map.Entry<String, HashSet<String>> entry : pinyin2hanzi.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            for (String string : entry.getValue()) {
                builder.append(string);
            }
            builder.append("\n");
        }
        String str = new String(builder);

        Utils.writeStringToFile(str, PINYIN2HANZI_FILE);
        Utils.println("finish pinyin2hanzi.txt");
    }
    private static void initDir() {
        Utils.createDir(INPUT_PATH);
        Utils.createDir(OUTPUT_PATH);
    }
    public static void main(String[] args) {
        initDir();
        generator();
        writeStateToFile();
        writeObservationToFile();
        writePinyin2HanziToFile();
    }
    
}
