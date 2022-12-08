package com.QuesSystem.ques.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.entity.OftenUseQuestion;
import com.QuesSystem.ques.enums.QuestionType;
import com.QuesSystem.ques.repository.CommonQuestionDao;
import com.QuesSystem.ques.service.ifs.OftenUseQuestionService;

@Service
public class OftenUseQuestionImpl implements OftenUseQuestionService {
	
	@Autowired
	private CommonQuestionDao commomQuestionDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/* 新增常用問題防呆 */
	@Override
	public String ErrorMsg(String Title, String Choices, int Type) {
		/* 問題標題防呆 */
		if (!StringUtils.hasText(Title)) {
			// 問題標題為空
			return MistakeMsg.QuestionMsg.No_QuesTitle;
		} else if (Title.length() < 3) {
			// 問題至少要有三個字
			return MistakeMsg.QuestionMsg.Title_AtLastThree;
		}

		/* 問題答案即問題種類防呆 */
		if (Type == QuestionType.單選方塊.getCode() && Choices == null) 
		{
			/* 單選方塊需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.Must_QuesAndAns;
		} 
		else if(Type == QuestionType.單選方塊.getCode() && Choices.length() < 6)
		{
			/* 單選方塊需要輸入問題,答案至少要有6個字 */
			return MistakeMsg.QuestionMsg.Radio_Must_QuesAndAnsMustSix;
		}
		
		if (Type == QuestionType.複選方塊.getCode() && Choices == null) 
		{
			/* 複選方塊需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.Must_QuesAndAns;
		} 
		else if(Type == QuestionType.複選方塊.getCode() && Choices.length() < 8)
		{
			/* 複選方塊需要輸入問題,答案至少要有8個字 */
			return MistakeMsg.QuestionMsg.Check_Must_QuesAndAnsMustEight;
		}
		
		if (Type != QuestionType.文字方塊.getCode() && Choices == null) 
		{
			/* 不是文字方塊,需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.Not_TextBox_MustQues_AndAns;
		}
		else if(Type == QuestionType.文字方塊.getCode() && Choices != null) 
		{
			/* 文字方塊不需要輸入問題答案 */
			return MistakeMsg.QuestionMsg.TextBox_MustQues_NotAns;
		}
		return "";
	}
	
	/**
	 * @param pageNum
	 * @param pageSize
	 * @return oftenuseQues
	 */
	@Override
	public Page<OftenUseQuestion> getOftenUseByPageList(int pageNum, int pageSize) {
		//1.使用 Sort.by()先進行排序
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
	    //2.再做分頁
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		//3.自訂義的 --> Page<> 的方法(列出不是唱用問題的問卷)
		Page<OftenUseQuestion> oftenuseQues = commomQuestionDao.findAll(pageable);
		//4.回傳oftenuseQues
		return oftenuseQues;
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @param oftenuseTitle 常用問題問卷標題
	 * @return oftenuseQues
	 */
	@Override
	public Page<OftenUseQuestion> searchOftenUseByoftenuseTitle(int pageNum, int pageSize, String Title) {
		//使用 Sort.by()先進行排序
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		//2.再做分頁
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		//3.自訂義的 --> Page<> 的方法(使用常用問題標題搜尋)
		Page<OftenUseQuestion> oftenuseQues = commomQuestionDao.findByoftenuseTitle(pageable, Title);
		//4.回傳oftenuseQues
		return oftenuseQues;
	}

	@Override
	public void deleteOftenUseQuestion(String[] Id) {
		try {
			for(String oftenUse : Id) {
				commomQuestionDao.deleteById(oftenUse);
				
			}
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		
	}
}
