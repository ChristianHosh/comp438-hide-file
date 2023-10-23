module com.comp438.hidefile {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires lombok;

    opens com.comp438.hidefile to javafx.fxml;
    exports com.comp438.hidefile;
    exports com.comp438.hidefile.controller;
    opens com.comp438.hidefile.controller to javafx.fxml;
    exports com.comp438.hidefile.service;
    opens com.comp438.hidefile.service to javafx.fxml;
}