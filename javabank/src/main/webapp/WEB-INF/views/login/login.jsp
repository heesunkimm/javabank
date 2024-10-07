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
<title>javabank_Login</title>
</head>
<body>
    <section id="login">
        <form name="loginForm" action="/login" method="post">
            <div class="logo_box">
                <p>java<em>bank</em></p>
            </div>
            
            <div class="input_box">
                <label>
                <c:if test="${empty cookie['saveId']}">
                  	<input type="text" name="userId" value="" placeholder="ID" required>
              	</c:if>
              	<c:if test="${not empty cookie['saveId']}">
              		<input type="text" name="userId" value="${cookie['saveId'].value}" placeholder="ID" required>
              	</c:if>
                </label>
                <label>
                    <input type="password" name="userPw" value="" placeholder="PASSWORD" required>
                </label>
                <button class="login_btn" type="submit">로그인</button>
            </div>

            <div class="save_box">
                <label>
                <c:if test="${empty cookie['saveId']}">
                	아이디저장<input type="checkbox" name="saveId" value="">
                </c:if>
                <c:if test="${not empty cookie['saveId']}">
           			아이디저장<input type="checkbox" name="saveId" value="" checked> 
                </c:if>
                </label>
            </div>

            <div class="join_box">
                <a class="join_btn" href="/join">회원가입</a>
                <ul class="find_box">
                    <li><a class="findId_btn popup_btn" href="javascript:;" data-popup="findbyid">아이디찾기</a></li>
                    <li><a class="findPw_btn popup_btn" href="javascript:;" data-popup="findbypw">비밀번호찾기</a></li>
                </ul>
            </div>
        </form>
    </section>
    <!-- 아이디찾기 팝업 -->
    <div id="findbyid" class="popup_box" style="display: none;">
       <p class="popup_title">아이디 찾기</p>
       <form name="f" action="findIdbyEmail" method="post">
           <div class="input_box">
               <div class="email_box">
                   <label>
                       <input type="text" name="email01" value="" placeholder="이메일입력" required>
                   </label>
                   <label>
                       <select name="userEmail2" class="email02">
                           <option value="naver.com">@naver.com</option>
                           <option value="nate.com">@nate.com</option>
                           <option value="gmail.com">@gmail.com</option>
                       </select>
                   </label>
                   <input type="hidden" name="userEmail" value="">
                   <button type="button" class="confirm_btn">인증번호 발송</button>
               </div>
           </div>
           <!-- s: 인증번호 박스 -->
           <div class="comfirm_box" style="display: none;">
               <label>
                   <input type="text" name="confirmNum" placeholder="인증번호 입력" required>
                   <div class="count_box">
                       <p>3:00</p>
                   </div>
               </label>
               <button class="confirm_btn" type="button">인증확인</button>
           </div>
           <!-- e: 인증번호 박스 -->
           <div class="pbtn_box">
               <button class="submit_btn" type="submit">확인</button>
               <button class="close_btn" type="button" data-popup="findbyid">취소</button>
           </div>
       </form>
   </div>
   <!-- 비밀번호찾기 팝업 -->
    <div id="findbypw" class="popup_box" style="display: none;">
        <p class="popup_title">비밀번호 찾기</p>
        
        <form name="f" action="findPwbyEmail" method="post">
            <div class="input_box">
                <label>
                    <input type="text" class="userInputId" name="findbypw_id" value="" placeholder="아이디 입력" required>
                </label>
                <div class="email_box">
                   <label>
                       <input type="text" name="email01" value="" placeholder="이메일입력" required>
                   </label>
                   <label>
                       <select name="userEmail2" class="email02">
                           <option value="naver.com">@naver.com</option>
                           <option value="nate.com">@nate.com</option>
                           <option value="gmail.com">@gmail.com</option>
                       </select>
                   </label>
                   <input type="hidden" name="userEmail" value="">
                   <button type="button" class="confirm_btn">인증번호 발송</button>
            	</div>
            </div>
            <!-- s: 인증번호 박스 -->
            <div class="comfirm_box" style="display: none;">
                <label>
                    <input type="text" name="confirmNum" placeholder="인증번호 입력" required>
                    <div class="count_box">
                        <p>3:00</p>
                    </div>
                </label>
                <button class="confirm_btn" type="button">인증확인</button>
            </div>
            <!-- e: 인증번호 박스 -->
            <div class="pbtn_box">
                <button class="submit_btn" type="submit">확인</button>
                <button class="close_btn" type="button" data-popup="findbypw">취소</button>
            </div>
        </form>
    </div>
    <div class="dimm"></div>
</body>
<script>
	$(document).ready(function() {
		// 비밀번호 정규표현식 변환
		$("input[name='userId'], input[name='userPw']").keyup(function() {
			let reg_txt = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
		    $(this).val(reg_txt);
		});
		// 이메일 정규표현식 변환
		$("input[name='email01']").keyup(function() {
			let email01 = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
			$(this).val(email01);
			let userEmail = $("input[name='email01']").val() + $("select[name='email02']").val();
		});
		
	    $(".login_btn").on("click", function(e) {
	        let userId = $("input[name='userId']").val().trim();
	        let userPw = $("input[name='userPw']").val().trim();
	
	        // 유효성 검사
	        if (userId === '') {
	            alert("아이디를 입력해주세요.");
	            e.preventDefault();
	            return false;
	        } else if (userPw === '') {
	            alert("비밀번호를 입력해주세요.");
	            e.preventDefault();
	            return false;
	        }
            $("form[name='loginForm']").submit();
	    });

		// msg가 존재하는 경우 alert
		let msg = "${msg}";
		if (msg != "") {
			alert(msg);
		}
	});
	
</script>

</html>