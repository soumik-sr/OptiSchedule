# OptiSchedule

### Enterprise Scheduling & Revenue Optimization Engine

> An optimization engine that selects the most profitable set of non-overlapping jobs from large-scale scheduling requests using Dynamic Programming and Binary Search.

---

##  Overview

Large organizations such as multinational corporations, consulting firms, hospitals, logistics companies, and technology companies handle thousands or even millions of scheduling requests.

Each request can represent:

- A client meeting
- A consulting session
- A doctor appointment
- A machine reservation
- A cloud/GPU allocation
- An interview slot
- A maintenance window
- A delivery operation

Every request has three properties:

```text
Start Time
End Time
Expected Profit / Revenue
```

The organization has a limited resource. For example, one consultant cannot attend two meetings at the same time.

Therefore, accepting every profitable request is impossible.

The actual business problem becomes:

> **Which combination of non-overlapping jobs should the organization accept to maximize total revenue?**

OptiSchedule solves this optimization problem efficiently using:

- Sorting
- Binary Search
- Dynamic Programming

The resulting algorithm operates in:

```text
O(n log n)
```

time complexity.

---

#  Real-World Business Scenario

Imagine a multinational consulting company receives:

```text
1,000,000 meeting requests per day
```

Each request contains:

| Request | Start | End | Revenue |
|---------|-------|-----|---------|
| Meeting A | 09:00 | 10:00 | $500 |
| Meeting B | 09:30 | 11:00 | $900 |
| Meeting C | 10:00 | 11:00 | $600 |
| Meeting D | 11:00 | 13:00 | $1,200 |

A consultant cannot attend overlapping meetings.

The company therefore needs an automated system capable of determining:

```text
Which meetings should we accept?
```

while maximizing:

```text
Total Revenue
```

OptiSchedule provides the optimization engine responsible for this decision.

---

#  Business Requirement

The system must:

1. Accept a large collection of scheduling requests.
2. Identify conflicting requests.
3. Select a set of non-overlapping requests.
4. Maximize total revenue.
5. Produce the result efficiently.
6. Scale to large datasets.
7. Avoid unnecessary repeated calculations.

---

#  Core Optimization Problem

Each job is represented as:

```text
Job = (Start Time, End Time, Profit)
```

For every job, the system has two choices:

### Option 1 — Reject the Job

Move to the next available job.

```text
Profit = Best Profit After Current Job
```

### Option 2 — Accept the Job

Add its profit and move to the first job that starts after it ends.

```text
Profit =
Current Job Profit
+
Best Profit From Next Compatible Job
```

Therefore:

```text
Best(i) =
max(
    Skip Job i,
    Take Job i + Best(Next Compatible Job)
)
```

This recurrence forms the basis of the Dynamic Programming solution.

---

#  System Architecture

```text
                    ┌─────────────────────────┐
                    │   Scheduling Requests   │
                    │                         │
                    │ Start | End | Revenue   │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │     Input Validation    │
                    │                         │
                    │ Validate array lengths  │
                    │ Validate job data       │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       Job Builder       │
                    │                         │
                    │ Start | End | Profit    │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │     Sort by Start       │
                    │         Time            │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │      Binary Search      │
                    │                         │
                    │ Find next compatible    │
                    │ job for every request   │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │  Dynamic Programming    │
                    │                         │
                    │ Take vs Skip            │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │   Optimal Schedule      │
                    │                         │
                    │ Maximum Total Revenue   │
                    └─────────────────────────┘
```

---

#  System Design

## 1. Input Layer

The input layer receives scheduling requests.

Each request contains:

```text
Start Time
End Time
Profit
```

In the current implementation, these are represented using three arrays:

```text
int[] startTime
int[] endTime
int[] profit
```

### Why is this required?

The optimization engine must know:

- When a job starts.
- When it finishes.
- How valuable it is.

Without these three values, the system cannot determine whether two jobs conflict or which job is more profitable.

---

# 2. Job Representation

The system converts the input into a unified structure:

```text
Job
 ├── start
 ├── end
 └── profit
```

Internally, the current optimized implementation stores jobs as:

```text
int[][] time
```

where:

```text
time[i][0] → Start Time
time[i][1] → End Time
time[i][2] → Profit
```

### Why is this required?

The three input arrays represent related information.

Combining them allows the scheduler to treat each request as one logical entity.

This makes sorting and optimization possible.

---

# 3. Sorting Layer

All jobs are sorted according to their start time.

```java
Arrays.sort(time, (a, b) -> a[0] - b[0]);
```

Example:

```text
Before Sorting

Job A → 10 - 12
Job B → 2  - 5
Job C → 6  - 9
Job D → 1  - 3
```

After sorting:

```text
Job D → 1 - 3
Job B → 2 - 5
Job C → 6 - 9
Job A → 10 - 12
```

### Why is sorting required?

The Binary Search component depends on the jobs being ordered by start time.

Once the jobs are sorted, the system can efficiently locate:

> The first job whose start time is greater than or equal to the current job's end time.

Without sorting, this search would require scanning the remaining jobs one by one.

That would increase the complexity from:

```text
O(log n)
```

to:

```text
O(n)
```

for each job.

---

# 4. Compatibility Search

For every selected job, the system must find the next job that does not overlap.

Suppose:

```text
Current Job

Start = 3
End   = 7
```

The next valid job must satisfy:

```text
Next Start >= 7
```

The system uses Binary Search to find the earliest such job.

```text
Current Job
     │
     ▼
End Time = 7

Sorted Jobs:

Start
  1
  3
  5
  7  ← First compatible job
  9
  12
```

### Why is Binary Search required?

A linear scan would require:

```text
O(n)
```

per job.

Binary Search reduces this to:

```text
O(log n)
```

per job.

For hundreds of thousands of scheduling requests, this difference becomes significant.

---

# 5. Dynamic Programming Layer

After identifying the next compatible job, the scheduler must decide:

```text
Take the current job
```

or

```text
Skip the current job
```

For job `i`:

```text
Take:

profit[i] + DP[next]

Skip:

DP[i + 1]
```

The final recurrence is:

```text
DP[i] = max(
    profit[i] + DP[next],
    DP[i + 1]
)
```

### Why is Dynamic Programming required?

Different scheduling decisions can lead to the same future state.

Without Dynamic Programming, the system would repeatedly solve the same subproblems.

Dynamic Programming stores previously computed results.

Therefore:

```text
Repeated Work
      ↓
Avoided
      ↓
Stored DP State
```

This changes an otherwise exponential recursive search into an efficient solution.

---

#  Iterative Architecture

The iterative implementation processes the DP states from right to left.

```text
Job n-1
   ↓
Job n-2
   ↓
Job n-3
   ↓
...
Job 0
```

The DP array is:

```text
int[] dp = new int[n + 1];
```

where:

```text
dp[i]
```

represents:

> Maximum profit obtainable using jobs from index `i` onward.

The final answer is:

```text
dp[0]
```

---

#  Recursive Architecture

The recursive implementation starts at the first job:

```text
solve(0)
```

and recursively explores:

```text
                    Job i
                   /     \
               Skip       Take
                │           │
              i + 1      next job
```

Memoization stores the result of each state:

```text
Integer[] dp
```

Therefore, each state is calculated only once.

---

#  Iterative vs Recursive Design

Both implementations use the same fundamental algorithm:

```text
Sorting
+
Binary Search
+
Dynamic Programming
```

Their difference is how the DP states are evaluated.

| Feature | Recursive | Iterative |
|---------|-----------|-----------|
| DP Strategy | Top-Down | Bottom-Up |
| Memoization | Yes | Not required |
| Recursion | Yes | No |
| Call Stack | O(n) | O(1) |
| DP Memory | O(n) | O(n) |
| Total Auxiliary Space | O(n) + Stack | O(n) |
| Function Call Overhead | Higher | Lower |
| Stack Overflow Risk | Possible | None |
| Cache Locality | Lower | Better |
| Production Suitability | Good | Better |

---

#  Why Iterative is More Efficient

Both approaches have the same Big-O complexity:

```text
O(n log n)
```

However, Big-O does not represent every runtime cost.

The iterative solution has several practical advantages.

### 1. No recursive function calls

The recursive implementation repeatedly performs:

```text
solve()
    ↓
solve()
        ↓
solve()
```

Each call creates a stack frame.

The iterative implementation uses a simple loop.

---

### 2. No recursion stack

The recursive implementation requires:

```text
Call Stack → O(n)
```

The iterative implementation does not.

Therefore, the iterative approach avoids stack overflow for deep recursion.

---

### 3. Better memory behavior

The iterative implementation uses:

```text
DP Array
```

while the recursive implementation uses:

```text
DP Array
+
Call Stack
```

---

### 4. Lower constant overhead

Recursive function calls introduce additional JVM overhead.

The iterative approach performs the same logical work through a loop.

Therefore, it is generally faster in practice.

---

#  Benchmark

The two implementations were executed using the same generated dataset.

```text
========================================
Jobs                : 1000

## Iterative Scheduler

Maximum Profit      : 485083
Execution Time      : 1.380 ms

## Recursive Scheduler

Maximum Profit      : 485083
Execution Time      : 1.682 ms

Winner              : Iterative Scheduler
```

### Result Analysis

Both implementations produced:

```text
Maximum Profit = 485083
```

This confirms that both implementations produce the same optimal result for the benchmark dataset.

The iterative implementation completed in:

```text
1.380 ms
```

while the recursive implementation required:

```text
1.682 ms
```

The iterative implementation was therefore approximately:

```text
17.9%
```

faster for this benchmark run.

The difference is caused primarily by recursive call overhead and additional stack operations.

> Benchmark results are hardware, JVM, and workload dependent. The result above represents one benchmark run and should not be interpreted as a universal performance guarantee.

---

#  Complexity Analysis

Let:

```text
n = Number of Jobs
```

## Sorting

Jobs are sorted by start time:

```text
O(n log n)
```

---

## Binary Search

Each job performs one Binary Search:

```text
O(log n)
```

For `n` jobs:

```text
O(n log n)
```

---

## Dynamic Programming

Every job is processed once:

```text
O(n)
```

---

## Overall Time Complexity

Therefore:

```text
O(n log n)
+
O(n log n)
+
O(n)
```

which simplifies to:

```text
┌─────────────────┐
│ O(n log n)      │
└─────────────────┘
```

---

#  Space Complexity

The scheduler stores:

```text
Jobs      → O(n)
DP Array  → O(n)
```

Therefore:

```text
O(n)
```

The recursive version additionally requires a recursion stack:

```text
O(n)
```

So its practical memory usage is higher.

---

#  Why Each Component Exists

| Component | Requirement |
|-----------|-------------|
| Input Arrays | Represent scheduling requests |
| Job Structure | Group related job information |
| Sorting | Enables efficient compatibility search |
| Binary Search | Finds next compatible job efficiently |
| DP Array | Stores optimal subproblem results |
| Iterative DP | Removes recursion overhead |
| Recursive DP | Provides a top-down reference implementation |
| Benchmark | Measures real execution performance |
| Main | Provides an executable demonstration |

---

#  Enterprise Scalability

The main reason for using Binary Search + Dynamic Programming is scalability.

A naive approach could compare every job with every other job:

```text
O(n²)
```

For:

```text
n = 1,000,000
```

this becomes impractical.

OptiSchedule instead operates in:

```text
O(n log n)
```

This makes the algorithm substantially more suitable for large scheduling datasets.

---

#  Potential Applications

Although the project is demonstrated using generic jobs, the same optimization engine can be applied to many domains.

| Domain | Scheduling Resource |
|--------|---------------------|
| Consulting | Consultants |
| Healthcare | Doctors |
| Cloud Computing | GPUs / Servers |
| Manufacturing | Machines |
| Airlines | Runways |
| Logistics | Vehicles |
| Education | Classrooms |
| Corporate | Meeting Rooms |
| Recruitment | Interview Panels |
| Maintenance | Engineering Teams |

The underlying optimization problem remains the same:

```text
Limited Resource
       +
Conflicting Requests
       +
Different Profits
       ↓
Maximum Possible Revenue
```

---

#  Project Structure

```text
OptiSchedule/
│
├── README.md
├── LICENSE
├── .gitignore
│
├── src/
│   ├── Job.java
│   ├── IterativeScheduler.java
│   ├── RecursiveScheduler.java
│   ├── Main.java
│   └── Benchmark.java
│
├── docs/
│   ├── architecture.md
│   ├── complexity.md
│   └── benchmark.md
│
├── examples/
│   ├── sample_input.txt
│   └── sample_output.txt
│
└── assets/
```

---

#  Running the Project

Compile the Java source files:

```bash
javac src/*.java
```

Run the demonstration:

```bash
java -cp src Main
```

Run the benchmark:

```bash
java -cp src Benchmark
```

---

#  Example

Input:

```text
Start Time = [1, 2, 3, 3]
End Time   = [3, 4, 5, 6]
Profit     = [50, 10, 40, 70]
```

The optimal selection is:

```text
Job 1: 1 → 3   Profit = 50
Job 4: 3 → 6   Profit = 70
```

Total:

```text
50 + 70 = 120
```

Output:

```text
120
```

---

#  Design Considerations

## Non-Overlapping Boundary

A job is considered compatible when:

```text
nextStart >= currentEnd
```

Therefore, these jobs can coexist:

```text
Job A: 09:00 → 10:00
Job B: 10:00 → 11:00
```

because Job B starts exactly when Job A ends.

---

## Deterministic Benchmarking

The benchmark uses a fixed random seed when generating test data.

This ensures that the same dataset can be reproduced.

That is important when comparing two implementations because both must receive identical workloads.

---

## License

This project is licensed under the MIT License.
