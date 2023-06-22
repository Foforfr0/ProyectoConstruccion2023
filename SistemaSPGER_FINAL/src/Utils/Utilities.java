package Utils;

import javafx.scene.control.Alert;

public class Utilities {
    public static boolean compareString(Object obj, String text) {
        if (obj instanceof String) {
            String otraCadena = (String) obj;
            // Obtener la longitud de las dos cadenas
            int length = text.length();
            // Comprobar si las dos cadenas tienen la misma longitud
            if (length == otraCadena.length()) {
                // Comparar caracter por caracter
                for (int i = 0; i < length; i++) {
                    if (text.charAt(i) != otraCadena.charAt(i)) {
                        return false;
                    }
                }
                // Si todas las comparaciones son iguales, las dos cadenas son iguales
                return true;
            }
        }
        return false;
    }   
}
