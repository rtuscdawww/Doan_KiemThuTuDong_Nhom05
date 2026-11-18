package AutoTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class ViewDetailsTermTest {
    @Test
    public void viewLecturerHoursDetailStatistics() {
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

            WebElement statsMenu = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[normalize-space()='Thống kê']"))
            );
            statsMenu.click();

            WebElement lecturerHoursSubMenu = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[normalize-space()='Số giờ giảng viên']"))
            );
            lecturerHoursSubMenu.click();

            wait.until(d -> d.getCurrentUrl().contains("/Statistics"));
            System.out.println("Da vao trang Thong ke so gio giang vien.");

            // ==========================================
            // BƯỚC 1: TÌM VÀ NHẤN DROPDOWN "HỌC KỲ"
            // ==========================================
            WebElement hocKyDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Học kỳ')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Học kỳ')]/following-sibling::div | //*[contains(text(), 'Học kỳ')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(hocKyDropdown)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hocKyDropdown); }
            
            // ==========================================
            // BƯỚC 2: CHỌN "998" TỪ DANH SÁCH XỔ RA
            // ==========================================
            WebElement option998 = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='998'] | //div[normalize-space()='998'] | //span[normalize-space()='998']")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(option998)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option998); }
            System.out.println("Da loc Hoc ky: 998.");

            try { Thread.sleep(1000); } catch (InterruptedException ignored) {} // Nghỉ 1s chờ bảng tải dữ liệu

            // ==========================================
            // BƯỚC 3: NHẤN SANG TAB "CHI TIẾT"
            // ==========================================
            WebElement tabChiTiet = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@class, 'nav-link') and contains(text(), 'Chi')] | //a[contains(text(), 'Chi')] | //div[contains(text(), 'Chi')]")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(tabChiTiet)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabChiTiet); }
            
            System.out.println("Da mo thanh cong man hinh Chi tiet theo Hoc ky 998.");

        } finally {
            try {
                Thread.sleep(8000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
            System.out.println("Dong trinh duyet Chrome.");
        }
    }
}