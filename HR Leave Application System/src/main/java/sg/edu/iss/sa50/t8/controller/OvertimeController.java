package sg.edu.iss.sa50.t8.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.repository.StaffRepository;
import sg.edu.iss.sa50.t8.service.EmailService;
import sg.edu.iss.sa50.t8.service.IOvertimeService;
import sg.edu.iss.sa50.t8.service.OvertimeserviceImpl;

@Controller
@RequestMapping("/overtime")
public class OvertimeController {
	@Autowired
	StaffRepository srepo;
	
	@Autowired
	EmailService emailservice;
	
	@Autowired
	protected IOvertimeService overtimeService;

	@Autowired
	public void setIOvertimeService(OvertimeserviceImpl overtimeSerImpl) {
		this.overtimeService = overtimeSerImpl;
	}

	@RequestMapping("/claim")
	public String claim(Model model,HttpSession session) {
		model.addAttribute("overtime", new Overtime());
		Staff s = new Staff("staff1","staff1@gmail.com");
		model.addAttribute("staff", s);
		return "overtime-claim";
	}


	@RequestMapping("/history")
	public String History(Model model,@SessionAttribute("user") Employee emp) {
		model.addAttribute("overtime", overtimeService.findAllOvertimeByStaffId(emp.getId()));
		return "overtime-history";
	}

	/*
	 * @RequestMapping("/save") public String
	 * save(@ModelAttribute("overtime") @Valid Overtime overtime, BindingResult
	 * bindingResult,Model model) { if (bindingResult.hasErrors()) { return
	 * "overtime-claim"; } overtimeService.saveOvertime(overtime);
	 * model.addAttribute("overtime", overtimeService.findAllOvertimeByStaffId(6));
	 * return "overtime-history"; }
	 */

	@RequestMapping("/save")
	public String save(@ModelAttribute("overtime") @Valid Overtime overtime,
			BindingResult bindingResult,
			Model model,@SessionAttribute("user") Employee emp) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("error", "Overtime Date must be past date and claimed overtime hr can't be more than 24 hr");
			model.addAttribute("overtime", overtime);
			return "overtime-claim";
		}
		Date d1 = overtime.getOvertimeDate();
		if (d1.after(new Date())) {
			model.addAttribute("errorDate", "Overtime claim Date must not be future date.");
			model.addAttribute("overtime", overtime);
			return "overtime-claim";
		}
		overtime.setStaff((Staff) srepo.findById(emp.getId()).get());
		overtimeService.saveOvertime(overtime);
		emailservice.notifyStaffForOT(overtime);
		emailservice.notifyManagerForOT(overtime);
		
		model.addAttribute("overtime", overtimeService.findAllOvertimeByStaffId(emp.getId()));
		return "overtime-history";
	}
}
