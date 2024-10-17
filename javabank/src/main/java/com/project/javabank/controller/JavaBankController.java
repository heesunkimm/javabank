package com.project.javabank.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.javabank.dto.AccountDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.JavaBankMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class JavaBankController {

	@Autowired
	private JavaBankMapper mapper;
	
	@GetMapping("/index")
	public String index(@AuthenticationPrincipal User user,
	                    @RequestParam(value="javabank", required=false) String javabank, Model model) {
	    // 로그인 정보 꺼내기
	    model.addAttribute("loginId", user.getUsername());
	    model.addAttribute("loginRoles", user.getAuthorities());

	    // 로그인 유저의 주거래 입출금계좌 조회
	    AccountDTO mainAccount = mapper.loginUserMainAccountInfo(user.getUsername());
	    model.addAttribute("mainAccount", mainAccount);
	    // 로그인 유저의 예적금 계좌리스트
	    List<ProductDTO> pdtlist = mapper.loginUserProduct(user.getUsername());
	    for (ProductDTO pdto : pdtlist) {
	    	if(pdto.getCategory().equals("정기예금")) {
	    	    model.addAttribute("depositList", pdtlist);
	    	}else if (pdto.getCategory().equals("정기적금")) {
	    		model.addAttribute("savingAccountList", pdtlist);
	    	}
	    }
	    return "pages/index";
	}
	
	// 입출금계좌 개설 페이지
	@GetMapping("/add_account")
	public String addAccount(@AuthenticationPrincipal User user, Model model) {
		String userId = user.getUsername();
	    
	    // 로그인 유저이름 가져오기
	    UserDTO udto = mapper.loginUserById(userId);
	    model.addAttribute("loginUser", udto.getUserName());
        
		return "pages/add_account";
	}
	@PostMapping("/add_account")
	public String addAccount(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes, @ModelAttribute AccountDTO dto) {
		String depositPw = dto.getDepositPw();
		String userId = user.getUsername();
		int transactionLimit = dto.getTransactionLimit();
	    String mainAccount = "N";
	    
	    // 로그인 유저이름 가져오기
	    UserDTO udto = mapper.loginUserById(userId);
	    String userName = udto.getUserName();
	    
	    Map<String, Object> checkParams = new HashMap<>();
	    checkParams.put("userId", userId);
	    checkParams.put("mainAccount", "Y");
	    
	    // 로그인 유저의 주거래 계좌 유무 확인
	    int mainAccountExists = mapper.loginUserMainAccountCheck(checkParams);
	    
	    // 주거래 계좌가 없으면 주거래 계좌로 설정
	    if (mainAccountExists == 0) {
	        mainAccount = "Y";
	    }
	    
		// 중복되지 않는 계좌번호 생성
		Random random = new Random();
	    String accountNum = "0925";
	    
	    boolean isCheck = true;
	    
	    while (isCheck) {
	        // 계좌번호 4자리씩 3개 그룹 생성
	        Set<String> numCheck = new HashSet<>();
	        
	        // 4자리씩 3개의 그룹을 생성하여 Set에 저장
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
	        
	        // 계좌 중복체크 
	        int res = mapper.checkAccount(accountNum);
	        // 중복계좌가 없는 경우
	        if (res == 0) {
	        	isCheck = false;
	        }
	    }
		
        String depositAccount = accountNum;
		
		Map<String, Object> params = new HashMap<>();
		params.put("depositAccount", depositAccount);
		params.put("depositPw", depositPw);
		params.put("userId", userId);
		params.put("transactionLimit", transactionLimit);
		params.put("mainAccount", mainAccount);
		params.put("userName", userName);
		
		// 입출금통장 개설
		try {
			mapper.addAccount(params);
	        redirectAttributes.addFlashAttribute("msg", "입출금 통장이 개설되었습니다.");
	    } catch(Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "입출금 통장 개설에 실패하였습니다.");
	    }
		return "redirect:/index";
	}
	
	// 입출금계좌 조회 페이지
	@GetMapping("account_list")
	public String accountList(@AuthenticationPrincipal User user, @RequestParam String depositAccount , Model model) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",user.getUsername());
		params.put("depositAccount", depositAccount);
		
		// 선택한 계좌의 상세 정보
		AccountDTO accountInfo = mapper.loginUserAccountInfo(params);
		model.addAttribute("accountInfo", accountInfo);
		
	    // 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountList = mapper.accountList(params);
	    model.addAttribute("accountList", accountList);
	    model.addAttribute("depositAccount", depositAccount);
	    
		return "pages/account_list";
	}
	
	// 송금 계좌 입력 페이지
	@GetMapping("transfer")
	public String transfer(@AuthenticationPrincipal User user, Model model, @RequestParam("depositAccount") String depositAccount) {
		model.addAttribute("depositAccount", depositAccount);
		// 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountlist = mapper.loginUserAccount(user.getUsername());
	    model.addAttribute("accountlist", accountlist);
	    
	    // 로그인 유저의 최근 입출금거래 계좌리스트
	    
		return "pages/transfer";
	}
	// db에 존재하는 계좌인지 확인
	@ResponseBody
	@PostMapping("accountCheck.ajax")
	public String accountCheck(@AuthenticationPrincipal User user, @RequestParam String transferAccount) {
	    AccountDTO accountCheck = mapper.accountCheck(transferAccount);
		
		// 계좌가 있는 경우
	    if (accountCheck != null && accountCheck.getDepositAccount() != null) {
	        return "OK";
	    } else {
	        return "FALSE";
	    }
	}
	
	// 송금 금액 설정 페이지
	@RequestMapping("transfer_money")
	public String transferMoney(@AuthenticationPrincipal User user, Model model,
			@RequestParam("depositAccount") String depositAccount, @RequestParam("memo") String memo, @RequestParam("transferAccount") String transferAccount) {
		String userId = user.getUsername();
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("depositAccount", depositAccount);
		params.put("meno", memo);
		
		// 계좌잔액 조회
		Integer balance = mapper.balanceCheck(depositAccount);
		
	    model.addAttribute("depositAccount", depositAccount);
	    model.addAttribute("memo", memo);
	    model.addAttribute("balance", balance);
	    model.addAttribute("transferAccount", transferAccount);
		
		return "pages/transfer_money";
	}
	
	// 계좌 비밀번호 확인
	@ResponseBody
	@PostMapping("accountPwCheck.ajax")
	public String accountPwCheck(@AuthenticationPrincipal User user, Model model, 
			@RequestParam("depositAccount") String depositAccount, @RequestParam("depositPw") String depositPw) {
		String userId = user.getUsername();
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("depositAccount", depositAccount);
		
		String res = mapper.accountPwCheck(params);
		
		if (!res.trim().equals(depositPw.trim())) {
	        return "FALSE";
	    } else {
	        return "OK";
	    }
	}
	
	// 송금
	 @Transactional
	@PostMapping("insertMoney")
	public String insertMoney(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes, @RequestParam("depositAccount") String depositAccount, 
			@RequestParam("memo") String memo, @RequestParam("deltaAmount") int deltaAmount, @RequestParam("transferAccount") String transferAccount) {
		
		int withdrawBalance = mapper.balanceCheck(depositAccount) - deltaAmount; // 출금 후 잔액
		
		// 출금
		Map<String, Object> withdrawParams = new HashMap<>();
        withdrawParams.put("depositAccount", depositAccount);
        withdrawParams.put("userId", user.getUsername());
        withdrawParams.put("type", "출금");
        withdrawParams.put("memo", memo);
        withdrawParams.put("deltaAmount", deltaAmount);
        withdrawParams.put("balance", withdrawBalance);
        withdrawParams.put("transferAccount", transferAccount);
        
        // 입금
        Map<String, Object> depositParams = new HashMap<>();
        depositParams.put("depositAccount", transferAccount);
        String recipientUserId = mapper.getUserIdByAccount(transferAccount);
        int depositBalance = mapper.balanceCheck(transferAccount) + deltaAmount; // 입금 후 잔액
        
        depositParams.put("userId", recipientUserId);
        depositParams.put("type", "입금");
        depositParams.put("memo", memo);
        depositParams.put("deltaAmount", deltaAmount);
        depositParams.put("balance", depositBalance);
        depositParams.put("transferAccount", depositAccount);
		
		try {
            mapper.insertMoney(withdrawParams);
            mapper.insertMoney(depositParams);
	        redirectAttributes.addFlashAttribute("msg", "송금이 완료되었습니다.");
	    } catch(Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "송금 실패하였습니다.");
	    }
		return "redirect:/index";
	}
	
	// 내계좌 모아보기
	@GetMapping("my_account")
	public String myAccount(@AuthenticationPrincipal User user, Model model) {
		// 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountlist = mapper.loginUserAccount(user.getUsername());
	    // 로그인 유저의 예적금 계좌리스트
	    List<ProductDTO> pdtlist = mapper.loginUserProduct(user.getUsername());

	    model.addAttribute("accountList", accountlist);
	    
	    for (ProductDTO pdto : pdtlist) {
	    	if(pdto.getCategory().equals("정기예금")) {
	    	    model.addAttribute("depositList", pdtlist);
	    	}else if (pdto.getCategory().equals("정기적금")) {
	    		model.addAttribute("savingAccountList", pdtlist);
	    	}
	    }
		return "pages/my_account";
	}
	
	// 정기 예금 개설 페이지
	@RequestMapping("add_deposit")
	public String addDeposit() {
		return "pages/add_deposit";
	}
	
	// 정기적금 개설 페이지
	@RequestMapping("add_installment_saving")
	public String addInstallmentSaving() {
		return "pages/add_installment_saving";
	}
	
	// 알림 페이지
	@GetMapping("alarm")
	public String Alarm() {
		return "pages/alarm";
	}
}
