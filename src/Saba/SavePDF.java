package Saba;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
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
        FileInputStream fis = new FileInputStream("D:\\ME_Data\\Excel\\Book.xlsx");
        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("Sheet1");
        int rowCount = sheet.getPhysicalNumberOfRows() - 1;

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--kiosk-printing");

        Map<String, Object> prefs = new HashMap<>();
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

        System.out.println("No of Record Found Into Excel :- " + rowCount);
        XSSFCell cell = null;

        for (int i = 1; i <= rowCount; i++) {
            try {
                String batchNo = sheet.getRow(i).getCell(0).getRawValue(); // unique number

                String thaneName = "";
                if (sheet.getRow(i).getCell(1).getRawValue() != null) {
                    CellType type = sheet.getRow(i).getCell(1).getCellType();
                    if (type == CellType.STRING) {
                        thaneName = sheet.getRow(i).getCell(1).getStringCellValue();
                    } else if (type == CellType.NUMERIC) {
                        thaneName = String.valueOf((int) sheet.getRow(i).getCell(1).getNumericCellValue());
                    }
                } else {
                    thaneName = "Blank";
                }
                thaneName = thaneName.replaceAll("[\\\\/:*?\"<>|]", "_");

                WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
                BatchFrom.clear();
                BatchFrom.sendKeys(batchNo);

                WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
                BatchTo.clear();
                BatchTo.sendKeys(batchNo);

                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
                Thread.sleep(4000);

                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();

                // PRINT Batch Report
                wd.executeScript("window.print();");
                Thread.sleep(10000);

                // RENAME Batch PDF
                String pdfFolder = "D:\\ME_Data\\Excel\\PDF\\";
                String sourcePath = pdfFolder + "untitled.pdf"; // Or "download.pdf"
                String targetPath = pdfFolder + batchNo + "_" + thaneName + "_Batch.pdf";

                File sourceFile = new File(sourcePath);
                File targetFile = new File(targetPath);
                int waitCount = 0;
                while (!sourceFile.exists() && waitCount < 20) {
                    Thread.sleep(1000);
                    waitCount++;
                }
                if (sourceFile.renameTo(targetFile)) {
                    System.out.println("Saved PDF as: " + targetFile.getName());
                } else {
                    System.out.println("Failed to rename Batch PDF for: " + thaneName);
                }

                Thread.sleep(7000);
                wd.findElement(By.xpath("//div[@id='me']//img[@id='Img121']")).click();
                Thread.sleep(2000);

                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
                Thread.sleep(2000);
                wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
                Thread.sleep(3000);

                // PRINT Challan
                wd.executeScript("window.print();");
                Thread.sleep(10000);

                // RENAME Challan PDF
                String challanTargetPath = pdfFolder + batchNo + "_" + thaneName + "_Challan.pdf";
                File challanTargetFile = new File(challanTargetPath);
                waitCount = 0;
                while (!sourceFile.exists() && waitCount < 20) {
                    Thread.sleep(1000);
                    waitCount++;
                }
                if (sourceFile.renameTo(challanTargetFile)) {
                    System.out.println("Saved PDF as: " + challanTargetFile.getName());
                } else {
                    System.out.println("Failed to rename Challan PDF for: " + thaneName);
                }

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
