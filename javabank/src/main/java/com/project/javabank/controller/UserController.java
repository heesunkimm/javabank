package com.project.javabank.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.UserMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
	@GetMapping("/")
	public String home() {
		return "login/login";
	}

	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
	    return "login/login"; 
	}
	
	@PostMapping("/login")
	public String Index() {
	    return "pages/index";  // 이 부분은 사용되지 않음
	}
	
//	@PostMapping("/login")
//	public String loginCheck(@RequestParam Map<String, String> params, HttpServletRequest req, HttpServletResponse resp) {
//	    String userId = params.get("userId");
//	    String userPw = params.get("userPw");
//	    String saveId = params.containsKey("saveId") ? "on" : "off";
//	    
//	    // 사용자 정보 가져오기
//	    UserDTO user = userMapper.findUserByLogin(userId);
//	    
//	    // user가 존재할 경우
//	    if (user != null) {
//	        // user가 입력한 패스워드가 DB에 저장된 패스워드와 같을 경우 
//	        if (user.getUserPw().equals(userPw)) {
////	            System.out.println("일치, 로그인 성공");
//	            
//	            // 쿠키처리
//	            if ("on".equals(saveId)) {
//	                // 아이디 저장 체크박스가 선택된 경우 
//	                Cookie cookie = new Cookie("saveId", userId); // 사용자 ID를 저장하는 쿠키 생성
//	                cookie.setMaxAge(24 * 60 * 60);  // 24시간 동안 유지
//	                cookie.setPath("/");              // 모든 경로에서 접근 가능하도록 설정
//	                resp.addCookie(cookie);           // 응답에 쿠키 추가
//	            } else {
//	                // 체크박스가 선택되지 않은 경우, 기존 쿠키 삭제
//	                Cookie cookie = new Cookie("saveId", "");
//	                cookie.setMaxAge(0);
//	                cookie.setPath("/");
//	                resp.addCookie(cookie);  
//	            }
//	            
//	            return "/pages/index";
//	        } else {
//	            // 비밀번호 불일치 처리
////	            System.out.println("비밀번호 불일치");
//	            req.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
//	            return "login/login";
//	        }
//	    } else {
//	        // 등록되지 않은 ID 처리
////	        System.out.println("등록되지 않은 ID입니다. 다시 확인 후 로그인 해주세요.");
//	        req.setAttribute("msg", "등록되지 않은 ID입니다. 다시 확인 후 로그인 해주세요.");
//	        return "login/login";
//	    }
//	}

	@GetMapping("/join")
	public String join() {
		return "login/join";
	}
	
	// 아이디 중복체크
	@ResponseBody
	@GetMapping("/idCheck.ajax")
	public int idCheck(@RequestParam String userId) {
		int res = userMapper.idCheck(userId);
		System.out.println(res);
		return res;
	}
	
	@GetMapping("/index")
    public String index() {
        return "pages/index";
    }
}
