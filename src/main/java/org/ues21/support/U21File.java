package org.ues21.support;

import org.ues21.dataStructures.hashTable.SymbolsHashTable;

/**
 * Object representation of the .u21 file
 */
public class U21File {
    private int nameLength;
    private String name;
    private String code;
    private SymbolsHashTable map;

    public U21File(int nameLength, String name, String code, SymbolsHashTable map) {
        this.nameLength = nameLength;
        this.name = name;
        this.code = code;
        this.map = map;
    }

    public int getNameLength() {
        return nameLength;
    }

    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public SymbolsHashTable getTablaSimbolos() {
        return map;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTablaSimbolos(SymbolsHashTable arbolHuffman) {
        this.map = arbolHuffman;
    }
}
