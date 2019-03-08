package org.ues21.aed2.estructuras.map;

import org.ues21.aed2.estructuras.lista.Lista;
import org.ues21.aed2.estructuras.lista.Nodo;

public class TablaHash<T> {

    private int m = 200; // Tamaño del mapa
    private int size = 0; // Cantidad de elementos en el mapa (nodos)
    private Lista[] map;

    public TablaHash() {
        this.map = new Lista[this.m];
    }

    public void insertar(String key,T value) {
        int hash = hash(key);
        Nodo nodo = new Nodo(value, null);

        if (this.map[hash] == null) {
            Lista l = new Lista(nodo);
            this.map[hash] = l;
        } else {
            this.map[hash].insertarUltimo(nodo);
        }

        this.size ++;
    }

    public void get(String key) {
        int hash = hash(key);
    }

    /**
     * *
     * Hash usando aritmetica modular
     *
     */
    public int hash(String k) {
        String [] vecK = k.split("-");
        char [] vecChars = vecK[0].toCharArray();
        int acum = 0;
        for (char vecChar : vecChars) {
            acum += vecChar;
        }
        int nro = Integer.parseInt(vecK[1]);
        return (acum * nro) % this.m;
    }
}
