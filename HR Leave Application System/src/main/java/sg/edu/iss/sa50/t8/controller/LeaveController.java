package sg.edu.iss.sa50.t8.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.sun.el.parser.ParseException;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.LeaveStatus;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.repository.BlockedLeavesRepository;
import sg.edu.iss.sa50.t8.repository.StaffRepository;
import sg.edu.iss.sa50.t8.service.EmailService;
import sg.edu.iss.sa50.t8.service.IEmployeeService;
import sg.edu.iss.sa50.t8.service.ILeaveService;
import sg.edu.iss.sa50.t8.service.LeaveServiceImpl;
import sg.edu.iss.sa50.t8.service.StaffService;

//split to architecture design controller
//need to discuss to shift methods to respective controllers
@Controller
@RequestMapping("/leaves")
public class LeaveController {

	private static final LeaveStatus Applied = null;
	@Autowired
	@Qualifier("staffService")
	protected IEmployeeService stService;

	@Autowired
	public void setIEmployeeService(StaffService stService) {
		this.stService = stService;
	}



	@Autowired
	BlockedLeavesRepository blRepo;

	@Autowired
	EmailService ems;

	@Autowired
	StaffRepository srepo;

	@Autowired
	protected ILeaveService leaveService;

	@Autowired
	public void setILeaveService(LeaveServiceImpl leaveSerImpl) {
		this.leaveService = leaveSerImpl;
	}

	@RequestMapping("/apply")
	public String apply() {
		return "leaves-apply";
	}

	@RequestMapping("/detail/{id}")
	public String detail(@RequestParam("formId") String formId, @PathVariable("id") Integer id, Model model,
			@SessionAttribute("user") Employee emp) {
		String lType = leaveService.findLeaveTypeById(id);
		if (lType.equalsIgnoreCase("Annual Leave")) {
			long curAnn = leaveService.findCurAnnLeave(emp.getId());
			model.addAttribute("eCurAnnLeave", curAnn);
			model.addAttribute("formId", formId);
			model.addAttribute("annualLeave", leaveService.findLeaveById(id));
			// return "leaves-update-annual";
			return "view-detail-annual";
		} else if (lType.equalsIgnoreCase("Medical Leave")) {
			long medAnn = leaveService.findMedAnnLeave(emp.getId());
			model.addAttribute("eMedAnnLeave", medAnn);
			model.addAttribute("formId", formId);
			model.addAttribute("medicalLeave", leaveService.findLeaveById(id));
			// return "leaves-update-medical";
			return "view-detail-medical";
		} else {
			model.addAttribute("formId", formId);
			model.addAttribute("compensationLeave", leaveService.findLeaveById(id));
			// return "leaves-apply-compensation";
			return "view-detail-compensation";
		}
	}

	@RequestMapping("/annualAdd")
	public String annualaddForm(@SessionAttribute("user") Employee emp, Model model) {
		model.addAttribute("annualLeave", new AnnualLeave());
		long curAnn = leaveService.findCurAnnLeave(emp.getId());
		model.addAttribute("eCurAnnLeave", curAnn);
		return "leaves-apply-annual";
	}

	@RequestMapping("/medicalAdd")
	public String medicaladdForm(@SessionAttribute("user") Employee emp, Model model) {
		model.addAttribute("medicalLeave", new MedicalLeave());
		long medAnn = leaveService.findMedAnnLeave(emp.getId());
		model.addAttribute("eMedAnnLeave", medAnn);
		return "leaves-apply-medical";
	}

	@RequestMapping("/compensationAdd")
	public String compensationaddForm(Model model) {
		model.addAttribute("compensationLeave", new CompensationLeave());
		return "leaves-apply-compensation";
	}

	@RequestMapping("/annual/save")
	public String saveAnnualForm(@ModelAttribute("annualLeave") @Valid AnnualLeave annualLeave,
			BindingResult bindingResult, Model model, @SessionAttribute("user") Employee emp)
					throws ParseException, java.text.ParseException {

		long curAnn = leaveService.findCurAnnLeave(emp.getId());
		model.addAttribute("eCurAnnLeave", curAnn);
		if (bindingResult.hasErrors()) {
			return "leaves-apply-annual";
		}

		Date d1 = annualLeave.getStartDate();
		Date d2 = annualLeave.getEndDate();
//		if (!d1.after(new Date())) {
//			model.addAttribute("errorDate", "Start Date must be future date.");
//			model.addAttribute("annualLeave", annualLeave);
//			return "leaves-apply-annual";
//		}
//		if (!d2.after(new Date())) {
//			model.addAttribute("errorEndDate", "End Date must be future date.");
//			model.addAttribute("annualLeave", annualLeave);
//			return "leaves-apply-annual";
//		}
		long duration = duration(d1, d2);
		int satsunCount = saturdaysundaycount(d1, d2);
		long actualleavesdays = ActualLeaveDays(d1, d2);
		boolean startDateBeforeEndDate = compareDates(d1, d2);

		long existingDays = leaveService.findCurAnnLeave(emp.getId());
		annualLeave.setStaff((Staff) srepo.findById(emp.getId()).get()); // use session later
		annualLeave.setStatus(LeaveStatus.Applied);

		List<AnnualLeave> al = leaveService.findAllAnnualLeavesByStaffId((Staff)emp);
		for(AnnualLeave a : al) {
			if(annualLeave.getStatus().equals(LeaveStatus.Applied) || 
					annualLeave.getStatus().equals(LeaveStatus.Approved)) {
				Date oldd1 = a.getStartDate();
				Date oldd2 = a.getEndDate();
				List<Date> olddates = getDatesBetweenUsingJava7(oldd1, oldd2);
				for(Date d : olddates) {
					Calendar c4 = Calendar.getInstance();
					c4.setTime(d);
					System.out.println(d);					
					Calendar c3 = Calendar.getInstance();
					c3.setTime(annualLeave.getStartDate());				
					if (c3.get(Calendar.YEAR) == c4.get(Calendar.YEAR)) {
						if (c3.get(Calendar.MONTH) == c4.get(Calendar.MONTH)) {
							if (c4.get(Calendar.DAY_OF_MONTH) == c3.get(Calendar.DAY_OF_MONTH)) {
								model.addAttribute("errorSameDate", "You have applied on the same date.");
								model.addAttribute("annualLeave", annualLeave);
								return "leaves-apply-annual";
							}
						}
					}

				}}
		}
		if (annualLeave.getStatus().equals(LeaveStatus.Applied) && existingDays >= actualleavesdays
				&& startDateBeforeEndDate == true) {
			if (duration <= 14) {
				long weekendExclude = duration - satsunCount;
				leaveService.updateCurAnnLeaveDate(emp.getId(), existingDays - weekendExclude);
				ems.notifyManager(annualLeave);
				ems.notifyStaff(annualLeave);
				leaveService.saveAnnualLeave(annualLeave);
				model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
				return "leaves-history";
			} else {
				leaveService.updateCurAnnLeaveDate(emp.getId(), existingDays - actualleavesdays);
				ems.notifyManager(annualLeave);
				ems.notifyStaff(annualLeave);
				leaveService.saveAnnualLeave(annualLeave);
				model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
				return "leaves-history";
			}
		} else if (existingDays < actualleavesdays) {
			model.addAttribute("error1", "You have not enough eligible leave days to apply this.");
			return "leaves-apply-annual";
		} 

		else {
			model.addAttribute("error", "Start date must be earlier than end date.");
			return "leaves-apply-annual";
		}


	}

	@RequestMapping("/medical/save")
	public String saveMedicalForm(@ModelAttribute("medicalLeave") @Valid MedicalLeave medicalLeave,
			BindingResult bindingResult, Model model, @SessionAttribute("user") Employee emp)
					throws ParseException, java.text.ParseException {
		long medAnn = leaveService.findMedAnnLeave(emp.getId());
		model.addAttribute("eMedAnnLeave", medAnn);
		if (bindingResult.hasErrors()) {
			return "leaves-apply-medical";
		}
		Date d1 = medicalLeave.getStartDate();
		Date d2 = medicalLeave.getEndDate();
//
//		if (!d1.after(new Date())) {
//			model.addAttribute("errorDate", "Start Date must be future date.");
//			model.addAttribute("medicalLeave", medicalLeave);
//			return "leaves-apply-medical";
//		}
//		if (!d2.after(new Date())) {
//			model.addAttribute("errorEndDate", "End Date must be future date.");
//			model.addAttribute("medicalLeave", medicalLeave);
//			return "leaves-apply-medical";
//		}

		long duration = duration(d1, d2);
		// long actualleavesdays = ActualLeaveDays(d1, d2);
		boolean startDateBeforeEndDate = compareDates(d1, d2);
		long existingDays = leaveService.findMedAnnLeave(emp.getId());
		medicalLeave.setStaff((Staff) srepo.findById(emp.getId()).get()); // use session later
		medicalLeave.setStatus(LeaveStatus.Applied);

		List<MedicalLeave> ml = leaveService.findAllMedicalLeavesByStaffId((Staff)emp);
		for(MedicalLeave a : ml) {
			if(medicalLeave.getStatus().equals(LeaveStatus.Applied) || 
					medicalLeave.getStatus().equals(LeaveStatus.Approved)) {
				Date oldd1 = a.getStartDate();
				Date oldd2 = a.getEndDate();
				List<Date> olddates = getDatesBetweenUsingJava7(oldd1, oldd2);
				for(Date d : olddates) {
					Calendar c4 = Calendar.getInstance();
					c4.setTime(d);
					System.out.println(d);					
					Calendar c3 = Calendar.getInstance();
					c3.setTime(medicalLeave.getStartDate());				
					if (c3.get(Calendar.YEAR) == c4.get(Calendar.YEAR)) {
						if (c3.get(Calendar.MONTH) == c4.get(Calendar.MONTH)) {
							if (c4.get(Calendar.DAY_OF_MONTH) == c3.get(Calendar.DAY_OF_MONTH)) {
								model.addAttribute("errorSameDate", "You have applied on the same date.");
								model.addAttribute("medicalLeave", medicalLeave);
								return "leaves-apply-medical";
							}
						}
					}

				}}
		}
		if (medicalLeave.getStatus().equals(LeaveStatus.Applied) && existingDays >= duration
				&& startDateBeforeEndDate == true && duration <= 60) {
			leaveService.updateCurMedLeaveDate(emp.getId(), existingDays - duration);
			ems.notifyManager(medicalLeave);
			ems.notifyStaff(medicalLeave);
			leaveService.saveMedicalLeave(medicalLeave);
			model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
			return "leaves-history";
		} else if (existingDays < duration) {
			model.addAttribute("error1", "You have not enough eligible leave days to apply this.");
			return "leaves-apply-medical";
		} else {
			model.addAttribute("error", "Start date must be earlier than end date.");
			return "leaves-apply-medical";
		}

	}

	@RequestMapping("/compensation/save")
	public String saveCompensationForm(@ModelAttribute("compLeave") CompensationLeave compLeave, Model model,
			@SessionAttribute("user") Employee emp) {
		compLeave.setStaff((Staff) srepo.findById(emp.getId()).get()); // use session later
		compLeave.setStatus(LeaveStatus.Applied);
		// int totalOtHr =((StaffService) stService).findTotalOTHoursByEmpId(6);
		int totalOtHr = ((StaffService) stService).findTotalOTHoursByEmpId(emp.getId());
		Date startDate = compLeave.getStartDate();

		if (!startDate.after(new Date())) {
			model.addAttribute("errorDate", "Start Date must be future date.");
			model.addAttribute("compensationLeave", compLeave);
			return "leaves-apply-compensation";
		}
		if (totalOtHr < 4) {
			model.addAttribute("error", "Sorry!You don't have eligilibility to apply this compensation leave.");
			model.addAttribute("compensationLeave", compLeave);
			return "leaves-apply-compensation";
		}

		List<CompensationLeave> cl = leaveService.findAllCompensationLeavesByStaffId((Staff)emp);
		for(CompensationLeave a : cl) {
			if(compLeave.getStatus().equals(LeaveStatus.Applied) || 
					compLeave.getStatus().equals(LeaveStatus.Approved)) {
				Date oldd1 = a.getStartDate();
				Calendar c4 = Calendar.getInstance();
				c4.setTime(oldd1);
				System.out.println(oldd1);					
				Calendar c3 = Calendar.getInstance();
				c3.setTime(compLeave.getStartDate());				
				if (c3.get(Calendar.YEAR) == c4.get(Calendar.YEAR)) {
					if (c3.get(Calendar.MONTH) == c4.get(Calendar.MONTH)) {
						if (c4.get(Calendar.DAY_OF_MONTH) == c3.get(Calendar.DAY_OF_MONTH)) {
							if(a.getClaimQuota().equalsIgnoreCase(compLeave.getClaimQuota())) {
								System.out.println(a.getClaimQuota());
								model.addAttribute("errorSameQuota", "You have applied on the same Quota.");
								model.addAttribute("compensationLeave", compLeave);
								return "leaves-apply-compensation";
							}

						}
					}
				}

			}}


		int updateHr = ((StaffService) stService).findTotalOTHoursByEmpId(emp.getId());
		updateHr -= 4;
		((StaffService) stService).updateTotalOTHoursByEmpId(emp.getId(), updateHr);
		leaveService.saveCompensationLeave(compLeave);
		ems.notifyStaff(compLeave);
		ems.notifyManager(compLeave);
		model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
		return "leaves-history";
	}

	@RequestMapping("/update/annual")
	public String updateAnnualForm(@ModelAttribute("annualLeave") @Valid AnnualLeave annualLeave,
			BindingResult bindingResult, Model model, @SessionAttribute("user") Employee emp)
					throws ParseException, java.text.ParseException {
		long curAnn = leaveService.findCurAnnLeave(emp.getId());
		model.addAttribute("eCurAnnLeave", curAnn);
		if (bindingResult.hasErrors()) {
			return "leaves-update-annual";
		}
		AnnualLeave oldAnnualLeave = leaveService.findAnnualLeaveById(annualLeave.getId());
		Date oldd1 = oldAnnualLeave.getStartDate();
		Date oldd2 = oldAnnualLeave.getEndDate();
		long oldActual = ActualLeaveDays(oldd1, oldd2);
		long existingDays = leaveService.findCurAnnLeave(emp.getId());
		long totaloldLeaves = oldActual + existingDays;

		Date d1 = annualLeave.getStartDate();
		Date d2 = annualLeave.getEndDate();
		long actualleavesdays = ActualLeaveDays(d1, d2);

		boolean startDateBeforeEndDate = compareDates(d1, d2);
		annualLeave.setStaff((Staff) srepo.findById(emp.getId()).get()); // use session later
		annualLeave.setStatus(LeaveStatus.Applied);
		if (annualLeave.getStatus().equals(LeaveStatus.Applied) && existingDays >= actualleavesdays
				&& startDateBeforeEndDate == true) {
			if (oldd1 == d1 || oldd2 == d2) {
				model.addAttribute("errorSameDate", "You have applied on the same date.");
				model.addAttribute("annualLeave", annualLeave);
				return "leaves-update-annual";
			} else {
				long updateActualLeaves = totaloldLeaves - actualleavesdays;
				leaveService.updateCurAnnLeaveDate(emp.getId(), updateActualLeaves);
				leaveService.saveAnnualLeave(annualLeave);
				leaveService.updateLeaveStatus(annualLeave.getId(), LeaveStatus.Updated);
				model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
				return "leaves-history";
			}
		} else {
			model.addAttribute("error", "You are not eligible");
			return "leaves-update-annual";
		}

	}

	@RequestMapping("/update/medical")
	public String updateMedicalForm(@ModelAttribute("medicalLeave") @Valid MedicalLeave medicalLeave,
			BindingResult bindingResult, Model model, @SessionAttribute("user") Employee emp)
					throws ParseException, java.text.ParseException {
		long curAnn = leaveService.findMedAnnLeave(emp.getId());
		model.addAttribute("eCurAnnLeave", curAnn);
		if (bindingResult.hasErrors()) {
			return "leaves-update-medical";
		}
		MedicalLeave oldAnnualLeave = leaveService.findMedicalLeaveById(medicalLeave.getId());
		Date oldd1 = oldAnnualLeave.getStartDate();
		Date oldd2 = oldAnnualLeave.getEndDate();
		long oldActual = ActualLeaveDays(oldd1, oldd2);
		long existingDays = leaveService.findMedAnnLeave(emp.getId());
		long totaloldLeaves = oldActual + existingDays;

		Date d1 = medicalLeave.getStartDate();
		Date d2 = medicalLeave.getEndDate();
		long actualleavesdays = ActualLeaveDays(d1, d2);

		boolean startDateBeforeEndDate = compareDates(d1, d2);
		medicalLeave.setStaff((Staff) srepo.findById(emp.getId()).get()); // use session later
		medicalLeave.setStatus(LeaveStatus.Applied);
		if (medicalLeave.getStatus().equals(LeaveStatus.Applied) && existingDays >= actualleavesdays
				&& startDateBeforeEndDate == true) {
			long updateActualLeaves = totaloldLeaves - actualleavesdays;
			leaveService.updateCurMedLeaveDate(emp.getId(), updateActualLeaves);
			leaveService.saveMedicalLeave(medicalLeave);
			model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
			return "leaves-history";
		} else {
			model.addAttribute("error", "You are not eligible");
			return "leaves-update-medical";
		}

	}

	@RequestMapping("/history")
	public String History(Model model, @SessionAttribute("user") Employee emp) {
		model.addAttribute("Leaves", leaveService.findAllLeaves(emp.getId()));
		return "leaves-history";
	}

	@RequestMapping(value = "/cancel/{id}")
	public String cancel(@PathVariable("id") Integer id, Model model) throws ParseException, java.text.ParseException {
		Leaves l = leaveService.findLeaveById(id);
		String lType = l.getDiscriminatorValue();

		if (lType.equalsIgnoreCase("Annual Leave")) {
			AnnualLeave al = leaveService.findAnnualLeaveById(id);
			if (al.getStatus().equals(LeaveStatus.Approved)) {
				Date d1 = al.getStartDate();
				Date d2 = al.getEndDate();
				long actualleavedays = ActualLeaveDays(d1, d2);
				System.out.println("An Appl:" + actualleavedays);
				long existingDays = leaveService.findCurAnnLeave(al.getStaff().getId());
				leaveService.updateCurAnnLeaveDate(al.getStaff().getId(), actualleavedays + existingDays);

			}
		} else if (lType.equalsIgnoreCase("Medical Leave")) {
			MedicalLeave ml = leaveService.findMedicalLeaveById(id);
			if (ml.getStatus().equals(LeaveStatus.Approved)) {
				Date d1 = ml.getStartDate();
				Date d2 = ml.getEndDate();
				long duration = duration(d1, d2);
				//long actualleavedays = ActualLeaveDays(d1, d2);
				System.out.println("An Appl:" + duration);
				long existingDays = leaveService.findMedAnnLeave(ml.getStaff().getId());
				leaveService.updateCurMedLeaveDate(ml.getStaff().getId(), duration + existingDays);
			}
		} else {
			if (l.getStatus().equals(LeaveStatus.Approved)) {
				int updateHr = ((StaffService) stService).findTotalOTHoursByEmpId(l.getStaff().getId());
				updateHr += 4;
				((StaffService) stService).updateTotalOTHoursByEmpId(l.getStaff().getId(), updateHr);
			}
		}
		leaveService.updateLeaveStatus(id, LeaveStatus.Cancelled);
		Leaves toSave = leaveService.findLeaveById(id);
		System.out.println(toSave.getStatus());

		if(toSave.getStatus().equals(LeaveStatus.Cancelled)) {
			System.out.println(toSave.getStatus());
			ems.confirmStaffCancellation(toSave);
			ems.confirmStaffCancellationToManager(toSave);
		}
		return "forward:/leaves/history";
	}

	@RequestMapping(value = "/update/{id}")
	public String update(@PathVariable("id") Integer id, Model model, @SessionAttribute("user") Employee emp) {
		String lType = leaveService.findLeaveTypeById(id);
		if (lType.equalsIgnoreCase("Annual Leave")) {
			long curAnn = leaveService.findCurAnnLeave(emp.getId());
			model.addAttribute("eCurAnnLeave", curAnn);
			model.addAttribute("annualLeave", leaveService.findLeaveById(id));
			return "leaves-update-annual";
		} else if (lType.equalsIgnoreCase("Medical Leave")) {
			long medAnn = leaveService.findMedAnnLeave(emp.getId());
			model.addAttribute("eMedAnnLeave", medAnn);
			model.addAttribute("medicalLeave", leaveService.findLeaveById(id));
			return "leaves-update-medical";
		} else {
			model.addAttribute("compensationLeave", leaveService.findLeaveById(id));
			return "leaves-apply-compensation";
		}
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Integer id, Model model) throws ParseException, java.text.ParseException {
		Leaves l = leaveService.findLeaveById(id);
		String lType = l.getDiscriminatorValue();
		if (lType.equalsIgnoreCase("Annual Leave")) {
			AnnualLeave al = leaveService.findAnnualLeaveById(id);
			if (al.getStatus().equals(LeaveStatus.Applied)) {
				Date d1 = al.getStartDate();
				Date d2 = al.getEndDate();
				long actualleavedays = ActualLeaveDays(d1, d2);
				System.out.println("An Appl:" + actualleavedays);
				long existingDays = leaveService.findCurAnnLeave(al.getStaff().getId());
				System.out.println("Annual Delete Day Add: " + actualleavedays + existingDays);
				leaveService.updateCurAnnLeaveDate(al.getStaff().getId(), actualleavedays + existingDays);

			}
		} else if (lType.equalsIgnoreCase("Medical Leave")) {
			MedicalLeave ml = leaveService.findMedicalLeaveById(id);
			if (ml.getStatus().equals(LeaveStatus.Applied)) {
				Date d1 = ml.getStartDate();
				Date d2 = ml.getEndDate();
				//long actualleavedays = ActualLeaveDays(d1, d2);
				long duration = duration(d1, d2);
				System.out.println("An Appl:" + duration);
				long existingDays = leaveService.findMedAnnLeave(ml.getStaff().getId());
				leaveService.updateCurMedLeaveDate(ml.getStaff().getId(), duration + existingDays);
			}
		} else {
			if (l.getStatus().equals(LeaveStatus.Applied)) {
				int updateHr = ((StaffService) stService).findTotalOTHoursByEmpId(l.getStaff().getId());
				updateHr += 4;
				((StaffService) stService).updateTotalOTHoursByEmpId(l.getStaff().getId(), updateHr);
			}
		}
		leaveService.updateLeaveStatus(id, LeaveStatus.Deleted);
		return "forward:/leaves/history";
	}

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
		else if(d1.equals(d2)) {
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

	// retrieve list of dates
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

	// acutal leave days excluding sat and sun, holildays
	public long ActualLeaveDays(Date d1, Date d2) throws ParseException, java.text.ParseException {
		int blcount = blockedLeave(d1, d2);
		long duration = duration(d1, d2);
		int satsundays = saturdaysundaycount(d1, d2);
		long actualleavedays = duration - blcount - satsundays;

		return actualleavedays;
	}
}
