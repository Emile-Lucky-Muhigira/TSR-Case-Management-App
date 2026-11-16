import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestCaseManager {
    private final List<TestCase> testCases = new ArrayList<>();
    private int nextId = 1;

    public void addManualTest(
            String title,
            String description,
            String preconditions,
            String expectedResult,
            String testerName) {

        TestCase test = new ManualTestCase(
                nextId, title, description, preconditions,
                expectedResult, testerName);

        testCases.add(test);
        nextId++;
    }

    public void addAutomatedTest(
            String title,
            String description,
            String preconditions,
            String expectedResult,
            String scriptPath) {

        TestCase test = new AutomatedTestCase(
                nextId, title, description, preconditions,
                expectedResult, scriptPath);

        testCases.add(test);
        nextId++;
    }

    public List<TestCase> getAllTests() {
        return new ArrayList<>(testCases);
    }

    public TestCase findById(int id) {
        for (TestCase test : testCases) {
            if (test.getId() == id) {
                return test;
            }
        }
        return null;
    }

    public boolean updateStatus(int id, TestStatus status) {
        TestCase test = findById(id);

        if (test == null) {
            return false;
        }

        test.setStatus(status);
        return true;
    }

    public List<TestCase> search(String keyword) {
        List<TestCase> matches = new ArrayList<>();
        String searchText = keyword.toLowerCase(Locale.ROOT);

        for (TestCase test : testCases) {
            String title = test.getTitle().toLowerCase(Locale.ROOT);
            String description =
                    test.getDescription().toLowerCase(Locale.ROOT);

            if (title.contains(searchText)
                    || description.contains(searchText)) {
                matches.add(test);
            }
        }

        return matches;
    }

    public void displayTests(List<TestCase> tests) {
        if (tests.isEmpty()) {
            System.out.println("No test cases found.");
            return;
        }

        System.out.printf(
                "%-6s | %-30s | %-10s | %-8s | %s%n",
                "ID", "Title", "Type", "Status", "Tester / Script path");

        System.out.println(
                "------------------------------------------------------------");

        for (TestCase test : tests) {
            System.out.printf(
                    "%-6d | %-30s | %-10s | %-8s | %s%n",
                    test.getId(),
                    test.getTitle(),
                    test.getType(),
                    test.getStatus(),
                    test.getSpecificDetails());
        }
    }

    public void generateReport() {
        int manual = 0;
        int automated = 0;
        int pass = 0;
        int fail = 0;
        int blocked = 0;
        int notRun = 0;

        for (TestCase test : testCases) {
            if (test instanceof ManualTestCase) {
                manual++;
            } else if (test instanceof AutomatedTestCase) {
                automated++;
            }

            switch (test.getStatus()) {
                case PASS -> pass++;
                case FAIL -> fail++;
                case BLOCKED -> blocked++;
                case NOT_RUN -> notRun++;
            }
        }

        int total = testCases.size();
        double passRate = total == 0 ? 0.0 : pass * 100.0 / total;

        System.out.println("================================");
        System.out.println("      TEST EXECUTION REPORT");
        System.out.println("================================");
        System.out.println("Total Test Cases : " + total);
        System.out.println("  - Manual      : " + manual);
        System.out.println("  - Automated   : " + automated);
        System.out.println("--------------------------------");
        System.out.println("PASS            : " + pass);
        System.out.println("FAIL            : " + fail);
        System.out.println("BLOCKED         : " + blocked);
        System.out.println("NOT RUN         : " + notRun);
        System.out.printf(
                Locale.ROOT, "Pass Rate       : %.1f%%%n", passRate);
        System.out.println("================================");
    }

    public void restoreTests(List<TestCase> loadedTests) {
        testCases.clear();
        testCases.addAll(loadedTests);

        nextId = 1;

        for (TestCase test : testCases) {
            if (test.getId() >= nextId) {
                nextId = test.getId() + 1;
            }
        }
    }
}