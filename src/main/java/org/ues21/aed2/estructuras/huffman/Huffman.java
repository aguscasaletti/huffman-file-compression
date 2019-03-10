package org.ues21.aed2.estructuras.huffman;

import org.ues21.aed2.estructuras.lista.Nodo;
import org.ues21.aed2.estructuras.tablaHash.ItemTablaSimbolos;
import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;
import org.ues21.aed2.vista.ProgressListener;

/**
 *
 * @author Agustín Aliaga
 *
 * Clase que se encarga de la construcción del árbol Huffman
 *
 */
public class Huffman {

    private ListaHuffman listaArbolHuffman = new ListaHuffman();

    private TablaHashSimbolos tablaSimbolos = new TablaHashSimbolos();

    private ProgressListener progressListener;

    private String texto;

    /**
     * @param texto el mensaje inicial (texto a comprimir)
     */
    public Huffman(final String texto) {
        this.texto = texto;
        this.construirArbolHuffman();
    }

    /**
     * @param texto el mensaje inicial (texto a comprimir)
     * @param listener el listener de progreso
     */
    public Huffman(final String texto, ProgressListener listener){
        this.progressListener = listener;
        this.texto = texto;
        this.construirArbolHuffman();
    }

    public String getTexto() {
        return texto;
    }

    /**
     * Método que se encarga de construír el árbol de Huffman
     */
    private void construirArbolHuffman() {
        // Agregar los caracteres a la lista (y calcular la frecuencia de cada uno)
        for (char c : this.texto.toCharArray()) {
            NodoHuffman nodo = new NodoHuffman(Character.toString(c), 1, null, null);
            listaArbolHuffman.agregarOrdenado(nodo);
        }

        int frec = 0;
        Nodo p1, p2;
        NodoHuffman izq = null, der = null;
        int tam = listaArbolHuffman.getSize();
        int total = listaArbolHuffman.getSize();

        // Construír árbol tomando siempre los dos elementos mas pequeños (menor frecuencia) de forma iterativa.
        while (tam > 1) {
            p1 = listaArbolHuffman.get(1);
            p2 = listaArbolHuffman.get(2);

            // Notificar progreso
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

        // Cuando el árbol fue creado, crear ahora la tabla de símbolos
        if (this.listaArbolHuffman.getFrente() != null) {
            NodoHuffman p = (NodoHuffman) this.listaArbolHuffman.getFrente().getInfo();
            crearTablaSimbolos(p, this.tablaSimbolos, "");
        }

        // Notificar finalización del progreso
        if (this.progressListener != null) {
            this.progressListener.onComplete();
        }
    }

    /**
     * Método (recursivo) para crear la tabla de símbolos
     */
    private void crearTablaSimbolos(NodoHuffman p, TablaHashSimbolos tablaSimbolos, String huffmanCode) {
        if (p != null) {
            if (p.getIzq() == null && p.getDer() == null) {
                tablaSimbolos.insertar(p.getCaracter(), new ItemTablaSimbolos(p.getCaracter().charAt(0), huffmanCode));
            }
            crearTablaSimbolos(p.getIzq(), tablaSimbolos, huffmanCode + "0");
            crearTablaSimbolos(p.getDer(), tablaSimbolos, huffmanCode + "1");
        }
    }

    public TablaHashSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }
}
