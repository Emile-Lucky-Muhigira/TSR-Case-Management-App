# TSR Case Management App

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
javac -d out src/Main.java src/model/*.java src/service/*.java src/storage/*.java
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
5. **Generate Report**: display counts by type and status, plus pass rate. Pass rate is PASS / total cases × 100, including blocked and not-run cases in the denominator. An empty collection reports 0.0%.
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
└── src/
    ├── Main.java
    ├── model/
    │   ├── TestCase.java
    │   ├── ManualTestCase.java
    │   ├── AutomatedTestCase.java
    │   └── TestStatus.java
    ├── service/
    │   └── TestCaseManager.java
    └── storage/
        └── FileStorage.java
```

- `Main.java`: console menu, input, and startup/shutdown flow.
- `model`: test case fields, validation, manual/automated types, and status enum. Each class or enum has its own Java file.
- `service`: collection management, IDs, search, status updates, and reporting.
- `storage`: saving and loading using Java Properties and file I/O.

## Troubleshooting

- **java/javac not recognized**: install a JDK and add its `bin` directory to PATH, then reopen the terminal.
- **Could not find or load main class Main**: run the compile command successfully, then run `java -cp out Main` from the project root.
- **Saved tests appear missing**: check the working directory; the app reads its data file relative to that directory.


