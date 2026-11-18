
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

public class ViewListofPrice {

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

            clickElement(btnDangNhap);

            wait.until(d -> !d.getCurrentUrl().contains("/Account/Login"));

            System.out.println("Dang nhap thanh cong.");
        }

        // 2. Mở menu Thù lao
        WebElement thuLaoMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='sidebarnav']//li/a[contains(@href, '#') and .//i[contains(@class, 'money') or contains(@class, 'dollar') or contains(@class, 'cash')]] | //span[contains(., 'Th') and contains(., 'lao')]")
            )
        );

        clickElement(thuLaoMenu);
        System.out.println("Da nhan menu Thu lao.");

        // 3. Mở menu Đơn giá & hệ số
        WebElement priceCoefficientMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/PriceCoefficient')]")
            )
        );

        clickElement(priceCoefficientMenu);

        wait.until(ExpectedConditions.urlContains("/PriceCoefficient"));

        System.out.println("Da mo menu Don gia & he so.");

        // 4. Chọn năm học 2033 - 2034
        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'select2-selection--single')]")
            )
        );

        clickElement(dropdown);

        WebElement year = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'select2-results__option') and contains(.,'2033') and contains(.,'2034')]")
            )
        );

        clickElement(year);

        System.out.println("Da chon nam hoc 2033 - 2034.");

        // 5. Mở tab Đơn giá
        WebElement tabDonGia = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[normalize-space()='Đơn giá']")
            )
        );

        clickElement(tabDonGia);
    }
    @Test(priority = 1)
public void viewStandardProgramPrice() throws InterruptedException {

    WebElement tab = wait.until(
        ExpectedConditions.elementToBeClickable(
            By.linkText("CTĐT tiêu chuẩn")
        )
    );

    clickElement(tab);
    verifyPriceTable(tab);

    System.out.println("TC_RM_11_01 Passed");
    Thread.sleep(1000);
}

@Test(priority = 2)
public void viewSpecialProgramPrice() throws InterruptedException {

    WebElement tab = wait.until(
        ExpectedConditions.elementToBeClickable(
            By.id("special-tab")
        )
    );

    clickElement(tab);
    verifyPriceTable(tab);

    System.out.println("TC_RM_11_02 Passed");
    Thread.sleep(1000);
}

@Test(priority = 3)
public void viewForeignLecturerPrice() throws InterruptedException {

    WebElement tab = wait.until(
        ExpectedConditions.elementToBeClickable(
            By.id("foreign-tab")
        )
    );
    clickElement(tab);
    verifyPriceTable(tab);
     System.out.println("TC_RM_11_03 Passed");
    Thread.sleep(3000);
}

    private void verifyPriceTable(WebElement tab) {

    wait.until(d -> tab.getAttribute("class").contains("active"));

    String target = tab.getAttribute("href");
    String panelId = target.substring(target.lastIndexOf("#") + 1);

    WebElement panel = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.id(panelId)
        )
    );

    List<WebElement> rows = wait.until(d -> {

        List<WebElement> data = panel.findElements(
            By.cssSelector("table tbody tr")
        );

        boolean hasData = data.stream().anyMatch(
            row -> row.isDisplayed()
                && row.findElements(By.tagName("td")).size() >= 2
        );

        return hasData ? data : null;
    });

    Assert.assertFalse(rows.isEmpty(),
        "Bang don gia khong co du lieu");

    System.out.println("So dong du lieu: " + rows.size());
}
    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }

    @AfterClass
    public void teardown() throws InterruptedException {

        if (driver != null) {
            Thread.sleep(5000);
            driver.quit();
        }
    }
}
