package tp.concern;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tp.Presenter;
import tp.appointment.NewAppointmentView;
import tp.forms.FormsView;
import tp.model.Appointment;
import tp.model.Concern;
import tp.model.Form;
import tp.model.MyTab;
import tp.model.Reminder;
import tp.model.Student;
import tp.model.Topic;
import tp.options.EditTopicView;
import tp.reminders.NewReminderView;

public class ConcernView extends GridPane {

	private Presenter presenter;
	private Concern concern;
	private MyTab tab;
	// lokal hinterlegte Studenten, nicht unbedingt gespeichert. Nutzen: Basis f�r
	// Suchanfragen
	private ObservableList<Student> localStudents;
	private ObservableList<Student> filteredStudents;

	// ==================================

	private Label titleLabel;
	private TextField titleTextField;
	private Label errorLabel;
	private Button createButton;
	private Button closeButton;
	private Label closeStatusLabel;
	private HBox topicHBox;
	private Label topicLabel;
	private ComboBox<Topic> topicComboBox;
	private Button newTopicButton;

	private Label studentLabel;
	private TextField searchTextField;
	private Button changeStudentSelectionButton;
	private TableView<Student> studentTableView;

	private Label notesLabel;
	private TextArea notesTextArea;

	private Label reminderLabel;
	private HBox reminderHBox;
	private Button newReminderButton;
	private Button deleteReminderButton;
	private TableView<Reminder> reminderTableView;

	private Label fileLabel;
	private Button changeFileSelectionButton;
	private TableView<Form> fileTableView;

	private Label appointmentLabel;
	private HBox appointmentHBox;
	private Button newAppointmentButton;
	private Button deleteAppointmentButton;
	private TableView<Appointment> appointmentTableView;

	// =========================================================================
	// Konstruktoren
	// =========================================================================

	// neue ConcernView
	public ConcernView(Presenter presenter, MyTab tab) {
		this.presenter = presenter;
		this.tab = tab;
		localStudents = FXCollections.observableArrayList();

		buildView();

	}

	// neue Concern View mit bereits gew�hlten Studenten
	public ConcernView(Presenter presenter, MyTab tab, ObservableList<Student> students) {
		this.presenter = presenter;
		this.tab = tab;
		localStudents = FXCollections.observableArrayList(students);
		buildView();

	}

	// bei bestehendem Concern
	public ConcernView(Presenter presenter, MyTab tab, Concern concern) {
		this.presenter = presenter;
		this.tab = tab;
		this.concern = concern;
		if (concern.getStudents() != null) {
			localStudents = concern.getStudents();
		} else {
			localStudents = FXCollections.observableArrayList();
		}

		buildView();
		fillView();
	}

	// =========================================================================
	// private Methoden
	// =========================================================================

	@SuppressWarnings("unchecked")
	private void buildView() {
		setPadding(new Insets(20));
		setHgap(20);
		setVgap(20);

		titleLabel = new Label("Titel:");
		titleTextField = new TextField(
				new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		errorLabel = new Label("");
		errorLabel.setTextFill(Color.RED);
		createButton = new Button("Erstellen");
//		TODO
		//if (concern == null) {
//			createButton = new Button("Erstellen");
//		} else {
//			saveButton = new Button("Speichern");
//		}

		// ---------------------------------------------------

		closeButton = new Button("Abschlie�en");
		closeStatusLabel = new Label();
		closeStatusLabel.setVisible(false);

		// Im Falle eines neuen (ungespeicherten) Anliegen
		if (concern == null) {
			closeButton.setVisible(false);
		}

		// ---------------------------------------------------

		topicLabel = new Label("Thema:");
		ObservableList<Topic> topics = presenter.getTopics();
		topicComboBox = new ComboBox<Topic>(FXCollections.observableArrayList(topics));
		// Standard-Thema ("Sonstige" ausw�hlen), id == 1
		for (int i = 0; i < topics.size(); i++) {
			if (topics.get(i).getId() == 1) {
				topicComboBox.getSelectionModel().select(topics.get(i));
				break;
			}

		}

		newTopicButton = new Button("+");
		topicHBox = new HBox(topicComboBox, newTopicButton);
		newTopicButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		topicComboBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(topicComboBox, Priority.ALWAYS);

		// ---------------------------------------------------

		studentLabel = new Label("Studenten:");
		searchTextField = new TextField("");
		searchTextField.setPromptText("Durchsuche Studenten");
		HBox.setHgrow(searchTextField, Priority.ALWAYS);

		changeStudentSelectionButton = new Button("Auswahl �ndern");
		filteredStudents = FXCollections.observableArrayList(localStudents);
		studentTableView = new TableView<Student>(filteredStudents);

		notesLabel = new Label("Notizen");
		notesTextArea = new TextArea();

		reminderLabel = new Label("Erinnerungen");
		newReminderButton = new Button("Neu");
		deleteReminderButton = new Button("L�schen");
		reminderTableView = new TableView<Reminder>();
		reminderHBox = new HBox(newReminderButton, deleteReminderButton);

		fileLabel = new Label("Dateien");
		changeFileSelectionButton = new Button("Auswahl �ndern");
		fileTableView = new TableView<Form>();

		appointmentLabel = new Label("Termine");
		newAppointmentButton = new Button("Neu");
		deleteAppointmentButton = new Button("L�schen");
		appointmentHBox = new HBox(newAppointmentButton, deleteAppointmentButton);
		appointmentTableView = new TableView<Appointment>();

		// ======================================================================

		TableColumn<Student, Integer> mtrNrCol = new TableColumn<Student, Integer>("Matrikelnr.");
		mtrNrCol.setCellValueFactory(new PropertyValueFactory<>("mtrNr"));

		TableColumn<Student, String> nameCol = new TableColumn<Student, String>("Nachname");
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));

		TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("Vorname");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));

		studentTableView.getColumns().addAll(mtrNrCol, nameCol, firstNameCol);

		TableColumn<Reminder, Date> dateCol = new TableColumn<Reminder, Date>("Datum");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Reminder, String> messageCol = new TableColumn<Reminder, String>("Nachricht");
		messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

		reminderTableView.getColumns().addAll(dateCol, messageCol);

		TableColumn<Form, String> fileNameCol = new TableColumn<Form, String>("Dateiname");
		fileNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		fileTableView.getColumns().add(fileNameCol);

		TableColumn<Appointment, Date> appDateCol = new TableColumn<Appointment, Date>("Datum");
		appDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Appointment, Long> startTimeCol = new TableColumn<Appointment, Long>("Von");
		startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeString"));

		TableColumn<Appointment, Long> endTimeCol = new TableColumn<Appointment, Long>("Bis");
		endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeString"));

		TableColumn<Appointment, String> roomNmbCol = new TableColumn<Appointment, String>("Raum");
		roomNmbCol.setCellValueFactory(new PropertyValueFactory<>("roomNmb"));

		appointmentTableView.getColumns().addAll(appDateCol, startTimeCol, endTimeCol, roomNmbCol);

		// ======================================================================

		add(titleLabel, 0, 0);
		add(titleTextField, 1, 0, 3, 1);
		add(errorLabel, 4, 1, 2, 1);
		GridPane.setHalignment(errorLabel, HPos.LEFT);
		createButton.setMaxWidth(Double.MAX_VALUE);
		add(createButton, 4, 0);
		GridPane.setHalignment(createButton, HPos.LEFT);
		closeButton.setMaxWidth(Double.MAX_VALUE);
		//TODO 
		if (concern != null) {
			createButton.setVisible(false);
		} else {
			createButton.setVisible(true);
		}
		add(closeButton, 5, 0);
		GridPane.setHalignment(closeButton, HPos.LEFT);

		add(topicLabel, 0, 1);
		add(topicHBox, 1, 1, 3, 1);

		add(studentLabel, 0, 2);
		add(searchTextField, 1, 2, 3, 1);
		changeStudentSelectionButton.setMaxWidth(Double.MAX_VALUE);
		add(changeStudentSelectionButton, 4, 2);
		GridPane.setHalignment(changeStudentSelectionButton, HPos.LEFT);
		add(studentTableView, 0, 3, 6, 1);

		add(notesLabel, 6, 0);
		add(notesTextArea, 6, 1, 3, 3);

		add(reminderLabel, 0, 4);
		add(reminderHBox, 1, 4, 2, 1);
		add(reminderTableView, 0, 5, 3, 1);
		reminderHBox.setSpacing(5);
		reminderHBox.setAlignment(Pos.CENTER_RIGHT);

		add(fileLabel, 3, 4);
		add(changeFileSelectionButton, 4, 4, 2, 1);
		GridPane.setHalignment(changeFileSelectionButton, HPos.RIGHT);
		add(fileTableView, 3, 5, 3, 1);

		add(appointmentLabel, 6, 4);
		add(appointmentHBox, 7, 4, 2, 1);
		appointmentHBox.setSpacing(5);
		appointmentHBox.setAlignment(Pos.CENTER_RIGHT);
		add(appointmentTableView, 6, 5, 3, 1);

		// ==================================================

		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(100 / 9);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);
		getColumnConstraints().add(column);

		// ==================================================
		createButton.setOnAction((event) -> {
			save(true);

		});

		newTopicButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neues Thema");
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			stage.setResizable(false);
			stage.setScene(
					new Scene(new EditTopicView(stage, presenter, this), getWidth() * (0.6), getHeight() * (0.7)));
			stage.show();
		});

		searchTextField.textProperty().addListener((obs, oldText, newText) -> {
			filterStudents(newText);
		});

		changeStudentSelectionButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Studenten hinzuf�gen");
			stage.setResizable(false);
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			stage.setScene(new Scene(new AddStudentToConcernView(presenter, stage, this, studentTableView.getItems()),
					getWidth() * (0.6), getHeight() * (0.7)));
			stage.show();
		});

		newReminderButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neue Erinnerung hinzuf�gen");
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			stage.setResizable(false);
			stage.setScene(new Scene(new NewReminderView(stage, reminderTableView.getItems(), concern.getId()),
					getWidth() * (0.6), getHeight() * (0.7)));
			stage.show();
		});

		deleteReminderButton.setOnAction((event) -> {
			Reminder reminderToDelete = reminderTableView.getSelectionModel().getSelectedItem();

			if (reminderToDelete != null) {
				reminderTableView.getItems().remove(reminderToDelete);
				presenter.deleteReminder(reminderToDelete);
			}

		});

		changeFileSelectionButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Datei zum Anliegen hinzuf�gen");

			ObservableList<Form> topicRelatedFiles;
			Topic selectedTopic = topicComboBox.getSelectionModel().getSelectedItem();
			if (selectedTopic != null) {
				topicRelatedFiles = FXCollections.observableArrayList(selectedTopic.getForms());
			} else {
				topicRelatedFiles = FXCollections.observableArrayList();
			}

			ObservableList<Form> filesAlreadyInConcern = FXCollections.observableArrayList(fileTableView.getItems());

			stage.setResizable(false);
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			stage.setScene(new Scene(new FormsView(presenter, stage, this, filesAlreadyInConcern, topicRelatedFiles),
					600, 500));
			stage.show();
		});

		newAppointmentButton.setOnAction((event) -> {
			Stage stage = new Stage();
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Neuen Termin hinzuf�gen");
			stage.setResizable(false);
			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Icon.png")));
			stage.setScene(new Scene(new NewAppointmentView(stage, presenter, this), 400, 220));
			stage.show();
		});

		deleteAppointmentButton.setOnAction((event) -> {
			Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
			if (appointmentToDelete != null) {
				appointmentTableView.getItems().remove(appointmentToDelete);
				presenter.deleteAppointment(appointmentToDelete);
			}

		});

		closeButton.setOnAction(event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Anliegen " + concern.getTitle() + " abschlie�en");
			alert.setHeaderText("Bitte w�hlen sie den korrekten Abschluss-Status des Anliegen" + "\n"
					+ "INFO: Abgeschlossene Anliegen sind (mit Ausnahme des Fehleintrages) weiterhin einsehbar." + "\n"
					+ "ACHTUNG: Das Schlie�en eines Anliegens ist nicht umkehrbar");

			ButtonType completed = new ButtonType("Schlie�en mit Status \"Erledigt\"");
			ButtonType uncompleted = new ButtonType("Schlie�en mit Status \"Abgebrochen\"");
			ButtonType deletable = new ButtonType("L�schen als Fehleintrag");
			ButtonType abortMission = new ButtonType("Abbrechen");

			// Standard ButtonTypes entfernen
			alert.getButtonTypes().clear();

			// Eigene ButtonTypes hinzuf�gen
			alert.getButtonTypes().addAll(completed, uncompleted, deletable, abortMission);

			// Alert anzeigen
			Optional<ButtonType> option = alert.showAndWait();

			// Resultat verarbeiten
			if (option.get() == completed) {
				concern.setCompleted(true);
				concern.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				presenter.saveEditedConcern(concern);
				closeButton.setVisible(false);

			} else if (option.get() == uncompleted) {
				concern.setClosingDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				presenter.saveEditedConcern(concern);
				closeButton.setVisible(false);
			} else if (option.get() == deletable) {
				presenter.deleteConcern(concern);
				presenter.closeThisTab(tab);
			} else if (option.get() == abortMission) {

			}
		});

		topicComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (oldVal != null && oldVal.getForms() != null) {
				fileTableView.getItems().removeAll(oldVal.getForms());
			}
			if (newVal != null && newVal.getForms() != null) {
				fileTableView.getItems().addAll(newVal.getForms());
			}
		});

		studentTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
					Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
					if (selectedStudent != null) {
						presenter.openStudenTab(selectedStudent);
						;
					}

				}
			}
		});

		fileTableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() > 1) {
					Form selectedForm = fileTableView.getSelectionModel().getSelectedItem();
					if (selectedForm != null) {
						presenter.handleExportForm(selectedForm);

					}

				}
			}
		});

	}

	private boolean concernTitleAlreadyExists(String newTitle) {
		ArrayList<Concern> allConcerns = new ArrayList<>(presenter.getConcerns());

		if (concern != null)
		{
			for(int i = 0; i < allConcerns.size(); i++)
			{
				if(concern.getId() == allConcerns.get(i).getId())
				{
					allConcerns.remove(i);
					break;
				}
				
			}
			
		}

		for (Concern c : allConcerns) {
			if (c.getTitle().equals(newTitle)) {
				return true;
			}

		}

		return false;
	}

	private void filterStudents(String searchTerm) {

		if (searchTerm.isEmpty()) {
			filteredStudents.clear();
			filteredStudents.addAll(localStudents);
		} else {

			filteredStudents.clear();
			String[] searchTerms = searchTerm.toLowerCase().split(" ");

			for (Student student : localStudents) {
				if (Presenter.containsAll(student.toString().toLowerCase(), searchTerms)) {
					filteredStudents.add(student);
				}

			}

		}
	}

	private void fillView() {
		titleTextField.setText(concern.getTitle());
		topicComboBox.getSelectionModel().select(concern.getTopic());
		if (concern.getReminders() != null) {
			reminderTableView.setItems(concern.getReminders());
		}
		if (concern.getFiles() != null) {
			fileTableView.setItems(concern.getFiles());
		}
		if (concern.getTopic() != null && concern.getTopic().getForms() != null) {
			fileTableView.getItems().addAll(concern.getTopic().getForms());
		}
		notesTextArea.setText(concern.getNotes());
		if (concern.getAppointments() != null) {
			appointmentTableView.setItems(concern.getAppointments());
		}
		
		// Wenn Anliegen abgeschlossen ist closeStatus setzen
		if (concern.getClosingDate() != null)
		{
			closeButton.setVisible(false);
			if (concern.isCompleted() == true)
			{
				closeStatusLabel.setText("Status: Erledigt (" + concern.getClosingDate() + ")");
			} else {
				closeStatusLabel.setText("Status: Abgebrochen (" + concern.getClosingDate() + ")");
			}

			closeStatusLabel.setVisible(true);
		}
		
		if (concern.getStudents() != null)
		{
			localStudents = concern.getStudents();
		}
		else
		{
			localStudents = FXCollections.observableArrayList();
		}
		filterStudents(searchTextField.getText());

	}

	// =========================================================================
	// �ffentliche Methoden
	// =========================================================================

	//blockedByErrors = false: Save Methode wird auch ausgef�hrt, wenn bestimmte Felder nicht ausgef�llt sind: Standartwert
	//blockedByErrors = true: Save Methode wird nicht ausgef�hrt, wenn bestimmte Felder nicht ausgef�llt sind: error message
	public void save(boolean blockedByErrors) {
		// -------------------------------------------------------------------
		// Abfangen von Fehlern eines unausreichend ausgef�lltem Profil
		// -------------------------------------------------------------------

		String newTitle = titleTextField.getText();

		if (blockedByErrors) {
			if (newTitle.equals("")) {
				errorLabel.setText("Titel nicht gesetzt");
				return;
			}
			if (concernTitleAlreadyExists(newTitle)) {
				errorLabel.setText("Titel bereits vergeben");
				return;
			}

		} else if (!blockedByErrors) {
			if (newTitle.equals("")) {
				newTitle = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			}
			if (concernTitleAlreadyExists(newTitle)) {
				int i = 2;
				String changedNewTitle;
				do {
					changedNewTitle = newTitle + "_" + i;
					i++;
				} while (concernTitleAlreadyExists(changedNewTitle));

				newTitle = changedNewTitle;

			}
			String title = newTitle;
			Platform.runLater(()->titleTextField.setText(title));

		}

		// -------------------------------------------------------------------
		// Speichern
		// -------------------------------------------------------------------

		Topic newTopic = topicComboBox.getSelectionModel().getSelectedItem();

		// Nur Files die nicht zum Topic geh�ren
		ObservableList<Form> newFiles = FXCollections.observableArrayList(fileTableView.getItems());
		newFiles.removeAll(newTopic.getForms());

		// Kein Anliegen hinterlegt -> Neu erstellen
		if (concern == null) {
			// Concern erstellen
			concern = new Concern(newTitle);

			// Attribute setzen
			concern.setTopic(newTopic);
			concern.setStudents(localStudents);
			concern.setNotes(notesTextArea.getText());
			concern.setReminders(reminderTableView.getItems());
			concern.setAppointments(appointmentTableView.getItems());
			concern.setFiles(newFiles);

			// Speichern + Anpassen
			int newConcernId = presenter.saveNewConcern(concern);
			concern.setId(newConcernId);
			tab.setTabId("c" + newConcernId);
			createButton.setVisible(false);
			closeButton.setVisible(true);

		}
		// Bestehendes Anliegen -> �nderungen speichern
		else {
			// Attribute setzen
			concern.setTitle(newTitle);
			concern.setTopic(newTopic);
			concern.setStudents(localStudents);
			concern.setNotes(notesTextArea.getText());
			concern.setReminders(reminderTableView.getItems());
			concern.setAppointments(appointmentTableView.getItems());
			concern.setFiles(newFiles);

			// Speichern
			presenter.saveEditedConcern(concern);

		}

		// -------------------------------------------------------------------
		// Oberfl�che anpassen
		// -------------------------------------------------------------------

		if(blockedByErrors) {
			errorLabel.setText("");
			presenter.updateRightToolbar();
		} else {
			errorLabel.setText("");
			presenter.updateRightToolbar();
		}

		// Tabbeschriftungs-Bug (Doppelte Tabbeschriftung)
		// tab.setText(newTitle);

	}
	
	
	public void addStudentsToConcern(ObservableList<Student> students) {
		localStudents.clear();
		localStudents.addAll(students);

		filterStudents(searchTextField.getText());

	}

	public void addFilesToConcern(ObservableList<Form> files) {
		Topic selectedTopic = topicComboBox.getSelectionModel().getSelectedItem();
		if (selectedTopic != null) {
			fileTableView.setItems(FXCollections.observableArrayList(selectedTopic.getForms()));
		} else {
			fileTableView.getItems().clear();
		}
		fileTableView.getItems().addAll(files);
	}

	public void addAppointment(Appointment appointment) {
		appointmentTableView.getItems().add(appointment);
		// Wenn Concern schon existiert, Termin direkt in Datenbank �bertragen
		if (concern != null) {
			presenter.saveNewAppointment(concern.getId(), appointment);
			presenter.updateRightToolbar();
		}
	}

	/**
	 * genutzt, wenn �ber das entsprechende Pop-Up Fenster Themen neu erstellt
	 * werden
	 */
	public void addNewTopic(Topic topic) {
		topicComboBox.getItems().add(topic);
		topicComboBox.getSelectionModel().select(topic);
	}

	public boolean isNotSaved() {
		if(concern==null) {
			return true;
		}else {
			return false;
		}
	}
	
	public void updateView() {

		//Concern neu holen
		concern = presenter.getConcern(concern.getId());

		fillView();

		// Evtl. ge�nderte Themen (�ber Options) aktualisieren
		Topic selectedTopic = topicComboBox.getSelectionModel().getSelectedItem();
		topicComboBox.setItems(presenter.getTopics());

		// Topic Auswahl wiederherstellen
		if (selectedTopic != null) {
			for (Topic topic : topicComboBox.getItems()) {
				if (topic.getId() == selectedTopic.getId()) {
					topicComboBox.getSelectionModel().select(topic);
					break;
				}

			}

		}

	}

}
