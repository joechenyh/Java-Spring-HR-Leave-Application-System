package sg.edu.iss.sa50.t8.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.service.AdminService;
import sg.edu.iss.sa50.t8.service.IEmployeeService;

@Configuration
@EnableScheduling
public class SchedulerController {
	
	@Autowired
	@Qualifier("adminService")
	private IEmployeeService aservice;
	
	@Autowired
	public void setILeaveService(AdminService aservice) {
		this.aservice = aservice;
	}
	
	@Autowired
	private ApiController controller;
	
	//trigger at 12 noon on the first day of every month
	@Scheduled(cron = "0 0 12 1 * ?") 
	public void scheduledLeaveIncrement() {
		List<Staff> list = ((AdminService) aservice).findAllNonAdminStaff();
		list.stream().forEach(e -> {
			e.setAnnualLeaveDays(e.getAnnualLeaveDays() + e.getTotalAnnualLeaves()/12);
			((AdminService) aservice).save(e);
		});
	}
	
	//trigger at 00:00 on day-of-month 1 in January for every year to save public holiday from API
	@Scheduled(cron = "0 0 0 1 1 *")
	public void scheduledSetBlockedleave() {
		controller.setBlockedLeavesOnScheduled();
	}
	/*
	//Testing purposes only - scheduled set public holiday from API
	@Scheduled(fixedRate = 10000)
	public void scheduledSetBlockedleavetest() {
		System.out.println("\nSeeding holidays from API at 10 sec interval from scheduler - testing purposes\n");
		controller.setBlockedLeavesOnScheduled();
	}
	*/
	/*
	//Testing purposes only
	@Scheduled(fixedRate = 10000)
	public void scheduleIncrementLeaves() {
		List<Staff> list = ((AdminService) aservice).findAllNonAdminStaff();
		list.stream().forEach(e -> {
			System.out.print(e+ " ");
			System.out.println("AL: "+e.getAnnualLeaveDays());
		});
		
		list.stream().forEach(e -> {
			e.setAnnualLeaveDays(e.getAnnualLeaveDays() + e.getTotalAnnualLeaves()/12);
			((AdminService) aservice).save(e);
		});
		
		
		list.stream().forEach(e -> {
			System.out.print(e + " ");
			System.out.println("AL: "+e.getAnnualLeaveDays());
		});
	}*/
}
