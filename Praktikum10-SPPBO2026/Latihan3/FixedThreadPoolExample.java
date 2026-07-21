package Latihan3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolExample {

    public static void main(String[] args) {

        int threadCount = 5;
        int taskCount = 5;

        ExecutorService service =
                Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < taskCount; i++) {

            service.execute(() -> {

                System.out.println(
                        "Printing document by thread : "
                        + Thread.currentThread().getName());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

        }

        service.shutdown();

    }

}
