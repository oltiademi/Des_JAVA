module com.example.des_java {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens Projekt to javafx.fxml;
    exports Projekt;

}