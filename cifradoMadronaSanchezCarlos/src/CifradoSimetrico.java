import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Scanner;

public class CifradoSimetrico {
    Scanner sc = new Scanner(System.in);
    String texto;
    public String algoritmo = "AES";
    private Cipher cifrado;
    private byte[] iv;
    private Cipher descifrado;
    private ByteArrayOutputStream outputStream;


    public void metodoSimetrico() {
        try {
            System.out.println("Método del cifrado simétrico ejecutado.");
            System.out.println("Introduce un texto para cifrar");
            texto = sc.nextLine();
            System.out.println("Texto a cifrar: " + texto);

            // Generar clave secreta
            KeyGenerator keyGen = KeyGenerator.getInstance(algoritmo);
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            // Inicializar el cifrador
            cifrado = Cipher.getInstance(algoritmo);
            cifrado.init(Cipher.ENCRYPT_MODE, secretKey);

            String textoCifrado = new String(cifrado.doFinal(texto.getBytes()));

            System.out.println("Texto cifrado: " + Base64.getEncoder().encodeToString(textoCifrado.getBytes()));

            //Descifrado
            descifrado = Cipher.getInstance(algoritmo);
            descifrado.init(Cipher.DECRYPT_MODE, secretKey);
            System.out.println("Texto descifrado: " + new String(descifrado.doFinal(cifrado.doFinal(texto.getBytes()))));


        } catch (Exception e) {
            e.printStackTrace();


        }
    }
}