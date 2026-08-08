public class Main {
    public static void main(String[] args) {

        int[] start = {1,2,3,3};
        int[] end = {3,4,5,6};
        int[] profit = {50,10,40,70};

        IterativeScheduler scheduler = new IterativeScheduler();

        System.out.println(scheduler.jobScheduling(start,end,profit));
    }
}
