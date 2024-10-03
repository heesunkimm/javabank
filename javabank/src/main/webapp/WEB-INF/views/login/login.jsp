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
                    <li><a href="javascript:;">아이디찾기</a></li>
                    <li><a href="javascript:;">비밀번호찾기</a></li>
                </ul>
            </div>
        </form>
    </section>
</body>
<script>
	$(document).ready(function() {
		//정규표현식 변환
		$("input[name='userId'], input[name='userPw']").keyup(function() {
			let reg_txt = $(this).val().replace(/[^a-zA-Z0-9]/g, '');
		    $(this).val(reg_txt);
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