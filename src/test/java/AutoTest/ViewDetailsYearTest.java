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

public class ViewDetailsYearTest {
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

            WebElement thongKeTheoDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Thống kê theo')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Thống kê theo')]/following-sibling::div | //*[contains(text(), 'Thống kê theo')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(thongKeTheoDropdown)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", thongKeTheoDropdown); }
            
            WebElement optionNamHoc = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='Năm học'] | //div[normalize-space()='Năm học'] | //span[normalize-space()='Năm học']")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(optionNamHoc)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionNamHoc); }
            System.out.println("Da chon Thong ke theo nam hoc.");

            try { Thread.sleep(1000); } catch (InterruptedException ignored) {} // Nghỉ 1s chờ UI cập nhật

            WebElement namHocDropdown = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[contains(text(), 'Năm học')]/ancestor::div[1]/following-sibling::div | //label[contains(text(), 'Năm học')]/following-sibling::div | //*[contains(text(), 'Năm học')]/../..//div[@role='combobox' or contains(@class, 'select')]")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(namHocDropdown)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", namHocDropdown); }

            WebElement option2025 = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[normalize-space()='2025 - 2026'] | //div[normalize-space()='2025 - 2026'] | //span[normalize-space()='2025 - 2026']")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(option2025)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option2025); }
            System.out.println("Da loc Nam hoc: 2025 - 2026.");

            try { Thread.sleep(1000); } catch (InterruptedException ignored) {} // Nghỉ 1s chờ bảng tải dữ liệu

            WebElement tabChiTiet = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@class, 'nav-link') and contains(text(), 'Chi')] | //a[contains(text(), 'Chi')] | //div[contains(text(), 'Chi')]")
                )
            );
            try { wait.until(ExpectedConditions.elementToBeClickable(tabChiTiet)).click(); } 
            catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabChiTiet); }
            
            System.out.println("Da mo thanh cong man hinh Chi tiet.");

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