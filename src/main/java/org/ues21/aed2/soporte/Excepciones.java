package org.ues21.aed2.soporte;

public class Excepciones {
    public static String COMMAND_HELP = "El comando debe ser de la forma:\n Para comprimir:\thu21 -c origen [destino]\n Para descomprimir:\thu21 -d origen [destino]";

    public static class ArchivoInvalidoException extends Exception {
        public ArchivoInvalidoException() {
            super("Archivo inválido! Seleccione otro.");
        }
    }

    public static class ComandoInvalidoException extends Exception {
        public ComandoInvalidoException() {
            super(COMMAND_HELP);
        }
    }
}
