package Praktikum10;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class DownloadTask implements Runnable {

    private JProgressBar progressBar;

    public DownloadTask(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void run() {

        for (int i = 0; i <= 100; i++) {

            final int progress = i;

            SwingUtilities.invokeLater(() ->
                progressBar.setValue(progress)
            );

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}