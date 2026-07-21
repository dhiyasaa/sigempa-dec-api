package Latihan5;

import java.util.Calendar;

class Command implements Runnable {

    String taskName;

    public Command(String taskName) {
        this.taskName = taskName;
    }

    public void run() {
        try {

            System.out.println("Task name : "
                    + this.taskName
                    + " Current time : "
                    + Calendar.getInstance().get(Calendar.SECOND));

            Thread.sleep(2000);

            System.out.println("Executed : " + this.taskName
                    + " Current time : "
                    + Calendar.getInstance().get(Calendar.SECOND));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
