package constants;

import helpers.DateTimeHelper;
import utils.PropertiesFile;

public class Constants {
    public static String PROJECT_PATH = System.getProperty("user.dir");
    public static final String URL_TIKI = PropertiesFile.getPropValue("URL_TIKI");
    public static String OUTPUT_PATH = PROJECT_PATH + "/resources/output/";
    public static String TIME_STAMP_STRING = DateTimeHelper.generateTimeStampString("yyyy-MM-dd-HH-mm-ss");
    public static String REPORT_LOCATION = OUTPUT_PATH + "report-" + TIME_STAMP_STRING + "/";
    public static String REPORT_FILE_PATH = REPORT_LOCATION + "report-" + TIME_STAMP_STRING + ".html";
    public static int TOTAL_TESTCASES = 0;
    public static int TOTAL_PASSED = 0;
    public static int TOTAL_FAILED = 0;
    public static int TOTAL_SKIPPED = 0;
    public static final String RETRY_TEST_FAIL = PropertiesFile.getPropValue("RETRY_TEST_FAIL");
    public static final String REPORT_TITLE = PropertiesFile.getPropValue("REPORT_TITLE");
    public static final String EXTENT_REPORT_NAME = PropertiesFile.getPropValue("EXTENT_REPORT_NAME");
    public static final String EXTENT_REPORT_FILE_NAME = EXTENT_REPORT_NAME + ".html";

}
