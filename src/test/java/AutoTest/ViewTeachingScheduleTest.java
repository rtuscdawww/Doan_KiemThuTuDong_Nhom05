
package AutoTest;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ViewTeachingScheduleTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private final String URL =
        "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        js = (JavascriptExecutor) driver;

        driver.get(URL);

        if (driver.getCurrentUrl().contains("/Account/Login")) {
            WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit']")
                )
            );

            js.executeScript("arguments[0].click();", loginButton);
        }

        WebElement statsMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//span[normalize-space()='Thống kê']"
                )
            )
        );

        js.executeScript("arguments[0].click();", statsMenu);

        WebElement scheduleMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//a[contains(@href,'/Statistics/Timetable')]"
                )
            )
        );

        js.executeScript("arguments[0].click();", scheduleMenu);

        wait.until(
            ExpectedConditions.urlContains("/Statistics/Timetable")
        );

        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("span.select2-selection--multiple")
            )
        );
    }

    private void selectSemesterAndWeek() {

        selectDropdownValue("Học kỳ", "998");
        selectDropdownValue("Tuần", "48");
    }

    private void selectDropdownValue(String label, String value) {

        By dropdown = By.xpath(
            "//*[normalize-space()='" + label + ":']" +
            "/following::span[contains(@class,'select2-selection')][1]"
        );

        WebElement box = wait.until(
            ExpectedConditions.elementToBeClickable(dropdown)
        );

        js.executeScript("arguments[0].click();", box);

        WebElement option = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//li[contains(@class,'select2-results__option')" +
                    " and contains(normalize-space(.),'" + value + "')]"
                )
            )
        );

        option.click();
    }

    private void selectLecturers(String... lecturers) {

        WebElement lecturerDropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("span.select2-selection--multiple")
            )
        );

        lecturerDropdown.click();

        By clearAll = By.xpath(
            "//*[normalize-space()='Bỏ chọn tất cả']"
        );

        List<WebElement> clearButtons =
            driver.findElements(clearAll);

        if (!clearButtons.isEmpty()) {
            WebElement clearButton = wait.until(
                ExpectedConditions.elementToBeClickable(clearAll)
            );
            clearButton.click();
        }

        for (String lecturer : lecturers) {

            lecturerDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("span.select2-selection--multiple")
                )
            );

            lecturerDropdown.click();

            WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input.select2-search__field")
                )
            );

            search.clear();
            search.sendKeys(lecturer);

            WebElement result = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//li[contains(@class,'select2-results__option')" +
                        " and contains(normalize-space(.),'" +
                        lecturer + "')]"
                    )
                )
            );

            result.click();
        }
    }

    @Test
    public void testViewOneLecturerSchedule() {

        selectSemesterAndWeek();

        selectLecturers("Đặng Thị Trúc Đào");

        WebElement table = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.tagName("table")
            )
        );

        Assert.assertTrue(
            table.isDisplayed(),
            "Không hiển thị bảng thời khóa biểu!"
        );
    }

    @Test
    public void testViewTwoLecturersSchedule() {

        selectSemesterAndWeek();

        selectLecturers(
            "Đặng Thị Trúc Đào",
            "Lê Nguyên Khải"
        );

        WebElement table = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.tagName("table")
            )
        );

        Assert.assertTrue(
            table.isDisplayed(),
            "Không hiển thị bảng thời khóa biểu!"
        );
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            driver.quit();
        }
    }
}
