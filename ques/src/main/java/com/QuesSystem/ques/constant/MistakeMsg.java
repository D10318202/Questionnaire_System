package com.QuesSystem.ques.constant;

public class MistakeMsg {
	
	/* 問卷相關訊息 */
	public static class QuestionnaireMsg {
		
		/* 尚未輸入任何條件*/
		public static final String Not_Thing = "*你沒有輸入任何資訊.*";
		
		/* 搜尋文字不能小於兩個字*/
		public static final String Less_Then_Two_Words = "*關鍵字至少要有兩個字.*";
		
		/* 查詢問卷成功*/
		public static final String Search_Success = "*搜尋問卷成功.*";
		
		/* 尚未有這份問卷*/
		public static final String Not_Has_Questionnaire = "*沒有搜尋到這份問卷.*";
		
		/* 問卷刪除成功*/
		public static final String Questionnaire_Has_Deleted = "*問卷已被刪除.*";
		
		/* 請檢查是否有編輯問卷 */
		public static final String Check_Enter_Questionnaire = "*確認是否有編輯問卷.*";
		
		/* 新增問卷成功*/
		public static final String Save_Questionnaire_Success = "*新增問卷成功.*";
		
		/* 新增問卷標題.內容.起始結束日期成功*/
		public static final String Save_Questionnaire_Title_Body_Date = "*問卷標題.內容.起始結束日期新增成功.*";
	
		/* 問卷標題為空 */
		public static final String No_Title = "*問卷標題不能為空*";
		
		/* 問卷標題至少五個字 */
		public static final String Title_AtLastFive = "*問卷標題至少要有5個字*";
		
		/* 問卷內容為空 */
		public static final String No_Body = "*問卷內容不能為空*";
		
		/* 問卷內容小於20個字 */
		public static final String Body_AtLastTwenty = "*問卷內容至少要有20個字*";
		
		/* 起始日期為空 */
		public static final String No_StartTime = "*問卷起始日期不能為空*";
		
		/* 結束日期為空 */
		public static final String No_EndTime = "*問卷結束日期不能為空*";
		
		/* 問卷Id不能為空 */
		public static final String QuestionnaireId_Is_Null = "*問卷Id不能沒有值.*";
				
	}
	
	/* 問題.常用問題相關訊息 */
	public static class QuestionMsg {
		
		/* 問題標題為空 */
		public static final String No_QuesTitle = "*尚未填寫問題標題*";
		
		/* 問題標題至少三個字 */
		public static final String Title_AtLastThree = "*問題標題至少要有三個字*";
		
		/* 尚未輸入問題標題以及問題回答 */
		public static final String Not_Enter_AnyThing = "* 尚未輸入問題標題以及問題回答*";
		
		/* 常用問題建立完成 */
		public static final String Success_Save_OftenUse = "*新增常用問題完成.*";
		
		/* 尚未有這份常用問題*/
		public static final String Not_Has_OftenUse_Question = "*尚未有這份常用問題*";
		
		/* 未勾選要刪除的常用問題*/
		public static final String Not_Select_OftenUse_Question = "*未勾選要刪除的常用問題*";
		
		/* 常用問題刪除成功*/
		public static final String OftenUse_Question_Has_Deleted = "*常用問題刪除成功*";
		
		/* 搜尋文字不能小於兩個字*/
		public static final String Less_Then_Two_Words = "*關鍵字不能小於兩個字*";
		
		/* 單選方塊需要輸入問題和答案 / 複選方塊需要輸入問題和答案 */
		public static final String Must_QuesAndAns = "*單選方塊悍婦選方塊需要輸入問題和答案*";
		
		/* 單選方塊需要輸入問題,答案至少要有6個字 */
		public static final String Radio_Must_QuesAndAnsMustSix = "*單選方塊需要輸入問題,答案至少要有6個字*";
		
		/* 複選方塊需要輸入問題,答案至少要有8個字 */
		public static final String Check_Must_QuesAndAnsMustEight = "*複選方塊需要輸入問題,答案至少要有8個字*";
		
		/* 不是文字方塊,需要輸入問題答案 */
		public static final String Not_TextBox_MustQues_AndAns = "*不是文字方塊,需要輸入問題答案*";
		
		/* 文字方塊不需要輸入問題答案 */
		public static final String TextBox_MustQues_NotAns = "*文字方塊不需要輸入問題答案*";
		
		/* 至少要有一個必填項目 */
		public static final String Have_One_Must_Question = "*至少要有一個必填項目*";
		
		/* 加入問題成功*/
		public static final String Save_Question_Success = "*加入問題成功.*";
	}
	
	/* 回答者相關訊息 */
	public static class UserInfoMsg {
		
		/* 名字不能為空 */
		public static final String Name_Can_Not_Empty = "*尚未輸入名字.*";
		
		/* 名字不能少於兩個字 */
		public static final String Name_Can_Not_Less_Two = "*名字至少要有兩個字*";
		
		/* 年齡不能為空 */
		public static final String Age_Can_Not_Empty = "*尚未輸入年齡*";
		
		/* 年齡不能小於10歲 */
		public static final String Age_Can_Not_Less_Ten = "*年齡不能小於10歲*";
		
		/* 電話不能為空 */
		public static final String Phone_Can_Not_Empty = "*尚未輸入電話*";
		
		/* 電話不能少於10~11位數字 */
		public static final String Phone_Can_Not_Less_Ten_Eleven = "*電話號碼至少要有10碼或11碼*";
		
		/* 信箱不能為空 */
		public static final String Email_Can_Not_Empty = "*尚未輸入信箱*";
		
		/* 信箱須包含@ */
		public static final String Email_Must_Have_At = "*信箱須包含@*";
		
	}
}
