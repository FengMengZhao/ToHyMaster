package com.fmz.springweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HelloWorldAnnotationController{

	private int count = 0;
	
	@RequestMapping(value="/count")
	public ModelAndView count(){
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", "count...");
		mv.setViewName("/jsp/count.jsp");
		System.out.println(count++);
		return mv;
	}
	
	@RequestMapping(value="/hi")
	public ModelAndView hi(String param){
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", param);
		mv.setViewName("/jsp/hi.jsp");
		return mv;
	}
}
