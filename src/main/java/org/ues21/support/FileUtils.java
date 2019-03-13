package org.ues21.support;

import org.ues21.dataStructures.hashTable.SymbolsTableItem;
import org.ues21.dataStructures.hashTable.SymbolsHashTable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class that interacts with the filesystem and reads/writes plain text files and U21 files
 */
public class FileUtils {

    /**
     * Read plain text file, throw an exception if binary file is found
     */
    public static String readFile(String path) throws FileNotFoundException, Exceptions.InvalidFileException {
        StringBuilder sb = new StringBuilder();
        int totalChars = 0;

        File file = new File(path);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String nextLine = sc.nextLine();
            totalChars += nextLine.toCharArray().length;
            sb.append(nextLine + "\n");
        }

        if (totalChars == 0) {
            throw new Exceptions.InvalidFileException();
        }

        return sb.toString();
    }

    /**
     * Write plain text file
     */
    public static void writeFile(String path, String content) {
        try {
            Files.write(Paths.get(path), content.getBytes(UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method that returns a String that represents the given bytes. Ej: "011001010001101001".
     * Converting between String and bytes is a bad practice that should be avoided to achieve greater performance.
     *
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
     * Helper method that returns how many bytes we need to contain an N number of bits
     */
    private static int getMinimumBytesLength(int bitsAmount) {
        return (int) Math.ceil((double)bitsAmount / 8.0);
    }

    /**
     * Helper method that takes a String and maps it to a byte array.
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
     * Writes compressed .u21 file
     */
    public static void writeU21File(String path, String encodedMessage, SymbolsHashTable symbolsTable, String fileName) {
        if (path == null || path.isEmpty()) {
            path = "comprimido.u21";
        }

        byte[] vectByte = getByteArray(encodedMessage);

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "rw");

            /**
             * prefix
             */
            rda.writeChar('h');
            rda.writeChar('u');
            rda.writeChar('f');

            /**
             * file name length
             */
            rda.writeInt(fileName.length());

            /**
             * file name
             */
            for (int i = 0; i < fileName.length(); i++) {
                rda.writeChar(fileName.charAt(i));
            }

            /**
             * encoded message
             */
            rda.writeInt(encodedMessage.length());
            rda.write(vectByte, 0, vectByte.length);

            /**
             * symbols table
             */
            rda.writeInt(symbolsTable.getSize());
            StringBuilder sbTableCodes =  new StringBuilder();

            for (SymbolsTableItem symbolsTableItem : symbolsTable) {
                rda.writeChar(symbolsTableItem.getSymbol());
            }

            for (SymbolsTableItem symbolsTableItem : symbolsTable) {
                rda.writeInt(symbolsTableItem.getHuffmanCode().length());
                sbTableCodes.append(symbolsTableItem.getHuffmanCode());
            }

            byte[] codeBytes = getByteArray(sbTableCodes.toString());
            rda.write(codeBytes, 0, codeBytes.length);

            rda.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Read compressed .u21 file
     */
    public static U21File readU21File(String path) {
        StringBuilder sbPrefix = new StringBuilder(),
                sbName = new StringBuilder();

        SymbolsHashTable hashTable = new SymbolsHashTable(SymbolsHashTable.TableMode.KEY_HUFFMAN_CODE);
        String code = "";
        int nameLength = 0;

        try {
            RandomAccessFile rda = new RandomAccessFile(path, "r");
            /**
             * prefix
             */
            for (int i = 0; i < 3; i++) {
                sbPrefix.append(rda.readChar());
            }

            if (!sbPrefix.toString().equals("huf")) {
                throw new Exceptions.InvalidFileException();
            }

            /**
             * filename length
             */
            nameLength = rda.readInt();

            /**
             * file name
             */
            for (int i = 0; i < nameLength; i++) {
                sbName.append(rda.readChar());
            }

            /**
             * encoded message
             */
            int bitsLength = rda.readInt();

            int CodeBytesAmount = getMinimumBytesLength(bitsLength);
            byte[] codeBytes = new byte[CodeBytesAmount];
            rda.read(codeBytes, 0, CodeBytesAmount);
            code = toBitString(codeBytes);
            code = code.substring(0, bitsLength);

            /**
             * symbols table
             */
            int symbolsCount = rda.readInt();
            TableEntry[] tableEntries = new TableEntry[symbolsCount];

            for (int i = 0; i < symbolsCount; i++) {
                TableEntry entry = new TableEntry();
                entry.symbol = rda.readChar();
                tableEntries[i] = entry;
            }

            int bitsCount = 0;
            for (int i = 0; i < symbolsCount; i++) {
                tableEntries[i].bitsLength = rda.readInt();
                bitsCount += tableEntries[i].bitsLength;
            }

            int requiredBytes = getMinimumBytesLength(bitsCount);
            byte[] bytes = new byte[requiredBytes];
            rda.read(bytes, 0, requiredBytes);

            String bitsCode = toBitString(bytes);

            for (TableEntry entry: tableEntries) {
                try {
                    entry.code = bitsCode.substring(0, entry.bitsLength);
                }
                catch (StringIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
                bitsCode = bitsCode.substring(entry.bitsLength);
                hashTable.insert(entry.code, new SymbolsTableItem(entry.symbol, entry.code));
            }

            rda.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new U21File(
                nameLength,
                sbName.toString(),
                code,
                hashTable
        );

    }

    /**
     * Helper class to progressively build the table
     */
    private static class TableEntry {
        char symbol;
        String code;
        int bitsLength;
    }
}
