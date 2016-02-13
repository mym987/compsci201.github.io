package main;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;
import ngram.MarkovModel;
import gui.NgramGui;
/**
 * Main for Markov Text Generation Program
 * 
 * @author Mike Ma
 * @date Feb 13, 2016
 *
 */
public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		NgramGui gui = new NgramGui(stage);
		Random rand = new Random(1234);
		gui.setModel(new MarkovModel(rand));
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
