package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.arbol.avl.AVLTree;

public class ArchivoU21 {
    private String nombre;
    private String extension;
    private String codigo;
    private AVLTree avlTree;

    public ArchivoU21(String nombre, String extension, String codigo, AVLTree avlTree) {
        this.nombre = nombre;
        this.extension = extension;
        this.codigo = codigo;
        this.avlTree = avlTree;
    }

    public String getNombre() {
        return nombre;
    }

    public String getExtension() {
        return extension;
    }

    public String getCodigo() {
        return codigo;
    }

    public AVLTree getTablaSimbolos() {
        return avlTree;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTablaSimbolos(AVLTree arbolHuffman) {
        this.avlTree = arbolHuffman;
    }
}
