package Saba;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class SavePDF extends Utility {

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        FileInputStream fis = new FileInputStream("E:\\Manaci_Vijay\\Flex_1004_To_1564.xlsx");

        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet2");
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;

        // Setup WebDriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--kiosk-printing");

        // Set Chrome preferences for silent printing and file saving
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("printing.print_preview_sticky_settings.appState",
            "{\"recentDestinations\": [{\"id\": \"Save as PDF\", \"origin\": \"local\", \"account\": \"\"}],"
            + "\"selectedDestinationId\": \"Save as PDF\","
            + "\"version\": 2}");
        prefs.put("savefile.default_directory", "E:\\Eclipse_Excel\\C273_Second"); // Set download location
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", "E:\\Eclipse_Excel\\C273_Second");
        options.setExperimentalOption("prefs", prefs);

        ChromeDriver wd = new ChromeDriver(options);
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

       // Utility selUtil = new Utility();
        XSSFCell cell = null;
        login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");
        
    	
		//Actions a = new Actions(wd);
		Thread.sleep(2000);

		// Clickbatch
		wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
		Thread.sleep(1000);

		wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
		Thread.sleep(1000);

		// For read and write data from excel

		System.out.println("No of Record Found Into Excel :- " + rowCount);


        for (int i =1; i <=rowCount; i++) {
            try {
                // Enter Batch No From
                WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
                BatchFrom.clear();
                BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

                // Enter Batch No To
                WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
                BatchTo.clear();
                BatchTo.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

                // Click Search
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
                Thread.sleep(4000);

                /*
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
				if (selUtil.isDisaplyed(By.xpath("//input[@id='ctl00_ctpContent_Button1']"), wd, 5000) == true);

				// Click Print Button to print Batch Report
				
				WebElement btnprint = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_Button1']"));
				Thread.sleep(1000);
				a.moveToElement(btnprint).release().perform();
				Thread.sleep(1000);
				btnprint.click();
				//Thread.sleep(3000);
				
				// Execute the print command silently
               // wd.executeScript("window.print();");
                //Thread.sleep(10000);  // Wait for print to complete
				
				
				//a.sendKeys(Keys.ENTER).build().perform();

				// Close Batch Report
				
				Thread.sleep(7000);
				wd.findElement(By.xpath("//div[@id='me']//img[@id='Img121']")).click();
				Thread.sleep(2000);
				//wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				//Thread.sleep(4000);
                */
                
				
                // Click On Challan
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();

                
                // Click Save Button for Challan
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
                Thread.sleep(2000);

                // Click Print Button for Challan
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
                Thread.sleep(3000);
                // Execute the print command silently
                wd.executeScript("window.print();");
                Thread.sleep(10000);  // Wait for print to complete

                // Navigate back after print
                wd.navigate().back();
                Thread.sleep(2000);
                wd.navigate().back();
                Thread.sleep(2000);
                 
                
                // Write status in Excel
                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("PASS");

                FileOutputStream outputStream = new FileOutputStream("E:\\User\\Desktop\\W445_W446_AD\\Extra_AIC_01.xlsx");
                wb.write(outputStream);

            } catch (Exception e) {
                // Write fail status in Excel
                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("Fail");
                FileOutputStream outputStream = new FileOutputStream("E:\\User\\Desktop\\W445_W446_AD\\Extra_AIC_02.xlsx");
                wb.write(outputStream);

                // Restart loop on failure
                WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
                Batch.click();
                Thread.sleep(4000);
                wd.findElement(By.linkText("Batch List")).click();
                continue;
            }
        }
        System.out.println("Job Done");
        wd.quit();
    }
}
