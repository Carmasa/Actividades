package com.act02.e5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CuentaAtras extends Application {
    private TextField campoTiempo;
    private ProgressBar barraProgreso;
    private Label etiquetaEstado;
    private Button botonInicio;

    @Override
    public void start(Stage escenarioPrincipal) {
        VBox raiz = new VBox(15);
        raiz.setPadding(new Insets(20));

        Label etiqueta = new Label("Introduce segundos:");
        campoTiempo = new TextField("10");

        barraProgreso = new ProgressBar(1.0);
        barraProgreso.setPrefWidth(300);

        etiquetaEstado = new Label("Listo");

        botonInicio = new Button("Iniciar Cuenta Atrás");
        botonInicio.setOnAction(e -> iniciarCuentaAtras());

        raiz.getChildren().addAll(etiqueta, campoTiempo, botonInicio, barraProgreso, etiquetaEstado);

        Scene escena = new Scene(raiz, 350, 250);
        escenarioPrincipal.setTitle("Temporizador de Cuenta Atrás");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    private void iniciarCuentaAtras() {
        try {
            int segundos = Integer.parseInt(campoTiempo.getText());
            etiquetaEstado.setText("Contando hacia atrás...");
            barraProgreso.setProgress(1.0);
            botonInicio.setDisable(true);

            Thread hiloCuentaAtras = new Thread(() -> {
                double totalMilis = segundos * 1000;
                double milisActuales = totalMilis;

                while (milisActuales > 0) {
                    try {
                        Thread.sleep(100); // actualizaciones de 100ms
                        milisActuales -= 100;

                        double progreso = milisActuales / totalMilis;

                        double finalMilisActuales = milisActuales;
                        Platform.runLater(() -> {
                            barraProgreso.setProgress(progreso);
                            etiquetaEstado.setText(String.format("Tiempo restante: %.1f s", finalMilisActuales / 1000));
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Platform.runLater(() -> {
                    etiquetaEstado.setText("¡Tiempo terminado!");
                    barraProgreso.setProgress(0);
                    botonInicio.setDisable(false);
                    mostrarAlerta("¡Cuenta Atrás Finalizada!");
                });
            });

            hiloCuentaAtras.setDaemon(true); // Asegurar que el hilo muere si la app cierra
            hiloCuentaAtras.start();

        } catch (NumberFormatException ex) {
            mostrarAlerta("Por favor, introduce un número válido.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText(mensaje);
        alerta.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
