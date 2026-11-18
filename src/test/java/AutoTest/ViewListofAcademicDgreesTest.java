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

public class ViewListofAcademicDgreesTest {
    
    @Test
    public void viewAcademicDegreeList() {
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

            // Tạm dừng một chút để chờ hiệu ứng menu xổ xuống
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            // 3. Mở menu Học hàm, học vị (Menu con)
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
            System.out.println("Đã nhấn menu Học hàm, học vị.");

            // Đợi trang xử lý chuyển hướng URL
            wait.until(d -> d.getCurrentUrl().contains("/AcademicDegree"));
            System.out.println("URL sau khi nhấn: " + driver.getCurrentUrl());

            // 4. Kiểm tra
            WebElement tabHocHam = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'nav-tabs')]//a[contains(@href,'/AcademicDegree')] | //a[contains(@href,'/AcademicDegree') and not(contains(@href, 'Rank'))]")
                )
            );
            try {
                tabHocHam.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabHocHam);
            }

            // Đợi bảng dữ liệu load xong (tìm thanh search để chắc chắn trang đã render)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='search' or contains(@placeholder, 'm')]")));

            // 5. Kiểm tra các cột tiêu đề của bảng (Dùng index để tránh lỗi font tiếng Việt)
            WebElement colMa = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//thead//th[1]")));
            WebElement colTen = driver.findElement(By.xpath("//table//thead//th[2]"));
            WebElement colThuTu = driver.findElement(By.xpath("//table//thead//th[3]"));
            
            Assert.assertTrue(colMa.isDisplayed(), "Lỗi: Không hiển thị cột Mã học hàm, học vị (Cột 1)");
            Assert.assertTrue(colTen.isDisplayed(), "Lỗi: Không hiển thị cột Tên học hàm, học vị (Cột 2)");
            Assert.assertTrue(colThuTu.isDisplayed(), "Lỗi: Không hiển thị cột Thứ tự (Cột 3)");
            System.out.println("Kiểm tra tiêu đề bảng: Hợp lệ.");

            // 6. Kiểm tra dữ liệu trong bảng
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            if (rows.size() == 1 && rows.get(0).getText().toLowerCase().contains("không có")) {
                 System.out.println("-> Bảng hiện tại đang trống (chưa có dữ liệu). Pass kịch bản hiển thị bảng trống.");
            } else {
                 Assert.assertTrue(rows.size() > 0, "Lỗi: Bảng không render được thẻ <tr> nào!");
                 System.out.println("-> Xem danh sách thành công, tổng số dòng hiển thị trên trang hiện tại: " + rows.size());
            }

        } finally {
            try {
                // Dừng lại 5 giây trước khi thực hiện lệnh quit
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