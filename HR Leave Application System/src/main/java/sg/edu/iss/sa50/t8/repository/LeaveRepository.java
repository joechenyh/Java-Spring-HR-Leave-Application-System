package sg.edu.iss.sa50.t8.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.LeaveStatus;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Staff;
@Primary
public interface LeaveRepository extends JpaRepository<Leaves, Integer>{

	@Query(value="SELECT * FROM Leaves WHERE staff_id=?1 and YEAR(start_date) = YEAR(CURDate())",nativeQuery = true)
	List<Leaves> findAll(int staff_id);
	
	@Query(value="SELECT * FROM Leaves WHERE id=:leaveId",nativeQuery = true)
	Leaves findLeaveById(@Param("leaveId") int leaveId);
	
	@Query(value="SELECT Leave_Type FROM Leaves WHERE id=:leaveId",nativeQuery = true)
	String findLeaveTypeById(@Param("leaveId") int leaveId);
	
	@Query(value="SELECT l FROM Leaves l WHERE l.staff = :staff "
			+ "and l.status in (sg.edu.iss.sa50.t8.model.LeaveStatus.Applied,"
			 +"sg.edu.iss.sa50.t8.model.LeaveStatus.Updated)") 
	List<Leaves> findPendingleavesByStaff(@Param("staff") Staff Staff);
	
	@Query(value="SELECT l FROM Leaves l WHERE l.staff = :staff") 
	List<Leaves> findAllLeavesByStaff(@Param("staff") Staff Staff);
	
	@Modifying
	@Query(value="UPDATE Leaves SET status=?2 WHERE id=?1",nativeQuery = true)
	public void updateLeaveStatus(int leaveId,LeaveStatus status);
	
	@Query(value="SELECT l FROM AnnualLeave l WHERE id=:leaveId")
	public AnnualLeave findAnnualLeaveById(@Param("leaveId") int leaveId);
	
	@Query(value="SELECT l FROM MedicalLeave l WHERE id=:leaveId")
	public MedicalLeave findMedicalLeaveById(@Param("leaveId") int leaveId);

	//SELECT * FROM `leaves` WHERE leave_type = 'Annual Leave' AND staff_id = 6 AND  STATUS IN( 0, 4)
	/*
	 * @Query(value="SELECT l FROM AnnualLeave l WHERE l.staff=:staff" +
	 * "and l.status in (sg.edu.iss.sa50.t8.model.LeaveStatus.Applied,"
	 * +"sg.edu.iss.sa50.t8.model.LeaveStatus.Approved)") public List<AnnualLeave>
	 * findAllAnnualLeavesByStaffId (@Param("staff")Staff staff);
	 */
	
	@Query(value="SELECT l FROM AnnualLeave l WHERE l.staff=:staff")
	public List<AnnualLeave> findAllAnnualLeavesByStaffId (@Param("staff")Staff staff);
	
	@Query(value="SELECT l FROM MedicalLeave l WHERE l.staff=:staff")
	public List<MedicalLeave> findAllMedicalLeavesByStaffId (@Param("staff")Staff staff);

	@Query(value="SELECT l FROM CompensationLeave l WHERE l.staff=:staff")
	public List<CompensationLeave> findAllCompensationLeavesByStaffId (@Param("staff")Staff staff);
}
