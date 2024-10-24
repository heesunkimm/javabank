<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="../css/reset.css">
<link rel="stylesheet" href="../css/style.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="../js/script.js"></script>
<title>javabank</title>
</head>
<body>
    <header>
    <input class="csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="logo_box">
	        <p><a href="index">java<em>bank</em></a></p>
        </div>

        <div class="icon_box">
        	<a href="alarm" data-read="">
	            <div class="img_box">
	                <img src="../../images/icons/alarm.png">
	            </div>
	            <p class="alarm_txt">0</p>
        	</a>
        	<a href="javascript:;">
	            <div class="setting_btn img_box">
	                <img src="../../images/icons/setting.png">
	            </div>
        	</a>
            <ul class="setting_box" style="display: none;">
                <li><a href="my_account">내계좌 모아보기</a></li>
                <li><a href="/logout">로그아웃</a></li>
            </ul>
        </div>
    </header>
    <script type="text/javascript">
    $(document).ready(function() {
		let csrfToken = $(".csrfToken").val();
    	
    	$.ajax({
    		url: "alarmCheck.ajax",
    		type: "post",
    		headers: {"X-CSRF-TOKEN": csrfToken}, 
    		data: {userId: ${loginId}},
    		success: function(res){
    			
    		},error: function(err){
    			console.log(err);
    		}
    	});
	});
    </script>