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
                        <input type="text" name="userName" placeholder="이름" required>
                    </label>
                    <label>
                        <input type="text" name="userBirth" placeholder="생년월일 ex&#41;2024-10-01" required>
                    </label>
                    <div class="email_box">
                        <label>
                            <input type="text" name="" placeholder="이메일" required>
                        </label>
                        <select>
                            <option value="@naver.com">@naver.com</option>
                            <option value="@nate.com">@nate.com</option>
                            <option value="@gamil.com">@gamil.com</option>
                        </select>
                        <input type="hidden" name="userEmail">
                    </div>
                    <div class="tel_box">
                        <label>
                            <input type="text" name="userTel" placeholder="핸드폰번호 ex&#41;010-0000-0000" required>
                        </label>
                        <button class="confirm_btn confirm_btn--01" type="button">인증받기</button>
                    </div>
                    <!-- s: 인증번호 박스 -->
                    <div class="comfirm_box" style="display: none;">
                        <label>
                            <input type="text" placeholder="인증번호 입력" required>
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
	$('.join_btn').on('click', function() {
		// 유효성 검
		if($("input[name='userName']").val().trim() == '') {
			alert("이름을 입력해주세요.");
		}else if ($("input[name='userBirth']").val().trim() == '') {
			alert("생년월을 입력해주세요.");
		}else if ($("input[name='email01']").val().trim() == '') {
			alert("이메을 입력해주세요.");
		}else if ($("input[name='userTel']").val().trim() == '') {
			alert("휴대폰 번호 입력해주세요.");
		}else if ($("input[name='userId']").val().trim() == '') {
			alert("아이디를 입력해주세요.");
		}else if ($("input[name='userPw']").val().trim() == '') {
			alert("비밀번호를 입력해주세요.");
		}else if ($("input[name='userPw2']").val().trim() == '') {
			alert("비밀번확인을 입력해주세요.");
		}
	});
	
	
	
</script>
</html>