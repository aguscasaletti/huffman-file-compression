package org.ues21.aed2.estructuras.huffman;
import org.ues21.aed2.estructuras.lista.Nodo;

/**
 * @author Agustín Aliaga
 *
 * Clase que implementa una lista simplemente enlazada utilizada para construir el árbol de Huffman.
 * Para ello usamos esta Lista como una Cola de Prioridad (Priority Queue).
 *
 */
public class ListaHuffman {

    private Nodo frente = null;
    private int size = 0;

    /**
     *
     * Función que agrega un caracter a la lista de manera ordenada. Si se encuentra, se incrementa la frecuencia del mismo.
     *
     * Fixes:
     *
     * - Cuando se encontraba el caracter dentro de la lista y se incrementaba su frecuencia,
     *  no se acomodaba el nodo de manera tal que quedara ordenada la lista. Como consecuencia de esto,
     *  el árbol generado no era correcto. Ahora, cuando eso sucede, movemos el nodo hacia adelante hasta ubicarlo
     *  en su lugar correspondiente.
     *
     * - Al utilizar esta clase solo para la construcción del Arbol de Huffman no es necesario parametrizarla con Generics
     *   y hacer tanto downcasting en este método.
     *
     */
    public void agregarOrdenado(final NodoHuffman info) {
        Nodo<NodoHuffman> actual = this.getFrente();
        Nodo anterior = null;
        boolean found = false;
        Nodo nuevo = new Nodo(info, null);

        if (actual == null) {
            this.setFrente(nuevo);
            this.size ++;
            return;
        }

        Nodo insert = null;

        while (actual != null) {
            if (info.getCaracter() != null &&
                    (info.getCaracter().equals(actual.getInfo().getCaracter()))) {
                found = true;
                actual.getInfo().setFrec(actual.getInfo().getFrec() + 1);
                while (actual.getSiguiente() != null &&
                        actual.getInfo().getFrec()
                                > (actual.getSiguiente().getInfo()).getFrec()) {
                    Nodo aux = actual.getSiguiente();
                    if (anterior != null) {
                        anterior.setSiguiente(aux);
                    } else {
                        // Es el primer nodo
                        this.frente = aux;
                    }
                    actual.setSiguiente(aux.getSiguiente());
                    aux.setSiguiente(actual);
                    anterior = aux;
                }
                break;
            }

            if (actual.getInfo().getFrec() < info.getFrec()) {
                insert = actual;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        }

        if (!found) {
            if (insert == null) {
                // Es el primer Nodo a insertar
                nuevo.setSiguiente(this.frente);
                this.frente = nuevo;
            } else {
                Nodo aux = insert.getSiguiente();
                insert.setSiguiente(nuevo);
                nuevo.setSiguiente(aux);
            }

            this.size ++;
        }

    }

    public Nodo get(int pos) {
        Nodo p = this.getFrente();
        int i = 1;
        while (p != null) {
            if (i == pos) {
                return p;
            }
            p = p.getSiguiente();
            i++;
        }
        return null;
    }


    public NodoHuffman borrarPrimero() {
        if (this.getFrente() != null) {
            Nodo<NodoHuffman> p = this.getFrente();
            NodoHuffman y =  p.getInfo();
            this.setFrente(this.getFrente().getSiguiente());
            p.setSiguiente(null);

            this.size --;
            return y;
        } else {
            return null;
        }

    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the frente
     */
    public Nodo<NodoHuffman> getFrente() {
        return frente;
    }

    /**
     * @param frente the frente to set
     */
    public void setFrente(Nodo frente) {
        this.frente = frente;
    }

}
