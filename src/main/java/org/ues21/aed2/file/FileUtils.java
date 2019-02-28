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

        String[] segments = path.split("/");

        //El código generado como una cadena de caracteres.
        System.out.println("Código huffman generado como cadena de caracteres: ");
        System.out.println(contenido);
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
            rda.writeInt(vectByte.length);
            rda.write(vectByte, 0, vectByte.length);

            // Escribir diccionario
            Nodo p = diccionario.getFrente();
            while (p != null) {
                rda.writeChar(((String[])p.getInfo())[0].charAt(0));
                String code = ((String[])p.getInfo())[1];
                rda.writeInt(code.length());

                BitSet bitSet = new BitSet(code.length());
                for (int i = 0; i < code.length(); i++) {
                    if (code.charAt(i) == '1') {
                        bitSet.set(i);
                    }
                }
                if (bitSet.toByteArray().length == 0) {
                    rda.write(new byte[1]);
                } else {
                    rda.write(bitSet.toByteArray(), 0, bitSet.toByteArray().length);
                }

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
        StringBuilder sbId = new StringBuilder();
        StringBuilder sbExt = new StringBuilder();
        StringBuilder sbNombre = new StringBuilder();
        StringBuilder sbTiraBit = new StringBuilder();
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

            byte b;

            for (int i = 0; i < largoBits; i++) {
                b = rda.readByte();
                BitSet bitset1 = new BitSet(8);
                //Tener en cuenta que si los ultimos Bits del código no llegan
                //a 8 se completan con ceros
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
                }
            }

            try {
                while (true) {
                    char c = rda.readChar();
                    int bitsLength = rda.readInt();

                    double bytesAmount = Math.ceil((double) bitsLength / 8.0);
                    StringBuilder sbCode = new StringBuilder();

                    for (int i = 0; i < bytesAmount; i++) {
                        b = rda.readByte();
                        BitSet bitset1 = new BitSet(8);
                        for (int j = 0; j < 8; j++) {
                            if ((b & (1 << j)) > 0) {
                                bitset1.set(j);
                            }
                        }

                        for (int j = 0; j < 8; j++) {
                            if (bitset1.get(j)) {
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
            } catch (EOFException e) {
                rda.close();
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
            Files.lines(
                    Paths.get(path),
                    StandardCharsets.UTF_8
            ).forEach(sb::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
