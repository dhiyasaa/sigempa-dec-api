class CookingJob implements Runnable {

    private String task;

    CookingJob(String task) {
        this.task = task;
    }

    public void run() {
        System.out.println(task + " is being prepared by "
                + Thread.currentThread().getName());
    }

}