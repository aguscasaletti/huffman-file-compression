package org.ues21.main;

import org.ues21.dataStructures.huffman.Huffman;
import org.ues21.support.U21File;
import org.ues21.support.HuffmanEncoder;
import org.ues21.support.Exceptions;
import org.ues21.support.FileUtils;

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
                throw new Exceptions.InvalidCommandException();
            }

            String mode = args[0],
                    sourcePath = args[1],
                    outputPath = args.length > 2 ? args[2] : "";

            if (!mode.equals("-c") && !mode.equals("-d")) {
                throw new Exceptions.InvalidCommandException();
            }

            if (mode.equals("-c")) {
                String content = FileUtils.readFile(sourcePath);
                Huffman tree = new Huffman(content);
                String encodedMessage = HuffmanEncoder.encode(tree.getSymbolsTable(), content);
                String originalName = sourcePath.lastIndexOf(File.separator) == -1
                        ? sourcePath
                        : sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1);

                if (outputPath.isEmpty()) {
                    String initialPath = sourcePath.lastIndexOf(File.separator) == -1
                            ? ""
                            : sourcePath.substring(0, sourcePath.lastIndexOf(File.separator) + 1);

                    String noExt = originalName.lastIndexOf(".") == -1
                            ? originalName
                            : originalName.substring(0, originalName.lastIndexOf("."));

                    outputPath = initialPath + noExt + ".u21";
                }

                FileUtils.writeU21File(outputPath, encodedMessage, tree.getSymbolsTable(), originalName);
            } else {
                U21File file = FileUtils.readU21File(sourcePath);
                String decodedMessage = HuffmanEncoder.decode(file);
                String fileName = file.getName();

                if (outputPath.isEmpty()) {
                    String path = sourcePath.lastIndexOf(File.separator) == -1
                            ? ""
                            : sourcePath.substring(0, sourcePath.lastIndexOf(File.separator) + 1);

                    outputPath = path + fileName;
                }

                FileUtils.writeFile(outputPath, decodedMessage);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
