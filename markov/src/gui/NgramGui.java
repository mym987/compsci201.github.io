package gui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import ngram.INgram;
import ngram.MarkovModel;

/**
 * GUI for Markov Text Generation Program
 * 
 * @author Mike Ma
 * @date Feb 13, 2016
 *
 */
public class NgramGui {
	private INgram _Model;
	private final VBox _Pane;
	private final Stage _Stage;
	private final TextArea _Text;
	private final TextField _Status;
	private boolean _loaded = false;

	@SuppressWarnings("static-access")
	public NgramGui(Stage stage) {
		_Pane = new VBox(5);
		_Stage = stage;
		stage.setScene(new Scene(_Pane));
		_Text = getTextArea();
		_Status = new TextField("Markov");
		_Status.setEditable(false);
		_Stage.setMaxWidth(Double.MAX_VALUE);
		TitledPane outPane = enclose("output", _Text);
		outPane.setMaxHeight(Double.MAX_VALUE);
		TitledPane msgPane = enclose("message", _Status);
		_Pane.getChildren().addAll(new Menus(this), outPane, msgPane);
		_Pane.setVgrow(outPane, Priority.ALWAYS);
		stage.show();
		showErr(new RuntimeException());
	}

	public void setModel(INgram model) {
		_Model = model;
	}

	protected Stage getStage() {
		return _Stage;
	}

	private TitledPane enclose(String title, Node n) {
		TitledPane pane = new TitledPane(title, n);
		pane.setCollapsible(false);
		pane.setAlignment(Pos.CENTER);
		return pane;
	}

	private TextArea getTextArea() {
		TextArea txt = new TextArea("CompSci 201 Markov Assignment");
		txt.setEditable(false);
		txt.setWrapText(true);
		txt.setMaxWidth(Double.MAX_VALUE);
		txt.setMaxHeight(Double.MAX_VALUE);
		return txt;
	}

	protected void showErr(Exception e) {
		new ErrorDialog(e);
	}

	protected void clear() {
		_Text.clear();
		_Status.clear();
	}

	protected void readFile(File file) {
		clear();
		Scanner s = null;
		try {
			s = new Scanner(new BufferedInputStream(new FileInputStream(file)));
			_Model.initialize(s);
			_loaded = true;
		} catch (Exception e) {
			showErr(e);
		} finally {
			if (s != null)
				s.close();
		}
	}

	protected void readURL(String url) {
		clear();
		Scanner s = null;
		try {
			s = new Scanner(new BufferedInputStream((new URL(url)).openStream()));
			_Model.initialize(s);
			_loaded = true;
		} catch (Exception e) {
			showErr(e);
		} finally {
			if (s != null)
				s.close();
		}
	}
	
	protected boolean getLoaded(){
		return _Model != null && _loaded;
	}

	protected void saveFile(File file) {
		//TODO
	}

	protected void generate(int k, int length) {
		try {
			long start = System.nanoTime();
			String s = _Model.makeNGram(k, length);
			long end = System.nanoTime();
			String msg = "";
			if (_Model instanceof MarkovModel)
				msg = String.format("Read %d chars in %.3f seconds", s.length(), (end - start) / 1.0);
			else
				msg = String.format("Read %d words in %.3f seconds", s.length() - s.replace(" ", "").length() + 1,
						(end - start) / 1.0);
			showMsg(msg);
		} catch (Exception e) {
			showErr(e);
		}
	}

	protected void updateText(String s) {
		_Text.appendText(s);
	}

	protected void showMsg(String msg) {
		_Status.setText(msg);
	}
}

class Menus extends MenuBar {
	private NgramGui _Gui;

	protected Menus(NgramGui gui) {
		_Gui = gui;
		Menu file = new Menu("File");
		Menu help = new Menu("About");
		file.getItems().addAll(getOpen(), getURL(), getSave(), getClear(), new SeparatorMenuItem(), getExit());
		getMenus().addAll(file, help);
	}

	private MenuItem getOpen() {
		MenuItem item = new MenuItem("Open File");
		item.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
		item.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Text File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
					new ExtensionFilter("All Files", "*.*"));
			File selectedFile = fileChooser.showOpenDialog(_Gui.getStage());
			if (selectedFile != null && selectedFile.canRead()) {
				_Gui.readFile(selectedFile);
			}
		});
		return item;
	}

	private MenuItem getURL() {
		MenuItem item = new MenuItem("Open URL");
		item.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN));
		item.setOnAction(e -> {
			Dialog<String> dialog = new TextInputDialog();
			dialog.setTitle("Enter URL");
			dialog.setHeaderText("Please enter a valid URL");
			dialog.showAndWait().ifPresent(s -> _Gui.readURL(s));
		});
		return item;
	}

	private MenuItem getSave() {
		MenuItem item = new MenuItem("Save Text");
		item.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN));
		item.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Text File");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showSaveDialog(_Gui.getStage());
			if (selectedFile != null) {
				_Gui.saveFile(selectedFile);
			}
		});
		return item;
	}

	private MenuItem getExit() {
		MenuItem item = new MenuItem("Exit");
		item.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));
		item.setOnAction(e -> {
			System.exit(0);
		});
		return item;
	}

	private MenuItem getClear() {
		MenuItem item = new MenuItem("Clear Text");
		item.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
		item.setOnAction(e -> {
			_Gui.clear();
		});
		return item;
	}
}

class ControlPane extends GridPane {
	private TextField _TFK, _TFL;
	private Button _OK;
	private final NgramGui _Gui;

	ControlPane(NgramGui gui) {
		_Gui = gui;
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(20, 150, 10, 10));
		addButton();
		addK();
		addL();
	}
	
	private void addButton(){
		_OK = new Button("Generate");
		add(_OK, 2,0);
		_OK.setOnAction(e->generate());
		_OK.setDisable(true);
	}
	
	private void generate(){
		try {
			int k = Integer.parseInt(_TFK.getText().trim());
			int l = Integer.parseInt(_TFL.getText().trim());
			_Gui.generate(k, l);
		} catch (Exception e) {
			_Gui.showErr(e);
		}
	}

	private void addK() {
		_TFK = new TextField("1");
		_TFK.setPromptText("Enter a positive integer");
		_TFK.setTooltip(new Tooltip("Enter a positive integer"));
		add(new Label("K:"), 0, 0);
		add(_TFK, 1, 0);
		_TFK.textProperty().addListener((ch,oV,nV)->validate());
	}

	private void addL() {
		_TFL = new TextField("100");
		_TFL.setPromptText("Enter a positive integer");
		_TFL.setTooltip(new Tooltip("Enter a positive integer"));
		add(new Label("MaxLength:"), 0, 1);
		add(_TFL, 1, 1);
		_TFL.textProperty().addListener((ch,oV,nV)->validate());
	}

	protected void validate() {
		try {
			int k = Integer.parseInt(_TFK.getText().trim());
			int l = Integer.parseInt(_TFL.getText().trim());
			_OK.setDisable(!(k > 0 && l > 0 && _Gui.getLoaded()));
		} catch (Exception e) {
			_OK.setDisable(true);
		}
	}
}

class ErrorDialog extends Alert {
	public ErrorDialog(Exception e) {
		super(AlertType.ERROR);
		setTitle("Error");
		setHeaderText("Error 错误 エラー Erreur خطأ");
		setContentText(e.toString());

		// Set expandable Exception into the dialog pane.
		getDialogPane().setExpandableContent(getContent(e));
		showAndWait();
	}

	private GridPane getContent(Exception e) {
		// Create expandable Exception.
		Label label = new Label("The exception stacktrace was:");
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionText = sw.toString();

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		return expContent;
	}
}
