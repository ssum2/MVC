package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
/*
	[추상 메소드 선언]
	1) 추상메소드 선언시 public abstract 지정자가 자동으로 붙음
	2) 추상변수 선언시 자동으로 public static final이 붙음 
 */
//	#웹 실제 실행메소드	
	void execute(HttpServletRequest req, HttpServletResponse res) throws Exception;
	
}
