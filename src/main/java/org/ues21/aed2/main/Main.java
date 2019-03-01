/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ues21.aed2.main;

import org.ues21.aed2.file.FileUtils;
import org.ues21.aed2.modelo.ArbolHuffman;
import org.ues21.aed2.modelo.CodificadorHuffman;
import org.ues21.aed2.soporte.U21File;


/**
 *
 * @author Agustín Aliaga
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Se debe proporcionar al menos un modo y un archivo de origen");
            return;
        }

        String mode = args[0],
            origen = args[1],
            destino = args.length > 2 ? args[2] : "";

        if (!mode.equals("-c") && !mode.equals("-d")) {
            throw new Exception("Seleccione un modo válido (-c o -d)");
        }

        if (mode.equals("-c")) {
            String content = FileUtils.leer(origen);
            ArbolHuffman arbol = new ArbolHuffman(content);
            String codigo = CodificadorHuffman.codificar(arbol.getDiccionarioHuffman(), content);
            FileUtils.escribirU21(destino, codigo, arbol.getDiccionarioHuffman());
        } else {
            U21File archivo = FileUtils.leerU21(origen);
            String mensajeOriginal = CodificadorHuffman.decodificar(archivo);
            FileUtils.escribir(destino, mensajeOriginal);
        }

    }
}
