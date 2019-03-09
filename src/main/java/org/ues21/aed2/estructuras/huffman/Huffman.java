package org.ues21.aed2.estructuras.huffman;

import org.ues21.aed2.estructuras.lista.Nodo;
import org.ues21.aed2.estructuras.tablaHash.ItemTablaSimbolos;
import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;
import org.ues21.aed2.vista.ProgressListener;

/**
 *
 * @author Agustín Aliaga
 */
public class Huffman {

    private ListaHuffman listaArbolHuffman = new ListaHuffman();

//    private CharMap listaSimbolos = new CharMap();
    private TablaHashSimbolos listaSimbolos = new TablaHashSimbolos();

    private ProgressListener progressListener;

    private String texto;

    public Huffman(final String texto) {
        this.texto = texto;
        this.construirArbolHuffman();
    }

    public Huffman(final String texto, ProgressListener listener){
        this.progressListener = listener;
        this.texto = texto;
        this.construirArbolHuffman();
    }

    public String getTexto() {
        return texto;
    }

    private void construirArbolHuffman() {
        for (char c : this.texto.toCharArray()) {
            NodoHuffman nodo = new NodoHuffman(Character.toString(c), 1, null, null);
            listaArbolHuffman.agregarOrdenado(nodo);
        }

        int frec = 0;
        Nodo p1, p2;
        NodoHuffman izq = null, der = null;
        int tam = listaArbolHuffman.getSize();
        int total = listaArbolHuffman.getSize();

        while (tam > 1) {
            p1 = listaArbolHuffman.get(1);
            p2 = listaArbolHuffman.get(2);

            if (this.progressListener != null) {
                int progress = (total - tam) * 100 / total;
                this.progressListener.onProgressUpdate(progress);
            }

            if (p1 != null) {
                izq = (NodoHuffman) p1.getInfo();
                frec = izq.getFrec();
            }
            if (p2 != null) {
                der = (NodoHuffman) p2.getInfo();
                frec += der.getFrec();
            }

            NodoHuffman nodo1 = new NodoHuffman(null, frec, izq, der);
            listaArbolHuffman.agregarOrdenado(nodo1);
            listaArbolHuffman.borrarPrimero();
            listaArbolHuffman.borrarPrimero();
            tam = listaArbolHuffman.getSize();
        }
//
//        if (this.listaArbolHuffman.getFrente() != null) {
//            NodoHuffman p = (NodoHuffman) this.listaArbolHuffman.getFrente().getInfo();
//            crearTablaSimbolos(p, this.listaSimbolos, "");
//        }


        if (this.listaArbolHuffman.getFrente() != null) {
            NodoHuffman p = (NodoHuffman) this.listaArbolHuffman.getFrente().getInfo();
            crearTablaSimbolos(p, this.listaSimbolos, "");
        }

        if (this.progressListener != null) {
            this.progressListener.onComplete();
        }
    }

    /**
     * Método que crea la tabla de los símbolos finales a partir del nodo huffman raíz
     */
//    private void crearTablaSimbolos(NodoHuffman p, CharMap tablaSimbolos, String huffmanCode) {
//        if (p != null) {
//            if (p.getIzq() == null && p.getDer() == null) {
//                tablaSimbolos.insertar(p.getCaracter().charAt(0), huffmanCode);
//            }
//            crearTablaSimbolos(p.getIzq(), tablaSimbolos, huffmanCode + "0");
//            crearTablaSimbolos(p.getDer(), tablaSimbolos, huffmanCode + "1");
//        }
//    }
    private void crearTablaSimbolos(NodoHuffman p, TablaHashSimbolos tablaSimbolos, String huffmanCode) {
        if (p != null) {
            if (p.getIzq() == null && p.getDer() == null) {
                tablaSimbolos.insertar(p.getCaracter(), new ItemTablaSimbolos(p.getCaracter().charAt(0), huffmanCode));
            }
            crearTablaSimbolos(p.getIzq(), tablaSimbolos, huffmanCode + "0");
            crearTablaSimbolos(p.getDer(), tablaSimbolos, huffmanCode + "1");
        }
    }


    public ListaHuffman getListaArbolHuffman() {
        return listaArbolHuffman;
    }

//    public CharMap getListaSimbolos() {
//        return listaSimbolos;
//    }

    public TablaHashSimbolos getListaSimbolos() {
        return listaSimbolos;
    }
}
