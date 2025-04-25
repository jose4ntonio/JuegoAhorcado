package Logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Palabras {
    private static final String ARCHIVO = "palabras.txt";
    private static final Random random = new Random();
    private static final String[] PALABRAS_PREDETERMINADAS = {
        "sol",
        "luna",
        "arbol",
        "casa",
        "rio",
        "cielo",
        "nube",
        "estrella",
        "flor",
        "mesa"
    };

    public static String obtenerPalabraAleatoria() {
        List<String> palabras = new ArrayList<>();
        
        // Verificar si el archivo existe, si no, crearlo
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (String palabra : PALABRAS_PREDETERMINADAS) {
                    writer.write(palabra);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Si falla la creación, usar una palabra por defecto
                return "error";
            }
        }

        // Leer palabras del archivo
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    palabras.add(linea.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // Palabra por defecto en caso de error
        }

        return palabras.isEmpty() ? "java" : palabras.get(random.nextInt(palabras.size()));
    }
}