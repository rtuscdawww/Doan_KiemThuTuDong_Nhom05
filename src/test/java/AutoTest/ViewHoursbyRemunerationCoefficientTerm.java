
package AutoTest;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ViewHoursbyRemunerationCoefficientTerm {

    @Test
    public void viewHoursbyRemunerationCoefficientTerm() {

        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("user-data-dir=C:\\ChromeSeleniumProfile");

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(30)
            );

            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get(
                "https://cntttest.vanlanguni.edu.vn:18081/Phancong02/"
            );

            if (driver.getCurrentUrl().contains("/Account/Login")) {
                WebElement login = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath(
                            "//button[@type='submit']"
                            + " | //a[normalize-space()='Đăng nhập']"
                        )
                    )
                );
                login.click();
            }

            WebElement thongKe = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space(text())='Thống kê']")
                )
            );
            thongKe.click();
            System.out.println("Đã click Thống kê.");

            WebElement soGio = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space(text())='Số giờ giảng viên']")
                )
            );
            soGio.click();

            wait.until(ExpectedConditions.urlContains("/Statistics"));
            System.out.println("Đã mở Số giờ giảng viên.");

            WebElement thongKeTheo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "(//select | //*[@role='combobox']"
                        + " | //div[contains(@class,'multiselect')])[1]"
                    )
                )
            );

            if (thongKeTheo.getTagName().equalsIgnoreCase("select")) {
                new Select(thongKeTheo).selectByVisibleText("Học kỳ");
            } else {
                thongKeTheo.click();

                wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath(
                            "//*[normalize-space(text())='Học kỳ'"
                            + " and (self::li or self::span"
                            + " or @role='option')]"
                        )
                    )
                ).click();
            }

            System.out.println("Đã chọn Thống kê theo Học kỳ.");

            WebElement hocKy = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "(//select | //*[@role='combobox']"
                        + " | //div[contains(@class,'multiselect')])[2]"
                    )
                )
            );

            if (hocKy.getTagName().equalsIgnoreCase("select")) {
                new Select(hocKy).selectByVisibleText("999");
            } else {
                hocKy.click();

                wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath(
                            "//*[normalize-space(text())='999'"
                            + " and (self::li or self::span"
                            + " or @role='option')]"
                        )
                    )
                ).click();
            }

            System.out.println("Đã chọn Học kỳ 999.");

            WebElement label = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "//label[contains(@class,'form-check-label')"
                        + " and contains(.,'Xem theo ca giảng')]"
                    )
                )
            );

            WebElement checkbox = label.findElement(
                By.xpath(
                    "./preceding-sibling::input[@type='checkbox']"
                    + " | ./parent::*//input[@type='checkbox']"
                    + " | //*[@id='" + label.getAttribute("for") + "']"
                )
            );

            if (!checkbox.isSelected()) {
                js.executeScript("arguments[0].click();", checkbox);
            }

            wait.until(d -> checkbox.isSelected());

            Assert.assertTrue(
                checkbox.isSelected(),
                "Không tick được Xem theo ca giảng!"
            );

            System.out.println("Đã tick Xem theo ca giảng.");

            WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "//table[.//tbody/tr] | //canvas"
                        + " | //*[contains(@class,'apexcharts-canvas')]"
                        + " | //*[contains(@class,'echarts')]"
                    )
                )
            );

            Assert.assertTrue(
                result.isDisplayed(),
                "Không hiển thị kết quả thống kê!"
            );

            System.out.println("PASS: Đã hiển thị thống kê.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("FAIL: " + e.getMessage(), e);

        } finally {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (driver != null) {
                driver.quit();
            }
        }
    }
}
