package com.QuesSystem.ques.service.ifs;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.QuesSystem.ques.entity.Userinfo;

public interface UserinfoService {

//	public void deleteUserinfo(String[] questionnaire);
	
	public List<Userinfo> getuserInfoList (HttpSession session) throws Exception;
	
	/* 回答者資料防呆 */
	public String ErrorMsg(String name, String phone, String email, String age); 
}
