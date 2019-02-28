package org.ues21.aed2.soporte;

import org.ues21.aed2.modelo.ListaHuffman;

public class U21File {
    private String nombre;
    private String extension;
    private String codigo;
    private ListaHuffman diccionario;

    public U21File(String nombre, String extension, String codigo, ListaHuffman diccionario) {
        this.nombre = nombre;
        this.extension = extension;
        this.codigo = codigo;
        this.diccionario = diccionario;
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

    public ListaHuffman getDiccionario() {
        return diccionario;
    }
}
