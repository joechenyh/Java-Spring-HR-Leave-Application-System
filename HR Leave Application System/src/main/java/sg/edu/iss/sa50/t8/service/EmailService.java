package sg.edu.iss.sa50.t8.service;

import org.springframework.mail.MailException;

import sg.edu.iss.sa50.t8.model.AnnualLeave;
import sg.edu.iss.sa50.t8.model.Leaves;
import sg.edu.iss.sa50.t8.model.MedicalLeave;
import sg.edu.iss.sa50.t8.model.Overtime;

public interface EmailService {

	public void notifyManager(MedicalLeave leave) throws MailException;

	public void notifyManager(AnnualLeave leave) throws MailException;

	public void notifyStaff(AnnualLeave leave) throws MailException;

	public void notifyStaff(MedicalLeave leave) throws MailException;

	public void notifyManagerForOT(Overtime ot) throws MailException;

	public void notifyStaffForOT(Overtime ot) throws MailException;

	public void notifyStaff(Leaves leave) throws MailException;

	public void notifyManager(Leaves leave) throws MailException;

	public void confirmStaffCancellation(Leaves leave) throws MailException;

	public void confirmStaffCancellationToManager(Leaves leave) throws MailException;

}
