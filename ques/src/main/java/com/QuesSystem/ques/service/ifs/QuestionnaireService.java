package com.QuesSystem.ques.service.ifs;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuesSystem.ques.entity.Questionnaire;

public interface QuestionnaireService {

	public List<Questionnaire> getQuestionnaireList();
	
	/* 刪除問卷 */
	public void deleteQuestionnaire(String[] questionnaireId);
	
	/* 新增問卷防呆 - 在QuestionnaireContoller的84行被使用 */ 
	public String ErrorMsg(String questionnaireTitle,String questionnaireBody, String startDate, String endDate);
	
	/* 利用分頁取得問卷清單(前台) */
	public Page<Questionnaire> getQuestionnaireByPageListFront(int pageNum, int pageSize);
	
	/**
	 * @param pageNum
	 * @param pageSize
	 * @param questionnaireTitle 問卷標題
	 * @return questionnaires
	 */
	public Page<Questionnaire> searchQuestionnaireByQuesTitleFront(int pageNum, int pageSize, String questionnaireTitle);

	/*
	 * 利用分頁取得問卷清單(後台)
	 */
	public Page<Questionnaire> getQuestionnaireByPageList(int pageNum,int pageSize);
	
	public Page<Questionnaire> searchQuestionnaireByQuesTitle(int pageNum,int pageSize, String questionnaireTitle);
	
	public Page<Questionnaire> searchQuestionnaireByAllTime(int pageNum,int pageSize, Date startDate, Date endDate);
}
