package org.ues21.view;

import com.sun.tools.javac.util.Pair;
import org.ues21.dataStructures.huffman.Huffman;
import org.ues21.support.U21File;
import org.ues21.support.HuffmanEncoder;
import org.ues21.support.Exceptions;
import org.ues21.support.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Form extends JFrame{
    private JPanel panel1;
    private JTextField comprInputPathTF;
    private JButton comprOpenInputBtn;
    private JTextField comprOutputPathTF;
    private JButton comprOpenOutputDirBtn;
    private JButton descOpenInputButton;
    private JButton descOpenOutputDirButton;
    private JTextField descInputPathTF;
    private JTextField descOutputPathTF;
    private JButton compressBtn;
    private JButton decompressBtn;

    private String[] SUGGESTED_EXTENSIONS = {
            "txt",
            "js",
            "json",
            "csv",
            "md",
            "html",
            "xml",
            "css"
    };

    Form() {
        this.setSize(500, 300);
        this.setTitle("Compresor Huffman");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.panel1);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        this.setVisible(true);

        comprOpenInputBtn.addActionListener(e -> {
            this.onComprAbrirInputClick();
        });
        comprInputPathTF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onComprAbrirInputClick();
            }
        });

        comprOpenOutputDirBtn.addActionListener(e -> {
            this.onComprAbrirOutputClick();
        });
        comprOutputPathTF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onComprAbrirOutputClick();
            }
        });

        compressBtn.addActionListener(e -> {
            ProgressDialog progressDialog = new ProgressDialog(
                    new CompressWorker(this.comprInputPathTF.getText()),
                    ProgressDialog.WorkerMode.COMPRESS,
                    this.comprOutputPathTF.getText()
            );

            progressDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    comprInputPathTF.setText("");
                    comprOutputPathTF.setText("");
                    descInputPathTF.setText("");
                    descOutputPathTF.setText("");
                }
            });
        });

        descOpenInputButton.addActionListener(e -> {
            this.onDescAbrirInputClick();
        });

        descInputPathTF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onDescAbrirInputClick();
            }
        });

        descOpenOutputDirButton.addActionListener(e -> {
            this.onDescAbrirOutputClick();
        });

        descOutputPathTF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onDescAbrirOutputClick();
            }
        });

        decompressBtn.addActionListener(e -> {
            ProgressDialog progressDialog = new ProgressDialog(
                    new DecompressWorker(this.descInputPathTF.getText()),
                    ProgressDialog.WorkerMode.UNCOMPRESS,
                    this.descOutputPathTF.getText()
            );

            progressDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    super.windowClosed(e);
                    comprInputPathTF.setText("");
                    comprOutputPathTF.setText("");
                    descInputPathTF.setText("");
                    descOutputPathTF.setText("");
                }
            });
        });
    }

    private void onComprAbrirInputClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Supported Files", SUGGESTED_EXTENSIONS));
        int returnVal = fileChooser.showOpenDialog(Form.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.comprInputPathTF.setText(file.getAbsolutePath());
            this.comprOutputPathTF.setText(
                    generarOutputPath(file.getAbsolutePath())
            );
            this.compressBtn.setEnabled(true);
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void onComprAbrirOutputClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(Form.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.comprOutputPathTF.setText(file.getAbsolutePath());
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void onDescAbrirInputClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("U21 Files", "u21"));
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(Form.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.descInputPathTF.setText(file.getAbsolutePath());
            this.descOutputPathTF.setText(
                    generarOutputPath(file.getAbsolutePath())
            );
            this.decompressBtn.setEnabled(true);
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void onDescAbrirOutputClick() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int returnVal = fileChooser.showOpenDialog(Form.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.descOutputPathTF.setText(file.getAbsolutePath());
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private String generarOutputPath(String inputPath) {
        return inputPath.substring(0, inputPath.lastIndexOf(File.separator));
    }

    public static void main (String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Form();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public class DecompressWorker extends SwingWorker<Object, Pair<String, String>> {
        private String inputPath;

        public DecompressWorker(String inputPath) {
            this.inputPath = inputPath;
        }

        @Override
        protected Pair<String, String> doInBackground() {
            U21File u21File = FileUtils.readU21File(this.inputPath);
            String mensajeOriginal = HuffmanEncoder.decode(u21File, new ProgressListener() {
                @Override
                public void onProgressUpdate(long progress) {
                    setProgress((int) progress);
                }

                @Override
                public void onComplete() {
                    setProgress(100);
                }
            });

            return new Pair<>(
                    mensajeOriginal,
                    u21File.getName()
            );
        }

        public String getInputPath() {
            return inputPath;
        }
    }

    public class CompressWorker extends SwingWorker<Object, Huffman> {
        private String inputPath;

        public CompressWorker(String inputPath) {
            this.inputPath = inputPath;
        }

        @Override
        protected Huffman doInBackground() {
            String content = null;
            try {
                content = FileUtils.readFile(this.inputPath);
            } catch (FileNotFoundException | Exceptions.InvalidFileException e) {
                e.printStackTrace();
            }
            Huffman arbol = new Huffman(content, new ProgressListener() {
                @Override
                public void onProgressUpdate(long progress) {
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress((int) progress);
                }

                @Override
                public void onComplete() {
                    setProgress(100);
                }

            });

            return arbol;
        }

        public String getInputPath() {
            return inputPath;
        }
    }
}
