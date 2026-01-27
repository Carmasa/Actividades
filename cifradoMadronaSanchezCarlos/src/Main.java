import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String opt;

        while (true) {
            System.out.println("Seleccione tipo de cifrado:");
            System.out.println("1) simétrico");
            System.out.println("2) asimétrico");
            System.out.print("0 para salir: ");
            opt = sc.nextLine().trim();

            switch (opt) {
                case "1":
                    CifradoSimetrico cifradoSimetrico = new CifradoSimetrico();
                    cifradoSimetrico.metodoSimetrico();
                    break;

                case "2":
                    asimetrico cifradoAsimetrico = new asimetrico();
                    cifradoAsimetrico.metodoAsimetrico();
                    break;

                case "0":
                    System.out.println("Saliendo del programa...");
                    sc.close();
                    return;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

            System.out.println(); // línea en blanco para mejor lectura
        }
    }
}