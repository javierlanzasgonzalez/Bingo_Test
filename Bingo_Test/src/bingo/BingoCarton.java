package bingo;

import java.util.*;

public class BingoCarton {

    private final int[][] carton;         // Matriz que representa los números del cartón
    private final boolean[][] marcados;   // Matriz que indica qué números ya han sido marcados
    private boolean lineaAnunciada = false; // Controla si ya se ha anunciado la primera línea

    /**
     * Constructor que inicializa el cartón y genera los números automáticamente.
     */
    public BingoCarton() {
        carton = new int[3][9];          // 3 filas y 9 columnas (formato estándar de cartón de bingo)
        marcados = new boolean[3][9];    // Inicialmente todo está sin marcar
        generarCarton();                 // Se genera automáticamente un nuevo cartón
    }

    /**
     * Genera un cartón de bingo válido con 15 números (5 por fila, 3 por columna).
     */
    private void generarCarton() {
        Random random = new Random();
        List<List<Integer>> columnas = new ArrayList<>();

        // Inicializar 9 listas para cada columna del cartón
        for (int i = 0; i < 9; i++) {
            columnas.add(new ArrayList<>());
        }

        // Rellenar cada columna con 3 números únicos dentro de su rango correspondiente
        for (int col = 0; col < 9; col++) {
            int min = col * 10 + 1; // Mínimo valor para la columna
            int max = (col == 8) ? 90 : (col + 1) * 10; // Última columna llega hasta 90

            Set<Integer> numeros = new TreeSet<>(); // Set para evitar duplicados y ordenar

            // Seleccionar 3 números únicos por columna
            while (numeros.size() < 3) {
                numeros.add(random.nextInt(max - min + 1) + min);
            }

            columnas.get(col).addAll(numeros); // Guardar números generados
        }

        // Asignar los números a las filas del cartón
        for (int fila = 0; fila < 3; fila++) {
            List<Integer> indicesSeleccionados = new ArrayList<>();

            // Elegir 5 columnas aleatorias para esta fila (solo en esas pondremos número)
            while (indicesSeleccionados.size() < 5) {
                int col = random.nextInt(9);
                if (!indicesSeleccionados.contains(col)) {
                    indicesSeleccionados.add(col);
                }
            }

            // Asignar los números seleccionados a su posición en el cartón
            for (int col = 0; col < 9; col++) {
                if (indicesSeleccionados.contains(col)) {
                    carton[fila][col] = columnas.get(col).remove(0); // Extraer en orden (ya estaban ordenados)
                } else {
                    carton[fila][col] = 0; // Casilla vacía
                }
            }
        }
    }

    /**
     * Marca un número si se encuentra en el cartón.
     * @param numero número a marcar
     */
    public void marcarNumero(int numero) {
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 9; col++) {
                if (carton[fila][col] == numero) {
                    marcados[fila][col] = true; // Marcar la casilla
                }
            }
        }
    }

    /**
     * Verifica si hay una línea completa (una fila con todos los números marcados).
     * Solo devuelve true la primera vez que ocurre.
     * @return true si se completa la primera línea, false en otro caso.
     */
    public boolean verificarPrimeraLinea() {
        if (this.lineaAnunciada) {
            return false; // Ya se anunció una línea antes
        }

        for (int fila = 0; fila < 3; fila++) {
            boolean lineaCompleta = true;

            for (int col = 0; col < 9; col++) {
                if (carton[fila][col] != 0 && !marcados[fila][col]) {
                    lineaCompleta = false; // Si hay un número no marcado, no hay línea
                }
            }

            if (lineaCompleta) {
                this.lineaAnunciada = true; // Evita repetir el aviso de línea
                return true; // Línea completada
            }
        }
        return false;
    }

    /**
     * Verifica si se ha hecho BINGO (las 3 filas con sus 5 números marcados).
     * @return true si se ha hecho bingo.
     */
    public boolean verificarBingo() {
        int filasCompletas = 0;

        for (int fila = 0; fila < 3; fila++) {
            boolean lineaCompleta = true;

            for (int col = 0; col < 9; col++) {
                if (carton[fila][col] != 0 && !marcados[fila][col]) {
                    lineaCompleta = false; // Falta marcar un número en esta fila
                }
            }

            if (lineaCompleta) {
                filasCompletas++;
            }
        }

        return filasCompletas == 3; // Solo es BINGO si las 3 filas están completas
    }

    /**
     * Devuelve la matriz del cartón con los números.
     * @return matriz de enteros con los valores del cartón
     */
    public int[][] getCarton() {
        return carton;
    }

    /**
     * Devuelve la matriz de marcados (true si el número fue acertado).
     * @return matriz booleana con los aciertos
     */
    public boolean[][] getMarcados() {
        return marcados;
    }
}