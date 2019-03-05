package org.ues21.aed2.modelo;

import org.ues21.aed2.modelo.NodoHuffman;
import org.ues21.aed2.soporte.U21File;


public class CodificadorHuffman {

    public static String codificar(final ListaHuffman diccionario, final String mensaje) {
        char[] vectTexto = mensaje.toCharArray();
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            Nodo nodo = diccionario.buscar(Character.toString(c));
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbCod.append(vec[1]);
            }
        }
        return sbCod.toString();
    }

    public static String decodificar(final U21File archivo, final ArbolHuffman.ProgressListener progressListener) {
        char[] vectTexto = archivo.getCodigo().toCharArray();
        StringBuilder sbMensaje = new StringBuilder();
        StringBuilder sbBusqueda = new StringBuilder();

        for (long i = 0; i < vectTexto.length; i++) {
            if (progressListener != null && i > 100 && i % 100 == 0) {
                progressListener.onProgressUpdate(i * 100 / vectTexto.length);
            }
            sbBusqueda.append(vectTexto[(int) i]);
            Nodo nodo = archivo.getDiccionario().buscarCaracter(sbBusqueda.toString());
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbMensaje.append(vec[0]);
                sbBusqueda = new StringBuilder();
            }
        }

        if (progressListener != null) {
            progressListener.onComplete();
        }
        return sbMensaje.toString();
    }

    public static String decodificar(final U21File archivo) {
        return CodificadorHuffman.decodificar(archivo, null);
    }
}
