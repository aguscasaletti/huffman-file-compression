package org.ues21.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ues21.dataStructures.huffman.Huffman;
import org.ues21.support.U21File;
import org.ues21.support.HuffmanEncoder;
import org.ues21.support.Exceptions;
import org.ues21.support.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Tests
 */
@RunWith(JUnit4.class)
public class HuffmanTests {

    private static String TEST_DIR = "src/test";
    private static String OUTPUT_DIR = TEST_DIR + "/generated/";

    private String[] inputTestSet = {
            "PATH://src/test/input/prueba.txt",
            "PATH://src/test/input/shakespeare.txt",
            "PATH://src/test/input/big.txt",
            "PATH://src/test/input/words.txt",
    };

    @Test
    public void testGeneratedTreeFromInput() {
        Stream.of(inputTestSet).forEach(input -> {

            Huffman huffman = new Huffman(input);

            Map<String, Boolean> inputCharsMap = new HashMap<>();
            for (char symbol : input.toCharArray()) {
                inputCharsMap.put(String.valueOf(symbol), true);
            }

            assertEquals(huffman.getSymbolsTable().getSize(), inputCharsMap.size());

            for (Map.Entry<String, Boolean> entry : inputCharsMap.entrySet())
            {
                assertNotNull(huffman.getSymbolsTable().find(entry.getKey()));
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
            String result = HuffmanEncoder.encode(huffman.getSymbolsTable(), input);
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
                    Profiler.profileTiming("read file", () -> {
                        try {
                            input[0] = FileUtils.readFile(input[0].replace("PATH://", ""));
                        } catch (FileNotFoundException | Exceptions.InvalidFileException e) {
                            e.printStackTrace();
                        }
                    });
                }

                AtomicReference<Huffman> arbol = new AtomicReference<>();
                Profiler.profileTiming("create tree and symbols table", () -> {
                    arbol.set(new Huffman(input[0]));
                });

                AtomicReference<String> codigo = new AtomicReference<>();
                Profiler.profileTiming("encode", () -> {
                    codigo.set(HuffmanEncoder.encode(arbol.get().getSymbolsTable(), input[0]));
                });

                System.out.println("Cantidad de símbolos diferentes: " + arbol.get().getSymbolsTable().getSize());

                Profiler.profileTiming("write U21 file", () -> {
                    FileUtils.writeU21File(testFileName, codigo.get(), arbol.get().getSymbolsTable(), count[0] + "file.txt");
                });

                AtomicReference<U21File> archivo = new AtomicReference<>();
                Profiler.profileTiming("read U21 file", () -> {
                    archivo.set(FileUtils.readU21File(testFileName));
                });

                assertEquals(arbol.get().getSymbolsTable().getSize(), archivo.get().getTablaSimbolos().getSize());
                assertEquals(codigo.get(), archivo.get().getCode());

                AtomicReference<String> mensajeOriginal = new AtomicReference<>();
                Profiler.profileTiming("decode message", () -> {
                    mensajeOriginal.set(HuffmanEncoder.decode(archivo.get()));
                });

                assertEquals(input[0], mensajeOriginal.get());

                FileUtils.writeFile(OUTPUT_DIR + count[0] + "-testMessage.txt", mensajeOriginal.get());

                String mensajeGuardado = FileUtils.readFile(OUTPUT_DIR + count[0] + "-testMessage.txt");
                assertEquals(input[0], mensajeGuardado);

                count[0]++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
