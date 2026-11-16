package validation;

import enums.TestStatus;
import enums.TestType;

/** Reusable input rules; models also enforce required fields. */
public final class InputValidator {
    private InputValidator() { }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String requireText(String value, String field) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(field + " is required.");
        }
        return value.trim();
    }

    public static TestType parseType(String value) {
        for (TestType type : TestType.values()) {
            if (value != null && type.toString().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Please enter Manual or Automated.");
    }

    public static int parseId(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID. Please enter a whole number.");
        }
    }

    public static TestStatus parseStatusChoice(String choice) {
        return switch (choice.trim()) {
            case "1" -> TestStatus.PASS;
            case "2" -> TestStatus.FAIL;
            case "3" -> TestStatus.BLOCKED;
            case "4" -> TestStatus.NOT_RUN;
            default -> throw new IllegalArgumentException(
                    "Invalid status choice. Enter a number from 1 to 4.");
        };
    }
}
