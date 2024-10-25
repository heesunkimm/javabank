package com.project.javabank.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    
    @Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String home() {
		return "login/login";
	}
	// 로그인
	@GetMapping("/login")
	public String login(@RequestParam(value="loginError", required=false) String loginError,
						@RequestParam(value="insertError", required=false) String insertError,
						@RequestParam(value="joinError", required=false) String joinError,
						@RequestParam(value="joinOK", required=false) String joinOK,
						@RequestParam(value="logout", required=false) String logout,
						Model model, HttpServletRequest req) {
		
		// CSRF 토큰 꺼내기
		CsrfToken csrfToken = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        
		// CSRF 토큰 model 객체에 담아 뷰로 전달하기
		model.addAttribute("_csrf", csrfToken);        
        
        if (loginError != null) {
        	model.addAttribute("msg","입력하신 ID와 PW를 다시 확인해주세요.");
        }
        
        if (insertError != null) {
        	model.addAttribute("msg","회원가입 도중 에러가 발생했습니다. 관리자에게 문의해주세요.(에러코드:AA01)");
        }        
        
        if (joinError != null) {
        	model.addAttribute("msg","회원가입 도중 에러가 발생했습니다. 관리자에게 문의해주세요.(에러코드:AB01)");
        }    
        
        if (joinOK != null) {
        	model.addAttribute("msg","회원가입이 정상적으로 완료되었습니다. 가입하신 아이디와 비밀번호로 로그인해주세요.");
        }   
        
        if (logout != null) {
        	model.addAttribute("msg","로그아웃 되었습니다. 이용해주셔서 감사합니다.");
        }  
	    return "login/login"; 
	}
	
	// 아이디찾기
	@ResponseBody
	@PostMapping("/findUserById.ajax")
	public String findUserById(HttpServletResponse resp, String userEmail) throws Exception {
	    try {
	    	// 아이디찾기
	        UserDTO dto = userMapper.findUserByEmail(userEmail);

	        if (dto != null && userEmail.equals(dto.getUserEmail())) {
	        	String userId = dto.getUserId();
	            MimeMessage msg = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	            helper.setFrom("admin@javabank.com");
	            helper.setTo(userEmail);
	            helper.setSubject("javabank 아이디찾기 메일입니다.");
	            helper.setText("안녕하세요 javabank입니다.\n\n 해당 메일로 가입된 아이디는 " + userId + " 입니다." + "\n\n --javabank--");
	            mailSender.send(msg);
	            return "OK";
	        } else {
	            System.out.println("Response: FAIL");
	            return "FAIL";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "ERROR";
	    }
	}
	
	// 인증확인
	@ResponseBody
	@PostMapping("/findUserInfo.ajax")
	public String findUserInfo(HttpServletResponse resp, HttpSession session, @RequestParam("userId") String userId, @RequestParam("userEmail") String userEmail) throws Exception {
	    try {
	        // 랜덤 인증번호 생성
	        Random random = new Random();
	        String code = String.valueOf(random.nextInt(900000) + 100000);
	        Cookie cookie = new Cookie("checkCode", code);
	        cookie.setMaxAge(24 * 60 * 60); // 24시간
	        cookie.setPath("/");
	        resp.addCookie(cookie);
	        
	        UserDTO dto = userMapper.findUserByEmail(userEmail);

	        // 사용자 정보가 일치할 경우
	        if (dto != null) {
	        	if(userEmail.equals(dto.getUserEmail())) {
	        		// 아이디와 이메일이 모두 일치할 경우
	        		if(userId.equals(dto.getUserId())) {
		        		MimeMessage msg = mailSender.createMimeMessage();
		                MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		                helper.setFrom("admin@javabank.com");
		                helper.setTo(userEmail);
		                helper.setSubject("javabank 인증번호입니다.");
		                helper.setText("안녕하세요 javabank입니다.\n\n" +
		                               "javabank 인증 번호 : " + code + 
		                               "\n\n인증번호 인증 후 비밀번호 재설정을 완료해주세요." + "\n\n--javabank--");
		                mailSender.send(msg);
		                return "OK";
		        	}else {
		        		 // 아이디불일치
		        		return "ID_FALSE";
		        	}
	        	}else {
	        		// 이메일불일치
	        		return "EMAIL_FALSE";
	        	}
	        } else {
	        	return "FALSE";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "ERROR"; // 일반적인 오류 처리
	    }
	}
	// 비밀번호 변경
	@ResponseBody
	@PostMapping("/updateUserPw.ajax")
	public String updateUserPw(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("userPw", passwordEncoder.encode(userPw));
		
		int res = userMapper.updateUserPw(params);
		if(res > 0) {
			return "OK";
		}else {
			return "FALSE";
		}
	}

	// 회원가입
	@GetMapping("/join")
	public String join(Model model, HttpServletRequest req) {
		return "login/join";
	}
	@PostMapping("/join")
	public String joinProcess(@RequestParam Map<String, String> reqParams){
		// 비밀번호 암호화
		String userPw = reqParams.get("userPw");		
		String encodedUserPw = passwordEncoder.encode(userPw);
		
		// 이메일 합치기
		String userEmail = reqParams.get("userEmail1") + reqParams.get("userEmail2");
		
		Map<String, Object> params = new HashMap<>();
		params.put("userName", reqParams.get("userName"));
		params.put("userBirth", reqParams.get("userBirth"));
		params.put("userEmail", userEmail);
		params.put("userTel", reqParams.get("userTel"));
		params.put("userId", reqParams.get("userId"));
		params.put("userPw", encodedUserPw);
		
		try {
			int joinResult = userMapper.joinUser(params);
			if(joinResult > 0) {
				System.out.println("회원가입 INSERT 성공");
				return "redirect:/login?joinOK";
			} else {
				System.out.println("회원가입 INSERT 실패");
				return "redirect:/login?insertError";
			}
		}catch(Exception e) {
			System.out.println("에러 발생");
			e.printStackTrace();
			return "redirect:/login?joinError";
		}
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
	         helper.setFrom("admin@javabank.com");
	         helper.setTo(userEmail);
	         helper.setSubject("javabank 인증번호입니다.");
	         helper.setText("안녕하세요 javabank입니다.\n\n javabank 인증 번호 : " + code + " \n\n 인증번호 인증 후 회원가입을 완료해주세요." + "\n\n --javabank--");
	         mailSender.send(msg);
	        
	         req.setAttribute("msg", "해당 이메일로 인증번호를 전송하였습니다.");
	         return "OK";
 		 }catch(Exception  e) {
 			 e.printStackTrace();  // 로그를 통해 상세 예외 메시지 확인
	         req.setAttribute("msg", "해당 이메일로 인증번호 전송중 오류가 발생했습니다.");
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
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		new SecurityContextLogoutHandler().logout(req, resp, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login?logout";
	}
}
