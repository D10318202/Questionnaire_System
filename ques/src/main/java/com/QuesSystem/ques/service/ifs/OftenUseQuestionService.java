package com.QuesSystem.ques.service.ifs;

import org.springframework.data.domain.Page;

import com.QuesSystem.ques.entity.OftenUseQuestion;

public interface OftenUseQuestionService {
	
	public void deleteOftenUseQuestion(String[] Id);
	
	/* 新增常用問題防呆 - 在QuestionController裡的61行被使用*/
	public String ErrorMsg(String Title, String Choices, int Type);

	/**
	 * @param pageNum
	 * @param pageSize
	 * @return oftenuseQues
	 */
	public Page<OftenUseQuestion> getOftenUseByPageList(int pageNum, int pageSize);

	/**
	 * @param pageNum
	 * @param pageSize
	 * @param oftenuseTitle 常用問題問卷標題
	 * @return oftenuseQues
	 */
	public Page<OftenUseQuestion> searchOftenUseByoftenuseTitle(int pageNum, int pageSize, String Title);
	
	
}
