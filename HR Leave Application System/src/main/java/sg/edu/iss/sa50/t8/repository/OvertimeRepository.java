package sg.edu.iss.sa50.t8.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.sa50.t8.model.Overtime;
import sg.edu.iss.sa50.t8.model.OvertimeStatus;

public interface OvertimeRepository extends JpaRepository<Overtime, Integer>{
	@Query(value="SELECT sum(overtimeHours) FROM overtime WHERE staff_id=?1 and overTimeStatus='Approved'",nativeQuery = true)
	public int findTotalOvertimeByStaffId(int staff);
	
	@Query(value="SELECT * FROM overtime where staff_id=?1",nativeQuery = true)
	public ArrayList<Overtime> findAllOvertimeByStaffId(int staff);

	@Query(value="SELECT id FROM overtime WHERE staff_id=?1 and overTimeStatus='Approved' ORDER BY id ASC LIMIT 1",nativeQuery = true)
	public int findEarliestApprovedOvertimeId(int staff);
	
	@Query(value="UPDATE overtime SET overTimeStatus=?2 WHERE id=?1",nativeQuery = true)
	public void updateOvertimeStatus(int overtimeId,OvertimeStatus status);
	
}
