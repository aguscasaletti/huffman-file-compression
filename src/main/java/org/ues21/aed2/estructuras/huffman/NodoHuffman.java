package org.ues21.aed2.estructuras.huffman;

/**
 *
 * @author Agustín Aliaga
 */
public class NodoHuffman implements Comparable<NodoHuffman> {

    private String caracter;
    private int frec;
    private NodoHuffman izq;
    private NodoHuffman der;

    public NodoHuffman(
        final String caracter,
        final int frec,
        final NodoHuffman izq,
        final NodoHuffman der
    ) {
        this.caracter = caracter;
        this.frec = frec;
        this.izq = izq;
        this.der = der;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public int getFrec() {
        return frec;
    }

    public void setFrec(int frec) {
        this.frec = frec;
    }

    public NodoHuffman getIzq() {
        return izq;
    }

    public void setIzq(NodoHuffman izq) {
        this.izq = izq;
    }

    public NodoHuffman getDer() {
        return der;
    }

    public void setDer(NodoHuffman der) {
        this.der = der;
    }

    public boolean esHoja() {
        return this.der == null && this.izq == null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
            getClass() == obj.getClass() &&
            (this == obj || this.caracter.equals(((NodoHuffman) obj).caracter));
    }

    @Override
    public int compareTo(NodoHuffman o) {
       return this.getFrec() - o.getFrec();
    }
}
