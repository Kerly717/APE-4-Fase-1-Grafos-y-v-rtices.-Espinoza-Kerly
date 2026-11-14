import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Red social AmbaConecta: grafo simple, no dirigido y no ponderado.
 * Representación: lista de adyacencia (id -> lista de ids amigos).
 */
public class RedSocial {

    private final Map<String, String> nombres = new LinkedHashMap<>();
    private final Map<String, List<String>> adyacencia = new LinkedHashMap<>();
    private int totalAmistades = 0;

    /** Registra un usuario con id único. */
    public boolean agregarUsuario(String id, String nombre) {
        if (id == null || nombre == null || nombres.containsKey(id)) {
            System.out.println("RECHAZADO: el usuario " + id + " ya existe o los datos son inválidos.");
            return false;
        }
        nombres.put(id, nombre);
        adyacencia.put(id, new ArrayList<>());
        return true;
    }

    /** Crea una amistad mutua aplicando las reglas del grafo simple. */
    public boolean agregarAmistad(String a, String b) {
        if (!nombres.containsKey(a) || !nombres.containsKey(b)) {
            System.out.println("RECHAZADO " + a + " - " + b + ": usuario inexistente.");
            return false;
        }
        if (a.equals(b)) {
            System.out.println("RECHAZADO " + a + " - " + b + ": un usuario no puede ser amigo de sí mismo (lazo).");
            return false;
        }
        if (existeRelacion(a, b)) {
            System.out.println("RECHAZADO " + a + " - " + b + ": la amistad ya existe (arista duplicada).");
            return false;
        }
        adyacencia.get(a).add(b);
        adyacencia.get(b).add(a); // no dirigido: se guarda en ambos sentidos
        totalAmistades++;
        return true;
    }

    /** true si a y b son amigos. */
    public boolean existeRelacion(String a, String b) {
        return adyacencia.containsKey(a) && adyacencia.get(a).contains(b);
    }

    /** Lista de amigos (vecinos) de un usuario. */
    public List<String> vecinos(String id) {
        if (!adyacencia.containsKey(id)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adyacencia.get(id));
    }

    /** Número de amigos del usuario. */
    public int grado(String id) {
        return adyacencia.containsKey(id) ? adyacencia.get(id).size() : -1;
    }

    public void mostrar() {
        System.out.println("\n--- Lista de adyacencia ---");
        for (String id : adyacencia.keySet()) {
            System.out.println(id + " (" + nombres.get(id) + ") [grado " + grado(id) + "] -> " + adyacencia.get(id));
        }
        int suma = 0;
        for (String id : adyacencia.keySet()) suma += grado(id);
        System.out.println("Usuarios: " + nombres.size() + " | Amistades: " + totalAmistades);
        System.out.println("Suma de grados: " + suma + " = 2 x " + totalAmistades + " = " + (2 * totalAmistades));
    }

    public static void main(String[] args) {
        RedSocial red = new RedSocial();

        // V: 6 usuarios
        red.agregarUsuario("U01", "Mateo");
        red.agregarUsuario("U02", "Sofía");
        red.agregarUsuario("U03", "Andrés");
        red.agregarUsuario("U04", "Camila");
        red.agregarUsuario("U05", "Luis");
        red.agregarUsuario("U06", "Valeria");

        // E: 5 amistades
        red.agregarAmistad("U02", "U03");
        red.agregarAmistad("U03", "U04");
        red.agregarAmistad("U02", "U04");
        red.agregarAmistad("U04", "U05");
        red.agregarAmistad("U05", "U06");

        red.mostrar();

        System.out.println("\n--- Pregunta 5: consultas ---");
        System.out.println("existeRelacion(U02, U03) = " + red.existeRelacion("U02", "U03"));
        System.out.println("existeRelacion(U04, U05) = " + red.existeRelacion("U04", "U05"));
        System.out.println("existeRelacion(U01, U02) = " + red.existeRelacion("U01", "U02"));
        System.out.println("existeRelacion(U03, U06) = " + red.existeRelacion("U03", "U06"));
        System.out.println("vecinos(U04) = " + red.vecinos("U04"));
        System.out.println("grado(U04) = " + red.grado("U04"));

        System.out.println("\n--- Pregunta 6: amistades inválidas ---");
        red.agregarAmistad("U04", "U04"); // lazo
        red.agregarAmistad("U03", "U02"); // duplicado (misma arista que U02-U03)
        red.agregarAmistad("U04", "U07"); // usuario inexistente
    }
}
