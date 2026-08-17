# Week 3: Testing, Optimization, and Reporting

## Evidence of integration

`PersistenceSchedulingIntegrationTest` initializes a temporary SQLite schema, inserts two buildings, two resources, and two requests through the DAOs. It reloads those records using `PersistenceService`, confirms that priority 5 is scheduled before priority 2, and confirms resources are matched to requests at their respective buildings.

| Request | Priority | Building | Assigned resource | Result |
| --- | ---: | ---: | --- | --- |
| Power fault | 5 | Engineering | Electrical team | scheduled first |
| Leaking tap | 2 | Library | Plumbing team | scheduled second |

## Reproducible measurements

Run `mvn package` and then execute the two benchmark commands in the root README. Each prints a CSV header and one median runtime for each increasing input size. Median timing reduces the effect of warm-up and background processes. The final graph should use `size`/`vertices` on the x-axis and `median_ms` on the y-axis.

The expected comparison is qualitative: merge sort has `O(n log n)` growth, while Dijkstra on the deliberately sparse benchmark graph has `O(E + V log V)` growth. Absolute timing varies by hardware, Java version, and machine load, so the report records the environment alongside the generated CSV.

## Continuous integration

The GitHub Actions workflow installs Temurin 17, caches Maven dependencies, and runs `mvn verify`. Maven's compiler lint treats Java warnings as errors, then Checkstyle runs at the `verify` phase to reject tab characters and missing final newlines. A failure in compilation, tests, lint, packaging, or coverage-report generation fails the workflow. Test results and the JaCoCo HTML report are retained as workflow artifacts even after a failure.
