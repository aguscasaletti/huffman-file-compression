package org.ues21.aed2.soporte;

import org.ues21.aed2.estructuras.tablaHash.ItemTablaSimbolos;
import org.ues21.aed2.estructuras.tablaHash.TablaHashSimbolos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Clase que interactúa con el filesystem para escribir y leer archivos de texto plano y archivos comprimidos .u21
 */
public class FileUtils {

    /**
     * Método que lee un archivo de texto plano
     * @param path path donde se encuenta el archivo a leer
     * @return el contenido en String del archivo leído
     */
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

    /**
     * Método que se usa para escribir un archivo de texto plano
     * @param path el path donde se desea escribir
     * @param contenido el String que queremos escribir en el archivo
     */
    public static void escribir(String path, String contenido) {
        try {
            Files.write(Paths.get(path), contenido.getBytes(UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper que devuelve un String que representa los bits dados. Ej: "011001010001101001".
     * Esta es una operación computacionalmente costosa que debería obviarse.
     *
     * @param bytes el byte array del cual se desea esta información
     * @return el String que representa dichos bits
     */
    private static String toBitString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes) {
            sb.append(
                    new StringBuffer(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')).reverse()
            );
        }
        return sb.toString();
    }

    /**
     * Helper que devuelve la cantidad de bytes necesarios para representar una cantidad N de bits
     * @param bitsAmount la cantidad de bits
     * @return la cantidad de bytes necesarios
     */
    private static int getMinimumBytesLength(int bitsAmount) {
        return (int) Math.ceil((double)bitsAmount / 8.0);
    }

    /**
     * Helper que recibe una cadena en String de bits. Ej: "00101010111" y devuelve el byte array correspondiente
     * Notese que para cuando un byte final está solo compuesto por ceros, entonces bitSet.toByteArray() no devuelve dicho byte.
     * Por esta razón se inicializa un byte array vacío del tamaño deseado.
     *
     * @param bits
     * @return
     */
    private static byte[] getByteArray(String bits) {
        BitSet bitSet = new BitSet(bits.length());
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1') {
                bitSet.set(i);
            }
        }

        byte[] codeBytes = new byte[getMinimumBytesLength(bits.length())];
        byte[] bitSetByteArray = bitSet.toByteArray();

        for (int i = 0; i < bitSetByteArray.length; i++) {
            codeBytes[i] = bitSetByteArray[i];
        }

        return codeBytes;
    }

    /**
     * Método que escribe un archivo .u21 (comprimido) en el filesystem
     * @param path el path donde se desea escribir el archivo
     * @param codigo el contenido (mensaje codificado) del mismo
     * @param tablaSimbolos la tabla de símbolos que lo define
     */
    public static void escribirU21(String path, String codigo, TablaHashSimbolos tablaSimbolos) {
        if (path == null || path.isEmpty()) {
            path = "comprimido.u21";
        }

        byte[] vectByte = getByteArray(codigo);

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "rw");

            /**
             * Escribimos el prefijo
             */
            rda.writeChar('h');
            rda.writeChar('u');
            rda.writeChar('f');

            /**
             * Escribimos la extensión del archivo
             */
            rda.writeChar('t');
            rda.writeChar('x');
            rda.writeChar('t');

            /**
             * Escribimos el nombre del archivo
             */
            rda.writeChar('P');
            rda.writeChar('R');
            rda.writeChar('U');
            rda.writeChar('E');
            rda.writeChar('B');
            rda.writeChar('C');
            rda.writeChar('O');
            rda.writeChar('M');

            /**
             * Escribir código
             */
            rda.writeInt(codigo.length());
            rda.write(vectByte, 0, vectByte.length);

            /**
             * Escribir tabla de símbolos
             */
            // Escribimos la cantidad de símbolos distintos de la tabla
            rda.writeInt(tablaSimbolos.getSize());
            StringBuilder sbCodigosTabla =  new StringBuilder();

            // Usando la interfaz Iterable recorremos la tabla y escribimos todos los símbolos originales
            for (ItemTablaSimbolos itemTablaSimbolos: tablaSimbolos) {
                rda.writeChar(itemTablaSimbolos.getSimbolo());
            }

            // Luego escribimos las longitudes (cantidad de bits) de cada código Huffman correspondiente
            for (ItemTablaSimbolos itemTablaSimbolos: tablaSimbolos) {
                rda.writeInt(itemTablaSimbolos.getCodigo().length());
                sbCodigosTabla.append(itemTablaSimbolos.getCodigo());
            }

            // Finalmente escribimos todos códigos de la tabla en conjunto (ahorramos un poco de espacio de esta forma).
            byte[] codeBytes = getByteArray(sbCodigosTabla.toString());
            rda.write(codeBytes, 0, codeBytes.length);

            rda.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método que lee un archivo comprimido .u21 y devuelve una representación del mismo usando la clase ArchivoU21
     * @param path el path donde está el archivo
     * @return la representación del archivo con ArchivoU21
     */
    public static ArchivoU21 leerU21(String path) {
        StringBuilder sbPrefijo = new StringBuilder(),
                sbExt = new StringBuilder(),
                sbNombre = new StringBuilder();

        TablaHashSimbolos tablaHash = new TablaHashSimbolos();
        String codigo = "";

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "r");
            /**
             * Leemos el prefijo
             */
            for (int i = 0; i < 3; i++) {
                sbPrefijo.append(rda.readChar());
            }

            /**
             * Leemos la extensión del archivo
             */
            for (int i = 3; i < 6; i++) {
                sbExt.append(rda.readChar());
            }

            /**
             * Leemos el nombre del archivo
             */
            for (int i = 6; i < 14; i++) {
                sbNombre.append(rda.readChar());
            }

            /**
             * Leemos el código
             */
            int largoBits = rda.readInt();

            int numeroBytesCodigo = getMinimumBytesLength(largoBits);
            byte[] bytesCodigo = new byte[numeroBytesCodigo];
            rda.read(bytesCodigo, 0, numeroBytesCodigo);
            codigo = toBitString(bytesCodigo);
            codigo = codigo.substring(0, largoBits);

            /**
             * Leemos la tabla de símbolos
             */
            // Leemos la cantidad de símbolos de la tabla
            int cantBytesSimbolos = rda.readInt();
            EntradaTabla[] entradaTabla = new EntradaTabla[cantBytesSimbolos];

            // Leemos los caracteres / símbolos originales
            for (int i = 0; i < cantBytesSimbolos; i++) {
                EntradaTabla entrada = new EntradaTabla();
                entrada.simbolo = rda.readChar();
                entradaTabla[i] = entrada;
            }

            // Leemos las longitudes de bits de cada código correspondiente
            int cantBitsSimbolos = 0;
            for (int i = 0; i < cantBytesSimbolos; i++) {
                entradaTabla[i].largoBits = rda.readInt();
                cantBitsSimbolos += entradaTabla[i].largoBits;
            }

            // Leemos todos los códigos de la tabla de huffman
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
                codigo,
                tablaHash
        );

    }

    /**
     * Helper class para la construcción progresiva de la tabla de símbolos.
     * Solo se usa localmente.
     */
    private static class EntradaTabla {
        char simbolo;
        String codigo;
        int largoBits;
    }
}
