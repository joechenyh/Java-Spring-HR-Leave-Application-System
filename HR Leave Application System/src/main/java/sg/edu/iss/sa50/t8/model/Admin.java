package sg.edu.iss.sa50.t8.model;
import javax.persistence.*;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends Employee {

	public Admin() {
		super();
	}
	
	public Admin(String name, String email) { 
		super(name,email);
		super.setPassword("admin");
        
    }

}
