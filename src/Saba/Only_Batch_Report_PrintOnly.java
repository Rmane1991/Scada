package Saba;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Only_Batch_Report_PrintOnly extends Utility {

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {

        String currentStep = "";
        FileInputStream fis = new FileInputStream("D:\\ME_Data\\Excel\\Book.xlsx");

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;

        // Setup WebDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        ChromeDriver wd = new ChromeDriver(options);
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");

        Thread.sleep(2000);
        wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
        Thread.sleep(1000);
        wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
        Thread.sleep(1000);

        // First batch number from Excel
        String firstBatchNo = sheet.getRow(1).getCell(0).getRawValue();

        WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
        BatchFrom.clear();
        BatchFrom.sendKeys(firstBatchNo);

        WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
        BatchTo.clear();
        BatchTo.sendKeys(firstBatchNo);

        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
        Thread.sleep(4000);

        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
        Thread.sleep(3000);

        ((JavascriptExecutor) wd).executeScript("window.print();");
        Thread.sleep(3000);

        System.out.println("No of Record Found Into Excel :- " + rowCount);

        for (int i = 335; i <= 336; i++) 
        {
            try {
                String batchNoLoop = sheet.getRow(i).getCell(0).getRawValue();
                String reportUrl = "http://www.bmc-scada.online/app/Reports/SkylandReportViewerNew.aspx?btno=" + batchNoLoop;

                wd.get("about:blank");
                Thread.sleep(3000);

                ((JavascriptExecutor) wd).executeScript("window.location.href = arguments[0];", reportUrl);
                Thread.sleep(4000);

                ((JavascriptExecutor) wd).executeScript("window.print();");
                Thread.sleep(3000);

                System.out.println("✅ Print triggered for batch: " + batchNoLoop);

            } catch (Exception e) {
                System.out.println("❌ Error at step: " + currentStep + " - " + e.getMessage());
            }
        }

        wd.quit();
        wb.close();
    }
}

