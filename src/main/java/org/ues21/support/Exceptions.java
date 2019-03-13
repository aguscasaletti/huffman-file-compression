package org.ues21.support;

public class Exceptions {
    public static String COMMAND_HELP = "The command has to be like this:\n For compression:\thu21 -c inputPath [outputPath]\n For decompression:\thu21 -d inputPath [outputPath]";

    public static class InvalidFileException extends Exception {
        public InvalidFileException() {
            super("Invalid file! Please select another one.");
        }
    }

    public static class InvalidCommandException extends Exception {
        public InvalidCommandException() {
            super(COMMAND_HELP);
        }
    }
}
