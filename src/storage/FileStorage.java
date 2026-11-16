package storage;

import model.TestCase;
import model.ManualTestCase;
import model.AutomatedTestCase;
import model.TestStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FileStorage {
    private final File file = new File("testcases.properties");

    public void save(List<TestCase> tests) throws IOException {
        Properties data = new Properties();
        data.setProperty("count", String.valueOf(tests.size()));

        for (int i = 0; i < tests.size(); i++) {
            TestCase test = tests.get(i);
            String prefix = "test." + i + ".";

            data.setProperty(prefix + "id",
                    String.valueOf(test.getId()));
            data.setProperty(prefix + "type",
                    test.getType());
            data.setProperty(prefix + "title",
                    test.getTitle());
            data.setProperty(prefix + "description",
                    test.getDescription());
            data.setProperty(prefix + "preconditions",
                    test.getPreconditions());
            data.setProperty(prefix + "expectedResult",
                    test.getExpectedResult());
            data.setProperty(prefix + "status",
                    test.getStatus().name());
            data.setProperty(prefix + "specificDetails",
                    test.getSpecificDetails());
        }

        File temporaryFile = new File("testcases.properties.tmp");

        try (FileWriter writer = new FileWriter(
                temporaryFile, StandardCharsets.UTF_8)) {
            data.store(writer, "QA Tracker saved test cases");
        }

        Files.move(
                temporaryFile.toPath(),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public List<TestCase> load() throws IOException {
        List<TestCase> tests = new ArrayList<>();

        if (!file.exists()) {
            return tests;
        }

        Properties data = new Properties();

        try (FileReader reader = new FileReader(
                file, StandardCharsets.UTF_8)) {
            data.load(reader);
        } catch (IllegalArgumentException e) {
            throw new IOException("The saved file is malformed.", e);
        }

        try {
            int count = Integer.parseInt(required(data, "count"));

            if (count < 0) {
                throw new IllegalArgumentException(
                        "Test count cannot be negative.");
            }

            Set<Integer> usedIds = new HashSet<>();

            for (int i = 0; i < count; i++) {
                String prefix = "test." + i + ".";

                int id = Integer.parseInt(
                        required(data, prefix + "id"));

                if (id <= 0 || id == Integer.MAX_VALUE
                        || !usedIds.add(id)) {
                    throw new IllegalArgumentException(
                            "Invalid or duplicate test ID.");
                }

                String type = required(data, prefix + "type");
                String title = required(data, prefix + "title");
                String description =
                        required(data, prefix + "description");
                String preconditions =
                        required(data, prefix + "preconditions");
                String expectedResult =
                        required(data, prefix + "expectedResult");
                String details =
                        required(data, prefix + "specificDetails");

                TestCase test;

                if (type.equals("Manual")) {
                    test = new ManualTestCase(
                            id, title, description, preconditions,
                            expectedResult, details);
                } else if (type.equals("Automated")) {
                    test = new AutomatedTestCase(
                            id, title, description, preconditions,
                            expectedResult, details);
                } else {
                    throw new IllegalArgumentException(
                            "Unknown test type: " + type);
                }

                TestStatus status = TestStatus.valueOf(
                        required(data, prefix + "status"));

                test.setStatus(status);
                tests.add(test);
            }
        } catch (IllegalArgumentException e) {
            throw new IOException(
                    "Invalid saved test data: " + e.getMessage(), e);
        }

        return tests;
    }

    private String required(Properties data, String key) {
        String value = data.getProperty(key);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Missing saved field: " + key);
        }

        return value;
    }
}
