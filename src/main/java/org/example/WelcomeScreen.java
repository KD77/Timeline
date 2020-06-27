package org.example;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class WelcomeScreen {


	public ImageView LoginImage, ViewTimelineButton, CreateButton;

	public void initialize() throws FileNotFoundException {
		if(!App.loggedInUser.loggedIn) {
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/LoginButtonReleased.png")));
		}else{
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/profileButton.png")));
		}
	}


	public void loginButtonPressed() throws IOException {
		if(!App.loggedInUser.loggedIn)
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/LoginButtonPressed.png")));
		else
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/profileButton.png")));
	}
	
	public void loginButtonReleased() throws IOException {
		if(!App.loggedInUser.loggedIn) {
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/LoginButtonReleased.png")));
			App.setRoot("Login");
		}else{
			LoginImage.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/profileButton.png")));
			App.setRoot("ProfilePage");
		}

	}
	
	public void viewButtonPressed() {
		try{
			ViewTimelineButton.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/ViewButtonPressed.png")));

		}catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}

	}
	
	public void viewButtonReleased() throws IOException {
		try{
			ViewTimelineButton.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/ViewButtonReleased.png")));
		}catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		App.setRoot("Search");
	}
	
	public void createButtonPressed() {

		try{
			CreateButton.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/CreatePressed.png")));
		}catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}

	}

	@FXML
	private void createButtonReleased() throws IOException {
		try{
			CreateButton.setImage(new Image(new FileInputStream("src/main/resources/org/example/Buttons/CreateReleased.png")));
		}catch (FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		App.setRoot("create");

	}


}

