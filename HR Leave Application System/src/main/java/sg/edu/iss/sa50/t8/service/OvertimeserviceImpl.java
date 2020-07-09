package sg.edu.iss.sa50.t8.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.OvertimeStatus;
import sg.edu.iss.sa50.t8.repository.OvertimeRepository;

@Service
public class OvertimeserviceImpl implements IOvertimeService{
	@Autowired
	OvertimeRepository oRepo;
	
	@Override
	public boolean saveOvertime(Overtime overtime) {
		if (oRepo.save(overtime) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int findTotalOvertimeByStaffId(int staff) {
		return oRepo.findTotalOvertimeByStaffId(staff);
	}

	@Override
	public ArrayList<Overtime> findAllOvertimeByStaffId(int staff) {
		return oRepo.findAllOvertimeByStaffId(staff);
	}

	@Override
	public int findEarliestApprovedOvertimeId(int staff) {
		// TODO Auto-generated method stub
		return oRepo.findEarliestApprovedOvertimeId(staff);
	}

	@Override
	public void updateOvertimeStatus(int overtimeId, OvertimeStatus status) {
		oRepo.updateOvertimeStatus(overtimeId, status);
	}

	@Override
	public ArrayList<Overtime> findAllOvertime() {
		return (ArrayList<Overtime>)oRepo.findAll();
	}

}
