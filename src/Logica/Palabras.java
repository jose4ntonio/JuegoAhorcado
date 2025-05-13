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
    private static final String ARCHIVO = "palabras.txt"; // Nombre del archivo
    private static final Random random = new Random(); // Generador de aleatorios
    private static final String[] PALABRAS_PREDETERMINADAS = {
        "sol", "luna", "arbol", "casa", "rio",
        "cielo", "nube", "estrella", "flor", "mesa"
    }; // Palabras por defecto

    // Obtiene una palabra aleatoria
    public static String obtenerPalabraAleatoria() {
        List<String> palabras = new ArrayList<>();
        
        // Manejo de archivo: Crea archivo si no existe
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (String palabra : PALABRAS_PREDETERMINADAS) {
                    writer.write(palabra); // Escribe palabra
                    writer.newLine(); // Nueva línea
                }
            } catch (IOException e) {
                e.printStackTrace(); // Maneja error
                return "error"; // Palabra por defecto
            }
        }

        // Manejo de archivo: Lee palabras del archivo
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    palabras.add(linea.trim().toLowerCase()); // Agrega palabra
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Maneja error
            return "error"; // Palabra por defecto
        }

        return palabras.isEmpty() ? "java" : palabras.get(random.nextInt(palabras.size())); // Devuelve palabra aleatoria
    }
}
