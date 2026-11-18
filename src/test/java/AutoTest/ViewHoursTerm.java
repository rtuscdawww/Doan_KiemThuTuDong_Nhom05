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

public class ViewHoursTerm {
     @Test
    public void viewLecturerHoursStatistics() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments(
            "user-data-dir=C:\\ChromeSeleniumProfile"
        );

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.manage().window().maximize();
            driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            System.out.println("Dang o URL: " + driver.getCurrentUrl());

            if (driver.getCurrentUrl().contains("/Account/Login")) {
                System.out.println("He thong yeu cau Dang nhap, tu dong click Dang nhap");
                WebElement btnDangNhap = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[normalize-space()='Đăng nhập'] | //button[contains(., 'Đăng nhập')] | //a[contains(., 'Đăng nhập')]")
                    )
                );
                try {
                    btnDangNhap.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnDangNhap);
                }
                wait.until(d -> !d.getCurrentUrl().contains("/Account/Login"));
                System.out.println("Dang nhap thanh cong!");
            }


            // Chờ menu Thống kê xuất hiện và có thể nhấn
            WebElement statsMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//*[normalize-space()='Thống kê']"
                    )
                )
            );
            
            // Nhấn menu Thống kê để mở rộng danh sách menu con
            statsMenu.click();
            System.out.println("Đã nhấn menu Thống kê.");

            // Chờ menu con Số giờ giảng viên xuất hiện và có thể nhấn
            WebElement lecturerHoursSubMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//*[normalize-space()='Số giờ giảng viên']"
                    )
                )
            );

            // Nhấn menu con Số giờ giảng viên
            lecturerHoursSubMenu.click();

            // Đợi trang xử lý thao tác (URL thay đổi sang trang Statistics)
            wait.until(d ->
                d.getCurrentUrl().contains("/Statistics") || !d.getCurrentUrl().contains("/Account/Login")
            );

            System.out.println(
                "URL sau khi nhấn Số giờ giảng viên: "
                + driver.getCurrentUrl()
            );

            // Kiểm tra xem đã chuyển hướng đúng sang trang thống kê chưa
            Assert.assertTrue(
                driver.getCurrentUrl().contains("Statistics"),
                "Không mở được trang Thống kê số giờ giảng viên!"
            );

            System.out.println(
                "Đã thực hiện thao tác mở trang Thống kê số giờ giảng viên thành công."
            );

        } finally {
            try {
                // Dừng lại 20 giây trước khi thực hiện lệnh quit
                Thread.sleep(5000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}
