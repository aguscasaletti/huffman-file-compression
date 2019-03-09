package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.tablaHash.ItemTablaSimbolos;
import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;
import org.ues21.aed2.vista.ProgressListener;

public class CodificadorHuffman {

    public static String codificar(final TablaHashSimbolos tablaSimbolos, final String mensaje) {
        char[] vectTexto = mensaje.toCharArray();
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            ItemTablaSimbolos itemTablaSimbolos = tablaSimbolos.findSimbolo(String.valueOf(c));
            if (itemTablaSimbolos != null) {
                sbCod.append(itemTablaSimbolos.getCodigo());
            }
        }
        return sbCod.toString();
    }

    public static String decodificar(final ArchivoU21 archivo, final ProgressListener progressListener) {
        char[] vectTexto = archivo.getCodigo().toCharArray();
        StringBuilder sbMensaje = new StringBuilder();
        StringBuilder sbBusqueda = new StringBuilder();

        for (long i = 0; i < vectTexto.length; i++) {
            if (progressListener != null && i > 100 && i % 100 == 0) {
                progressListener.onProgressUpdate(i * 100 / vectTexto.length);
            }
            sbBusqueda.append(vectTexto[(int) i]);
            ItemTablaSimbolos itemTablaSimbolos = archivo.getTablaSimbolos().findCodigo(sbBusqueda.toString());
            if (itemTablaSimbolos != null) {
                sbMensaje.append(itemTablaSimbolos.getSimbolo());
                sbBusqueda.setLength(0);
            }
        }

        if (progressListener != null) {
            progressListener.onComplete();
        }
        return sbMensaje.toString();
    }

    public static String decodificar(final ArchivoU21 archivo) {
        return CodificadorHuffman.decodificar(archivo, null);
    }
}
