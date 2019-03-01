package org.ues21.aed2.file;

import org.ues21.aed2.modelo.ListaHuffman;
import org.ues21.aed2.modelo.Nodo;
import org.ues21.aed2.modelo.NodoHuffman;
import org.ues21.aed2.soporte.U21File;
import sun.jvm.hotspot.utilities.Bits;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;

public class FileUtils {

    public static void escribirU21(String path, String contenido, ListaHuffman diccionario) {
        if (path == null || path.isEmpty()) {
           path = "comprimido.u21";
        }

        BitSet bitset = new BitSet(contenido.length());

        char[] codVec = contenido.toCharArray();

        //Para pasar el codigo de bits en string a un bitset y
        //poder manejar bytes directamente
        for (int i = 0; i < codVec.length; i++) {
            char d = codVec[i];
            if (d == '1') {
                bitset.set(i);
            }
        }

        //Estos bytes son los que debo almacenar en el archivo
        //de acceso aleatorio
        byte[] vectByte = bitset.toByteArray();

        //Yapa: Escritura de huffman en archivo de acceso aleatorio siguiendo
        //el formato de archivo requerido en el final. En este caso se considera
        //que el archivo original se denomina PRUEBCOM.txt
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

            // Escribir tamaño de diccionario (para evitar leer los bits de padding)
            rda.writeInt(diccionario.getSize());

            // Escribir diccionario
            Nodo p = diccionario.getFrente();
            while (p != null) {
                rda.writeChar(((String[])p.getInfo())[0].charAt(0));

                String code = ((String[])p.getInfo())[1];
                rda.writeInt(code.length());

                BitSet codeBitset = new BitSet(code.length());
                for (int i = 0; i < code.length(); i++) {
                    if (code.charAt(i) == '1') {
                        codeBitset.set(i);
                    }
                }

                // Create the required amount of bytes. When BitSet doesn't contain zeros in a byte it won't return it
                // for the "toByteArray()" call. That's why we do it this way.
                byte[] codeBytes = new byte[(int) Math.ceil((double)code.length() / 8.0)];
                for (int i = 0; i < codeBitset.toByteArray().length; i++) {
                    codeBytes[i] = codeBitset.toByteArray()[i];
                }

                rda.write(codeBytes, 0, codeBytes.length);

                p = p.getSiguiente();
            }

            rda.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void escribir(String path, String contenido) {
        try {
            Files.write(Paths.get(path), contenido.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static U21File leerU21(String path) {
        StringBuilder sbId = new StringBuilder(),
         sbExt = new StringBuilder(),
         sbNombre = new StringBuilder(),
         sbTiraBit = new StringBuilder();
        ListaHuffman diccionario = new ListaHuffman();

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "r");
            //El identificador del archivo es: huf
            for (int i = 0; i < 3; i++) {
                sbId.append(rda.readChar());
            }

            //La extension del archivo comprimido es (en este caso): txt
            for (int i = 3; i < 6; i++) {
                sbExt.append(rda.readChar());
            }

            //El nombre del archivo original es (en este caso): PRUEBCOM
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

            int largoDiccionario = rda.readInt();

            for (int i = 0; i < largoDiccionario; i++) {
                char c = rda.readChar();
                int bitsLength = rda.readInt();

                double bytesAmount = Math.ceil((double) bitsLength / 8.0);
                StringBuilder sbCode = new StringBuilder();

                for (int j = 0; j < bytesAmount; j++) {
                    byte b = rda.readByte();
//                    try {
//                        b = rda.readByte();
//                    } catch(EOFException ex) {
//                        ex.printStackTrace();
//                    }
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
                diccionario.agregar(vec);
            }

            rda.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new U21File(
                sbNombre.toString(),
                sbExt.toString(),
                sbTiraBit.toString(),
                diccionario
        );
    }

    public static String leer(String path) {
        StringBuilder sb = new StringBuilder();

        try {
            Files.lines(Paths.get(path), StandardCharsets.UTF_8).forEach(sb::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
