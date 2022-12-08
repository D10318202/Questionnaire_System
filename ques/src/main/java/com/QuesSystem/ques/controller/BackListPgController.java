package com.QuesSystem.ques.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.OftenUseQuestion;
import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.model.TotalAnswerVal;
import com.QuesSystem.ques.repository.CommonQuestionDao;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.repository.UserinfoDao;
import com.QuesSystem.ques.service.ifs.AmountService;
import com.QuesSystem.ques.service.ifs.AnswerService;
import com.QuesSystem.ques.service.ifs.QuestionService;
import com.QuesSystem.ques.service.ifs.QuestionnaireService;
import com.QuesSystem.ques.service.ifs.UserinfoService;

@Controller
public class BackListPgController {

	@Autowired
	private QuestionnaireService questionnaireService;
	
	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private UserinfoDao userinfoDao;
	
	@GetMapping(UrlPath.Path.URL_BACK_LISTPAGE)
	public String backListPage(Model model, 
			                   HttpSession session, 
			                   RedirectAttributes redirectAttrs,
			                   // 頁碼,不待此參數預設為0
			                   @RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			                   @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

		Page<Questionnaire> questionnaires = questionnaireService.getQuestionnaireByPageList(pageNum, pageSize);
		model.addAttribute("questionnaires", questionnaires);
		model.addAttribute("pageSize", pageSize);
		session.removeAttribute("questionnaire"); // ��l����session�M��
		session.removeAttribute("changeques");
		session.removeAttribute("questions");

		return UrlPath.Path.URL_BACK_LISTPAGE;
	}
	
	@GetMapping(value = { "/backListPage" }, params = "search")
	public String backListSearchKey(Model model, RedirectAttributes redirectAttrs,
			@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "title", required = false, defaultValue = "") String questionnaireTitle,
			@RequestParam(name = "startDate", required = false, defaultValue = "") String startDate,
			@RequestParam(name = "endDate", required = false, defaultValue = "") String endDate) throws ParseException {

		if (questionnaireTitle.isEmpty() && startDate.isEmpty() && endDate.isEmpty()) {
			
			redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Not_Thing);
			
			return "redirect:/backListPage";
		} else if (questionnaireTitle.length() < 2) {
			
			redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Less_Then_Two_Words);
			
			return "redirect:/backListPage";
		}

		Page<Questionnaire> questionnaires = null;

		if (startDate.isEmpty() && endDate.isEmpty()) {
			
			questionnaires = questionnaireService.searchQuestionnaireByQuesTitle(pageNum, 
					                                                             pageSize, 
					                                                             questionnaireTitle);
		} else if (!startDate.isEmpty() && !endDate.isEmpty()) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate = formatter.parse(startDate);
			Date eDate = formatter.parse(endDate);
			
			Calendar C = Calendar.getInstance();
			C.setTime(eDate);
			C.add(Calendar.DATE, 1);
			Date endDatePlus = C.getTime();
			
			questionnaires = questionnaireService.searchQuestionnaireByAllTime(pageNum, pageSize, sDate, endDatePlus);
		} else {
			redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Not_Thing);
			return "redirect:/backListPage";
		}

		model.addAttribute("questionnaires", questionnaires);
		redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Search_Success);
		return "backListPage";

	}

	@GetMapping(value = { "/backListPage" }, params = "delete")
	public String deleteByQuestionnaire(Model model, 
			                            RedirectAttributes redirectAttrs,
			                            @RequestParam(name = "ID", required = false) String[] questionnaireId) {
		if (questionnaireId == null) {
			redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Not_Has_Questionnaire);
			return "redirect:/backListPage";
		}
	
		questionnaireService.deleteQuestionnaire(questionnaireId);
		questionDao.deletebyQuestionnaireId(questionnaireId);
		userinfoDao.deletebyQuestionnaireId(questionnaireId);

		redirectAttrs.addFlashAttribute("WrongMessage", MistakeMsg.QuestionnaireMsg.Questionnaire_Has_Deleted);
		return "redirect:/backListPage";
	}
}
