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

    public static String decodificar(final U21File archivo) {
        char[] vectTexto = archivo.getCodigo().toCharArray();
        StringBuilder sbMensaje = new StringBuilder();
        StringBuilder sbBusqueda = new StringBuilder();

        for (int i = 0; i < vectTexto.length; i++) {
            sbBusqueda.append(vectTexto[i]);
            Nodo nodo = archivo.getDiccionario().buscarCaracter(sbBusqueda.toString());
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbMensaje.append(vec[0]);
                sbBusqueda = new StringBuilder();
            }
        }
        return sbMensaje.toString();
    }
}
