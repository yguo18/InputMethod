package com.ztc.pinyinToChinese;

import java.util.ArrayList;
import java.util.Scanner;

public class Test {
	public static void main(String[] args){
		DefaultHmmParams hmmParams = new DefaultHmmParams();
		while(true){
			System.out.println("«Î ‰»Î∆¥“Ù£∫");
			Scanner sc = new Scanner(System.in);
			String pinyin = sc.nextLine();
			String[] p = pinyin.split("\\s+");
			ArrayList<String> s = new ArrayList<String>();
			for(int i = 0; i<p.length; i++){
				s.add(p[i]);
			}
			for(PriorityQueueWrapper.Item item : Viterbi.compute(hmmParams, s, 20, false)){
				System.out.print(item.getPath());
			}
			System.out.println();
		}
	}
}
