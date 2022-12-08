package com.QuesSystem.ques.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuesSystem.ques.entity.Questionnaire;

@Transactional
@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire, String> {
	
	/* 問卷列表搜尋
	 * 使用questionnaireId
	 */
	public List<Questionnaire> findListByQuestionnaireId(String questionnaireId);
	
	/* 問卷搜尋 v
	 * 使用questionnaireId
	 */
	@Query("select q from Questionnaire q where q.questionnaireId = :inputQuesId")
	public void findByQuestionnaireId(@Param("inputQuesId") String questionnaireId);
	
	/* 問卷搜尋
	 * 使用questionnaireTitle
	 */
	@Query("select q from Questionnaire q where q.questionnaireTitle = :inputQuesTitle")
	public void findByQuestionnaireTitle(@Param("inputQuesTitle") String questionnaireTitle);		
		
	/* 問卷搜尋(List)
	 * 使用questionnaireStates = 1
	 * 關閉 = 0, 已啟用 = 1
	 */
	@Query("select q from Questionnaire q where q.questionnaireStates = :inputQuesStates")
	public List<Questionnaire> findByQuesstates(@Param("inputQuesStates") boolean questionnaireStates);
	
	/* �R���ݨ�
	 * �ϥ�questionnaireId
	 */
	@Modifying
	@Query("delete from Questionnaire q where q.questionnaireId = :inputQuesId")
	public void deletebyQuestionnaireId(@Param("inputQuesId") String questionnaireId);  //更改
	
	
	/*
	 * 搜尋功能前台 v
	 */
	@Query("select q from Questionnaire q where q.questionnaireStates = :inputQuesStates")
	public Page<Questionnaire> findByPageableFront(Pageable pageable,
			                                  @Param("inputQuesStates") boolean questionnaireStates);
	
	@Query("select q from Questionnaire q where q.questionnaireTitle like %:title%"
			+ " and q.questionnaireStates = :inputQuesStates")
	public Page<Questionnaire> findByQuesTitleFront(Pageable pageable,
			                                        @Param("title") String questionnaireTitle,
			                                        @Param("inputQuesStates") boolean questionnaireStates);
	
	/*
	 * 搜尋功能後台 v
	 */	
	@Query("select q from Questionnaire q where q.questionnaireTitle like %:title%")
	public Page<Questionnaire> findByQuesTitle(Pageable pageable,
			                                   @Param("title") String questionnaireTitle);
	
	@Query("select q " + "from Questionnaire q"
		    + " where q.endDate is null and q.startDate >= :startDate and q.startDate < :endDatePlus "
			+ " or q.startDate >= :startDate and q.endDate < :endDatePlus")
	public Page<Questionnaire> findByStartDateAndEndDate(Pageable pageable, 
			                                             @Param("startDate") Date startDate, 
			                                             @Param("endDatePlus") Date endDatePlus);

}
