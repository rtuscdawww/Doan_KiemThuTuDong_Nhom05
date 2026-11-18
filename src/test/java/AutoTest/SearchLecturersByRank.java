
package AutoTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class SearchLecturersByRank {

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

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[normalize-space()='Thù lao']")
        )).click();

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
    }

    @Test(priority = 1)
    public void searchValidLecturer() throws InterruptedException {

        searchLecturer("Đào");

        Thread.sleep(3000);

        boolean found = wait.until(d ->
            d.findElements(By.cssSelector("table tbody tr"))
                .stream().anyMatch(row ->
                    row.isDisplayed() &&
                    row.getText().contains("Đào")
                )
        );

        Assert.assertTrue(
            found,
            "Khong tim thay giang vien Đào."
        );

        System.out.println("TC_RM_21_01 Passed.");

        Thread.sleep(3000);
    }

    @Test(priority = 2)
    public void searchInvalidLecturer() throws InterruptedException {

        searchLecturer("TESTGV99999");

        Thread.sleep(3000);

        boolean hasData = driver.findElements(
            By.cssSelector("table tbody tr")
        ).stream().anyMatch(row ->
            row.isDisplayed() &&
            row.findElements(By.tagName("td")).size() >= 4
        );

        Assert.assertFalse(
            hasData,
            "Van hien thi giang vien khong phu hop."
        );

        System.out.println("TC_RM_21_02 Passed.");

        Thread.sleep(3000);
    }

    private void searchLecturer(String keyword) {

        WebElement search = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='search' or contains(@placeholder,'tìm kiếm')]")
            )
        );

        search.sendKeys(Keys.CONTROL + "a");
        search.sendKeys(Keys.BACK_SPACE);
        search.sendKeys(keyword);

        System.out.println("Tu khoa tim kiem: " + keyword);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
