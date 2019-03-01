package org.ues21.aed2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ues21.aed2.file.FileUtils;
import org.ues21.aed2.modelo.ArbolHuffman;
import org.ues21.aed2.modelo.CodificadorHuffman;
import org.ues21.aed2.modelo.ListaHuffman;
import org.ues21.aed2.modelo.Nodo;
import org.ues21.aed2.soporte.U21File;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ArbolHuffmanTests {

    private String[] inputTestSet = {
            "Se debe proporcionar al menos un modo y un archivo de origen",
            "Hola como estás!!! :D !",
            "LSDKFLAKDFL DFGldskfgs dflgklds gKLDFKHLDFGk KHLDKGHOrk$%·L$K %$&",
            "!=!=!=!=!!!===DSdfsoe·/$%&/%&/(%&/(!=!=!=!=!!!===DSdfsoe·%&$%%&&&&&/%&///%?????",
            "adsf 67509)))))))))))43945)·$)%))))))" +
                    "%·$%·$%· V # 34345",
            "When packing signed bytes into an int, each byte needs to be masked off because it is sign-extended to 32 bits (rather than zero-extended) due to the arithmetic promotion rule (described in JLS, Conversions and Promotions). There's an interesting puzzle related to this described in Java Puzzlers (\"A Big Delight in Every Byte\") by Joshua Bloch and Neal Gafter . When comparing a byte value to an int value, the byte is sign-extended to an int and then this value is compared to the other int"
    };

    @Test
    public void testGeneratedTreeFromInput() {
        Stream.of(inputTestSet).forEach(input -> {

            ArbolHuffman arbolHuffman = new ArbolHuffman(input);

            Map<String, Boolean> inputCharsMap = new HashMap<>();
            for (char symbol : input.toCharArray()) {
                inputCharsMap.put(String.valueOf(symbol), true);
            }

            assertEquals(arbolHuffman.getDiccionarioHuffman().getSize(), inputCharsMap.size());

            for (Map.Entry<String, Boolean> entry : inputCharsMap.entrySet())
            {
                assertNotNull(arbolHuffman.getDiccionarioHuffman().buscar(entry.getKey()));
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
            String result = CodificadorHuffman.codificar(arbolHuffman.getDiccionarioHuffman(), input);
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
            String result = CodificadorHuffman.codificar(arbolHuffman.getDiccionarioHuffman(), input);
            U21File file = new U21File("", "", result, arbolHuffman.getDiccionarioHuffman());
            String mensajeOriginal = CodificadorHuffman.decodificar(file);

            assertEquals(input, mensajeOriginal);
        });
    }

    private void printDiccionario(ListaHuffman diccionario) {
        System.out.println(diccionario.getSize() + " ---------------------------------");
        Nodo p = diccionario.getFrente();
        int count = 1;
        while (p != null) {
            System.out.println(count + " - " + ((String[])p.getInfo())[0] + " - " + ((String[])p.getInfo())[1]);
            p = p.getSiguiente();
            count++;
        }
    }

    @Test
    public void testSerialization() {
        final int[] count = { 0 };
        Stream.of(inputTestSet).forEach(input -> {
            String testFileName = "testFile.u21" + count[0];

            // Serialize
            ArbolHuffman arbol = new ArbolHuffman(input);
            String codigo = CodificadorHuffman.codificar(arbol.getDiccionarioHuffman(), input);
            FileUtils.escribirU21(testFileName, codigo, arbol.getDiccionarioHuffman());

            // Deserialize
            U21File archivo = FileUtils.leerU21(testFileName);

            // Compare dictionaries
            Nodo frente = arbol.getDiccionarioHuffman().getFrente();
            Nodo deserialized = archivo.getDiccionario().getFrente();

            assertEquals(arbol.getDiccionarioHuffman().getSize(), archivo.getDiccionario().getSize());

            while (frente != null) {
                assertEquals(((String[])frente.getInfo())[0], ((String[])deserialized.getInfo())[0]);
                assertEquals(((String[])frente.getInfo())[1], ((String[])deserialized.getInfo())[1]);

                deserialized = deserialized.getSiguiente();
                frente = frente.getSiguiente();
            }

            // Compare codes
            assertEquals(codigo, archivo.getCodigo());

            String mensajeOriginal = CodificadorHuffman.decodificar(archivo);
            FileUtils.escribir(count[0] + "testMessage.txt", mensajeOriginal);

            String mensajeGuardado = FileUtils.leer(count[0] + "testMessage.txt");
            // Messages should be equal
            assertEquals(input, mensajeGuardado);

            count[0] ++;
        });
    }
}
