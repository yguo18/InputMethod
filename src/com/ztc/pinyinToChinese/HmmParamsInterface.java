package com.ztc.pinyinToChinese;

import java.util.ArrayList;

public interface HmmParamsInterface {
	Double start(String state);
	Double emission(String state, String observation);
	Double transition(String state1, String state2);
	ArrayList<String> getStates(String observation);
}
