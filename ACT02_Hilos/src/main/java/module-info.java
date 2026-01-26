module org.example.act02_hilos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.example.act02_hilos to javafx.fxml;

    exports org.example.act02_hilos;

    // Exports for the new exercises
    exports com.act02.e4;
    exports com.act02.e5;
    exports com.act02.e6;
}