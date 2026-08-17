# University of Ghana Smart Campus Service Operations Optimizer

The Smart Campus Service Operations Optimizer is a Data Structures and Algorithms project developed for DCIT 204/308 at the University of Ghana. The system models real-world campus operations including maintenance request management, resource allocation, route optimization, service scheduling, and operational reporting.

The project demonstrates the practical application of custom data structures and algorithms through a Ghanaian university context. Core implementations include linked lists, stacks, queues, deques, priority queues, binary search trees, balanced trees, B-trees, hash tables, heaps, disjoint sets, and graph structures. The system further incorporates searching and sorting algorithms, shortest-path computation, minimum spanning tree generation, greedy optimization, dynamic programming techniques, database persistence, correctness testing, and empirical performance evaluation.

The objective is to provide an efficient decision-support platform capable of managing campus service requests, optimizing resource deployment, analyzing operational efficiency, and demonstrating the relationship between theoretical algorithmic complexity and real-world performance.

## Project Structure

```text
dcit-308-final-project/
├── .github/workflows/       # CI configuration
├── database/
│   ├── data/                # Sample CSV data
│   ├── migrations/          # Database migration files
│   └── schema.sql           # Database schema
├── docs/                    # Reports, screenshots, trace tables, and notes
├── resources/
│   ├── diagrams/            # Project diagrams
│   └── templates/           # Reusable project templates
├── scripts/                 # Utility and project scripts
├── src/
│   ├── main/
│   │   ├── java/com/ug/smartcampus/
│   │   │   ├── algorithm/   # Allocation, graph, search, and sorting algorithms
│   │   │   ├── config/      # Application and database configuration
│   │   │   ├── database/    # Database manager, DAOs, and repositories
│   │   │   ├── datastructures/ # Linear, nonlinear, tree, and graph structures
│   │   │   ├── experiment/  # Benchmarks and performance measurements
│   │   │   ├── model/       # Domain models
│   │   │   ├── service/     # Application services
│   │   │   └── util/        # Shared utilities
│   │   └── resources/       # Application properties and database resources
│   └── test/                # Unit, edge-case, and performance tests
├── pom.xml                 # Maven build and dependency configuration
└── README.md               # Project documentation
```

## Data Structures and Algorithms

The implementation demonstrates the following campus-oriented operations:

- Linear structures in `datastructures/linear`: linked lists, dynamic arrays, FIFO queues, LIFO stacks, deques, and circular queues.
- Nonlinear structures in `datastructures/nonlinear`: binary heaps and priority queues for severity-based scheduling, hash tables for average O(1) ID lookup, and disjoint sets for connectivity.
- Trees in `datastructures/trees`: binary search trees, a red-black-tree-backed ordered set, and a B-tree-style ordered index.
- Weighted adjacency-list graphs in `datastructures/graph`: BFS and DFS traversals, Dijkstra shortest paths for technician routing, and Kruskal/Prim minimum spanning trees.
- Search and sorting algorithms: linear search, binary search, insertion sort, selection sort, merge sort, and quicksort.
- Allocation algorithms: greedy priority-based resource matching and dynamic-programming knapsack selection.

`SchedulingService` uses a priority queue so higher-severity requests are planned first, `RoutingService` uses Dijkstra for weighted campus routes, and `ResourceAllocationService` applies greedy matching by building. The structures and algorithms have JUnit tests under `src/test/java`.

## Build and Run

Install JDK 17+ and Maven, then run the complete quality gate:

```bash
mvn verify
```

`verify` runs the JUnit suite, enables Java compiler lint warnings as errors, creates the shaded executable JAR, and writes a JaCoCo HTML coverage report to `target/site/jacoco/index.html`.

Load the supplied CSV fixture data once, then display the severity-ordered schedule:

```bash
java -jar target/smartcampus.jar --load-sample
java -jar target/smartcampus.jar --schedule
```

Running the JAR with no arguments opens the Smart Campus desktop dashboard. It provides a guided, visual workflow for data loading, priority scheduling, resource allocation, shortest-route exploration, and reporting/experiment steps.

The SQLite database is stored at `database/smart-campus.db`; its location can be changed in `src/main/resources/application/application.properties`. The loader is idempotent, so re-running `--load-sample` does not duplicate rows.

## Persistence and Workflow

`SampleDataLoader` imports buildings before resources and requests, preserving database foreign-key constraints. `PersistenceService` reads `RequestDao` and `ResourceDao` records into the lists consumed by the scheduling and allocation services. The integration test creates an isolated SQLite database, saves requests/resources, reloads them through the DAO boundary, verifies priority order, and verifies building-based allocations.

```text
CSV data -> SampleDataLoader -> SQLite / DAOs -> PersistenceService
                                                    |            |
                                             SchedulingService  ResourceAllocationService
```

## Performance Experiments

The repeatable command-line benchmarks print CSV-compatible timing tables using seven runs and the median result:

```bash
java -cp target/smartcampus.jar com.ug.smartcampus.experiment.benchmark.SortingBenchmark
java -cp target/smartcampus.jar com.ug.smartcampus.experiment.benchmark.GraphBenchmark
```

Sorting uses merge sort and should grow approximately `O(n log n)`. The graph test keeps a sparse connected graph with about `3V` edges and measures Dijkstra, whose expected complexity is `O(E + V log V)`. Save the output in `docs/performance-graphs/` and plot size against `median_ms` for the final submission.
