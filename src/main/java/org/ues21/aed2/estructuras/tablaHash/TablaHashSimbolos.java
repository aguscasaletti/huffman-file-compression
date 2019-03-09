package org.ues21.aed2.estructuras.tablaHash;

import org.ues21.aed2.estructuras.lista.ListaEnlazada;
import org.ues21.aed2.estructuras.lista.Nodo;

import java.util.Iterator;

/**
 * Implementación de un HashTable para los códigos del árbol de huffman.
 * Los valores que guarda son de tipo ItemTablaSimbolos.
 *
 * Mejora 1
 * - Se utiliza esta implementación de Tabla Hash en vez de la lista enlazada original para optimizar
 *   los tiempos de búsqueda y de inserción al momento de codificar o decodificar un mensaje.
 *   En una lista enlazada el peor caso será siempre O(n), y en HashTable nos aproximamos a O(1) para alfabetos chicos.
 *   La mejora de performance se nota sobre todo en la decodificación, donde para cada grupo de bits que se va leyendo
 *   se debe buscar el caracter original.
 * - Para la función de hash se usa la función hashCode() de String y se usa aritmética modular para evitar desbordamientos en el array.
 * - Se usa una tabla hash con direccionamiento cerrado (se utilizan Listas para resolver colisiones)
 * - Se implementa además la posibilidad de iterar sobre los elementos de la tabla hash (por ejemplo mediante un for each),
 *   mediante el uso de la interfaz Iterable. Esto es especialmente útil cuando queremos escribir los códigos a un archivo.
 *
 * En esta hash table se puede usar cualquier String como key. Se puede usar como key el "Símbolo" o el "Código",
 * pero se debe tener noción de cual se está usando para luego llamar al find correspondiente (findSimbolo o findCodigo)
 *
 */
public class TablaHashSimbolos implements Iterable<ItemTablaSimbolos> {

    private int m = 500; // Tamaño del mapa (aproximadamente el doble de cantidad de caracteres ASCII extendido)
    private int size = 0; // Cantidad de elementos en el mapa (nodos)
    private ListaEnlazada[] map;

    public TablaHashSimbolos() {
        this.map = new ListaEnlazada[this.m];
    }

    /**
     * Insertar una entrada de tabla de símbolos para un key dado.
     *
     * @param key la key de la tabla. Puede ser símbolo original o código huffman.
     * @param value la entrada de la tabla
     */
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

    /**
     * Obtener la entrada de tabla para un símbolo dado.
     *
     * @param simbolo el símbolo original / caracter original
     * @return la entrada de tabla correspondiente
     */
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


    /**
     * Obtener la entrada de tabla para un código huffman dado.
     *
     * @param codigo el código Huffman
     * @return la entrada de tabla correspondiente
     */
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

    /**
     * Función de hash
     * @param k input
     * @return el hash nativo de String módulo m
     */
    private int hash(String k) {
        return Math.abs(k.hashCode() % this.m);
    }

    public int getSize() {
        return size;
    }

    /**
     * Implementación de iterable
     */
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
