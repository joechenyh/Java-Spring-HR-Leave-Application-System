package sg.edu.iss.sa50.t8.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DiscriminatorValue("Medical Leave")
public class MedicalLeave extends Leaves {

	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="yyyy-MM-dd")
	@NotNull
	private Date endDate;

	public MedicalLeave() {
		super();
		super.setStatus(LeaveStatus.Applied);
	}

	public MedicalLeave(Date endDate) {
		super();
		super.setStatus(LeaveStatus.Applied);
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
}
