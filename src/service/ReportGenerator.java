package service;

import model.TestCase;
import enums.TestType;
import java.util.List;
import java.util.Locale;

public class ReportGenerator {
    public void generateReport(List<TestCase> testCases) {
        int manual = 0;
        int automated = 0;
        int pass = 0;
        int fail = 0;
        int blocked = 0;
        int notRun = 0;

        for (TestCase test : testCases) {
            if (test.getType() == TestType.MANUAL) {
                manual++;
            } else if (test.getType() == TestType.AUTOMATED) {
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

}
