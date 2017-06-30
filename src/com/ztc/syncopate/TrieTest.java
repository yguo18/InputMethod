package com.ztc.syncopate;

import java.util.ArrayList;
import java.util.List;

import com.ztc.readFile.FileTools;

public class TrieTest {
	static TrieTree tree = new TrieTree("root");
	public static void main(String[] args){
		String spell = "maozhuxiwansui";
		initSpell();
		System.out.println(tree.splitSpell(spell));
	}
	
	private static void initSpell(){
		List<String> spells = new ArrayList<String>();
		FileTools.readFile(spells, "./dateFile/spell.txt");
		for(int i = 0; i < spells.size(); i++){
			tree.insert(spells.get(i));
		}
	}
}
