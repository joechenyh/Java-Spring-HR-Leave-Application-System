package sg.edu.iss.sa50.t8.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sun.el.parser.ParseException;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.Manager;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.OvertimeStatus;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.repository.BlockedLeavesRepository;
import sg.edu.iss.sa50.t8.service.EmailService;
import sg.edu.iss.sa50.t8.service.ILeaveService;
import sg.edu.iss.sa50.t8.service.LeaveServiceImpl;
import sg.edu.iss.sa50.t8.service.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	@Autowired
	EmailService emailservice;

	@Autowired
	@Qualifier("managerService")
	protected ManagerService manService;
	@Autowired
	BlockedLeavesRepository blRepo;
	@Autowired
	public void setILeaveService(ManagerService manService) {
		this.manService = manService;
	}
	
	@Autowired
	protected ILeaveService leaveService;

	@Autowired
	public void setILeaveService(LeaveServiceImpl leaveSerImpl) {
		this.leaveService = leaveSerImpl;
	}

	@RequestMapping("/list")
	public String home() {
		return "home";
	}

	@RequestMapping("/staffList")
	public String stafflist(Model model, HttpSession session) {
		Employee emp = (Employee) session.getAttribute("user");
		if (emp.getDiscriminatorValue().equals("Manager")) {
			Manager man = (Manager) emp;
			model.addAttribute("suboridateList", ((ManagerService) manService).findSub(man));
			return "manager-dashboard";
		}else {
			model.addAttribute("errorMsg","You are not a manger, pls login as a manager first.");
			return "error";}
	}

	@RequestMapping("/staffLeaveHistoryList/{id}")
	public String staffLeaveHistory(Model model,
			HttpSession session,@PathVariable("id") Integer id) {
		Employee emp = (Employee) session.getAttribute("user");
		if (emp.getDiscriminatorValue().equals("Manager")) {
			Manager man = (Manager) emp;
			Staff sub = ((ManagerService) manService).findStaffById(id);
			if (sub.getManager().getId()==man.getId()) {
				model.addAttribute("Leaves",
						((ManagerService) manService).findAllLeaveByStaff(sub));
				return "manager-LeavesHistoryList";
			}else {
				model.addAttribute("errorMsg","Sorry you don't have authority. "
						+ "This staff is not your subordinate.");
				return "error";
			}
		}
		model.addAttribute("errorMsg","Sorry you don't have authority. Pls Login as a manager.");
		return "error";

	}


	@RequestMapping("/leavesAppForApprovalList")
	public String listforApproval(Model model, HttpSession session) {
		Employee emp = (Employee) session.getAttribute("user");
		if (emp.getDiscriminatorValue().equals("Manager")) {
			Manager man = (Manager) emp;
			model.addAttribute("Leaves", ((ManagerService) manService).findAllPendingLeaves(man));
			return "manager-leavesApprovalList";
		}
		model.addAttribute("errorMsg","Sorry you don't have authority. Pls Login as a manager.");
		return "error";
	}

	@RequestMapping("/leavesAppDetails/{id}") 
	public String showLeaveAppDetail(Model model, @PathVariable("id") Integer id, 
			HttpSession session) {
		Employee emp = (Employee) session.getAttribute("user");
		if (emp == null) {
			model.addAttribute("errorMsg","Sorry you haven't log in."
					+ "Pls Log in as a manager.");
			return "error";}
		if (emp.getDiscriminatorValue().equals("Manager")) {
			Manager man = (Manager) emp;
			Leaves l = ((ManagerService) manService).findById(id).get();
			if (l.getStaff().getManager().getId()== man.getId()) {
				session.setAttribute("leavesId", id);
				model.addAttribute("leaves", l);
				model.addAttribute("leaves", l);
				List<Leaves> matesLeavesList = ((ManagerService) manService).findAllSubLeavesByPeriod2(l, man);
				model.addAttribute("MatesLeaves", matesLeavesList);
				return "manager-leaveAppDetails";
			}
			else {
				model.addAttribute("errorMsg","Sorry you don't have authority. "
						+ "This staff is not your subordinate.");
				return "error";
			}
		} else {
			model.addAttribute("errorMsg","Sorry you don't have authority. "
					+ "Pls Log in as a manager.");
			return "error";
		}
	}

	@RequestMapping(value = "leavesAppDetails/respond")
	public String responseTrySessionID(HttpSession session,
			@RequestParam(value = "managerComment") String manCom,
			@RequestParam(value = "action", required = true) String action, 
			Model model) throws ParseException, java.text.ParseException {
		Integer id = (Integer) session.getAttribute("leavesId");
		Leaves leaves = manService.findById(id).get();
		if (action.equals("approve")) {
			// change status into approved
			manService.approveLeave(leaves);
			manService.setComment(leaves, manCom);
			emailservice.notifyStaff(leaves);
			session.removeAttribute("leavesId");
			return "forward:/manager/leavesAppForApprovalList";
		}
		if (action.equals("reject")) {
			// validate first: check if comment is not empty
			if (manCom.isEmpty()) {
				model.addAttribute("errorRem", "You must make comment before rejecting."); 
				model.addAttribute("leaves", leaves); 
				emailservice.notifyStaff(leaves);
				return "manager-leaveAppDetails";
				}
			manService.rejectLeave(leaves);
			manService.setComment(leaves, manCom);
			//Adding balance back
			if (leaves.getDiscriminatorValue().equals("Annual Leave")) {
				//{ if annual leave >14 days ) return days_W_weekends else return  duration
				AnnualLeave lcast = (AnnualLeave) leaves;
				long actualdays = duration(lcast.getStartDate(),lcast.getEndDate());
				if (actualdays>14) { 
					actualdays = ActualLeaveDays(lcast.getStartDate(),lcast.getEndDate());
				}
				System.out.println("rejecting AnnualLeave Application: add balance back" + actualdays);
				//add actualleavedays back into database this staff currentLeaveDays;
				long currBalance = leaveService.findCurAnnLeave(lcast.getStaff().getId());
				leaveService.updateCurAnnLeaveDate(lcast.getStaff().getId(), actualdays + currBalance);
				System.out.println("existingdays : " +  currBalance );
				
			}
			//if (medical leave) return actualLeaveDays
			if (leaves.getDiscriminatorValue().equals("Medical Leave")) {
				MedicalLeave lcast = (MedicalLeave) leaves;
				long actualdays = ActualLeaveDays(lcast.getStartDate(),lcast.getEndDate());
				//add actualleavedays back into database this staff currentLeaveDays;
				System.out.println("rejecting AnnualLeave Application: add balance back" + actualdays);
				long currBalance = leaveService.findMedAnnLeave(lcast.getStaff().getId());
				System.out.println("rejecting AnnualLeave Application: add balance back" + currBalance);
				leaveService.updateCurMedLeaveDate(lcast.getStaff().getId(), actualdays + currBalance);
			}
			if (leaves.getDiscriminatorValue().equals("Compensation Leave")) {
				System.out.println("rejecting CompLeave: add 4 OT hours balance back");
				CompensationLeave lcast = (CompensationLeave) leaves;
				int updateHr = ((ManagerService) manService).findTotalOTHoursByEmpId(lcast.getStaff().getId());
				((ManagerService) manService).updateTotalOTHoursByEmpId(lcast.getStaff().getId(), updateHr+4);
			}
			session.removeAttribute("leavesId");
			return "forward:/manager/leavesAppForApprovalList";
		}
		return "forward:/manager/leavesAppForApprovalList";
	}

	//Overtime
	@RequestMapping("/overtimelist")
	public String approveovertime(@SessionAttribute("user") Employee emp, Model model) {
		if(emp.getDiscriminatorValue().equals("Manager")) {
			model.addAttribute("overtimelist",manService.findStaffOvertime((Manager) emp).stream().filter(x -> x.getOverTimeStatus() == OvertimeStatus.Applied).toArray());

			return "manager-OTApprovalList";
		}
		model.addAttribute("errorMsg","Sorry you don't have authority. Pls Login as a manager.");
		return "error";
	}

	@RequestMapping("/overtimeprocessed")
	public String overtimeprocessed(@SessionAttribute("user") Employee emp,Overtime et, Model model) {
		if(emp.getDiscriminatorValue().equals("Manager")) {
			
			//			fetching OT from db
			Overtime newOT = (Overtime) manService.findOvertime(et.getId());
			System.out.println("OT fetched from db");
			System.out.println(newOT);

			//			setting status from manager
			newOT.setOverTimeStatus(et.getOverTimeStatus());

			//adding OT hours when necessary
			if (newOT.getOverTimeStatus().equals(OvertimeStatus.Approved)) {
				manService.AddOvertimeHours(newOT);;
			}

			//			save records in db
			manService.SetOTStatus(newOT);
			System.out.println("done saving");

			//			notify staff via email
			emailservice.notifyStaffForOT(newOT);
			System.out.println("Staff notification email sent");

			//			redirect
			return "forward:/manager/overtimelist";
		}

		model.addAttribute("errorMsg","Sorry you don't have authority. Pls Login as a manager.");
		return "error";
	}
	
	// Below just static methods for date calculation?
	public static int saturdaysundaycount(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		int sundays = 0;
		int saturday = 0;

		while (!c1.after(c2)) {
			if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				saturday++;
			}
			if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				sundays++;
			}

			c1.add(Calendar.DATE, 1);
		}

		System.out.println("Saturday Count = " + saturday);
		System.out.println("Sunday Count = " + sundays);
		return saturday + sundays;
	}

	// check duration of date
	public static long duration(Date time1, Date time2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time1);
		cal.setTime(time2);
		long diff = (time2.getDate() - time1.getDate()) + 1;
		return diff;
	}

	// check and compare between startdate and enddate;
	public static boolean compareDates(Date d1, Date d2) {
		boolean status = false;
		if (d1.after(d2)) {
			System.out.println("Date1 is after Date2");
			status = false;
		} else if (d1.before(d2)) {
			System.out.println("Date1 is before Date2");
			status = true;
		}

		return status;
	}

	// check blocked leave days
	public int blockedLeave(Date startDate, Date endDate) throws ParseException, java.text.ParseException {
		ArrayList<Date> dlist = (ArrayList<Date>) blRepo.findAllDates();
		int count = 0;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(endDate);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		String newDate = sdf.format(cal.getTime());
		Date da = sdf.parse(newDate);
		System.out.println("Incremnted current date by one: " + newDate);

		long duration = duration(startDate, endDate);
		System.out.println("duration" + duration);
		List<Date> lDate = getDatesBetweenUsingJava7(startDate, da);
		for (Date d : lDate) {
			Calendar c4 = Calendar.getInstance();
			c4.setTime(d);
			System.out.println(d);
			for (Date holidays : dlist) {
				Calendar c3 = Calendar.getInstance();
				c3.setTime(holidays);
				if (c3.get(Calendar.YEAR) == c4.get(Calendar.YEAR)) {
					if (c3.get(Calendar.MONTH) == c4.get(Calendar.MONTH)) {
						if (c4.get(Calendar.DAY_OF_MONTH) == c3.get(Calendar.DAY_OF_MONTH)) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	//retrieve list of dates
	public static List<Date> getDatesBetweenUsingJava7(Date startDate, Date endDate) {
		List<Date> datesInRange = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		while (calendar.before(endCalendar)) {
			Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return datesInRange;
	}

	public long ActualLeaveDays(Date d1, Date d2) throws ParseException, java.text.ParseException {
		int blcount = blockedLeave(d1, d2);
		long duration = duration(d1, d2);
		int satsundays = saturdaysundaycount(d1, d2);
		long actualleavedays = duration - blcount - satsundays;

		return actualleavedays;
	}
	
}
