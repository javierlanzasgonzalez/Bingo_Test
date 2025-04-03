package bingo;

import java.util.*;

/**
 * Clase que gestiona la lógica principal del juego de bingo,
 * incluyendo el control de los números ya sacados y la interacción con el cartón.
 */
public class BingoJuego {

    private final BingoCarton carton;           // Cartón de bingo asociado al jugador
    private final Set<Integer> numerosSacados;  // Conjunto de números ya sorteados
    private final Random random;                // Generador de números aleatorios

    /**
     * Constructor de la clase BingoJuego
     * @param carton Cartón de bingo que usará el jugador en esta partida
     */
    public BingoJuego(BingoCarton carton) {
        this.carton = carton;
        this.numerosSacados = new HashSet<>();  // Inicialmente ningún número ha sido sacado
        this.random = new Random();
    }

    /**
     * Saca un número aleatorio entre 1 y 90 que no haya sido sacado antes.
     * Marca el número en el cartón si está presente.
     * @return número sorteado o -1 si ya se han sacado los 90 números.
     */
    public int sacarNumero() {
        // Si ya se han sacado los 90 números, no se puede sacar más
        if (numerosSacados.size() >= 90) {
            return -1;
        }

        int numero;
        do {
            numero = random.nextInt(90) + 1; // Genera un número entre 1 y 90
        } while (numerosSacados.contains(numero)); // Repetir si ya fue sacado

        numerosSacados.add(numero);        // Guardar el número como ya sacado
        carton.marcarNumero(numero);       // Marcar en el cartón si aparece
        return numero;                     // Devolver el número sorteado
    }

    /**
     * Devuelve el cartón de bingo asociado al juego.
     * @return cartón del jugador
     */
    public BingoCarton getCarton() {
        return carton;
    }
}