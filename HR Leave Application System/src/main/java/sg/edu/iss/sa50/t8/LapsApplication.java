package sg.edu.iss.sa50.t8;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import sg.edu.iss.sa50.t8.model.Admin;
import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.BlockedLeaves;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.LeaveStatus;
import sg.edu.iss.sa50.t8.model.Manager;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.OvertimeStatus;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.repository.BlockedLeavesRepository;
import sg.edu.iss.sa50.t8.repository.EmployeeRepository;
import sg.edu.iss.sa50.t8.repository.LeaveRepository;
import sg.edu.iss.sa50.t8.repository.OvertimeRepository;
import sg.edu.iss.sa50.t8.service.EmailService;

@SpringBootApplication
public class LapsApplication {

	@Autowired
	EmployeeRepository empRepo;
	/*
	 * @Autowired StaffRepository stfRepo;
	 * 
	 * @Autowired AdminRepository admRepo;
	 * 
	 * @Autowired ManagerRepository manRepo;
	 */
	@Autowired
	LeaveRepository lRepo;
	@Autowired
	OvertimeRepository oRepo;
	
	@Autowired
	EmailService ems;
	
	@Autowired
	BlockedLeavesRepository blRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(LapsApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Let's start to see our models! ");
			
			Admin adm1 = new Admin("Martin","martin.dreamz@hotmail.com");
			Admin adm2 = new Admin("Martin2","e0533410@u.nus.edu");
			Manager man1 = new Manager("Joe","martin.dreamz@hotmail.com",null,16,20);
			Manager man2 = new Manager("Joe2","e0533363@u.nus.edu",man1,16,15);
			Manager man3 = new Manager("Joe3","martin.dreamz@hotmail.com",man2,15,15);
			Staff s1 = new Staff("Martin1","martin.dreamz@hotmail.com",man2,(long)10,(long)10);
			s1.setTotalOTHours(8);
			Staff s2 = new Staff("Bianca2","e0533381@u.nus.edu",man1,(long)12,(long)10);
			Staff s3 = new Staff("Bianca3","e0533381@u.nus.edu",man3,(long)14,(long)10);
			Staff s4 = new Staff("Yirui","ye0533384@u.nus.edu",man1,(long)20,(long)60);
			Staff s5 = new Staff("Yirui2","martin.dreamz@hotmail.com",man2,(long)4,(long)18);
			Staff s6 = new Staff("Yirui3","e0533363@u.nus.edu",man3,(long)22,(long)60);
			Staff s7 = new Staff("Daryl","de0533363@u.nus.edu",man1,(long)22,(long)60);
			Staff s8 = new Staff("Daryl2","de0533363@u.nus.edu",man2,(long)22,(long)60);
			Staff s9 = new Staff("Daryl3","de0533363@u.nus.edu",man3,(long)22,(long)60);
			Staff s10 = new Staff("Theingi","te0533363@u.nus.edu",man1,(long)22,(long)60);
			Staff s11 = new Staff("Theingi2","te0533363@u.nus.edu",man2,(long)22,(long)60);
			Staff s12 = new Staff("Theingi3","te0533363@u.nus.edu",man3,(long)22,(long)60);
			Staff s13 = new Staff("WuttYee","we0533363@u.nus.edu",man1,(long)22,(long)60);
			Staff s14 = new Staff("WuttYee2","we0533363@u.nus.edu",man2,(long)22,(long)60);
			Staff s15 = new Staff("WuttYee3","we0533363@u.nus.edu",man3,(long)22,(long)60);
			
			
			
			
			  //set max leaves man1.setAnnualLeaveDays(18); man2.setAnnualLeaveDays(18);
			/*
			 * man3.setAnnualLeaveDays(18); s1.setAnnualLeaveDays(14);
			 * s2.setAnnualLeaveDays(14); s3.setAnnualLeaveDays(14);
			 * s4.setAnnualLeaveDays(14); s5.setAnnualLeaveDays(14);
			 * s6.setAnnualLeaveDays(14); s7.setAnnualLeaveDays(14);
			 * s8.setAnnualLeaveDays(14); s9.setAnnualLeaveDays(14);
			 * s10.setAnnualLeaveDays(14); s11.setAnnualLeaveDays(14);
			 * s12.setAnnualLeaveDays(14); s13.setAnnualLeaveDays(14);
			 * s14.setAnnualLeaveDays(14); s15.setAnnualLeaveDays(14);
			 */
			  
			  man1.setTotalAnnualLeaves(18);
			  man2.setTotalAnnualLeaves(18);
			  man3.setTotalAnnualLeaves(18);
			  
			  s1.setTotalAnnualLeaves(14);
			  s2.setTotalAnnualLeaves(14);
			  s3.setTotalAnnualLeaves(14);
			  s4.setTotalAnnualLeaves(14);
			  s5.setTotalAnnualLeaves(14);
			  s6.setTotalAnnualLeaves(14);
			  s7.setTotalAnnualLeaves(14);
			  s8.setTotalAnnualLeaves(14);
			  s9.setTotalAnnualLeaves(14);
			  s10.setTotalAnnualLeaves(14);
			  s11.setTotalAnnualLeaves(14);
			  s12.setTotalAnnualLeaves(14);
			  s13.setTotalAnnualLeaves(14);
			  s14.setTotalAnnualLeaves(14);
			  s15.setTotalAnnualLeaves(14);
			  
			  
			  
			 
			 
			
			
			
			Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse("08/01/2020");
			Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse("08/03/2020");

			Date d3 = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2019");
			Date d4 = new SimpleDateFormat("MM/dd/yyyy").parse("01/05/2019");
			
			Date d5 = new SimpleDateFormat("MM/dd/yyyy").parse("09/09/2020");
			Date d6 = new SimpleDateFormat("MM/dd/yyyy").parse("09/12/2020");
			
			Date d7 = new SimpleDateFormat("MM/dd/yyyy").parse("08/08/2020");
			Date d8 = new SimpleDateFormat("MM/dd/yyyy").parse("08/12/2020");
			
			Date d9 = new SimpleDateFormat("MM/dd/yyyy").parse("07/10/2020");
			Date d10 = new SimpleDateFormat("MM/dd/yyyy").parse("07/15/2020");
			
			Date d11 = new SimpleDateFormat("MM/dd/yyyy").parse("08/05/2020");
			Date d12 = new SimpleDateFormat("MM/dd/yyyy").parse("08/08/2020");
			
			Date d13 = new SimpleDateFormat("MM/dd/yyyy").parse("01/05/2019");
			Date d14 = new SimpleDateFormat("MM/dd/yyyy").parse("01/08/2019");
			
			Date d20 = new SimpleDateFormat("MM/dd/yyyy").parse("07/02/2020");
			Date d21 = new SimpleDateFormat("MM/dd/yyyy").parse("07/05/2020");
			
			AnnualLeave al1 = new AnnualLeave(d21, "oversea travel");
			al1.setStartDate(d20);
			al1.setStaff(s1);
			al1.setLeaveReason("Moving House");
			al1.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al2 = new AnnualLeave(d8, "asia missing");
			al2.setStartDate(d7);
			al2.setStaff(s1);
			al2.setLeaveReason("visit korea");
			al2.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al3 = new AnnualLeave(d2, "oversea travel");
			al3.setStartDate(d1);
			al3.setStaff(s5);
			al3.setLeaveReason("Moving House");
			al3.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al4 = new AnnualLeave(d2, "oversea travel");
			al4.setStartDate(d1);
			al4.setStaff(man2);
			al4.setLeaveReason("Moving House");
			al4.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al5 = new AnnualLeave(d6, "oversea travel");
			al5.setStartDate(d5);
			al5.setStaff(s5);
			al5.setLeaveReason("Moving House");
			al5.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al6 = new AnnualLeave(d10, "local");
			al6.setStartDate(d9);
			al6.setStaff(s6);
			al6.setLeaveReason("school holidays");
			al6.setManagerComment("Sorry,Project first.");
			al6.setStatus(LeaveStatus.Rejected);
			
			AnnualLeave al7 = new AnnualLeave(d4, "overseas travel");
			al7.setStartDate(d3);
			al7.setStaff(s7);
			al7.setLeaveReason("school holidays");
			al7.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al8 = new AnnualLeave(d2, "local,987654321");
			al8.setStartDate(d1);
			al8.setStaff(s10);
			al8.setLeaveReason("school holidays");
			al8.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al9 = new AnnualLeave(d4, "local,887654321");
			al9.setStartDate(d3);
			al9.setStaff(s12);
			al9.setLeaveReason("New Years");
			al9.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al10 = new AnnualLeave(d14, "local,997654321");
			al10.setStartDate(d13);
			al10.setStaff(s15);
			al10.setLeaveReason("New Years");
			al10.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al11 = new AnnualLeave(d12, " ");
			al11.setStartDate(d11);
			al11.setStaff(s6);
			al11.setLeaveReason("March holidays");
			al11.setStatus(LeaveStatus.Approved);
			
			AnnualLeave al12 = new AnnualLeave(d10, " ");
			al12.setStartDate(d9);
			al12.setStaff(s9);
			al12.setLeaveReason("March holidays");
			al12.setStatus(LeaveStatus.Approved);
			
			MedicalLeave ml2 = new MedicalLeave(d4);
			ml2.setStartDate(d3);
			ml2.setStaff(s3);
			ml2.setLeaveReason("Fever");
			ml2.setManagerComment("Do not find an excuse");
			ml2.setStatus(LeaveStatus.Rejected);
			
			MedicalLeave ml3 = new MedicalLeave(d6);
			ml3.setStartDate(d5);
			ml3.setStaff(s1);
			ml3.setLeaveReason("High Fever");
			ml3.setManagerComment("Can");
			ml3.setStatus(LeaveStatus.Approved);
			
			MedicalLeave ml1 = new MedicalLeave(d2);
			ml1.setStartDate(d1);
			ml1.setStaff(s3);
			ml1.setLeaveReason("Stomach ache");
			
			
			MedicalLeave ml4 = new MedicalLeave(d4);
			ml4.setStartDate(d3);
			ml4.setStaff(s6);
			ml4.setLeaveReason("Stomach ache too");
			ml4.setStatus(LeaveStatus.Approved);
			
			MedicalLeave ml5 = new MedicalLeave(d12);
			ml5.setStartDate(d11);
			ml5.setStaff(s8);
			ml5.setLeaveReason("Covid-19");
			ml5.setManagerComment("Rest well");
			ml5.setStatus(LeaveStatus.Approved);
			
			MedicalLeave ml6 = new MedicalLeave(d13);
			ml6.setStartDate(d14);
			ml6.setStaff(s9);
			ml6.setLeaveReason("Covid-19");
			ml6.setManagerComment("Rest well");
			ml6.setStatus(LeaveStatus.Approved);
			
			Overtime ot1 = new Overtime(d1,s1,4);
			ot1.setOverTimeStatus(OvertimeStatus.Approved);
			Overtime ot2 = new Overtime(d2,s1,4);
			ot2.setOverTimeStatus(OvertimeStatus.Rejected);
			Overtime ot3 = new Overtime(d1,s1,1);
			ot3.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot4 = new Overtime(d2,s1,3);
			ot4.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot5 = new Overtime(d1,s5,4);
			ot5.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot6 = new Overtime(d2,s5,4);
			ot6.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot7 = new Overtime(d1,s5,1);
			ot7.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot8 = new Overtime(d2,s5,3);
			ot8.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot9 = new Overtime(d13,s10,2);
			ot9.setOverTimeStatus(OvertimeStatus.Applied);
			Overtime ot10 = new Overtime(d14,s10,2);
			ot10.setOverTimeStatus(OvertimeStatus.Applied);
			
			
			
			
			CompensationLeave c =new CompensationLeave("Half Day PM");
			c.setStartDate(d2);
			c.setStaff(s1);
			c.setLeaveReason("Compensation 1");
			c.setStatus(LeaveStatus.Approved);
			
			CompensationLeave c1 =new CompensationLeave("Half Day AM");
			c1.setStartDate(d4);
			c1.setStaff(s11);
			c1.setLeaveReason("Compensation 1.1");
			c1.setStatus(LeaveStatus.Approved);
			
			CompensationLeave c2 =new CompensationLeave("Half Day PM");
			c2.setStartDate(d4);
			c2.setStaff(s11);
			c2.setLeaveReason("Compensation 1.2");
			c2.setStatus(LeaveStatus.Approved);
			
			CompensationLeave c3 =new CompensationLeave("Half Day AM");
			c3.setStartDate(d12);
			c3.setStaff(s13);
			c3.setLeaveReason("Compensation 2.1");
			c3.setStatus(LeaveStatus.Applied);
			
			CompensationLeave c4 =new CompensationLeave("Half Day PM");
			c4.setStartDate(d12);
			c4.setStaff(s13);
			c4.setLeaveReason("Compensation 2.2");
			c4.setStatus(LeaveStatus.Approved);
			
			man1.setCurrentAnnualLeaves(18);
			man1.setCurrentMedicalLeaves(20);
			man2.setCurrentAnnualLeaves(16);
			man2.setCurrentMedicalLeaves(15);
			man2.setCurrentAnnualLeaves(18);
			man2.setCurrentMedicalLeaves(15);
			s1.setCurrentAnnualLeaves(8);
			s1.setCurrentMedicalLeaves(7);
			s2.setCurrentAnnualLeaves(14);
			s2.setCurrentMedicalLeaves(10);
			s3.setCurrentAnnualLeaves(14);
			s3.setCurrentMedicalLeaves(10);
			s4.setCurrentAnnualLeaves(14);
			s4.setCurrentMedicalLeaves(60);
			s5.setCurrentAnnualLeaves(9);
			s5.setCurrentMedicalLeaves(18);
			s6.setCurrentAnnualLeaves(14);
			s6.setCurrentMedicalLeaves(60);
			s7.setCurrentAnnualLeaves(14);
			s7.setCurrentMedicalLeaves(60);
			s8.setCurrentAnnualLeaves(14);
			s8.setCurrentMedicalLeaves(57);
			s9.setCurrentAnnualLeaves(14);
			s9.setCurrentMedicalLeaves(60);
			s10.setCurrentAnnualLeaves(12);
			s10.setCurrentMedicalLeaves(60);
			s11.setCurrentAnnualLeaves(14);
			s11.setCurrentMedicalLeaves(60);
			s12.setCurrentAnnualLeaves(10);
			s12.setCurrentMedicalLeaves(50);
			s13.setCurrentAnnualLeaves(14);
			s13.setCurrentMedicalLeaves(60);
			s14.setCurrentAnnualLeaves(11);
			s14.setCurrentMedicalLeaves(40);
			s15.setCurrentAnnualLeaves(8);
			s15.setCurrentMedicalLeaves(16);
			
			
			
			empRepo.save(adm1);
			empRepo.save(adm2);
			empRepo.save(man1);
			empRepo.save(man2);
			empRepo.save(man3);
			
			empRepo.save(s1);
			empRepo.save(s2);
			empRepo.save(s3);
			empRepo.save(s4);
			empRepo.save(s5);
			empRepo.save(s6);
			empRepo.save(s7);
			empRepo.save(s8);
			empRepo.save(s9);
			empRepo.save(s10);
			empRepo.save(s11);
			empRepo.save(s12);
			empRepo.save(s13);
			empRepo.save(s14);
			empRepo.save(s15);
			
			lRepo.save(al1);
			lRepo.save(al2);
			lRepo.save(al3);
			lRepo.save(al4);
			lRepo.save(al5);
			lRepo.save(al6);
			lRepo.save(al7);
			lRepo.save(al8);
			lRepo.save(al9);
			lRepo.save(al10);
			
			lRepo.save(ml1);
			lRepo.save(ml2);
			lRepo.save(ml3);
			lRepo.save(ml4);
			lRepo.save(ml5);
			lRepo.save(ml6);
			
			lRepo.save(al2);
			lRepo.save(c);
			lRepo.save(c1);
			lRepo.save(c2);
			lRepo.save(c3);
			lRepo.save(c4);
			
			oRepo.save(ot1);
			oRepo.save(ot2);
			oRepo.save(ot3);
			oRepo.save(ot4);
			oRepo.save(ot5);
			oRepo.save(ot6);
			oRepo.save(ot7);
			oRepo.save(ot8);
			oRepo.save(ot9);
			oRepo.save(ot10);
		

			Date h1 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/01");
			Date h2 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/25");
			Date h3 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/26");
			Date h4 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/04/10");			
			Date h5 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/05/01");
			Date h6 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/05/07");			
			Date h7 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/05/24");
			Date h8 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/07/31");
			Date h9 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/08/09");
			Date h10 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/11/14");
			Date h11 = new SimpleDateFormat("yyyy/MM/dd").parse("2020/12/25");

			BlockedLeaves blLeaves1 = new BlockedLeaves("New Year", h1);
			BlockedLeaves blLeaves2 = new BlockedLeaves("Chinese New Year", h2);
			BlockedLeaves blLeaves3 = new BlockedLeaves("Chinese New Year", h3);
			BlockedLeaves blLeaves4 = new BlockedLeaves("Good Friday", h4);
			BlockedLeaves blLeaves5 = new BlockedLeaves("Labour's Day", h5);
			BlockedLeaves blLeaves6 = new BlockedLeaves("Vesak's Day", h6);
			BlockedLeaves blLeaves7 = new BlockedLeaves("Hari Raya Puasa", h7);
			BlockedLeaves blLeaves8 = new BlockedLeaves("Hari Raya Hagi", h8);
			BlockedLeaves blLeaves9 = new BlockedLeaves("National Day", h9);
			BlockedLeaves blLeaves10 = new BlockedLeaves("Deepavali", h10);
			BlockedLeaves blLeaves11 = new BlockedLeaves("Christmas Day", h11);
			
			blRepo.save(blLeaves1);
			blRepo.save(blLeaves2);
			blRepo.save(blLeaves3);
			blRepo.save(blLeaves4);
			blRepo.save(blLeaves5);
			blRepo.save(blLeaves6);
			blRepo.save(blLeaves7);
			blRepo.save(blLeaves8);
			blRepo.save(blLeaves9);
			blRepo.save(blLeaves10);
			blRepo.save(blLeaves11);
			
			
			System.out.println("CHEERS! At Least Run Liao. Check all DB tables ba.");
			System.out.println("I want to test the discriminator:");
			System.out.println(al1.getDiscriminatorValue());
			System.out.println(adm1.getDiscriminatorValue());
			
//			Admin adm1 = new Admin("Martin","martin.dreamz@hotmail.com");
//			Manager man1 = new Manager("Joe","martin.dreamz@hotmail.com",null,16,20);
//			Staff s2 = new Staff("Bianca2","e0533382@u.nus.edu",man1,(long)12,(long)10);
//			
//			Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse("05/01/2020");
//			Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse("05/03/2020");
//
//			Date d3 = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2019");
//			Date d4 = new SimpleDateFormat("MM/dd/yyyy").parse("01/05/2019");
//			
//			Date d5 = new SimpleDateFormat("MM/dd/yyyy").parse("09/09/2020");
//			Date d6 = new SimpleDateFormat("MM/dd/yyyy").parse("09/12/2020");
//			
//			Date d7 = new SimpleDateFormat("MM/dd/yyyy").parse("08/08/2020");
//			Date d8 = new SimpleDateFormat("MM/dd/yyyy").parse("08/12/2020");
//			
//			Date d9 = new SimpleDateFormat("MM/dd/yyyy").parse("03/03/2020");
//			Date d10 = new SimpleDateFormat("MM/dd/yyyy").parse("03/12/2020");
//			
//			Date d11 = new SimpleDateFormat("MM/dd/yyyy").parse("03/05/2020");
//			Date d12 = new SimpleDateFormat("MM/dd/yyyy").parse("03/08/2020");
//			
//			Date d13 = new SimpleDateFormat("MM/dd/yyyy").parse("01/05/2019");
//			Date d14 = new SimpleDateFormat("MM/dd/yyyy").parse("01/08/2019");
//			
//			AnnualLeave al1 = new AnnualLeave(d2, "oversea travel");
//			al1.setStartDate(d1);
//			al1.setLeaveReason("Moving House");
//			al1.setStatus(LeaveStatus.Approved);
//			
//			AnnualLeave al2 = new AnnualLeave(d8, "asia missing");
//			al2.setStartDate(d7);
//			al2.setLeaveReason("visit korea");
//			al2.setStatus(LeaveStatus.Approved);
//			
//			AnnualLeave al3 = new AnnualLeave(d2, "oversea travel");
//			al3.setStartDate(d1);
//			al3.setLeaveReason("Moving House");
//			al3.setStatus(LeaveStatus.Approved);
//
//			List<Leaves> list = new ArrayList<>();
//			list.add(al1);
//			list.add(al2);
//			list.add(al3);
//			
//			s2.setLeaves(list);
//			
//			empRepo.save(adm1);
//			empRepo.save(s2);
//			empRepo.save(man1);
		}; 
	}

}
