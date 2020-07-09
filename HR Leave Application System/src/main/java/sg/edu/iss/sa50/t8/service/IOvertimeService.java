package sg.edu.iss.sa50.t8.service;

import java.util.ArrayList;

import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.OvertimeStatus;

public interface IOvertimeService {
	public ArrayList<Overtime> findAllOvertime();
	public boolean saveOvertime(Overtime overtime);	
	public int findTotalOvertimeByStaffId(int staff);
	public ArrayList<Overtime> findAllOvertimeByStaffId(int staff);
	public int findEarliestApprovedOvertimeId(int staff);
	public void updateOvertimeStatus(int overtimeId,OvertimeStatus status);
}
