
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

public class ViewListofCoefficients {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setupAndNavigate() {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        driver.manage().window().maximize();
        driver.get("https://cntttest.vanlanguni.edu.vn:18081/Phancong02/");

        // 1. Tu dong dang nhap
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

        // 2. Mo menu Thu lao
        WebElement thuLaoMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[@id='sidebarnav']//li/a[contains(@href, '#') and .//i[contains(@class, 'money') or contains(@class, 'dollar') or contains(@class, 'cash')]] | //span[contains(., 'Th') and contains(., 'lao')]")
            )
        );

        clickElement(thuLaoMenu);
        System.out.println("Da mo menu Thu lao.");

        // 3. Mo menu Don gia & he so
        WebElement priceCoefficientMenu = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'/PriceCoefficient')]")
            )
        );

        clickElement(priceCoefficientMenu);

        wait.until(
            ExpectedConditions.urlContains("/PriceCoefficient")
        );

        System.out.println("Da mo menu Don gia & he so.");
    }

    @Test
    public void viewCoefficients() throws InterruptedException {

        // 4. Chon nam hoc
        WebElement dropdown = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector(".select2-selection--single")
            )
        );

        clickElement(dropdown);

        WebElement year = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'select2-results__option') and contains(.,'2033') and contains(.,'2034')]")
            )
        );

        clickElement(year);

        wait.until(d -> d.findElement(
            By.cssSelector(".select2-selection--single")
        ).getText().contains("2033"));

        System.out.println("Da chon nam hoc 2033 - 2034.");

        // 5. Chon tab He so
        WebElement tab = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[normalize-space()='Hệ số']")
            )
        );

        clickElement(tab);

        wait.until(d -> tab.getAttribute("class").contains("active"));

        System.out.println("Da nhan tab He so.");

        // 6. Kiem tra bang he so
        String target = tab.getAttribute("href");
        String panelId = target.substring(target.lastIndexOf("#") + 1);

        WebElement panel = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.id(panelId)
            )
        );

        List<WebElement> cells = wait.until(d -> {

            List<WebElement> data = panel.findElements(
                By.cssSelector("table tbody tr:first-child td")
            );

            if (data.size() < 4) {
                return null;
            }

            for (int i = 0; i < 4; i++) {
                if (!data.get(i).isDisplayed()
                    || data.get(i).getText().trim().isEmpty()) {
                    return null;
                }
            }

            return data;
        });

        String vietnamese = cells.get(0).getText().trim();
        String english = cells.get(1).getText().trim();
        String theory = cells.get(2).getText().trim();
        String practice = cells.get(3).getText().trim();

        // Kiem tra gia tri he so
        Assert.assertEquals(vietnamese, "1.20");
        Assert.assertEquals(english, "1.00");
        Assert.assertEquals(theory, "1.00");
        Assert.assertEquals(practice, "1.00");

        System.out.println("He so tieng Viet: " + vietnamese);
        System.out.println("He so tieng Anh: " + english);
        System.out.println("He so ly thuyet: " + theory);
        System.out.println("He so thuc hanh: " + practice);

        System.out.println("TC_RM_14_01 Passed");

        Thread.sleep(5000);
    }

    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }

    @AfterClass(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
