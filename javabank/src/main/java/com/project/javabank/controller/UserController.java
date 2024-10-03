package com.project.javabank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.javabank.mapper.UserMapper;

@Controller
public class UserController {

	@Autowired
	private UserMapper userMapper;
	
	@GetMapping(value={"/", "/login"})
	public String loginIndex() {
	    return "login/login";
	}

	@GetMapping("/join")
	public String join() {
		return "login/join";
	}
	
	@GetMapping("/index")
    public String index() {
        return "pages/index";
    }
}
