package enums;

public enum TestType {
    MANUAL("Manual"), AUTOMATED("Automated");

    private final String label;

    TestType(String label) { this.label = label; }

    @Override
    public String toString() { return label; }
}
