class CookingTask extends Thread {

    private String task;

    CookingTask(String task) {
        this.task = task;
    }

    public void run() {
        System.out.println(task + " is being prepared by "
                + Thread.currentThread().getName());
    }

}