module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    //this is needed----------------
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;

    //------------------------------

    opens org.example to javafx.fxml;
    exports org.example;
}

