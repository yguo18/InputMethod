package com.ztc.syncopate;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
	String name; //�����ַ�����
	int fre; //���ʳ��ֵ�Ƶ��
	boolean end; //�Ƿ��ǵ��ʽ�β
	boolean root; //�Ƿ��Ǹ����
	Map<String, TrieNode> childrens;//�ӽڵ���Ϣ
	public TrieNode(String name){
		this.name = name;
		if(childrens == null){
			childrens = new HashMap<String, TrieNode>();
		}
		setFre(0);
		setRoot(false);
		setEnd(false);
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public int getFre(){
		return fre;
	}
	public void setFre(int fre){
		this.fre = fre;
	}
	public boolean isEnd(){
		return end;
	}
	public void setEnd(boolean end){
		this.end = end;
	}
	public boolean isRoot(){
		return root;
	}
	public void setRoot(boolean root){
		this.root = root;
	}
	
	public Map<String, TrieNode> getChildrens(){
		return childrens;
	}
	public void setChildrens(Map<String, TrieNode> childrens){
		this.childrens = childrens;
	}
	
}
