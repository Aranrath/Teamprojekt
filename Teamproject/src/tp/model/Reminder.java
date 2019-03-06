package tp.model;

import java.util.Date;

public class Reminder {
	
	private int id;
	private String message;
	private Date date;
	
	public Reminder(int id, String message, Date date)
	{
		this.id = id;
		this.message = message;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public Date getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}