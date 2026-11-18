
package AutoTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class DuplicateMajorNamesTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setupAndNavigate() {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        // Tu dong dang nhap
        if (driver.getCurrentUrl().contains("/Account/Login")) {

            WebElement btnDangNhap = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space()='Đăng nhập'] | //button[contains(., 'Đăng nhập')] | //a[contains(., 'Đăng nhập')]")
                )
            );

            clickElement(btnDangNhap);

            wait.until(d -> !d.getCurrentUrl().contains("/Account/Login"));

            System.out.println("Dang nhap thanh cong.");
        }

        // Mo menu Thu lao
        WebElement thuLaoMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='sidebarnav']//li/a[contains(@href, '#') and .//i[contains(@class, 'money') or contains(@class, 'dollar') or contains(@class, 'cash')]] | //span[contains(., 'Th') and contains(., 'lao')]")
            )
        );

        clickElement(thuLaoMenu);

        System.out.println("Da mo menu Thu lao.");

        // Mo menu Mon hoc
        WebElement subjectMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/Subject')]")
            )
        );

        clickElement(subjectMenu);

        wait.until(ExpectedConditions.urlContains("/Subject"));

        System.out.println("Da mo menu Mon hoc.");

        // Chon hoc ky 999
        WebElement semester = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("(//span[contains(@class,'select2-selection--single')])[1]")
            )
        );

        clickElement(semester);

        WebElement semester999 = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'select2-results__option') and normalize-space()='999']")
            )
        );

        clickElement(semester999);

        wait.until(d -> d.findElement(
            By.xpath("(//span[contains(@class,'select2-selection--single')])[1]")
        ).getText().trim().equals("999"));

        System.out.println("Da chon hoc ky 999.");
    }

    @Test
    public void verifyDuplicateMajorNames() throws InterruptedException {

        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("select2-major-container")
            )
        );

        clickElement(dropdown);

        WebElement firstMajor = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li[id^='select2-major-result-'][id$='-00001']")
            )
        );

        WebElement secondMajor = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li[id^='select2-major-result-'][id$='-00100IT1010']")
            )
        );

        String firstName = firstMajor.getText().trim();
        String secondName = secondMajor.getText().trim();

        System.out.println("Nganh 1 - 00001: " + firstName);
        System.out.println("Nganh 2 - 00100IT1010: " + secondName);

        Thread.sleep(5000);

        Assert.assertNotEquals(
            firstName,
            secondName,
            "BUG-002: Hai nganh khac nhau co cung ten hien thi."
        );

        System.out.println("TC_RM_16_04 Passed.");
    }

    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(15000);
            driver.quit();
        }
    }
}
