/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.modelo;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;


/**
 *
 * @author agustin
 */
public class ArbolHuffman {

    private ListaHuffman listaArbolHuffman = new ListaHuffman();
    private ListaHuffman listaCodHuffman = new ListaHuffman();

    public void crearArbol(String texto) {
        char[] vectTexto = texto.toCharArray();
        char c;
        NodoHuffman nodo;
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            nodo = new NodoHuffman(Character.toString(c), 1, null, null);
            getListaArbolHuffman().agregarOrdenado(nodo);
        }

        int pos1, pos2, frec = 0;
        Nodo p1, p2;
        NodoHuffman izq = null, der = null;
        int tam = getListaArbolHuffman().getSize();
        while (tam > 1) {
            pos1 = 1;
            pos2 = 2;
            p1 = getListaArbolHuffman().get(pos1);
            p2 = getListaArbolHuffman().get(pos2);

            if (p1 != null) {
                izq = (NodoHuffman) p1.getInfo();
                frec = izq.getFrec();
            }
            if (p2 != null) {
                der = (NodoHuffman) p2.getInfo();
                frec += der.getFrec();
            }

            NodoHuffman nodo1 = new NodoHuffman(null, frec, izq, der);
            getListaArbolHuffman().agregar(nodo1);
            getListaArbolHuffman().borrarPrimero();
            getListaArbolHuffman().borrarPrimero();
            tam = getListaArbolHuffman().getSize();
        }

        if (this.listaArbolHuffman.getFrente() != null) {
            StringBuilder sbCod = new StringBuilder();
            NodoHuffman p = (NodoHuffman) this.listaArbolHuffman.getFrente().getInfo();
            crearListaHuffman(p, this.listaCodHuffman, sbCod);
            String codigo = caracterAcodigo(vectTexto);

            //El código generado como una cadena de caracteres.
            System.out.println("Código huffman generado como cadena de caracteres: ");
            System.out.println(codigo);
            BitSet bitset = new BitSet(codigo.length());

            char[] codVec = codigo.toCharArray();

            //Para pasar el codigo de bits en string a un bitset y 
            //poder manejar bytes directamente
            for (int i = 0; i < codVec.length; i++) {
                char d = codVec[i];
                if (d == '1') {
                    bitset.set(i);
                }
            }

            //Estos bytes son los que debo almacenar en el archivo 
            //de acceso aleatorio
            byte[] vectByte = bitset.toByteArray();

            //Yapa: Escritura de huffman en archivo de acceso aleatorio siguiendo
            //el formato de archivo requerido en el final. En este caso se considera
            //que el archivo original se denomina PRUEBCOM.txt
            try {
                RandomAccessFile rda = new RandomAccessFile("/home/agustin/Escritorio/pruebaHuffman.u21", "rw");
                rda.writeChar('h');
                rda.writeChar('u');
                rda.writeChar('f');
                rda.writeChar('t');
                rda.writeChar('x');
                rda.writeChar('t');
                rda.writeChar('P');
                rda.writeChar('R');
                rda.writeChar('U');
                rda.writeChar('E');
                rda.writeChar('B');
                rda.writeChar('C');
                rda.writeChar('O');
                rda.writeChar('M');
                rda.writeInt(vectByte.length);
                rda.write(vectByte, 0, vectByte.length);

                rda.close();

            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }

            //Yapa: Lectura de huffman en archivo de acceso aleatorio siguiendo
            //el formato de archivo requerido en el final.
            try {
                RandomAccessFile rda = new RandomAccessFile("/home/agustin/Escritorio/pruebaHuffman.u21", "r");
                //El identificador del archivo es: huf
                StringBuilder sbId = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                    sbId.append(rda.readChar());
                }

                //La extension del archivo comprimido es (en este caso): txt
                StringBuilder sbExt = new StringBuilder();
                for (int i = 3; i < 6; i++) {
                    sbExt.append(rda.readChar());
                }
                
                //El nombre del archivo original es (en este caso): PRUEBCOM
                StringBuilder sbNombre = new StringBuilder();
                for (int i = 6; i < 14; i++) {
                    sbNombre.append(rda.readChar());
                }
                
                int largoBits = rda.readInt();

                byte b;
                StringBuilder sbTiraBit = new StringBuilder();
                try {
                    System.out.println("Código leido desde archivo de acceso aleatorio: ");
                    while (true) {
                        b = rda.readByte();
                        BitSet bitset1 = new BitSet(8);
                        //Tener en cuenta que si los ultimos Bits del código no llegan 
                        //a 8 se completan con ceros
                        for (int j = 0; j < 8; j++) {
                            if ((b & (1 << j)) > 0) {
                                bitset1.set(j);
                            }
                        }

                        for (int j = 0; j < 8; j++) {
                            if (bitset1.get(j)) {
                                sbTiraBit.append("1");
                            } else {
                                sbTiraBit.append("0");
                            }
                        }

                    }
                } catch (EOFException e) {
                    rda.close();
                    System.out.println(sbTiraBit.toString()); 
                }

            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

    }

    private void crearListaHuffman(NodoHuffman p, ListaHuffman listaCodHuffman, StringBuilder sbCod) {
        if (p != null) {
            if (p.getIzq() == null && p.getDer() == null) {
                String[] vec = new String[2];
                vec[0] = p.getCaracter();
                vec[1] = sbCod.toString();
                sbCod.delete(0, sbCod.capacity());
                listaCodHuffman.agregar(vec);
            }
            crearListaHuffman(p.getIzq(), listaCodHuffman, sbCod.append("0"));
            crearListaHuffman(p.getDer(), listaCodHuffman, sbCod.append("1"));
        }
    }

    private String caracterAcodigo(char[] vectTexto) {
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vectTexto.length; i++) {
            c = vectTexto[i];
            Nodo nodo = listaCodHuffman.buscar(Character.toString(c));
            if (nodo != null) {
                String[] vec = (String[]) nodo.getInfo();
                sbCod.append(vec[1]);
            }
        }
        return sbCod.toString();
    }


    /**
     * @return the listaArbolHuffman
     */
    public ListaHuffman getListaArbolHuffman() {
        return listaArbolHuffman;
    }

    /**
     * @param listaArbolHuffman the listaArbolHuffman to set
     */
    public void setListaArbolHuffman(ListaHuffman listaArbolHuffman) {
        this.listaArbolHuffman = listaArbolHuffman;
    }

    /**
     * @return the listaCodHuffman
     */
    public ListaHuffman getListaCodHuffman() {
        return listaCodHuffman;
    }

    /**
     * @param listaCodHuffman the listaCodHuffman to set
     */
    public void setListaCodHuffman(ListaHuffman listaCodHuffman) {
        this.listaCodHuffman = listaCodHuffman;
    }
}
