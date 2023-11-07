module ui {
    requires core;
    requires filehandling;
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens ui.controllers to javafx.graphics, javafx.fxml;
    opens ui to javafx.graphics, javafx.fxml;
}
