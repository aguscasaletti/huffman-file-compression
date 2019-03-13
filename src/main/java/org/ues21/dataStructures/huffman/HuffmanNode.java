package org.ues21.dataStructures.huffman;

/**
 *
 * @author Agustín Aliaga
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

    private String character;
    private int freq;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(
        final String character,
        final int freq,
        final HuffmanNode left,
        final HuffmanNode right
    ) {
        this.character = character;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public boolean esHoja() {
        return this.right == null && this.left == null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
            getClass() == obj.getClass() &&
            (this == obj || this.character.equals(((HuffmanNode) obj).character));
    }

    @Override
    public int compareTo(HuffmanNode o) {
       return this.getFreq() - o.getFreq();
    }
}
