/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.modelo;

/**
 *
 * @author agustin
 */
public class NodoHuffman implements Comparable<NodoHuffman> {

    private String caracter;
    private int frec;
    private NodoHuffman izq;
    private NodoHuffman der;

    public NodoHuffman(String caracter, int frec, NodoHuffman izq, NodoHuffman der) {
        this.caracter = caracter;
        this.frec = frec;
        this.izq = izq;
        this.der = der;
    }

    /**
     * @return the frec
     */
    public int getFrec() {
        return frec;
    }

    /**
     * @param frec the frec to set
     */
    public void setFrec(int frec) {
        this.frec = frec;
    }

    /**
     * @return the izq
     */
    public NodoHuffman getIzq() {
        return izq;
    }

    /**
     * @param izq the izq to set
     */
    public void setIzq(NodoHuffman izq) {
        this.izq = izq;
    }

    /**
     * @return the der
     */
    public NodoHuffman getDer() {
        return der;
    }

    /**
     * @param der the der to set
     */
    public void setDer(NodoHuffman der) {
        this.der = der;
    }

    /**
     * @return the caracter
     */
    public String getCaracter() {
        return caracter;
    }

    /**
     * @param caracter the caracter to set
     */
    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodoHuffman other = (NodoHuffman) obj;
        return this.caracter == other.caracter;
    }

    @Override
    public int compareTo(NodoHuffman o) {
       return this.getFrec()- o.getFrec();
    }

}
