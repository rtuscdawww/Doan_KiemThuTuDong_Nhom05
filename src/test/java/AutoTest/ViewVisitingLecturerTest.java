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

public class ViewVisitingLecturerTest {
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

            driver.get(
                "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/"
            );

            WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(30));

            System.out.println("Đang ở URL: " + driver.getCurrentUrl());

            // 1. Chờ và nhấn menu Thống kê
            WebElement statsMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space()='Thống kê']")
                )
            );
            statsMenu.click();

            // 2. Chờ và nhấn menu con GV thỉnh giảng
            WebElement visitingLecturerSubMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space()='GV thỉnh giảng']")
                )
            );
            visitingLecturerSubMenu.click();

            // Đợi trang xử lý chuyển hướng
            wait.until(d ->
                d.getCurrentUrl().contains("/Statistics/VisitingLecturer") || !d.getCurrentUrl().contains("/Account/Login")
            );
            
            Assert.assertTrue(
                driver.getCurrentUrl().contains("Statistics"),
                "Không mở được trang Thống kê GV thỉnh giảng!"
            );

            // ==========================================
            // THAO TÁC CHỌN HỌC KỲ
            // ==========================================
            WebElement hocKyDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(), 'Chọn học kỳ')] | //label[contains(text(), 'Học kỳ:')]/following-sibling::div//input")
                )
            );
            try {
                wait.until(ExpectedConditions.elementToBeClickable(hocKyDropdown)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hocKyDropdown);
            }
            
            // Chọn một học kỳ (Ví dụ: 999)
            WebElement option999 = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='999'] | //div[normalize-space()='999']")
                )
            );
            try {
                wait.until(ExpectedConditions.elementToBeClickable(option999)).click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option999);
            }
            System.out.println("Đã chọn học kỳ 999.");

            // ==========================================
            // THAO TÁC NHẤN NÚT "THỐNG KÊ" MÀU TÍM
            // ==========================================
            WebElement btnThongKe = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[contains(., 'Thống kê')] | //div[contains(@class, 'btn') and contains(., 'Thống kê')]")
                )
            );
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(btnThongKe)).click();
            } catch (Exception e) {
                System.out.println("Click thường bị chặn, dùng Javascript để click nút Thống kê...");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnThongKe);
            }
            System.out.println("Đã nhấn nút Thống kê.");

            // ==========================================
            // CHỜ HIỂN THỊ KẾT QUẢ VÀ TẮT TRÌNH DUYỆT
            // ==========================================
            // Chờ một phần tử trong bảng dữ liệu xuất hiện (chứng tỏ đã load xong)
            WebElement tableHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//th[contains(., 'MÃ GVTG')] | //*[contains(text(), 'Hiển thị')]")
                )
            );
            
            Assert.assertTrue(tableHeader.isDisplayed(), "Bảng kết quả chưa hiển thị!");
            System.out.println("Load dữ liệu thành công!");

        } finally {
            try {
                // Dừng 15 giây để bạn nhìn thấy kết quả
                Thread.sleep(15000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
            System.out.println("Đã đóng trình duyệt.");
        }
    }
}