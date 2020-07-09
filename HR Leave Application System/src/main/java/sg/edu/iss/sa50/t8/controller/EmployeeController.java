package sg.edu.iss.sa50.t8.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.Manager;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.service.IEmployeeService;
import sg.edu.iss.sa50.t8.service.LeaveServiceImpl;
import sg.edu.iss.sa50.t8.service.StaffService;
//split to architecture design controller
//need to discuss to shift methods to respective controllers
@Controller
@RequestMapping("/employee")
public class EmployeeController {
	//move 2 login methods into LoginControllers
	// admin
	// rest of methods all moved into admin controller

	//employee
	@Autowired
	@Qualifier("staffService")
	protected IEmployeeService sservice;

	@Autowired
	public void setIStaffService(StaffService sservice) {
		this.sservice = sservice;
	}
	@Autowired
	protected LeaveServiceImpl leaveservice;

	//employee
	@RequestMapping("/leaves")
	public String Leaves(@SessionAttribute("user") Employee emp, HttpSession session) {
		session.setAttribute("user", ((StaffService) sservice).findById(emp.getId()));
		
		return "leaves";
	}

	@RequestMapping("/movement-register")
	public String movementregister(@SessionAttribute("user") Employee emp,Model model) {
		if(!emp.getDiscriminatorValue().equals("Admin")) {
			if(emp.getDiscriminatorValue().equals("Staff")){
				Staff staff = (Staff) emp;			
				List<Staff> staffs = ((StaffService) sservice).findAllStaffbyManager(staff.getManager().getId());
				List<Leaves> leaves = new ArrayList<>();
				for(Staff s : staffs) {
					for(Leaves l: leaveservice.findAllLeavesByStaff(s)) {
						leaves.add(l);
					}
				}
				for(Leaves l: leaveservice.findAllLeavesByStaff(staff.getManager())) {
					leaves.add(l);
				}
				model.addAttribute("movelist",leaves.stream().sorted(Comparator.comparing(Leaves:: getStartDate).reversed()).toArray());
				return "movement-register";
			}
			if(emp.getDiscriminatorValue().equals("Manager")){
				Manager manager = (Manager) emp;
				List<Staff> staffs = ((StaffService) sservice).findAllStaffbyManager(manager.getId());
				List<Leaves> leaves = new ArrayList<>();
				for(Staff s : staffs) {
					for(Leaves l: leaveservice.findAllLeavesByStaff(s)) {
						leaves.add(l);
					}
				}
				for(Leaves l: leaveservice.findAllLeavesByStaff(manager)) {
					leaves.add(l);
				}
				model.addAttribute("movelist",leaves);
				return "movement-register";
			}

		}
		model.addAttribute("errorMsg","You have no access");
		return "error";

	}
}