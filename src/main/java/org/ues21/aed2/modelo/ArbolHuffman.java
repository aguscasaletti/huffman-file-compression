package org.ues21.aed2.modelo;

/**
 *
 * @author Agustín Aliaga
 */
public class ArbolHuffman {

    // Lista que contiene inicialmente en cada Nodo los caracteres de entrada y su frecuencia,
    // y (luego de que el arbol se construye) finalmente pasa a contener el NodoHuffman raíz.
    private ListaHuffman listaArbolHuffman = new ListaHuffman();

    // Lista que contiene en cada nodo un String array, donde el primer item es el símbolo de entrada y el segundo
    // es el código de CodificadorHuffman correspondiente.
    private ListaHuffman diccionarioHuffman = new ListaHuffman();

    private ProgressListener progressListener;

    private String texto;

    public ArbolHuffman(final String texto) {
        this.texto = texto;
        this.construirArbolHuffman(texto);
    }

    public ArbolHuffman(final String texto, ProgressListener listener){
        this.progressListener = listener;
        this.texto = texto;
        this.construirArbolHuffman(texto);
    }

    public String getTexto() {
        return texto;
    }

    private void construirArbolHuffman(final String texto) {
        for (char c : texto.toCharArray()) {
            NodoHuffman nodo = new NodoHuffman(Character.toString(c), 1, null, null);
            listaArbolHuffman.agregarOrdenado(nodo);
        }

        int pos1, pos2, frec = 0;
        Nodo p1, p2;
        NodoHuffman izq = null, der = null;
        int tam = listaArbolHuffman.getSize();
        int total = listaArbolHuffman.getSize();

        while (tam > 1) {
            pos1 = 1;
            pos2 = 2;
            p1 = listaArbolHuffman.get(pos1);
            p2 = listaArbolHuffman.get(pos2);

            if (this.progressListener != null) {
                int progress = (total - tam) * 100 / total;
                this.progressListener.onProgressUpdate(progress);
            }

            if (p1 != null) {
                izq = (NodoHuffman) p1.getInfo();
                frec = izq.getFrec();
            }
            if (p2 != null) {
                der = (NodoHuffman) p2.getInfo();
                frec += der.getFrec();
            }

            NodoHuffman nodo1 = new NodoHuffman(null, frec, izq, der);
            listaArbolHuffman.agregarOrdenado(nodo1);
            listaArbolHuffman.borrarPrimero();
            listaArbolHuffman.borrarPrimero();
            tam = listaArbolHuffman.getSize();


        }

        if (this.listaArbolHuffman.getFrente() != null) {
            NodoHuffman p = (NodoHuffman) this.listaArbolHuffman.getFrente().getInfo();
            crearDiccionario(p, this.diccionarioHuffman, "");
        }

        if (this.progressListener != null) {
            this.progressListener.onComplete();
        }
    }

    /**
     * Método que crea el diccionario de los símbolos finales a partir del nodo huffman raíz
     */
    private void crearDiccionario(NodoHuffman p, ListaHuffman diccionario, String huffmanCode) {
        if (p != null) {
            if (p.getIzq() == null && p.getDer() == null) {
                String[] vec = new String[2];
                vec[0] = p.getCaracter();
                vec[1] = huffmanCode;
                diccionario.agregar(vec);
            }
            crearDiccionario(p.getIzq(), diccionario, huffmanCode + "0");
            crearDiccionario(p.getDer(), diccionario, huffmanCode + "1");
        }
    }

    public ListaHuffman getListaArbolHuffman() {
        return listaArbolHuffman;
    }

    public ListaHuffman getDiccionarioHuffman() {
        return diccionarioHuffman;
    }

    public interface ProgressListener {
        void onProgressUpdate(long progress);

        void onComplete();

        void onError();
    }
}
