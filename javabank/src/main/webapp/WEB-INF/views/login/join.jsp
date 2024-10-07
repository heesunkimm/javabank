<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="css/reset.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="js/script.js"></script>
<title>javabank_Join</title>
</head>
<body>
	<section id="join">
        <form name="joinForm" action="/join" method="post">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>
            
            <div class="input_box">
                <div class="info_box">
                    <label>
                        <input type="text" name="userName" placeholder="이름" maxlength="10"; required>
                    </label>
                    <label>
                        <input type="text" name="userBirth" placeholder="생년월일 ex&#41;2024-10-01" maxlength="10" required>
                    </label>
                    <div class="tel_box">
                        <label>
                            <input type="text" name="userTel" placeholder="핸드폰번호 ex&#41;010-0000-0000" maxlength="13" required>
                        </label>
                    </div>
                    <div class="email_box">
                        <label>
                            <input type="text" name="email01" placeholder="이메일" maxlength="16" required>
                        </label>
                        <select name="email02">
                            <option value="@naver.com">@naver.com</option>
                            <option value="@nate.com">@nate.com</option>
                            <option value="@gamil.com">@gamil.com</option>
                        </select>
                        <button class="confirm_btn confirm_btn--01" type="button">인증받기</button>
                        <input type="hidden" name="userEmail" value="">
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="comfirm_box" style="display: none;">
                        <label>
                            <input type="text" name="confirmNum" placeholder="인증번호 입력" required>
                            <div class="count_box">
                                <p>3:00</p>
                            </div>
                        </label>
                        <button class="confirm_btn confirm_btn--02" type="button">인증확인</button>
                    </div>
                    <!-- e: 인증번호 박스 -->
                </div>

                <div class="my_box">
                    <div class="id_box">
                        <label>
                            <input type="text" name="ID" placeholder="아이디" required>
                            <input type="hidden" name="userId">
                        </label>
                        <button class="repeat_btn" type="button">중복확인</button>
                    </div>
                    <label>
                        <input type="password" name="userPw" placeholder="비밀번호 ex&#41;영문,숫자가 포함된 6글자 이상의 조합" required>
                    </label>
                    <label>
                        <input type="password" name="userPw2" placeholder="비밀번호확인" required>
                    </label>
                    <span class="passwd_noti"></span>
                </div>
                <button class="join_btn" type="button">회원가입</button>
            </div>
        </form>
    </section>
</body>
<script>
	// 이름 정규표현식 변환
	$("input[name='userName']").keyup(function() {
		let name = $(this).val().replace(/[^가-힣]/g, '');
		$(this).val(name);
	});
	// 생년월일 정규표현식 변환
	$("input[name='userBirth']").keyup(function() {
	    let birth = $(this).val().replace(/[^0-9]/g, '');
	
	    if (birth.length < 5) {
	        $(this).val(birth);
	    } else if (birth.length < 7) {
	        $(this).val(birth.replace(/(\d{4})(\d{1,2})/, '$1-$2'));
	    } else {
	        $(this).val(birth.replace(/(\d{4})(\d{2})(\d{1,2})/, '$1-$2-$3'));
	    }
	});
	// 이메일 정규표현식 변환
	$("input[name='email01']").keyup(function() {
		let email01 = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
		$(this).val(email01);
		let userEmail = $("input[name='email01']").val() + $("select[name='email02']").val();
	});
	// 휴대폰번호 정규표현식 변환
	$("input[name='userTel']").keyup(function() {
	    let tel = $(this).val().replace(/[^0-9]/g, '');
	
	    if (tel.length < 4) {
	        $(this).val(tel);
	    } else if (tel.length < 8) {
	        $(this).val(tel.replace(/(\d{3})(\d{1,4})/, '$1-$2'));
	    } else {
	        $(this).val(tel.replace(/(\d{3})(\d{4})(\d{1,4})/, '$1-$2-$3'));
	    }
	});
	// 비밀번호 정규표현식 변환
	$("input[name='userPw'], input[name='userPw2']").keyup(function() {
		let pw = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
	
        $(this).val(pw);
	});
	
	
	let timer; // 타이머 변수
	let timeout = false; // 타이머 만료 여부
	
	// 인증받기 버튼 클릭시
	$(".confirm_btn--01").on("click", function() {
	    let email01 = $("input[name='email01']").val();
	    let email02 = $("select[name='email02']").val();
	    let addEmail = email01 + email02;
	
	    $("input[name='userEmail']").val(addEmail);
	    
	    let userEmail = addEmail;
	
	    if (email01.trim() === '') {
	        alert("이메일을 입력해주세요.");
	        return;
	    } else if (email01.trim().length < 6) {
	        alert("형식에 맞는 이메일주소를 입력해주세요.");
	        return;
	    } 
	
	    // 중복 체크 AJAX 요청
	    $.ajax({
	        url: "/mailCheck.ajax",
	        type: "POST",
	        data: {userEmail: userEmail},
	        success: function(res) {
	            if (res === 'OK') {
	                alert("사용 가능한 이메일 주소입니다.\n해당 이메일로 인증번호 발송되었습니다.");
	                $(".comfirm_box").show();
	                $("input[name='email01']").prop("disabled", true);
	                $("select[name='email02']").prop("disabled", true);
	                $(".confirm_btn--01").prop("disabled", true);
	                $(".confirm_btn--01").addClass("disabled");
	                Timer(); // 인증번호 발송 후 타이머 시작
	
	                // 이메일 전송 AJAX 요청
	                $.ajax({
	                    url: "/sendEmail.ajax",
	                    type: "POST",
	                    data: { userEmail: userEmail },
	                    success: function(res) {
	                        if (res === 'OK') {
	                            console.log("인증메일을 발송");
	                        } else {
	                            alert("이미 등록된 이메일. 확인필요.");
	                        }
	                    },
	                    error: function(err) {
	                        console.log(err);
	        	            alert("서버 요청 실패.\n네트워크 상태를 확인해주세요.");
	                    }
	                });
	            } else {
	                alert("이미 사용중인 이메일입니다.");
	                $("input[name='email01']").val(""); // 입력창 초기화
	                $("input[name='email01']").focus(); // 포커스
	            }
	        },
	        error: function(err) {
	            console.error(err);
	            alert("서버 요청 실패.\n네트워크 상태를 확인해주세요.");
	        }
	    });
	});
	// 인증번호입력 유효시간 타이머
	function Timer() {
	    let time = 180; // 3분
	    timeout = false; // 타이머 시작 시 초기화
	
	    timer = setInterval(function() {
	        let min = Math.floor(time / 60);
	        let sec = time % 60;
	        $(".count_box p").text(min + ":" + (sec < 10 ? '0' : '') + sec);
	
	        if (time <= 0) {
	            clearInterval(timer); // 타이머 중지
	            timeout = true; // 타이머 만료 설정
	            $(".count_box p").text("3:00");
	            $(".confirm_btn--01").text("재인증");
	            $("input[name='email01']").prop("disabled", false);
	            $("select[name='email02']").prop("disabled", false);
	            $(".confirm_btn--01").prop("disabled", false);
	            $(".confirm_btn--01").removeClass("disabled");
	        }
	        time--;
	    }, 1000);
	}
	// 타이머 중지
	function stopTimer() {
	    if (timer) {
	        clearInterval(timer);
	        timer = null;
	    }
	}
	
	// 인증확인 버튼 클릭시
	$(".confirm_btn--02").on("click", function() {
	    let code = $("input[name='confirmNum']").val();
	    if(!timeout){
	    	$.ajax({
	            url: '/codeCheck.ajax',
	            type: 'POST',
	            data: {code: code},
	            success: function (res) {
	                if (res === 'OK') {
	                    alert("인증 성공하였습니다.");
	                    $("input[name='confirmNum']").prop("disabled", true);
	                    $(".confirm_btn--02").prop("disabled", true);
	    	            $(".confirm_btn--02").addClass("disabled");
	                    stopTimer(); // 인증 성공 시 타이머 중지
	                } else {
	                    alert("인증 실패! 다시 입력해주세요.");
	                }
	            },
	            error: function (err) {
	                console.error(err);
	            }
	        });
	    } else{
	    	alert("인증시간이 초과되어 재인증이 필요합니다.")
	    }
	})
	
	// 아이디 중복체크
	$(".repeat_btn").on("click", function() {
	    let idVal = $("input[name='ID']").val().trim();
	    $("input[name='userId']").val(idVal);
	    let userId = $("input[name='userId']").val();
		
		if (userId === '') {
			alert("아이디를 입력해주세요");
		}else {
			$.ajax ({
				type: "GET",
				url: "/idCheck.ajax",
				dataType: "text",
				data: {userId : userId},
				success: function(res) {
					if(res == 0) {
						alert("사용가능한 아이디입니다.");
						$("input[name='ID']").prop("disabled", true);	
						$(".repeat_btn").prop("disabled", true);
						$(".repeat_btn").addClass("disabled");
					}else {
						alert("이미 사용중인 아이디입니다.");
					}
				},error: function(err) {
					console.log(err);
				}
			})
		}
	});
	
	// 비밀번호 일치여부 확인
	$("input[name='userPw'], input[name='userPw2']").on("keyup", function() {
		let userPw = $("input[name='userPw']").val();
		let userPw2 = $("input[name='userPw2']").val();
		
		if(userPw != '' && userPw2 != '' && userPw === userPw2) {
			$(".passwd_noti").text("일치").css("color", "#359176");
		}else if (userPw != userPw2) {
			$(".passwd_noti").text("불일치").css("color", "#fc5c50");
		}else {
			$(".passwd_noti").text("");
		}
	})
	
	// 회원가입 버튼 클릭시 유효성 체크
	$('.join_btn').on('click', function() {
		if($("input[name='userName']").val().trim() == '') {
			return alert("이름을 입력해주세요.");
		}else if ($("input[name='userBirth']").val().trim() == '') {
			return alert("생년월을 입력해주세요.");
		}else if ($("input[name='userTel']").val().trim() == '') {
			return alert("휴대폰 번호 입력해주세요.");
		}else if ($("input[name='email01']").val().trim() == '') {
			return alert("이메일을 입력해주세요.");
		}else if($("input[name='confirmNum']").val().trim() == '' && !$(".confirm_btn--02").hasClass("disabled")){
			return alert("이메일 인증을 완료해주세요.");
		}else if ($("input[name='userId']").val().trim() == '') {
			return alert("아이디를 입력해주세요.");
		}else if (!$(".repeat_btn").hasClass("disabled")) {
			return alert("아이디 중복확인을 완료해주요.");
		}else if ($("input[name='userPw']").val().trim() == '') {
			return alert("비밀번호를 입력해주세요.");
		}else if ($("input[name='userPw2']").val().trim() == '') {
			return alert("비밀번호확인을 입력해주세요.");
		}else if ($("input[name='userPw']").val().trim() != '' != $("input[name='userPw2']").val().trim() == '') {
			return alert("비밀번호가 일치하지 않습니다.");
		}
		
		$("form[name='joinForm']").submit();
	});
	
	// msg가 존재하는 경우 alert
	let msg = "${msg}";
	if (msg != "") {
		alert(msg);
	}
</script>
</html>