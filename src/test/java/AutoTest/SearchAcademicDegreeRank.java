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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SearchAcademicDegreeRank {
      private WebDriver driver;
      private WebDriverWait wait;

      @BeforeClass
     public void setupAndNavigate() {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");
        
        // 1. Tự động đăng nhập
        if (driver.getCurrentUrl().contains("/Account/Login")) {
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
          }

     @Test(priority = 1)
     public void CB_results_1() throws InterruptedException {
        WebElement searchBox = driver.findElement(By.xpath("//input[@type='search' or contains(@placeholder, 'tìm kiếm')]"));
        searchBox.clear();
        
        // Nhập từ khóa tồn tại
        String validKeyword = "CB_TEST"; 
        searchBox.sendKeys(validKeyword);
        Thread.sleep(5000); 
        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        boolean hasData = rows.size() > 0 && !rows.get(0).getText().toLowerCase().contains("không có");
        
        Assert.assertTrue(hasData, "Lỗi: Tìm kiếm từ khóa hợp lệ nhưng không trả về kết quả!");
        System.out.println("TC_F7_05_01 Passed: Tìm kiếm có kết quả trả về " + rows.size() + " dòng.");
    }
    @Test(priority = 2)
    public void CB_results_2() throws InterruptedException {
        WebElement searchBox = driver.findElement(By.xpath("//input[@type='search' or contains(@placeholder, 'tìm kiếm')]"));
        searchBox.clear();
        
        // Nhập từ khóa không tồn tại
        String invalidKeyword = "test_CB"; 
        searchBox.sendKeys(invalidKeyword);
        Thread.sleep(5000); // Chờ table filter

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        
        // Bảng Datatable thường trả về 1 dòng chứa câu thông báo khi không có dữ liệu
        boolean isEmptyMsgDisplayed = rows.size() == 1 && 
            (rows.get(0).getText().toLowerCase().contains("không tìm thấy") || 
             rows.get(0).getText().toLowerCase().contains("không có") || 
             rows.get(0).getText().toLowerCase().contains("no data"));
             
        Assert.assertTrue(isEmptyMsgDisplayed, "Lỗi: Tìm kiếm từ khóa không tồn tại nhưng vẫn hiển thị dữ liệu hoặc sai thông báo trống!");
        System.out.println("Passed: Tìm kiếm không có kết quả hiển thị thông báo trống chính xác.");
    }
    @AfterClass
    public void teardown() {
        if (driver != null) {
            try {
                Thread.sleep(3000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }

    // Hàm hỗ trợ click
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
     }
}

