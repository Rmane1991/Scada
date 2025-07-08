package Saba;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Print_Ch_Batch extends Utility {

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        FileInputStream fis = new FileInputStream("D:\\ME_Data\\Excel\\Book.xlsx");
        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;

        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--kiosk-printing"); // Silent printing to default printer
        ChromeDriver wd = new ChromeDriver(options);
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        XSSFCell cell = null;
        login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");

        Thread.sleep(2000);
        wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
        Thread.sleep(1000);
        wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
        Thread.sleep(1000);

        System.out.println("No of Record Found Into Excel :- " + rowCount);

        for (int i = 1; i <= rowCount; i++) {
            try {
                String batchNo = sheet.getRow(i).getCell(0).getRawValue();

                // Enter Batch No From
                WebElement BatchFrom = wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom"));
                BatchFrom.clear();
                BatchFrom.sendKeys(batchNo);

                // Enter Batch No To
                WebElement BatchTo = wd.findElement(By.id("ctl00_ctpContent_txtBatchTo"));
                BatchTo.clear();
                BatchTo.sendKeys(batchNo);

                // Click Search
                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(4000);

                // Click Print Batch Report
                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgPrint")).click();
                Thread.sleep(3000);
                wd.executeScript("window.print();");
                Thread.sleep(4000);
                wd.navigate().back();
                Thread.sleep(2000);

                // Repeat Search for Challan
                WebElement BatchFrom_CH = wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom"));
                BatchFrom_CH.clear();
                BatchFrom_CH.sendKeys(batchNo);

                WebElement BatchTo_CH = wd.findElement(By.id("ctl00_ctpContent_txtBatchTo"));
                BatchTo_CH.clear();
                BatchTo_CH.sendKeys(batchNo);

                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(2000);

                // Click on Challan icon
                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1")).click();
                Thread.sleep(3000);

                try {
                    // Save Challan if Save button is available
                    wd.findElement(By.id("ctl00_ctpContent_btnSave")).click();
                    Thread.sleep(2000);
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                } catch (Exception e) {
                    // If Save not needed, click only Print
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                }

                Thread.sleep(3000);
                wd.executeScript("window.print();");
                Thread.sleep(10000);

                wd.navigate().back();
                Thread.sleep(2000);
                wd.navigate().back();
                Thread.sleep(2000);

                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("PASS");
                FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
                wb.write(outputStream);

            } catch (Exception e) {
                e.printStackTrace();
                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("Fail");
                FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_02.xlsx");
                wb.write(outputStream);

                try {
                    WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
                    Batch.click();
                    Thread.sleep(4000);
                    wd.findElement(By.linkText("Batch List")).click();
                } catch (Exception ex) {
                    // silently ignore and continue
                }
                continue;
            }
        }

        System.out.println("Job Done");
        wd.quit();
    }
}

