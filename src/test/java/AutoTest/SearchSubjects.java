
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

public class SearchSubjects {

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
        }

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[normalize-space()='Thù lao']")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'/Subject')]")
        )).click();

        wait.until(ExpectedConditions.urlContains("/Subject"));

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("(//span[contains(@class,'select2-selection--single')])[1]")
        )).click();

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//li[normalize-space()='999']")
        )).click();

        System.out.println("Da chon hoc ky 999.");
    }

    @Test(priority = 1)
    public void searchValidSubject() throws InterruptedException {

        searchSubject("Lập trình");

        Thread.sleep(3000);

        List<WebElement> rows = driver.findElements(
            By.cssSelector("table tbody tr")
        );

        boolean found = rows.stream().anyMatch(row ->
            row.isDisplayed() &&
            row.getText().toLowerCase().contains("lập trình")
        );

        Assert.assertTrue(found, "Khong tim thay mon hoc.");

        System.out.println("TC_RM_18_01 Passed.");
    }

    @Test(priority = 2)
    public void searchInvalidSubject() throws InterruptedException {

        searchSubject("TEST99999");

        Thread.sleep(3000);

        List<WebElement> rows = driver.findElements(
            By.cssSelector("table tbody tr")
        );

        boolean hasData = rows.stream().anyMatch(row ->
            row.isDisplayed() &&
            row.findElements(By.tagName("td")).size() >= 5
        );

        Assert.assertFalse(hasData, "Van hien thi du lieu mon hoc.");

        System.out.println("TC_RM_18_02 Passed.");
    }

    private void searchSubject(String keyword) {

        WebElement search = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='search' or contains(@placeholder,'tìm kiếm')]")
            )
        );

        search.clear();
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
