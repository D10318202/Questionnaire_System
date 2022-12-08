package com.QuesSystem.ques.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.repository.QuestionnaireDao;
import com.QuesSystem.ques.service.ifs.QuestionnaireService;

@Service
public class QuestionnaireSereviceImpl implements QuestionnaireService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private QuestionnaireDao questionnaireDao;

	public QuestionnaireDao getQuestionnaireDao() {
		return questionnaireDao;
	}

	public void setQuestionnaireDao(QuestionnaireDao questionnaireDao) {
		this.questionnaireDao = questionnaireDao;
	}

	@Override
	public List<Questionnaire> getQuestionnaireList() {
		List<Questionnaire> list = questionnaireDao.findAll();
		if (list.isEmpty()) {
			return new ArrayList<>();
		}
		for (Questionnaire item : list) {
			System.out.println("show lists.");
//			System.out.println(item.getQuestionnaireId());
//			System.out.println(item.getTitleId());
//			System.out.println(item.getTitle());
//			System.out.println(item.getBody());
//			System.out.println(item.getStartDate());
//			System.out.println(item.getEndDate());
		}
		return list;
	}

	/*
	 * 刪除問卷 使用questionnaireId
	 */
	@Override
	public void deleteQuestionnaire(String[] questionnaireId) {
		try {
			for (String string : questionnaireId) {
				questionnaireDao.deleteById(string);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/* 新增問卷防呆 */
	@Override
	public String ErrorMsg(String questionnaireTitle, String questionnaireBody, String startDate, String endDate) {
		/* 問卷標題防呆 */
		if (!StringUtils.hasText(questionnaireTitle)) {
			/* 問卷標題為空 */
			return MistakeMsg.QuestionnaireMsg.No_Title;
		} else if (questionnaireTitle.length() < 5) {
			/* 問卷標題至少五個字 */
			return MistakeMsg.QuestionnaireMsg.Title_AtLastFive;
		}

		/* 問卷內容防呆 */
		if (!StringUtils.hasText(questionnaireBody)) {
			/* 問卷內容為空 */
			return MistakeMsg.QuestionnaireMsg.No_Body;
		} else if (questionnaireBody.length() < 20) {
			/* 問卷內容小於20個字 */
			return MistakeMsg.QuestionnaireMsg.Body_AtLastTwenty;
		}

		/* 起始結束防呆 */
		if (!StringUtils.hasText(startDate)) {
			// 起始日期為空
			return MistakeMsg.QuestionnaireMsg.No_StartTime;
		} else if (!StringUtils.hasText(endDate)) {
			// 結束日期為空
			return MistakeMsg.QuestionnaireMsg.No_EndTime;
		}
		return "";
	}

	/* 分頁查詢前台 */
	@Override
	public Page<Questionnaire> getQuestionnaireByPageListFront(int pageNum, int pageSize) {
		// 1.使用 Sort.by()先進行排序
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		// 2.再做分頁
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		// 3.自訂義的 --> Page<> 的方法(列出不是唱用問題的問卷)
		Page<Questionnaire> questionnaires = questionnaireDao.findByPageableFront(pageable, true);
		// 4.回傳questionnaires
		return questionnaires;
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @param questionnaireTitle 問卷標題
	 * @return questionnaires
	 */
	@Override
	public Page<Questionnaire> searchQuestionnaireByQuesTitleFront(int pageNum, int pageSize,
			String questionnaireTitle) {
		/* 使用 Sort.by()先進行排序 */
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		/* 2.再做分頁 */
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		/* 3.自訂義的 --> Page<> 的方法(使用問卷標題搜尋) */
		Page<Questionnaire> questionnaires = questionnaireDao.findByQuesTitleFront(pageable, questionnaireTitle, true);
		/* 4.回傳questionnaires */
		return questionnaires;
	}

	/* 分頁查詢後台 */
	/**
	 * @param pageNum
	 * @param pageSize
	 * @return questionnaires
	 */
	@Override
	public Page<Questionnaire> getQuestionnaireByPageList(int pageNum, int pageSize) {
		/* 1.使用 Sort.by()先進行排序 */
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		/* 2.再做分頁 */
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		/* 3.自訂義的 --> Page<> 的方法(列出不是唱用問題的問卷) */
		Page<Questionnaire> questionnaires = questionnaireDao.findAll(pageable);
		/* 4.回傳questionnaires */
		return questionnaires;
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @param startDate 開始日期
	 * @param endDate   結束日期
	 * @return questionnaires
	 */
	@Override
	public Page<Questionnaire> searchQuestionnaireByAllTime(int pageNum, int pageSize, Date startDate, Date endDate) {
		/* 1.使用 Sort.by()先進行排序 */
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		/* 2.再做分頁 */
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		/* 3.自訂義的 --> Page<> 的方法(使用開始日起即結束日期搜尋) */
		Page<Questionnaire> questionnaires = questionnaireDao.findByStartDateAndEndDate(pageable, startDate, endDate);
		/* 4.回傳questionnaires */
		return questionnaires;
	}

	/**
	 * @param pageNum
	 * @param pageSize
	 * @param questionnaireTitle 問卷標題
	 * @return questionnaires
	 */
	@Override
	public Page<Questionnaire> searchQuestionnaireByQuesTitle(int pageNum, int pageSize, String questionnaireTitle) {
		/* 使用 Sort.by()先進行排序 */
		Order order = new Sort.Order(Sort.Direction.DESC, "createDate");
		/* 2.再做分頁 */
		Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(order));
		/* 3.自訂義的 --> Page<> 的方法(使用問卷標題搜尋) */
		Page<Questionnaire> questionnaires = questionnaireDao.findByQuesTitle(pageable, questionnaireTitle);
		/* 4.回傳questionnaires */
		return questionnaires;
	}
}
