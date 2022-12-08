package com.QuesSystem.ques.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
/* 跟資料庫互動 */
@Table(name = "questionnaires")  
@XmlRootElement
@NamedQuery(name = "questionnaires.findAll", query = "SELECT q FROM Questionnaire q")
public class Questionnaire{
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "questionnaire_id", unique = true)
	private String questionnaireId;

	/**
	 * 問卷標題
	 */
	@Column(name = "questionnaire_title", length=20)	
	private String questionnaireTitle;
    
	/**
	 * 問卷大綱
	 */
	@Column(name = "questionnaire_body", length=200)	
	private String questionnaireBody;
	
	/*
	 * 問卷狀態
	 */
	@Column(name = "questionnaire_states")
	private boolean questionnaireStates;
		
	@Column(name="start_date")
	private Date startDate;

	@Column(name="end_date")
	private Date endDate;	
	
	@Column(name="create_date")
	private Date createDate = new Date();
	
	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getQuestionnaireTitle() {
		return questionnaireTitle;
	}

	public void setQuestionnaireTitle(String questionnaireTitle) {
		this.questionnaireTitle = questionnaireTitle;
	}

	public String getQuestionnaireBody() {
		return questionnaireBody;
	}

	public void setQuestionnaireBody(String questionnaireBody) {
		this.questionnaireBody = questionnaireBody;
	}

	public boolean isQuestionnaireStates() {
		return questionnaireStates;
	}

	public void setQuestionnaireStates(boolean questionnaireStates) {
		this.questionnaireStates = questionnaireStates;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
