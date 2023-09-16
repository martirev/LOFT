module ui {
    requires javafx.controls;
    requires javafx.fxml;
    //Json
    requires json.simple;

    opens ui.controllers to javafx.graphics, javafx.fxml;
    opens ui to javafx.graphics, javafx.fxml;
}
