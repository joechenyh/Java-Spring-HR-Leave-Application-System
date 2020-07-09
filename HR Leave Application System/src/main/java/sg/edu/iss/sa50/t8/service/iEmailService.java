package sg.edu.iss.sa50.t8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.CompensationLeave;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Overtime;

@Service
public class iEmailService implements EmailService{

	private JavaMailSender jvm;

	@Autowired
	public iEmailService(JavaMailSender jvm) {
		this.jvm = jvm;
	}

	@Override
	public void notifyManager(AnnualLeave leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getManager().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application from " + leave.getStaff().getName());
		msg.setText("Dear " + leave.getStaff().getManager().getName()+ ",\n\n"+leave.getStaff().getName()+ " applied annual leave from "+leave.getStartDate().toString()+" to "+
				leave.getEndDate().toString()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);
	}

	@Override
	public void notifyManager(MedicalLeave leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getManager().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application from " + leave.getStaff().getName());
		msg.setText("Dear " + leave.getStaff().getManager().getName()+ ",\n\n"+leave.getStaff().getName()+ " applied medical leave from "+leave.getStartDate().toString()+" to "+
				leave.getEndDate().toString()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);

	}
	@Override
	public void notifyStaff(AnnualLeave leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application on " + leave.getStartDate() + " " + leave.getStatus());
		msg.setText("Dear " + leave.getStaff().getName()+ ",\n\nYour application for annual leave from "+leave.getStartDate().toString()+" to "+
				leave.getEndDate().toString() +" has been " + leave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);

	}

	@Override
	public void notifyStaff(MedicalLeave leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application on " + leave.getStartDate() + " " + leave.getStatus());
		msg.setText("Dear " + leave.getStaff().getName()+ ",\n\nYour application for medical leave from "+leave.getStartDate().toString()+" to "+
				leave.getEndDate().toString() +" has been " + leave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);
	}

	@Override
	public void confirmStaffCancellation(Leaves leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave cancellation on " + leave.getStartDate());

		if(leave.getDiscriminatorValue().equals("Compensation Leave")) {
			CompensationLeave nleave = (CompensationLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getName()+ ",\n\nYour "+nleave.getDiscriminatorValue()+" application on "+nleave.getStartDate()
			+" "+ nleave.getClaimQuota()+" has been " + nleave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		}else{
			msg.setText("Dear " + leave.getStaff().getName()+ ",\n\nYour "+leave.getDiscriminatorValue()+" application on "+leave.getStartDate()
			+" has been " + leave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		}
		jvm.send(msg);
	}

	@Override
	public void confirmStaffCancellationToManager(Leaves leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getManager().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave cancellation on " + leave.getStartDate()+" from "+ leave.getStaff().getName());
System.out.println(leave.getDiscriminatorValue());
		if(leave.getDiscriminatorValue().equals("Compensation Leave")) {
			CompensationLeave nleave = (CompensationLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getManager().getName()+ 
					",\n\nYour staff "+nleave.getStaff().getName()+" "+nleave.getStatus()+
					" "+ nleave.getDiscriminatorValue()+" application on "+nleave.getStartDate()
					+" "+ nleave.getClaimQuota()+".\n\nFrom,\nTeam8LAPS");
		}else{
			msg.setText("Dear " + leave.getStaff().getManager().getName()+ 
					",\n\nYour staff "+leave.getStaff().getName()+" "+leave.getStatus()+
					" "+ leave.getDiscriminatorValue()+" application on "+leave.getStartDate()
					+".\n\nFrom,\nTeam8LAPS");
		}
		jvm.send(msg);
	}

	@Override
	public void notifyManagerForOT(Overtime ot) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(ot.getStaff().getManager().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Overtime Application on " + ot.getOvertimeDate());
		msg.setText("Dear " + ot.getStaff().getManager().getName()+ ",\nYour Staff "+ot.getStaff().getName() +" applied "+ot.getOvertimeHours()+" hours of overtime on "+
				ot.getOvertimeDate()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);
	}

	@Override
	public void notifyStaffForOT(Overtime ot) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(ot.getStaff().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Overtime Application on " + ot.getOvertimeDate() + " " + ot.getOverTimeStatus());
		msg.setText("Dear " + ot.getStaff().getName()+ ",\n\nYour application of "+ot.getOvertimeHours()+" hours of overtime on "+
				ot.getOvertimeDate()+" has been "+ ot.getOverTimeStatus()+".\n\nFrom,\nTeam8LAPS");
		jvm.send(msg);
	}


	@Override
	public void notifyStaff(Leaves leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application on " + leave.getStartDate() + " " + leave.getStatus());
		if(leave.getDiscriminatorValue().equals("Compensation Leave")) {
			CompensationLeave nleave = (CompensationLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getName()+ ",\n\nYour "+nleave.getDiscriminatorValue()+" application on "+nleave.getStartDate()
			+" "+ nleave.getClaimQuota()+" has been " + nleave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		}else{
			msg.setText("Dear " + leave.getStaff().getName()+ ",\n\nYour "+leave.getDiscriminatorValue()+" application on "+leave.getStartDate()
			+" has been " + leave.getStatus()+".\n\nFrom,\nTeam8LAPS");
		}
		jvm.send(msg);
	}

	@Override
	public void notifyManager(Leaves leave) throws MailException{
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(leave.getStaff().getManager().getEmail());
		msg.setFrom("gdipsa50t8@gmail.com");
		msg.setSubject("Leave application from " + leave.getStaff().getName());

		if(leave.getDiscriminatorValue().equals("Medical Leave")) {
			MedicalLeave nleave = (MedicalLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getManager().getName()+ ",\n\n"+nleave.getStaff().getName()+ " applied medical leave from "+nleave.getStartDate().toString()+" to "+
					nleave.getEndDate().toString()+".\n\nFrom,\nTeam8LAPS");}

		if(leave.getDiscriminatorValue().equals("Compensation Leave")) {
			CompensationLeave nleave = (CompensationLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getManager().getName()+ ",\n\n"+nleave.getStaff().getName()+ " applied compensation leave on "+nleave.getStartDate().toString()+ " "+nleave.getClaimQuota()
			+".\n\nFrom,\nTeam8LAPS");}

		if(leave.getDiscriminatorValue().equals("Annual Leave")) {
			AnnualLeave nleave = (AnnualLeave) leave;
			msg.setText("Dear " + nleave.getStaff().getManager().getName()+ ",\n\n"+nleave.getStaff().getName()+ " applied annual leave from "+nleave.getStartDate().toString()+" to "+
					nleave.getEndDate().toString()+".\n\nFrom,\nTeam8LAPS");}

		jvm.send(msg);

	}
}
