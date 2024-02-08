module smu {
    requires javafx.controls;
    requires javafx.fxml;

    opens smu to javafx.fxml;
    exports smu;
}
