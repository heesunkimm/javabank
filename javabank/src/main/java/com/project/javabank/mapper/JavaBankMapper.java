package com.project.javabank.mapper;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JavaBankMapper {

	@Autowired
	private SqlSession sqlSession;
}
