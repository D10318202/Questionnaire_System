package com.QuesSystem.ques.controller;

import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.constant.UrlPath;
import com.QuesSystem.ques.entity.OftenUseQuestion;
import com.QuesSystem.ques.repository.CommonQuestionDao;
import com.QuesSystem.ques.service.ifs.OftenUseQuestionService;
import com.QuesSystem.ques.service.ifs.QuestionService;

@Controller
public class OftenUseController {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private OftenUseQuestionService oftenUseQuestionService;

    @Autowired
    private CommonQuestionDao commonQuestionDao;
    
    @GetMapping(UrlPath.Path.URL_BACK_OFTENUSE)
	public String oftenUse(Model model, 
			               HttpSession session, 
			               RedirectAttributes redirectAttrs,
			               
			               /* 頁碼,不帶此參數預設為0 */
			               @RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
			               
			               /* 單一頁面顯示10筆問卷清單 */
			               @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
		
		/* 這邊要跟th:each{ }裡的命名一致 */
		Page<OftenUseQuestion> oftenuses = oftenUseQuestionService.getOftenUseByPageList(pageNum, pageSize);
		model.addAttribute("oftenuses", oftenuses);
		model.addAttribute("pageSize", pageSize);
		session.removeAttribute("oftenuses");
		session.removeAttribute("changeoften");

		return UrlPath.Path.URL_BACK_OFTENUSE;
	}
    
    @GetMapping(UrlPath.Path.URL_BACK_ADDOFTENUSE)
	public String addOftenUse(Model model, 
			                  HttpSession session,
			                  @RequestParam(name = "ID", required = false) String Id) {
		
		if(Id == null) {
			session.removeAttribute(Id);
			return UrlPath.Path.URL_BACK_ADDOFTENUSE;
		}
		
		OftenUseQuestion oftenUseQuestion = commonQuestionDao.findById(Id).get();
		model.addAttribute("changeoften", oftenUseQuestion);
		session.setAttribute("changeoften", oftenUseQuestion);
		
		return UrlPath.Path.URL_BACK_ADDOFTENUSE;
	}
    
    /* 新增常用問題 */
	@PostMapping(value = { "/backAddOftenUse" })
	public String createOftenUse(Model model, RedirectAttributes redirectAttrs,
			                     @RequestParam("Title") String Title,
			                     @RequestParam("Choices") String Choices, 
			                     @RequestParam("Type") int Type,
			                     @RequestParam(name = "mustKeyin", defaultValue = "0") boolean mustKeyin) throws ParseException {

		// 如果常用問題的標題及回答皆為空
		if (Title.isEmpty() && Choices.isEmpty()) {	
			
			// 給予提示訊息，並重新導向(後台)新增常用問題的頁面
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Enter_AnyThing);
			return "redirect:/backAddOftenUse";
		}
				
		try {		
			
			/* 常用問題防呆 */
			String ErrorMsg = oftenUseQuestionService.ErrorMsg(Title, Choices, Type);
			if(!ErrorMsg.isEmpty()) {
				model.addAttribute("ErrorMsg", ErrorMsg);
				return "backAddOftenUse";
			}
			
		    OftenUseQuestion often = new OftenUseQuestion();
		    /* 設定問題標題 */
		    often.setTitle(Title);
		    /* 設定問題回答 */
		    often.setChoices(Choices);
		    /* 設定問題種類 */
		    often.setType(Type);
		    /* 設定是否為必須回答 */
		    often.setMustKeyin(mustKeyin);
		    commonQuestionDao.save(often);
		}
		catch (Exception e){
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Enter_AnyThing);
		    return "redirect:/backAddOftenUse";
		}
				
		redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Success_Save_OftenUse);
		return "redirect:/backOftenUse";
	}
	
	/* 編輯常用問題 */
	@PostMapping(value = { "/backAddOftenUse" }, params = "edit")
	public String editQuestion(Model model, 
			                   RedirectAttributes redirectAttrs,
			                   HttpSession session,
			                   @RequestParam(name = "ID", required = false) String Id,
			                   @RequestParam("Title") String Title,
			                   @RequestParam("Choices") String Choices, 
			                   @RequestParam("Type") int Type,
			                   @RequestParam(name = "mustKeyin", defaultValue = "0") boolean mustKeyin) throws ParseException {
        OftenUseQuestion changeoften = (OftenUseQuestion) session.getAttribute("changeoften");
        String oftenId = changeoften.getId();
        
		/* 常用問題防呆 */
		String ErrorMsg = oftenUseQuestionService.ErrorMsg(Title, Choices, Type);
		if(!ErrorMsg.isEmpty()) {
			model.addAttribute("ErrorMsg", ErrorMsg);
			return "rediredt:/backAddOftenUse?ID=" + oftenId;
		}		
		
		try {		
		    /* 設定問題標題 */
			changeoften.setTitle(Title);
			/* 設定問題回答 */
			changeoften.setChoices(Choices);
			/* 設定問題種類 */
			changeoften.setType(Type);
		    /* 設定是否為必須回答 */
			changeoften.setMustKeyin(mustKeyin);
		    commonQuestionDao.save(changeoften);
		}
		catch (Exception e){
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Enter_AnyThing);
		    return "redirect:/backAddOftenUse";
		}
				
		redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Success_Save_OftenUse);
		return "redirect:/backOftenUse";
	}
	
	/* 刪除常用問題 */
	@GetMapping(value = "/backOftenUse", params = "delete")
	public String deleteByOftenUseQues(Model model,
			                           RedirectAttributes redirectAttrs,			
			                           @RequestParam(name="Id", required = false) String[] Id) {
		// 如果常用問題ID是空值的話
		if(Id == null) {
			
			// 給予提示訊息，並跳轉至後台常用問題管理頁面
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Has_OftenUse_Question);
			return "redirect:/backOftenUse";
		}
		
		// 常用問題ID有值的話，則使用ID進行刪除
		oftenUseQuestionService.deleteOftenUseQuestion(Id);
		
		// 給予提示訊息，並跳轉至後台常用問題管理頁面
		redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.OftenUse_Question_Has_Deleted);
		return "redirect:/backOftenUse";
	}
	
	/* 搜尋常用問題 */
	@GetMapping(value = { "/backOftenUse" }, params = "search")
	public String backOftenUseListSearchKey(Model model,
                                            RedirectAttributes redirectAttrs,
                                            @RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
                                            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            @RequestParam(name = "title", required = false, defaultValue = "") String Title) {
		
		// 如果常用問題的標題為空的話
		if(Title.isEmpty()) {
			
			// 給予提示訊息，並跳轉至常用問題管理頁面
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Select_OftenUse_Question);
			return "redirect:/backOftenUse";
		}
		// 如果常用問題的標題長度少於2個字的話
		else if(Title.length() < 2) {
			
			// 給予提示訊息，並跳轉至常用問題管理頁面
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Less_Then_Two_Words);
			return "redirect:/backOftenUse";
		}

		
		Page<OftenUseQuestion> oftenuses = null;
		
		// 如果常用問題的標題不是空的話
		if(!Title.isEmpty()) {
			
			// 藉由標題搜尋常用問題
			oftenuses = oftenUseQuestionService.searchOftenUseByoftenuseTitle(pageNum, pageSize, Title);
		}
		else{
			
			// 給予提示訊息，並跳轉至常用問題管理頁面
			redirectAttrs.addFlashAttribute("oftenUseMsg", MistakeMsg.QuestionMsg.Not_Select_OftenUse_Question);
			return "redirect:/backOftenUse";
		}
		
		// 把oftenuses傳送到前端顯示
		model.addAttribute("oftenuses", oftenuses);
		// 跳轉至後台常用問題管理頁面
		return UrlPath.Path.URL_BACK_OFTENUSE;
		
	}
		
}
