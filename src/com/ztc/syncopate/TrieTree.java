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
	 * 插入操作
	 * @param word
	 */
	public void insert(String word){
		//...
	}
	/**
	 * 查找操作
	 */
	public int searchFre(){
		int fre = -1;
		//....
		return fre;
	}
	/**
	 * 切分操作
	 */
	public String splitSpell(String spell){
		//....
		
		return spell;
	}
	
	
}
