package com.ztc.pinyinToChinese;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueWrapper {
	private PriorityQueue<Item> priorityQueue;
	private int capacity; // 容量
	
	public PriorityQueueWrapper(int capacity){
		priorityQueue = new PriorityQueue<Item>(new MyComparator());
		this.capacity = capacity;
	}
	public void put(double score, ArrayList<String> path){
		Item item = new Item(score, path);
		priorityQueue.add(item);
		
		//如果超容，则把优先级最小的删除
		if(priorityQueue.size() > capacity){
			priorityQueue.poll();
		}
	}
	public PriorityQueue<Item> getPriorityQueue(){
		return priorityQueue;
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Item item : priorityQueue){
			builder.append(item.getScore());
			builder.append(" ");
			builder.append(item.getPath());
			builder.append("\n");
		}
		return new String(builder);
	}
	
	
	class Item{
		private double score;
		private ArrayList<String> path;
		
		public Item(double score, ArrayList<String> path) {
			this.score = score;
			this.path = path;
		}
		public double getScore(){
			return score;
		}
		public void setScore(double score){
			this.score = score;
		}
		
		public ArrayList<String> getPath(){
			return path;
		}
		public void setPath(ArrayList<String> path){
			this.path = path;
		}
		
	}
	class MyComparator implements Comparator<Item>{
		@Override
		public int compare(Item i1, Item i2){
			if(i1.getScore() < i2.getScore())
				return -1;
			if(i1.getScore() > i2.getScore())
				return 1;
			return 0;
		}
	}
	
	
}
