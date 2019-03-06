package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.lista.ListaHuffman;
import org.ues21.aed2.estructuras.lista.Nodo;

public class CodificadorHuffman {

    public static String codificar(final ListaHuffman tablaSimbolos, final String mensaje) {
        char[] vectTexto = mensaje.toCharArray();
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            Nodo nodo = tablaSimbolos.buscar(Character.toString(c));
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbCod.append(vec[1]);
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
            Nodo nodo = archivo.getTablaSimbolos().buscarCaracter(sbBusqueda.toString());
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbMensaje.append(vec[0]);
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
