package com.ztc.syncopate;

public class TrieTree {
	TrieNode root;
	public TrieTree(String name){
		root = new TrieNode(name);
		root.setFre(0);
		root.setRoot(true);
		root.setEnd(false);
	}
	/**
	 * �������
	 * @param word
	 */
	public void insert(String word){
		//...
	}
	/**
	 * ���Ҳ���
	 */
	public int searchFre(){
		int fre = -1;
		//....
		return fre;
	}
	/**
	 * �зֲ���
	 */
	public String splitSpell(String spell){
		//....
		
		return spell;
	}
	
	
}
