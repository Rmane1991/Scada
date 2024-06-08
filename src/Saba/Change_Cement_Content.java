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
//import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Change_Cement_Content {

	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		
		FileInputStream fis = new FileInputStream("D:\\Eclipse_Excel\\C_270_M40es_Print.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		
		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet1");
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;

		// Web Driver setup
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		ChromeDriver wd = new ChromeDriver(options);
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Create Object

		Utility selUtil = new Utility();
		XSSFCell cell = null;
		
		//Login Into BMC Scada Website
		selUtil.login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");
		Thread.sleep(2000);

		// Clickbatch
		wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
		Thread.sleep(1000);
		wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
		Thread.sleep(1000);

		// For read and write data from excel

		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 1; i <= rowCount; i++) {

			try {
				// Enter Batch No
				WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
				BatchFrom.clear();
				BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

				// Enter Batch No To
				WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
				BatchTo.clear();
				BatchTo.sendKeys(sheet.getRow(i).getCell(1).getRawValue());

				// Click Search Button

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				Thread.sleep(4000);

				// CLick On Challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();
				Thread.sleep(1000);

				// Set Cement Content
				Thread.sleep(1000);
				WebElement txtmincement = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtmincemqty']"));
				txtmincement.clear();
				Thread.sleep(500);
				txtmincement.sendKeys(sheet.getRow(i).getCell(2).getRawValue());
				Thread.sleep(1000);
			
				// Click Save Button for challan
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
				Thread.sleep(2000);
				cell = sheet.getRow(i).createCell(6);
				cell.setCellValue("PASS");

				
				//Write Into Excel remark
				FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\C_270_M40es_Print_01.xlsx");
				wb.write(outputStream);
				Thread.sleep(2000);
				
				// Back To next challan
				wd.navigate().back();
				Thread.sleep(2000);
			}
			catch (Exception e) 
			{
				WebElement Batch = wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]"));
				Batch.click();
				Thread.sleep(4000);
				wd.findElement(By.linkText("Batch List")).click();
				continue;
			}

		}
		System.out.println("Job Done");
		wd.close();
	}

}
