package com.act02.e4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class Carrera extends Application {
    private TextField campoDistancia;
    private TextField[] camposVelocidad = new TextField[4];
    private Label[] cochesEmojis = new Label[4];
    private Label[] etiquetasEstado = new Label[4];
    private TextArea areaRegistro;
    private AtomicBoolean carreraFinalizada = new AtomicBoolean(false);

    // Ancho de la pista visual
    private static final double ANCHO_PISTA = 600;
    private static final String[] EMOJIS = { "🚗", "🏎️", "🚓", "🚕" };

    @Override
    public void start(Stage escenarioPrincipal) {
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(10));

        // Configuración
        HBox panelConfig = new HBox(10);
        panelConfig.setAlignment(Pos.CENTER_LEFT);
        Label etiquetaDistancia = new Label("Distancia simulada (m):");
        campoDistancia = new TextField("100");
        panelConfig.getChildren().addAll(etiquetaDistancia, campoDistancia);
        raiz.getChildren().add(panelConfig);

        // cont pistas
        VBox contenedorPistas = new VBox(0);
        contenedorPistas.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        for (int i = 0; i < 4; i++) {
            // Panel para un carril individual
            VBox carrilPanel = new VBox(5);
            carrilPanel.setPadding(new Insets(5));
            carrilPanel.setStyle("-fx-border-color: #ccc; -fx-border-style: solid hidden solid hidden;");

            // Controles del coche
            HBox controlesCoche = new HBox(10);
            controlesCoche.setAlignment(Pos.CENTER_LEFT);
            Label etiquetaCoche = new Label("Coche " + (i + 1) + " Vel (m/s):");
            camposVelocidad[i] = new TextField((10 + i * 5) + "");
            camposVelocidad[i].setPrefWidth(50);
            etiquetasEstado[i] = new Label("Listo");
            controlesCoche.getChildren().addAll(etiquetaCoche, camposVelocidad[i], etiquetasEstado[i]);

            // La pista visual
            Pane pista = new Pane();
            pista.setPrefWidth(ANCHO_PISTA);
            pista.setPrefHeight(40);
            pista.setStyle("-fx-background-color: #555;"); // camino

            // Línea de meta
            Line lineaMeta = new Line(ANCHO_PISTA - 50, 0, ANCHO_PISTA - 50, 40);
            lineaMeta.setStroke(Color.WHITE);
            lineaMeta.setStrokeWidth(4);
            lineaMeta.getStrokeDashArray().addAll(10d, 10d); // meta

            // El Coche (Emoji)
            cochesEmojis[i] = new Label(EMOJIS[i]);
            cochesEmojis[i].setFont(new Font(24));
            cochesEmojis[i].setLayoutX(10); // posicion inicial
            cochesEmojis[i].setLayoutY(0);

            pista.getChildren().addAll(lineaMeta, cochesEmojis[i]);

            carrilPanel.getChildren().addAll(controlesCoche, pista);
            contenedorPistas.getChildren().add(carrilPanel);
        }
        raiz.getChildren().add(contenedorPistas);

        Button botonInicio = new Button("Comenzar carrera");
        botonInicio.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-base: #4CAF50;");
        botonInicio.setOnAction(e -> iniciarCarrera());

        areaRegistro = new TextArea();
        areaRegistro.setPrefHeight(100);
        areaRegistro.setEditable(false);

        raiz.getChildren().addAll(botonInicio, areaRegistro);

        Scene escena = new Scene(raiz, 650, 700);
        escenarioPrincipal.setTitle("Carrera Hilos");
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.show();
    }

    private void iniciarCarrera() {
        carreraFinalizada.set(false);
        areaRegistro.appendText("--- NUEVA CARRERA ---\n");

        try {
            int distanciaSimulada = Integer.parseInt(campoDistancia.getText());

            for (int i = 0; i < 4; i++) {
                cochesEmojis[i].setLayoutX(10); // Resetear posición
                etiquetasEstado[i].setText("Corriendo...");

                int velocidad = Integer.parseInt(camposVelocidad[i].getText());

                // Crear e iniciar hilo de coche
                HiloCoche coche = new HiloCoche(i + 1, distanciaSimulada, velocidad);
                new Thread(coche).start();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error en los números. Revisa la distancia y velocidades.");
        }
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }

    class HiloCoche implements Runnable {
        private int id;
        private int distanciaTotalSimulada; // Metros
        private int velocidad; // m/s
        private double distanciaActual = 0;

        // Configuración de la pista visual
        private final double POSICION_INICIAL = 10;
        private final double POSICION_META = ANCHO_PISTA - 50;
        private final double DISTANCIA_VISUAL_TOTAL = POSICION_META - POSICION_INICIAL;

        public HiloCoche(int id, int distanciaTotal, int velocidad) {
            this.id = id;
            this.distanciaTotalSimulada = distanciaTotal;
            this.velocidad = velocidad;
        }

        @Override
        public void run() {
            while (distanciaActual < distanciaTotalSimulada && !carreraFinalizada.get()) {
                try {
                    Thread.sleep(50); // Fluidez visual (20 FPS aprox)

                    // Calcular avance
                    // paso (m) = velocidad (m/s) * tiempo (0.05s)
                    double paso = velocidad * 0.05;
                    distanciaActual += paso;

                    // Calcular posición visual en píxeles
                    // Porcentaje completado
                    double porcentaje = distanciaActual / distanciaTotalSimulada;
                    if (porcentaje > 1.0)
                        porcentaje = 1.0;

                    double nuevaPosicionX = POSICION_INICIAL + (DISTANCIA_VISUAL_TOTAL * porcentaje);

                    Platform.runLater(() -> {
                        if (!carreraFinalizada.get()) {
                            cochesEmojis[id - 1].setLayoutX(nuevaPosicionX);
                            etiquetasEstado[id - 1].setText(String.format("%.0f m", distanciaActual));
                        }
                    });

                } catch (InterruptedException e) {
                    return;
                }
            }

            if (distanciaActual >= distanciaTotalSimulada) {
                // Comprobar ganador de forma atómica
                if (carreraFinalizada.compareAndSet(false, true)) {
                    Platform.runLater(() -> {
                        // Asegurar visualmente que llegó a la meta
                        cochesEmojis[id - 1].setLayoutX(POSICION_META);
                        etiquetasEstado[id - 1].setText("¡GANADOR! 🥇");
                        areaRegistro
                                .appendText("¡El Coche " + id + " (" + EMOJIS[id - 1] + ") ha ganado la carrera!\n");

                        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                        alerta.setTitle("¡Tenemos un ganador!");
                        alerta.setHeaderText("Victoria del Coche " + id);
                        alerta.setContentText("¡El vehiculo " + EMOJIS[id - 1] + " ha cruzado la meta primero!");
                        alerta.show();
                    });
                } else {
                    Platform.runLater(() -> {
                        if (cochesEmojis[id - 1].getLayoutX() < POSICION_META) {
                            cochesEmojis[id - 1].setLayoutX(POSICION_META); // Cruzar meta visualmente
                        }
                        etiquetasEstado[id - 1].setText("Finalizado");
                    });
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
