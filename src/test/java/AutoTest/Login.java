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
import org.testng.annotations.Test;

import java.time.Duration;

public class Login {
    @Test
    public void testLoginValidAccount() {
        System.out.println("--- CHẠY TC-LOGIN-01: Đăng nhập hợp lệ ---");
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Nhấn nút Đăng nhập nếu có
            if (driver.getCurrentUrl().contains("/Account/Login")) {
                WebElement btnDangNhap = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit'] | //form//button | (//button)[1]")
                ));
                js.executeScript("arguments[0].click();", btnDangNhap);
            }

            wait.until(d -> !d.getCurrentUrl().contains("/Account/Login")); //xác thực

            String currentUrl = driver.getCurrentUrl();
            System.out.println("URL hiện tại: " + currentUrl);
            Assert.assertFalse(currentUrl.contains("/Account/Login"), "TC-LOGIN-01 FAIL: Vẫn kẹt ở trang Login.");
            
            System.out.println("TC-LOGIN-01 PASS: Đăng nhập hợp lệ thành công, đã chuyển tới Dashboard.");

        } finally {
            driver.quit();
        }
    }

    @Test
    public void testLoginInvalidAccount() {
        System.out.println("--- CHẠY TC-LOGIN-02: Đăng nhập không hợp lệ ---");
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            driver.manage().window().maximize();
            driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Nhấn nút Đăng nhập
            if (driver.getCurrentUrl().contains("/Account/Login")) {
                WebElement btnDangNhap = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit'] | //form//button | (//button)[1]")
                ));
                js.executeScript("arguments[0].click();", btnDangNhap);
            }

            wait.until(ExpectedConditions.urlContains("login.microsoftonline.com"));
            System.out.println("Đã nhảy sang trang Microsoft để nhập User/Pass.");

            WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='email']")));
            emailInput.sendKeys("rtuscdawww26@gmail.com");
          
            WebElement btnNext = driver.findElement(By.xpath("//input[@type='submit']"));
            js.executeScript("arguments[0].click();", btnNext);

            WebElement errorMsg = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@id, 'usernameError')] | //*[contains(text(), 'sự cố') or contains(text(), 'We couldn')]")
            ));

            Assert.assertTrue(errorMsg.isDisplayed(), "TC-LOGIN-02 FAIL: Không thấy cảnh báo lỗi đăng nhập.");
            
            System.out.println("TC-LOGIN-02 PASS: Đã chặn thành công tài khoản không hợp lệ và hiện cảnh báo.");

        } catch (Exception e) {
            System.out.println("Có lỗi trong quá trình chạy TC-LOGIN-02: " + e.getMessage());
            Assert.fail("TC-LOGIN-02 FAIL do lỗi Exception.");
        } finally {
            try {
                Thread.sleep(15000); 
            } catch (InterruptedException ignored) {}
            driver.quit();
        }
    }
}