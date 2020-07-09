package sg.edu.iss.sa50.t8.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.iss.sa50.t8.model.Admin;
import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.service.AdminService;
import sg.edu.iss.sa50.t8.service.IEmployeeService;
import sg.edu.iss.sa50.t8.service.StaffService;

@Controller
@RequestMapping("/employee")
public class LoginController {

	@Autowired
	@Qualifier("staffService")
	protected IEmployeeService sservice;

	@Autowired
	public void setIStaffService(StaffService sservice) {
		this.sservice = sservice;
	}

	@Autowired
	@Qualifier("adminService")
	protected IEmployeeService aservice;


	@Autowired
	public void setILeaveService(AdminService aservice) {
		this.aservice = aservice;}


	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	// admin-login

	@RequestMapping("/login2")
	public String adminLogin(Model model) { 
		Staff emp = new Staff();
		model.addAttribute("admin", emp);

		return "adminlogin";
	}

	@RequestMapping("/bianca")
	public String biancaJS(Model model) {
		Employee emp = new Employee();
		model.addAttribute("emp", emp);
		return "BiancaJS-adminedit";
	}

	@RequestMapping("/adminlogin")
	public String adminlogin(@ModelAttribute("admin") Employee emp,HttpSession session,Model model) {
		for(Admin a :((AdminService) aservice).findallAdmin()){
			System.out.println(a);
			if(emp.getName().equals(a.getName())){
				System.out.println("admin name exist");
				if (emp.getPassword().equals(a.getPassword())){
					System.out.println("admin password correct");
					session.setAttribute("user",a);					
					return "admin";
				}
				model.addAttribute("errorMsg","Password is not correct. Pls try again. OR ARE YOU AN ADMIN ? :)");
				return "error";
			}
		}
		model.addAttribute("errorMsg","You don't exist.");
		return "error";
	}

	// employee-login 
	@RequestMapping("/login")
	public String employeeLogin(Model model) { 
		Employee emp = new Employee();
		model.addAttribute("employee", emp);

		return "employeelogin";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "forward:/employee/home";
	}

	@RequestMapping("/employeelogin")
	public String LoginSuccessful(@ModelAttribute("employee") Employee emp, Model model,HttpSession session) {
		for(Employee e: ((StaffService) sservice).findAllNonAdminStaff()){
			System.out.println(e);
			if(emp.getName().equals(e.getName())){
				System.out.println("staff exist");
				if (emp.getPassword().equals(e.getPassword())){
					System.out.println("staff password correct");
					session.setAttribute("user",e);					
					return "leaves";
				}	
				model.addAttribute("errorMsg","Name or Password is not correct. Pls try again. OR ARE YOU AN ADMIN ? :)");
				return "error";
			}
		}
		model.addAttribute("errorMsg","You don't exist.");
		return "error";
	}



}
