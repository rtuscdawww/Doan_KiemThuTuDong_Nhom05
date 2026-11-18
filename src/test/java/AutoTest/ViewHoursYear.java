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

public class ViewHoursYear {
    @Test
    public void viewLecturerHoursYearStatistics() {
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

            // ==========================================
            // THÊM THAO TÁC CHỌN THỐNG KÊ THEO: NĂM HỌC
            // ==========================================
            
            // 1. Tìm dropdown "Thống kê theo:" bằng XPath quét rộng cấu trúc HTML
            WebElement thongKeTheoDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Thống kê theo')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Thống kê theo')]/following-sibling::div | //*[contains(text(), 'Thống kê theo')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            
            // Cố gắng click bình thường. Nếu UI có animation che khuất gây lỗi, dùng Javascript ép click!
            try {
                wait.until(ExpectedConditions.elementToBeClickable(thongKeTheoDropdown)).click();
            } catch (Exception e) {
                System.out.println("Dùng click cơ bản thất bại, chuyển sang click bằng Javascript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", thongKeTheoDropdown);
            }
            
            // 2. Chờ danh sách xổ ra và chọn "Năm học"
            WebElement optionNamHoc = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='Năm học'] | //div[normalize-space()='Năm học'] | //span[normalize-space()='Năm học']")
                )
            );
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(optionNamHoc)).click();
            } catch (Exception e) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", optionNamHoc);
            }
            
            System.out.println("Đã chuyển bộ lọc thành: Thống kê theo Năm học.");

        } finally {
            try {
                // Dừng lại 20 giây trước khi thực hiện lệnh quit để quan sát kết quả
                Thread.sleep(20000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }
}