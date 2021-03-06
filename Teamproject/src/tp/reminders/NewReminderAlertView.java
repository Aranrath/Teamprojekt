package tp.reminders;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Reminder;

public class NewReminderAlertView extends GridPane
{
	private Presenter presenter;
	private ObservableList<Reminder> newReminders;
	private Stage stage;
	
	//===========================
	
	private Label newRemindersLabel;
	private ListView<Reminder> remindersListView;
	private Label addedToRemindersViewLabel;
	private Button showRemindersViewButton;
	
	
	public NewReminderAlertView(Presenter presenter, Stage stage, ObservableList<Reminder> newReminders)
	{
		this.presenter = presenter;
		this.newReminders = newReminders;
		this.stage = stage;
		buildView();
		
	}

	private void buildView() {
		
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);
		
		newRemindersLabel = new Label("Neue Erinnerungen seit Ihrer letzen Sitzung:");
		remindersListView = new ListView<Reminder>(newReminders);
		addedToRemindersViewLabel = new Label("Die Erinnerungen wurden der List Ihrer zu bearbeitenden Erinnerungen hinzugefügt");
		showRemindersViewButton = new Button("Zu bearbeitende Erinnerungen anzeigen");
		
		//=============================
		
		add(newRemindersLabel,0,0);
		add(remindersListView,0,1);
		add(addedToRemindersViewLabel,0,2);
		GridPane.setHalignment(addedToRemindersViewLabel, HPos.RIGHT);
		add(showRemindersViewButton,0,3);
		GridPane.setHalignment(showRemindersViewButton, HPos.RIGHT);
		
		//===================================================================
		//Constraints
						
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100);
				
		getColumnConstraints().addAll(col);
		
		//-------------------------------------------------
		
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(25/3);
		
		RowConstraints listViewRow = new RowConstraints();
		listViewRow.setPercentHeight(75);
		
		getRowConstraints().addAll(buttonRow, listViewRow,buttonRow, buttonRow);
		
		
		//==============================
		remindersListView.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Reminder selectedReminder = remindersListView.getSelectionModel().getSelectedItem();
		        	if(selectedReminder != null)
		        	{
		        		stage.close();
						Long concernId = selectedReminder.getConcernId();
		        		presenter.openConcernTab(presenter.getConcern(concernId));
		        	}
		                             
		        }
		    }
		});
		
		showRemindersViewButton.setOnAction(event -> {
			stage.close();
			presenter.openRemindersTab();
		});
		
	}


}
