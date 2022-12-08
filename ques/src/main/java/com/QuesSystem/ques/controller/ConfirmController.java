package com.QuesSystem.ques.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.repository.UserinfoDao;
import com.QuesSystem.ques.service.ifs.QuestionService;
import com.google.gson.Gson;

@Controller
public class ConfirmController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private UserinfoDao userInfoDao;
	
	@Autowired
	private QuestionnaireDao questionnaireDao;
	
	@GetMapping(UrlPath.Path.URL_FRONT_CONFIRM)
	public String confirmPage(Model model,
		                      HttpSession session,
				              HttpServletRequest request,
			                  @RequestParam(name = "ID", required = false) String questionnaireId) {
		Questionnaire questionnaire = questionnaireDao.findById(questionnaireId).get();
		
		List<Question> questionList = questionDao.findListByQuestionnaireId(questionnaire);
		
		model.addAttribute("questionnaires", questionnaire);
		model.addAttribute("questionInfo", questionList);
		
		return UrlPath.Path.URL_FRONT_CONFIRM;
	}

	@ResponseBody
	@GetMapping(value = { "/getQuestions/{questionnaireId}" })
	public String getQuestions(Model model, 
			                   @PathVariable("questionnaireId") Questionnaire questionnaireId) {
		return questionService.questionToJson(questionnaireId);
	}

	@PostMapping(value = { "/confirmPage" })
	public String saveAnswer(Model model, 
			                 HttpSession session, 
			                 RedirectAttributes redirectAttrs,
			                 HttpServletRequest request) throws UnsupportedEncodingException {
		Gson gson = new Gson();
		Userinfo userInfoSession = (Userinfo) session.getAttribute("userInfo");
		Questionnaire questionnaireId = userInfoSession.getQuestionnaireId();
		List<Question> questionList = questionDao.findListByQuestionnaireId(questionnaireId);
		List<Question> list = new ArrayList<>();
		for (Question question : questionList) {
			if (question.isMustKeyin() == true) {
				list.add(question);
			}
		}
		Cookie[] cookies = request.getCookies(); // 取得所有cookie
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("ansInfo")) { // 如果有符合名字的cookie
					String answer = URLDecoder.decode(cookie.getValue(), "UTF-8"); // 解碼
					List<Answers> targetList = new ArrayList<Answers>();
					Answers[] ans;
					ans = gson.fromJson(answer, Answers[].class);
					for (Answers a : ans) {
						targetList.add(a);
					}
					Integer targetId = null;
					for (Question ques : list) {
						targetId = ques.getQuestionNumber();
						boolean correct = false;
						for (Answers item : targetList) {
							if (list.size() > targetList.size()) {
								break;
							}
							if ((targetId + "").equals(item.getQuestionNumber())) {
								correct = true;
								break;
							}
						}
						if (!correct) {
							redirectAttrs.addFlashAttribute("alertMessage", "請檢查必填後再送出");
							return "redirect:/formPage?ID=" + questionnaireId;
						}
					}
					userInfoSession.setAnswer(answer);
					userInfoDao.save(userInfoSession);
				}
			}
		}
		redirectAttrs.addFlashAttribute("succesfulMessage", "作答成功!! 感謝您的填寫");
		return "redirect:/listPage";
	}
}
