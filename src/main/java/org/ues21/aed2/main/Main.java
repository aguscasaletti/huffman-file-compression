package org.ues21.aed2.main;

import org.ues21.aed2.estructuras.huffman.Huffman;
import org.ues21.aed2.soporte.ArchivoU21;
import org.ues21.aed2.soporte.CodificadorHuffman;
import org.ues21.aed2.soporte.Excepciones;
import org.ues21.aed2.soporte.FileUtils;

import java.io.File;


/**
 * CLI app
 * @author Agustín Aliaga
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                throw new Excepciones.ComandoInvalidoException();
            }

            String mode = args[0],
                    origen = args[1],
                    destino = args.length > 2 ? args[2] : "";

            if (!mode.equals("-c") && !mode.equals("-d")) {
                throw new Excepciones.ComandoInvalidoException();
            }

            if (mode.equals("-c")) {
                String content = FileUtils.leer(origen);
                Huffman arbol = new Huffman(content);
                String codigo = CodificadorHuffman.codificar(arbol.getTablaSimbolos(), content);
                String nombreOriginal = origen.lastIndexOf(File.separator) == -1
                        ? origen
                        : origen.substring(origen.lastIndexOf(File.separator) + 1);

                if (destino.isEmpty()) {
                    String pathOriginal = origen.lastIndexOf(File.separator) == -1
                            ? ""
                            : origen.substring(0, origen.lastIndexOf(File.separator) + 1);

                    String sinExt = nombreOriginal.lastIndexOf(".") == -1
                            ? nombreOriginal
                            : nombreOriginal.substring(0, nombreOriginal.lastIndexOf("."));

                    destino = pathOriginal + sinExt + ".u21";
                }

                FileUtils.escribirU21(destino, codigo, arbol.getTablaSimbolos(), nombreOriginal);
            } else {
                ArchivoU21 archivo = FileUtils.leerU21(origen);
                String mensajeOriginal = CodificadorHuffman.decodificar(archivo);
                String nombreOriginal = archivo.getNombre();

                if (destino.isEmpty()) {
                    String path = origen.lastIndexOf(File.separator) == -1
                            ? ""
                            : origen.substring(0, origen.lastIndexOf(File.separator) + 1);

                    destino = path + nombreOriginal;
                }

                FileUtils.escribir(destino, mensajeOriginal);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
