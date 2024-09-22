package extentReports.pages;

import java.time.Duration;
import java.util.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class Login {
    WebDriver driver;

    @FindBy(id = "username")
    WebElement username_txt_box;

    @FindBy(id = "password")
    WebElement password_txt_box;

    @FindBy(className = "button")
    WebElement login_button;

    @FindBy(className = "username-text")
    WebElement username_label;

    String url = "https://crio-qkart-frontend-qa.vercel.app/login";

    public Login(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    public void navigateToLoginPage() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogin(String Username, String Password) throws InterruptedException {
        username_txt_box.sendKeys(Username); // Enter the username in the login page
        password_txt_box.sendKeys(Password);
        login_button.click(); // click the login Button
        Thread.sleep(5000);
        synchronized (driver) {
            driver.wait(2000);
        }
        return this.VerifyUserLoggedIn(Username);
    }

    public Boolean VerifyUserLoggedIn(String Username) {
        try {
            return username_label.getText().equals(Username);
        } catch (Exception e) {
            // User May not have logged in
            return false;
        }
        // top right of the page
    }


}
