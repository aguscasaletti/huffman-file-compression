package org.ues21.aed2.estructuras.lista;

public class ListaEnlazada {

    private Nodo frente;
    private int size;

    public ListaEnlazada(Nodo frente) {
        this.frente = frente;
    }

    public void insertarUltimo(Nodo nodo) {
        if (this.frente == null) {
            this.frente = nodo;
            return;
        }

        Nodo aux = this.frente;
        while (aux.getSiguiente() != null) {
            aux = aux.getSiguiente();
        }

        aux.setSiguiente(nodo);
        size ++;
    }

    public Nodo getFrente() {
        return frente;
    }

    public void setFrente(Nodo frente) {
        this.frente = frente;
    }

    public int getSize() {
        return size;
    }
}
