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
        <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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
           			아이디저장<input type="checkbox" name="saveId" value="on" checked> 
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
       <div class="input_box">
           <div class="email_box">
               <label>
                   <input type="text" name="email01" value="" placeholder="이메일입력" required>
               </label>
               <label>
                   <select name="email02" class="email02">
                       <option value="@naver.com">@naver.com</option>
                       <option value="@nate.com">@nate.com</option>
                       <option value="@gmail.com">@gmail.com</option>
                   </select>
               </label>
           </div>
       </div>
       <div class="pbtn_box">
           <button class="submit_btn" type="button">확인</button>
           <button class="close_btn" type="button" data-popup="findbyid">취소</button>
       </div>
   </div>
   <!-- 비밀번호찾기 팝업 -->
    <div id="findbypw" class="popup_box" style="display: none;">
        <p class="popup_title">비밀번호 찾기</p>
        <div class="input_box">
            <label>
                <input type="text" class="userInputId" name="userId" value="" placeholder="아이디 입력" required>
            </label>
            <div class="email_box">
               <label>
                   <input type="text" name="email01" value="" placeholder="이메일입력" required>
               </label>
               <label>
                   <select name="email02" class="email02">
                       <option value="@naver.com">@naver.com</option>
                       <option value="@nate.com">@nate.com</option>
                       <option value="@gmail.com">@gmail.com</option>
                   </select>
               </label>
               <button type="button" class="confirm_btn confirm_btn01">인증번호 발송</button>
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
            <button class="confirm_btn confirm_btn02" type="button">인증확인</button>
        </div>
        <!-- e: 인증번호 박스 -->
        <div class="pbtn_box">
            <button class="submit_btn" type="submit">확인</button>
            <button class="close_btn" type="button" data-popup="findbypw">취소</button>
        </div>
    </div>
    <div class="dimm"></div>
</body>
<script>
	$(document).ready(function() {
	    let csrfToken = $(".csrfToken").val();
		// 비밀번호 정규표현식 변환
		$("input[name='userId'], input[name='userPw']").keyup(function() {
			let reg_txt = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
		    $(this).val(reg_txt);
		});
		// 이메일 정규표현식 변환
		$("input[name='email01']").keyup(function() {
			let email01 = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
			$(this).val(email01);
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
	    
	    // 아이디찾기
	    $("#findbyid .submit_btn").on("click", function() {
	    	let email01 = $("input[name='email01']").val().trim();
			let email02 = $("select[name='email02']").val();
			let userEmail = email01 + email02;
			
			console.log(email01);
	    	
			// 이메일이 공백일 경우
	    	if(email01 === "") {
	    		return alert("이메일을 입력해주세요.");
	    	}
			
		    $.ajax({
		        url: "/findUserById.ajax",
		        type: 'POST',
		        headers: {"X-CSRF-TOKEN": csrfToken},
		        data: {userEmail: userEmail},
		        success: function(res) {
		            if (res === "OK") {
		                alert("해당 이메일로 아이디 발송되었습니다.\n메일을 확인해주세요.");
		                $("input[name='email01']").val("");
		                $("#findbyid, .dimm").removeClass("s_active");
		            } else if (res === "FAIL") {
		                alert("등록되지 않은 이메일입니다.");
		            } else {
		                alert("인증번호 전송 중 오류가 발생했습니다.");
		            }
		        },
		        error: function (err) {
		            console.error("error:", err);
		        }
		    });
	    });
	    
	    // 비밀번호찾기
	    $("#findbypw .submit_btn").on("click", function() {
	    	let userId = $("#findbypw input[name='userId']").val().trim();
	    	let email01 = $("input[name='email01']").val().trim();
			let email02 = $("select[name='email02']").val();
			let userEmail = email01 + email02;
			
			// 유효성 체크
			if(userId === '') {
				return alert("아이디를 입력해주세요.");
			}else if(userEmail === '') {
				return alert("이메일을 입력해주세요.");
			}else if(!$("#findbypw .confirm_btn01").hasClass("disabled") || !$("#findbypw .confirm_btn02").hasClass("disabled")) {
				return alert("이메일 인증을 완료해주세요.");
			}
			
	    });
	    $("#findbypw .confirm_btn01").on("click", function() {
	    	let email01 = $("input[name='email01']").val().trim();
	    	
	    	if(email01 === '') {
	    		return alert("이메일을 입력해주세요.");
	    	}
	    	
	    	$.ajax({
		        url: "/findUserInfo.ajax",
		        type: 'POST',
		        headers: {"X-CSRF-TOKEN": csrfToken},
		        data: {userEmail: userEmail},
		        success: function(res) {
		        	console.log(res);
		        },
		        error: function (err) {
		            console.error("error:", err);
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
	    
		let msg = "${msg}";
		if (msg != "") {
			alert(msg);
		}
	});
</script>
</html>