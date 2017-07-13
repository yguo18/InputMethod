package com.ztc.pinyinToChinese;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ztc.readFile.FileTools;
import com.ztc.syncopate.TrieTree;

public class Test {
	static TrieTree tree = new TrieTree("root");
	public static void main(String[] args){
		DefaultHmmParams hmmParams = new DefaultHmmParams();
		initSpell();
		Scanner sc = new Scanner(System.in);
		System.out.println("«Î ‰»Î∆¥“Ù£∫");
		while(sc.hasNext()){		
			String pinyin = sc.nextLine();
			pinyin = tree.splitSpell(pinyin);
			String[] p = pinyin.split("\\s+");
			ArrayList<String> s = new ArrayList<String>();
			for(int i = 0; i<p.length; i++){
				s.add(p[i]);
			}
			for(PriorityQueueWrapper.Item item : Viterbi.compute(hmmParams, s, 6, true)){
				System.out.print(item.getPath());
			}
			System.out.println();
			System.out.println("«Î ‰»Î∆¥“Ù£∫");
		}
//		while(sc.hasNext()){
//			
//			String pinyin = sc.nextLine();
//			String[] p = pinyin.split("\\s+");
//			ArrayList<String> s = new ArrayList<String>();
//			for(int i = 0; i<p.length; i++){
//				s.add(p[i]);
//			}
//			for(PriorityQueueWrapper.Item item : Viterbi.compute(hmmParams, s, 6, true)){
//				System.out.print(item.getPath());
//			}
//			System.out.println();
//			System.out.println("«Î ‰»Î∆¥“Ù£∫");
//		}
	}
	private static void initSpell(){
		List<String> spells = new ArrayList<String>();
		FileTools.readFile(spells, "./dateFile/spell.txt");
		for(int i = 0; i < spells.size(); i++){
			tree.insert(spells.get(i));
		}
	}
}

