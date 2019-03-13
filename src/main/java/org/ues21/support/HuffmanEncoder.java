package org.ues21.support;

import org.ues21.dataStructures.hashTable.SymbolsTableItem;
import org.ues21.dataStructures.hashTable.SymbolsHashTable;
import org.ues21.view.ProgressListener;

/**
 * Class that encodes and decodes message
 */
public class HuffmanEncoder {

    public static String encode(final SymbolsHashTable symbolsTable, final String message) {
        char[] vect = message.toCharArray();
        char c;
        StringBuilder sbCod = new StringBuilder();
        for (int i = 0; i < vect.length; i++) {
            c = vect[i];
            SymbolsTableItem symbolsTableItem = symbolsTable.find(String.valueOf(c));
            if (symbolsTableItem != null) {
                sbCod.append(symbolsTableItem.getHuffmanCode());
            }
        }
        return sbCod.toString();
    }

    public static String decode(final U21File file, final ProgressListener progressListener) {
        char[] vect = file.getCode().toCharArray();
        StringBuilder sbMessage = new StringBuilder();
        StringBuilder sbLookup = new StringBuilder();

        for (long i = 0; i < vect.length; i++) {
            if (progressListener != null && i > 100 && i % 100 == 0) {
                progressListener.onProgressUpdate(i * 100 / vect.length);
            }
            sbLookup.append(vect[(int) i]);
            SymbolsTableItem symbolsTableItem = file.getTablaSimbolos().find(sbLookup.toString());
            if (symbolsTableItem != null) {
                sbMessage.append(symbolsTableItem.getSymbol());
                sbLookup.setLength(0);
            }
        }

        if (progressListener != null) {
            progressListener.onComplete();
        }
        return sbMessage.toString();
    }

    public static String decode(final U21File file) {
        return HuffmanEncoder.decode(file, null);
    }
}
