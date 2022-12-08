package com.QuesSystem.ques.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.service.ifs.QuestionService;
import com.QuesSystem.ques.service.ifs.UserinfoService;

@Controller
public class formController {
	
	@Autowired
	private UserinfoService userInfoService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private QuestionnaireDao questionnaireDao;

	@Autowired
	private QuestionDao questionDao;
	
	@GetMapping(UrlPath.Path.URL_FRONT_FORM)
	public String formPage(Model model,
			               HttpSession session,
			               @RequestParam(name = "ID", required = false) String questionnaireId) {
		
		Questionnaire questionnaire = questionnaireDao.findById(questionnaireId).get();
		
		List<Question> questionList = questionDao.findListByQuestionnaireId(questionnaire);
		
		model.addAttribute("questionnaires", questionnaire);
		model.addAttribute("questionInfo", questionList);
		session.setAttribute("questionnaireInfo", questionnaire);
		session.removeAttribute("userInfo");
		return UrlPath.Path.URL_FRONT_FORM;
	}

	/* 建立回答者資料 */
	@PostMapping(value="/formPage")
	public String createAnswer(HttpSession session,
			                   RedirectAttributes redirectAttrs,			                 
			                   @ModelAttribute Userinfo userInfo,			                
			                   @RequestParam("userName") String name, 
			                   @RequestParam("userPhone") String phone,
			                   @RequestParam("userEmail") String email, 
			                   @RequestParam("userAge") String age) throws Exception {
		
		Questionnaire questionnaire = (Questionnaire) session.getAttribute("questionnaireInfo");
		String quesId = questionnaire.getQuestionnaireId();
        String userId = UUID.randomUUID().toString();
        
        if(quesId == null) {
        	redirectAttrs.addFlashAttribute("userInfoErrorMsg", MistakeMsg.QuestionnaireMsg.QuestionnaireId_Is_Null);
        	return UrlPath.Path.URL_FRONT_LISTPAGE;
        }
        
        String errorMsg = userInfoService.ErrorMsg(name, phone, email, age);
		if(!errorMsg.isEmpty()) {
			redirectAttrs.addFlashAttribute("errorMsg", errorMsg);
			return "redirect:/formPage?ID=" + quesId;
		}
        
		userInfo.setName(name.trim());
		userInfo.setPhone(phone.trim());
		userInfo.setEmail(email.trim());
		userInfo.setAge(age.trim());
		userInfo.setUserId(userId);
		userInfo.setQuestionnaireId(questionnaire);
		session.setAttribute("userInfo", userInfo);	
							
	    return "redirect:/confirmPage?ID=" + quesId;

	}
		
	@ResponseBody
	@GetMapping(value = {"/loadQuestions/{questionnaireId}"})
	public String loadQuestions(Model model,
			                    @PathVariable("questionnaireId") Questionnaire questionnaireId) {
         return questionService.questionToJson(questionnaireId);
	}

}
