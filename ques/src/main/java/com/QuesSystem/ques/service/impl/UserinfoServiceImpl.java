package com.QuesSystem.ques.service.impl;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.QuesSystem.ques.constant.MistakeMsg;
import com.QuesSystem.ques.entity.Userinfo;

import com.QuesSystem.ques.repository.UserinfoDao;
import com.QuesSystem.ques.service.ifs.UserinfoService;

@Service
public class UserinfoServiceImpl implements UserinfoService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserinfoDao userinfoDao;

//	@Override
//	public void deleteUserinfo(String[] questionnaireId) {		
//		try {
//			for (String string : questionnaireId) {
//				List<Userinfo> userlist = userinfoDao.findListByQuestionnaireId(string);
//				for (Userinfo userinfo : userlist) {
//					userinfoDao.delete(userinfo);
//				}		
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}			
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Userinfo> getuserInfoList (HttpSession session) throws Exception{
		
		try {			
			List<Userinfo> userInfoList = (List<Userinfo>) session.getAttribute("useranswers");
			if(userInfoList == null || userInfoList.isEmpty()) {
				return null;
			}
			userInfoList.sort(Comparator.comparing(Userinfo :: getCreateDate).reversed());
			return userInfoList;
			
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/* 回答者輸入資料檢查 */
	@Override
	public String ErrorMsg(String name, String phone, String email, String age) {
		
		/* 名字檢查 */
		if(!StringUtils.hasText(name)) {
			/* 名字不能為空 */
			return MistakeMsg.UserInfoMsg.Name_Can_Not_Empty;
		}		
		else if (name.length() < 2){
			/* 名字不能少於兩個字 */
			return MistakeMsg.UserInfoMsg.Name_Can_Not_Less_Two;
		}
		
		/* 年齡檢查 */
		if(!StringUtils.hasText(age)) {
			/* 年齡不能為空 */
			return MistakeMsg.UserInfoMsg.Age_Can_Not_Empty;
		}
		
		/* 電話檢查 */
		if(!StringUtils.hasText(phone)) {
			/* 電話不能為空 */
			return MistakeMsg.UserInfoMsg.Phone_Can_Not_Empty;
		}
		else if (phone.length() < 9 || phone.length() < 10) {
			/* 電話不能少於10~11位數字 */
			return MistakeMsg.UserInfoMsg.Phone_Can_Not_Less_Ten_Eleven;
		}
		
		/* 信箱檢查 */
		if(!StringUtils.hasText(email)) {
			/* 信箱不能為空 */
			return MistakeMsg.UserInfoMsg.Email_Can_Not_Empty;
		}
		else if(!email.contains("@")) {
			/* 信箱須包含@ */
			return MistakeMsg.UserInfoMsg.Email_Must_Have_At;
		}
		
		return "";
	}
}
