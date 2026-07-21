package Praktikum10;

import java.awt.EventQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class DownloadManagerGUI extends JFrame {

    private JPanel contentPane;

    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private JProgressBar progressBar3;

    private JButton btnDownload;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {

                DownloadManagerGUI frame = new DownloadManagerGUI();
                frame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();

            }

        });

    }

    public DownloadManagerGUI() {

        setTitle("Download Manager App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,450,250);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Download Manager App");
        lblTitle.setBounds(145,10,180,20);
        contentPane.add(lblTitle);

        JLabel lbl1 = new JLabel("File 1");
        lbl1.setBounds(30,50,50,20);
        contentPane.add(lbl1);

        progressBar1 = new JProgressBar();
        progressBar1.setBounds(90,50,250,20);
        progressBar1.setStringPainted(true);
        contentPane.add(progressBar1);

        JLabel lbl2 = new JLabel("File 2");
        lbl2.setBounds(30,85,50,20);
        contentPane.add(lbl2);

        progressBar2 = new JProgressBar();
        progressBar2.setBounds(90,85,250,20);
        progressBar2.setStringPainted(true);
        contentPane.add(progressBar2);

        JLabel lbl3 = new JLabel("File 3");
        lbl3.setBounds(30,120,50,20);
        contentPane.add(lbl3);

        progressBar3 = new JProgressBar();
        progressBar3.setBounds(90,120,250,20);
        progressBar3.setStringPainted(true);
        contentPane.add(progressBar3);

        btnDownload = new JButton("Downloading");
        btnDownload.setBounds(145,165,130,30);
        contentPane.add(btnDownload);

        btnDownload.addActionListener(e -> startDownload());

    }

    private void startDownload() {

        btnDownload.setEnabled(false);

        progressBar1.setValue(0);
        progressBar2.setValue(0);
        progressBar3.setValue(0);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(new DownloadTask(progressBar1));
        executor.execute(new DownloadTask(progressBar2));
        executor.execute(new DownloadTask(progressBar3));

        executor.shutdown();

    }

}
