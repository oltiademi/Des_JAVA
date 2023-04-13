module com.example.des_java {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.des_java to javafx.fxml;
    exports com.example.des_java;
}