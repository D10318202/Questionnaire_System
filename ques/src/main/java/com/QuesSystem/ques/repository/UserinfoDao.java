package com.QuesSystem.ques.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.QuesSystem.ques.entity.Questionnaire;
import com.QuesSystem.ques.entity.Userinfo;

@Transactional
@Repository
public interface UserinfoDao extends JpaRepository<Userinfo, String>{
	
	@Query("select user from Userinfo user where user.questionnaireId = :questionnaireId")
	public List<Userinfo> findListByQuestionnaireId(@Param("questionnaireId")String questionnaireId);
	
	/* 顯示問題(使用questionnaireId) - WebController裡的169行被使用 */
	public List<Userinfo> findByQuestionnaireId(Questionnaire questionnaireId);
	
	/* ���o�^���H
	 * �ϥ�userId
	 */
	@Query("select user from Userinfo user where user.userId = :inputUserId")
	public void findbyUserId(@Param("inputUserId") String userId);
	
	/* �R���^���H
	 * �ϥ�userId
	 */
	@Query("delete from Userinfo user where user.userId = :inputUserId")
	public void deletebyUserId(@Param("inputUserId") String userId);
	
	@Modifying
	@Query("delete from Userinfo user where user.questionnaireId = :inputQuesId")
	public void deletebyQuestionnaireId(@Param("inputQuesId")String[] questionnaireId);
	
//	@Query("select user from Userinfo user where user.userId = :inputUserId")
//	public Page<Userinfo> findAllByUserId(@Param("inputUserId")String userId, 
//			                              Pageable pageable);
}
