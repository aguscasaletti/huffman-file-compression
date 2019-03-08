package org.ues21.aed2.vista;

import org.ues21.aed2.soporte.first.FileUtils;
import org.ues21.aed2.estructuras.arbol.ArbolHuffman;
import org.ues21.aed2.soporte.first.CodificadorHuffman;
import org.ues21.aed2.soporte.first.ArchivoU21;
import org.ues21.aed2.soporte.ProgressListener;
import org.ues21.aed2.soporte.third.ArbolHuffmanThird;
import org.ues21.aed2.soporte.third.ArchivoU21Third;
import org.ues21.aed2.soporte.third.CodificadorHuffmanThird;
import org.ues21.aed2.soporte.third.FileUtilsThird;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class View extends JFrame implements PropertyChangeListener {

    private JProgressBar progressBar;
    private JPanel titleRow, compressButtonsRow, statusRow, actionsRow;
    private JLabel statusLabel;

    private ComprimirWorker comprimirWorker;
    private DescomprimirWorker descomprimirWorker;
    private JButton guardarButton;

    private boolean compress = true;

    private ArbolHuffmanThird arbolHuffman;
    private String mensajeOriginal;

    private void visibilityInit() {
        this.titleRow.setVisible(true);
        this.compressButtonsRow.setVisible(true);
        this.progressBar.setVisible(false);
        this.actionsRow.setVisible(false);
        this.statusRow.setVisible(false);
    }

    private void setInProgress() {
        this.titleRow.setVisible(false);
        this.compressButtonsRow.setVisible(false);
        this.progressBar.setVisible(true);
        this.statusLabel.setText("Procesando el archivo...");
        this.statusRow.setVisible(true);
        this.actionsRow.setVisible(false);
    }

    private void setSuccess() {
        this.titleRow.setVisible(false);
        this.compressButtonsRow.setVisible(false);
        this.progressBar.setVisible(false);
        this.statusLabel.setText("¡Archivo procesado con éxito!");
        this.statusRow.setVisible(true);
        this.actionsRow.setVisible(true);
    }

    public View() {
        this.setSize(500, 250);
        BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.setTitle("Compresor Huffman");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        JButton comprimirButton = new JButton("Comprimir un archivo");
        comprimirButton.addActionListener(event -> {
            compress = true;
            this.startIndeterminate();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.setMultiSelectionEnabled(false);
            int returnVal = fileChooser.showOpenDialog(View.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                comprimirWorker = new ComprimirWorker(file.getAbsolutePath());
                comprimirWorker.addPropertyChangeListener(this);
                comprimirWorker.execute();
            } else {
                System.out.println("Open command cancelled by user.");
                this.stopIndeterminate();
            }
        });
        comprimirButton.setSize(100, 80);

        JButton descomprimirButton = new JButton("Descomprimir un archivo");
        descomprimirButton.addActionListener(event -> {
            compress = false;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("U21 Files", "u21"));
            fileChooser.setMultiSelectionEnabled(false);
            int returnVal = fileChooser.showOpenDialog(View.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.startIndeterminate();
                this.descomprimirWorker = new DescomprimirWorker(fileChooser.getSelectedFile().getAbsolutePath());
                descomprimirWorker.addPropertyChangeListener(this);
                descomprimirWorker.execute();
            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        descomprimirButton.setSize(100, 80);

        JFileChooser saveFile = new JFileChooser();
        saveFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saveFile.setAcceptAllFileFilterUsed(false);
        saveFile.setDialogTitle("Guardar nuevo archivo en directorio");

        titleRow = new JPanel();
        titleRow.setMaximumSize(new Dimension(this.getWidth(), 50));
        titleRow.add(new JLabel("Seleccioná la operación que querés realizar"));
        this.add(titleRow);

        compressButtonsRow = new JPanel();
        compressButtonsRow.setMaximumSize(new Dimension(this.getWidth(), 60));
        compressButtonsRow.add(comprimirButton);
        compressButtonsRow.add(descomprimirButton);
        this.add(compressButtonsRow);

        progressBar = new JProgressBar();
        progressBar.setSize(50, 50);
        progressBar.setValue(50);
        this.add(progressBar);

        statusRow = new JPanel();
        statusRow.setMaximumSize(new Dimension(this.getWidth(), 60));
        statusLabel = new JLabel("");
        statusRow.add(statusLabel);
        this.add(statusRow);

        guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(event -> {
            int returnVal = saveFile.showOpenDialog(View.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = saveFile.getSelectedFile();
                System.out.println("Opening: " + file.getName() + ".");

                if (this.compress) {
                    this.startIndeterminate();
                    String codigo = CodificadorHuffmanThird.codificar(arbolHuffman.getListaSimbolos(), arbolHuffman.getTexto());
                    FileUtilsThird.escribirU21(file.getAbsolutePath() + "/testAgus.u21", codigo, arbolHuffman.getListaSimbolos());
                    this.stopIndeterminate();
                } else {
                    this.startIndeterminate();
                    FileUtilsThird.escribir(saveFile.getSelectedFile().getAbsolutePath() + "/kalsdkasda.txt", this.mensajeOriginal);
                    this.stopIndeterminate();
                }

                this.visibilityInit();
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(new File(saveFile.getSelectedFile().getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Open command cancelled by user.");
            }
        });
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(event -> {
            this.visibilityInit();
        });
        actionsRow = new JPanel();
        actionsRow.setMaximumSize(new Dimension(this.getWidth(), 60));
        actionsRow.add(volverButton);
        actionsRow.add(guardarButton);

        this.add(actionsRow);
        this.visibilityInit();
        this.setVisible(true);
    }

    public static void main (String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new View();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startIndeterminate() {
        this.progressBar.setIndeterminate(true);
        this.progressBar.setVisible(true);
    }

    private void stopIndeterminate() {
        this.progressBar.setIndeterminate(false);
        this.progressBar.setVisible(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name.equals("progress")) {
            int progress = (int) evt.getNewValue();
            System.out.println(progress);
            if (progress > 0) {
                this.progressBar.setVisible(true);
                this.progressBar.setIndeterminate(false);
                this.progressBar.setValue(progress);
                this.setInProgress();
            }
            repaint();
        } else if (name.equals("state")) {
            SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
            switch (state) {
                case DONE:
                    if (this.compress) {
                        try {
                            arbolHuffman = (ArbolHuffmanThird) this.comprimirWorker.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        this.setSuccess();
                    } else {
                        try {
                            this.mensajeOriginal = (String) this.descomprimirWorker.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        this.setSuccess();
                    }
                    this.guardarButton.doClick();
                    break;
            }
        }
    }

    public class DescomprimirWorker extends SwingWorker<Object, String> {
        private String inputPath;

        public DescomprimirWorker(String inputPath) {
            this.inputPath = inputPath;
        }

        @Override
        protected String doInBackground() {
            ArchivoU21Third archivoU21 = FileUtilsThird.leerU21(this.inputPath);
            String mensajeOriginal = CodificadorHuffmanThird.decodificar(archivoU21, new ProgressListener() {
                @Override
                public void onProgressUpdate(long progress) {
                    if (progress == -98) {
                        System.out.println("");
                    }
                    setProgress((int) progress);
                }

                @Override
                public void onComplete() {
                    setProgress(100);
                }
            });

            return mensajeOriginal;
        }
    }

    public class ComprimirWorker extends SwingWorker<Object, ArbolHuffman> {
        private String filePath;

        public ComprimirWorker(String filePath) {
            this.filePath = filePath;
        }

        @Override
        protected ArbolHuffman doInBackground() {
            String content = FileUtils.leer(this.filePath);
            ArbolHuffman arbol = new ArbolHuffman(content, new ProgressListener() {
                @Override
                public void onProgressUpdate(long progress) {
                    try {
                        Thread.sleep(10);
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
    }
}
