package com.project.javabank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.project.javabank.mapper.JavaBankMapper;

@Controller
public class JavaBankController {

	@Autowired
	private JavaBankMapper Mapper;
}
