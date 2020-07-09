package sg.edu.iss.sa50.t8.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Overtime {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	//@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="yyyy-MM-dd")
	private Date overtimeDate; 
	
	@ManyToOne(cascade={CascadeType.REMOVE}) 
	@JoinColumn(name="staff_id")
	private Staff staff; 
	
	@Min(1)
	@Max(24)
	private int overtimeHours;
	
	private OvertimeStatus overTimeStatus;
	
	public Overtime() {
		super();
		setOverTimeStatus(OvertimeStatus.Applied);
	}
	public Overtime(@Past Date overtimeDate, Staff staff, int overtimeHours) {
		super();
		this.overtimeDate = overtimeDate;
		this.staff = staff;
		this.overtimeHours = overtimeHours;
		setOverTimeStatus(OvertimeStatus.Applied);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getOvertimeDate() {
		return overtimeDate;
	}
	public void setOvertimeDate(Date overtimeDate) {
		this.overtimeDate = overtimeDate;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public int getOvertimeHours() {
		return overtimeHours;
	}
	public void setOvertimeHours(int overtimeHours) {
		this.overtimeHours = overtimeHours;
	}
	public OvertimeStatus getOverTimeStatus() {
		return overTimeStatus;
	}
	public void setOverTimeStatus(OvertimeStatus overTimeStatus) {
		this.overTimeStatus = overTimeStatus;
	}
	@Override
	public String toString() {
		return "Overtime [id=" + id + ", overtimeDate=" + overtimeDate + ", staff=" + staff + ", overtimeHours="
				+ overtimeHours + ", overTimeStatus=" + overTimeStatus + "]";
	}
	
}
