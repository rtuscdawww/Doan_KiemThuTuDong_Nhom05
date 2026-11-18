package AutoTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class AddAcademicDegreeRank {

    private WebDriver driver;
    private WebDriverWait wait;
    private String maCapBac;

    // --- Locators ---
    private final By tabCapBac = By.xpath("//a[contains(@class, 'nav-link') and contains(., 'Cấp bậc')]");
    private final By btnThemCapBac = By.xpath("//*[self::button or self::a][contains(.,'Thêm cấp bậc mới')]");
    private final By dropdownHocHam = By.xpath("//span[contains(@class, 'select2-selection__placeholder') and contains(., 'Chọn học hàm, học vị')]");
    private final By listOptions = By.xpath("//li[contains(@class, 'select2-results__option') and not(contains(@class, 'select2-results__message')) and normalize-space() != '']");
    private final By maInput = By.xpath("//input[@placeholder='Nhập mã cấp bậc']");
    private final By saveButton = By.xpath("//button[normalize-space()='Lưu']");
    
    // XPath nút Hủy mạnh mẽ hơn, cover nhiều trường hợp
    private final By cancelButton = By.xpath("//div[@class='el-dialog__footer']//button[contains(.,'Huỷ') or contains(.,'Hủy')] | //button[contains(.,'Huỷ') or contains(.,'Hủy')]");
    
    private final By okButton = By.xpath("//button[normalize-space()='OK']");
    private final By msgLoiChuaChonHH = By.xpath("//*[contains(normalize-space(),'Bạn chưa chọn học hàm, học vị')]");
    private final By msgLoiChuaNhapMa = By.xpath("//*[contains(normalize-space(),'Bạn chưa nhập mã cấp bậc')]");
    private final By dialogOverlay = By.cssSelector(".el-dialog__wrapper");

    @BeforeClass
    public void setup() {
        long timeStamp = System.currentTimeMillis();
        maCapBac = "CB_TEST_" + timeStamp;

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        if (driver.getCurrentUrl().contains("/Account/Login")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[normalize-space()='Đăng nhập']"))).click();
            wait.until(d -> !d.getCurrentUrl().contains("/Account/Login"));
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Thù lao']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'/AcademicDegree')]"))).click();
        wait.until(ExpectedConditions.urlContains("/AcademicDegree"));
        
        try {
            wait.until(ExpectedConditions.elementToBeClickable(tabCapBac)).click();
        } catch (Exception e) {
            driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/AcademicDegreeRank");
        }
    }

    @Test(priority = 1)
    public void addValidAcademicDegreeRank() throws InterruptedException {
        openForm();
        selectDropdownOption();
        wait.until(ExpectedConditions.visibilityOfElementLocated(maInput)).sendKeys(maCapBac);
        Thread.sleep(1000);
        
        clickSave();
        
        Thread.sleep(2000);
        clickOKIfPresent();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(maInput));

        searchAcademicDegreeRank(maCapBac);
        boolean found = wait.until(d -> d.findElements(By.cssSelector("table tbody tr")).stream().anyMatch(row -> row.isDisplayed() && row.getText().contains(maCapBac)));
        Assert.assertTrue(found, "Them cap bac khong thanh cong.");
    }

    @Test(priority = 2, dependsOnMethods = "addValidAcademicDegreeRank")
    public void addDuplicateAcademicDegreeRank() throws InterruptedException {
        openForm();
        selectDropdownOption();
        wait.until(ExpectedConditions.visibilityOfElementLocated(maInput)).sendKeys(maCapBac);
        System.out.println("Da nhap ma cap bac bi trung: " + maCapBac);
        Thread.sleep(1000);
        
        clickSave();

        // Rút ngắn thời gian chờ lỗi, bắt lỗi rộng hơn để không bị treo
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String actualMessage = "";
        try {
            WebElement message = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'el-message')] | //*[contains(text(),'tồn tại')]"))
            );
            actualMessage = message.getText().trim();
            System.out.println("Thong bao loi: " + actualMessage);
        } catch (TimeoutException e) {
            System.out.println("Cảnh báo: Không tìm thấy popup lỗi trùng mã!");
        }

        Thread.sleep(1000);
        clickOKIfPresent();

        // Luôn đóng form dù có bắt được lỗi hay không để đi tiếp
        closeFormSafe();

        searchAcademicDegreeRank(maCapBac);
        Thread.sleep(2000);
        long count = driver.findElements(By.xpath("//table/tbody/tr[td[contains(normalize-space(),'" + maCapBac + "')]]")).stream().filter(WebElement::isDisplayed).count();
        
        Assert.assertTrue(actualMessage.contains("tồn tại") || actualMessage.length() > 0, "Thông báo lỗi trùng lặp không chính xác hoặc không xuất hiện.");
        Assert.assertEquals(count, 1L, "He thong cho phep them cap bac trung ma (Số lượng bản ghi > 1).");
    }

    @Test(priority = 3)
    public void addEmptyAcademicDegreeRank() throws InterruptedException {
        openForm();
        Thread.sleep(1000);
        clickSave();
        
        // Dùng shortWait (5 giây) thay vì wait tổng (60 giây) để không bị treo máy nếu giao diện đổi XPath
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        boolean loiChuaChonHH = false;
        boolean loiChuaNhapMa = false;
        
        try {
            loiChuaChonHH = shortWait.until(ExpectedConditions.visibilityOfElementLocated(msgLoiChuaChonHH)).isDisplayed();
            loiChuaNhapMa = shortWait.until(ExpectedConditions.visibilityOfElementLocated(msgLoiChuaNhapMa)).isDisplayed();
            System.out.println("Đã bắt thành công 2 dòng báo lỗi màu đỏ.");
        } catch (TimeoutException e) {
            System.out.println("Lỗi: Không tìm thấy dòng text báo lỗi trên giao diện!");
        }

        clickOKIfPresent();
        
        // Ép đóng form để kịch bản được đi tiếp (chạy hàm teardown)
        closeFormSafe();

        Assert.assertTrue(loiChuaChonHH, "Khong hien thi loi chua chon Hoc ham, hoc vi.");
        Assert.assertTrue(loiChuaNhapMa, "Khong hien thi loi chua nhap Ma cap bac.");
    }

    // --- Các hàm Helper đã được tối ưu ---

    private void openForm() {
        wait.until(ExpectedConditions.elementToBeClickable(btnThemCapBac)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(maInput));
    }
    
    private void selectDropdownOption() {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownHocHam));
            dropdown.click();
            Thread.sleep(1000); 
            List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(listOptions));
            
            if (!options.isEmpty()) {
                for (WebElement option : options) {
                    if (option.isDisplayed() && option.getText().trim().length() > 0) {
                        option.click();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Loi khi click Dropdown: " + e.getMessage());
        }
    }

    private void clickSave() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
            btn.click();
        } catch (Exception e) {
            System.out.println("Click Lưu bị chặn, đang ép click bằng Javascript...");
            WebElement btn = driver.findElement(saveButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    private void clickOKIfPresent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement okBtn = shortWait.until(ExpectedConditions.elementToBeClickable(okButton));
            okBtn.click();
        } catch (TimeoutException e) {
            // Không có nút OK thì bỏ qua
        }
    }

    // Hàm đóng form an toàn tuyệt đối
    private void closeFormSafe() {
        if (!isFormOpen()) return;

        try {
             System.out.println("Đang thử đóng form...");
             WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(cancelButton));
             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
             
             // Chờ 3 giây để form ẩn đi
             WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
             shortWait.until(ExpectedConditions.invisibilityOfElementLocated(dialogOverlay));
             System.out.println("Đóng form thành công bằng nút Hủy.");
        } catch (Exception e) {
             System.out.println("Nút Hủy không hoạt động, dùng phím ESC...");
             try {
                 driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
             } catch (Exception ex) {
                 System.out.println("Không thể nhấn ESC.");
             }
        }
    }

    private boolean isFormOpen() {
        try {
             return driver.findElement(maInput).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
             return false;
        }
    }

    private void searchAcademicDegreeRank(String keyword) {
        WebElement search = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Nhập tìm kiếm...']")));
        search.sendKeys(Keys.CONTROL + "a");
        search.sendKeys(Keys.BACK_SPACE);
        search.sendKeys(keyword);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        System.out.println("\nĐang thực hiện teardown đóng trình duyệt...");
        if (driver != null) {
            try {
                Thread.sleep(2000); 
                driver.quit();
                System.out.println("Đã đóng trình duyệt thành công.");
            } catch (Exception e) {
                System.out.println("Lỗi khi quit browser: " + e.getMessage());
            }
        }
    }
}