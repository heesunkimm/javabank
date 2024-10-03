<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
        <form name="f" action="/join" method="post">
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
                    <div class="email_box">
                        <label>
                            <input type="text" name="email01" placeholder="이메일" maxlength="16" required>
                        </label>
                        <select name="email02">
                            <option value="@naver.com">@naver.com</option>
                            <option value="@nate.com">@nate.com</option>
                            <option value="@gamil.com">@gamil.com</option>
                        </select>
                        <input type="hidden" name="userEmail">
                    </div>
                    <div class="tel_box">
                        <label>
                            <input type="text" name="userTel" placeholder="핸드폰번호 ex&#41;010-0000-0000" maxlength="13" required>
                        </label>
                        <button class="confirm_btn confirm_btn--01" type="button">인증받기</button>
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="comfirm_box" style="display: none;">
                        <label>
                            <input type="text" name="confirmNum" placeholder="인증번호 입력" required>
                            <div class="count_box">
                                <span>3:00</span>
                            </div>
                        </label>
                        <button class="confirm_btn confirm_btn--02" type="button">인증확인</button>
                    </div>
                    <!-- e: 인증번호 박스 -->
                </div>

                <div class="my_box">
                    <div class="id_box">
                        <label>
                            <input type="text" name="userId" placeholder="아이디" required>
                        </label>
                        <button class="repeat_btn" type="button">중복확인</button>
                    </div>
                    <label>
                        <input type="password" name="userPw" placeholder="비밀번호 ex&#41;영문,숫자가 포함된 8글자 이상의 조합" required>
                    </label>
                    <label>
                        <input type="password" name="userPw2" placeholder="비밀번호확인" required>
                    </label>
                    <!-- <p class="passwd_noti"></p> -->
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
	
	// 인증받기 버튼 클릭시
	$(".confirm_btn--01").on("click", function() {
		let tel = $("input[name='userTel']").val();
		if(tel.trim() === '') {
			alert("핸드폰번호를 입력해주세요.");
		}else if(tel.trim().length < 13) {
			alert("형식에 맞는 핸드폰번호를 입력해주세요.");
		}else {
			$(".comfirm_box").show();
		}
	});
	// 아이디 중복체크
	$(".repeat_btn").on("click", function() {
		let userId = $("input[name='userId']").val().trim();
		
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
					}else {
						alert("이미 사용중인 아이디입니다.");
					}
				},error: function(err) {
					console.log(err);
				}
			})
		}
	});
	
	// 회원가입 버튼 클릭시 유효성 체크
	$('.join_btn').on('click', function() {
		if($("input[name='userName']").val().trim() == '') {
			return alert("이름을 입력해주세요.");
		}else if ($("input[name='userBirth']").val().trim() == '') {
			return alert("생년월을 입력해주세요.");
		}else if ($("input[name='email01']").val().trim() == '') {
			return alert("이메을 입력해주세요.");
		}else if ($("input[name='userTel']").val().trim() == '') {
			return alert("휴대폰 번호 입력해주세요.");
		}else if($("input[name='confirmNum']").val().trim() == ''){
			return alert("휴대폰 인증을 완료해주세요.");
		}else if ($("input[name='userId']").val().trim() == '') {
			return alert("아이디를 입력해주세요.");
		}else if ($("input[name='userPw']").val().trim() == '') {
			return alert("비밀번호를 입력해주세요.");
		}else if ($("input[name='userPw2']").val().trim() == '') {
			return alert("비밀번확인을 입력해주세요.");
		}

		// 비밀번호 체크
		let userPw = $("input[name='userPw']").val();
		let userPw2 = $("input[name='userPw2']").val();
		
		if(userPw != userPw2 || userPw.length != userPw2) {
			return alert("비밀번호가 일치하지 않습니다.");
		}
	});
	
	
	
	
	
</script>
</html>