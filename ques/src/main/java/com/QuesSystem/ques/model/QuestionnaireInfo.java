package com.QuesSystem.ques.model;

import java.util.List;

import com.QuesSystem.ques.entity.Question;

public class QuestionnaireInfo {

	private List<Question> questions;
	
	private List<Answers> answers;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Answers> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answers> answers) {
		this.answers = answers;
	}
}
