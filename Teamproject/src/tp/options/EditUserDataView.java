package tp.options;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Options;

public class EditUserDataView extends GridPane
{
	private Presenter presenter;
	private Options options;
	private Stage stage;
	
	//=======================
	
	private Label userNameLabel;
	private TextField userNameTextField;
	private Label userIDLabel;
	private TextField userIDTextField;
	private Label passwordLabel;
	private TextField passwordPasswordField;
	private Button saveButton;
	private Label errorLabel;
	
	public EditUserDataView (Presenter presenter, Stage stage, Options options)
	{
		this.presenter = presenter;
		this.options = options;
		this.stage = stage;
		
		setBackground(new Background(new BackgroundFill(Color.BISQUE, null, null)));
		
		buildView();
		fillView();

	}


	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);
		
		 userNameLabel = new Label("Nutzername (unter diesem Namen werden E-Mails versendet):");
		 userNameTextField = new TextField();
		 userIDLabel = new Label("Benutzername der r/ft Webmail (zum Versenden und Empfangen von E-Mails der r/ft Webmail):");
		 userIDTextField = new TextField();
		 passwordLabel = new Label("Passwort der r/ft Webmail (zum Versenden und Empfangen von E-Mails der r/ft Webmail):");
		 passwordPasswordField = new PasswordField();
		 saveButton = new Button("Speichern");
		 errorLabel = new Label("");
		 errorLabel.setText("Alle Felder m�ssen ausgef�llt sein");
		 errorLabel.setTextFill(Color.RED);
		 errorLabel.setVisible(false);
		
		// =====================================================================

			add(userNameLabel, 0, 0);
			add(userNameTextField, 0, 1);
			add(userIDLabel, 0, 2);
			add(userIDTextField, 0, 3);
			add(passwordLabel, 0, 4);
			add(passwordPasswordField, 0, 5);
			add(saveButton, 0, 6);
			GridPane.setHalignment(saveButton, HPos.RIGHT);
			add(errorLabel, 0, 7);
			GridPane.setHalignment(errorLabel, HPos.RIGHT);
		 
			// =====================================================================
			
			saveButton.setOnAction(e -> {
				boolean changed = false;
				
				String newUserName = userNameTextField.getText();
				String newUserID = userIDTextField.getText();
				String newPassword = passwordPasswordField.getText();
				
				if(newUserName.equals("") || newUserID.equals("") || newPassword.equals(""))
				{
					errorLabel.setVisible(true);
					return;
				}
				
				if(options.getUserName()!= newUserName)
				{
					options.setUserName(newUserName);
					changed = true;
					
				}
				if(options.getUserID()!= newUserID)
				{
					options.setUserID(newUserID);
					changed = true;
					
				}
				if(options.getPassword()!= newPassword)
				{
					options.setPassword(newPassword);
					changed = true;
					
				}
				
				if(changed)
				{
					presenter.saveEditedOptions(options);
				}
				stage.close();
				
				presenter.showNewReminderView(options);
			});
		 
		 
	}
	
	private void fillView() {
		
		String userName = options.getUserName();
		String userID = options.getUserID();
		String password = options.getPassword();
		
		if(userName != null)
		{
			userNameTextField.setText(userName);
		}
		if(userID != null)
		{
			userIDTextField.setText(userID);
		}
		if(password != null)
		{
			passwordPasswordField.setText(password);
		}
		
	}

}