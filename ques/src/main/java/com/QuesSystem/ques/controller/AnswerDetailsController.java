package com.QuesSystem.ques.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.model.TotalAnswerVal2;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.service.ifs.AmountService;
import com.QuesSystem.ques.service.ifs.AnswerService;
import com.google.gson.Gson;

@Controller
public class AnswerDetailsController {
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private QuestionnaireDao questionnaireDao;
	
	@Autowired
	private AmountService amountService;
	
	@Autowired
	private AnswerService answerService;
	
	@GetMapping(UrlPath.Path.URL_FRONT_ANSWERDETAIL)
	public String answerDetail(Model model,
			                   @RequestParam(name = "ID", required = false) String questionnaireId) {
		Questionnaire questionnaire = questionnaireDao.findById(questionnaireId).get();		
		model.addAttribute("questionnaires", questionnaire);
		
		return UrlPath.Path.URL_FRONT_ANSWERDETAIL;
	}
	
	@ResponseBody
	@GetMapping(value = {"/loadTotals/{questionnaireId}"})
    public String CountTotals(Model model,
    		                  @PathVariable("questionnaireId") Questionnaire questionnaireId) {
		Gson gson = new Gson();
		
		List<Question> questionList = questionDao.findListByQuestionnaireId(questionnaireId);
        List<Answers> ansList = answerService.seperateAnswer(questionnaireId);
		List<TotalAnswerVal2> totalAnswerVal2List = amountService.getTotals(questionList, ansList);
		return gson.toJson(totalAnswerVal2List);
		
	} 
}
