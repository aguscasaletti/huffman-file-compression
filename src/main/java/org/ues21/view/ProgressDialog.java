package org.ues21.view;

import com.sun.tools.javac.util.Pair;
import org.ues21.dataStructures.huffman.Huffman;
import org.ues21.support.HuffmanEncoder;
import org.ues21.support.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ProgressDialog extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private SwingWorker swingWorker;
    private WorkerMode workerMode;
    private String outputPath = "";

    public enum WorkerMode {
            COMPRESS,
            UNCOMPRESS
    }

    public ProgressDialog(SwingWorker swingWorker, WorkerMode workerMode, String outputPath) {
        this.swingWorker = swingWorker;
        this.workerMode = workerMode;
        this.outputPath = outputPath;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height /2);

        buttonOK.addActionListener(e -> onOK());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.swingWorker.addPropertyChangeListener(this);
        this.progressBar.setIndeterminate(true);
        this.swingWorker.execute();
        this.setVisible(true);
    }

    private void onOK() {
        dispose();
        if (Desktop.isDesktopSupported() && !this.outputPath.isEmpty()) {
            try {
                Desktop.getDesktop().open(new File(this.outputPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            }
            repaint();
        } else if (name.equals("state")) {
            SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
            if (state == SwingWorker.StateValue.DONE) {
                if (this.workerMode == WorkerMode.COMPRESS) {
                    try {
                        this.progressBar.setIndeterminate(true);
                        Huffman huffman = (Huffman) this.swingWorker.get();
                        String codigo = HuffmanEncoder.encode(huffman.getSymbolsTable(), huffman.getText());
                        String inputPath = ((Form.CompressWorker) this.swingWorker).getInputPath();
                        String nombreOriginal = inputPath.substring(inputPath.lastIndexOf(File.separator) + 1);

                        String output = this.outputPath +
                                "/" +
                                (nombreOriginal.lastIndexOf(".") == -1
                                ? nombreOriginal
                                : nombreOriginal.substring(0, nombreOriginal.lastIndexOf("."))) +
                                ".u21";

                        FileUtils.writeU21File(output, codigo, huffman.getSymbolsTable(), nombreOriginal);
                        this.progressBar.setVisible(false);
                        this.statusLabel.setText("File compressed successfully!");
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        this.progressBar.setIndeterminate(true);
                        Pair<String, String> res = (Pair<String, String>) this.swingWorker.get();
                        String mensajeOriginal = res.fst;
                        String nombreOriginal = res.snd;
                        String path = this.outputPath + "/" + nombreOriginal;
                        FileUtils.writeFile(path, mensajeOriginal);

                        this.progressBar.setVisible(false);
                        this.statusLabel.setText("File uncompressed successfully!");
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
