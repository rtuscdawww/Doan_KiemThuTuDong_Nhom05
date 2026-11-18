
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
import java.util.List;

public class ViewListofSubjects {

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

    @Test(priority = 1)
    public void viewAllSubjects() throws InterruptedException {

        openMajorDropdown();

        WebElement all = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'select2-results__option') and normalize-space()='Tất cả']")
            )
        );

        clickElement(all);

        Thread.sleep(3000);

        verifySubjectTable();

        System.out.println("TC_RM_16_01 Passed: View all subjects.");

        Thread.sleep(3000);
    }

    @Test(priority = 2)
    public void viewFirstITMajor() throws InterruptedException {

        openMajorDropdown();

        WebElement major = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("li[id^='select2-major-result-'][id$='-00001']")
            )
        );

        clickElement(major);

        System.out.println("Da chon nganh CNTT - 00001.");

        Thread.sleep(3000);

        verifySubjectTable();

        System.out.println("TC_RM_16_02 Passed: View first IT major.");

        Thread.sleep(3000);
    }


    private void openMajorDropdown() {

        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.id("select2-major-container")
            )
        );

        clickElement(dropdown);
    }

    private void verifySubjectTable() {

        List<WebElement> rows = wait.until(d -> {

            List<WebElement> data = d.findElements(
                By.cssSelector("table tbody tr")
            );

            boolean hasData = data.stream().anyMatch(
                row -> row.isDisplayed()
                    && row.findElements(By.tagName("td")).size() >= 5
            );

            return hasData ? data : null;
        });

        Assert.assertFalse(
            rows.isEmpty(),
            "Bang danh sach mon hoc khong co du lieu."
        );

        System.out.println("So dong mon hoc: " + rows.size());
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
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
