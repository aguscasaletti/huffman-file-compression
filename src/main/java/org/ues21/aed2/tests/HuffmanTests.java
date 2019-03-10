package org.ues21.aed2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ues21.aed2.estructuras.huffman.Huffman;
import org.ues21.aed2.soporte.ArchivoU21;
import org.ues21.aed2.soporte.CodificadorHuffman;
import org.ues21.aed2.soporte.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Clase de tests
 */
@RunWith(JUnit4.class)
public class HuffmanTests {

    private static String TEST_DIR = "src/test";
    private static String OUTPUT_DIR = TEST_DIR + "/generated/";

    private String[] inputTestSet = {
//            "Se debe proporcionar al menos un modo y un archivo de origen \n",
//            "Hola como estás!!! :D !\n",
//            "LSDKFLAKDFL DFGldskfgs dflgklds gKLDFKHLDFGk KHLDKGHOrk$%·L$K %$&\n",
//            "!=!=!=!=!!!===DSdfsoe·/$%&/%&/(%&/(!=!=!=!=!!!===DSdfsoe· ÷¬¢#@|≠´‚¢∞@##¢¢#∞ %&$%%&&&&&/%&///%?????\n",
//            "When packing signed bytes into an int, each byte needs to be masked off because it is sign-extended to 32 bits (rather than zero-extended) due to the arithmetic promotion rule (described in JLS, Conversions and Promotions). There's an interesting puzzle related to this described in Java Puzzlers (\"A Big Delight in Every Byte\") by Joshua Bloch and Neal Gafter . When comparing a byte value to an int value, the byte is sign-extended to an int and then this value is compared to the other int\n",
            "PATH://src/test/input/prueba.txt",
            "PATH://src/test/input/shakespeare.txt",
            "PATH://src/test/input/big.txt",
            "PATH://src/test/input/words.txt",
//            "PATH://src/test/input/chino.txt",
//            "PATH://src/test/input/big2.txt"
    };

    @Test
    public void testGeneratedTreeFromInput() {
        Stream.of(inputTestSet).forEach(input -> {

            Huffman huffman = new Huffman(input);

            Map<String, Boolean> inputCharsMap = new HashMap<>();
            for (char symbol : input.toCharArray()) {
                inputCharsMap.put(String.valueOf(symbol), true);
            }

            assertEquals(huffman.getTablaSimbolos().getSize(), inputCharsMap.size());

            for (Map.Entry<String, Boolean> entry : inputCharsMap.entrySet())
            {
                assertNotNull(huffman.getTablaSimbolos().findSimbolo(entry.getKey()));
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

            Huffman huffman = new Huffman(input);
            String result = CodificadorHuffman.codificar(huffman.getTablaSimbolos(), input);
            assertTrue(
                    result.replace("0", "")
                            .replace("1", "")
                            .isEmpty()
            );
        });
    }

    @Test
    public void testSerialization() {
        System.out.println("--------------------------------- Tests  ------------------------------");

        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File[] testDirFiles = outputDir.listFiles();

        if (testDirFiles != null) {
            // Delete all generated files if any
            Arrays.stream(testDirFiles).forEach(File::delete);
        }

        final int[] count = {0};
        Stream.of(inputTestSet).forEach(value -> {
            try {
                System.out.println(" ------  Profiling: -----" + value.substring(value.length() - 10, value.length()));

                String testFileName = OUTPUT_DIR + count[0] + "file.u21";
                String[] input = {value};

                if (input[0].startsWith("PATH://")) {
                    // Leer archivo original como String
                    Profiler.profileTiming("read file", () -> {
                        try {
                            input[0] = FileUtils.leer(input[0].replace("PATH://", ""));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                }

                // Crear árbol de Huffman y tabla de símbolos
                AtomicReference<Huffman> arbol = new AtomicReference<>();
                Profiler.profileTiming("create tree and symbols table", () -> {
                    arbol.set(new Huffman(input[0]));
                });

                // Codificar (obtener string final)
                AtomicReference<String> codigo = new AtomicReference<>();
                Profiler.profileTiming("encode", () -> {
                    codigo.set(CodificadorHuffman.codificar(arbol.get().getTablaSimbolos(), input[0]));
                });

                System.out.println("Cantidad de símbolos diferentes: " + arbol.get().getTablaSimbolos().getSize());

                // Escribir archivo U21
                Profiler.profileTiming("write U21 file", () -> {
                    FileUtils.escribirU21(testFileName, codigo.get(), arbol.get().getTablaSimbolos(), count[0] + "file.txt");
                });

                // Leer archivo U21
                AtomicReference<ArchivoU21> archivo = new AtomicReference<>();
                Profiler.profileTiming("read U21 file", () -> {
                    archivo.set(FileUtils.leerU21(testFileName));
                });

                assertEquals(arbol.get().getTablaSimbolos().getSize(), archivo.get().getTablaSimbolos().getSize());
                assertEquals(codigo.get(), archivo.get().getCodigo());

                // Decodificar y obtener mensaje original
                AtomicReference<String> mensajeOriginal = new AtomicReference<>();
                Profiler.profileTiming("decode message", () -> {
                    mensajeOriginal.set(CodificadorHuffman.decodificar(archivo.get()));
                });

                assertEquals(input[0], mensajeOriginal.get());

                // Fix line breaks
                FileUtils.escribir(OUTPUT_DIR + count[0] + "-testMessage.txt", mensajeOriginal.get());

                String mensajeGuardado = FileUtils.leer(OUTPUT_DIR + count[0] + "-testMessage.txt");
                // Messages should be equal
                assertEquals(input[0], mensajeGuardado);

                count[0]++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
