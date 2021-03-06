package tp.options;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.model.Options;
import tp.model.PO;
import tp.model.Subject;
import tp.model.Topic;

public class OptionsView extends GridPane {
	

	private Presenter presenter;
	private Options options;
	
	//============================================

	private ListView<PO> posList;
	private ListView<Topic> topicsList;
	private ListView<Subject> subjectsList;

	private Button addTopicButton;
	private Button addPOButton;
	private Button addSubjectButton;
	
	private Button deleteTopicButton;
	private Button deletePOButton;
	private Button deleteSubjectButton;
	
	//------------------------------
	private GridPane userDataGridPane;
	
	private Label userDataLabel;
	private Label userNameLabel;
	private Label currentUserNameLabel;
	private Label userIDLabel;
	private Label currentUserIDLabel;
	private Button editUserDataButton;

	public OptionsView(Presenter presenter) {
		this.presenter = presenter;
		options = presenter.getOptions();
		buildView();
		fillView();
	}

	private void buildView() {
		setPadding(new Insets(10, 10, 10, 10));
		setHgap(10);
		setVgap(10);

		posList = new ListView<PO>();
		topicsList = new ListView<Topic>();
		subjectsList = new ListView<Subject>();

		addTopicButton = new Button("+");
		addPOButton = new Button("+");
		addSubjectButton = new Button("+");
		
		deleteTopicButton = new Button("L�schen");
		deletePOButton= new Button("L�schen");
		deleteSubjectButton= new Button("L�schen");
		
		//--------------------------------------------
		
		userDataGridPane = new GridPane();
		
		userDataLabel = new Label("Nutzerdaten:");
		userNameLabel = new Label("Nutzername");
		currentUserNameLabel = new Label(options.getUserName());
		userIDLabel = new Label("rf/t Kennung");
		currentUserIDLabel = new Label(options.getUserID());
		editUserDataButton = new Button("Nutzerdaten �ndern");
		
		//============================================

		add(new Label("Themen (von Anliegen)"), 0, 0);
		HBox topicButtonBox = new HBox(deleteTopicButton, addTopicButton);
		add(topicButtonBox,1,0);
		add(topicsList, 0, 1, 2, 1);
		topicButtonBox.setAlignment(Pos.CENTER_RIGHT);

		add(new Label("PO's (Studieng�nge)"), 2, 0);
		HBox poButtonBox = new HBox(deletePOButton, addPOButton);
		add(poButtonBox,3,0);
		add(posList, 2, 1, 2, 1);
		poButtonBox.setAlignment(Pos.CENTER_RIGHT);

		add(new Label("Module"), 4, 0);
		HBox subjectButtonBox = new HBox(deleteSubjectButton,addSubjectButton);
		add(subjectButtonBox, 5,0);
		add(subjectsList, 4, 1 , 2, 1);
		subjectButtonBox.setAlignment(Pos.CENTER_RIGHT);
		
		add(userDataGridPane,0,2,2,1);
		
		//===================================================================
		//Constraints
						
		ColumnConstraints col = new ColumnConstraints();
		col.setPercentWidth(100 / 6);
				
		getColumnConstraints().addAll(col,col,col,col,col,col);
		
		//-------------------------------------------------
		
		RowConstraints buttonRow = new RowConstraints();
		buttonRow.setPercentHeight(5);
		
		RowConstraints listViewRow = new RowConstraints();
		listViewRow.setPercentHeight(75);
		
		RowConstraints userDataGridRow = new RowConstraints();
		userDataGridRow.setPercentHeight(20);
		
		getRowConstraints().addAll(buttonRow, listViewRow,userDataGridRow);
		
		
		//------------------------------------
		
		userDataGridPane.setPadding(new Insets(10, 10, 10, 10));
		userDataGridPane.setHgap(10);
		userDataGridPane.setVgap(10);
		
		userDataGridPane.add(userDataLabel,0,0,2,1);
		userDataGridPane.add(userNameLabel,0,1);
		GridPane.setHalignment(currentUserNameLabel, HPos.RIGHT);
		userDataGridPane.add(currentUserNameLabel,1,1);
		userDataGridPane.add(userIDLabel,0,2);
		GridPane.setHalignment(currentUserIDLabel, HPos.RIGHT);
		userDataGridPane.add(currentUserIDLabel,1,2);
		GridPane.setHalignment(editUserDataButton, HPos.RIGHT);
		userDataGridPane.add(editUserDataButton,0,3,2,1);
		
		
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(50);
		userDataGridPane.getColumnConstraints().add(column);
		userDataGridPane.getColumnConstraints().add(column);
		
		userDataGridPane.setBackground(new Background(new BackgroundFill(Color.BISQUE, null, null)));
		
		//============================================

		addTopicButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neues Thema");
            stage.setResizable(false);
            stage.getIcons().add(new Image("\\Icon.png"));
            stage.setScene(new Scene(new EditTopicView(stage, presenter, (OptionsView) topicsList.getParent()), getWidth()*(0.3), getHeight()*(0.7)));
            stage.show();
		});
		
		
		
		addPOButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neue PO");
            stage.setResizable(false);
            stage.getIcons().add(new Image("\\Icon.png"));
            stage.setScene(new Scene(new EditPOView(stage, presenter, (OptionsView) posList.getParent()), getWidth()*(0.3), getHeight()*(0.7)));
            stage.show();
		});
		addSubjectButton.setOnAction((event)-> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Neues Modul");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(new EditSubjectView(stage, presenter, (OptionsView) posList.getParent()), 300, 200));
            stage.show();
		});
		
		editUserDataButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setAlwaysOnTop(true);
            stage.setTitle("Nutzerdaten �ndern");
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(new EditUserDataView(this, presenter, stage, options), getWidth()*(0.6), getHeight()*(0.7)));
            stage.show();
		});
		
		deleteTopicButton.setOnAction((event) -> {
			Topic topicToDelete = topicsList.getSelectionModel().getSelectedItem();
			if(topicToDelete != null)
			{
				presenter.deleteTopic(topicToDelete);
				topicsList.getItems().remove(topicToDelete);
			}
		});
		
		deletePOButton.setOnAction((event) -> {
			PO poToDelete = posList.getSelectionModel().getSelectedItem();
			if(poToDelete != null)
			{
				presenter.deletePO(poToDelete);
				posList.getItems().remove(poToDelete);
			}
		});
		
		deleteSubjectButton.setOnAction((event) -> {
			Subject subjectToDelete = subjectsList.getSelectionModel().getSelectedItem();
			if(subjectToDelete != null)
			{
				presenter.deleteSubject(subjectToDelete);
				subjectsList.getItems().remove(subjectToDelete);
			}
		});
		
		topicsList.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Topic selectedTopic = topicsList.getSelectionModel().getSelectedItem();
		        	if(selectedTopic != null)
		        	{
		        		Stage stage = new Stage();
		    			stage.setAlwaysOnTop(true);
		    			stage.initModality(Modality.APPLICATION_MODAL);
		                stage.setTitle("Thema \"" + selectedTopic.getTitle() + "\" bearbeiten");
		                stage.setResizable(false);
		                stage.getIcons().add(new Image("\\Icon.png"));
		                stage.setScene(new Scene(new EditTopicView(stage, presenter,(OptionsView) topicsList.getParent(), selectedTopic), getWidth()*(0.3), getHeight()*(0.7)));
		                stage.show();
		        	}
		                               
		        }
		    }
		});
		
		
		posList.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	PO selectedPO = posList.getSelectionModel().getSelectedItem();
		        	if(selectedPO != null)
		        	{
		        		Stage stage = new Stage();
		    			stage.setAlwaysOnTop(true);
		    			stage.initModality(Modality.APPLICATION_MODAL);
		                stage.setTitle("PO \"" + selectedPO.getName() + "\" bearbeiten");
		                stage.setResizable(false);
		                stage.getIcons().add(new Image("\\Icon.png"));
		                stage.setScene(new Scene(new EditPOView(stage, presenter, (OptionsView) posList.getParent(), selectedPO), getWidth()*(0.3), getHeight()*(0.7)));
		                stage.show();
		        	}
		                               
		        }
		    }
		});
		
		
		subjectsList.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
		        	Subject selectedSubject = subjectsList.getSelectionModel().getSelectedItem();
		        	if(selectedSubject != null)
		        	{
		        		Stage stage = new Stage();
		    			stage.setAlwaysOnTop(true);
		    			stage.initModality(Modality.APPLICATION_MODAL);
		                stage.setTitle("Modul \"" + selectedSubject.getTitle() + "\" bearbeiten");
		                stage.setResizable(false);
		                stage.getIcons().add(new Image("\\Icon.png"));
		                stage.setScene(new Scene(new EditSubjectView(stage, presenter, (OptionsView) posList.getParent(), selectedSubject), getWidth()*(0.3), getHeight()*(0.3)));
		                stage.show();
		        	}
		                               
		        }
		    }
		});
		
		
		
	}

	private void fillView() {

		posList.setItems(presenter.getPOs());
		
		subjectsList.setItems(presenter.getSubjects());
		
		//Topics setzen, aber Standardtopic("Sonstige") mit id=1 ausblenden, damit nicht editierbar
		ObservableList<Topic> topics = presenter.getTopics();
		for(int i = 0; i < topics.size(); i++)
		{
			if(topics.get(i).getId() == 1)
			{
				topics.remove(topics.get(i));
				break;
			}
			
		}
		topicsList.setItems(topics);
		
	}


	public void updateView() {
		fillView();
		options = presenter.getOptions();
		currentUserNameLabel.setText(options.getUserName());
		currentUserIDLabel.setText(options.getUserID());
	}

}
