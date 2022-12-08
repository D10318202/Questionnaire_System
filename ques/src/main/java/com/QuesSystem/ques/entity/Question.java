package com.QuesSystem.ques.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "questions")
@XmlRootElement
@NamedQuery(name = "questions.findAll", query = "SELECT ques FROM Question ques")
public class Question{

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "question_id", unique = true)
	private String questionId;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "questionnaire_Id")
	private Questionnaire questionnaireId;
	
	@Column(name = "question_title", length=15)
	private String questionTitle;

	@Column(name = "question_choices")
	private String questionChoices;
	
	@Column(name = "question_type")
	private int questionType;

	@Column(name = "must_keyin")
	private boolean mustKeyin;
	
	@Column(name = "question_number")
	private int questionNumber;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Questionnaire getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Questionnaire questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getQuestionChoices() {
		return questionChoices;
	}

	public void setQuestionChoices(String questionChoices) {
		this.questionChoices = questionChoices;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public boolean isMustKeyin() {
		return mustKeyin;
	}

	public void setMustKeyin(boolean mustKeyin) {
		this.mustKeyin = mustKeyin;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
}
