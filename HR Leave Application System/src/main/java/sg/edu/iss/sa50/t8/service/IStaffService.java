	package sg.edu.iss.sa50.t8.service;

import org.springframework.stereotype.Service;

@Service
public interface IStaffService {
	public void updateTotalOTHoursByEmpId(int empId,int hr);
	public int findTotalOTHoursByEmpId(int empId);
}
