
package AutoTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class ViewLecturersByRank {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        if (driver.getCurrentUrl().contains("/Account/Login")) {

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space()='Đăng nhập']")
            )).click();

            wait.until(d -> !d.getCurrentUrl().contains("/Account/Login"));

            System.out.println("Dang nhap thanh cong.");
        }
    }

    @Test
    public void viewLecturersByRank() throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[normalize-space()='Thù lao']")
        )).click();

        System.out.println("Da mo menu Thu lao.");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'/LecturerRank')]")
        )).click();

        wait.until(ExpectedConditions.urlContains("/LecturerRank"));

        System.out.println("Da mo menu Cap bac GV.");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".select2-selection--single")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//li[normalize-space()='999']")
        )).click();

        System.out.println("Da chon hoc ky 999.");

        Thread.sleep(3000);

        List<WebElement> rows = wait.until(d -> {

            List<WebElement> data = d.findElements(
                By.cssSelector("table tbody tr")
            );

            boolean hasData = data.stream().anyMatch(row ->
                row.isDisplayed() &&
                row.findElements(By.tagName("td")).size() >= 4
            );

            return hasData ? data : null;
        });

        Assert.assertFalse(
            rows.isEmpty(),
            "Khong hien thi danh sach giang vien."
        );

        System.out.println("So dong giang vien: " + rows.size());
        System.out.println("TC_RM_19 Passed.");
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
