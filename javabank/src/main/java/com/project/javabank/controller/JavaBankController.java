package com.project.javabank.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.javabank.dto.AccountDTO;
import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.JavaBankMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JavaBankController {

	@Autowired
	private JavaBankMapper mapper;
	
	
	@RequestMapping("add_account")
	public String addAccount(HttpServletRequest req, @ModelAttribute AccountDTO dto) {
		
		int depositPw = dto.getDepositPw();
		String userId = dto.getUserId();
	    int accountBalance = 0;
	    double interestRate = 0.1;
	    String accountLimit = dto.getAccountLimit();
	    String mainAccount = "";
        
		Random random = new Random();
	    String accountNum = "0925";
		
	    // 계좌번호 생성
	    Set<String> numCheck = new HashSet<>();
	    
	    // 4자리씩 3개의 그룹을 생성
        while (numCheck.size() < 3) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int num = random.nextInt(10);
                sb.append(num);
            }
            numCheck.add(sb.toString());
        }
        
        // Set을 리스트로 변환하여 계좌번호에 추가
        for (String num : numCheck) {
        	accountNum += "-" + num;
        }
        
//        System.out.println("생성 계좌: " + accountNum);
        String depositAccount = accountNum;
		
		Map<String, Object> params = new HashMap<>();

		params.put("depositAccount", depositAccount);
		params.put("depositPw", depositPw);
		params.put("userId", userId);
		params.put("accountBalance", accountBalance);
		params.put("interestRate", interestRate);
		params.put("accountLimit", accountLimit);
		params.put("mainAccount", mainAccount);
		
		int res = mapper.checkAccount(depositAccount);
		
		// 계좌번호 존재시
		if(res > 0) {
			
		}else {
			
		}
        

		
		
		
		return "pages/add_account";
	}
}
