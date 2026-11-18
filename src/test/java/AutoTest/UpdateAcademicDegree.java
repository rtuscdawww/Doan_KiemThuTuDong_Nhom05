
package AutoTest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class UpdateAcademicDegree {

    private WebDriver driver;
    private WebDriverWait wait;

    private String ma;
    private String ten;
    private String tenMoi;

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
    public void setup() throws InterruptedException {

        long timeStamp = System.currentTimeMillis();

        ma = "HV_" + timeStamp;
        ten = "Tiến sĩ CNTT " + timeStamp;
        tenMoi = "Tiến sĩ Khoa học máy tính " + timeStamp;

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

        createTestData();
    }

    private void createTestData() throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[self::button or self::a][contains(.,'Thêm HH, HV mới')]")
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            maInput
        )).sendKeys(ma);

        driver.findElement(tenInput).sendKeys(ten);

        System.out.println("Da tao du lieu kiem thu: " + ma);
        System.out.println("Ten ban dau: " + ten);

        Thread.sleep(5000);

        clickSave();

        Thread.sleep(5000);

        clickOKIfPresent();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            tenInput
        ));

        searchAcademicDegree(ma);

        verifyName(ten);

        System.out.println("Da tao thanh cong du lieu kiem thu.");
    }

    @Test(priority = 1)
    public void updateValidAcademicDegree() throws InterruptedException {

        openEditForm();

        inputName(tenMoi);

        System.out.println("Da nhap ten moi: " + tenMoi);

        Thread.sleep(5000);

        clickSave();

        Thread.sleep(5000);

        clickOKIfPresent();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            tenInput
        ));

        searchAcademicDegree(ma);

        verifyName(tenMoi);

        System.out.println("TC_RM_03-1 Passed.");

        Thread.sleep(5000);
    }

    @Test(priority = 2, dependsOnMethods = "updateValidAcademicDegree")
    public void updateEmptyAcademicDegree() throws InterruptedException {

        openEditForm();

        inputName("");

        System.out.println("Da bo trong ten hoc vi.");

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
            "He thong cho phep cap nhat ten hoc vi trong."
        );

        searchAcademicDegree(ma);

        verifyName(tenMoi);

        System.out.println("TC_RM_03-2 Passed.");

        Thread.sleep(5000);
    }

    @Test(priority = 3, dependsOnMethods = "updateValidAcademicDegree")
    public void cancelUpdateAcademicDegree() throws InterruptedException {

        openEditForm();

        inputName("Hoc vi khong luu");

        System.out.println("Da nhap ten hoc vi khong luu.");

        Thread.sleep(5000);

        closeForm();

        searchAcademicDegree(ma);

        verifyName(tenMoi);

        System.out.println("Du lieu hoc vi khong thay doi.");
        System.out.println("TC_RM_03-3 Passed.");

        Thread.sleep(5000);
    }

    private void openEditForm() {

        By editButton = By.xpath(
            "//table/tbody/tr[td[normalize-space()='" + ma + "']]" +
            "//*[self::a or self::button][" +
            "contains(@title,'Sửa') or " +
            "contains(@class,'edit') or " +
            ".//*[contains(@class,'edit') or " +
            "contains(@data-feather,'edit')]]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(
            editButton
        )).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            tenInput
        ));

        System.out.println("Da mo form cap nhat HH, HV.");
    }

    private void inputName(String name) {

        WebElement input = wait.until(
            ExpectedConditions.elementToBeClickable(tenInput)
        );

        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.BACK_SPACE);

        if (!name.isEmpty()) {
            input.sendKeys(name);
        }
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
            tenInput
        ));

        System.out.println("Da nhan nut Huy.");
    }

    private boolean isFormOpen() {

        return driver.findElements(tenInput)
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

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//table/tbody/tr[td[normalize-space()='" + ma + "']]")
        ));
    }

    private void verifyName(String expectedName) {

        boolean found = wait.until(d ->
            d.findElements(
                By.xpath("//table/tbody/tr[td[normalize-space()='" + ma + "']]")
            ).stream().anyMatch(row ->
                row.isDisplayed() &&
                row.findElements(By.tagName("td"))
                    .stream().anyMatch(td ->
                        td.getText().trim().equals(expectedName)
                    )
            )
        );

        Assert.assertTrue(
            found,
            "Ten hoc vi khong dung voi ket qua mong doi."
        );

        System.out.println("Ten hoc vi hien tai: " + expectedName);
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
