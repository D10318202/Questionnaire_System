package com.QuesSystem.ques.service.ifs;

import java.util.List;

import org.springframework.data.domain.Page;

import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.model.QuestionnaireInfo;

public interface AnswerService {

	/* 利用分頁取得填寫資料(後台) */
	public Page<Userinfo> getAnswersByPageList(int pageNum,int pageSize);
	
	public QuestionnaireInfo createQuestionnaireInfo(String userId);

	public List<Answers> seperateAnswer(Questionnaire questionnaireId);
}
