package AutoTest;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class ViewLecturerTimetableTest {

    @Test
    public void viewLecturerTimetable() {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

            // ==========================================
            // 1. ĐĂNG NHẬP
            // ==========================================
            if (driver.getCurrentUrl().contains("/Account/Login")) {
                WebElement btnDangNhap = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit'] | //form//button | (//button)[1]")
                ));
                js.executeScript("arguments[0].click();", btnDangNhap);
                wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/Account/Login")));
            }

            // ==========================================
            // 2. MỞ MENU TỚI LỊCH GIẢNG DẠY
            // ==========================================
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(), 'ng k')] | //a[contains(@href, 'Statistics')]")
            )).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, '/Statistics/Timetable')]")
            )).click();

            wait.until(ExpectedConditions.urlContains("/Statistics/Timetable"));
            System.out.println("Đã mở Lịch giảng dạy.");
            Thread.sleep(2000);

            // ==========================================
            // 3. CHỌN HỌC KỲ 998 (Nếu cần)
            // ==========================================
            try {
                WebElement hocKyBox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//label[contains(text(), 'c k')]/following-sibling::*//span[contains(@class, 'select2-selection')])[1] | (//span[contains(@class, 'select2-selection')])[1]")
                ));
                hocKyBox.click();

                WebElement option998 = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class,'select2-results__option') and contains(text(), '998')]")
                ));
                option998.click();
                System.out.println("Đã chọn Học kỳ 998.");
                Thread.sleep(1500);
            } catch (Exception e) {
                System.out.println("Bỏ qua chọn Học kỳ, sử dụng mặc định.");
            }

            // ==========================================
            // 4. LỌC TỪNG GIẢNG VIÊN (SỬ DỤNG ĐÚNG PLACEHOLDER)
            // ==========================================
            String[] lecturers = {
                "\u0110\u1eb7ng Th\u1ecb Tr\u00fac \u0110\u00e0o", // Đặng Thị Trúc Đào
                "L\u00ea Nguy\u00ean Kh\u1ea3i"                  // Lê Nguyên Khải
            };

            for (String lecturer : lecturers) {
                // TÌM CHÍNH XÁC Ô CÓ CHỮ MỜ "Lọc giảng viên" ĐỂ TRÁNH NHẦM LẪN VỚI Ô KHÁC
                WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Lọc giảng viên' or contains(@placeholder, 'gi')]")
                ));
                
                searchBox.click();
                searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                
                // Gõ tên giảng viên
                searchBox.sendKeys(lecturer);
                
                // Chờ hệ thống quay vòng vòng tìm kiếm
                Thread.sleep(1500); 

                // Nhấn phím Enter để dứt khoát chọn kết quả đầu tiên thả xuống
                searchBox.sendKeys(Keys.ENTER);
                System.out.println("=> Đã tick chọn xong giảng viên: " + lecturer);
                
                // Dừng 1 giây để bảng load xong rồi mới điền người tiếp theo
                Thread.sleep(1000); 
            }

            System.out.println("==> ĐÃ HOÀN TẤT LỌC ĐỦ 2 GIẢNG VIÊN!");

            // ==========================================
            // 5. XEM BẢNG VÀ KẾT THÚC
            // ==========================================
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));
            System.out.println("Đã hiển thị lịch giảng dạy thành công. Dừng hình 15 giây...");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xem lịch giảng dạy", e);
        } finally {
            try {
                // Đứng hình 15 giây để bạn xem kết quả trên bảng
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (driver != null) {
                driver.quit();
            }
            System.out.println("Đã đóng trình duyệt.");
        }
    }
}