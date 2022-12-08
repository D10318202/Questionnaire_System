package com.QuesSystem.ques.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.google.gson.Gson;

@Controller
public class QuestionnaireController implements Serializable {

	private static final long serialVersionUID = 1L;
		
	@Autowired
	private QuestionnaireDao questionnaireDao;
	
	@Autowired
	private QuestionDao questionDao;
	
	@Autowired
	private UserinfoDao userInfoDao;
	
	@Autowired
	private CommonQuestionDao commonQuestionDao;
	
	@Autowired
	private QuestionnaireService questionnaireService;
		
	@Autowired
	private AmountService amountService;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	private boolean isCreateMode;
	
	/* 後台內頁顯示 */
	@GetMapping(UrlPath.Path.URL_BACK_ADDQUESTIONNAIRE)
	public String addques(Model model, 			
			              HttpSession session, 
			              RedirectAttributes redirectAttrs,
			              @RequestParam(name = "ID", required = false) String questionnaireId) {
         /* 如果問卷ID不是空值的話 */
		 if (questionnaireId != null) {

			 /* 藉由問卷ID取得問卷 */
			Questionnaire questionnaire = questionnaireDao.findById(questionnaireId).get();
			
			/* 藉由問卷ID取得問題清單 */
			List<Question> quesList = questionDao.findListByQuestionnaireId(questionnaire);
			
			/* 把問卷內容顯示在前端畫面，並儲存到Session內 */
			List<Userinfo> useranswers = userInfoDao.findByQuestionnaireId(questionnaire);

			/* 把問卷內容顯示在前端畫面，並儲存到Session內 */
			model.addAttribute("changeques", questionnaire);
			session.setAttribute("changeques", questionnaire);

			/* 如果Session為空值的話 */
			if (session.getAttribute("questions") == null) {
				
				/* 把取得的問題清單儲存到Session內 */
				session.setAttribute("questions", quesList);
			}
			
			/* 如果使用者資訊及回答不為空的話 */
			if (!useranswers.isEmpty()) {
				
				/* 把useranswers的內容顯示在前端畫面 */
				model.addAttribute("useranswers", useranswers);
			}
			
			/* 藉由問卷ID取得所有回答(清單) */
			List<Answers> ansList = answerService.seperateAnswer(questionnaire);
			
			List<TotalAnswerVal> totalList = amountService.getTotalAnswers(quesList, ansList);
			model.addAttribute("totalanswerVal", totalList);

			List<OftenUseQuestion> oftenuseList = commonQuestionDao.findAll(); 
			model.addAttribute("OftenUseQues", oftenuseList);		    
			
			return UrlPath.Path.URL_BACK_ADDQUESTIONNAIRE;
		}
		else {		
				List<Question> questionlist = new ArrayList<>();
				session.setAttribute("questions", questionlist);
				
				return UrlPath.Path.URL_BACK_ADDQUESTIONNAIRE;
			}
	}
	
	/* 新增問卷 */
	@PostMapping(value = "/backAddquestionnaire")
	public String createQuestionnaire(Model model,
									  HttpSession session, 
									  @RequestParam("title") String questionnaireTitle,
									  @RequestParam("body") String questionnaireBody, 
									  @RequestParam("startDate") String startDate,
									  @RequestParam("endDate") String endDate, 
									  @RequestParam(name = "state", defaultValue = "0") boolean questionnaireStates) throws ParseException {			

		    
		/* 新增問卷 - 使用ErrorMsg方法 */
		String errorMsg = questionnaireService.ErrorMsg(questionnaireTitle, questionnaireBody, startDate, endDate);
		if(!errorMsg.isEmpty()) {
			model.addAttribute("errorMsg", errorMsg);
			return "backAddquestionnaire";
		}
		
		Questionnaire questionnaire = new Questionnaire(); // �Ыذݨ�����
		/* 設定問卷內容 */
		questionnaire.setQuestionnaireTitle(questionnaireTitle);		
		
		/* 時間格式轉換 */
		questionnaire.setQuestionnaireBody(questionnaireBody);
		
		/* 時間格式轉換 */
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = formatter.parse(startDate);
		
		/* 設定問卷開始日期 */
		questionnaire.setStartDate(sDate);
		Date eDate = formatter.parse(endDate);
		
		/* 設定問卷結束日期 */
		questionnaire.setEndDate(eDate);
		
		/* 設定問卷狀態 */
		questionnaire.setQuestionnaireStates(questionnaireStates);
		
		/* 把問卷資料存入Session */
		session.setAttribute("questionnaire", questionnaire);

		/* 尋找所有的常用問題 */
		List<OftenUseQuestion> oftenuseList = commonQuestionDao.findAll();
		/* 把oftenuseList傳送至前端顯示 */
		model.addAttribute("oftenUseQues", oftenuseList); 
		
		model.addAttribute("quesErrorMsg", MistakeMsg.QuestionnaireMsg.Save_Questionnaire_Title_Body_Date);
		
		/* 跳轉至後台新增問卷頁面 */
		return "backAddquestionnaire";
	}	
	
	@ResponseBody
	@GetMapping(value = {"/ViewOftenUseQuestion/{Id}" })
	public String ViewOftenUseQuestionnaire(Model model, 
			                                @PathVariable("Id") String Id) {
		Gson gson = new Gson();
		Optional<OftenUseQuestion> oftenusequestion = commonQuestionDao.findById(Id);
		if(!oftenusequestion.isEmpty()) {
			return gson.toJson(oftenusequestion.get());
		}
		return "notThing";		
	}
	
	/* 編輯問卷 */
	@PostMapping(value = {"/backAddquestionnaire" }, params = "createq")
	public String changeQuestionnaire(Model model,
			                          HttpSession session, 
			                          RedirectAttributes redirectAttrs,
			                          /* 問卷ID */ 
			                          @RequestParam(name = "ID", required = false) String questionnaireId,
			                          /* title = 用戶輸入的值 / 問卷標題 */ 
			                          @RequestParam("title") String questionnaireTitle,
			                          /* body = 用戶輸入的值 / 問卷內容 */ 
			                          @RequestParam("body") String questionnaireBody, 
			                          /* startDate = 用戶輸入的值 / 起始時間 */ 
			                          @RequestParam("startDate") String startDate,
			                          /* endDate = 用戶輸入的值 / 結束時間 */ 
			                          @RequestParam("endDate") String endDate, 
			                          /* state = 用戶輸入的值 / 問卷狀態 */ 
			                          @RequestParam(name = "state", defaultValue = "0") boolean questionnaireStates) throws ParseException {
		
		/* 取得問卷的Session */
		Questionnaire changeques = (Questionnaire) session.getAttribute("changeques");
		/* 取得QuestionnaireId(問卷ID) */
		String quesID = changeques.getQuestionnaireId();
		
		String errorMsg = questionnaireService.ErrorMsg(questionnaireTitle, questionnaireBody, startDate, endDate);
		if(!errorMsg.isEmpty()) {
			
			/* 把錯誤訊息顯示在前端 */
			model.addAttribute("errorMsg", errorMsg);
			/* 跳轉至(Url帶有問卷ID的)後台內頁 */
			return "redirect:/backAddquestionnaire?ID=" + quesID;
		}
		
		/* 設定問卷標題 */
		changeques.setQuestionnaireTitle(questionnaireTitle);
		
		/* 設定問卷內容 */
		changeques.setQuestionnaireBody(questionnaireBody);
		
		/* 時間格式轉換 */
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date sDate = formatter.parse(startDate);
		/* 設定問卷起始時間 */
		changeques.setStartDate(sDate);
		
		Date eDate = formatter.parse(endDate);
		/* 設定問卷結束時間 */
		changeques.setEndDate(eDate);
		
		/* 設定問題狀態 */
		changeques.setQuestionnaireStates(questionnaireStates);
		
		/* 把問卷編輯的資料存入Session內 */
		session.setAttribute("questionnaire", changeques);
		
		/* 跳轉至(Url帶有問卷ID的)後台內頁 */
		return "redirect:/backAddquestionnaire?ID=" + quesID;	
	}
	
	/* 新增問卷 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/backAddquestionnaire" }, params = "createquestion")
	public String createQues(Model model, 
			                 HttpSession session, 
			                 RedirectAttributes redirectAttrs) {
		
		//任何東西宣告都是object.所有的物件都繼承object
		Questionnaire questionnaire = (Questionnaire) session.getAttribute("questionnaire"); 
		if (questionnaire == null) {
			redirectAttrs.addFlashAttribute("quesErrorMsg", MistakeMsg.QuestionnaireMsg.Check_Enter_Questionnaire);
			return "redirect:/backAddquestionnaire";
		}
	
		/* 從Session中取得問題清單 */
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		boolean getMust = questionService.QuestionMust(questionList);

		if (getMust == false) {
			model.addAttribute("quesErrorMsg", MistakeMsg.QuestionMsg.Have_One_Must_Question);
			List<OftenUseQuestion> oftenList = commonQuestionDao.findAll();
			model.addAttribute("oftenUseQues", oftenList);
			return "backAddquestionnaire";
		}

		questionnaireDao.save(questionnaire);
		questionDao.saveAll(questionList);		
		

		redirectAttrs.addFlashAttribute("quesErrorMsg", MistakeMsg.QuestionnaireMsg.Save_Questionnaire_Success);
		return "redirect:/backListPage";
	}
	
	/* 編輯問卷 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/backAddquestionnaire" }, params = "editquestion")
	public String editQues(Model model, 
			               HttpSession session, 
			               RedirectAttributes redirectAttrs) {
		
		// 取得問卷Session
		Questionnaire changequesId = (Questionnaire) session.getAttribute("changeques");
		// 從Session內取得問卷ID
		String changequestionnaireId = changequesId.getQuestionnaireId();
		Questionnaire changeques = (Questionnaire) session.getAttribute("questionnaire");
	    
		if (changeques == null) {
			
			/* 尚未填寫問卷 */
			redirectAttrs.addFlashAttribute("quesErrorMsg", MistakeMsg.QuestionnaireMsg.Check_Enter_Questionnaire);
			return "redirect:/backAddquestionnaire?ID=" + changequestionnaireId;
		}
		
		/* 取得問卷ID */
		String quesId = changeques.getQuestionnaireId();
	
		/* 取得帶有問題清單的Session */
		List<Question> questionList = (List<Question>) session.getAttribute("questions");
		/* 判斷所有問題是否為必填 */
		boolean getMust = questionService.QuestionMust(questionList);

		/* 如果問題不是必填的話 */
		if (getMust == false) {
			
			/* 給予提示訊息 */
			model.addAttribute("quesErrorMsg", MistakeMsg.QuestionMsg.Have_One_Must_Question);
			/* 取得所有常用問題 */
			List<OftenUseQuestion> oftenList = commonQuestionDao.findAll();
			/* 把所有常用問題顯示於前端畫面中 */
			model.addAttribute("oftenUseQues", oftenList);
			/* 跳轉至(Url帶有問卷ID的)後台新增問卷頁面 */
			return "redirect:/backAddquestionnaire?ID=" + quesId;
		}

		questionnaireDao.save(changeques);
		for(Question question : questionList) {
			/* 取得QuestionId */
			String Id = question.getQuestionId();
			Optional<Question> questionOp = questionDao.findById(Id);
			
			/* 如果questionOp是空的,則儲存問題 */
			if(questionOp.isEmpty()) {
				questionDao.save(question);	
			}
			/* 若不是則取得Id */
			else {
				/* 若questionOp不是空的，則取得問題的所有資訊 */
				Question editQues = questionOp.get();
				/* 設定(編輯)問題標題 */
				editQues.setQuestionTitle(question.getQuestionTitle());
				/* 設定(編輯)問題回答 */
				editQues.setQuestionChoices(question.getQuestionChoices());
				/* 設定(編輯)問題類型 */
				editQues.setQuestionType(question.getQuestionType());
				/* 設定(編輯)問題是否為必填 */
				editQues.setMustKeyin(question.isMustKeyin());
				/* 把editQues內所設定(編輯)的資訊儲存至資料庫 */
				questionDao.save(editQues);
			}
		}

		redirectAttrs.addFlashAttribute("quesErrorMsg", MistakeMsg.QuestionnaireMsg.Save_Questionnaire_Success);
		return "redirect:/backListPage";
	}
	
	@ResponseBody
	@GetMapping(value = {"/loadEditQuestion/{count}"})
	public String getEditQuestion(Model model,
			                      HttpSession session,
			                      RedirectAttributes redirectAttrs,
			                      @PathVariable("count") int count) {
		
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		List<Question> queslist = (List<Question>) session.getAttribute("questions");
		Question result = queslist.get(count);
		
		return gson.toJson(result);
		
	}
}
