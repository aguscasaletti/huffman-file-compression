package org.ues21.aed2.soporte.first;

import org.ues21.aed2.estructuras.arbol.NodoHuffman;
import org.ues21.aed2.estructuras.lista.ListaHuffman;
import org.ues21.aed2.estructuras.lista.Nodo;


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

    private static byte[] getByteArray(String bits) {
        BitSet bitSet = new BitSet(bits.length());
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                bitSet.set(i);
            }
        }

        // Create the required amount of bytes. When BitSet doesn't contain zeros in a byte it won't return it
        // for the "toByteArray()" call. That's why we do it this way.
        byte[] codeBytes = new byte[(int) Math.ceil((double)bits.length() / 8.0)];
        byte[] bitSetByteArray = bitSet.toByteArray();

        for (int i = 0; i < bitSetByteArray.length; i++) {
            codeBytes[i] = bitSetByteArray[i];
        }

        return codeBytes;
    }

    public static void escribirU21(String path, String contenido, ListaHuffman tablaSimbolos) {
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

            // Escribir tamaño de la tabla de simbolos (para evitar leer los bits de padding)
            rda.writeInt(tablaSimbolos.getSize());

            // Escribir tabla de símbolos
            Nodo p = tablaSimbolos.getFrente();
            while (p != null) {
                rda.writeChar(((String[])p.getInfo())[0].charAt(0));

                String code = ((String[])p.getInfo())[1];
                rda.writeInt(code.length());

                byte[] codeBytes = getByteArray(code);

                rda.write(codeBytes, 0, codeBytes.length);

                p = p.getSiguiente();
            }

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
        ListaHuffman tablaSimbolos = new ListaHuffman();

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

            for (int i = 0; i < Math.ceil((double)largoBits / 8.0); i++) {
                byte b = rda.readByte();
                BitSet bitset1 = new BitSet(8);

                for (int j = 0; j < 8; j++) {
                    if ((b & (1 << j)) > 0) {
                        bitset1.set(j);
                    }
                }

                for (int j = 0; j < 8; j++) {
                    if (bitset1.get(j)) {
                        sbTiraBit.append("1");
                    } else {
                        sbTiraBit.append("0");
                    }

                    if (sbTiraBit.length() == largoBits) {
                        break;
                    }
                }
            }

            int largoListaSimbolos = rda.readInt();

            for (int i = 0; i < largoListaSimbolos; i++) {
                char c = rda.readChar();
                int bitsLength = rda.readInt();

                double bytesAmount = Math.ceil((double) bitsLength / 8.0);
                StringBuilder sbCode = new StringBuilder();

                for (int j = 0; j < bytesAmount; j++) {
                    byte b = rda.readByte();
                    BitSet bitset1 = new BitSet(8);
                    for (int k = 0; k < 8; k++) {
                        if ((b & (1 << k)) > 0) {
                            bitset1.set(k);
                        }
                    }

                    for (int k = 0; k < 8; k++) {
                        if (bitset1.get(k)) {
                            sbCode.append("1");
                        } else {
                            sbCode.append("0");
                        }

                        if (sbCode.toString().length() == bitsLength) {
                            break;
                        }
                    }
                }

                String[] vec = new String[2];
                vec[0] = String.valueOf(c);
                vec[1] = sbCode.toString();
                tablaSimbolos.agregar(vec);
            }

            rda.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ArchivoU21(
                sbNombre.toString(),
                sbExt.toString(),
                sbTiraBit.toString(),
                tablaSimbolos
        );
    }
}
