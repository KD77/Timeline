package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileScreen {

    @FXML private Label helloLabel;
    @FXML private ScrollPane adminSetting;
    @FXML private AnchorPane leftScrollPane;
    @FXML private AnchorPane bigAnchor;
    @FXML private AnchorPane rightScrollPane;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Label errorLabel;


    @FXML public void changeUserName() throws SQLException {
        String user=username.getText();
        username.clear();
        int userId=App.loggedInUser.getUserID();
        PreparedStatement statement = Database.con.prepareStatement("update users set username = '"+user+"' where userID= "+userId);
        statement.executeUpdate();
    }
    //at least 8 digits to 15 {8,15}
    //at least one number (?=.*\\d)
    //at least one lowercase (?=.*[a-z])
    //at least one uppercase (?=.*[A-Z])
    private boolean validatePassword(PasswordField password) {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,15})");
        Matcher matcher = p.matcher(password.getText());
        if (matcher.matches()) {
            errorLabel.setText("");
            return true;

        } else {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Please enter a valid password (at least one uppercase, lowercase and 8 or more characters ");
            return false;

        }
    }
    @FXML public void changePass() throws SQLException {
        if (password.getText().equals(confirmPassword.getText())&&validatePassword(confirmPassword)) {
            String pass = password.getText();

            password.clear();
            confirmPassword.clear();
            int userId=App.loggedInUser.getUserID();
            PreparedStatement statement = Database.con.prepareStatement("update users set password = sha1('"+pass+"') where userID= "+userId);
            statement.executeUpdate();
            errorLabel.setTextFill(Color.GREEN);
            errorLabel.setText("Password changed");

        }else if (!password.getText().equals(confirmPassword.getText())){
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Passwords do not match");
        }
    }

    public void initialize() {
        showUsers();
        showTimelines();

        helloLabel.setText("Hello " + App.loggedInUser.getUsername());

        if(App.loggedInUser.isAdmin() == 0){
            bigAnchor.getChildren().remove(adminSetting);
        }
    }



    public void showUsers() {
        try {
            PreparedStatement stmnt = Database.con.prepareStatement("select userID, username, admin from users");
            ArrayList<User> users = new ArrayList<>();

            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("userID"));
                user.setUsername(rs.getString("Username"));
                user.setAdmin(rs.getInt("Admin"));
                users.add(user);
            }

            int y = 70;

            for (User user : users) {

                if(y > leftScrollPane.getPrefHeight()) {
                    leftScrollPane.setPrefHeight(leftScrollPane.getPrefHeight() + 75);
                }

                Label label = new Label();
                label.setText(user.getUsername());
                label.setStyle("-fx-text-fill: #41ba84");
                label.setLayoutX(leftScrollPane.getPrefWidth() - (leftScrollPane.getPrefWidth()*0.9));
                label.setLayoutY(y);


                CheckBox checkBox = new CheckBox();
                checkBox.setLayoutX(leftScrollPane.getPrefWidth() - (leftScrollPane.getPrefWidth()*0.2));
                checkBox.setLayoutY(y);

                y += 25;

                checkBox.setSelected(user.isAdmin() == 1);

                checkBox.setOnAction(e -> {
                    try {
                        if (e.getSource() instanceof CheckBox) {
                            System.out.println("Action performed on checkbox " + checkBox.getText());
                            if (user.isAdmin() == 0) {
                                checkBox.setSelected(!checkBox.isSelected());
                                checkBox.setSelected(true);
                                PreparedStatement makeAdmin = Database.con.prepareStatement("update users set admin=1 where userID=" + user.getUserID());
                                makeAdmin.executeUpdate();

                            } else if (user.isAdmin() != 0) {
                                checkBox.setSelected(!checkBox.isSelected());
                                PreparedStatement makeAdmin = Database.con.prepareStatement("update users set admin=0 where userID=" + user.getUserID());
                                makeAdmin.executeUpdate();
                                checkBox.setSelected(false);
                            }
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                });

                leftScrollPane.getChildren().addAll(label, checkBox);
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }

        showTimelines();
    }

    public void showTimelines() {
        try {
            PreparedStatement stmnt = Database.con.prepareStatement("select userID, title, timelineID from timeline");
            ArrayList<TimeLine> timeLines = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                if(rs.getInt("userID") == App.loggedInUser.getUserID() || App.loggedInUser.isAdmin() == 1){
                    TimeLine tempTimeLine = new TimeLine();
                    tempTimeLine.setTimelineID(rs.getInt("timelineID"));
                    tempTimeLine.setTitle(rs.getString("title"));
                    tempTimeLine.setUserID(rs.getInt("userID"));
                    timeLines.add(tempTimeLine);
                }
            }

            int y = 70;

            for (TimeLine timeLine : timeLines) {

                if(y > rightScrollPane.getPrefHeight()) {
                    rightScrollPane.setPrefHeight(rightScrollPane.getPrefHeight() + 120);
                }
                Label label = new Label();
                label.setText(timeLine.getTitle());
                label.setStyle("-fx-text-fill: #ffffff");
                label.setLayoutX(rightScrollPane.getPrefWidth() - rightScrollPane.getPrefWidth()*0.9);
                label.setLayoutY(y);

                Button button = new Button();
                button.setText("Delete");
                button.getStyleClass().add("buttons");
                button.setStyle("-fx-text-fill: #ffffff");
                button.setLayoutX(rightScrollPane.getPrefWidth() - rightScrollPane.getPrefWidth()*0.2);
                button.setLayoutY(y);

                Button view = new Button();
                view.setText("View");
                view.getStyleClass().add("buttons");
                view.setLayoutX((rightScrollPane.getPrefWidth() - rightScrollPane.getPrefWidth()*0.2)-60);
                view.setLayoutY(y);

                button.setOnAction(actionEvent -> {
                    try {
                        Database.deleteTimeLine(Database.getTimeLineID(timeLine.getTitle()));
                        rightScrollPane.getChildren().removeAll(label, button);
                    } catch (SQLException throwables) {
                        System.out.println("Unable to delete timeline");
                    }
                });
                view.setOnAction(actionEvent ->{
                        try {
                            App.previousPage="ProfilePage";
                            LoadTimeLineScreen.timelineId = timeLine.getTimelineID();
                            App.setRoot("LoadTL");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });
                y += 50;

                rightScrollPane.getChildren().addAll(label, button, view);
            }

        } catch (SQLException ignored) {

        }

    }

    public void back() throws IOException {
        App.setRoot("Welcome");
    }

    public void logOut() throws IOException {
        App.loggedInUser = new User();
        App.setRoot("Welcome");
    }
}
