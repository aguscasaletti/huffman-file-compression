/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.estructuras.lista;

import org.ues21.aed2.estructuras.huffman.NodoHuffman;

/**
 *
 * @author Agustín Aliaga
 */
public class ListaHuffman {

    private Nodo frente = null;
    private int size = 0;

    /**
     * Método que permite agregar nodos al final de la lista sin repetidos
     *
     * @param <T>
     * @param info El valor del nodo a ser insertado
     */
    public <T> void agregarOrdenado(final T info) {
        Nodo actual = this.getFrente();
        Nodo anterior = null;
        boolean found = false;
        Nodo nuevo = new Nodo(info, null);

        if (actual == null) {
            this.setFrente(nuevo);
            this.setSize(this.getSize() + 1);
            return;
        }

        Nodo insert = null;

        while (actual != null) {
            if (((NodoHuffman) info).getCaracter() != null &&
                    ((NodoHuffman) info).getCaracter().equals(((NodoHuffman) actual.getInfo()).getCaracter())) {
                found = true;
                ((NodoHuffman) actual.getInfo()).setFrec(((NodoHuffman) actual.getInfo()).getFrec() + 1);
                while (actual.getSiguiente() != null &&
                        ((NodoHuffman) actual.getInfo()).getFrec()
                                > ((NodoHuffman)actual.getSiguiente().getInfo()).getFrec()) {
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

            if (((NodoHuffman) actual.getInfo()).getFrec() < ((NodoHuffman) info).getFrec()) {
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

            this.setSize(this.getSize() + 1);
        }

    }

    public <T>T get(int pos) {
        Nodo p = this.getFrente();
        int i = 1;
        while (p != null) {
            if (i == pos) {
                return (T) p;
            }
            p = p.getSiguiente();
            i++;
        }
        return null;
    }


    public <T>T borrarPrimero() {
        if (this.getFrente() != null) {
            Nodo p = this.getFrente();
            T y =  (T) p.getInfo();
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
    public Nodo getFrente() {
        return frente;
    }

    /**
     * @param frente the frente to set
     */
    public void setFrente(Nodo frente) {
        this.frente = frente;
    }

}
