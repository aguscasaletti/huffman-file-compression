package org.ues21.aed2.soporte.third;

import org.ues21.aed2.estructuras.arbol.NodoHuffman;
import org.ues21.aed2.estructuras.lista.ListaHuffman;
import org.ues21.aed2.estructuras.lista.Nodo;
import org.ues21.aed2.soporte.ProgressListener;

/**
 *
 * @author Agustín Aliaga
 */
public class ArbolHuffmanThird {

    private ListaHuffman listaArbolHuffman = new ListaHuffman();

    private CharMap listaSimbolos = new CharMap();

    private ProgressListener progressListener;

    private String texto;

    public ArbolHuffmanThird(final String texto) {
        this.texto = texto;
        this.construirArbolHuffman(texto);
    }

    public ArbolHuffmanThird(final String texto, ProgressListener listener){
        this.progressListener = listener;
        this.texto = texto;
        this.construirArbolHuffman(texto);
    }

    public String getTexto() {
        return texto;
    }

    private void construirArbolHuffman(final String texto) {
        for (char c : texto.toCharArray()) {
            NodoHuffman nodo = new NodoHuffman(Character.toString(c), 1, null, null);
            listaArbolHuffman.agregarOrdenado(nodo);
        }

        int pos1, pos2, frec = 0;
        Nodo p1, p2;
        NodoHuffman izq = null, der = null;
        int tam = listaArbolHuffman.getSize();
        int total = listaArbolHuffman.getSize();

        while (tam > 1) {
            pos1 = 1;
            pos2 = 2;
            p1 = listaArbolHuffman.get(pos1);
            p2 = listaArbolHuffman.get(pos2);

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
    private void crearTablaSimbolos(NodoHuffman p, CharMap tablaSimbolos, String huffmanCode) {
        if (p != null) {
            if (p.getIzq() == null && p.getDer() == null) {
                tablaSimbolos.insertar(p.getCaracter().charAt(0), huffmanCode);
            }
            crearTablaSimbolos(p.getIzq(), tablaSimbolos, huffmanCode + "0");
            crearTablaSimbolos(p.getDer(), tablaSimbolos, huffmanCode + "1");
        }
    }

    public ListaHuffman getListaArbolHuffman() {
        return listaArbolHuffman;
    }

    public CharMap getListaSimbolos() {
        return listaSimbolos;
    }
}
