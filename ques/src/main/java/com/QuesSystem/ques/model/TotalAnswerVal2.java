package com.QuesSystem.ques.model;

import java.util.Set;

public class TotalAnswerVal2 {

	private String QuestionTitle;
	
	private Set<ChoicesInfo> choicesInfo;

	public String getQuestionTitle() {
		return QuestionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		QuestionTitle = questionTitle;
	}

	public Set<ChoicesInfo> getChoicesInfo() {
		return choicesInfo;
	}

	public void setChoicesInfo(Set<ChoicesInfo> choicesInfo) {
		this.choicesInfo = choicesInfo;
	}
}
