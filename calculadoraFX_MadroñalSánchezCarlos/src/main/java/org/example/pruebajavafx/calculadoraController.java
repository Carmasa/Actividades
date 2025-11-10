package org.example.pruebajavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class calculadoraController {

    @FXML
    private Label welcomeText;

    private double numero1 = 0;
    private String operador = "";
    private boolean nuevaOperacion = true;

    @FXML
    public void initialize() {
        welcomeText.setText("0");
    }

    @FXML
    private void boton0Click() {
        agregarNumero("0");
    }
    @FXML
    private void boton1Click() {
        agregarNumero("1");
    }
    @FXML
    private void boton2Click() {
        agregarNumero("2"); }
    @FXML
    private void boton3Click() {
        agregarNumero("3");
    }
    @FXML
    private void boton4Click() {
        agregarNumero("4");
    }
    @FXML
    private void boton5Click() {
        agregarNumero("5");
    }
    @FXML
    private void boton6Click() {
        agregarNumero("6");
    }
    @FXML
    private void boton7Click() {
        agregarNumero("7");
    }
    @FXML
    private void boton8Click() {
        agregarNumero("8");
    }
    @FXML
    private void boton9Click() {
        agregarNumero("9");
    }

    private void agregarNumero(String num) {
        if (nuevaOperacion || welcomeText.getText().equals("0")) {
            welcomeText.setText(num);
            nuevaOperacion = false;
        } else {
            welcomeText.setText(welcomeText.getText() + num);
        }
    }

    @FXML
    private void botonMasClick() {
        operar("+");
    }

    @FXML
    private void botonMenosClick() {
        operar("-");
    }

    @FXML
    private void botonMultClick() {
        operar("*");
    }

    @FXML
    private void botonDivClick() {
        operar("/");
    }

    private void operar(String op) {
        numero1 = Double.parseDouble(welcomeText.getText());
        operador = op;
        nuevaOperacion = true;
    }

    @FXML
    private void botonResultadoClick() {
        double numero2 = Double.parseDouble(welcomeText.getText());
        double resultado = 0;

        switch (operador) {
            case "+": resultado = numero1 + numero2; break;
            case "-": resultado = numero1 - numero2; break;
            case "*": resultado = numero1 * numero2; break;
            case "/":
                if (numero2 != 0)
                    resultado = numero1 / numero2;
                else {
                    welcomeText.setText("Error");
                    nuevaOperacion = true;
                    return;
                }
                break;
            default:
                welcomeText.setText("0");
                return;
        }

        welcomeText.setText(String.valueOf(resultado));
        nuevaOperacion = true;
    }

    @FXML
    private void botonClearClick() {
        welcomeText.setText("0");
        numero1 = 0;
        operador = "";
        nuevaOperacion = true;
    }
}
