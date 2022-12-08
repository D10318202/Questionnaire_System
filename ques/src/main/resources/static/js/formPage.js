$(document).ready(function() {
	$(".inputRadio").click(function() {
		let mustValue = $(this).val();
		var mustID = $(this).attr("id");
		var arr = JSON.parse(sessionStorage.getItem('ansInfo')) || [];
		var inputAnswer = {
			questionNumber: mustID, answer: mustValue
		};
		if (arr.length > 0 && arr != null) {
			var sameKey = false;
			for (var i = 0; i < arr.length; i++) {
				if (arr[i].questionNumber == mustID) {
					arr[i] = inputAnswer;
					sameKey = true;
					break;
				}
			}
			if (sameKey == false)
				arr.push(inputAnswer);
		}
		else {
			arr.push(inputAnswer);
		}
		var val = JSON.stringify(arr);
		sessionStorage.setItem("ansInfo", val);		
		val = (val + '').replace(/[^!#$&-+\--:<-\[\]-~]/g, encodeURIComponent);
		document.cookie = "ansInfo" + "=" + val;
	});

	$(".inputCheck").click(function() {
		var isChecked = $(this).is(":checked");
		var chkArr = JSON.parse(sessionStorage.getItem('ansInfo')) || [];
		let mustValue = $(this).val();
		var mustID = $(this).attr("id");
		var inputAnswer = {
			questionNumber: mustID, answer: mustValue + ';'
		};
		if (isChecked == true) {
			if (chkArr != null && chkArr.length > 0) {
				var sameKey = false;
				for (var i = 0; i < chkArr.length; i++) {
					if (chkArr[i].questionNumber == inputAnswer.questionNumber) {
						chkArr[i].answer = chkArr[i].answer.concat(inputAnswer.answer);
						sameKey = true;
						break;
					}
				}
				if (sameKey == false)
					chkArr.push(inputAnswer);
			}
			else {
				chkArr.push(inputAnswer);
			}
			var val = JSON.stringify(chkArr);
			sessionStorage.setItem("ansInfo", val);
			val = (chkArr + '').replace(/[^!#$&-+\--:<-\[\]-~]/g, encodeURIComponent);
			document.cookie = "ansInfo" + "=" + val;
		}
		else {
			if (chkArr != null && chkArr.length > 0) {
				for (var i = 0; i < chkArr.length; i++) {
					if (chkArr[i].questionNumber == inputAnswer.key) {
						chkArr[i].answer = chkArr[i].answer.replace(inputAnswer.answer, "");
						break;
					}
				}
				var val = JSON.stringify(chkArr);     //陣列轉為JSON,填入sessionStorage
				sessionStorage.setItem("ansInfo", val);
				val = (val + '').replace(/[^!#$&-+\--:<-\[\]-~]/g, encodeURIComponent);
				document.cookie = "ansInfo" + "=" + val;
			}
		}
	});

	$('.txtArea').change(function() {
		var mustID = $(this).attr("id");
		var txtVal = $(this).val();
		var txtArr = JSON.parse(sessionStorage.getItem('ansInfo')) || [];
		var inputAnswer = {
			questionNumber: mustID, answer: txtVal
		};
		if (txtArr != null && txtArr.length > 0) {
			var sameKey = false;
			for (var i = 0; i < txtArr.length; i++) {
				if (txtArr[i].key == mustID) {
					txtArr[i] = inputAnswer;
					sameKey = true;
					break;
				}
			}
			if (sameKey == false)
				txtArr.push(inputAnswer);
		}
		else {
			txtArr.push(inputAnswer);
		}
		var val = JSON.stringify(txtArr);
		sessionStorage.setItem("ansInfo", val);
		val = (val + '').replace(/[^!#$&-+\--:<-\[\]-~]/g, encodeURIComponent);
		document.cookie = "ansInfo" + "=" + val;
		$('.txtArea').trigger('change');
	});

	var UserAns = JSON.stringify(sessionStorage.getItem('ansInfo'));

	if (UserAns !== null) {
		const pageUrl = new URL(window.location.href);
		var questionnaireId = pageUrl.searchParams.get("ID");

		$.ajax({
			url: "/loadQuestions/" + questionnaireId,
			type: "GET",
			dataType: 'json',
			success: function(result) {
				if (result !== null) {
					for (var i = 0; i < result.length; i++) {
						var question = result[i];

						for (var k = 0; k < UserAns.length; k++) {
							if (question.questionType == 0) {
								if (question.questionId == UserAns[k].key) {
									$('[id="' + UserAns[k].questionNumber + '"][value="' + UserAns[k].answer + '"]').attr('checked', true);
								}
							} else if (question.questionType == 1) {
								if (question.questionId == UserAns[k].key) {

									var splitAnswer = UserAns[k].value.split(';');
									//檢查多選選項
									for (var l = 0; l < splitAnswer.length; l++) {
										$('[id="' + UserAns[k].questionNumber + '"][value="' + splitAnswer[l] + '"]').prop("checked", true);
									}
								}
							} else {
								if (question.questionId == UserAns[k].key) {
									$('[id="' + UserAns[k].questionNumber + '"]').val(UserAns[k].answer);
								}
							}
						}
					}
				}
				else {
					alert("問題發生!! 請重新嘗試!");
				}
			}
		});
	}
})