import java.util.Arrays;

public class RecursiveScheduler {

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {

        int n = startTime.length;

        int[][] time = new int[n][3];

        for (int i = 0; i < n; i++) {
            time[i][0] = startTime[i];
            time[i][1] = endTime[i];
            time[i][2] = profit[i];
        }

        Arrays.sort(time, (a, b) -> a[0] - b[0]);

        Integer[] dp = new Integer[n + 1];

        return solve(0, time, n, dp);
    }

    private int solve(int i, int[][] time, int n, Integer[] dp) {

        if (i >= n)
            return 0;

        if (dp[i] != null)
            return dp[i];

        int skip = solve(i + 1, time, n, dp);

        int take = time[i][2];

        int next = search(i, time);

        if (next != -1)
            take += solve(next, time, n, dp);

        return dp[i] = Math.max(take, skip);
    }

    private int search(int i, int[][] arr) {

        int left = 0;
        int right = arr.length - 1;
        int ans = -1;

        int end = arr[i][1];

        while (left <= right) {

            int mid = (left + right) >>> 1;

            if (arr[mid][0] >= end) {
                ans = mid;
                right = mid - 1;
            } else
                left = mid + 1;
        }

        return ans;
    }
}
