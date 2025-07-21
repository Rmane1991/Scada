package Saba;

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

public class Print_With_Batch_No extends Utility {

   
	
	public static void main(String[] args) throws IOException, InterruptedException {
        FileInputStream fis = new FileInputStream("D:\\ME_Data\\Excel\\Book.xlsx");
        @SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;
        
        System.out.println(rowCount);

        File pdfFolder = new File("D:\\ME_Data\\PDF");
        if (!pdfFolder.exists()) pdfFolder.mkdirs();

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

        
        
        
		/*
		 * ChromeOptions options = new ChromeOptions();
		 * options.addArguments("--remote-allow-origins=*");
		 * 
		 * 
		 * Map<String, Object> prefs = new HashMap<>();
		 * prefs.put("printing.print_preview_sticky_settings.appState",
		 * "{\"recentDestinations\": [{\"id\": \"Save as PDF\", \"origin\": \"local\", \"account\": \"\"}],"
		 * + "\"selectedDestinationId\": \"Save as PDF\",\"version\": 2}");
		 * prefs.put("download.prompt_for_download", false);
		 * prefs.put("download.default_directory", "D:\\ME_Data\\PDF");
		 * options.setExperimentalOption("prefs", prefs);
		 */
        ChromeDriver wd = new ChromeDriver(options);
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        XSSFCell cell = null;
        login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");
        Thread.sleep(2000);

        wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
        Thread.sleep(1000);
        wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
        Thread.sleep(1000);

        for (int i = 335; i <= 336; i++) {
            String currentStep = "";
            try {
                String batchNo = sheet.getRow(i).getCell(0).getRawValue();

                wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom")).clear();
                wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom")).sendKeys(batchNo);
                wd.findElement(By.id("ctl00_ctpContent_txtBatchTo")).clear();
                wd.findElement(By.id("ctl00_ctpContent_txtBatchTo")).sendKeys(batchNo);
                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(4000);

                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgPrint")).click();
                Thread.sleep(3000);
                
                
                wd.executeScript("window.print();");
                Thread.sleep(3000); // Wait for file to be saved

                String downloadDir = "D:\\ME_Data\\PDF";
                String newName = "Batch_" + batchNo + ".pdf";

                // Detect most recent PDF in the folder
                File dir = new File(downloadDir);
                File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".pdf"));

                if (files != null && files.length > 0) {
                    File latestFile = files[0];
                    for (File file : files) {
                        if (file.lastModified() > latestFile.lastModified()) {
                            latestFile = file;
                        }
                    }

                    File renamed = new File(downloadDir + File.separator + newName);
                    if (latestFile.renameTo(renamed)) {
                        System.out.println("✅ File saved and renamed to: " + renamed.getName());
                    } else {
                        System.out.println("❌ Failed to rename file.");
                    }
                } else {
                    System.out.println("❌ No PDF file found in directory.");
                }

                
                
				/*
				 * wd.executeScript("window.print();"); Thread.sleep(2000); Actions AC= new
				 * Actions(wd); AC.sendKeys("Enter").perform();
				 * 
				 * String batchPath = "D:\\ME_Data\\PDF\\Batch_" + batchNo + ".pdf";
				 * 
				 * Process p1 = Runtime.getRuntime().exec("D:\\ME_Data\\Script\\SavePDF.exe \""
				 * + batchPath + "\""); p1.waitFor(); // Ensure AutoIt completes before next
				 * step Thread.sleep(3000);
				 */
                
				/*
				 * String batchPath = "D:\\ME_Data\\PDF\\Batch_" + batchNo + ".pdf";
				 * Runtime.getRuntime().exec("D:\\ME_Data\\Script\\SavePDF.exe \"" + batchPath +
				 * "\""); Thread.sleep(4000);
				 */
                wd.navigate().back();
                Thread.sleep(2000);

                wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom")).clear();
                wd.findElement(By.id("ctl00_ctpContent_txtBatchFrom")).sendKeys(batchNo);
                wd.findElement(By.id("ctl00_ctpContent_txtBatchTo")).clear();
                wd.findElement(By.id("ctl00_ctpContent_txtBatchTo")).sendKeys(batchNo);
                wd.findElement(By.id("ctl00_ctpContent_btnSearch")).click();
                Thread.sleep(2000);

                wd.findElement(By.id("ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1")).click();
                Thread.sleep(3000);

                try {
                    wd.findElement(By.id("ctl00_ctpContent_btnSave")).click();
                    Thread.sleep(2000);
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                    Thread.sleep(3000);
                } catch (Exception e) {
                    wd.findElement(By.id("ctl00_ctpContent_btnPrint")).click();
                    Thread.sleep(3000);
                }

                wd.executeScript("window.print();");
                Thread.sleep(2000);

                String challanPath = "D:\\ME_Data\\PDF\\Challan_" + batchNo + ".pdf";
               
				Process p2 = Runtime.getRuntime().exec("D:\\ME_Data\\Script\\SavePDF.exe \"" + challanPath + "\"");
                p2.waitFor();
                Thread.sleep(3000);
				/*
				 * String challanPath = "D:\\ME_Data\\PDF\\Challan_" + batchNo + ".pdf";
				 * Runtime.getRuntime().exec("D:\\ME_Data\\Script\\SavePDF.exe \"" + challanPath
				 * + "\""); Thread.sleep(4000);
				 */

                wd.navigate().back();
                Thread.sleep(2000);
                wd.navigate().back();
                Thread.sleep(2000);

                cell = sheet.getRow(i).createCell(6);
                cell.setCellValue("PASS");
                FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
                wb.write(outputStream);

            } catch (Exception e) {
                System.out.println(i + " ❌ Error at step: " + currentStep);
                try {
                    cell = sheet.getRow(i).createCell(6);
                    cell.setCellValue("Fail: " + currentStep);
                    FileOutputStream outputStream = new FileOutputStream("D:\\ME_Data\\Excel\\Book_01.xlsx");
                    wb.write(outputStream);
                } catch (Exception ex) {
                    System.out.println("⚠️ Failed to write FAIL status to Excel at row " + i);
                }

                try {
                    WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
                    Batch.click();
                    Thread.sleep(4000);
                    wd.findElement(By.linkText("Batch List")).click();
                } catch (Exception ex) {
                    System.out.println("⚠️ Recovery failed after failure at row " + i);
                }
            }
        }

        System.out.println("✅ Job Done");
        wd.quit();
    }
}
