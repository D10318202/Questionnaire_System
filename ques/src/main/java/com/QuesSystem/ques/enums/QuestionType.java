package com.QuesSystem.ques.enums;

import lombok.Getter;

@Getter
public enum QuestionType {

	單選方塊(0), 複選方塊(1), 文字方塊(2);

	private int Code;

	QuestionType(int Code) {
		this.Code = Code;		
	}

	public int getCode() {
		return Code;
	}	
}
