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

public class PersonalHoursTerm {
    @Test
    public void viewPersonalHoursEmptyStatistics() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments(
            "user-data-dir=C:\\ChromeSeleniumProfile"
        );

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.manage().window().maximize();

            driver.get(
                "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/"
            );

            WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

            System.out.println(
                "Đang ở URL: " + driver.getCurrentUrl()
            );

            // 1. Chờ menu Thống kê xuất hiện và có thể nhấn
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

            // 2. Chờ menu con Số giờ cá nhân xuất hiện và có thể nhấn[cite: 14]
            WebElement personalHoursSubMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//*[normalize-space()='Số giờ cá nhân']"
                    )
                )
            );

            // Nhấn menu con Số giờ cá nhân
            personalHoursSubMenu.click();

            // Đợi trang xử lý thao tác (URL thay đổi sang trang /Statistics/Personal)[cite: 14]
            wait.until(d ->
                d.getCurrentUrl().contains("/Statistics/Personal") || !d.getCurrentUrl().contains("/Account/Login")
            );

            System.out.println(
                "URL sau khi nhấn Số giờ cá nhân: "
                + driver.getCurrentUrl()
            );

            // Kiểm tra xem đã chuyển hướng đúng sang trang thống kê cá nhân chưa[cite: 14]
            Assert.assertTrue(
                driver.getCurrentUrl().contains("Personal"),
                "Không mở được trang Thống kê số giờ cá nhân!"
            );

            System.out.println(
                "Đã thực hiện thao tác mở trang Thống kê số giờ cá nhân thành công."
            );

            // ==========================================
            // THÊM THAO TÁC CHỌN HỌC KỲ VÀ KIỂM TRA TRẠNG THÁI TRỐNG
            // ==========================================
            
            // 3. Tìm dropdown "Học kỳ:"[cite: 14]
            WebElement hocKyDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Học kỳ')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Học kỳ')]/following-sibling::div | //*[contains(text(), 'Học kỳ')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(hocKyDropdown)).click();
            } catch (Exception e) {
                System.out.println("Dùng click cơ bản thất bại, chuyển sang click bằng Javascript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", hocKyDropdown);
            }
            
            // 4. Chọn Học kỳ "998"[cite: 14]
            WebElement option998 = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='998'] | //div[normalize-space()='998'] | //span[normalize-space()='998']")
                )
            );
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(option998)).click();
            } catch (Exception e) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", option998);
            }
            
            System.out.println("Đã chọn học kỳ: 998.");

            // 5. Xác nhận hiển thị thông báo "Chưa có dữ liệu hệ số cho năm học này"[cite: 14]
            WebElement emptyDataMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Chưa có dữ liệu hệ số cho năm học này')]")
                )
            );

            Assert.assertTrue(
                emptyDataMessage.isDisplayed(),
                "Lỗi: Không hiển thị thông báo 'Chưa có dữ liệu hệ số cho năm học này' khi chọn học kỳ không có thực tế."
            );
            
            System.out.println("Test Passed: Hệ thống hiển thị đúng trạng thái 'Chưa có dữ liệu hệ số cho năm học này'.");

        } finally {
            try {
                // Dừng lại 15 giây trước khi thực hiện lệnh quit để quan sát kết quả
                Thread.sleep(15000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}