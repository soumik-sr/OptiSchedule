import java.util.Arrays;

public class IterativeScheduler {

    //inserting a arr as input
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;

        int[][] time = new int[n][3];

        for (int i = 0; i < n; i++) {
            time[i][0] = startTime[i];
            time[i][1] = endTime[i];
            time[i][2] = profit[i];
        }

        Arrays.sort(time, (a, b) -> a[0] - b[0]);

        int[] dp = new int[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            int take = time[i][2];

            int next = search(i, time);

            if (next != -1)
                take += dp[next];

            int skip = dp[i + 1];

            dp[i] = Math.max(take, skip);
        }

        return dp[0];
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
