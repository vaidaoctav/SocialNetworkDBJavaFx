module com.example.mysocialnetworkdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.mysocialnetworkdb to javafx.fxml;
    opens com.example.mysocialnetworkdb.domain to javafx.fxml,javafx.base;
    exports com.example.mysocialnetworkdb;
}