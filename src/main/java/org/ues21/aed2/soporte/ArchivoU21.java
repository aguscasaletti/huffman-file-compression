package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;

/**
 * Representación de un archivo .u21
 * Se utiliza al leer el archivo comprimido, para construír un objeto manejable por el programa.
 */
public class ArchivoU21 {
    private int largoNombre;
    private String nombre;
    private String codigo;
    private TablaHashSimbolos map;

    public ArchivoU21(int largoNombre, String nombre, String codigo, TablaHashSimbolos map) {
        this.largoNombre = largoNombre;
        this.nombre = nombre;
        this.codigo = codigo;
        this.map = map;
    }

    public int getLargoNombre() {
        return largoNombre;
    }

    public void setLargoNombre(int largoNombre) {
        this.largoNombre = largoNombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public TablaHashSimbolos getTablaSimbolos() {
        return map;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTablaSimbolos(TablaHashSimbolos arbolHuffman) {
        this.map = arbolHuffman;
    }
}
