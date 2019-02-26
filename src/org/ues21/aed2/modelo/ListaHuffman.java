/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.modelo;

import org.ues21.aed2.soporte.ExcepcionNoEncontrado;

/**
 *
 * @author agustin
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
    public <T> void agregarOrdenado(T info) {
        Nodo p = this.getFrente();
        Nodo q = null;
        boolean band = false;
        NodoHuffman nodo;
        Nodo nvo = new Nodo(info, null);
        if (p == null) {
            this.setFrente(nvo);
            this.setSize(this.getSize() + 1);
        } else {
            while (p != null) {
                nodo = (NodoHuffman) p.getInfo();
                if (((NodoHuffman) info).getFrec() <= nodo.getFrec()) {
                    if (nodo.getCaracter().equals(((NodoHuffman) info).getCaracter())) {
                        band = true;
                        nodo.setFrec(nodo.getFrec() + 1);
                        break;
                    }
                } else {
                    break;
                }
                q = p;
                p = p.getSiguiente();
            }

            if (q != null && !band) {
                if (p == null) {
                    q.setSiguiente(nvo);
                } else {
                    //if (((NodoHuffman) info).getFrec() < nodo.getFrec()) {
                    q.setSiguiente(nvo);
                    nvo.setSiguiente(p);
                    //}
                }
                this.setSize(this.getSize() + 1);
            }

        }

    }

    /**
     * Método que permite agregar nodos al final de una lista.
     *
     * @param <T>
     * @param info El valor info del nodo
     */
    public <T> void agregar(T info) {
        Nodo p = this.getFrente();
        Nodo q = null;
        while (p != null) {
            q = p;
            p = p.getSiguiente();
        }

        Nodo nvo = new Nodo(info, null);
        if (q != null) {
            q.setSiguiente(nvo);
        } else {
            this.setFrente(nvo);
        }
        this.setSize(this.getSize() + 1);

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
            p = null;
            
            this.size--;
            return y;
        } else {
            return null;
        }

    }

    /**
     * Método que muestra el contenido de la lista
     */
    public void mostrar() {
        Nodo p = this.getFrente();

        while (p != null) {
            System.out.println("Nodo: " + p.getInfo());
            p = p.getSiguiente();
        }
    }

    public void borrar(int info) {
        Nodo p = this.getFrente();
        Nodo q = null;

        while (p != null) {
            if (((int) p.getInfo()) == info) {
                break;
            }
            q = p;
            p = p.getSiguiente();
        }

        if (p != null) {
            if (q != null) {
                q.setSiguiente(p.getSiguiente());
                p.setSiguiente(null);
                p = null;
            } else {
                // es el frente
                this.setFrente(p.getSiguiente());
                p.setSiguiente(null);
                p = null;
            }
        }
    }
    
    public Nodo buscar(String caracter) {
        Nodo p = this.getFrente();
        while (p != null) {
            String[] vec = (String[]) p.getInfo();
            if (caracter.equals(vec[0])) {
                return p;
            }
            p = p.getSiguiente();
        }
        return p;
    }


    /**
     * Método que permite buscar un nodo e insertar uno nuevo si el nodo buscado
     * existe y sino avisar por pantalla en caso contrario.
     *
     * @param info La info con el nodo a insertar si existe
     */
    public void buscarInsertar(int info) {
        try {
            Nodo[] pq = buscar(info);
            Nodo p = pq[0];
            Nodo q = pq[1];
            Nodo nvo = new Nodo(info, p);
            q.setSiguiente(nvo);
        } catch (ExcepcionNoEncontrado ex) {
            System.err.println("Nodo no Encontrado!!!");
        }

    }

    /**
     * Método que permite buscar un nodo cuya info es pasada como parametro.
     * Devuelve un vector con los punteros p y q en caso de exito y una
     * execpción en caso contrario.
     *
     * @param info La info del nodo buscado.
     * @return Devuelve un vector con los punteros p y q en caso de exito y una
     * execpción en caso contrario.
     * @throws ExcepcionNoEncontrado La excepción disparada si el nodo no se
     * encuentra.
     */
    public Nodo[] buscar(int info) throws ExcepcionNoEncontrado {
        Nodo p = this.getFrente();
        Nodo q = null;
        while (p != null) {
            if (((int) p.getInfo()) == info) {
                Nodo[] nodos = new Nodo[2];
                nodos[0] = p;
                nodos[1] = q;
                return nodos;
            }
            q = p;
            p = p.getSiguiente();
        }
        throw new ExcepcionNoEncontrado("Nodo no encontrado!!");
    }


    /**
     * Método que permite agregar un nodo al principio de la lista
     *
     * @param info
     */
    public void insertarPrimero(int info) {
        Nodo p = new Nodo(info, this.getFrente());
        this.setFrente(p);
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
