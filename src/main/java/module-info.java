module org.example.monopoly {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;

    opens org.example.monopoly to javafx.fxml;
    opens org.example.monopoly.model to javafx.fxml, com.google.gson;
    exports org.example.monopoly;
    exports org.example.monopoly.model;
}
