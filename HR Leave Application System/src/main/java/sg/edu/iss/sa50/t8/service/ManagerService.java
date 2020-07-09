package sg.edu.iss.sa50.t8.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.iss.sa50.t8.model.*;
import sg.edu.iss.sa50.t8.repository.*;

@Service
public class ManagerService implements IEmployeeService {
	@Autowired
	LeaveRepository lrepo;
	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	OvertimeRepository oRepo;

	public Optional<Leaves> findById(int id) {
		return lrepo.findById(id);
	}

	public Staff findStaffById(int id) {
		return empRepo.findStaffById(id);
	}

	public void approveLeave(Leaves leaves) {
		leaves.setStatus(LeaveStatus.Approved);
		lrepo.save(leaves);
	}

	public void rejectLeave(Leaves leaves) {
		leaves.setStatus(LeaveStatus.Rejected);
		lrepo.save(leaves);
	}

	public void setComment(Leaves leaves, String comment) {
		leaves.setManagerComment(comment);
		lrepo.save(leaves);
	}

	public List<Leaves> findAllPendingLeaves(Manager man) {
		ArrayList<Staff> stfList = empRepo.findSubordinates(man);
		List<Leaves> l = new ArrayList<Leaves>();
		for (Staff stf : stfList) {
			l.addAll(lrepo.findPendingleavesByStaff(stf));
		}
		return l;
	}

	public List<Leaves> findAllLeaveByStaff(Staff stf) {
		return lrepo.findAllLeavesByStaff(stf);
	}

	public ArrayList<Staff> findSub(Manager man) {
		return empRepo.findSubordinates(man);
	}
	
	public List<Leaves> findAllSubLeavesByPeriod2(Leaves l, Manager man){
		ArrayList<Staff> stfList = empRepo.findSubordinates(man);
		List<Leaves> leaveslist = new ArrayList<Leaves>();
		List<Leaves> result = new ArrayList<Leaves>();
		for (Staff stf : stfList) {
			leaveslist.addAll(lrepo.findAllLeavesByStaff(stf));
		}
		for (Leaves ll : leaveslist) {
			Date lStart =  l.getStartDate();
			Date llStart =  ll.getStartDate();
			Date lEnd =  l.getStartDate();
			Date llEnd =  ll.getStartDate();
			if (l.getDiscriminatorValue().equals("Medical Leave")){
				MedicalLeave lcast  = (MedicalLeave) l;
				lEnd =  lcast.getEndDate();
			}
			else if (l.getDiscriminatorValue().equals("AnnualLeave")){
				MedicalLeave lcast  = (MedicalLeave) l;
				lEnd =  lcast.getEndDate();
			}
			if (ll.getDiscriminatorValue().equals("Medical Leave")){
				MedicalLeave llcast  = (MedicalLeave) ll;
				llEnd =  llcast.getEndDate();
			}
			else if (ll.getDiscriminatorValue().equals("Annual Leave")){
				AnnualLeave lcast  = (AnnualLeave) ll;
				llEnd =  lcast.getEndDate();
			}
			if (llStart.compareTo(lEnd)<=0 && llEnd.compareTo(lStart)>=0) {
				if(ll.getId()!= l.getId()) {result.add(ll);}
				}
		}
		
		return result;
	}
	public List<Overtime> findStaffOvertime(Manager man) {

		List<Overtime> overtimelist = new ArrayList<Overtime>();

		for (Staff staff : empRepo.findSubordinates(man)) {
			for (Overtime o : oRepo.findAllOvertimeByStaffId(staff.getId())) {
				overtimelist.add(o);
			}
		}
		return overtimelist;
	}

	public Overtime findOvertime(int id) {
		return oRepo.findById(id).get();
	}

	public void SetOTStatus(Overtime ot) {
		oRepo.save(ot);
	}

	public void AddOvertimeHours(Overtime ot) {
		Staff staff = empRepo.findStaffById(ot.getStaff().getId());
		staff.setTotalOTHours(staff.getTotalOTHours() + ot.getOvertimeHours());
		empRepo.save(staff);
	}
	
	public void updateTotalOTHoursByEmpId(int empId, int hr) {
		Employee emp = empRepo.findEmployeeById(empId);
		emp.setTotalOTHours(hr);
		empRepo.save(emp);
	}

	public Employee findEmployeeById(int EmpId) {
		return empRepo.findEmployeeById(EmpId);
	}
	public int findTotalOTHoursByEmpId(int empId) {
		return (Integer) empRepo.findTotalOTHoursByEmpId(empId);
	}

}
