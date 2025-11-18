import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        // Lista de comandos a ejecutar
        String[][] comandos = {
                {"Notepad", "notepad"},
                {"Calculadora", "calc"},
                {"Shell CMD", "cmd", "/C", "echo Prueba de ExitValue"},
                {"Comando Incorrecto", "comando_no_existe"},
                {"Ping correcto", "ping", "localhost", "-n", "1"},
                {"Ping mal parametrizado", "ping", "-parametroIncorrecto"}
        };

        for (String[] comando : comandos) {
            String nombre = comando[0];

            System.out.println("\nEjecutando: " + nombre);

            try {
                ProcessBuilder pb = new ProcessBuilder(comando[1],
                        comando.length > 2 ? comando[2] : "",
                        comando.length > 3 ? comando[3] : "",
                        comando.length > 4 ? comando[4] : "");

                Process p = pb.start();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream())
                );

                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(p.getErrorStream())
                );

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("  [OUT] " + line);
                }

                while ((line = errorReader.readLine()) != null) {
                    System.out.println("  [ERR] " + line);
                }

                int exitCode = p.waitFor();

                System.out.println("➡ Programa: " + nombre);
                System.out.println("➡ Código de salida: " + exitCode);

            } catch (IOException e) {
                System.out.println("ERROR: El comando no existe o no puede ejecutarse.");
            } catch (InterruptedException e) {
                System.out.println("ERROR: Ejecución interrumpida.");
            }
        }

        System.out.println("\nPrograma terminado.");
    }
}
