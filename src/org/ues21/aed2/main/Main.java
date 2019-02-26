/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.main;

import org.ues21.aed2.modelo.ArbolHuffman;


/**
 *
 * @author agustin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ArbolHuffman arbol = new ArbolHuffman();
        arbol.crearArbol("hola que tal como estas, espero que bien cuando comprimas con Huffman");

    }
    
}
