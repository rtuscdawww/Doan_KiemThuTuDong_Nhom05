
package AutoTest;

import java.time.Duration;
import java.util.List;

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

public class ViewHoursbyRemunerationCoefficientYear {

    @Test
    public void viewHoursbyRemunerationCoefficientYear() {

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
                wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@type='submit'] | //a[contains(.,'Đăng nhập')]")
                )).click();
            }

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Thống kê']")
            )).click();

            System.out.println("Đã click Thống kê.");

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Số giờ giảng viên']")
            )).click();

            wait.until(ExpectedConditions.urlContains("/Statistics"));

            System.out.println("Đã mở Số giờ giảng viên.");

            By dropdownLocator = By.cssSelector(
                "select, [role='combobox'], .multiselect"
            );

            WebElement thongKeTheo = wait.until(d -> {
                for (WebElement e : d.findElements(dropdownLocator)) {
                    if (e.isDisplayed()) {
                        return e;
                    }
                }
                return null;
            });

            if (thongKeTheo.getTagName().equalsIgnoreCase("select")) {
                new Select(thongKeTheo).selectByVisibleText("Năm học");
            } else {
                thongKeTheo.click();

                wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//li[normalize-space(.)='Năm học']"
                        + " | //*[@role='option'][normalize-space(.)='Năm học']"
                        + " | //li//span[normalize-space(.)='Năm học']"
                    )
                )).click();
            }

            System.out.println("Đã chọn Thống kê theo: Năm học.");

            By namHocLocator = By.xpath(
                "//*[normalize-space(text())='Năm học:']"
                + "/following::select[1]"
                + " | //*[normalize-space(text())='Năm học:']"
                + "/following::*[@role='combobox'][1]"
                + " | //*[normalize-space(text())='Năm học:']"
                + "/following::*[contains(@class,'multiselect')][1]"
            );

            WebElement namHoc = wait.until(d -> {
                for (WebElement e : d.findElements(namHocLocator)) {
                    if (e.isDisplayed()) {
                        return e;
                    }
                }
                return null;
            });

            if (namHoc.getTagName().equalsIgnoreCase("select")) {
                new Select(namHoc).selectByVisibleText("2025 - 2026");
            } else {
                js.executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    namHoc
                );

                namHoc.click();

                By optionLocator = By.xpath(
                    "//li[normalize-space(.)='2025 - 2026']"
                    + " | //*[@role='option'][normalize-space(.)='2025 - 2026']"
                    + " | //li//span[normalize-space(.)='2025 - 2026']"
                    + " | //div[contains(@class,'multiselect__option')]"
                    + "[normalize-space(.)='2025 - 2026']"
                );

                WebElement option2025 = wait.until(
                    ExpectedConditions.elementToBeClickable(optionLocator)
                );

                option2025.click();
            }

            wait.until(d -> {
                WebElement selected = d.findElement(namHocLocator);

                if (selected.getTagName().equalsIgnoreCase("select")) {
                    return new Select(selected)
                        .getFirstSelectedOption()
                        .getText()
                        .replaceAll("\\s+", "")
                        .equals("2025-2026");
                }

                return selected.getText()
                    .replaceAll("\\s+", "")
                    .contains("2025-2026");
            });

            System.out.println("PASS: Đã chọn năm học 2025 - 2026.");

            WebElement label = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "//label[contains(@class,'form-check-label')"
                        + " and contains(.,'Xem theo ca giảng')]"
                    )
                )
            );

            List<WebElement> inputs = label.findElements(
                By.xpath(
                    "./preceding-sibling::input[@type='checkbox']"
                    + " | ./parent::*//input[@type='checkbox']"
                )
            );

            WebElement checkbox;

            if (!inputs.isEmpty()) {
                checkbox = inputs.get(0);
            } else {
                String id = label.getAttribute("for");

                Assert.assertTrue(
                    id != null && !id.isEmpty(),
                    "Không tìm thấy checkbox Xem theo ca giảng!"
                );

                checkbox = driver.findElement(By.id(id));
            }

            if (!checkbox.isSelected()) {
                js.executeScript("arguments[0].click();", label);
            }

            if (!checkbox.isSelected()) {
                js.executeScript("arguments[0].click();", checkbox);
            }

            wait.until(d -> checkbox.isSelected());

            Assert.assertTrue(
                checkbox.isSelected(),
                "Không tick được Xem theo ca giảng!"
            );

            System.out.println("PASS: Đã tick Xem theo ca giảng.");

            By resultLocator = By.xpath(
                "//table[.//tbody/tr]"
                + " | //canvas"
                + " | //*[contains(@class,'apexcharts-canvas')]"
                + " | //*[contains(@class,'echarts')]"
                + " | //*[contains(.,'Chưa có dữ liệu') and not(*)]"
            );

            WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(resultLocator)
            );

            Assert.assertTrue(
                result.isDisplayed(),
                "Không hiển thị kết quả thống kê!"
            );

            System.out.println(
                "PASS: Đã hiển thị kết quả thống kê năm học 2025 - 2026."
            );

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

            System.out.println("Đã đóng trình duyệt.");
        }
    }
}
