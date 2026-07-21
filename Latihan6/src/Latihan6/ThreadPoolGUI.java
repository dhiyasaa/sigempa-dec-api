package Latihan6;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.concurrent.ExecutorService;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField threadCountField;
    private JTextField taskCountField;

    private JButton startButton;
    private JButton clearButton;

    private JTextArea logArea;

    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    private JLabel statusLabel;

    private ExecutorService threadPool;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ThreadPoolGUI frame = new ThreadPoolGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ThreadPoolGUI() {

        setTitle("Aplikasi ThreadPool dengan GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 647, 504);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setBounds(10, 10, 613, 51);
        controlPanel.setLayout(null);
        contentPane.add(controlPanel);

        JLabel lblThread = new JLabel("Jumlah Thread:");
        lblThread.setBounds(10, 19, 90, 13);
        controlPanel.add(lblThread);

        threadCountField = new JTextField("3");
        threadCountField.setBounds(95, 16, 45, 20);
        controlPanel.add(threadCountField);

        JLabel lblTask = new JLabel("Jumlah Tugas:");
        lblTask.setBounds(150, 19, 90, 13);
        controlPanel.add(lblTask);

        taskCountField = new JTextField("20");
        taskCountField.setBounds(240, 16, 45, 20);
        controlPanel.add(taskCountField);

        startButton = new JButton("Mulai Proses");
        startButton.setBackground(new Color(46, 204, 113));
        startButton.setFocusPainted(false);
        startButton.setBounds(300, 15, 139, 23);
        startButton.addActionListener(e -> startProcessing());
        controlPanel.add(startButton);

        clearButton = new JButton("Bersihkan Log");
        clearButton.setBounds(449, 14, 120, 23);
        clearButton.addActionListener(e -> clearLog());
        controlPanel.add(clearButton);

        JPanel panelKiri = new JPanel();
        panelKiri.setLayout(null);
        panelKiri.setBounds(10, 71, 195, 369);
        contentPane.add(panelKiri);

        JLabel lblStatus = new JLabel("Status Tugas");
        lblStatus.setBounds(10, 3, 100, 13);
        panelKiri.add(lblStatus);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 25, 175, 334);
        panelKiri.add(scrollPane);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        scrollPane.setViewportView(taskList);

        JPanel panelKanan = new JPanel();
        panelKanan.setLayout(null);
        panelKanan.setBounds(215, 71, 408, 369);
        contentPane.add(panelKanan);

        JLabel lblLog = new JLabel("Log Aktivitas");
        lblLog.setBounds(10, 3, 100, 13);
        panelKanan.add(lblLog);

        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(10, 25, 388, 334);
        panelKanan.add(scrollPane2);

        logArea = new JTextArea();
        logArea.setEditable(false);
        scrollPane2.setViewportView(logArea);

        statusLabel = new JLabel("Semua tugas selesai!");
        statusLabel.setBounds(10, 439, 250, 18);
        contentPane.add(statusLabel);
    }
    private void startProcessing() {

        try {

            int threadCount = Integer.parseInt(threadCountField.getText());
            int taskCount = Integer.parseInt(taskCountField.getText());

            if (threadCount < 1 || taskCount < 1) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah thread dan tugas harus lebih dari 0!",
                        "Input Tidak Valid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            startButton.setEnabled(false);

            taskListModel.clear();
            logArea.setText("");

            logArea.append("=== Memulai Proses Baru ===\n");
            logArea.append("ThreadPool dibuat dengan "
                    + threadCount + " worker threads!\n\n");

            statusLabel.setText("Memproses "
                    + taskCount + " tugas dengan "
                    + threadCount + " threads...");

            threadPool = Executors.newFixedThreadPool(threadCount);

            for (int i = 1; i <= taskCount; i++) {
                taskListModel.addElement("Task #" + i + " - Waiting");
            }

            for (int i = 1; i <= taskCount; i++) {
                Task task = new Task(i, logArea, taskListModel);
                threadPool.execute(task);
            }

            new Thread(() -> {

                threadPool.shutdown();

                try {

                    if (threadPool.awaitTermination(5, TimeUnit.MINUTES)) {

                        SwingUtilities.invokeLater(() -> {

                            logArea.append("\n=== Semua tugas selesai ===\n");
                            statusLabel.setText("Semua tugas selesai!");
                            startButton.setEnabled(true);

                        });

                    }

                } catch (InterruptedException e) {

                    threadPool.shutdownNow();

                }

            }).start();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Masukkan angka yang valid!",
                    "Input Tidak Valid",
                    JOptionPane.ERROR_MESSAGE);

        }

    }

    private void clearLog() {

        logArea.setText("");
        taskListModel.clear();
        statusLabel.setText("Log dibersihkan. Siap untuk proses baru.");

    }

    }