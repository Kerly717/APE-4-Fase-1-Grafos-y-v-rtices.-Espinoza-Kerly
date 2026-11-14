import java.util.Arrays;

/**
 * Pruebas automáticas de RedSocial (sin librerías externas).
 * Ejecutar: java PruebasRedSocial   -> imprime PASA/FALLA por caso y un resumen.
 */
public class PruebasRedSocial {

    private static int pasan = 0;
    private static int fallan = 0;

    private static void verificar(String caso, Object esperado, Object obtenido) {
        boolean ok = esperado.equals(obtenido);
        if (ok) pasan++; else fallan++;
        System.out.printf("%-5s %-58s esperado=%s obtenido=%s%n",
                ok ? "PASA" : "FALLA", caso, esperado, obtenido);
    }

    private static RedSocial crearRed() {
        RedSocial red = new RedSocial();
        String[][] us = {{"U01","Mateo"},{"U02","Sofía"},{"U03","Andrés"},
                         {"U04","Camila"},{"U05","Luis"},{"U06","Valeria"}};
        for (String[] u : us) red.agregarUsuario(u[0], u[1]);
        red.agregarAmistad("U02", "U03");
        red.agregarAmistad("U03", "U04");
        red.agregarAmistad("U02", "U04");
        red.agregarAmistad("U04", "U05");
        red.agregarAmistad("U05", "U06");
        return red;
    }

    public static void main(String[] args) {
        RedSocial red = crearRed();

        System.out.println("=== Grados ===");
        verificar("T01 grado(U01) usuario aislado", 0, red.grado("U01"));
        verificar("T02 grado(U04) mayor grado", 3, red.grado("U04"));
        verificar("T03 grado(U06) extremo", 1, red.grado("U06"));
        verificar("T04 grado(U99) usuario inexistente", -1, red.grado("U99"));
        int suma = 0;
        for (String id : Arrays.asList("U01","U02","U03","U04","U05","U06")) suma += red.grado(id);
        verificar("T05 suma de grados = 2 x aristas", 10, suma);

        System.out.println("=== existeRelacion ===");
        verificar("T06 U02-U03 amigos", true, red.existeRelacion("U02", "U03"));
        verificar("T07 U03-U02 simetría (no dirigido)", true, red.existeRelacion("U03", "U02"));
        verificar("T08 U04-U05 amigos", true, red.existeRelacion("U04", "U05"));
        verificar("T09 U01-U02 no amigos (aislado)", false, red.existeRelacion("U01", "U02"));
        verificar("T10 U03-U06 no amigos (solo camino)", false, red.existeRelacion("U03", "U06"));

        System.out.println("=== vecinos ===");
        verificar("T11 vecinos(U04)", "[U03, U02, U05]", red.vecinos("U04").toString());
        verificar("T12 vecinos(U01) vacío", "[]", red.vecinos("U01").toString());
        verificar("T13 vecinos(U99) vacío", "[]", red.vecinos("U99").toString());

        System.out.println("=== Reglas (rechazos) ===");
        verificar("T14 lazo U04-U04 rechazado", false, red.agregarAmistad("U04", "U04"));
        verificar("T15 duplicado U03-U02 rechazado", false, red.agregarAmistad("U03", "U02"));
        verificar("T16 usuario inexistente U04-U07 rechazado", false, red.agregarAmistad("U04", "U07"));
        verificar("T17 usuario repetido U02 rechazado", false, red.agregarUsuario("U02", "Otro"));
        verificar("T18 grado(U04) no cambió tras rechazos", 3, red.grado("U04"));

        System.out.println("=== Amistad válida nueva ===");
        verificar("T19 U01-U06 aceptada", true, red.agregarAmistad("U01", "U06"));
        verificar("T20 grado(U01) pasa a 1", 1, red.grado("U01"));

        System.out.println("\nResumen: " + pasan + " pasan, " + fallan + " fallan de " + (pasan + fallan));
        if (fallan > 0) System.exit(1);
    }
}
