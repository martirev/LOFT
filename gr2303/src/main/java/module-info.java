module ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ui.controllers to javafx.graphics, javafx.fxml;
    opens ui to javafx.graphics, javafx.fxml;
}
