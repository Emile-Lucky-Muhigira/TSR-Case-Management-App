package model;

import enums.TestType;

public class ManualTestCase extends TestCase {
    private final String testerName;

    public ManualTestCase(
            int id,
            String title,
            String description,
            String preconditions,
            String expectedResult,
            String testerName) {

        super(id, title, description, preconditions, expectedResult);
        this.testerName = testerName == null ? "" : testerName;
    }

    public String getTesterName() {
        return testerName;
    }

    @Override
    public TestType getType() {
        return TestType.MANUAL;
    }

    @Override
    public String getSpecificDetails() {
        return testerName;
    }
}
