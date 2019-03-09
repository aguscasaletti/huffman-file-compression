package org.ues21.aed2.estructuras.map;

public class CharMap {
    private String[] map = new String[256];
    private int size = 0;

    public void insertar(char simbolo, String codigo) {
        try {
            map[simbolo] = codigo;
            size ++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("El caracter no es ASCII: " + simbolo);
            System.out.println("Valor: " + Integer.valueOf(simbolo));
            ex.printStackTrace();
        }
    }

    public String get(char simbolo) {
        return map[simbolo];
    }

    public String[] getMap() {
        return map;
    }

    public int getSize() {
        return size;
    }
}
