package sg.edu.iss.sa50.t8.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Manager")
public class Manager extends Staff{
	
	@OneToMany(mappedBy="manager",cascade={CascadeType.MERGE}) 
	private List<Staff> staffs;
	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public Manager() {
		super();
	}
	
	public Manager(String name, String email,
			Manager manager, 
			long annualLeaveDays, long medicalLeaveDays) {
		super(name,email,manager,annualLeaveDays,medicalLeaveDays);
	}

}
