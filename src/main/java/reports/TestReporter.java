package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.codeborne.selenide.WebDriverRunner;
import helpers.DateTimeHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import constants.Constants;

import java.io.File;
import java.util.ArrayList;

public class TestReporter {
    public static Log log4j;

    public static ExtentTest logStepInfo(ExtentTest logTest, String description, Object... args) {
        return logTest.createNode(description);
    }

    public static ExtentTest createNodeForExtentReport(ExtentTest parentTest, String description) {
        return parentTest.createNode(description);
    }

    public static ExtentTest createTestForExtentReport(ExtentReports report, String description) {
        return report.createTest(description);
    }

    public static void captureScreenshot(ExtentTest logTest, ITestResult result, String detail, String screenShotName) {
        try {
            screenShotName = screenShotName + DateTimeHelper.generateTimeStampString("yyyy-MM-dd-HH-mm-ss") + ".png";

            TakesScreenshot ts = (TakesScreenshot) WebDriverRunner.getWebDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = Constants.REPORT_LOCATION + screenShotName; //Storing the image in reportLocation

            File destination = new File(dest);
            FileUtils.copyFile(source, destination);

            //Add current url to report
            if (WebDriverRunner.getWebDriver().getCurrentUrl() != null)
                logTest.info("Page url: " + WebDriverRunner.getWebDriver().getCurrentUrl());

            //Add screenshot to report
            String screenShotLink = "<a href=\"" + screenShotName + "\"" + screenShotName + "</a>";
            if (result.getStatus() == ITestResult.FAILURE) {
                logTest.fail(detail + screenShotLink).addScreenCaptureFromPath(screenShotName);
            } else logTest.pass(detail + screenShotLink).addScreenCaptureFromPath(screenShotName);
        } catch (Exception e) {
            log4j.info("An error occurred when capturing screen shot: ", e);
        }
    }

    public static void getTestCaseExecutionCount(ArrayList<String> testCaseList) {
        for (int i = 0; i < testCaseList.size(); i++) {
            if (testCaseList.get(i).contains(": pass")) {
                Constants.TOTAL_PASSED++;
            } else if (testCaseList.get(i).contains(": skip")) {
                Constants.TOTAL_SKIPPED++;
            } else Constants.TOTAL_FAILED++;
        }
        Constants.TOTAL_TESTCASES = testCaseList.size();
    }

    public static void log4jConfiguration() {
        try {
            log4j = LogFactory.getLog(new Object().getClass().getName());
        } catch (Exception e) {
            log4j.error("log4jConfiguaration method - ERROR: ", e);
        }
    }
}
