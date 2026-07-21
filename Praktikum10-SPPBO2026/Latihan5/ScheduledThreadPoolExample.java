package Latihan5;

import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExample {

    public static void main(String[] args) {

        int corePoolSize = 2;

        try (ScheduledThreadPoolExecutor threadPool =
                new ScheduledThreadPoolExecutor(corePoolSize)) {

            Runnable task1 = new Command("task1");
            Runnable task2 = new Command("task2");

            System.out.println("Current time:"
                    + Calendar.getInstance().get(Calendar.SECOND));

            threadPool.scheduleAtFixedRate(task1, 2, 8, TimeUnit.SECONDS);

            threadPool.scheduleWithFixedDelay(task2, 5, 5, TimeUnit.SECONDS);

            try {
                Thread.sleep(30000); // Wait for 30 seconds
            } catch (Exception e) {
                e.printStackTrace();
            }

            threadPool.shutdown();

        }

    }

}