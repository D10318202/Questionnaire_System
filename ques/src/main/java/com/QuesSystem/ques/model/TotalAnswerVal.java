package com.QuesSystem.ques.model;

import java.util.Map;

public class TotalAnswerVal {
	
	private String title;
	
	private Map<String, Integer> choicesMap;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Integer> getChoicesMap() {
		return choicesMap;
	}

	public void setChoicesMap(Map<String, Integer> choicesMap) {
		this.choicesMap = choicesMap;
	}	

}
