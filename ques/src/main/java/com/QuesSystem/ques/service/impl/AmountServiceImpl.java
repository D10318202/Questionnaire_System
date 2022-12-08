package com.QuesSystem.ques.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.QuesSystem.ques.entity.Question;
import com.QuesSystem.ques.model.Answers;
import com.QuesSystem.ques.model.ChoicesInfo;
import com.QuesSystem.ques.model.TotalAnswerVal;
import com.QuesSystem.ques.model.TotalAnswerVal2;
import com.QuesSystem.ques.service.ifs.AmountService;

@Service
public class AmountServiceImpl implements AmountService {

	/* 後台統計頁 */
	@Override
	public List<TotalAnswerVal> getTotalAnswers(List<Question> quesList, 
			                                    List<Answers> ansList) {
		List<TotalAnswerVal> totalAnswerList = new ArrayList<>();
		
		for (Question question : quesList) {
			if (question.isMustKeyin() == true) {
				if (question.getQuestionType() == 2) {
					Map<String, Integer> totalMap = new HashMap<>();
					TotalAnswerVal totalAns = new TotalAnswerVal();
					totalAns.setTitle(question.getQuestionTitle() + "?(*必填)");
					totalMap.put("-資料不統計-", 0);
					totalAns.setChoicesMap(totalMap);
					totalAnswerList.add(totalAns);
				} else {
					TotalAnswerVal totalAnswer = new TotalAnswerVal();
					totalAnswer.setTitle(question.getQuestionTitle()+ "?(*必填)");

					Map<String, Integer> totalMap = new HashMap<>();
					int count = 0;
					List<String> totalString = new ArrayList<>();

					answerTrim(ansList, question.getQuestionNumber(), totalString);

					for (String string : totalString) {
						count = Collections.frequency(totalString, string);
						totalMap.put(string, count);
					}

					totalAnswer.setChoicesMap(totalMap);
					totalAnswerList.add(totalAnswer);
				}
			}
			else {
				if (question.getQuestionType() == 2) {
					Map<String, Integer> totalMap = new HashMap<>();
					TotalAnswerVal totalAns = new TotalAnswerVal();
					totalAns.setTitle(question.getQuestionTitle());
					totalMap.put("-資料不填寫-", 0);
					totalAns.setChoicesMap(totalMap);
					totalAnswerList.add(totalAns);
				} else {
					TotalAnswerVal totalAnswer = new TotalAnswerVal();
					totalAnswer.setTitle(question.getQuestionTitle());

					Map<String, Integer> totalMap = new HashMap<>();
					int count = 0;
					List<String> totalString = new ArrayList<>();

					answerTrim(ansList, question.getQuestionNumber(), totalString);

					for (String string : totalString) {
						count = Collections.frequency(totalString, string);
						totalMap.put(string, count);
					}

					totalAnswer.setChoicesMap(totalMap);
					totalAnswerList.add(totalAnswer);
				}
			}
		}
		return totalAnswerList;
	}

	@Override
	public void answerTrim(List<Answers> answerList, 
			               int questionNumber, 
			               List<String> resultList) {
		for (Answers answers : answerList) {
			if (answers.getQuestionNumber().equals(questionNumber + "")) {
				String[] ansArr = answers.getAnswer().split(";");
				for (String AnsSplit : ansArr) {
					if (!AnsSplit.equals("")) {
						resultList.add(AnsSplit.trim());
					}
				}
			}
		}
	}

	/* 前台統計頁 */
	@Override
	public List<TotalAnswerVal2> getTotals(List<Question> quesList, 
			                               List<Answers> ansList) {
		List<TotalAnswerVal2> totalAnswerList = new ArrayList<>();
		for (Question question : quesList) {
			if (question.getQuestionType() == 2) {

				Set<ChoicesInfo> set = new HashSet<>();
				TotalAnswerVal2 totalAnswerVal2 = new TotalAnswerVal2();
				totalAnswerVal2.setQuestionTitle(question.getQuestionTitle());

				ChoicesInfo choicesInfo = new ChoicesInfo();
				choicesInfo.setChoices("資料不統計");
				choicesInfo.setCount(0);
				set.add(choicesInfo);

				totalAnswerVal2.setChoicesInfo(set);

				totalAnswerList.add(totalAnswerVal2);
			} else {
				TotalAnswerVal2 totalAnswerVal2 = new TotalAnswerVal2();
				totalAnswerVal2.setQuestionTitle(question.getQuestionTitle());
				Set<ChoicesInfo> set = new HashSet<>();

				/* 初始值為0 */
				int count = 0;
				List<String> totalString = new ArrayList<>();

				answerTrim(ansList, question.getQuestionNumber(), totalString);
				Set<String> newWords = new HashSet<String>(totalString);

				for (String string : newWords) {
					count = Collections.frequency(totalString, string);
					ChoicesInfo choicesInfo = new ChoicesInfo();
					choicesInfo.setChoices(string);
					choicesInfo.setCount(count);
					set.add(choicesInfo);
				}
				totalAnswerVal2.setChoicesInfo(set);
				totalAnswerList.add(totalAnswerVal2);
			}
		}
		return totalAnswerList;
	}
}
