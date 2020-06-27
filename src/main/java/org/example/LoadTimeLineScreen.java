package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class LoadTimeLineScreen {

    @FXML
    Label displayStars;
    @FXML
    Button star1;
    @FXML
    Button star2;
    @FXML
    Button star3;
    @FXML
    Button star4;
    @FXML
    Button star5;
    @FXML
    Image empty;
    @FXML
    ImageView image1;
    @FXML
    ImageView image2;
    @FXML
    ImageView image3;
    @FXML
    ImageView image4;
    @FXML
    ImageView image5;
    @FXML
    Image clicked;

    private int x;
    private boolean clickedCheck = false;
    @FXML
    private TextField username;
    @FXML
    private AnchorPane special;
    @FXML
    private PasswordField password;
    @FXML
    private BorderPane loginBox;
    @FXML
    private Label lblMessage;
    @FXML
    private Button cancel;

    @FXML
    private Button login;
    @FXML Label title;
    @FXML AnchorPane anchorPane;
    @FXML Button back;
    @FXML Button load;
    @FXML Slider slider;
    @FXML BorderPane loadBox;

    public static int timelineId=0;

    public void goBack() throws IOException {
            App.setRoot(App.previousPage);
    }

    @FXML
    public void loadTimeLine(){

        anchorPane.getChildren().remove(loadBox);

        try {
            TimeLine timeLine = Database.getTimeLine(timelineId);
            ArrayList<Events> events = Database.getEvents(timelineId);

            title.setText(timeLine.getTitle());
            int start = timeLine.getStart();
            slider.setMin(start);
            int end = timeLine.getEnd();
            slider.setMax(end);

            if ((end - start) < 1242) {
                slider.setPrefWidth(1248);
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);
                slider.setMajorTickUnit(10);
            } else {
                slider.setPrefWidth(end - start);
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);
                slider.setMajorTickUnit(100);
            }
            slider.setMinorTickCount(5);
            slider.setBlockIncrement(5);

            ArrayList<ImageView> imgs = new ArrayList<>();

            PreparedStatement stmt = Database.con.prepareStatement("SELECT * from nodepic WHERE TimelineID=" + timelineId + "");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int string = rs.getInt(2);
                ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) rs.getBinaryStream(3);
                BufferedImage bImage2 = ImageIO.read(byteArrayInputStream);
                File img = new File(string+".jpg");
                ImageIO.write(bImage2, "jpg", img);
                imgs.add(new ImageView(new Image(new FileInputStream(img))));

            }

            PreparedStatement statement = Database.con.prepareStatement("SELECT * from timelineinfo WHERE TimelineID=" + timelineId + "");
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Integer> hasImg = new ArrayList<>();

            while (resultSet.next()){
                int isTrue = resultSet.getInt(5);
                hasImg.add(isTrue);
            }


            int k = 0;
            int l = 0;
            int counter = 0;
            for (Events e:events){
                slider.setValue(e.getPoint());
                Bounds bounds = slider.lookup(".jfx-slider .thumb").getBoundsInParent();
                ImageView imageView = null;
                VBox eventVBox = new VBox();
                eventVBox.getStyleClass().add("paneBox");

                //line
                Rectangle rectangle = new Rectangle();
                rectangle.setStyle("fx-border-radius: 10px;\n" + " -fx-border-width: 2px;-fx-border-style: solid;-fx-display: inline-block;");
                rectangle.setWidth(5);
                rectangle.setHeight(70);
                rectangle.setX(bounds.getMaxX());
                rectangle.setFill(Color.web("#41ba84"));
                rectangle.setOpacity(0.5);
                rectangle.toBack();

                //Title
                Label label = new Label();
                label.setPrefWidth(150);
                label.setWrapText(true);
                label.setTextAlignment(TextAlignment.LEFT);
                label.setText(e.getNode());
                label.setStyle("-fx-background-color: #252525; -fx-text-fill: white; -fx-font-size: 15; -fx-alignment: top-center; -fx-underline: true");


                //img
                if(hasImg.get(l) == 1){
                    imageView = imgs.get(k);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    k++;
                }
                l++;

                //desc
                Label label1 = new Label();
                label1.setPrefWidth(150);
                label1.setTextAlignment(TextAlignment.LEFT);
                label1.setText(e.getDescription());
                label1.setStyle("-fx-background-color: #252525; -fx-text-fill: white;");


                label.setWrapText(false);
                label1.setWrapText(false);



                //Readjusts labels x value if width exceeds border
                if (e.getPoint() < 50) {
                    label.setLayoutX(10);
                    label1.setLayoutX(10);
                    eventVBox.setLayoutX(bounds.getMaxX());

                } else {
                    label.setLayoutX(10);
                    eventVBox.setLayoutX(bounds.getMaxX());
                    label1.setLayoutX(10);
                }



                AtomicReference<Boolean> isClicked = new AtomicReference<>(false);

                eventVBox.setOnMouseClicked(event1->{
                    if(!isClicked.get()){   //expands
                        label.setWrapText(true);
                        label1.setWrapText(true);
                        isClicked.set(true);
                        eventVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        rectangle.toBack();

                    }else {
                        label1.setWrapText(false);
                        eventVBox.setPrefHeight(50);
                        isClicked.set(false);
                    }
                });

                //label1 is overlapping nodename if one ch is entered
                //Alternates between upper and lower sides of the timeline
                if(imageView == null) {
                    if (counter % 2 == 0) {//down
                        eventVBox.setLayoutY(slider.getLayoutY()+rectangle.getHeight());
                        rectangle.setY(slider.getLayoutY()+5);
                    } else {//up
                        eventVBox.setLayoutY(slider.getLayoutY()-(rectangle.getHeight())-45);
                        rectangle.setY(266);
                    }
                    label.setLayoutY(10);
                    label1.setLayoutY(32);
                    eventVBox.getChildren().addAll(label, label1);
                }else {
                    if (counter % 2 == 0) {//down
                        eventVBox.setLayoutY(slider.getLayoutY()+rectangle.getHeight());
                        rectangle.setY(slider.getLayoutY()+5);
                    } else {//up
                        eventVBox.setLayoutY(slider.getLayoutY()-(rectangle.getHeight())-190);
                        rectangle.setY(266);
                    }
                    label.setLayoutY(10);
                    label1.setLayoutY(32);
                    eventVBox.getChildren().addAll(label, imageView, label1);
                }
                anchorPane.getChildren().addAll(eventVBox,rectangle);
                counter++;
            }
            //********************
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void Star1Action() {
        image1.setImage(clicked);
        image2.setImage(empty);
        image3.setImage(empty);
        image4.setImage(empty);
        image5.setImage(empty);
        clickedCheck = true;
        star1.setDisable(true);
        star2.setDisable(true);
        star3.setDisable(true);
        star4.setDisable(true);
        star5.setDisable(true);
        for (int i = 0; i < 1; i++) {
            ImageView imageView = new ImageView();
            imageView.preserveRatioProperty();
            imageView.setFitHeight(30.0);
            imageView.setFitWidth(30.0);
            imageView.setLayoutX(displayStars.getLayoutX() + 7.93 + x);
            imageView.setLayoutY(displayStars.getLayoutY() + 3.91);
            imageView.setBlendMode(BlendMode.SRC_ATOP);
            imageView.setImage(clicked);
            special.getChildren().add(imageView);
            x += 30;
        }
        try {
            if(App.loggedInUser.getUserID()!=-1){
                Database.giveRating(1,App.loggedInUser.getUserID(),timelineId);
            }else{
                loginBox.setDisable(false);
                loginBox.setOpacity(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void Star2Action() {
        image1.setImage(clicked);
        image2.setImage(clicked);
        image3.setImage(empty);
        image4.setImage(empty);
        image5.setImage(empty);
        clickedCheck = true;
        star1.setDisable(true);
        star2.setDisable(true);
        star3.setDisable(true);
        star4.setDisable(true);
        star5.setDisable(true);
        for (int i = 0; i < 2; i++) {
            ImageView imageView = new ImageView();
            imageView.preserveRatioProperty();
            imageView.setFitHeight(30.0);
            imageView.setFitWidth(30.0);
            imageView.setLayoutX(displayStars.getLayoutX() + 7.93 + x);
            imageView.setLayoutY(displayStars.getLayoutY() + 3.91);
            imageView.setSmooth(true);
            imageView.setImage(clicked);
            imageView.setCache(true);
            special.getChildren().add(imageView);
            x += 30;
        }
        try {
            if(App.loggedInUser.getUserID()!=-1){
                Database.giveRating(2,App.loggedInUser.getUserID(),timelineId);
            }else{
                loginBox.setDisable(false);
                loginBox.setOpacity(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void Star3Action() {
        image1.setImage(clicked);
        image2.setImage(clicked);
        image3.setImage(clicked);
        image4.setImage(empty);
        image5.setImage(empty);
        clickedCheck = true;
        star1.setDisable(true);
        star2.setDisable(true);
        star3.setDisable(true);
        star4.setDisable(true);
        star5.setDisable(true);
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView();
            imageView.preserveRatioProperty();
            imageView.setFitHeight(30.0);
            imageView.setFitWidth(30.0);
            imageView.setLayoutX(displayStars.getLayoutX() + 7.93 + x);
            imageView.setLayoutY(displayStars.getLayoutY() + 3.91);
            imageView.setSmooth(true);
            imageView.setImage(clicked);
            imageView.setCache(true);
            special.getChildren().add(imageView);
            x += 30;
        }

        try {
            if(App.loggedInUser.getUserID()!=-1){
                Database.giveRating(3,App.loggedInUser.getUserID(),timelineId);
            }else{
                loginBox.setDisable(false);
                loginBox.setOpacity(1);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void Star4Action() {
        image1.setImage(clicked);
        image2.setImage(clicked);
        image3.setImage(clicked);
        image4.setImage(clicked);
        image5.setImage(empty);
        clickedCheck = true;
        star1.setDisable(true);
        star2.setDisable(true);
        star3.setDisable(true);
        star4.setDisable(true);
        star5.setDisable(true);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView();
            imageView.preserveRatioProperty();
            imageView.setFitHeight(30.0);
            imageView.setFitWidth(30.0);
            imageView.setLayoutX(displayStars.getLayoutX() + 7.93 + x);
            imageView.setLayoutY(displayStars.getLayoutY() + 3.91);
            imageView.setSmooth(true);
            imageView.setImage(clicked);
            imageView.setCache(true);
            special.getChildren().add(imageView);
            x += 30;
        }
        try {
            if(App.loggedInUser.getUserID()!=-1){
                Database.giveRating(4,App.loggedInUser.getUserID(),timelineId);
            }else{
                loginBox.setDisable(false);
                loginBox.setOpacity(1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void Star5Action() {
        image1.setImage(clicked);
        image2.setImage(clicked);
        image3.setImage(clicked);
        image4.setImage(clicked);
        image5.setImage(clicked);
        clickedCheck = true;
        star1.setDisable(true);
        star2.setDisable(true);
        star3.setDisable(true);
        star4.setDisable(true);
        star5.setDisable(true);
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView();
            imageView.preserveRatioProperty();
            imageView.setFitHeight(30.0);
            imageView.setFitWidth(30.0);
            imageView.setLayoutX(displayStars.getLayoutX() + 7.93 + x);
            imageView.setLayoutY(displayStars.getLayoutY() + 3.91);
            imageView.setSmooth(true);
            imageView.setImage(clicked);
            imageView.setCache(true);
            special.getChildren().add(imageView);
            x += 30;
        }
        try {
            if(App.loggedInUser.getUserID()!=-1){
                Database.giveRating(5,App.loggedInUser.getUserID(),timelineId);
            }else{
                loginBox.setDisable(false);
                loginBox.setOpacity(1);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void hoverOverStar1() {
        if (!(clickedCheck)) {
            star1.hoverProperty().addListener(l -> image1.setImage(empty));
        }
        star1.setOnMouseMoved(m -> image1.setImage(clicked));
    }

    @FXML
    public void hoverOverStar2() {
        if (!(clickedCheck)) {
            star2.hoverProperty().addListener(l -> {
                image1.setImage(empty);
                image2.setImage(empty);
            });
        }
        star2.setOnMouseMoved(m -> {
            image1.setImage(clicked);
            image2.setImage(clicked);
        });
    }

    @FXML
    public void hoverOverStar3() {
        if (!(clickedCheck)) {
            star3.hoverProperty().addListener(l -> {
                image1.setImage(empty);
                image2.setImage(empty);
                image3.setImage(empty);
            });
        }
        star3.setOnMouseMoved(m -> {
            image1.setImage(clicked);
            image2.setImage(clicked);
            image3.setImage(clicked);
        });
    }

    @FXML
    public void hoverOverStar4() {
        if (!(clickedCheck)) {
            star4.hoverProperty().addListener(l -> {
                image1.setImage(empty);
                image2.setImage(empty);
                image3.setImage(empty);
                image4.setImage(empty);
            });
        }
        star4.setOnMouseMoved(m -> {
            image1.setImage(clicked);
            image2.setImage(clicked);
            image3.setImage(clicked);
            image4.setImage(clicked);
        });
    }

    @FXML
    public void hoverOverStar5() {
        if (!(clickedCheck)) {
            star5.hoverProperty().addListener(l -> {
                image1.setImage(empty);
                image2.setImage(empty);
                image3.setImage(empty);
                image4.setImage(empty);
                image5.setImage(empty);
            });
        }
        star5.setOnMouseMoved(m -> {
            image1.setImage(clicked);
            image2.setImage(clicked);
            image3.setImage(clicked);
            image4.setImage(clicked);
            image5.setImage(clicked);
        });
    }
    @FXML
    private void login() {
        String email = username.getText();
        String passwordText = password.getText();

        if (email.isEmpty() || passwordText.isEmpty()) {
            lblMessage.setText("Please fill in all info");
            lblMessage.setTextFill(Color.TOMATO);


        } else {
            // Tries to log in, if succeeds switch to ProfilePage else try again
            try {
                Database.userLogIn(email, passwordText);
                loginBox.setDisable(true);
                loginBox.setOpacity(0);

            } catch (SQLException e) {
                lblMessage.setText("Wrong username or password");
                lblMessage.setTextFill(Color.TOMATO);
                System.out.println(e.getMessage());

            }

        }


    }
    @FXML
    private void cancel() {
        loginBox.setDisable(true);
        loginBox.setOpacity(0);

    }
}
