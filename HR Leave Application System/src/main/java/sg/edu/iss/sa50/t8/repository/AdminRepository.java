package sg.edu.iss.sa50.t8.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.sa50.t8.model.Admin;
import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Manager;
import sg.edu.iss.sa50.t8.model.Staff;

public interface AdminRepository extends EmployeeRepository {
	/*@Query("SELECT e FROM Employee e"
			+ "WHERE e.name LIKE %?1"
			+ "OR e.name LIKE ?1%")
	List<Employee> searchEmployee(String searchTerm);*/
	
	List<Employee> findByNameContaining(String searchTerm);
	
	Employee findById(int id); 
	
	@Query("SELECT s FROM Admin s")
	List<Admin> findAllAdmin();
	
	@Query("SELECT e FROM Employee e WHERE e.class != 'Admin'")
	List<Staff> findAllNonAdminStaff();
	
	@Query("SELECT e FROM Employee e WHERE e.class = 'Admin' AND e.id = :id")
	Admin findAdminById(@Param("id") int id);
	
	@Modifying
	@Query("delete from Admin where id=?1")
	void deleteAdminById(int adminId);
	
	@Query("SELECT e FROM Employee e WHERE e.class = 'Manager' AND e.id = :id")
	Manager findManagerById(@Param("id") int id);
} 
