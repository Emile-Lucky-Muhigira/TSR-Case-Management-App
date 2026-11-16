public abstract class TestCase {
    private final int id;
    private final String title;
    private final String description;
    private final String preconditions;
    private final String expectedResult;
    private TestStatus status;

    public TestCase(
            int id,
            String title,
            String description,
            String preconditions,
            String expectedResult) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }

        if (expectedResult == null || expectedResult.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Expected result is required.");
        }

        this.id = id;
        this.title = title.trim();
        this.description = description == null ? "" : description;
        this.preconditions = preconditions == null ? "" : preconditions;
        this.expectedResult = expectedResult.trim();
        this.status = TestStatus.NOT_RUN;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPreconditions() {
        return preconditions;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        this.status = status;
    }

    public abstract String getType();

    public abstract String getSpecificDetails();
}