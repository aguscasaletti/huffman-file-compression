package org.ues21.aed2.soporte.third;

import org.ues21.aed2.estructuras.lista.Nodo;
import org.ues21.aed2.soporte.ProgressListener;
import org.ues21.aed2.soporte.first.ArchivoU21;

public class CodificadorHuffmanThird {

    public static String codificar(final CharMap tablaSimbolos, final String mensaje) {
        char[] vectTexto = mensaje.toCharArray();
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            String codigo  = tablaSimbolos.get(c);
            if (codigo != null) {
                sbCod.append(codigo);
            }
        }
        return sbCod.toString();
    }

    public static String decodificar(final ArchivoU21Third archivo, final ProgressListener progressListener) {
        char[] vectTexto = archivo.getCodigo().toCharArray();
        StringBuilder sbMensaje = new StringBuilder();
        StringBuilder sbBusqueda = new StringBuilder();

        for (long i = 0; i < vectTexto.length; i++) {
            if (progressListener != null && i > 100 && i % 100 == 0) {
                progressListener.onProgressUpdate(i * 100 / vectTexto.length);
            }
            sbBusqueda.append(vectTexto[(int) i]);
            BSTNode node = archivo.getTablaSimbolos().find(new ItemTablaSimbolos('_', sbBusqueda.toString()));
            if (node != null) {
                ItemTablaSimbolos itemTablaSimbolos = (ItemTablaSimbolos) node.getData();
                sbMensaje.append(itemTablaSimbolos.getSimbolo());
                sbBusqueda.setLength(0);
            }
        }

        if (progressListener != null) {
            progressListener.onComplete();
        }
        return sbMensaje.toString();
    }

    public static String decodificar(final ArchivoU21Third archivo) {
        return CodificadorHuffmanThird.decodificar(archivo, null);
    }
}
