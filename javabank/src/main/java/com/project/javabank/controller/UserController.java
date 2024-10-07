package com.project.javabank.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.UserMapper;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
    @Autowired
    private JavaMailSender mailSender;
    
    
    @GetMapping(value= {"/", "/login"}) 
    public String Home (){
    	return "login/login";
    }
	
	@PostMapping("/login")
	public String loginCheck(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) {
	    String userId = params.get("userId");
	    String userPw = params.get("userPw");
	    String saveId = params.containsKey("saveId") ? "on" : "off";
	    
	    // 사용자 정보 가져오기
	    UserDTO user = userMapper.findUserByLogin(userId);
	    
	    // user가 존재할 경우
	    if (user != null) {
	        // user가 입력한 패스워드가 DB에 저장된 패스워드와 같을 경우 
	        if (user.getUserPw().equals(userPw)) {
	            // 쿠키처리
	            if ("on".equals(saveId)) {
	                // 아이디 저장 체크박스가 선택된 경우 
	                Cookie cookie = new Cookie("saveId", userId); // 사용자 ID를 저장하는 쿠키 생성
	                cookie.setMaxAge(24 * 60 * 60);  // 24시간 동안 유지
	                cookie.setPath("/");              // 모든 경로에서 접근 가능하도록 설정
	                resp.addCookie(cookie);           // 응답에 쿠키 추가
	            } else {
	                // 체크박스가 선택되지 않은 경우, 기존 쿠키 삭제
	                Cookie cookie = new Cookie("saveId", "");
	                cookie.setMaxAge(0);
	                cookie.setPath("/");
	                resp.addCookie(cookie);  
	            }
	            
	            return "/pages/index";
	        } else {
	            // 비밀번호 불일치 처리
	            req.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
	            return "login/login";
	        }
	    } else {
	        // 등록되지 않은 ID 처리
	        req.setAttribute("msg", "등록되지 않은 ID입니다. 다시 확인 후 로그인 해주세요.");
	        return "login/login";
	    }
	}
	
	@GetMapping("/join")
	public String join() {
		return "login/join";
	}
    
    // 이메일 중복 확인
	 @ResponseBody
	 @PostMapping("/mailCheck.ajax")
	 public String checkId(String userEmail) {
		 int res = userMapper.checkEmail(userEmail);
		 
		 if(res == 0) {
			 return "OK";
		 }else {
			 return "FAIL";
		 }
	 }
    
	 // 회원가입 이메일 인증 
 	 @ResponseBody
 	 @PostMapping("/sendEmail.ajax")
 	 public String sendEmail(HttpServletRequest req, HttpServletResponse resp, HttpSession session, String userEmail) throws Exception {
 		 try {
 		        Random random = new Random();
 		        String code = String.valueOf(random.nextInt(900000) + 100000);
 		        Cookie cookie = new Cookie("checkCode",code);
 		        cookie.setMaxAge(24 * 60 * 60);
 		        cookie.setPath("/");
 		        resp.addCookie(cookie);
 		        
 		        UserDTO dto = userMapper.findUserByEmail(userEmail);
 		        
 		        if(dto!=null) {
 		        	return "FAIL"; // 이메일이 이미 존재하는 경우
 		        }
 		        
 		        // 이메일 전송 로직
 			 	MimeMessage msg = mailSender.createMimeMessage();
 		        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
 		        helper.setFrom("admin@javabucks.com");
 		        helper.setTo(userEmail);
 		        helper.setSubject("javabank 인증번호입니다.");
 		        helper.setText("안녕하세요 javabank입니다.\n\n javabank 인증 번호 : " + code
 		                + " \n\n 인증번호 인증 후 회원가입을 완료해주세요." + "\n\n --javabank--");
 		        mailSender.send(msg);
 		        
 		        req.setAttribute("msg", "해당 이메일로 정보를 전송하였습니다.");
 		        return "OK";
 		        
 		 }catch(Exception  e) {
 			  e.printStackTrace();  // 로그를 통해 상세 예외 메시지 확인
 		        req.setAttribute("msg", "이메일 전송 중 오류가 발생했습니다.");
 		        return "FAIL";
 		 }
 	 }
 	 
 	// 인증번호 체크
	 @ResponseBody
	 @PostMapping("/codeCheck.ajax")
	 public String codeCheck(@RequestParam("code") String code, HttpServletRequest req) {
		 Cookie [] ck = req.getCookies();
		 if(ck != null) {
			 for(Cookie cookie : ck) {
				 if(cookie.getName().contentEquals("checkCode")) {
					 if(cookie.getValue().equals(code)) {
						 return "OK";
					 }
				 }
			 }
		 }
		 return "FAIL";
	 }
	
	// 아이디 중복체크
	@ResponseBody
	@GetMapping("/idCheck.ajax")
	public int idCheck(@RequestParam String userId) {
		int res = userMapper.idCheck(userId);
		return res;
	}
	
	// 회원가입
	@PostMapping("/join")
	public String InsertMember(HttpServletRequest req, @ModelAttribute UserDTO dto) {
		String userId = dto.getUserId();
	    String userPw = dto.getUserPw();
	    String userName = dto.getUserName();
	    String userBirth = dto.getUserBirth();
	    String userEmail = dto.getUserEmail();
	    String userTel = dto.getUserTel();
		
		Map<String, Object> params = new HashMap<>();

		params.put("userId", userId);
		params.put("userPw", userPw);
		params.put("userName", userName);
		params.put("userBirth", userBirth);
		params.put("userEmail", userEmail);
		params.put("userTel", userTel);
		
		System.out.println(params);
		
		int res = userMapper.insertUser(params);
		
		if(res > 0) {
			req.setAttribute("msg", "회원가입이 완료되었습니다.");
			return "pages/index";
		}else {
			req.setAttribute("msg", "회원가입 실패하였습니다.");
			return "redirect:/join";
		}
		
	}
	
	@GetMapping("/index")
    public String index() {
        return "pages/index";
    }
}
