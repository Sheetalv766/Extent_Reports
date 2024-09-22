package extentReports.pages;

import java.sql.Timestamp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class Register {
    WebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/register"; 
    public String lastGeneratedUsername = "";

    @FindBy(id="username")
    WebElement username_txt_box;

    @FindBy(id="password")
    WebElement password_txt_box;

    @FindBy(id="confirmPassword")
    WebElement confirm_password_txt_box;

    @FindBy(className = "button")
    WebElement register_now_button;

    public Register(WebDriver driver)
    {
     this.driver = driver;   
    }

    public void navigateToRegisterPage()
    {
        if(!driver.getCurrentUrl().equals(this.url))
        {
            driver.get(this.url);
            PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
        }
    }

    public Boolean registerUser(String Username , String Password, Boolean makeUsernameDynamic ) throws InterruptedException
    {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // Get time stamp for generating a unique username 
        String test_data_username;
        String test_data_password = Password;
        if(makeUsernameDynamic)
            test_data_username = Username+"_"+String.valueOf(timestamp.getTime()); //concatenate the timestamp to string to form unique timestamp
        else
            test_data_username = Username;
        username_txt_box.sendKeys(test_data_username);//Type the generated username in the username field
        password_txt_box.sendKeys(test_data_password); // Entering the Password value
        confirm_password_txt_box.sendKeys(test_data_password); // Entering the Confirm Password Value
        register_now_button.click(); // clicking the register now button
        this.lastGeneratedUsername = test_data_username;
        //Thread.sleep(5000); //SLEEP_STMT_06: Wait for user to get created in the backend
        WebDriverWait wait = new WebDriverWait(driver,30);     
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));
        return this.driver.getCurrentUrl().endsWith("/login");
    }
}
