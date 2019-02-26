/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.modelo;

/**
 *
 * @author agustin
 * @param <T>
 */
public class Nodo<T> {
    
    private T info;
    private Nodo siguiente;

    public Nodo(T info, Nodo siguiente) {
        this.info = info;
        this.siguiente = siguiente;
    }

    
    /**
     * @return the info
     */
    public T getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(T info) {
        this.info = info;
    }

    /**
     * @return the siguiente
     */
    public Nodo getSiguiente() {
        return siguiente;
    }

    /**
     * @param siguiente the siguiente to set
     */
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
    
    
    
    
}
