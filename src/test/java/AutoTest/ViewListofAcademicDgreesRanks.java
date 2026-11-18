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
import java.util.List;

public class ViewListofAcademicDgreesRanks {
     @Test
    public void viewAcademicDegreeRankList() {
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

            // 1. Tự động đăng nhập 
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

            // 2. Mở menu Thù lao 
            WebElement thuLaoMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@id='sidebarnav']//li/a[contains(@href, '#') and .//i[contains(@class, 'money') or contains(@class, 'dollar') or contains(@class, 'cash')]] | //span[contains(., 'Th') and contains(., 'lao')]")
                )
            );
            try {
                thuLaoMenu.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", thuLaoMenu);
            }
            System.out.println("Đã nhấn menu Thù lao.");

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            // 3. Mở menu Học hàm, học vị (Menu cha)
            WebElement academicDegreeMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href,'/AcademicDegree')]")
                )
            );
            try {
                academicDegreeMenu.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", academicDegreeMenu);
            }
            System.out.println("Đã nhấn menu con Học hàm, học vị.");

            // 4. Chuyển sang Tab "Cấp bậc"
            WebElement tabCapBac = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'nav-tabs')]//a[contains(@href,'/AcademicDegreeRank') or contains(., 'Cấp bậc')]")
                )
            );
            try {
                tabCapBac.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabCapBac);
            }

            // Đợi trang xử lý chuyển hướng URL sang Cấp bậc
            wait.until(d -> d.getCurrentUrl().contains("/AcademicDegreeRank"));
            System.out.println("URL sau khi nhấn tab Cấp bậc: " + driver.getCurrentUrl());

            // Đợi bảng dữ liệu load xong (tìm thanh search)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='search' or contains(@placeholder, 'tìm kiếm')]")));

            // 5. Kiểm tra các cột tiêu đề của bảng Cấp bậc
            // Dựa theo giao diện, bảng này chủ yếu có cột "MÃ CẤP BẬC"
            WebElement colMaCapBac = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//thead//th[1]")));
            
            Assert.assertTrue(colMaCapBac.isDisplayed(), "Lỗi: Không hiển thị tiêu đề cột (Mã cấp bậc)");
            System.out.println("Kiểm tra tiêu đề bảng: Hợp lệ.");

            // 6. Kiểm tra dữ liệu trong bảng
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            if (rows.size() == 1 && rows.get(0).getText().toLowerCase().contains("không có")) {
                 System.out.println("-> Bảng hiện tại đang trống (chưa có dữ liệu). Pass kịch bản hiển thị bảng trống.");
            } else {
                 Assert.assertTrue(rows.size() > 0, "Lỗi: Bảng không render được thẻ <tr> nào!");
                 System.out.println("-> Xem danh sách thành công, tổng số dòng (bao gồm cả dòng tiêu đề nhóm) hiển thị trên trang hiện tại: " + rows.size());
            }

        } finally {
            try {
                Thread.sleep(5000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
