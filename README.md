# QA Tracker - Test Case Management App

**Author:** [Emile Lucky Muhigira](https://github.com/Emile-Lucky-Muhigira)

Java Programming Module 2 capstone project.

A Java console application (QA Tracker v1.0) for recording manual and automated test cases, updating their results, searching tests, and displaying a test execution report.

## Requirements

- JDK 17 or later, with `java` and `javac` available on your PATH.
- Git to clone the repository, or download and extract the ZIP from GitHub.
- Optional: IntelliJ IDEA.

No external libraries, Maven, Gradle, or database are required.

## Run from the terminal

Clone the repository and enter its directory:

```sh
git clone https://github.com/Emile-Lucky-Muhigira/TSR-Case-Management-App.git
cd TSR-Case-Management-App
```

Check Java, compile, and run from the project root:

```sh
java -version
javac -version
javac -d out src/Main.java src/model/*.java src/enums/*.java src/validation/*.java src/storage/*.java src/service/*.java
java -cp out Main
```

These commands work in Windows PowerShell and conventional macOS/Linux shells. Recompile after changing source files.

## Run in IntelliJ IDEA

1. Choose **File > Open** and open the cloned project folder.
2. Open **File > Project Structure > Project** and select JDK 17 or later as the Project SDK.
3. If `src` is not recognized as a source folder, right-click it and choose **Mark Directory as > Sources Root**.
4. Open `src/Main.java` and click the green Run icon beside `main`.
5. Enter menu choices in the Run console. Use the project root as the run configuration's working directory so it shares the terminal's saved data.

## Using the app

Enter a menu number and press Enter:

1. **Add Test Case**: enter title, description, preconditions, expected result, and type (`Manual` or `Automated`). Manual cases store a tester name; automated cases store a script path. Title and expected result are required. IDs are assigned automatically; initial status is `NOT_RUN`.
2. **View All Test Cases**: display ID, title, type, status, and tester name or script path.
3. **Update Test Status**: enter an existing ID, then choose `1` PASS, `2` FAIL, `3` BLOCKED, or `4` NOT_RUN.
4. **Search Test Cases**: search titles and descriptions without case sensitivity. An empty search shows all cases.
5. **Generate Report**: display counts by type and status, plus pass rate. Pass rate is PASS / total cases Ã— 100, including blocked and not-run cases in the denominator. An empty collection reports 0.0%.
6. **Save & Exit**: save the current test cases and close the app.

Automated cases store script paths only; the application does not execute scripts. Reports are printed in the console.

## Saving and loading

Choose **6. Save & Exit** to persist changes. Closing the console or stopping the process before saving loses unsaved changes.

Data is stored in UTF-8 in `testcases.properties` in the current working directory and loaded at startup. A missing file starts an empty collection. IDs, fields, types, and statuses are restored. Saving uses `testcases.properties.tmp` before replacing the saved file.

If the saved file is invalid or unreadable, startup reports the error and stops. Back up and inspect the file before changing it. A save error leaves the app running so you can retry.

Runtime data, compiled output, and local IDE configuration are excluded from Git.

## Source structure

```text
TSR-Case-Management-App/
├── README.md
├── .gitignore
├── tests/
│   └── test_assignment.py
└── src/
    ├── model/
    │   ├── TestCase.java
    │   ├── ManualTestCase.java
    │   └── AutomatedTestCase.java
    ├── enums/
    │   ├── TestStatus.java
    │   └── TestType.java
    ├── validation/
    │   └── InputValidator.java
    ├── storage/
    │   └── FileStorage.java
    ├── service/
    │   ├── TestCaseService.java
    │   └── ReportGenerator.java
    └── Main.java
```

The local folder can be named `QA_Tracker`; its name does not affect Java packages or execution. The GitHub repository retains its existing name.

- **model**: abstract base class and separate manual/automated subclasses, with private fields and required-field validation.
- **enums**: the allowed statuses and test types.
- **validation**: reusable required-text, type, ID, and status-choice checks.
- **storage**: UTF-8 file reading/writing and saved-record validation. Previous-version files using `Manual` and `Automated` type labels remain compatible.
- **service**: collection operations in `TestCaseService`; report calculations and display in `ReportGenerator`.
- **Main**: menu prompts, application startup, and coordinating operations.

## Assignment verification

Reviewed against `module_2_capstone.pdf` (Java Programming capstone). The implementation covers:

- **Add**: all specified fields, unique automatic IDs, default NOT_RUN, distinct manual and automated classes, and type-specific fields.
- **View**: formatted ID/title/type/status/type-specific details, with a message when empty.
- **Update**: current status display, all four allowed statuses, and friendly errors for invalid or missing IDs.
- **Search**: case-insensitive title/description matches, empty keyword returns all, and a message for no matches.
- **Report**: total, type counts, all status counts, one-decimal pass rate, and safe zero-case reporting.
- **Persistence**: Save & Exit, farewell message, automatic startup load, and restoration of every field including type and status.
- **Validation**: all eight rules listed in the questionnaire; invalid menu choices, nonnumeric IDs, corrupt saved files, save failures, and closed input are also handled.
- **Java concepts**: abstraction, inheritance, polymorphism, encapsulation, collections, enums, exception handling, and file I/O.

The assignment also requires a **live terminal demonstration and code review**. The code and checks support these deliverables; the student/group still needs to perform the demonstration and explain the implementation.

### Run the verification checks (optional)

The app itself only needs Java. The development checks additionally require Python 3, using its standard library only:

```sh
python -m unittest discover -s tests -v
```

The suite compiles the Java sources into a temporary directory and runs 10 black-box checks covering the six features, validation, saved-field round trips, unique IDs after restart, compatibility with previous saved files, and file/input error handling. Test data is isolated from your own saved cases.

### Suggested live demonstration

1. Start without a saved file; show the empty view and zero report.
2. Attempt an empty title and empty expected result; show rejection.
3. Add one Manual case and one Automated case; show both in the list.
4. Try an invalid type, nonexistent ID, and invalid status choice.
5. Change one case to PASS; show a 50.0% pass rate for two total cases.
6. Search by title, description, mixed case, empty keyword, and a nonmatching keyword.
7. Choose Save & Exit, restart, and show the restored cases and statuses.

## Troubleshooting

- **java/javac not recognized**: install a JDK and add its `bin` directory to PATH, then reopen the terminal.
- **Could not find or load main class Main**: run the compile command successfully, then run `java -cp out Main` from the project root.
- **Saved tests appear missing**: check the working directory; the app reads its data file relative to that directory.


