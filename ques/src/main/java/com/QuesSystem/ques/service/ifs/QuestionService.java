package com.QuesSystem.ques.service.ifs;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;

public interface QuestionService {

	public boolean QuestionMust(List<Question> list);
	
	/* 刪除問題
	 * 使用questionnaireId
	 */
	public void deleteQuestions(String[] questionnaire);
	
	/* 新增問題防呆 - 在QuestionController裡的59行被使用*/
	public String ErrorMsg(String questionTitle, 
			               String questionChoices, 
			               int questionType);
	
	/* 新增問題 - 在QuestionController裡的74.94行被使用*/
	public void createQues(HttpSession session, 
                           Model model,
                           Questionnaire questionnaire,
                           @RequestParam("questionTitle") String questionTitle,
                           @RequestParam("questionChoices") String questionChoices, 
                           @RequestParam("questionType") int questionType,
                           @RequestParam(name = "mustKeyin", defaultValue = "0") boolean mustKeyin);
	/* 增加問題 */
	public void editQues(HttpSession session, 
			             Model model, 
			             Questionnaire questionnaire, 
			             String questionTitle,
			             String questionChoices, 
			             int questionType, 
			             boolean mustKeyin);
	
	public List<Question> getQuestionList (HttpSession session) throws Exception;
	
	public String questionToJson(Questionnaire questionnaireId);
}
