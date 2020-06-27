package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SearchScreen {
    //switch screen---------------
    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("Welcome");
    }

    @FXML
    VBox vbox = new VBox();

    @FXML
    AnchorPane anchorPane = new AnchorPane();

    public TextField SearchBar;

    final ArrayList<Label> labels = new ArrayList<>();

    @FXML
    public void SearchButtonReleased() {
        anchorPane.getChildren().clear();
        int y = 0;

        try {

            String toSearch = "%" + SearchBar.getText() + "%";
            //the search word is checked if it matches any keywords in database
            PreparedStatement stmnt = Database.con.prepareStatement("select * from Timeline where title like ? OR keywords like ?");
            stmnt.setString(1, toSearch);
            stmnt.setString(2, toSearch);
            ResultSet rs = stmnt.executeQuery();

            List<TimeLine> timelineList = new ArrayList<>();

            //gets the userID,TimelineID,Title,Rating and keywords from database
            //adds them in timelineList
           while (rs.next()) {
                TimeLine timeline = new TimeLine();
                timeline.setUserID(rs.getInt("userID"));
                timeline.setTimelineID(rs.getInt("TimelineID"));
                timeline.setTitle(rs.getString("title"));
                timeline.setKeywords(rs.getString("keywords"));
                timeline.setRating(rs.getInt("rating"));
                timelineList.add(timeline);
           }
            //prints out no results found in search
            // menu if no matches were made in database
            if (timelineList.isEmpty()) {
                Label label = new Label();
                label.setText("No results found");
                label.setLayoutX(150);
                label.setLayoutY(10 + y);
                label.setMinHeight(5);
                label.setMinWidth(5);
                label.setTextFill(Color.WHITE);
                label.setStyle("-fx-font-size: 32; ");
                anchorPane.getChildren().addAll(label);
                labels.add(label);
                throw new SQLException("no results found");
            }
            //if matches were found labels are created
            else {
                for (TimeLine timeline : timelineList) {

                    Label label = new Label();
                    label.setText(timeline.getTitle());
                    label.getStyleClass().add("labelSearch");//css style
                    label.setLayoutX(180);
                    label.setLayoutY(10 + y);
                    label.setMinHeight(5);
                    label.setMinWidth(5);
                    FadeTransition ft = new FadeTransition(Duration.millis(500), label);
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    ft.setCycleCount(0);
                    ft.play();

                    label.setOnMouseClicked(mouseEvent -> { //if a label is clicked it switches to the load timeline screen
                        try {
                            App.previousPage="Search";
                            LoadTimeLineScreen.timelineId=timeline.getTimelineID();
                            App.setRoot("LoadTL");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    //prints out the number of stars proportionate
                    // to the rating integer in the database
                    if (timeline.getRating() != 0) {
                        int x = 0;
                        for (int i = 0; i < timeline.getRating(); i++) {

                            Image image = new Image(getClass().getResource("/org/example/STAR_FOR_BOTI.png").toString());

                            // simply displays ImageView the image as is
                            ImageView imageView = new ImageView();
                            imageView.setImage(image);
                            imageView.setFitHeight(25);
                            imageView.setFitWidth(25);
                            imageView.setLayoutX(115-x);
                            imageView.setLayoutY(label.getLayoutY()+12);
                            FadeTransition ft2 = new FadeTransition(Duration.millis(500), imageView);
                            ft2.setFromValue(0);
                            ft2.setToValue(1.0);
                            ft2.setCycleCount(0);
                            ft2.play();

                            anchorPane.getChildren().add(imageView);
                            x+=25;
                        }
                    }

                    anchorPane.getChildren().addAll(label);

                    labels.add(label);

                    y += 50;  //the labels gets a new y value after every iteration
                }
            }

        }
        //mainly catches connection errors
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}