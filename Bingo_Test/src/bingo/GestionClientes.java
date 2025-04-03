package bingo;

import java.io.*;
import java.util.InputMismatchException;
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

    /**
     * Metodo para modificar un cliente
     */
    public static void modificarcliente() {
        Scanner entrada = new Scanner(System.in);
        File fichero = new File("clientes.txt");
        File temp = new File("temporal.txt");
        // Si el fichero no existe muestra el mensaje
        if (!fichero.exists()) {
            System.out.println("El fichero no existe");
        } else {
            try {
                // Se abren los ficheros de lectura y escritura
                BufferedReader leer = new BufferedReader(new FileReader(fichero));
                BufferedWriter escribir = new BufferedWriter(new FileWriter(temp));
                String linea, codigo;
                int op;
                boolean cambios = false; // Booleano para controlar si se hacen cambios
                boolean encontrado = false; // Booleano para controlar si se encuentra cliente
                codigo = introducirDNI(entrada);
                while ((linea = leer.readLine()) != null) {
                    if (linea.startsWith(codigo)) {
                        encontrado = true;
                        System.out.println("Se va a modificar el cliente: " + linea);
                        String[] palabra = linea.split(" "); // Se crea un array con las palabras que
                        // tiene la linea para modificarlas despues
                        Menu.menuModCliente();
                        System.out.print("Seleccione que dato desea modificar: ");
                        op = entrada.nextInt();
                        entrada.nextLine();
                        switch (op) {
                            // Cada menu se utiliza para introducir el valor nuevo al cliente
                            case 1 ->
                                palabra[0] = introducirDNI(entrada);
                            case 2 ->
                                palabra[1] = introducirNombre(entrada);
                            case 3 ->
                                palabra[2] = introducirApellido1(entrada);
                            case 4 ->
                                palabra[3] = introducirApellido2(entrada);
                            case 5 ->
                                palabra[4] = introducirFecha(entrada);
                            case 6 -> {
                                // Al volver atras no se realizaran cambios y el booleano se pone false
                                System.out.println("No se va a realizar ningun cambio");
                                cambios = false;
                            }
                            default -> {
                                System.out.println("Opcion no valida, debe introducir una opcion de la lista");
                            }
                        }
                        if (op != 6) {
                            // Si se realiza algun cambio, se unen todas las palabras de nuevo en un String y se escribe en el fichero
                            escribir.write(String.join(" ", palabra) + "\n");
                            cambios = true;
                        }
                    } else {
                        escribir.write(linea + "\n");
                    }
                }
                // Cierre de los archivos
                escribir.close();
                leer.close();
                // Si no hay cambios se elimina el fichero temporal y si se hacen cambios
                // se elimina el fichero original y se renombra el temporal
                if (cambios) {
                    if (fichero.delete() && temp.renameTo(fichero)) {
                        System.out.println("Datos modificados correctamente");
                    } else {
                        throw new IOException();
                    }
                } else {
                    temp.delete();
                }
                // Si no se encuentra al cliente se muestra el mensaje
                if (!encontrado) {
                    System.out.println("No se ha encontrado el cliente");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error. Debe introducir un numero.\n");
                entrada.nextLine();
            } catch (IOException e) {
                System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
    }
}