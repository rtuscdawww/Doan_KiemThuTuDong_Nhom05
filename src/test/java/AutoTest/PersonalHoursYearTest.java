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

public class PersonalHoursYearTest {
    @Test
    public void viewPersonalHoursYearEmptyStatistics() {
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

            // 2. Chờ menu con Số giờ cá nhân xuất hiện và có thể nhấn
            WebElement personalHoursSubMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//*[normalize-space()='Số giờ cá nhân']"
                    )
                )
            );

            // Nhấn menu con Số giờ cá nhân
            personalHoursSubMenu.click();

            // Đợi trang xử lý thao tác (URL thay đổi sang trang /Statistics/Personal)
            wait.until(d ->
                d.getCurrentUrl().contains("/Statistics/Personal") || !d.getCurrentUrl().contains("/Account/Login")
            );

            System.out.println(
                "URL sau khi nhấn Số giờ cá nhân: "
                + driver.getCurrentUrl()
            );

            // Kiểm tra xem đã chuyển hướng đúng sang trang thống kê cá nhân chưa
            Assert.assertTrue(
                driver.getCurrentUrl().contains("Personal"),
                "Không mở được trang Thống kê số giờ cá nhân!"
            );

            System.out.println(
                "Đã thực hiện thao tác mở trang Thống kê số giờ cá nhân thành công."
            );

            // ==========================================
            // THAO TÁC ĐỔI BỘ LỌC VÀ CHỌN NĂM HỌC
            // ==========================================
            
            // 3. Tìm dropdown "Thống kê theo:"[cite: 15]
            WebElement thongKeTheoDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Thống kê theo')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Thống kê theo')]/following-sibling::div | //*[contains(text(), 'Thống kê theo')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            
            try {
                wait.until(ExpectedConditions.elementToBeClickable(thongKeTheoDropdown)).click();
            } catch (Exception e) {
                System.out.println("Dùng click cơ bản thất bại, chuyển sang click bằng Javascript...");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", thongKeTheoDropdown);
            }
            
            // Chọn "Năm học" từ danh sách xổ ra[cite: 15]
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

            // 4. Tìm dropdown "Năm học:"[cite: 15]
            WebElement namHocDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Năm học')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Năm học')]/following-sibling::div | //*[contains(text(), 'Năm học')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            
             try {
                wait.until(ExpectedConditions.elementToBeClickable(namHocDropdown)).click();
            } catch (Exception e) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", namHocDropdown);
            }

            // Chọn Năm học "2033 - 2034"[cite: 15]
            WebElement option2033 = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='2033 - 2034'] | //div[normalize-space()='2033 - 2034'] | //span[normalize-space()='2033 - 2034']")
                )
            );
            
             try {
                wait.until(ExpectedConditions.elementToBeClickable(option2033)).click();
            } catch (Exception e) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", option2033);
            }
            System.out.println("Đã lọc theo Năm học: 2033 - 2034.");

            // 5. Xác nhận hiển thị thông báo "Chưa có dữ liệu"[cite: 15]
            WebElement emptyDataMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Chưa có dữ liệu')]")
                )
            );

            Assert.assertTrue(
                emptyDataMessage.isDisplayed(),
                "Lỗi: Không hiển thị thông báo 'Chưa có dữ liệu' khi chọn năm học không có thực tế."
            );
            
            System.out.println("Test Passed: Hệ thống hiển thị đúng trạng thái 'Chưa có dữ liệu'.");

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