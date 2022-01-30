module teensy.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib.jdk8;
    opens uk.co.renbinden.teensy.controller to javafx.fxml;
    opens uk.co.renbinden.teensy.controller.tab to javafx.fxml;
    exports uk.co.renbinden.teensy;
}