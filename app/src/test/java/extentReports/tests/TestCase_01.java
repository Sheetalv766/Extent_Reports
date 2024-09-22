package extentReports.tests;

import extentReports.pages.Login;
import extentReports.pages.Register;
import static org.testng.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

// Extent report imports
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestCase_01 {

    static RemoteWebDriver driver;
    static String LastGeneratedName__;

    // report.config().setDocumentTitle("QKART SANITY");

    // declare ExtentReport and ExtentTest variables in the class
    static ExtentTest test;
    static ExtentReports report;

    @BeforeSuite(alwaysRun = true)
    public void createDriver() throws MalformedURLException {
        // Code to Launch Browser using Zalenium in Crio workspace
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timestampString = String.valueOf(timestamp.getTime());

        // TODO - 1. CREATE an instance of ExtentReports
        // path of the html

        report = new ExtentReports(
                System.getProperty("user.dir") + "/OurExtentReport" + timestampString + ".html");
        report.loadConfig(new File(System.getProperty("user.dir") + "/config.xml"));

        // TODO - 2. Start a new test
        test = report.startTest("QKartLogin");
    }

    public void registerNewUser() throws InterruptedException {
        SoftAssert sa = new SoftAssert();
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        sa.assertTrue(registration.registerUser("testUser", "abc@123", true),
                "Failed to create a new user ");
        LastGeneratedName__ = registration.lastGeneratedUsername;
        // sa.assertAll();
    }

    @Test(description = "Verify if new user can be created and logged in ")
    public void TestCase01() throws InterruptedException, IOException {
        registerNewUser();
        String lastGeneratedUserName = "***Qkart Site Activity***";
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // TODO - Check for successful navigation to login page and log pass or fail status

        var status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        try {
            assertTrue(status);
            test.log(LogStatus.PASS, "LOGIN PASSED");
        } catch (AssertionError e) {
            // Add a log statement that records a screenshot of test case failure.
            test.log(LogStatus.FAIL, test.addScreenCapture(capture(driver))
                    + "LOGIN FAILED, reason : " + e.getMessage());
        }
    }

    // Declare a method within TestCase_01
    // named capture() with signature as mentioned previously
    public static String capture(WebDriver driver) throws IOException {

        // Component that captures a screenshot
        // of the current WebDriver instance and stores it.
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Component that returns a File object
        // of the screenshot to be stored onto a file variable
        File Dest = new File(System.getProperty("user.dir") + "/QKARTImages/"
                + System.currentTimeMillis() + ".png");

        // Component that creates a destination folder
        // named QKARTImages within the ‘app’ folder and
        // stores the file name as the current system time.
        String errflpath = Dest.getAbsolutePath();
        FileUtils.copyFile(scrFile, Dest); 
        return errflpath;
    }

    @AfterSuite
    public void quitDriver() {
        driver.quit();

        // TODO - End the test
        report.endTest(test);

        // TODO - Write the test to filesystem
        report.flush();
    }
}
