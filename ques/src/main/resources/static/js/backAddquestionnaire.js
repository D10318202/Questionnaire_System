function select() {
	var Id = document.getElementById('selectOftenUse').value;
	$.ajax({
		url: "/ViewOftenUseQuestion/" + Id,
		type: "GET",
		success: function(often) {
			if (often == "notThing") {
				$("#title").val("");
				$("#selectType").val(0);
				$("#Must").prop("checked", false);
				$("#answer").val("");
			}
			var often = JSON.parse(often);
			$("#title").val(often.questionTitle);
			$("#selectType").val(often.questionType);
			if (often.mustKeyin == 1) {
				$("#Must").prop("checked", true);
			}
			else {
				$("#Must").prop("checked", false);
			}
			$("#answer").val(often.questionChoices);
		},
		error: function(xhr, ajaxOptions, thrownError) {
			alert("通訊失敗，請聯絡管理員。");
		}
	});
}

function ConfirmDelete() {
	return confirm("Are you sure you want to delete?");
}

$(document).ready(function() {
	$(".detail").click(function() {
		var id = $(this).attr("id");
		$('#formAnswers').removeClass('active show');
		$('#formAnswers-detail').addClass('active show');
		document.getElementById("questionnaireInfo").innerHTML = " ";
		$.ajax({
			url: "/loadAnswerDetail/" + id,
			type: "GET",
			dataType: 'json',
			success: function(result) {
				$("#txtname").val(result.name);
				$("#txtphone").val(result.phone);
				$("#txtemail").val(result.email);
				$("#txtage").val(result.age);
				$("#txtTime").text("填寫時間  :   " + result.createDate);
			},
			error: function(xhr, ajaxOptions, thrownError) {
				alert("通訊失敗，請聯絡管理員。");
			}
		});

		$.ajax({
			url: "/loadAnswerInfo/" + id,
			type: "GET",
			dataType: 'json',
			success: function(result) {
				for (var i = 0; i < result.questions.length; i++) {
					var question = result.questions[i];

					var title = `<h5>${i + 1}：${question.questionTitle}</h5>`;
					if (question.mustKeyin == true) {
						title += "<p style='color: red'>(*必填)</p>";
					}

					// 單選方塊
					if (question.questionType == 0) {
						var choices = (question.questionChoices).split(";");
						for (var j = 0; j < choices.length; j++) {
							title += `<input type="radio" class="ansRadio" id="${question.questionNumber}" 
							value = ${choices[j]} />${choices[j]}</br>`;
						}
					}
					//複選方塊
					else if (question.questionType == 1) {
						var choices = (question.questionChoices).split(";");
						for (var j = 0; j < choices.length; j++) {
							title += `<input type="checkbox" class="ansChb" id="${question.questionNumber}" 
						    value = ${choices[j]} />${choices[j]}</br>`;
						}
					}
					//文字方塊
					else {
						title += `<input type="text" id="${question.questionNumber}" placeholder="請在此做答"></input>`;
					}
					title += `<hr class="invisible"/>`;
					title += `</br></div >`;
					$("#questionnaireInfo").append(title);
				}

				for (var i = 0; i < result.questions.length; i++) {
					var question = result.questions[i];
					for (var k = 0; k < result.answers.length; k++) {
						var answer = result.answers[k];
						if (question.questionType == 0) {
							if (question.questionNumber == answer.questionNumber) {
								$('[id="' + answer.questionNumber + '"][value="' + answer.answer + '"]').attr('checked', true);
							}
						} else if (question.questionType == 1) {
							if (question.questionNumber == answer.questionNumber) {
								var splitAnswer = answer.answer.split(';');
								for (var l = 0; l < splitAnswer.length; l++) {
									$('[id="' + answer.questionNumber + '"][value="' + splitAnswer[l] + '"]').prop("checked", true);
								}
							}
						} else {
							if (question.questionNumber == answer.questionNumber) {
								$('[id="' + answer.questionNumber + '"]').val(answer.answer);
							}
						}
					}
				}
			},
			error: function(xhr, ajaxOptions, thrownError) {
				alert("通訊失敗，請聯絡管理員。");
			}
		});
	})
})