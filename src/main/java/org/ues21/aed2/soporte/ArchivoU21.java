package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;

/**
 * Representación de un archivo .u21
 * Se utiliza al leer el archivo comprimido, para construír un objeto manejable por el programa.
 */
public class ArchivoU21 {
    private String nombre;
    private String extension;
    private String codigo;
    private TablaHashSimbolos map;

    public ArchivoU21(String nombre, String extension, String codigo, TablaHashSimbolos map) {
        this.nombre = nombre;
        this.extension = extension;
        this.codigo = codigo;
        this.map = map;
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

    public TablaHashSimbolos getTablaSimbolos() {
        return map;
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

    public void setTablaSimbolos(TablaHashSimbolos arbolHuffman) {
        this.map = arbolHuffman;
    }
}
