package com.QuesSystem.ques.enums;

import lombok.Getter;

@Getter
public enum QuestionnaireState {

	尚未投票(0), 投票中(1), 已結束(2);
	
	private int quesNum;
	
	private QuestionnaireState(int quesNum) {
		this.quesNum = quesNum;
	}

	public int getQuesNum() {
		return quesNum;
	}

	public void setQuesNum(int quesNum) {
		this.quesNum = quesNum;
	}
}
