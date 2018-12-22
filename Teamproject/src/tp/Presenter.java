package tp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.collections.ObservableList;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.EMail;
import tp.model.Model;
import tp.model.Statistic;
import tp.model.Student;

public class Presenter
{
	private Model model;
	private MainView mainView;

	public Presenter(Model model, MainView mainView) {
		this.mainView = mainView;
		this.model = model;
		
	}

	
	//======================Updater======================
	public void updateWeekView()
	{
		//TODO
	}
	
	public void updateRightToolbar()
	{
		mainView.updateRightToolBar();
	}
	
	public void updateTabViews()
	{
		//TODO
	}
	
	
	//=====================Mail==========================
	
	
	public void sendMail(String userID, String name, Student recipient, String subject, String content) {
		try {
			// Create a properties file containing
			// the host address of your SMTP server
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", "mail.fh-trier.de");

			// Create a session with the Java Mail API
			Session mailSession = Session.getDefaultInstance(mailProps);
			// Create a new mail message
			MimeMessage message = new MimeMessage(mailSession);
			// Set the From and the Recipient
			message.setFrom(new InternetAddress(userID +"@fh-trier.de", name));
			//TODO get richtige?? E-Mail adresse (wie zugeordnet?)
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(recipient.geteMailAddresses()[0], recipient.getName()));
			// Set the subject
			message.setSubject(subject);
			// Set the message text
			message.setText(content);
			// Save all the changes you have made
			// to the message
			message.saveChanges();
			// Create a transport object for sending mail
			Transport transport = mailSession.getTransport("smtp");
			// Send the message
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			//save E-mail to Database
			EMail email = new EMail(content,subject, recipient, false);
			model.saveMail(email);
		} catch (Exception e) {
			//TODO Fehlerbehandlung ohne crash des Programms 
			e.printStackTrace();
		}
	}
	
	
	
	//===============Getter&Setter========================
	
	public String[] getSessionTabsIds() {
		return model.loadSessionTabsIds();
	}

	public Appointment[] getNext24hourAppointments() {
		return model.loadNext24hourAppointments();
	}
	


	public Student getStudent(int mtrNr) {
		return model.getStudent(mtrNr);
		
	}

	public Concern getConcern(int concernId) {
		return model.getConcern(concernId);
	}

	public Statistic getStatistic(int statisticId) {
		return model.getStatistic(statisticId);
	}


	public void deleteStudent(Student s) {
		model.deleteStudent(s);
		
		
	}


	public void openNewStudentTab() {
		mainView.openNewStudentTab();
		
	}


	public void openNewConcernTab(ObservableList<Student> students) {
		mainView.openNewConcernTab(students);
		
	}

}
