package com.QuesSystem.ques.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;
import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.model.QuestionnaireInfo;
import com.QuesSystem.ques.repository.QuestionDao;
import com.QuesSystem.ques.repository.UserinfoDao;
import com.QuesSystem.ques.service.ifs.AnswerService;
import com.google.gson.Gson;

@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private UserinfoDao userInfoDao;

	@Autowired
	private QuestionDao questionDao;

	/* 分頁查詢填寫資料 */
	@Override
	public Page<Userinfo> getAnswersByPageList(int pageNum, int pageSize) {
		// 1.使用 Sort.by()先進行排序
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		// 2.再做分頁
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		// 3.自訂義的 --> Page<> 的方法(列出不是常用問題的問卷)
		Page<Userinfo> useranswers = userInfoDao.findAll(pageable);
		// 4.回傳useranswers
		return useranswers;
	}

	// ??
	@Override
	public QuestionnaireInfo createQuestionnaireInfo(String userId) {
		Gson gson = new Gson();
		QuestionnaireInfo questionnaireResult = new QuestionnaireInfo();
        List<Answers> UserAnsList = new ArrayList<Answers>();
		Questionnaire questionnaireId;
		Answers[] ans;
		Optional<Userinfo> userinfoOp = userInfoDao.findById(userId);
		if (!userinfoOp.isEmpty()) {
			Userinfo userInfo = userinfoOp.get();
			questionnaireId = userInfo.getQuestionnaireId();
			List<Question> quesList = questionDao.findListByQuestionnaireId(questionnaireId);
			questionnaireResult.setQuestions(quesList);

			ans = gson.fromJson(userInfo.getAnswer(), Answers[].class);
			for (Answers a : ans) {
				UserAnsList.add(a);
			}
			questionnaireResult.setAnswers(UserAnsList);

		}
		return questionnaireResult;
	}
	
	@Override
	public List<Answers> seperateAnswer(Questionnaire questionnaireId){
		Gson gson = new Gson();
		List<Answers> UserAnsList = new ArrayList<Answers>();
		Answers[] ans;
		List<Userinfo> userInfoList = userInfoDao.findByQuestionnaireId(questionnaireId);
		for(Userinfo UserInfo : userInfoList) {
			ans = gson.fromJson(UserInfo.getAnswer(), Answers[].class);
			for(Answers answers : ans) {
			   UserAnsList.add(answers);
			}
		}		
		return UserAnsList;		
	}
}