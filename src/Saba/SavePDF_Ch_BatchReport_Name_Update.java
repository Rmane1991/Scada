package Saba;

import java.awt.AWTException;
import java.io.File;
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

public class SavePDF_Ch_BatchReport_Name_Update extends Utility {
	
	
	
	public static File waitForPdfDownload(String downloadDir, String expectedFileName, int timeoutSec) throws InterruptedException {
	    File file = new File(downloadDir, expectedFileName);
	    int waited = 0;
	    while (!file.exists() && waited < timeoutSec) {
	        Thread.sleep(1000);
	        waited++;
	    }

	    if (file.exists()) {
	        System.out.println("✅ File found: " + file.getAbsolutePath());
	        return file;
	    } else {
	        System.out.println("❌ File not found after waiting " + timeoutSec + " seconds.");
	        return null;
	    }
	}


    public static void main(String[] args) throws IOException, InterruptedException, AWTException 
    {
    	
    	
    	
        FileInputStream fis = new FileInputStream("D:\\ME_Data\\Excel\\Book.xlsx");

        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
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
        
        prefs.put("savefile.default_directory", "D:\\ME_Data\\Excel\\PDF"); // Set download location
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", "D:\\ME_Data\\Excel\\PDF");
        
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
		
		String currentStep = "";

		for (int i = 335; i <= 336; i++) {
		    try {
		        currentStep = "Reading Batch No from Excel (Row " + i + ")";
		        String batchNo = sheet.getRow(i).getCell(0).getRawValue();

		        currentStep = "Entering Batch No From";
		        WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
		        BatchFrom.clear();
		        BatchFrom.sendKeys(batchNo);

		        currentStep = "Entering Batch No To";
		        WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
		        BatchTo.clear();
		        BatchTo.sendKeys(batchNo);

		        currentStep = "Clicking Search Button for Batch";
		        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
		        Thread.sleep(4000);

		        currentStep = "Clicking Print Batch Report";
		        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
		        Thread.sleep(3000);

		        currentStep = "Triggering Batch Print";
		        wd.executeScript("window.print();");
		        
		        
		        String downloadDir = "D:\\ME_Data\\Excel\\PDF";
		        String fileName = "MeinfraNew.pdf";
                File downloaded = waitForPdfDownload(downloadDir, fileName, 15);
                
                
                if (downloaded != null) 
                {
                    // Rename file if needed
                    File newFile = new File(downloadDir, "Batch_" + batchNo + ".pdf");
                    if (downloaded.renameTo(newFile)) {
                        System.out.println("✅ Renamed to: " + newFile.getName());
                    } else {
                        System.out.println("❌ Rename failed.");
                    }
                }
                
               
		        Thread.sleep(4000);

		        currentStep = "Navigating Back after Batch Print";
		        wd.navigate().back();
		        Thread.sleep(2000);

		        currentStep = "Entering Batch From for Challan";
		        WebElement BatchFrom_CH = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
		        BatchFrom_CH.clear();
		        BatchFrom_CH.sendKeys(batchNo);

		        currentStep = "Entering Batch To for Challan";
		        WebElement BatchTo_CH = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
		        BatchTo_CH.clear();
		        BatchTo_CH.sendKeys(batchNo);

		        currentStep = "Clicking Search Button for Challan";
		        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
		        Thread.sleep(2000);

		        currentStep = "Clicking Challan Icon";
		        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();
		        Thread.sleep(3000);

		        try {
		            currentStep = "Clicking Save Button for Challan";
		            wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
		            Thread.sleep(2000);
		            currentStep = "Clicking Print Button for Challan";
		            wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
		            Thread.sleep(3000);
		        } catch (Exception e) 
		        {
		            currentStep = "Save not found, directly clicking Print Button for Challan";
		            wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
		            Thread.sleep(3000);
		        }

		        currentStep = "Triggering Challan Print";
		        wd.executeScript("window.print();");
		        Thread.sleep(3000);
		        
		        
		        
		        String fileName_C = "MCGMChallan.pdf";
                File downloaded_C = waitForPdfDownload(downloadDir, fileName_C, 15);
                
                if (downloaded_C != null) 
                {
                    // Rename file if needed
                    File newFile = new File(downloadDir, "Ch_" + batchNo + ".pdf");
                    if (downloaded_C.renameTo(newFile)) {
                        System.out.println("✅ Renamed to: " + newFile.getName());
                    } else {
                        System.out.println("❌ Rename failed.");
                    }
                }
		        
		        Thread.sleep(5000);

		        currentStep = "Navigating Back after Challan Print";
		        wd.navigate().back();
		        Thread.sleep(2000);
		        wd.navigate().back();
		        Thread.sleep(2000);

		        currentStep = "Writing PASS to Excel (Row " + i + ")";
		        cell = sheet.getRow(i).createCell(6);
		        cell.setCellValue("PASS");
		        FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
		        wb.write(outputStream);

		    } catch (Exception e) 
		    {
		        System.out.println(i+":- ❌ Error at step: " + currentStep);

		        try {
		            cell = sheet.getRow(i).createCell(6);
		            cell.setCellValue("Fail:-"+ currentStep);
		            FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
		            wb.write(outputStream);
		        } catch (Exception ex) {
		            System.out.println("⚠️ Failed to write FAIL status to Excel at row " + i);
		            ex.printStackTrace();
		        }

		        try {
		            currentStep = "Recovery: Clicking Batch List Menu";
		            WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
		            Batch.click();
		            Thread.sleep(4000);
		            wd.findElement(By.linkText("Batch List")).click();
		        } catch (Exception ex) {
		            System.out.println("⚠️ Recovery failed after failure at row " + i);
		        }

		        continue;
		    }
		}
		 System.out.println("Job Done");
	        wd.quit();
	    }
	}


		/*
		 * for (int i =335; i <=rowCount; i++) { try { // Enter Batch No From WebElement
		 * BatchFrom =
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
		 * BatchFrom.clear();
		 * BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
		 * 
		 * // Enter Batch No To WebElement BatchTo =
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
		 * BatchTo.clear(); BatchTo.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
		 * 
		 * // Click Search
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click()
		 * ; Thread.sleep(4000);
		 * 
		 * //CLick On Print Batch Report
		 * 
		 * wd.findElement(By.xpath(
		 * "//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
		 * Thread.sleep(3000);
		 * 
		 * // Execute the print command silently wd.executeScript("window.print();");
		 * Thread.sleep(4000); // Wait for print to complete wd.navigate().back();
		 * Thread.sleep(2000);
		 * 
		 * // Click On Challan
		 * 
		 * WebElement BatchFrom_CH =
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
		 * BatchFrom_CH.clear();
		 * BatchFrom_CH.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
		 * 
		 * // Enter Batch No To WebElement BatchTo_CH =
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
		 * BatchTo_CH.clear();
		 * BatchTo_CH.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
		 * 
		 * // Click Search
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click()
		 * ; Thread.sleep(2000);
		 * 
		 * //Click On Challan wd.findElement(By.xpath(
		 * "//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();
		 * Thread.sleep(3000);
		 * 
		 * // Click Save Button for Challan try {
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
		 * Thread.sleep(2000); // Click Print Button for Challan
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
		 * Thread.sleep(3000); }
		 * 
		 * catch (Exception e) { // Click Print Button for Challan
		 * wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
		 * Thread.sleep(3000); }
		 * 
		 * // Execute the print command silently wd.executeScript("window.print();");
		 * Thread.sleep(5000); // Wait for print to complete
		 * 
		 * // Navigate back after print wd.navigate().back(); Thread.sleep(2000);
		 * wd.navigate().back(); Thread.sleep(2000);
		 * 
		 * 
		 * // Write status in Excel cell = sheet.getRow(i).createCell(6);
		 * cell.setCellValue("PASS");
		 * 
		 * FileOutputStream outputStream = new
		 * FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx"); wb.write(outputStream);
		 * 
		 * } catch (Exception e) { // Write fail status in Excel cell =
		 * sheet.getRow(i).createCell(6); cell.setCellValue("Fail"); FileOutputStream
		 * outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_02.xlsx");
		 * wb.write(outputStream);
		 * 
		 * // Restart loop on failure WebElement Batch =
		 * wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
		 * Batch.click(); Thread.sleep(4000);
		 * wd.findElement(By.linkText("Batch List")).click(); continue; } }
		 */
       