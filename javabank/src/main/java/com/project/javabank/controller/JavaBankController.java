package com.project.javabank.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.project.javabank.dto.AlarmDTO;
import com.project.javabank.dto.DtransactionDTO;
import com.project.javabank.dto.ProductDTO;
import com.project.javabank.dto.UserDTO;
import com.project.javabank.mapper.JavaBankMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class JavaBankController {

	@Autowired
	private JavaBankMapper mapper;
	
	// 로그 선언
	private static final Logger logger = LoggerFactory.getLogger(JavaBankController.class);
	// logger.info: 일반 정보성 메세지 기록
	// logger.error: 오류 발생 시 예외메세지와 함꼐 기록
	// logger.debug: 디버깅용 상세 로그 기록
	
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
	    
	    // 정기예금, 정기적금 리스트 분리
	    List<ProductDTO> depositList = new ArrayList<>();
	    List<ProductDTO> savingAccountList = new ArrayList<>();
	    
	    for (ProductDTO pdto : pdtlist) {
	    	if(pdto.getCategory().equals("정기예금")) {
	    		depositList.add(pdto);
	    	}else if (pdto.getCategory().equals("정기적금")) {
	    		savingAccountList.add(pdto);
	    	}
	    }
	    model.addAttribute("depositList", depositList);
	    model.addAttribute("savingAccountList", savingAccountList);
	    return "pages/index";
	}
	// 읽지않은 알람
	@ResponseBody
	@PostMapping("alarmCheck.ajax")
	public int alarmCheck(@AuthenticationPrincipal User user, Model model) {
	    // 로그인 정보 가져오기
	    model.addAttribute("loginId", user.getUsername());

	    // 읽지않은 알람걧수 가져오기
	    int res = mapper.alarmCheck(user.getUsername());
	    return res;
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
        String str[] = depositAccount.split("-");
        String lastAccount = str[str.length -1];
		
		Map<String, Object> params = new HashMap<>();
		params.put("depositAccount", depositAccount);
		params.put("depositPw", depositPw);
		params.put("userId", userId);
		params.put("transactionLimit", transactionLimit);
		params.put("mainAccount", mainAccount);
		params.put("userName", userName);
		
		// 알람추가
		params.put("alarmCate", "입출금계좌 개설");
		params.put("alarmCont", "입출금계좌[" + lastAccount + "]가 개설되었습니다.");
		
		// 입출금통장 개설
		try {
			mapper.addAccount(params);
	        redirectAttributes.addFlashAttribute("msg", "입출금계좌가 개설되었습니다.");
	    } catch(Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "입출금계좌 개설에 실패하였습니다.");
	    }
		return "redirect:/index";
	}
	
	// 입출금계좌 조회 페이지
	@GetMapping("account_list")
	public String accountList(@AuthenticationPrincipal User user, @RequestParam String depositAccount, Model model) {
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
	    List<DtransactionDTO> recentlyList = mapper.recentlyAccountList(depositAccount);
    	model.addAttribute("recentlyList", recentlyList);
	    
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
		DtransactionDTO balanceCheck = mapper.balanceCheck(depositAccount);
		Integer balance = balanceCheck.getBalance();
		
		// 이체한도 확인
		int transferLimit = mapper.transferLimit(depositAccount);
		// 오늘 거래금액 조회
		int transferMoney = mapper.transferMoney(depositAccount);
		model.addAttribute("transferLimit", transferLimit);
		model.addAttribute("transferMoney", transferMoney);
		
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
		
		DtransactionDTO WbalanceCheck = mapper.balanceCheck(depositAccount);
		int withdrawBalance = WbalanceCheck.getBalance() - deltaAmount; // 출금 후 잔액
		
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
        DtransactionDTO DbalanceCheck = mapper.balanceCheck(transferAccount);
		int depositBalance = DbalanceCheck.getBalance() + deltaAmount; // 입금 후 잔액
        
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
	
	// 입출금 계좌 이자 스케줄링
	@Scheduled(cron = "0 0 0 1 * *")
	public void AccountScheduled() {
		List<AccountDTO> accountList = mapper.AllUserAccount();
        
		for(AccountDTO account : accountList) {
			try {
				// 이자계산
				double interest = account.getBalance() * account.getInterestRate();
				int deltaAmount = (int) interest;
				int balance = account.getBalance() + deltaAmount;
				
				Map<String, Object> params = new HashMap<>();
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "이자입금");
				params.put("deltaAmount", deltaAmount);
				params.put("balance", balance);
				params.put("transferAccount", "0925-0925-0925-0925");
				
				// 이자입금
				int res = mapper.insertMoney(params);
				if(res>0) {
	                logger.info("이자 입금 성공 - 계좌번호: " + account.getDepositAccount() + ", 이자: " + deltaAmount + "원");
				}else {
                    throw new Exception("이자 입금 실패 - 계좌번호: " + account.getDepositAccount());
				}
			}catch(Exception e) {
                logger.error("이자 입금 실패 - 계좌번호: " + account.getDepositAccount(), e);
			}
		}
	}
	
	// 정기 예금 개설 페이지
	@GetMapping("add_deposit")
	public String addDeposit(@AuthenticationPrincipal User user, Model model) {
		String userId = user.getUsername();
	    // 로그인 유저이름 가져오기
	    UserDTO udto = mapper.loginUserById(userId);
	    model.addAttribute("loginUser", udto.getUserName());
		
	    // 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountList = mapper.loginUserAccount(user.getUsername());
	    for(AccountDTO account : accountList) {
	    	String depositAccount = account.getDepositAccount();
	    	// 계좌잔액 조회
	    	DtransactionDTO balanceCheck = mapper.balanceCheck(depositAccount);
	    	Integer balance = balanceCheck.getBalance();
	    }
	    model.addAttribute("accountList", accountList);
		
		return "pages/add_deposit";
	}
	@PostMapping("add_deposit")
	public String addDeposit(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes, @ModelAttribute ProductDTO dto) {
		// 중복되지 않는 계좌번호 생성
  		Random random = new Random();
  	    String accountNum = "0426";
  	    
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

  	    String productAccount = accountNum;
        String str[] = productAccount.split("-");
        String lastAccount = str[str.length -1];
  	    double interestRate = dto.getInterestRate();

 		Map<String, Object> params = new HashMap<>();
 		// 상품계좌 생성 파라미터
 		params.put("productAccount", productAccount);
 		params.put("productPw", dto.getProductPw());
 		params.put("userId", user.getUsername());
 		params.put("category", "정기예금");
 		params.put("autoTransferDate","");
 		params.put("monthlyPayment","");
 		params.put("payment", dto.getPayment());
 		params.put("interestRate", interestRate);
 		params.put("depositAccount", dto.getDepositAccount());
  	  
  	    // 오늘 날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);
  	    
  	    if(interestRate == 0.028) {
          String expiryDate = currentTime.plusMonths(6).format(formatter);
          params.put("expiryDate", expiryDate);
  	    	
  	    }else {
  	    	String expiryDate = currentTime.plusYears(1).format(formatter);
  	    	params.put("expiryDate", expiryDate);
  	    }

  	    // 상품 최초 거래내역 파라미터
 		params.put("pdtType", "개설");
 		params.put("pdtMemo", "");
 		params.put("pdtDeltaAmount", dto.getPayment());
 		params.put("pdtBalance", dto.getPayment());
 		
 		DtransactionDTO balanceCheck = mapper.balanceCheck(dto.getDepositAccount());
    	Integer balance = balanceCheck.getBalance();
    	Integer total = balance - dto.getPayment();
    	String userId = balanceCheck.getUserId();
    	
 		// 입출금계좌에서 예금금액 출금
 		params.put("userId", userId);
 		params.put("type", "출금");
 		params.put("memo", "정기예금개설");
 		params.put("deltaAmount", dto.getPayment());
 		params.put("balance", total);
 		params.put("transferAccount", productAccount);

		// 알람추가
		params.put("alarmCate", "정기예금 개설");
		params.put("alarmCont", "정기예금[" + lastAccount + "]이 개설되었습니다.");
		
  	    // 정기예금 개설
		try {
			if(dto.getPayment() >= 500000) {
				mapper.addProduct(params);
				redirectAttributes.addFlashAttribute("msg", "정기예금이 개설되었습니다.");
			}
	    } catch(Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "정기예금 개설에 실패하였습니다.");
	    }
		return "redirect:/index";
	}
	
	// 정기적금 개설 페이지
	@GetMapping("add_installment_saving")
	public String addInstallmentSaving(@AuthenticationPrincipal User user, Model model) {
		String userId = user.getUsername();
	    // 로그인 유저이름 가져오기
	    UserDTO udto = mapper.loginUserById(userId);
	    model.addAttribute("loginUser", udto.getUserName());
	    
	    // 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountList = mapper.loginUserAccount(user.getUsername());
	    for(AccountDTO account : accountList) {
	    	String depositAccount = account.getDepositAccount();
	    	// 계좌잔액 조회
	    	DtransactionDTO balanceCheck = mapper.balanceCheck(depositAccount);
	    	Integer balance = balanceCheck.getBalance();
	    }
	    model.addAttribute("accountList", accountList);
	    
		return "pages/add_installment_saving";
	}
	@PostMapping("add_installment_saving")
	public String addInstallmentSaving(@AuthenticationPrincipal User user, Model model, RedirectAttributes redirectAttributes, @ModelAttribute ProductDTO dto) {
		// 중복되지 않는 계좌번호 생성
  		Random random = new Random();
  	    String accountNum = "0808";
  	    
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

  	    String productAccount = accountNum;
        String str[] = productAccount.split("-");
        String lastAccount = str[str.length -1];
        
  	    double interestRate = dto.getInterestRate();
  	    
 		Map<String, Object> params = new HashMap<>();
 		// 상품계좌 생성 파라미터
 		params.put("productAccount", productAccount);
 		params.put("productPw", dto.getProductPw());
 		params.put("userId", user.getUsername());
 		params.put("category", "정기적금");
 		params.put("autoTransferDate", dto.getAutoTransferDate());
 		params.put("monthlyPayment",dto.getMonthlyPayment());
 		params.put("payment", "");
 		params.put("interestRate", interestRate);
 		params.put("depositAccount", dto.getDepositAccount());
  	  
  	    // 오늘 날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);
  	    
  	    if(interestRate == 0.033) {
          String expiryDate = currentTime.plusMonths(6).format(formatter);
          params.put("expiryDate", expiryDate);
  	    	
  	    }else {
  	    	String expiryDate = currentTime.plusYears(1).format(formatter);
  	    	params.put("expiryDate", expiryDate);
  	    }

  	    // 상품 최초 거래내역 파라미터
 		params.put("pdtType", "개설");
 		params.put("pdtMemo", "");
 		params.put("pdtDeltaAmount", dto.getMonthlyPayment());
 		params.put("pdtBalance", dto.getMonthlyPayment());
 		
 		DtransactionDTO balanceCheck = mapper.balanceCheck(dto.getDepositAccount());
    	Integer balance = balanceCheck.getBalance();
    	Integer total = balance - dto.getMonthlyPayment();
    	String userId = balanceCheck.getUserId();
    	
 		// 입출금계좌에서 최초 적금금액 출금
 		params.put("userId", userId);
 		params.put("type", "출금");
 		params.put("memo", "정기적금개설");
 		params.put("deltaAmount", dto.getMonthlyPayment());
 		params.put("balance", total);
 		params.put("transferAccount", productAccount);

		// 알람추가
		params.put("alarmCate", "정기적금 개설");
		params.put("alarmCont", "정기적금[" + lastAccount + "]이 개설되었습니다.");
 		
  	    // 정기적금 개설
		try {
			mapper.addProduct(params);
	        redirectAttributes.addFlashAttribute("msg", "정기적금이 개설되었습니다.");
	    } catch(Exception e) {
	        redirectAttributes.addFlashAttribute("msg", "정기적금 개설에 실패하였습니다.");
	    }
		return "redirect:/index";
	}
	
	// 예적금계좌 상세 페이지
	@GetMapping("product_list")
	public String productList(@AuthenticationPrincipal User user, Model model, @RequestParam("productAccount") String productAccount) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId",user.getUsername());
		params.put("productAccount", productAccount);
		
		// 선택한 계좌의 상세 정보
		ProductDTO productInfo = mapper.loginUserProductInfo(params);
		model.addAttribute("productInfo", productInfo);
		
	    // 로그인 유저의 입출금 계좌리스트
		List<ProductDTO> productList = mapper.productList(params);
	    model.addAttribute("productList", productList);
	    
		return "pages/product_list";
	}
	
	// 정기예금 만기 및 이자 스케줄링
	@Scheduled(cron = "0 0 0 * * *")
	public void depositScheduled() {
		// 전체 예적금계좌 정보 조회
		List<ProductDTO> AllUserProduct = mapper.AllUserProduct();
		
		// 오늘날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);

  	    for (ProductDTO account : AllUserProduct) {
  	    	// 만기일
			String expireDate = account.getExpiryDate().substring(0, 10);
			int finalInterestRate =  (int) (account.getBalance() * account.getInterestRate());
			int finalBalance = account.getBalance() + finalInterestRate;
			// 계좌 잔액 체크
			DtransactionDTO balanceCheck = mapper.balanceCheck(account.getDepositAccount());
			
			// 정기예금 계좌 마지막 4자리수 꺼내기
			String productAccount = account.getProductAccount();
	        String str[] = productAccount.split("-");
	        String lastAccount = str[str.length -1];
	        
			Map<String, Object> params = new HashMap<>();
			// 만기거래내역 추가
			params.put("productAccount", account.getProductAccount());
			params.put("ptype", "입금");
			params.put("pmemo", "이자입금");
			params.put("pdeltaAmount", finalInterestRate);
			params.put("pbalance", finalBalance);
			
			// 정기예금 만기액, 이자 입금 파라미터
			params.put("depositAccount", balanceCheck.getDepositAccount());
			params.put("userId", account.getUserId());
			params.put("type", "입금");
			params.put("memo", "정기예금만기");
			params.put("deltaAmount", finalBalance);
			params.put("balance", balanceCheck.getBalance() + finalBalance);
			params.put("transferAccount", account.getDepositAccount());
			
			// 알림추가 파라미터
			params.put("userId", account.getUserId());
			params.put("alarmCate", "정기예금만기");
			params.put("alarmCont", "정기예금[" + lastAccount + "] 만기되었습니다.");
			
			// 오늘이 만기일인 경우
			if(Today.equals(expireDate)) {
				mapper.depositScheduled(params);
			}
		}
	}
	
	// 정기적금 입금 스케줄링 (매달 5일)
	@Scheduled(cron = "0 0 0 5 * *")
	public void insertMonthlyPayment05() {
		// 자동이체일체크
		List<ProductDTO> productList = mapper.autoTransferDateCheck(5);

		// 오늘날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);
		
		for(ProductDTO account : productList) {
			// 만기일
			String expireDate = account.getExpiryDate().substring(0, 10);
			int finalDeltaAmonut = account.getMonthlyPayment() + (int) (account.getBalance() * account.getInterestRate());
			int finalBalance = account.getBalance() + finalDeltaAmonut;

			// 계좌 잔액 체크
			DtransactionDTO balanceCheck = mapper.balanceCheck(account.getDepositAccount());
			
			// 정기예금 계좌 마지막 4자리수 꺼내기
			String productAccount = account.getProductAccount();
	        String str[] = productAccount.split("-");
	        String lastAccount = str[str.length -1];
	        
			Map<String, Object> params = new HashMap<>();
			
			// 만기거래내역 추가 파라미터
			params.put("productAccount", account.getProductAccount());
			
			
			if(Today.equals(expireDate)) {
				// 정기적금 만기
				params.put("ptype", "입금");
				params.put("pmemo", "만기이자");
				params.put("pdeltaAmount", finalDeltaAmonut);
				params.put("pbalance", finalBalance);

				// 만기시 이자 입금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "입금");
				params.put("memo", "정기적금만기");
				params.put("deltaAmount", finalBalance);
				params.put("balance", balanceCheck.getBalance() + finalBalance);
				params.put("transferAccount", account.getDepositAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금만기");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 만기되었습니다.");
				mapper.insertInterest(params);
			}else {
				// 정기적금 입금
				params.put("ptype", "입금");
				params.put("pmemo", "정기적금입금");
				params.put("pdeltaAmount", account.getMonthlyPayment());
				params.put("pbalance", account.getBalance() + account.getMonthlyPayment());

				// 정기적금 출금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "출금");
				params.put("memo", "정기적금출금");
				params.put("deltaAmount", account.getMonthlyPayment());
				params.put("balance", balanceCheck.getBalance() - account.getMonthlyPayment());
				params.put("transferAccount", account.getProductAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금입금");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 입금되었습니다.");
				mapper.savingAccountScheduled(params);
			}
		}
	}
	// 정기적금 입금 스케줄링 (매달 10일)
	@Scheduled(cron = "0 0 0 10 * *")
	public void insertMonthlyPayment10() {
		// 자동이체일체크
		List<ProductDTO> productList = mapper.autoTransferDateCheck(5);

		// 오늘날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);
		
		for(ProductDTO account : productList) {
			// 만기일
			String expireDate = account.getExpiryDate().substring(0, 10);
			int finalDeltaAmonut = account.getMonthlyPayment() + (int) (account.getBalance() * account.getInterestRate());
			int finalBalance = account.getBalance() + finalDeltaAmonut;

			// 계좌 잔액 체크
			DtransactionDTO balanceCheck = mapper.balanceCheck(account.getDepositAccount());
			
			// 정기예금 계좌 마지막 4자리수 꺼내기
			String productAccount = account.getProductAccount();
	        String str[] = productAccount.split("-");
	        String lastAccount = str[str.length -1];
	        
			Map<String, Object> params = new HashMap<>();
			
			// 만기거래내역 추가 파라미터
			params.put("productAccount", account.getProductAccount());
			
			
			if(Today.equals(expireDate)) {
				// 정기적금 만기
				params.put("ptype", "입금");
				params.put("pmemo", "만기이자");
				params.put("pdeltaAmount", finalDeltaAmonut);
				params.put("pbalance", finalBalance);

				// 만기시 이자 입금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "입금");
				params.put("memo", "정기적금만기");
				params.put("deltaAmount", finalBalance);
				params.put("balance", balanceCheck.getBalance() + finalBalance);
				params.put("transferAccount", account.getDepositAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금만기");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 만기되었습니다.");
				mapper.insertInterest(params);
			}else {
				// 정기적금 입금
				params.put("ptype", "입금");
				params.put("pmemo", "정기적금입금");
				params.put("pdeltaAmount", account.getMonthlyPayment());
				params.put("pbalance", account.getBalance() + account.getMonthlyPayment());

				// 정기적금 출금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "출금");
				params.put("memo", "정기적금출금");
				params.put("deltaAmount", account.getMonthlyPayment());
				params.put("balance", balanceCheck.getBalance() - account.getMonthlyPayment());
				params.put("transferAccount", account.getProductAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금입금");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 입금되었습니다.");
				mapper.savingAccountScheduled(params);
			}
		}
	}
	// 정기적금 입금 스케줄링 (매달 25일)
	@Scheduled(cron = "0 0 0 25 * *")
	public void insertMonthlyPayment25() {
		// 자동이체일체크
		List<ProductDTO> productList = mapper.autoTransferDateCheck(5);

		// 오늘날짜
  	    LocalDateTime currentTime = LocalDateTime.now();
  	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  	    String Today = currentTime.format(formatter);
		
		for(ProductDTO account : productList) {
			// 만기일
			String expireDate = account.getExpiryDate().substring(0, 10);
			int finalDeltaAmonut = account.getMonthlyPayment() + (int) (account.getBalance() * account.getInterestRate());
			int finalBalance = account.getBalance() + finalDeltaAmonut;

			// 계좌 잔액 체크
			DtransactionDTO balanceCheck = mapper.balanceCheck(account.getDepositAccount());
			
			// 정기예금 계좌 마지막 4자리수 꺼내기
			String productAccount = account.getProductAccount();
	        String str[] = productAccount.split("-");
	        String lastAccount = str[str.length -1];
	        
			Map<String, Object> params = new HashMap<>();
			
			// 만기거래내역 추가 파라미터
			params.put("productAccount", account.getProductAccount());
			
			
			if(Today.equals(expireDate)) {
				// 정기적금 만기
				params.put("ptype", "입금");
				params.put("pmemo", "만기이자");
				params.put("pdeltaAmount", finalDeltaAmonut);
				params.put("pbalance", finalBalance);

				// 만기시 이자 입금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "입금");
				params.put("memo", "정기적금만기");
				params.put("deltaAmount", finalBalance);
				params.put("balance", balanceCheck.getBalance() + finalBalance);
				params.put("transferAccount", account.getDepositAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금만기");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 만기되었습니다.");
				mapper.insertInterest(params);
			}else {
				// 정기적금 입금
				params.put("ptype", "입금");
				params.put("pmemo", "정기적금입금");
				params.put("pdeltaAmount", account.getMonthlyPayment());
				params.put("pbalance", account.getBalance() + account.getMonthlyPayment());

				// 정기적금 출금 파라미터
				params.put("depositAccount", account.getDepositAccount());
				params.put("userId", account.getUserId());
				params.put("type", "출금");
				params.put("memo", "정기적금출금");
				params.put("deltaAmount", account.getMonthlyPayment());
				params.put("balance", balanceCheck.getBalance() - account.getMonthlyPayment());
				params.put("transferAccount", account.getProductAccount());
				
				// 알림추가 파라미터
				params.put("alarmCate", "정기적금입금");
				params.put("alarmCont", "정기적금[" + lastAccount + "] 입금되었습니다.");
				mapper.savingAccountScheduled(params);
			}
		}
	}
	
	// 알림 페이지
	@GetMapping("alarm")
	public String Alarm(@AuthenticationPrincipal User user, Model model) {
		// 알람리스트 조회
		List<AlarmDTO> alarmList = mapper.alarmList(user.getUsername());
		model.addAttribute("alarmList", alarmList);
		// 알람상태업데이트
		mapper.alarmStatusUpdate(user.getUsername());
		return "pages/alarm";
	}
	
	// 내계좌 모아보기
	@GetMapping("my_account")
	public String myAccount(@AuthenticationPrincipal User user, Model model) {
		// 로그인 유저의 입출금 계좌리스트
	    List<AccountDTO> accountlist = mapper.loginUserAccount(user.getUsername());
	    // 로그인 유저의 예적금 계좌리스트
	    List<ProductDTO> pdtlist = mapper.loginUserProduct(user.getUsername());

	    model.addAttribute("accountList", accountlist);
	    
	    // 정기예금, 정기적금 리스트 분리
	    List<ProductDTO> depositList = new ArrayList<>();
	    List<ProductDTO> savingAccountList = new ArrayList<>();
	    
	    for (ProductDTO pdto : pdtlist) {
	    	if(pdto.getCategory().equals("정기예금")) {
	    		depositList.add(pdto);
	    	}else if (pdto.getCategory().equals("정기적금")) {
	    		savingAccountList.add(pdto);
	    	}
	    }
	    model.addAttribute("depositList", depositList);
	    model.addAttribute("savingAccountList", savingAccountList);
	    
		return "pages/my_account";
	}
	
	// 입출금계좌 삭제
	@ResponseBody
	@PostMapping("account_delete.ajax")
	public String accountDelete(@AuthenticationPrincipal User user, @RequestParam("depositAccount") String depositAccount) {
	    
	    // 계좌삭제 전 메인계좌, 잔액 체크
	    AccountDTO accountDelCheck = mapper.AccountDelCheck(depositAccount);

	    if ("Y".equals(accountDelCheck.getMainAccount())) {
	        return "주거래 계좌는 삭제할 수 없습니다.";
	    } else if (accountDelCheck.getBalance() > 0) {
	        return "잔액이 남아 있는 계좌는 삭제할 수 없습니다.";
	    } else {
	        // 계좌 삭제
	        mapper.accountDelete(depositAccount);
            return "OK";
	    }
	}
	
	// 예적금계좌 삭제
	@ResponseBody
	@PostMapping("product_delete.ajax")
	public String productDelete(@AuthenticationPrincipal User user, @RequestParam("productAccount") String productAccount) {
		
		try {
			// 예적금 상품 정보 확인
			ProductDTO productCheck =  mapper.productCheck(productAccount);
			// 입출금계좌 잔액 확인
			DtransactionDTO balanceCheck = mapper.balanceCheck(productCheck.getDepositAccount());
			
			Map<String, Object> params = new HashMap<>();
			params.put("productAccount", productAccount);
			params.put("depositAccount", productCheck.getDepositAccount());
			params.put("userId", productCheck.getUserId());
			params.put("type", "입금");
			params.put("memo", "");
			params.put("deltaAmount", productCheck.getBalance());
			params.put("balance", balanceCheck.getBalance() + productCheck.getBalance());
			params.put("transferAccount", productCheck.getProductAccount());
			System.out.println(params);
			mapper.productDelete(params);
			return "OK";
		}catch(Exception e) {
			logger.error("예적금 계좌 삭제 중 오류 발생", e);
	        return "계좌 삭제 중 오류가 발생했습니다.";
		}
	}
	
	// 주거래계좌 상태변경
	@ResponseBody
	@PostMapping("conversionMainAccount.ajax")
	public String conversionMainAccount(@AuthenticationPrincipal User user, @RequestParam("depositAccount") String depositAccount) {
		// 주거례계좌 유무 확인
		try {
			mapper.conversionMainAccount(user.getUsername(), depositAccount);
			return "OK";
		}catch(Exception e) {
			return "FALSE";
		}
	}
}
