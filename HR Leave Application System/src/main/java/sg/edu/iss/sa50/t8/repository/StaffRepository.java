package sg.edu.iss.sa50.t8.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.sa50.t8.model.Employee;
import sg.edu.iss.sa50.t8.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

	@Query("SELECT e FROM Employee e WHERE e.class != 'Admin'")
	List<Staff> findAllNonAdmin();
	
	Employee findEmployeeById(int id);
	 List<Employee> findByNameContaining(String searchTerm);
	
	 @Query("SELECT s FROM Staff s WHERE s.class = 'Staff' AND s.id = :id")
	 Staff findByStaffId(@Param("id") int id);
	 
	 Staff findStaffById(int id);
	 
	 @Query("SELECT e FROM Employee e")
	 List<Employee> findall();

	 @Modifying
	 @Query("delete from Staff  where id=?1")
	 void deleteStaffById(int staffId);

	 @Query("select e from Employee e where e.class != 'Admin' and e.manager.id = ?1")
	 List<Staff> findAllStaffbyManager(int id);


}

