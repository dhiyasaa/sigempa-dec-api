class Cooking {

    private String task;

    Cooking(String task) {
        this.task = task;
    }

    public void run() {
        System.out.println(task + " is being prepared");
    }

}