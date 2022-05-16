package org.tain.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST})
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
		System.out.println(">>> index.html..");
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", "Kiea 강석");
		map.put("date", LocalDateTime.now());
		modelMap.put("data", map);
		
		return "/index";
	}
}
