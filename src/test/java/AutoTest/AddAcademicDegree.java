package AutoTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class AddAcademicDegree {

    private WebDriver driver;
    private WebDriverWait wait;

    private String ma;
    private String ten;

    private final By maInput = By.xpath(
        "//input[@placeholder='Nhập mã học hàm, học vị']"
    );

    private final By tenInput = By.xpath(
        "//input[@placeholder='Nhập tên học hàm, học vị']"
    );

    private final By okButton = By.xpath(
        "//button[normalize-space()='OK']"
    );

    @BeforeClass
    public void setup() {
        // TẠO DỮ LIỆU ĐỘNG DỰA TRÊN TIMESTAMP ĐỂ KHÔNG BAO GIỜ BỊ TRÙNG LẶP GIỮA CÁC LẦN RUN
        long timeStamp = System.currentTimeMillis();
        ma = "HV_" + timeStamp;
        ten = "Tiến sĩ CNTT " + timeStamp;

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        if (driver.getCurrentUrl().contains("/Account/Login")) {

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space()='Đăng nhập']")
            )).click();

            wait.until(d ->
                !d.getCurrentUrl().contains("/Account/Login")
            );

            System.out.println("Dang nhap thanh cong.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[normalize-space()='Thù lao']")
        )).click();

        System.out.println("Da mo menu Thu lao.");

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'/AcademicDegree')]")
        )).click();

        wait.until(ExpectedConditions.urlContains("/AcademicDegree"));

        System.out.println("Da mo trang Hoc ham, hoc vi.");
    }

    @Test(priority = 1)
    public void addValidAcademicDegree() throws InterruptedException {

        openForm();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            maInput
        )).sendKeys(ma);

        driver.findElement(tenInput).sendKeys(ten);

        System.out.println("Da nhap ma: " + ma);
        System.out.println("Da nhap ten: " + ten);

        Thread.sleep(5000);

        clickSave();

        Thread.sleep(5000);

        clickOKIfPresent();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            maInput
        ));

        searchAcademicDegree(ma);

        boolean found = wait.until(d ->
            d.findElements(By.cssSelector("table tbody tr"))
                .stream().anyMatch(row ->
                    row.isDisplayed() &&
                    row.getText().contains(ma) &&
                    row.getText().contains(ten)
                )
        );

        Assert.assertTrue(
            found,
            "Them hoc vi khong thanh cong."
        );

        System.out.println("TC_RM_02-1 Passed.");

        Thread.sleep(5000);
    }

    @Test(priority = 2, dependsOnMethods = "addValidAcademicDegree")
    public void addDuplicateAcademicDegree() throws InterruptedException {

        openForm();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            maInput
        )).sendKeys(ma);

        driver.findElement(tenInput).sendKeys(ten);

        System.out.println("Da nhap hoc vi trung ma: " + ma);

        Thread.sleep(5000);

        clickSave();

        WebElement message = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space()='Mã học hàm, học vị này đã tồn tại!']")
            )
        );

        String actualMessage = message.getText().trim();

        System.out.println("Thong bao: " + actualMessage);

        Thread.sleep(5000);

        wait.until(ExpectedConditions.elementToBeClickable(
            okButton
        )).click();

        System.out.println("Da nhan nut OK.");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            okButton
        ));

        if (isFormOpen()) {
            closeForm();
        }

        searchAcademicDegree(ma);

        Thread.sleep(5000);

        long count = driver.findElements(
            By.xpath("//table/tbody/tr[td[normalize-space()='" + ma + "']]")
        ).stream().filter(WebElement::isDisplayed).count();

        Assert.assertEquals(
            actualMessage,
            "Mã học hàm, học vị này đã tồn tại!"
        );

        Assert.assertEquals(
            count,
            1L,
            "He thong cho phep them hoc vi trung ma."
        );

        System.out.println("TC_RM_02-2 Passed.");

        Thread.sleep(5000);
    }

    @Test(priority = 3)
    public void addEmptyAcademicDegree() throws InterruptedException {

        openForm();

        System.out.println("Bo trong ma va ten hoc vi.");

        Thread.sleep(5000);

        clickSave();

        Thread.sleep(5000);

        clickOKIfPresent();

        boolean formOpen = isFormOpen();

        if (formOpen) {
            closeForm();
        }

        Assert.assertTrue(
            formOpen,
            "He thong cho phep luu thong tin trong."
        );

        System.out.println("TC_RM_02-3 Passed.");

        Thread.sleep(5000);
    }

    private void openForm() {

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[self::button or self::a][contains(.,'Thêm HH, HV mới')]")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            maInput
        ));

        System.out.println("Da mo form them HH, HV.");
    }

    private void clickSave() {

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='Lưu']")
        )).click();

        System.out.println("Da nhan nut Luu.");
    }

    private void clickOKIfPresent() {

        try {

            WebDriverWait shortWait = new WebDriverWait(
                driver, Duration.ofSeconds(5)
            );

            shortWait.until(
                ExpectedConditions.elementToBeClickable(okButton)
            ).click();

            wait.until(
                ExpectedConditions.invisibilityOfElementLocated(okButton)
            );

            System.out.println("Da nhan nut OK.");

        } catch (TimeoutException e) {
            System.out.println("Khong co thong bao OK.");
        }
    }

    private void closeForm() {

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[normalize-space()='Huỷ' or normalize-space()='Hủy']")
        )).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            maInput
        ));

        System.out.println("Da nhan nut Huy.");
    }

    private boolean isFormOpen() {

        return driver.findElements(maInput)
            .stream().anyMatch(WebElement::isDisplayed);
    }

    private void searchAcademicDegree(String keyword) {

        WebElement search = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Nhập tìm kiếm...']")
            )
        );

        search.sendKeys(Keys.CONTROL + "a");
        search.sendKeys(Keys.BACK_SPACE);
        search.sendKeys(keyword);

        System.out.println("Tim kiem: " + keyword);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}