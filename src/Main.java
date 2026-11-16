import model.TestCase;
import enums.TestStatus;
import service.TestCaseService;
import storage.FileStorage;
import service.ReportGenerator;
import enums.TestType;
import validation.InputValidator;
import java.util.NoSuchElementException;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TestCaseService manager = new TestCaseService();
    private static final FileStorage storage = new FileStorage();

    public static void main(String[] args) {
        try {
            manager.restoreTests(storage.load());
        } catch (IOException e) {
            System.out.println("Could not load saved tests: "
                    + e.getMessage());
            System.out.println(
                    "Please check the saved file before restarting.");
            scanner.close();
            return;
        }

        boolean running = true;

        try {
            while (running) {
                displayMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> addTestCase();
                    case "2" -> manager.displayTests(manager.getAllTests());
                    case "3" -> updateTestStatus();
                    case "4" -> searchTestCases();
                    case "5" -> new ReportGenerator().generateReport(manager.getAllTests());
                    case "6" -> {
                        if (saveTests()) {
                            running = false;
                        }
                    }
                    default -> System.out.println(
                            "Invalid choice. Enter a number from 1 to 6.");
                }

                System.out.println();
            }

        } catch (NoSuchElementException e) {
            System.out.println("Input closed. Unsaved changes were not saved. Goodbye!");
        } finally {
            scanner.close();
        }
    }

    private static void displayMenu() {
        System.out.println("==============================");
        System.out.println("        QA TRACKER v1.0");
        System.out.println("==============================");
        System.out.println("1. Add Test Case");
        System.out.println("2. View All Test Cases");
        System.out.println("3. Update Test Status");
        System.out.println("4. Search Test Cases");
        System.out.println("5. Generate Report");
        System.out.println("6. Save & Exit");
        System.out.println("==============================");
        System.out.print("Enter your choice: ");
    }

    private static void addTestCase() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        if (InputValidator.isBlank(title)) {
            System.out.println("Title is required. Test case not created.");
            return;
        }

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Preconditions: ");
        String preconditions = scanner.nextLine();

        System.out.print("Expected result: ");
        String expectedResult = scanner.nextLine();

        if (InputValidator.isBlank(expectedResult)) {
            System.out.println(
                    "Expected result is required. Test case not created.");
            return;
        }

        TestType type;
        while (true) {
            System.out.print("Type (Manual or Automated): ");
            try {
                type = InputValidator.parseType(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            if (type == TestType.MANUAL) {
                System.out.print("Tester name: ");
                String testerName = scanner.nextLine();

                manager.addManualTest(
                        title, description, preconditions,
                        expectedResult, testerName);
            } else {
                System.out.print("Script path: ");
                String scriptPath = scanner.nextLine();

                manager.addAutomatedTest(
                        title, description, preconditions,
                        expectedResult, scriptPath);
            }

            System.out.println(
                    "Test case added successfully. Status: NOT_RUN.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateTestStatus() {
        System.out.print("Enter test case ID: ");
        int id;

        try {
            id = InputValidator.parseId(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ID. Please enter a whole number.");
            return;
        }

        TestCase test = manager.findById(id);

        if (test == null) {
            System.out.println("No test case found with ID " + id + ".");
            return;
        }

        System.out.println("Current status: " + test.getStatus());

        while (true) {
            System.out.println("1. PASS");
            System.out.println("2. FAIL");
            System.out.println("3. BLOCKED");
            System.out.println("4. NOT_RUN");
            System.out.print("Choose new status: ");

            String choice = scanner.nextLine().trim();

            TestStatus status;
            try {
                status = InputValidator.parseStatusChoice(choice);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            manager.updateStatus(id, status);
            System.out.println("Status updated to " + status + ".");
            return;
        }
    }

    private static void searchTestCases() {
        System.out.print("Enter keyword (leave empty to show all): ");
        String keyword = scanner.nextLine();

        manager.displayTests(manager.search(keyword));
    }

    private static boolean saveTests() {
        try {
            storage.save(manager.getAllTests());
            System.out.println("Test cases saved. Goodbye!");
            return true;
        } catch (IOException e) {
            System.out.println("Could not save tests: " + e.getMessage());
            System.out.println(
                    "Your tests remain in memory. Please retry Save & Exit.");
            return false;
        }
    }
}
