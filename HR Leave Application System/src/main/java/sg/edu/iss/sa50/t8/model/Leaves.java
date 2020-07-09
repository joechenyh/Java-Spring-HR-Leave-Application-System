package sg.edu.iss.sa50.t8.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Inheritance
@DiscriminatorColumn(name="Leave_Type")
public class Leaves {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="yyyy-MM-dd")
	@NotNull
	private Date startDate;

	private String leaveReason;
	private LeaveStatus status;

	private String managerComment;
	
	@ManyToOne(cascade={CascadeType.MERGE}) @JoinColumn(name="staff_id")
	private Staff staff;

	public Leaves() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Leaves(Date startDate,int comLeaveHours, String leaveReason,
			LeaveStatus status,String managerComment) {
		super();
		this.startDate = startDate;
		this.leaveReason = leaveReason;
		this.status = status;
		this.managerComment = managerComment;
	}

	public Leaves(Date startDate,int comLeaveHours, String leaveReason,
			LeaveStatus status, String managerComment, Staff staff) {
		super();
		this.startDate = startDate;
		this.leaveReason = leaveReason;
		this.status = status;
		this.managerComment = managerComment;
		this.staff = staff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	
	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String mangerComment) {
		this.managerComment = mangerComment;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
	@Transient
	public String getDiscriminatorValue(){
		return this.getClass().getAnnotation(DiscriminatorValue.class).value();
	}
}
