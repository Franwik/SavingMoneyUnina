module app.sources {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.sources to javafx.fxml;
    exports app.sources;
}