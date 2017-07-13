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
		TrieNode node = root;
		char[] words = word.toCharArray();
		for(int i = 0; i < words.length; i++){
			if(node.getChildrens().containsKey(words[i]+"")){
				if(i == words.length-1){
					TrieNode endNode = node.getChildrens().get(words[i]+"");
					endNode.setFre(endNode.getFre()+1);
					endNode.setEnd(true);
				}
			}else{
				TrieNode newNode = new TrieNode(words[i]+"");
				if(i == words.length -1){
					newNode.setFre(1);
					newNode.setEnd(true);
					newNode.setRoot(false);
				}
				node.getChildrens().put(words[i]+"",newNode);
			}
			node = node.getChildrens().get(words[i]+"");
		}
	}
	/**
	 * 查找操作
	 */
	public int searchFre(String word){
		int fre = -1;
		//....
		TrieNode node = root;
		char[] words = word.toCharArray();
		for(int i = 0; i<words.length; i++){
			if(node.getChildrens().containsKey(words[i]+"")){
				node = node.getChildrens().get(words[i]+"");
				fre = node.getFre();
			}else{
				fre = -1;
				break;
			}
		}
		return fre;
	}
	/**
	 * 是否包含字符串
	 */
	public boolean findInitialWith(String word){
		TrieNode node = root;
		char[] words = word.toCharArray();
		for(int i =0; i<words.length; i++){
			if(!node.getChildrens().containsKey(words[i]+"")){
				return false;
			}
			node = node.getChildrens().get(words[i]+"");
		}
		if(node == null){
			return false;
		}
		return true;
	}
	/**
	 * 分词操作
	 * @param spell
	 * @return
	 */
	public String splitSpell(String spell){
		//....
		TrieNode node = root;
		char[] letters = spell.toCharArray();
		String spells = "";
		for(int i = 0; i<letters.length; i++){
			if(node.getChildrens().containsKey(letters[i]+"")){
				spells += letters[i];
				node = node.getChildrens().get(letters[i]+"");
			}else {
				node = root;
				spells += " ";
				i--;
			}
		}
		return spells;
	}
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args){
		TrieTree trieTree = new TrieTree("guoyi");
		trieTree.insert("ang");
		trieTree.insert("wo");
		trieTree.insert("my");
		trieTree.insert("an");
		trieTree.insert("y");
		trieTree.insert("m");
		System.out.println(trieTree.searchFre("an"));
		System.out.println(trieTree.splitSpell("anym"));
		System.out.println(trieTree.findInitialWith("an3"));
	}
	
}
