package org.example;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class CreateScreen {
    //Switching screens
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Welcome");
    }
    //--------------------------
    public Events event1;
    final ArrayList<Events> events = new ArrayList<>();
    final ArrayList<Integer> hasImage = new ArrayList<>();
    private final ArrayList<String> files = new ArrayList<>();
    public int counter = 0;
    private boolean hasBeenPressed = false;
    private int latestNode;


    @FXML private TextField enternode;
    @FXML private TextField desc;
    @FXML private TextField title;
    @FXML private TextField keyWords;
    @FXML private Slider slider;
    @FXML private TextField startRange;
    @FXML private TextField endRange;
    @FXML private Button confirm;
    @FXML private TextField pointValue;
    @FXML private Button confirmPoint;
    @FXML private Label value;
    @FXML private Label lblMessage;
    @FXML AnchorPane anchorRight;
    @FXML AnchorPane anchorPane;
    @FXML private BorderPane loginBox;
    @FXML private TextField username;
    @FXML private PasswordField password;

    int nodeDif = 0;
    int eventCount = 0;
    private String filePath;

    final LinkedList<TextField> nodesvtwo = new LinkedList<>();
    final LinkedList<TextField> descriptionsvtwo = new LinkedList<>();
    final LinkedList<Button> deleteButtonsvtwo = new LinkedList<>();
    final LinkedList<Rectangle> deleteRect = new LinkedList<>();

    @FXML void numCap(){//to check if only number is entered
        String startRangeText=startRange.getText();
        startRange.clear();
        // noinspection Annotator
        startRange.appendText(startRangeText.replaceAll("[^-(\\d{1,100})|(\\d{1,100})]",""));
        String endRangeText=endRange.getText();
        endRange.clear();
        // noinspection Annotator
        endRange.appendText(endRangeText.replaceAll("[^-(\\d{1,100})|(\\d{1,100})]",""));
        String point=pointValue.getText();
        pointValue.clear();
        // noinspection Annotator
        pointValue.appendText(point.replaceAll("[^-(\\d{1,100})|(\\d{1,100})]",""));

    }
    @FXML
    private void textCap(){//to limit text size in node and description
        int limit = 20;
        int length = enternode.getText().length();
        String text;
        if(length > 0)
            text = enternode.getText(0,length-1);
        else
            text = "";

        if(length > limit){
            enternode.clear();
            enternode.appendText(text);
        }

        int limitDesc = 100;
        int lengthDesc = desc.getText().length();
        String textDesc;
        if(lengthDesc > 0)
            textDesc = desc.getText(0,lengthDesc-1);
        else
            textDesc = "";

        if(lengthDesc > limitDesc){
            desc.clear();
            desc.appendText(textDesc);
        }
    }

    @FXML
    private void update() {

        Label sliderValue = new Label();
        slider.valueProperty().addListener((observableValue, number, t1) -> {
            sliderValue.setText(String.valueOf((int) slider.getValue()));
            Bounds bounds = slider.lookup(".jfx-slider .thumb").getBoundsInParent();
            value.setText(String.valueOf((int)slider.getValue()));
            value.setLayoutY(slider.getLayoutY()-25);
            value.setLayoutX(bounds.getMaxX()-(value.getPrefWidth()/2)+5);
            FadeTransition ft = new FadeTransition(Duration.millis(500), value);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setCycleCount(0);
            ft.play();
        });

        int start = Integer.parseInt(startRange.getText());
        slider.setMin(start);
        int end = Integer.parseInt(endRange.getText());
        slider.setMax(end);


        if ((end - start) <= 100) {
            slider.setPrefWidth(999);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(false);
            slider.setMajorTickUnit(1);
        } else if ((end - start) > 100 && (end - start) <= 500) {
            slider.setPrefWidth(999);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(false);
            slider.setMajorTickUnit(25);
        } else if ((end - start) > 500 && (end - start) <= 1000) {
            slider.setPrefWidth(999);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(false);
            slider.setMajorTickUnit(50);
        } else {
            slider.setPrefWidth(end - start);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(false);
            slider.setMajorTickUnit(100);
        }
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(5);

        startRange.visibleProperty().set(false);
        endRange.visibleProperty().set(false);
        confirm.visibleProperty().set(false);
        pointValue.setLayoutY(32);
        confirmPoint.setLayoutY(32);

        slider.setDisable(false);
    }

    @FXML
    void value() {
        int value = Integer.parseInt(pointValue.getText());
        slider.setValue(value);
        pointValue.clear();
    }

    @FXML
    private void plusBt() {
        //right side generating boxes
        //Unable to add event if event title textfield is empty
        if (!enternode.getText().equals("")) {
            Bounds bounds = slider.lookup(".jfx-slider .thumb").getBoundsInParent();
            VBox eventVBox = new VBox();
            eventVBox.getStyleClass().add("paneBox");

            //line from slider to eventbox
            Rectangle rectangle = new Rectangle();
            rectangle.setStyle("fx-border-radius: 10px;\n" + " -fx-border-width: 2px;-fx-border-style: solid;-fx-display: inline-block;");
            rectangle.setWidth(5);
            rectangle.setHeight(70);
            rectangle.setX(bounds.getMaxX() + (rectangle.getWidth() / 2));
            rectangle.setFill(Color.web("#41ba84"));
            rectangle.setOpacity(0.5);
            rectangle.toBack();
            deleteRect.add(rectangle);
            //node properties
            Label label = new Label();
            label.setPrefWidth(150);
            label.setWrapText(true);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setText(enternode.getText());
            label.setStyle("-fx-background-color: #252525; -fx-text-fill: white; -fx-font-size: 15; -fx-alignment: top-center; -fx-underline: true");
            label.setWrapText(false);
            Bounds bounds1 = label.lookup(".label").getBoundsInParent();
            //description properties
            Label label1 = new Label();
            label1.setPrefWidth(150);
            label1.setTextAlignment(TextAlignment.LEFT);
            label1.setText(desc.getText());
            label1.setWrapText(false);
            label1.setStyle("-fx-background-color: #252525; -fx-text-fill: white;");
            //adding image to event
            Button setTrueImage = new Button();
            setTrueImage.setStyle(" -fx-border-radius: 10;\n" + " -fx-border-width: 2px;\n" + " -fx-border-color: #41ba84;\n" + " -fx-border-style: solid;\n" + " -fx-background-color: #494949;\n" + " -fx-background-radius: 10;\n" + " -fx-display: inline-block;\n" + " -fx-text-fill: #41ba84;");
            setTrueImage.setOnMouseEntered(e -> setTrueImage.setStyle("-fx-text-fill: #ffffff;\n" + " -fx-background-color: #41ba84;"));
            setTrueImage.setOnMouseExited(e -> setTrueImage.setStyle("-fx-border-radius: 10;\n" + " -fx-border-width: 2px;\n" + " -fx-border-color: #41ba84;\n" + " -fx-border-style: solid;\n" + " -fx-background-color: #494949;\n" + " -fx-background-radius: 10;\n" + " -fx-display: inline-block;\n" + " -fx-text-fill: #41ba84;"));
            setTrueImage.setOnMouseClicked(e -> setTrueImage.setStyle("-fx-background-color: #2b7a57;"));
            setTrueImage.setText("Save image");
            setTrueImage.setLayoutX(bounds1.getMaxX());

            Button addImage = new Button();
            addImage.setStyle(" -fx-border-radius: 10;\n" + " -fx-border-width: 2px;\n" + " -fx-border-color: #41ba84;\n" + " -fx-border-style: solid;\n" + " -fx-background-color: #252525;\n" + " -fx-background-radius: 10;\n" + " -fx-display: inline-block;\n" + " -fx-text-fill: #41ba84;");
            addImage.setOnMouseEntered(e -> addImage.setStyle("-fx-text-fill: #ffffff;\n" + " -fx-background-color: #41ba84;"));
            addImage.setOnMouseExited(e -> addImage.setStyle("-fx-border-radius: 10;\n" + " -fx-border-width: 2px;\n" + " -fx-border-color: #41ba84;\n" + " -fx-border-style: solid;\n" + " -fx-background-color: #494949;\n" + " -fx-background-radius: 10;\n" + " -fx-display: inline-block;\n" + " -fx-text-fill: #41ba84;"));
            addImage.setOnMouseClicked(e -> addImage.setStyle("-fx-background-color: #2b7a57;"));
            addImage.setText("Add image");
            addImage.setLayoutX(bounds1.getMaxX());
            //getting image from computer
            addImage.setOnMouseClicked(e -> {
                JButton open = new JButton();
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setDialogTitle("Choose a file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                }
                ImageView imageView = new ImageView();
                //Resize image
                imageView.setFitWidth(150);
                imageView.setFitHeight(200);
                //Set X- and Y- layout for picture corresponding to node-events
                imageView.setX(label.getLayoutX());
                imageView.setY(label1.getLayoutY()+30);
                try {
                    imageView.setImage(new Image(new FileInputStream(filePath)));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                try {
                    BufferedImage bImage = ImageIO.read(new File(filePath));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (filePath.endsWith(".jpg")) {
                        ImageIO.write(bImage, "jpg", bos);
                    } else if (filePath.endsWith(".png")) {
                        ImageIO.write(bImage, "png", bos);
                    } else if (filePath.endsWith("gif")) {
                        ImageIO.write(bImage, "gif", bos);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                eventVBox.getChildren().add(1,imageView);
                eventVBox.getChildren().remove(addImage);
            });
            //-----------------------------
            //Readjusts labels if width exceeds border
            if (bounds.getMaxX() < 50) {
                label.setLayoutX(10);
                label1.setLayoutX(10);
                addImage.setLayoutX(bounds1.getMaxX());
                eventVBox.setLayoutX(bounds.getMaxX());
            } else {
                label.setLayoutX(10);
                eventVBox.setLayoutX(bounds.getMaxX()-75);
                label1.setLayoutX(10);
                addImage.setLayoutX(10);
            }
            //-------------------------------
            //Alternates between upper and lower sides of the timeline
            if (events.size() % 2 == 0) {//down
                eventVBox.setLayoutY(slider.getLayoutY()+rectangle.getHeight());
                rectangle.setY(slider.getLayoutY()+5);
                label.setLayoutY(10);
                label1.setLayoutY(32);
                addImage.setLayoutY(label.getLayoutY()  + 60);
            } else {//up
                eventVBox.setLayoutY(slider.getLayoutY()-(rectangle.getHeight()+40));
                rectangle.setY(266);
                label.setLayoutY(10);
                label1.setLayoutY(32);
                addImage.setLayoutY(label.getLayoutY() - 60);
            }
            //-----------------------------
            //extending event boxes
            AtomicReference<Boolean> isClicked = new AtomicReference<>(false);
            eventVBox.setOnMouseClicked(event1->{
                if(!isClicked.get()){   //expands
                    label.setWrapText(true);
                    label1.setWrapText(true);
                    isClicked.set(true);
                    eventVBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    addImage.setLayoutY(eventVBox.getPrefHeight()-10);
                    eventVBox.getChildren().addAll(addImage,setTrueImage);
                    rectangle.toBack();

                }else {
                    label1.setWrapText(false);
                    eventVBox.setPrefHeight(50);
                    eventVBox.getChildren().removeAll(addImage,setTrueImage);
                    isClicked.set(false);
                }
            });
            //------------------------------------

            eventVBox.getChildren().addAll(label,label1);
            anchorRight.getChildren().addAll(eventVBox,rectangle);
            eventCount++;

            //-------------------------------------
            //left side generating event boxes
            //---------------------------------------------------------------
            TextField node = new TextField(enternode.getText());
            TextField descr = new TextField(desc.getText());
            //setting text limit when editing events
            node.setOnKeyTyped(typed -> {
                int limit = 20;
                int length = node.getText().length();
                String text;
                if(length > 0)
                    text = node.getText(0,length-1);
                else
                    text = "";

                if(length > limit){
                    node.clear();
                    node.appendText(text);
                }
            });
            descr.setOnKeyTyped(typed -> {
                int limit = 100;
                int length = descr.getText().length();
                String text;
                if(length > 0)
                    text = descr.getText(0,length-1);
                else
                    text = "";

                if(length > limit){
                    descr.clear();
                    descr.appendText(text);
                }
            });
            //-----------------------------------

            //handling enter button in textfield--
            node.setOnAction(event1 ->{
                events.get(nodesvtwo.indexOf(node)).setNode(label.getText());
                label.setText(node.getText());
            });

            descr.setOnAction(event1 ->{
                events.get(descriptionsvtwo.indexOf(descr)).setDescription(descr.getText());
                label1.setText(descr.getText());
            });
            //--------------------
            //node values
            node.setLayoutX(enternode.getLayoutX());
            node.setLayoutY(enternode.getLayoutY() + 199 + nodeDif);
            node.setMinHeight(26);
            node.setMinWidth(183);
            node.setStyle("-fx-border-radius: 4; " + "-fx-background-color: #252525;" + "-fx-border-style: none none solid none;" + "-fx-border-color: #41ba84;" + "-fx-text-fill: white;");
            //desc values
            descr.setLayoutX(desc.getLayoutX());
            descr.setLayoutY(node.getLayoutY() + node.getMinHeight() + 5);
            descr.setMinHeight(75);
            descr.setMinWidth(183);
            descr.setAlignment(Pos.TOP_LEFT);
            descr.setStyle("-fx-border-radius: 4;" + "-fx-background-color: #252525;" + "-fx-border-style: none none solid none;" + "-fx-border-color: #41ba84;" + "-fx-text-fill: white;");
            //--------------------
            //delete event button
            Button deleteBtn = new Button();
            deleteButtonsvtwo.add(deleteBtn);
            deleteBtn.setLayoutX(151);
            deleteBtn.setLayoutY(325 + nodeDif);
            deleteBtn.setStyle("-fx-background-color: #252525;");
            Image minusButton = null;
            try {
                minusButton = new Image(new FileInputStream("src\\main\\resources\\org\\example\\minus.png"));
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            ImageView minus = new ImageView(minusButton);
            minus.setPreserveRatio(true);
            minus.setPickOnBounds(true);
            minus.setFitHeight(42);
            minus.setFitWidth(45);
            deleteBtn.setGraphic(minus);
            //----------------------

            nodesvtwo.add(node);
            descriptionsvtwo.add(descr);
            anchorPane.getChildren().addAll(node, descr, deleteBtn);

            //add events
            event1 = new Events(enternode.getText(), desc.getText(),latestNode);
            event1.setPoint(slider.getValue());
            events.add(event1);

            deleteBtn.setOnAction(ev -> {
                //move boxes up if the box in middle is deleted
                for (int i = nodesvtwo.indexOf(node) + 1; i < counter; i++) {
                    nodesvtwo.get(i).setLayoutY(nodesvtwo.get(i).getLayoutY() - 190);
                    descriptionsvtwo.get(i).setLayoutY(descriptionsvtwo.get(i).getLayoutY() - 190);
                    deleteButtonsvtwo.get(i).setLayoutY(deleteButtonsvtwo.get(i).getLayoutY() - 190);
                }

                nodeDif -= 190;
                //remove deleted nodes and event from linked list and anchorpane
                anchorPane.getChildren().removeAll(nodesvtwo.get(nodesvtwo.indexOf(node)), descriptionsvtwo.get(descriptionsvtwo.indexOf(descr)),
                        deleteButtonsvtwo.get(deleteButtonsvtwo.indexOf(deleteBtn)));
                anchorRight.getChildren().remove(deleteRect.get(deleteRect.indexOf(rectangle)));
                nodesvtwo.remove(node);
                descriptionsvtwo.remove(descr);
                deleteButtonsvtwo.remove(deleteBtn);
                deleteRect.remove(rectangle);
                counter--;
                events.remove(event1);
                anchorRight.getChildren().remove(eventVBox);
            });
            counter += 1;
            //------------------------------------
            enternode.clear();
            desc.clear();
            nodeDif = nodeDif + 190;
            //adding image button
            addImage.setOnMouseClicked(e -> {

                hasBeenPressed = false;
                JButton open = new JButton();
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setDialogTitle("Choose a file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    files.add(filePath);
                }

                ImageView imageView = new ImageView();
                //Resize image
                imageView.setFitWidth(150);
                imageView.setFitHeight(200);
                //Set X- and Y- layout for picture corresponding to node-events
                imageView.setX(label.getLayoutX());
                imageView.setY(label1.getLayoutY() + 30);
                try {
                    imageView.setImage(new Image(new FileInputStream(filePath)));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                try {
                    BufferedImage bImage = ImageIO.read(new File(filePath));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (filePath.endsWith(".jpg")) {
                        ImageIO.write(bImage, "jpg", bos);
                    } else if (filePath.endsWith(".png")) {
                        ImageIO.write(bImage, "png", bos);
                    } else if (filePath.endsWith("gif")) {
                        ImageIO.write(bImage, "gif", bos);
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                eventVBox.getChildren().add(1, imageView);
                eventVBox.getChildren().remove(addImage);


            });
            //to check if image exist in event
            setTrueImage.setOnMouseClicked(ev -> {
                hasBeenPressed = true;
                eventVBox.getChildren().remove(setTrueImage);
                if (eventCount == 1) {
                    event1.setTrueOrFalse(1);
                }
                eventVBox.getChildren().remove(setTrueImage);
            });
        }
        assignTrueFalse();
    }

    private void assignTrueFalse() {

        if (hasBeenPressed) {
            hasImage.add(1);
        } else {
            hasImage.add(0);
        }

        hasBeenPressed = false;

        for (int i = 0; i < hasImage.size(); i++) {
            latestNode = hasImage.get(hasImage.size() - 1);
        }
    }

    @FXML
    private void cancel() {
        loginBox.setDisable(true);
        loginBox.setOpacity(0);
    }

    @FXML
    private void login() {
        String email = username.getText();
        String passwordText = password.getText();
        if (email.isEmpty() || passwordText.isEmpty()) {
            lblMessage.setText("Please fill in all info");
            lblMessage.setTextFill(Color.TOMATO);
        } else {
            try {

                Database.userLogIn(email,passwordText);
                loginBox.isDisable();
                loginBox.setOpacity(0);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    @FXML
    private void saveTL() {

        if (App.loggedInUser.getUserID() != -1) {
            TimeLine tl1 = new TimeLine();
            try {
                tl1.setStart(Integer.parseInt(startRange.getText()));
            } catch (NumberFormatException e) {
                tl1.setStart(0);
            }
            try {
                tl1.setEnd(Integer.parseInt(endRange.getText()));
            } catch (NumberFormatException e) {
                tl1.setEnd(0);
            }
            try {
                tl1.setTitle(title.getText());
            } catch (NumberFormatException e) {
                tl1.setTitle("No title");
            } // Set TimeLine title
            tl1.setUserID(App.loggedInUser.getUserID());
            tl1.setKeywords(keyWords.getText());
            Database.crateTimeLine(tl1);
            int tlID = Database.getTimeLineID(tl1.getTitle()); // get timeline id
            Database.saveEvents(events, tlID);
            Database.saveImagesDos(tlID,files);
        } else {
            loginBox.setDisable(false);
            loginBox.setOpacity(1);
        }

    }

}
