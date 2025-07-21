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
        
        String currentStep = "";

        for (int i = 335; i <= rowCount; i++) {
            try {
            	
            	currentStep = "Reading Batch No from Excel";
                String batchNo = sheet.getRow(i).getCell(0).getRawValue();

                // Enter Batch No From
                currentStep = "Entering Batch No From For Batch Report";
                WebElement BatchFrom = wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom"));
                BatchFrom.clear();
                BatchFrom.sendKeys(batchNo);

                // Enter Batch No To
                currentStep = "Entering Batch No To For Batch Report";
                WebElement BatchTo = wd.findElement(By.id("ctl00_ctpContent_txtBatchTo"));
                BatchTo.clear();
                BatchTo.sendKeys(batchNo);

                // Click Search
                currentStep = "Clicking Search Button for Batch Report";
                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(3000);

                // Click Print Batch Report
                currentStep = "Clicking Print Batch Report";
                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgPrint")).click();
                Thread.sleep(4000);
                wd.executeScript("window.print();");
                Thread.sleep(3000);
                wd.navigate().back();
                Thread.sleep(2000);

                // Repeat Search for Challan
                currentStep = "Entering Batch No From For Challan";
                WebElement BatchFrom_CH = wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom"));
                BatchFrom_CH.clear();
                BatchFrom_CH.sendKeys(batchNo);

                currentStep = "Entering Batch No To For Challan";
                WebElement BatchTo_CH = wd.findElement(By.id("ctl00_ctpContent_txtBatchTo"));
                BatchTo_CH.clear();
                BatchTo_CH.sendKeys(batchNo);

                currentStep = "Clicking Search Button for Challan";
                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(2000);

                // Click on Challan icon
                currentStep = "Clicking Challan Icon for Printing";
                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1")).click();
                Thread.sleep(3000);

                try 
                {
                	currentStep = "Click To Save Challan";
                    wd.findElement(By.id("ctl00_ctpContent_btnSave")).click();
                    Thread.sleep(2000);
                    currentStep = "Click To Print Challan";
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                } catch (Exception e) 
                {
                	currentStep = "Save button not found, trying to print directly";
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                }

                Thread.sleep(4000);
                currentStep = "Printing Challan";
                wd.executeScript("window.print();");
                Thread.sleep(3000);

                currentStep = "Back to Batch List First";
                wd.navigate().back();
                Thread.sleep(2000);
                currentStep = "Back to Batch List Second";
                wd.navigate().back();
                Thread.sleep(2000);

                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("PASS");
                FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
                wb.write(outputStream);

            } catch (Exception e) 
            {
            	currentStep = "Back to Batch List Second";
            	System.out.println(i+ " : " + currentStep);
                e.printStackTrace();
                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("Fail:-"+ currentStep);
                FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
                wb.write(outputStream);

                try {
                    WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
                    Batch.click();
                    Thread.sleep(4000);
                    wd.findElement(By.linkText("Batch List")).click();
                } catch (Exception ex) {
                    
                }
                continue;
            }
        }

        System.out.println("Job Done");
        wd.quit();
    }
}

