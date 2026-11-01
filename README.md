# class-project-fall-2026

## Before you push

Run these two commands from the project root:

```bash
./gradlew spotlessApply   # auto-format your code
./gradlew check           # compile, lint, test, and generate coverage
```

`./gradlew check` runs exactly what CI runs, so if it passes locally your PR
will pass CI.

### What the checks cover

| Check | Task | What it catches |
| --- | --- | --- |
| Formatting | `spotlessApply` / `spotlessCheck` | indentation, line wrapping, import order, trailing whitespace, missing final newline |
| Error Prone | `compileJava`, `compileTestJava` | bugs such as `equals` without `hashCode`, string comparison with `==`, ignored return values |
| Checkstyle | `checkstyleMain`, `checkstyleTest` | naming, braces, modifier order, empty statements, unused imports |
| Tests | `test` | JUnit failures |
| Coverage | `jacocoTestReport` | HTML report at `build/reports/jacoco/test/html/index.html` |

### Code style notes

- A single-line `if` / `for` / `while` body stays on the same line without
  braces: `if (x > 0) return true;`
- If the whole statement does not fit on one line, wrap the body in braces.
- Let the formatter decide layout; do not hand-align code.
- Reports from a failed `check` are in `build/reports/`.
