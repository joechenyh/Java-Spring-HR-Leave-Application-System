package sg.edu.iss.sa50.t8.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@DiscriminatorValue("Annual Leave")
public class AnnualLeave extends Leaves{
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="yyyy-MM-dd")
	@NotNull
	private Date endDate;
	
	@NotEmpty
	private String contactDetails;

	public AnnualLeave() {
		super();
		super.setStatus(LeaveStatus.Applied);
	}

	public AnnualLeave(Date endDate, String contactDetails) {
		super();
		super.setStatus(LeaveStatus.Applied);
		this.endDate = endDate;
		this.contactDetails = contactDetails;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}
}