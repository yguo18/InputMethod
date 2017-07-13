package com.ztc.viterbiTest;

import static com.ztc.viterbiTest.WeatherExample.Weather.*;
import static com.ztc.viterbiTest.WeatherExample.Activity.*;

public class WeatherExample {
	enum Weather{
		Rainny,
		Sunny,
	}
	enum Activity{
		walk,
		shop,
		clean,
	}
	static int[] states = new int[]{Rainny.ordinal(),Sunny.ordinal()};
	static int[] obeservations = new int[]{clean.ordinal(), walk.ordinal(), shop.ordinal()};
	static double[] start_probality = new double[]{0.6,0.4};// 初始概率
	static double[][] transition_probality = new double[][]{{0.7,0.3},{0.4,0.6}};//转移概率
	static double[][] emission_probality = new double[][]{  //发射概率
		{0.1,0.4,0.5},
		{0.6,0.3,0.1},
	};
	public static void main(String[] args){
		//System.out.println(Sunny.ordinal());
		int[] result = Viterbi.compute(obeservations, states, start_probality, transition_probality, emission_probality);
		for(int r : result){
			System.out.print(Weather.values()[r]+" ");
		}
	}
}
