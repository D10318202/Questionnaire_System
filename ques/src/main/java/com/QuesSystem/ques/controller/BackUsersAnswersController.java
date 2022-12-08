package com.QuesSystem.ques.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.model.QuestionnaireInfo;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.repository.UserinfoDao;
import com.QuesSystem.ques.service.ifs.AnswerService;
import com.google.gson.Gson;

@Controller
public class BackUsersAnswersController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UserinfoDao userInfoDao;

	@Autowired
	private AnswerService answerService;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@ResponseBody
	@GetMapping(value = { "/loadAnswerInfo/{id}" })
	public String loadAnswerInfo(Model model, 
			                     @PathVariable("id") String userId) {
		Gson gson = new Gson();
		QuestionnaireInfo questionnaireInfo = answerService.createQuestionnaireInfo(userId);
		return gson.toJson(questionnaireInfo);

	}
	
	@ResponseBody
	@GetMapping(value = {"/loadAnswerDetail/{id}"})
	public String loadAnswerDetail(Model model,
			                       @PathVariable("id")String userId) {
		Gson gson = new Gson();
        Optional<Userinfo> userinfoOp = userInfoDao.findById(userId);
		if(!userinfoOp.isEmpty()) {
			Userinfo userinfo = userinfoOp.get();
			return gson.toJson(userinfo);
		}
		return "notThing";
	}

	@GetMapping(value="/answerlisttoCSV")
	public void exportToCSV(HttpSession session,
			                HttpServletResponse response) throws IOException {
		
		response.setContentType("text/csv");
		response.setCharacterEncoding("UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=questionnaire_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		Questionnaire changeQues = (Questionnaire) session.getAttribute("changeques");
		List<Userinfo> list = userInfoDao.findByQuestionnaireId(changeQues);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] titles = new String[] { "UserId", "Age", "createDate", "Email", "Name", "Phone", "QuestionnaireId",
				"Answer"};
		String[] propertys = new String[] { "userId", "age", "createDate", "email", "name", "phone", "questionnaireId", 
				"answer"};

		csvWriter.writeHeader(titles);

		for (Userinfo userInfo : list) {
			csvWriter.write(userInfo, propertys);
		}
		csvWriter.close();
		response.setContentType("text/csv");
	}

}
