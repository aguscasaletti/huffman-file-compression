package org.ues21.aed2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ues21.aed2.soporte.FileUtils;
import org.ues21.aed2.estructuras.arbol.ArbolHuffman;
import org.ues21.aed2.soporte.CodificadorHuffman;
import org.ues21.aed2.estructuras.lista.ListaHuffman;
import org.ues21.aed2.estructuras.lista.Nodo;
import org.ues21.aed2.soporte.ArchivoU21;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ArbolHuffmanTests {

    public static String TEST_DIR = "src/test";
    public static String INPUT_DIR = TEST_DIR + "/input/";
    public static String OUTPUT_DIR = TEST_DIR + "/generated/";

    private String[] inputTestSet = {
            "Se debe proporcionar al menos un modo y un archivo de origen \n",
            "Hola como estás!!! :D !\n",
            "LSDKFLAKDFL DFGldskfgs dflgklds gKLDFKHLDFGk KHLDKGHOrk$%·L$K %$&\n",
            "!=!=!=!=!!!===DSdfsoe·/$%&/%&/(%&/(!=!=!=!=!!!===DSdfsoe·%&$%%&&&&&/%&///%?????\n",
            "When packing signed bytes into an int, each byte needs to be masked off because it is sign-extended to 32 bits (rather than zero-extended) due to the arithmetic promotion rule (described in JLS, Conversions and Promotions). There's an interesting puzzle related to this described in Java Puzzlers (\"A Big Delight in Every Byte\") by Joshua Bloch and Neal Gafter . When comparing a byte value to an int value, the byte is sign-extended to an int and then this value is compared to the other int\n",
            "PATH://src/test/input/prueba.txt",
            "PATH://src/test/input/shakespeare.txt",
            "PATH://src/test/input/big.txt"
    };

    @Test
    public void testGeneratedTreeFromInput() {
        Stream.of(inputTestSet).forEach(input -> {

            ArbolHuffman arbolHuffman = new ArbolHuffman(input);

            Map<String, Boolean> inputCharsMap = new HashMap<>();
            for (char symbol : input.toCharArray()) {
                inputCharsMap.put(String.valueOf(symbol), true);
            }

            assertEquals(arbolHuffman.getListaSimbolos().getSize(), inputCharsMap.size());

            for (Map.Entry<String, Boolean> entry : inputCharsMap.entrySet())
            {
                assertNotNull(arbolHuffman.getListaSimbolos().buscar(entry.getKey()));
                assertNotEquals(entry.getValue(), null);
                assertNotEquals(entry.getValue(), "");
                assertNotEquals(entry.getKey(), null);
                assertNotEquals(entry.getKey(), "");
            }
        });
    }

    @Test
    public void testEncodedResult() {
        Stream.of(inputTestSet).forEach(input -> {

            ArbolHuffman arbolHuffman = new ArbolHuffman(input);
            String result = CodificadorHuffman.codificar(arbolHuffman.getListaSimbolos(), input);
            assertTrue(
                    result.replace("0", "")
                            .replace("1", "")
                            .isEmpty()
            );
        });
    }

    @Test
    public void testEncodingDecoding() {
        Stream.of(inputTestSet).forEach(input -> {

            ArbolHuffman arbolHuffman = new ArbolHuffman(input);
            String result = CodificadorHuffman.codificar(arbolHuffman.getListaSimbolos(), input);
            ArchivoU21 file = new ArchivoU21("", "", result, arbolHuffman.getListaSimbolos());
            String mensajeOriginal = CodificadorHuffman.decodificar(file);

            assertEquals(input, mensajeOriginal);
        });
    }

    private void printListaSimbolos(ListaHuffman listaSimbolos) {
        System.out.println(listaSimbolos.getSize() + " ---------------------------------");
        Nodo p = listaSimbolos.getFrente();
        int count = 1;
        while (p != null) {
            System.out.println(count + " - " + ((String[])p.getInfo())[0] + " - " + ((String[])p.getInfo())[1]);
            p = p.getSiguiente();
            count++;
        }
    }

    @Test
    public void testSerialization() {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File[] testDirFiles = outputDir.listFiles();

        if (testDirFiles != null) {
            // Delete all generated files if any
            Arrays.stream(testDirFiles).forEach(File::delete);
        }

        final int[] count = { 0 };
        Stream.of(inputTestSet).forEach(input -> {
            System.out.println("Profiling: " + count[0]);

            String testFileName = OUTPUT_DIR + "/" + count[0] + "file.u21";

            if (input.startsWith("PATH://")) {
                long startReadfile = System.currentTimeMillis();
                input = FileUtils.leer(input.replace("PATH://", ""));
                System.out.println(String.format("Time (s) to read file: %s", (System.currentTimeMillis() - startReadfile) / 1000F));
            }

            long startCreateTree = System.currentTimeMillis();
            ArbolHuffman arbol = new ArbolHuffman(input);
            System.out.println(String.format("Time (s) to create tree: %s", (System.currentTimeMillis() - startCreateTree) / 1000F));

            long startEncoding = System.currentTimeMillis();
            String codigo = CodificadorHuffman.codificar(arbol.getListaSimbolos(), input);
            System.out.println(String.format("Time (s) to encode: %s", (System.currentTimeMillis() - startEncoding) / 1000F));

            System.out.println("Cantidad de símbolos diferentes: " + arbol.getListaSimbolos().getSize());

            long startWrite21 = System.currentTimeMillis();
            FileUtils.escribirU21(testFileName, codigo, arbol.getListaSimbolos());
            System.out.println(String.format("Time (s) to write U21: %s", (System.currentTimeMillis() - startWrite21) / 1000F));

            // Deserialize
            long startReading21 = System.currentTimeMillis();
            ArchivoU21 archivo = FileUtils.leerU21(testFileName);
            System.out.println(String.format("Time (s) to read U21: %s", (System.currentTimeMillis() - startReading21) / 1000F));

            // Compare dictionaries
            Nodo frente = arbol.getListaSimbolos().getFrente();
            Nodo deserialized = archivo.getTablaSimbolos().getFrente();

            assertEquals(arbol.getListaSimbolos().getSize(), archivo.getTablaSimbolos().getSize());

            while (frente != null) {
                assertEquals(((String[])frente.getInfo())[0], ((String[])deserialized.getInfo())[0]);
                assertEquals(((String[])frente.getInfo())[1], ((String[])deserialized.getInfo())[1]);

                deserialized = deserialized.getSiguiente();
                frente = frente.getSiguiente();
            }

            // Compare codes
            assertEquals(codigo, archivo.getCodigo());

            long startDecoding = System.currentTimeMillis();
            String mensajeOriginal = CodificadorHuffman.decodificar(archivo);
            System.out.println(String.format("Time (s) to decode message: %s", (System.currentTimeMillis() - startDecoding) / 1000F));

            assertEquals(input, mensajeOriginal);

            // Fix line breaks
            FileUtils.escribir(OUTPUT_DIR + count[0] + "-testMessage.txt", mensajeOriginal);

            String mensajeGuardado = FileUtils.leer(OUTPUT_DIR + count[0] + "-testMessage.txt");
            // Messages should be equal
            assertEquals(input, mensajeGuardado);

            count[0] ++;
        });
    }
}
