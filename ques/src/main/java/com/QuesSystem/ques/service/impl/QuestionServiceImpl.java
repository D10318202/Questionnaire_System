package com.QuesSystem.ques.service.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.entity.OftenUseQuestion;
import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.enums.QuestionType;
import com.QuesSystem.ques.repository.CommonQuestionDao;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.service.ifs.QuestionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class QuestionServiceImpl implements QuestionService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private int quesNumber = 0;

	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private CommonQuestionDao commonQuestionDao;

	@Override
	public boolean QuestionMust(List<Question> queslist) {
		boolean must = false;
		for (Question question : queslist) {
			if (question.isMustKeyin() == true) {
				must = true;
				return must;
			}
		}
		return must;
	}

	/*
	 * 刪除問題 使用questionnaireId
	 */
	@Override
	public void deleteQuestions(String[] questionnaireId) {		
		try {
			for (String string : questionnaireId) {
				List<Question> list = questionDao.findByQuestionnaireId(string);
				for (Question question : list) {
					questionDao.delete(question);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
//	@Override
//	public void deleteQuestions(String[] questionnaireId) {		
//		try {
//			for (String quesId : questionnaireId) {
//			     questionDao.deletebyQuestionnaireId(questionnaireId);
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//	}

	/* 新增問題防呆 */
	@Override
	public String ErrorMsg(String questionTitle, 
			               String questionChoices, 
			               int questionType) {
		/* 問題標題防呆 */
		if (!StringUtils.hasText(questionTitle)) {
			/* 問題標題為空 */
			return MistakeMsg.QuestionMsg.No_QuesTitle;
		} else if (questionTitle.length() < 3) {
			// 問題至少要有三個字
			return MistakeMsg.QuestionMsg.Title_AtLastThree;
		}

		/* 問題答案即問題種類防呆 */
		if (questionType == QuestionType.單選方塊.getCode() && questionChoices == null) 
		{
			/* 單選方塊需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.Must_QuesAndAns;
		} 
		else if(questionType == QuestionType.單選方塊.getCode() && questionChoices.length() < 6)
		{
			/* 單選方塊需要輸入問題,答案至少要有6個字 */
			return MistakeMsg.QuestionMsg.Radio_Must_QuesAndAnsMustSix;
		}
		
		if (questionType == QuestionType.複選方塊.getCode() && questionChoices == null) {
			/* 複選方塊需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.Must_QuesAndAns;
		} 
		else if(questionType == QuestionType.複選方塊.getCode() && questionChoices.length() < 8){
			/* 複選方塊需要輸入問題,答案至少要有8個字 */
			return MistakeMsg.QuestionMsg.Check_Must_QuesAndAnsMustEight;
		}
		
		if (questionType != QuestionType.文字方塊.getCode() && questionChoices == null)  {
			/* 不是文字方塊,需要輸入問題答案 */ 
			return MistakeMsg.QuestionMsg.Not_TextBox_MustQues_AndAns;
		}
		else if(questionType == QuestionType.文字方塊.getCode() && questionChoices != null) 
		{
			/* 文字方塊不需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.TextBox_MustQues_NotAns;
		}
		return "";
	}
	
	/* 新增問題 */
	@Override
	public void createQues(HttpSession session, Model model, 
			               Questionnaire questionnaire, 
			               String questionTitle,String questionChoices, 
			               int questionType, boolean mustKeyin) {
		
		@SuppressWarnings("unchecked")
		List<Question> questionlist = (List<Question>) session.getAttribute("questions");
		Question ques = new Question();

		// 設定問題標題
		ques.setQuestionTitle(questionTitle);
		// 設定問題回答
		ques.setQuestionChoices(questionChoices);
		// 設定問題種類
		ques.setQuestionType(questionType);
		// 設定是否為必須回答
		ques.setMustKeyin(mustKeyin);
		// 設定問題編號
		ques.setQuestionNumber(quesNumber);
		// 設定問卷編號
		ques.setQuestionnaireId(questionnaire);
		questionlist.add(ques);

		List<OftenUseQuestion> oftenList = commonQuestionDao.findAll();
		model.addAttribute("oftenUseQues", oftenList);

		// 問題編號遞增
		quesNumber++;		
	}
	
	/* 增加問題 */
	@Override
	public void editQues(HttpSession session, Model model, 
			               Questionnaire questionnaireId, 
			               String questionTitle,String questionChoices, 
			               int questionType, boolean mustKeyin) {
		
		@SuppressWarnings("unchecked")
		List<Question> questionlist = (List<Question>) session.getAttribute("questions");
		Question ques = new Question();
		
		// New新的流水號
		String questionID = UUID.randomUUID().toString();
		// 設定問題ID
		ques.setQuestionId(questionID);
		// 設定問題標題
		ques.setQuestionTitle(questionTitle);
		// 設定問題回答
		ques.setQuestionChoices(questionChoices);
		// 設定問題種類
		ques.setQuestionType(questionType);
		// 設定是否為必須回答
		ques.setMustKeyin(mustKeyin);
		// 設定問題編號
		ques.setQuestionNumber(quesNumber);
		// 設定問卷編號
		ques.setQuestionnaireId(questionnaireId);
		questionlist.add(ques);

		List<OftenUseQuestion> oftenList = commonQuestionDao.findAll();
		model.addAttribute("oftenUseQues", oftenList);

		// 問題編號遞增
		quesNumber++;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getQuestionList (HttpSession session) throws Exception{
		
		try {			
			List<Question> questionSessionList = (List<Question>) session.getAttribute("questions");
			if(questionSessionList == null || questionSessionList.isEmpty()) {
				return null;
			}
			return questionSessionList;
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Override
	public String questionToJson(Questionnaire questionnaireId) {
		Gson gson = new Gson();
		
		Type getType = new TypeToken<List<Question>>() {}.getType();
		
		List<Question> questionList = questionDao.findListByQuestionnaireId(questionnaireId);
		
		if(!questionList.isEmpty()) {
			return gson.toJson(questionList, getType);
		}
		return null;
	}
}
