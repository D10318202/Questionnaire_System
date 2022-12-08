package com.QuesSystem.ques.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.QuesSystem.ques.service.ifs.OftenUseQuestionService;
import com.QuesSystem.ques.service.ifs.QuestionnaireService;

@Controller
public class LoginController implements Serializable {

	private static final long serialVersionUID = 1L;

	/*  */
	@GetMapping(UrlPath.Path.URL_FRONT_LOGIN)
	public String login(Model model) {
		return UrlPath.Path.URL_FRONT_LOGIN;
	}

}
