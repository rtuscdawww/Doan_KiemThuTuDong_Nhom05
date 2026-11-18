
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
import org.testng.annotations.Test;

public class ViewPersonalHoursByLessonYearTest {

    @Test
    public void viewPersonalHoursByLessonYear() {

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
                    By.xpath(
                        "//button[@type='submit']"
                        + " | //a[contains(.,'Đăng nhập')]"
                    )
                )).click();
            }

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Thống kê']")
            )).click();

            System.out.println("Đã click Thống kê.");

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[normalize-space(text())='Số giờ cá nhân']")
            )).click();

            wait.until(
                ExpectedConditions.urlContains("/Statistics/Personal")
            );

            System.out.println("Đã mở Số giờ cá nhân.");

            By dropdownLocator = By.cssSelector(
                "select, [role='combobox'], div.multiselect"
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
                        + " | //span[contains(@class,'multiselect__option')]"
                        + "[normalize-space(.)='Năm học']"
                    )
                )).click();
            }

            System.out.println("Đã chọn Thống kê theo Năm học.");

            By namHocLocator = By.xpath(
                "//*[normalize-space(text())='Năm học:']"
                + "/following::select[1]"
                + " | //*[normalize-space(text())='Năm học:']"
                + "/following::*[@role='combobox'][1]"
                + " | //*[normalize-space(text())='Năm học:']"
                + "/following::div[contains(concat(' ',normalize-space(@class),' '),' multiselect ')][1]"
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
                new Select(namHoc).selectByVisibleText("2026 - 2027");
            } else {
                js.executeScript(
                    "arguments[0].scrollIntoView({block:'center'});",
                    namHoc
                );

                namHoc.click();

                By optionLocator = By.xpath(
                    "//li[normalize-space(.)='2026 - 2027']"
                    + " | //*[@role='option'][normalize-space(.)='2026 - 2027']"
                    + " | //li//span[normalize-space(.)='2026 - 2027']"
                    + " | //div[contains(@class,'multiselect__option')]"
                    + "[normalize-space(.)='2026 - 2027']"
                    + " | //span[contains(@class,'multiselect__option')]"
                    + "[normalize-space(.)='2026 - 2027']"
                );

                WebElement option2026 = wait.until(
                    ExpectedConditions.elementToBeClickable(optionLocator)
                );

                option2026.click();
            }

            wait.until(d -> {
                WebElement selected = d.findElement(namHocLocator);

                if (selected.getTagName().equalsIgnoreCase("select")) {
                    return new Select(selected)
                        .getFirstSelectedOption()
                        .getText()
                        .replaceAll("\\s+", "")
                        .equals("2026-2027");
                }

                return selected.getText()
                    .replaceAll("\\s+", "")
                    .contains("2026-2027");
            });

            System.out.println("Đã chọn Năm học 2026 - 2027.");

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

                if (id == null || id.isEmpty()) {
                    throw new RuntimeException(
                        "Không tìm thấy checkbox Xem theo ca giảng"
                    );
                }

                checkbox = driver.findElement(By.id(id));
            }

            if (!checkbox.isSelected()) {
                js.executeScript(
                    "arguments[0].click();",
                    label
                );
            }

            if (!checkbox.isSelected()) {
                js.executeScript(
                    "arguments[0].click();",
                    checkbox
                );
            }

            wait.until(d -> checkbox.isSelected());

            System.out.println("Đã tick Xem theo ca giảng.");

            By resultLocator = By.xpath(
                "//table[.//tbody/tr]"
                + " | //canvas"
                + " | //*[contains(@class,'apexcharts-canvas')]"
                + " | //*[contains(@class,'echarts')]"
                + " | //*[contains(.,'Chưa có dữ liệu') and not(*)]"
                + " | //*[contains(.,'Không có dữ liệu') and not(*)]"
            );

            wait.until(
                ExpectedConditions.visibilityOfElementLocated(resultLocator)
            );

            System.out.println(
                "Đã hiển thị thống kê năm học 2026 - 2027."
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực hiện kiểm thử", e);

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
