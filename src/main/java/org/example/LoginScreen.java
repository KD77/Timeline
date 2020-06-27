package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;

public class LoginScreen {

    public TextField emailField;
    public PasswordField passwordField;
    public Label lblMessage;


    public void signup() throws IOException {
        App.setRoot("SignUp");
}
    public void back() throws IOException {
        App.setRoot("Welcome");
    }


    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();


        // removes whitespaces in email field
        if (Character.isWhitespace(email.charAt(email.length() - 1))) {
            StringBuilder current = new StringBuilder();
            for (int i = 0; i < email.length() - 1; i++) {
                current.append(email.charAt(i));
            }
            email = current.toString();
        }

        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please fill in all info");
            lblMessage.setTextFill(Color.TOMATO);

        } else {
            // Tries to log in, if succeeds switch to ProfilePage else try again
            try{
                Database.userLogIn(email, password);
                lblMessage.setText("");
                App.setRoot("ProfilePage");
            } catch (SQLException | IOException e) {
                lblMessage.setText("Wrong username or password");
                lblMessage.setTextFill(Color.TOMATO);
                System.out.println(e.getMessage());
            }
        }
    }
}
