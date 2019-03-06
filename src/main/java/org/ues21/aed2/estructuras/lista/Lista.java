package org.ues21.aed2.estructuras.lista;

public class Lista {

    private Nodo frente;

    public Lista(Nodo frente) {
        this.frente = frente;
    }

    public Lista() {

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
    }

    public Nodo getFrente() {
        return frente;
    }

    public void setFrente(Nodo frente) {
        this.frente = frente;
    }
}
