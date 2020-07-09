
package sg.edu.iss.sa50.t8.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.sa50.t8.model.*;

@Primary
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	@Query(value="UPDATE Employee SET totalOTHours=?2 WHERE id=?1",nativeQuery = true)
	public void updateTotalOTHoursByEmpId(int empId,int hr);
	
	@Query(value="SELECT * FROM Employee where id =?1",nativeQuery = true)
	public Employee findEmployeeById(int EmpId);
	
	@Query(value="SELECT totalothours FROM Employee WHERE id=?1",nativeQuery = true)
	public int findTotalOTHoursByEmpId(int empId);
	
	@Query("SELECT s FROM Staff s where s.manager = :manager") 
	ArrayList<Staff> findSubordinates(@Param("manager") Manager manager);
	
	@Query("SELECT s FROM Staff s where s.id = :stfId") 
	Staff findStaffById(@Param("stfId") int id);
	
	@Query(value="SELECT m FROM Manager m")
	List<Employee> findAllManager();
	
	@Modifying
	@Query("delete  from Employee where id=?1")
	void deleteEmployeeById(int Id);
}
