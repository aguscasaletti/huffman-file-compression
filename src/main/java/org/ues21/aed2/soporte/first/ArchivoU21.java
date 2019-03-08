package org.ues21.aed2.soporte.first;

import org.ues21.aed2.estructuras.lista.ListaHuffman;

public class ArchivoU21 {
    private String nombre;
    private String extension;
    private String codigo;
    private ListaHuffman tablaSimbolos;

    public ArchivoU21(String nombre, String extension, String codigo, ListaHuffman tablaSimbolos) {
        this.nombre = nombre;
        this.extension = extension;
        this.codigo = codigo;
        this.tablaSimbolos = tablaSimbolos;
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

    public ListaHuffman getTablaSimbolos() {
        return tablaSimbolos;
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

    public void setTablaSimbolos(ListaHuffman tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }
}
