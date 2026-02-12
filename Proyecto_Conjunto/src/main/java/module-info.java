module org.example.proyecto_conjunto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires transitive java.sql;

    opens view to javafx.fxml;
    opens org.example.proyecto_conjunto to javafx.fxml;
    opens org.example.proyecto_conjunto.controller to javafx.fxml;
    opens org.example.proyecto_conjunto.model to javafx.base;

    exports org.example.proyecto_conjunto;
    exports org.example.proyecto_conjunto.model;
    exports org.example.proyecto_conjunto.dao;
    exports org.example.proyecto_conjunto.controller;
}