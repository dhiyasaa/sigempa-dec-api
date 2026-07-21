package Latihan6;

import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Task implements Runnable {

    private int taskId;
    private JTextArea logArea;
    private DefaultListModel<String> taskListModel;

    public Task(int taskId, JTextArea logArea, DefaultListModel<String> taskListModel) {

        this.taskId = taskId;
        this.logArea = logArea;
        this.taskListModel = taskListModel;

    }

    @Override
    public void run() {

        SwingUtilities.invokeLater(() -> {

            logArea.append("Task #" + taskId
                    + " dimulai oleh "
                    + Thread.currentThread().getName()
                    + "\n");

            updateTaskList("Task #" + taskId + " - Running");

        });

        try {

            int processingTime = new Random().nextInt(3000) + 1000;

            Thread.sleep(processingTime);

            SwingUtilities.invokeLater(() -> {

                logArea.append("Task #" + taskId
                        + " selesai oleh "
                        + Thread.currentThread().getName()
                        + " (waktu: "
                        + processingTime + " ms)\n");

                updateTaskList("Task #" + taskId + " - Completed ✓");

            });

        } catch (InterruptedException e) {

            SwingUtilities.invokeLater(() -> {

                logArea.append("Task #" + taskId
                        + " terganggu!\n");

                updateTaskList("Task #" + taskId + " - Interrupted");

            });

            Thread.currentThread().interrupt();

        }

    }

    private void updateTaskList(String status) {

        for (int i = 0; i < taskListModel.size(); i++) {

            if (taskListModel.get(i).startsWith("Task #" + taskId)) {

                taskListModel.set(i, status);
                break;

            }

        }

    }

}