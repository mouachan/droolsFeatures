package com.redhat.droolsfeature.model;

import java.util.Map;

public class Fact {
	private int value;
    private Map<String, Integer> myMap;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Map<String, Integer> getMyMap() {
		return myMap;
	}
	public void setMyMap(Map<String, Integer> myMap) {
		this.myMap = myMap;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Fact [value=");
		builder.append(value);
		builder.append(", myMap=");
		builder.append(myMap);
		builder.append("]");
		return builder.toString();
	}
}
