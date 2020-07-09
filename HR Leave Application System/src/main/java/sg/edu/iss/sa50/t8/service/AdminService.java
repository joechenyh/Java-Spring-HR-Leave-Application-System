package sg.edu.iss.sa50.t8.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.iss.sa50.t8.model.Admin;
import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Manager;
import sg.edu.iss.sa50.t8.model.Staff;
import sg.edu.iss.sa50.t8.repository.AdminRepository;
import sg.edu.iss.sa50.t8.repository.EmployeeRepository;
import sg.edu.iss.sa50.t8.repository.StaffRepository;

@Service
public class AdminService implements IEmployeeService {

	@Autowired
	AdminRepository arepo;

	@Autowired
	StaffRepository srepo;

	@Autowired
	EmployeeRepository eRepo;

	@Transactional
	public void deleteAdminById(int adminId) {
		arepo.deleteAdminById(adminId);
	}

	@Transactional
	public void deleteStaffById(int adminId) {
		srepo.deleteStaffById(adminId);
	}

	public List<Employee> findAllManager() {
		return eRepo.findAllManager();
	}

	public boolean saveAdmin(Admin admin) {
		return arepo.save(admin) != null ? true : false;
	}

	public List<Employee> searchEmployee(String searchTerm) {
		// return arepo.searchEmployee(searchTerm);
		return arepo.findByNameContaining(searchTerm);
	}

	public List<Employee> findAll() {
		return arepo.findAll();
	}

	public Employee findById(int id) {
		return arepo.findById(id);
	}

	public boolean save(Employee entry) {
		return arepo.save(entry) != null ? true : false;
	}

	public List<Staff> findAllNonAdminStaff() {
		return arepo.findAllNonAdminStaff();
	}

	public List<Admin> findallAdmin() {
		return arepo.findAllAdmin();
	}

	public Admin findAdminById(int id) {
		// TODO Auto-generated method stub
		return arepo.findAdminById(id);
	}

	public Staff findStaffById(int id) {
		// TODO Auto-generated method stub
		return arepo.findStaffById(id);
	}

	public Employee findEmployeeById(int EmpId) {
		return eRepo.findEmployeeById(EmpId);
	}

	public Manager findManagerById(int id) {
		// TODO Auto-generated method stub
		return arepo.findManagerById(id);
	}

	public void SeedNewStaff(Staff staff) {
		Staff employee = new Staff();
		employee.setId(staff.getId());
		employee.setName(staff.getName());
		employee.setPassword(staff.getPassword());
		employee.setEmail(staff.getEmail());
		employee.setManager(arepo.findManagerById(staff.getManId()));
		employee.setTotalAnnualLeaves(staff.getTotalAnnualLeaves());
		employee.setTotalMedicalLeaves(staff.getTotalMedicalLeaves());
		employee.setCurrentAnnualLeaves(staff.getCurrentAnnualLeaves());
		employee.setCurrentMedicalLeaves(staff.getCurrentMedicalLeaves());
		eRepo.save(employee);
	}

	public void SeedNewManager(Staff staff) {
		Manager employee = new Manager();
		employee.setId(staff.getId());
		employee.setName(staff.getName());
		employee.setPassword(staff.getPassword());
		employee.setEmail(staff.getEmail());
		employee.setCurrentAnnualLeaves(staff.getTotalAnnualLeaves());
		employee.setCurrentMedicalLeaves(staff.getTotalMedicalLeaves());
		employee.setCurrentAnnualLeaves(staff.getCurrentAnnualLeaves());
		employee.setCurrentMedicalLeaves(staff.getCurrentMedicalLeaves());
		eRepo.save(employee);
	}

	public void SeedNewAdmin(Staff staff) {
		Admin employee = new Admin();
		employee.setId(staff.getId());
		employee.setName(staff.getName());
		employee.setPassword(staff.getPassword());
		employee.setEmail(staff.getEmail());
		eRepo.save(employee);
	}

	public void deleteEmpbyID(int id) {
		eRepo.deleteEmployeeById(id);
	}

	@Transactional
	public void deleteById(int id) {
		Employee curr = (Employee) eRepo.findEmployeeById(id);
		if (curr.getDiscriminatorValue().equals("Manager")) {
			Manager currCast = (Manager) curr;
			Manager bigBoss = currCast.getManager();
			if (bigBoss != null) {
				bigBoss.getStaffs().remove(currCast);
				eRepo.save(bigBoss);
			}
			ArrayList<Staff> subList = eRepo.findSubordinates(currCast);
			if (subList != null) {
				for (Staff stf : subList) {
					stf.setManager(null);
					eRepo.save(stf);
				}
			}
			currCast.setStaffs(null);
			currCast.setManager(null);
			eRepo.save(currCast);
		}
		eRepo.deleteById(id);
	}

}
