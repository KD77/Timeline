package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpScreen {

    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;
    public PasswordField passwordConfirmField;
    public Label lblMessage;


    public void signUp() {

        String pass1 = passwordField.getText();
        String pass2 = passwordConfirmField.getText();
        String email=emailField.getText();
        String username=usernameField.getText();


        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordConfirmField.getText().isEmpty()){
            lblMessage.setText("Please fill all the fields ");
            lblMessage.setTextFill(Color.TOMATO);
        }
        else if(!pass1.equals(pass2)){
            lblMessage.setText("Passwords must match ");
            lblMessage.setTextFill(Color.TOMATO);
        }

        else {
            try{
                if (validateEmail() && validatePassword() ){
                    String insert = "INSERT INTO users(username, email, password, admin ) VALUES(?,?,sha1(?),?)";

                    PreparedStatement preparedStatement = Database.con.prepareStatement(insert);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, pass1);
                    preparedStatement.setShort(4, (short) 0);
                    preparedStatement.executeUpdate();

                    lblMessage.setText("signed up successfully");
                    lblMessage.setTextFill(Color.GREEN);
                    App.setRoot("Login");


                }




            } catch (SQLException | IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }
    }
    // should be in a form of kd@gmail.com or kd@student.lnu.se
    // can contain uppercase,lowercase and numbers
    private boolean validateEmail(){
        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher matcher=p.matcher(emailField.getText());
        if (matcher.find() && matcher.group().equals(emailField.getText())){
            return true;
        }
        else{
            lblMessage.setText("Please enter a valid email format ");
            return false;
        }
    }

    //at least 8 digits to 15 {8,15}
    //at least one number (?=.*\\d)
    //at least one lowercase (?=.*[a-z])
    //at least one uppercase (?=.*[A-Z])
    private boolean validatePassword() {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15})");
        Matcher matcher = p.matcher(passwordField.getText());
        if (matcher.matches()) {
            return true;

        } else {
            lblMessage.setText("Please enter a valid password (at least one uppercase, lowercase and 8 or more characters ");
            return false;

        }
    }

    public void back() throws IOException {
        App.setRoot("Login");
    }

}
