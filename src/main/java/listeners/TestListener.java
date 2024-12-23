package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;
import reports.TestReporter;

import java.io.File;

import static com.codeborne.selenide.Selenide.*;
import static constants.Constants.*;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.testng.CommandLineArgs.THREAD_COUNT;
import static reports.TestReporter.log4j;
import static reports.TestReporter.log4jConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;


public class TestListener implements ITestListener, IClassListener, ISuiteListener {

    public static ExtentTest logMethod;
    public static ExtentTest logStep = null;
    public static ExtentTest logSuite;
    public String testCaseName;
    public String testNameWithStatus;
    public static ArrayList<String> testCaseList = new ArrayList<String>();
    private static ExtentReports report;
    private static ExtentTest logClass;

    @Override
    public void onStart(ISuite suite) {
        log4jConfiguration();
        log4j.info("BeforeSuite - starts");

        try {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(REPORT_FILE_PATH);
            htmlReporter.loadXMLConfig(new File(PROJECT_PATH + "/src/test/resources/suites/config.xml"));
            report = new ExtentReports();
            report.attachReporter(htmlReporter);
            logSuite = report.createTest("Initial Setup");
        } catch (Exception e) {
            log4j.error("ERROR while initializing Extent report: " + e.getMessage());
        }

        File folder = new File(REPORT_LOCATION);
        folder.mkdirs();

        if (logSuite != null) {
            logSuite.info("Report link: " + REPORT_LOCATION);
            logSuite.info("Browser: " + BROWSER);
            logSuite.info("Thread count: " + THREAD_COUNT);
        }
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        log4j.info("BeforeClass - Starts for " + testClass.getName());
        testCaseName = testClass.getRealClass().getSimpleName();
        if (report != null) {
            logClass = report.createTest(testClass.getName());
        } else {
            log4j.error("Report is null. Make sure it is initialized in onStart.");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        log4j.info("BeforeMethod - Starts");
        logStep = null;
        open(URL_TIKI);

        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            Hashtable<String, String> dataTest = (Hashtable<String, String>) parameters[0];
            testNameWithStatus = testCaseName + ": " + dataTest.get("Data");
        } else {
            testNameWithStatus = testCaseName;
        }

        //Initiate logClass
        if (logClass == null) {
            logClass = TestReporter.createTestForExtentReport(report, testNameWithStatus);
        }

        //Initiate logMethod
        if (logClass != null) {
            logMethod = TestReporter.createNodeForExtentReport(logClass, testCaseName);
        } else {
            log4j.error("logClass is null, cannot create node.");
        }

        log4j.info("BeforeMethod - Ends");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log4j.info("Test Passed");
        logMethod.pass("Test Passed");
        testCaseList.add(testNameWithStatus + ": " + logMethod.getStatus());
        clearBrowserCookies();
        clearBrowserLocalStorage();
        logMethod = null;
        log4j.info("AfterMethod - Ends");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logMethod.fail("Test Failed");
        TestReporter.captureScreenshot(logMethod, result, result.getName(), result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logMethod.skip("Test Skipped");
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        if (report != null) {
            report.flush();
        }
        logClass = null;
    }

    @Override
    public void onFinish(ITestContext context) {
        log4j.info("AfterSuite - Finished");
        TestReporter.getTestCaseExecutionCount(testCaseList);
    }
}

