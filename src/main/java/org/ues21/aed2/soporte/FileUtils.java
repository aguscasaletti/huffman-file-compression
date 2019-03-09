package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.map.ItemTablaSimbolos;
import org.ues21.aed2.estructuras.map.TablaHashSimbolos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtils {

    public static String leer(String path) {
        StringBuilder sb = new StringBuilder();

        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                sb.append(nextLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void escribir(String path, String contenido) {
        try {
            Files.write(Paths.get(path), contenido.getBytes(UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toBitString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes) {
            sb.append(
                    new StringBuffer(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')).reverse()
            );
        }
        return sb.toString();
    }

    private static int getMinimumBytesLength(int bitsAmount) {
        return (int) Math.ceil((double)bitsAmount / 8.0);
    }

    private static byte[] getByteArray(String bits) {
        BitSet bitSet = new BitSet(bits.length());
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                bitSet.set(i);
            }
        }

        // Create the required amount of bytes. When BitSet doesn't contain zeros in a byte it won't return it
        // for the "toByteArray()" call. That's why we do it this way.
        byte[] codeBytes = new byte[getMinimumBytesLength(bits.length())];
        byte[] bitSetByteArray = bitSet.toByteArray();

        for (int i = 0; i < bitSetByteArray.length; i++) {
            codeBytes[i] = bitSetByteArray[i];
        }

        return codeBytes;
    }

//    public static void escribirU21(String path, String contenido, CharMap tablaSimbolos) {
    public static void escribirU21(String path, String contenido, TablaHashSimbolos tablaSimbolos) {
        if (path == null || path.isEmpty()) {
            path = "comprimido.u21";
        }

        byte[] vectByte = getByteArray(contenido);

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "rw");

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
            rda.writeInt(contenido.length());
            rda.write(vectByte, 0, vectByte.length);

//            String[] tabla = tablaSimbolos.getMap();
//
//            // Escribir tamaño de la tabla de simbolos (para evitar leer los bits de padding)
//            rda.writeInt(tablaSimbolos.getSize());
//            StringBuilder sbCodigos =  new StringBuilder();
//
//            for (char i = 0; i < tabla.length; i++) {
//                if (tabla[i] != null) {
//                    rda.writeChar(i);
//                }
//            }
//
//            for (char i = 0; i < tabla.length; i++) {
//                if (tabla[i] != null) {
//                    rda.writeInt(tabla[i].length());
//                    sbCodigos.append(tabla[i]);
//                }
//            }


            rda.writeInt(tablaSimbolos.getSize());
            StringBuilder sbCodigos =  new StringBuilder();

            for (ItemTablaSimbolos itemTablaSimbolos: tablaSimbolos) {
                rda.writeChar(itemTablaSimbolos.getSimbolo());
            }

            for (ItemTablaSimbolos itemTablaSimbolos: tablaSimbolos) {
                rda.writeInt(itemTablaSimbolos.getCodigo().length());
                sbCodigos.append(itemTablaSimbolos.getCodigo());
            }

            // Escribir bytes de los códigos
            byte[] codeBytes = getByteArray(sbCodigos.toString());
            rda.write(codeBytes, 0, codeBytes.length);

            rda.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArchivoU21 leerU21(String path) {
        StringBuilder sbId = new StringBuilder(),
                sbExt = new StringBuilder(),
                sbNombre = new StringBuilder(),
                sbTiraBit = new StringBuilder();

        TablaHashSimbolos tablaHash = new TablaHashSimbolos();
        String encodedMessageString = "";

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "r");
            for (int i = 0; i < 3; i++) {
                sbId.append(rda.readChar());
            }

            for (int i = 3; i < 6; i++) {
                sbExt.append(rda.readChar());
            }

            for (int i = 6; i < 14; i++) {
                sbNombre.append(rda.readChar());
            }

            int largoBits = rda.readInt();

            int requiredBytesMessage = getMinimumBytesLength(largoBits);
            byte[] bytesMessage = new byte[requiredBytesMessage];
            rda.read(bytesMessage, 0, requiredBytesMessage);
            encodedMessageString = toBitString(bytesMessage);

            encodedMessageString = encodedMessageString.substring(0, largoBits);

            int cantBytesSimbolos = rda.readInt();
            EntradaTabla[] entradaTabla = new EntradaTabla[cantBytesSimbolos];

            for (int i = 0; i < cantBytesSimbolos; i++) {
                EntradaTabla entrada = new EntradaTabla();
                entrada.simbolo = rda.readChar();
                entradaTabla[i] = entrada;
            }

            int cantBitsSimbolos = 0;
            for (int i = 0; i < cantBytesSimbolos; i++) {
                entradaTabla[i].largoBits = rda.readInt();
                cantBitsSimbolos += entradaTabla[i].largoBits;
            }

            int bytesRequeridos = getMinimumBytesLength(cantBitsSimbolos);
            byte[] bytes = new byte[bytesRequeridos];
            rda.read(bytes, 0, bytesRequeridos);

            String codigosBits = toBitString(bytes);

            for (EntradaTabla entrada: entradaTabla) {
                try {
                    entrada.codigo = codigosBits.substring(0, entrada.largoBits);
                }
                catch (StringIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
                codigosBits = codigosBits.substring(entrada.largoBits);
                tablaHash.insertar(entrada.codigo, new ItemTablaSimbolos(entrada.simbolo, entrada.codigo));
            }

            rda.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ArchivoU21(
                sbNombre.toString(),
                sbExt.toString(),
                encodedMessageString,
                tablaHash
        );

    }

    public static class EntradaTabla {
        public char simbolo;
        public String codigo;
        public int largoBits;
    }
}
