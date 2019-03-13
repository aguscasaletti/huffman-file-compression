package org.ues21.dataStructures.hashTable;

/**
 * Clase que representa un item en la tabla de símbolos donde:
 * - symbol = caracter original
 * - huffmanCode = código obtenido desde el árbol de Huffman
 */
public class SymbolsTableItem implements Comparable<SymbolsTableItem>{
    private char symbol;
    private String huffmanCode;

    public SymbolsTableItem(char symbol, String huffmanCode) {
        this.symbol = symbol;
        this.huffmanCode = huffmanCode;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getHuffmanCode() {
        return huffmanCode;
    }

    public void setHuffmanCode(String huffmanCode) {
        this.huffmanCode = huffmanCode;
    }

    @Override
    public int compareTo(SymbolsTableItem o) {
        return this.huffmanCode.compareTo(o.huffmanCode);
    }

}
