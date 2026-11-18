package AutoTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test; 

import java.time.Duration;

public class LoginTest {
    @Test
    public void loginTest() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        // Sử dụng profile Chrome đã đăng nhập tài khoản BCN Khoa
        //options.addArguments(
         //   "user-data-dir=C:\\ChromeSeleniumProfile"
        //);

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.manage().window().maximize();

            System.out.println("Bắt đầu truy cập vào hệ thống...");
            driver.get(
                "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/"
            );

            WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

            // Đợi quá trình chuyển hướng hoàn tất (đảm bảo không kẹt ở trang Login)
            wait.until(d ->
                !d.getCurrentUrl().contains("/Account/Login")
            );

            String currentUrl = driver.getCurrentUrl();
            System.out.println(
                "URL hiện tại sau khi truy cập: " + currentUrl
            );

            // Assert: Kiểm tra xem có về được trang chủ (không phải trang đăng nhập) hay không
            Assert.assertFalse(
                currentUrl.contains("/Account/Login"),
                "Đăng nhập thất bại, vẫn đang ở trang Login!"
            );

            System.out.println(
                "Đăng nhập tự động thành công!"
            );

        } finally {
            try {
                // Dừng lại 40 giây để xem kết quả trước khi tắt
                Thread.sleep(40000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
