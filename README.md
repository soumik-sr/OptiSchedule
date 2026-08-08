# OptiSchedule

Enterprise-grade Scheduling Optimization Engine built using Dynamic Programming and Binary Search.

## Overview

OptiSchedule maximizes total profit by selecting a subset of non-overlapping jobs.
Each job contains:

- Start Time
- End Time
- Profit

The engine sorts jobs, uses binary search to locate the next compatible job, and
applies Dynamic Programming to compute the optimal answer.

## Features

- O(n log n) Time Complexity
- O(n) Space Complexity
- Bottom-Up Iterative DP
- Top-Down Recursive DP
- Binary Search Optimization
- Enterprise Scheduling Use Cases

## Workflow

Incoming Jobs
    ↓
Sort by Start Time
    ↓
Binary Search
    ↓
Dynamic Programming
    ↓
Maximum Profit Schedule

## Complexity

| Operation | Complexity |
|-----------|------------|
| Sorting | O(n log n) |
| Binary Search | O(log n) |
| DP | O(n) |
| Total | O(n log n) |
| Space | O(n) |

## Recursive vs Iterative

| Property | Recursive | Iterative |
|-----------|-----------|-----------|
| Stack | O(n) | O(1) |
| Function Calls | Yes | No |
| Memory | Higher | Lower |
| Practical Speed | Slower | Faster |

The iterative version avoids recursion overhead and stack usage while maintaining
the same asymptotic complexity, making it preferable for production workloads.

## Project Structure

```
src/
    Job.java
    IterativeScheduler.java
    RecursiveScheduler.java
    Main.java
    Benchmark.java
```

## License

MIT
