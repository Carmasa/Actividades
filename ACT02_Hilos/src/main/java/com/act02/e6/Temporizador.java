package com.act02.e6;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class Temporizador extends Application {
    private TextField campoTiempoObjetivo;
    private ProgressBar barraProgreso;
    private Label etiquetaTiempo;
    private Button botonInicio;
    private Button botonCancelar;

    // AtomicBoolean da seguridad entre hilos
    private AtomicBoolean ejecutando = new AtomicBoolean(false);

    @Override
    public void start(Stage escenarioPrincipal) {
        VBox raiz = new VBox(15);
        raiz.setPadding(new Insets(20));

        Label etiqueta = new Label("Segundos objetivo:");
        campoTiempoObjetivo = new TextField("10");

        barraProgreso = new ProgressBar(0);
        barraProgreso.setPrefWidth(300);

        etiquetaTiempo = new Label("Tiempo transcurrido: 0.0 s");

        botonInicio = new Button("Iniciar");
        botonCancelar = new Button("Cancelar");
        botonCancelar.setDisable(true);

        botonInicio.setOnAction(e -> iniciarTemporizador());
        botonCancelar.setOnAction(e -> cancelarTemporizador());

        raiz.getChildren().addAll(etiqueta, campoTiempoObjetivo, botonInicio, botonCancelar, barraProgreso,
                etiquetaTiempo);

        Scene escena = new Scene(raiz, 350, 250);
        escenarioPrincipal.setTitle("Temporizador con Cancelación");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    private void iniciarTemporizador() {
        try {
            int segundosObjetivo = Integer.parseInt(campoTiempoObjetivo.getText());
            ejecutando.set(true);
            botonInicio.setDisable(true);
            botonCancelar.setDisable(false);
            barraProgreso.setProgress(0);

            new Thread(() -> {
                double segundosActuales = 0;
                double paso = 0.1; // 100ms

                while (segundosActuales < segundosObjetivo && ejecutando.get()) {
                    try {
                        Thread.sleep(100);
                        segundosActuales += paso;

                        double progreso = segundosActuales / segundosObjetivo;
                        double finalSegundosActuales = segundosActuales;

                        Platform.runLater(() -> {
                            barraProgreso.setProgress(progreso);
                            etiquetaTiempo.setText(String.format("Tiempo transcurrido: %.1f s", finalSegundosActuales));
                        });

                    } catch (InterruptedException e) {
                        break;
                    }
                }

                Platform.runLater(() -> {
                    botonInicio.setDisable(false);
                    botonCancelar.setDisable(true);
                    if (ejecutando.get()) {
                        // Finalizado naturalmente
                        etiquetaTiempo.setText("¡Finalizado! Objetivo alcanzado.");
                        barraProgreso.setProgress(1.0);
                    } else {
                        // Cancelado
                        etiquetaTiempo.setText(etiquetaTiempo.getText() + " (Cancelado)");
                    }
                });

            }).start();
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Número inválido");
            alerta.show();
        }
    }

    private void cancelarTemporizador() {
        ejecutando.set(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
