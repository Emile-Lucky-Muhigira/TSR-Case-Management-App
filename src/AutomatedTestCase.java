public class AutomatedTestCase extends TestCase {
    private final String scriptPath;

    public AutomatedTestCase(
            int id,
            String title,
            String description,
            String preconditions,
            String expectedResult,
            String scriptPath) {

        super(id, title, description, preconditions, expectedResult);
        this.scriptPath = scriptPath == null ? "" : scriptPath;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    @Override
    public String getType() {
        return "Automated";
    }

    @Override
    public String getSpecificDetails() {
        return scriptPath;
    }
}