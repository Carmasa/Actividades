module org.example.pruebajavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.pruebajavafx to javafx.fxml;
    exports org.example.pruebajavafx;
}