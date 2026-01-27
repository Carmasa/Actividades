import java.security.KeyPairGenerator;
import java.util.Scanner;
import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public class asimetrico {
    Scanner sc = new Scanner(System.in);
    String texto;
    public String algoritmo = "RSA";
    private Cipher cifrado;
    private Cipher descifrado;


    // Metodo para manejar el cifrado asimétrico
    public void metodoAsimetrico() {
        try {
            System.out.println("Método del cifrado asimétrico ejecutado.");
            System.out.println("Introduce un texto para cifrar");
            texto = sc.nextLine();
            System.out.println("Texto a cifrar: " + texto);

            //Generar clave pública y privada
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algoritmo);
            keyGen.initialize(2048);
            var pair = keyGen.generateKeyPair();
            var publicKey = pair.getPublic();
            var privateKey = pair.getPrivate();
            System.out.println("Clave pública: " + publicKey);
            System.out.println("Clave privada: " + privateKey);

            // Inicializar el cifrador
            cifrado = Cipher.getInstance(algoritmo);
            cifrado.init(Cipher.ENCRYPT_MODE, publicKey);
            String textoCifrado = new String(cifrado.doFinal(texto.getBytes()));

            System.out.println("Texto cifrado: " + Base64.getEncoder().encodeToString(textoCifrado.getBytes()));

            //Descifrado
            descifrado = Cipher.getInstance(algoritmo);
            descifrado.init(Cipher.DECRYPT_MODE, privateKey);
            System.out.println("Texto descifrado: " + new String(descifrado.doFinal(cifrado.doFinal(texto.getBytes()))));


        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
}