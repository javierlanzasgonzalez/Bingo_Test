package bingo;

import java.util.Scanner;

public class GestionClientes {

    /**
     * Funcion recursiva para introducir el DNI del cliente verificando que sea
     * un formato correcto
     *
     * @param entrada
     * @return codigo
     */
    public static String introducirDNI(Scanner entrada) {
        String codigo, input;
        System.out.print("Introduzca el DNI del cliente: ");
        input = entrada.nextLine();
        codigo = input.toUpperCase();
        if (codigo.matches("\\d{8}+[A-Z]")) {
            return codigo;
        }
        System.out.println("El DNI debe tener 8 digitos y 1 letra");
        return introducirDNI(entrada); // Si no se introducen correctamente, se vuelve a llamar a la misma funcion recursiva
    }

    /**
     * Funcion para introducir el nombre del cliente
     *
     * @param entrada
     * @return nombre
     */
    public static String introducirNombre(Scanner entrada) {
        String nombre;
        System.out.print("Introduzca el nombre: ");
        nombre = entrada.nextLine();

        return nombre;
    }

    /**
     * Funcion para introducir el primer apellido del cliente
     *
     * @param entrada
     * @return apellido1
     */
    public static String introducirApellido1(Scanner entrada) {
        String apellido1;
        System.out.print("Introduzca el primer apellido: ");
        apellido1 = entrada.nextLine();

        return apellido1;
    }

    /**
     * Funcion para introducir el segundo apellido del cliente
     *
     * @param entrada
     * @return apellido2
     */
    public static String introducirApellido2(Scanner entrada) {
        String apellido2;
        System.out.print("Introduzca el segundo apellido: ");
        apellido2 = entrada.nextLine();

        return apellido2;
    }

    /**
     * Funcion para introducir la fecha de nacimiento
     *
     * @param entrada
     * @return fecha
     */
    public static String introducirFecha(Scanner entrada) {
        String fecha;
        System.out.print("Introduzca la fecha de nacimiento del cliente (dd/mm/yyyy): ");
        fecha = entrada.nextLine();
        if (fecha.matches("^([0][1-9]|[12][0-9]|3[01])/([0][1-9]|1[0-2])/\\d{4}$")) {
            return fecha;
        }
        System.out.println("La fecha de nacimiento debe tener un formato de fecha (dd/mm/yyyy)");
        return introducirFecha(entrada); // Si no se introducen correctamente, se vuelve a llamar a la misma funcion recursiva
    }
}