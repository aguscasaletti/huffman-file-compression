package org.ues21.aed2.estructuras.tablaHash;

import org.ues21.aed2.estructuras.lista.ListaEnlazada;
import org.ues21.aed2.estructuras.lista.Nodo;

import java.util.Iterator;

public class TablaHashSimbolos implements Iterable<ItemTablaSimbolos> {

    private int m = 500; // Tamaño del mapa
    private int size = 0; // Cantidad de elementos en el mapa (nodos)
    private ListaEnlazada[] map;

    public TablaHashSimbolos() {
        this.map = new ListaEnlazada[this.m];
    }

    public void insertar(String key, ItemTablaSimbolos value) {
        int hash = hash(key);
        Nodo<ItemTablaSimbolos> nodo = new Nodo<>(value, null);

        if (this.map[hash] == null) {
            ListaEnlazada l = new ListaEnlazada(nodo);
            this.map[hash] = l;
        } else {
            this.map[hash].insertarUltimo(nodo);
        }

        this.size ++;
    }

    public ItemTablaSimbolos findSimbolo(String simbolo) {
        int hash = hash(simbolo);

        if (this.map[hash] == null) {
            return null;
        }

        Nodo nodo = this.map[hash].getFrente();
        while (nodo != null) {
            if (String.valueOf(((ItemTablaSimbolos)nodo.getInfo()).getSimbolo()).equals(simbolo)) {
                return (ItemTablaSimbolos) nodo.getInfo();
            }

            nodo = nodo.getSiguiente();
        }

        return null;
    }


    public ItemTablaSimbolos findCodigo(String codigo) {
        int hash = hash(codigo);

        if (this.map[hash] == null) {
            return null;
        }

        Nodo nodo = this.map[hash].getFrente();
        while (nodo != null) {
            if (String.valueOf(((ItemTablaSimbolos)nodo.getInfo()).getCodigo()).equals(codigo)) {
                return (ItemTablaSimbolos) nodo.getInfo();
            }

            nodo = nodo.getSiguiente();
        }

        return null;
    }

    private int hash(String k) {
        return Math.abs(k.hashCode() % this.m);
    }

    public int getSize() {
        return size;
    }

    public ListaEnlazada[] getMap() {
        return map;
    }

    @Override
    public Iterator<ItemTablaSimbolos> iterator() {
        return new Iterator<ItemTablaSimbolos>() {
            Nodo<ItemTablaSimbolos> current;
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                int index = currentIndex;
                if (current == null) {
                    return size != 0;
                }

                if (current.getSiguiente() != null) {
                    return true;
                }

                while (index < map.length - 1) {
                    index++;
                    if (map[index] != null) {
                        return map[index].getFrente().getInfo() != null;
                    }
                }

                return false;
            }

            @Override
            public ItemTablaSimbolos next() {
                if (current == null) {
                    for (int i = 0; i < map.length; i++) {
                        if (map[i] != null) {
                            currentIndex = i;
                            current = map[i].getFrente();
                            return current.getInfo();
                        }
                    }
                }

                if (current.getSiguiente() != null) {
                    current = current.getSiguiente();
                    return current.getInfo();
                }

                while (currentIndex < map.length - 1) {
                    currentIndex ++;
                    try {
                        if (map[currentIndex] != null) {
                            current = map[currentIndex].getFrente();
                            return current.getInfo();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                return null;
            }
        };
    }
}
