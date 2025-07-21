package Saba;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Only_Batch_Report_SavePDF extends Utility {

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
        options.addArguments("--kiosk-printing");

        // Set Chrome preferences for silent printing and file saving
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("printing.print_preview_sticky_settings.appState",
            "{\"recentDestinations\": [{\"id\": \"Save as PDF\", \"origin\": \"local\", \"account\": \"\"}],"
            + "\"selectedDestinationId\": \"Save as PDF\","
            + "\"version\": 2}");
        prefs.put("savefile.default_directory", "D:\\ME_Data\\Excel\\PDF");
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", "D:\\ME_Data\\Excel\\PDF");

        options.setExperimentalOption("prefs", prefs);

        ChromeDriver wd = new ChromeDriver(options);
        wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");

        Thread.sleep(2000);
        wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
        Thread.sleep(1000);
        wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
        Thread.sleep(1000);

        // Open first batch report through UI
        currentStep = "Reading Batch No from Excel (Row 1)";
        String firstBatchNo = sheet.getRow(1).getCell(0).getRawValue();

        currentStep = "Entering Batch No From";
        WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
        BatchFrom.clear();
        BatchFrom.sendKeys(firstBatchNo);

        currentStep = "Entering Batch No To";
        WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
        BatchTo.clear();
        BatchTo.sendKeys(firstBatchNo);

        currentStep = "Clicking Search Button for Batch";
        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
        Thread.sleep(4000);

        currentStep = "Clicking Print Batch Report";
        wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
        Thread.sleep(3000);

        currentStep = "Triggering Batch Print";
        wd.executeScript("window.print();");
        Thread.sleep(3000);
        
        String downloadDirPath = "D:\\ME_Data\\Excel\\PDF";
        String baseFileName = "MeinfraNew.pdf";
        File firstDownloaded = waitForPdfDownload(downloadDirPath, baseFileName, 15);

        if (firstDownloaded != null) {
            File renamedFirst = new File(downloadDirPath, "Batch_" + firstBatchNo + ".pdf");
            if (firstDownloaded.renameTo(renamedFirst)) {
                System.out.println("✅ Renamed to: " + renamedFirst.getName());
            } else {
                System.out.println("❌ Rename failed.");
            }
        }

        // Loop for remaining batches
        System.out.println("No of Record Found Into Excel :- " + rowCount);

        for (int i = 335; i <= 336; i++) {
            try {
                currentStep = "Reading Batch No from Excel (Row " + i + ")";
                String batchNoLoop = sheet.getRow(i).getCell(0).getRawValue();
                String reportUrl = "http://www.bmc-scada.online/app/Reports/SkylandReportViewerNew.aspx?btno=" + batchNoLoop;

                currentStep = "Opening blank page and entering URL manually";
                wd.get("about:blank"); // Reset browser to ensure manual typing

                Thread.sleep(3000); // Short wait before simulating input

                // Set URL in address bar using JavaScript and simulate Enter key
                JavascriptExecutor js = (JavascriptExecutor) wd;
                js.executeScript("window.location.href = arguments[0];", reportUrl);

                Thread.sleep(4000); // Wait for navigation to complete

                currentStep = "Triggering print for batch " + batchNoLoop;
                wd.executeScript("window.print();");

                Thread.sleep(3000); // Allow time for PDF to download

                File downloadedLoop = waitForPdfDownload(downloadDirPath, baseFileName, 15);

                if (downloadedLoop != null) {
                    File renamedLoop = new File(downloadDirPath, "Batch_" + batchNoLoop + ".pdf");

                    // ✅ Replace file if already exists
                    if (renamedLoop.exists()) {
                        renamedLoop.delete();
                    }

                    if (downloadedLoop.renameTo(renamedLoop)) {
                        System.out.println("✅ Renamed to: " + renamedLoop.getName());
                    } else {
                        System.out.println("❌ Rename failed for batch: " + batchNoLoop);
                    }
                } else {
                    System.out.println("❌ PDF not downloaded for batch: " + batchNoLoop);
                }

                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("❌ Error at step: " + currentStep + " - " + e.getMessage());
            }
        }


        wd.quit();
        wb.close();
    }
}
