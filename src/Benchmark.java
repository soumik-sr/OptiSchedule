import java.util.Random;

public class Benchmark {

    public static void main(String[] args) {

        int n = 1000;

        int[] start = new int[n];
        int[] end = new int[n];
        int[] profit = new int[n];

        Random random = new Random(42);

        for (int i = 0; i < n; i++) {
            start[i] = random.nextInt(1_000_000);
            end[i] = start[i] + random.nextInt(100) + 1;
            profit[i] = random.nextInt(1000) + 1;
        }

        IterativeScheduler iterative = new IterativeScheduler();
        RecursiveScheduler recursive = new RecursiveScheduler();


        iterative.jobScheduling(start, end, profit);
        recursive.jobScheduling(start, end, profit);

        long startTime = System.nanoTime();
        int iterativeAnswer = iterative.jobScheduling(start, end, profit);
        long endTime = System.nanoTime();

        long iterativeTime = endTime - startTime;

        startTime = System.nanoTime();
        int recursiveAnswer = recursive.jobScheduling(start, end, profit);
        endTime = System.nanoTime();

        long recursiveTime = endTime - startTime;

        System.out.println("========================================");
        System.out.println("          OptiSchedule Benchmark");
        System.out.println("========================================");
        System.out.println("Jobs                : " + n);
        System.out.println();

        System.out.println("Iterative Scheduler");
        System.out.println("-------------------");
        System.out.println("Maximum Profit      : " + iterativeAnswer);
        System.out.printf("Execution Time      : %.3f ms%n", iterativeTime / 1_000_000.0);

        System.out.println();

        System.out.println("Recursive Scheduler");
        System.out.println("-------------------");
        System.out.println("Maximum Profit      : " + recursiveAnswer);
        System.out.printf("Execution Time      : %.3f ms%n", recursiveTime / 1_000_000.0);

        System.out.println();

        if (iterativeTime < recursiveTime) {
            System.out.println("Winner              : Iterative Scheduler");
        } else {
            System.out.println("Winner              : Recursive Scheduler");
        }
    }
}