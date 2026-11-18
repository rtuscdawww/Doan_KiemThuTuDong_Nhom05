
package AutoTest;

import java.time.Duration;

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

public class LogoutTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testLogoutFunction() {

        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        //Xuất hiện trang đăng nhập, nhấn Đăng nhập
        if (driver.getCurrentUrl().contains("/Account/Login")) {

            WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit']")
                )
            );

            js.executeScript("arguments[0].click();", loginButton);
        }

        // Chờ trang chủ hiển thị tài khoản BCN Khoa
        WebElement accountMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//*[contains(normalize-space(.),'dao.2274802010148@vanlanguni.vn')"
                    + " and (self::a or self::button or self::span or self::div)]"
                    + "[not(.//*[contains(normalize-space(.),'dao.2274802010148@vanlanguni.vn')])]"
                )
            )
        );

        // Mở menu tài khoản
        js.executeScript("arguments[0].click();", accountMenu);

        WebElement logoutButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//a[contains(normalize-space(.),'Đăng xuất')]"
                    + " | //button[contains(normalize-space(.),'Đăng xuất')]"
                )
            )
        );

        // Tự động đăng xuất
        js.executeScript("arguments[0].click();", logoutButton);

        // Chờ quay về trang đăng nhập
        wait.until(ExpectedConditions.urlContains("/Account/Login"));

        Assert.assertTrue(
            driver.getCurrentUrl().contains("/Account/Login"),
            "Đăng xuất không thành công!"
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
