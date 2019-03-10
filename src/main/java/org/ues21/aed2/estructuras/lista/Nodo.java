package org.ues21.aed2.estructuras.lista;

/**
 *
 * Nodo para utilizar en lista enlazada
 *
 * @author Agustín Aliaga
 */
public class Nodo<T> {
    
    private T info;
    private Nodo<T> siguiente;

    public Nodo(T info, Nodo siguiente) {
        this.info = info;
        this.siguiente = siguiente;
    }

    public T getInfo() {
        return info;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

}
