package sg.edu.iss.sa50.t8.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.LeaveStatus;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Staff;

@Service
public interface ILeaveService {
	public boolean saveAnnualLeave(AnnualLeave aLeave);	
	public boolean saveMedicalLeave(MedicalLeave mLeave);
	public boolean saveCompensationLeave(CompensationLeave cLeave);
	public void updateLeaveStatus(int leaveId,LeaveStatus status);
	public Leaves findLeaveById(int leaveId);
	public String findLeaveTypeById(int leaveId);
	public ArrayList<Leaves> findAllLeaves(int id);
	public long findCurAnnLeave(int id);
	public long findMedAnnLeave(int id);
	public AnnualLeave findAnnualLeaveById(int leaveId);
	public MedicalLeave findMedicalLeaveById(int leaveId);
	public void updateCurAnnLeaveDate(int staffId,long days);
	public void updateCurMedLeaveDate(int staffId,long days);	
	public List<AnnualLeave> findAllAnnualLeavesByStaffId(Staff staff);
	public List<MedicalLeave> findAllMedicalLeavesByStaffId(Staff staff);
	public List<CompensationLeave> findAllCompensationLeavesByStaffId(Staff staff);
	public List<Leaves> findAllLeavesByStaff(Staff staff);
}

