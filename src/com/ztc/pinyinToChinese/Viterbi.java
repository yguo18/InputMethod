package com.ztc.pinyinToChinese;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Viterbi {
	/**
	 * 
	 * @param hmmParams   隐马尔科夫模型参数：start=初始概率，emission=发射概率， transition=状态转移概率
	 * @param observations  观察序列
	 * @param pathNum   记录的路径个数
	 * @param isLog  是否使用log函数求最大值
	 * @return
	 */
	public static ArrayList<PriorityQueueWrapper.Item> compute(HmmParamsInterface hmmParams, ArrayList<String> observations, int pathNum, boolean isLog) {
        double minProb = 3.14e-200;
        ArrayList<HashMap<String, PriorityQueueWrapper>> V = new ArrayList<HashMap<String, PriorityQueueWrapper>>();
        int t = 0;
        String curObs = observations.get(0);//获取首个拼音

        ArrayList<String> prevStates, curStates;
        prevStates = curStates = hmmParams.getStates(curObs); // 拼音对应的汉字用链表来表示
//        prevStates = curStates = curObs;
        

        Double score = 0.0;   // 打分
        ArrayList<String> path = null;  // 记录路径
        V.add(new HashMap<String, PriorityQueueWrapper>());
        for (String state : curStates) {
            if (isLog) {
                score = Math.log(Math.max(hmmParams.start(state), minProb)) + Math.log(Math.max(hmmParams.emission(state, curObs), minProb));
            } else {
            	/**
            	 * hmmParams.start(state):返回当前汉字的概率，hmmParams.emission(state, curObs)：返回由拼音转换成当前汉字的发射概率
            	 */
                score = Math.max(hmmParams.start(state), minProb) * Math.max(hmmParams.emission(state, curObs), minProb);
            }
            path = new ArrayList<>();
            path.add(state);//把汉字加入列表

            if (!V.get(0).containsKey(state)) {
                V.get(0).put(state, new PriorityQueueWrapper(pathNum));
            }
            V.get(0).get(state).put(score, path);
        }

        for (int i = 1; i < observations.size(); i++) {
            curObs = observations.get(i);

            if (V.size() == 2) {
                V.remove(0);
            }

            V.add(new HashMap<String, PriorityQueueWrapper>());

            prevStates = curStates;
            curStates = hmmParams.getStates(curObs);

            for (String state : curStates) {
                if (!V.get(1).containsKey(state)) {
                    V.get(1).put(state, new PriorityQueueWrapper(pathNum));
                }
                ArrayList<String> newPath = null;
                for (String state0 : prevStates) {
                    for (PriorityQueueWrapper.Item item : V.get(0).get(state0).getPriorityQueue()) {
                        if (isLog) {
                            score = item.getScore()
                                    + Math.log(Math.max(hmmParams.transition(state0, state), minProb))
                                    + Math.log(Math.max(hmmParams.emission(state, curObs), minProb));
                        } else {
                            score = item.getScore()
                                    * Math.max(hmmParams.transition(state0, state), minProb)
                                    * Math.max(hmmParams.emission(state, curObs), minProb);
                        }
                        newPath = new ArrayList<String>();
                        newPath.addAll(item.getPath());
                        newPath.add(state);
                        V.get(1).get(state).put(score, newPath);
                    }
                }
            }
        }

        PriorityQueueWrapper priorityQueueWrapperResult = new PriorityQueueWrapper(pathNum);
        if (observations.size() > 1) {
        	
            t = 1;
        } else {
            t = 0;
        }
        for (String lastState : V.get(t).keySet()) {
            for (PriorityQueueWrapper.Item item : V.get(t).get(lastState).getPriorityQueue()) {
                priorityQueueWrapperResult.put(item.getScore(), item.getPath());
            }
        }

        ArrayList<PriorityQueueWrapper.Item> result = new ArrayList<PriorityQueueWrapper.Item>();
        for (PriorityQueueWrapper.Item item : priorityQueueWrapperResult.getPriorityQueue()) {
           result.add(item);
        }

        Collections.sort(result, new Comparator<PriorityQueueWrapper.Item>() {
            @Override
            public int compare(PriorityQueueWrapper.Item o1, PriorityQueueWrapper.Item o2) {
                if (o1.getScore() > o2.getScore()) {
                    return -1;
                }
                if (o1.getScore() < o2.getScore()) {
                    return 1;
                }
                return 0;
            }
        });

        return result;
    }
}
