package sg.edu.iss.sa50.t8.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.iss.sa50.t8.model.Employee;

@Controller
@RequestMapping("/")
public class AccountController {

	@RequestMapping("/")
	public String home(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if(session == null) {
			return "home";
		}else {
			Employee emp = (Employee) session.getAttribute("user");
			if(emp.getDiscriminatorValue().equals("Admin")) {
				return "admin";
			}else {
				return "leaves";}
		}
	}
}

