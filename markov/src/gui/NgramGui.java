package gui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Scanner;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ngram.INgram;
import ngram.MarkovModel;

public class NgramGui{
	private INgram _Model;
	private BorderPane _Pane;
	
	public NgramGui(Stage stage){
		_Pane = new BorderPane();
		stage.setScene(new Scene(_Pane));
		stage.show();
	}
	
	public void setModel(INgram model){
		_Model = model;
	}
	
	protected void showErr(Exception e){
		
	}
	
	protected void clear(){
		
	}
	
	protected void readFile(File file){
		clear();
		Scanner s = null;
		try{
			s = new Scanner(new BufferedInputStream(new FileInputStream(file)));
			_Model.initialize(s);
		}catch(Exception e){
			showErr(e);
		}finally {
			if(s!=null)
				s.close();
		}
	}
	
	protected void readURL(String url){
		clear();
		Scanner s = null;
		try{
			s = new Scanner(new BufferedInputStream((new URL(url)).openStream()));
			_Model.initialize(s);
		}catch(Exception e){
			showErr(e);
		}finally {
			if(s!=null)
				s.close();
		}
	}
	
	protected void generate(int k, int length){
		try{
			long start = System.nanoTime();
			String s = _Model.makeNGram(k, length);
			long end = System.nanoTime();
			String msg = "";
			if(_Model instanceof MarkovModel)
				msg = String.format("Read %d chars in %.3f seconds", s.length(), (end-start)/1.0);
			else
				msg = String.format("Read %d words in %.3f seconds", s.length() - s.replace(" ","").length() + 1, (end-start)/1.0);
			showMsg(msg);
		}catch(Exception e){
			showErr(e);
		}
	}
	
	protected void display(String s){
		//TODO
	}
	
	protected void showMsg(String msg){
		//TODO
	}
}
