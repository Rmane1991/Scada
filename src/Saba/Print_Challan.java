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
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Print_Challan {

	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		FileInputStream fis = new FileInputStream("D:\\Eclipse_Excel\\C_273_Print.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet1");
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;// sheet.getLastRowNum() - sheet.getFirstRowNum();

		// Web Driver setup
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--kiosk-printing");
		// options.addArguments("--disable-print-preview");
		ChromeDriver wd = new ChromeDriver(options);

		// WebDriver wd = new ChromeDriver(options);

		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Create Object
		Utility selUtil = new Utility();
		XSSFCell cell = null;
		selUtil.login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");

		@SuppressWarnings("unused")
		Actions a = new Actions(wd);
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
				BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

				// Enter Batch No To
				
				WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
				BatchTo.sendKeys(sheet.getRow(i).getCell(1).getRawValue());

				// Click Search Button For Searching Challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				Thread.sleep(4000);

			/*	// Printing  Batch Report
				
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
				if (selUtil.isDisaplyed(By.xpath("//input[@id='ctl00_ctpContent_Button1']"), wd, 5000) == true);

				// Click Print Button to print Batch Report
				
				WebElement btnprint = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_Button1']"));
				Thread.sleep(1000);
				a.moveToElement(btnprint).release().perform();
				Thread.sleep(1000);
				btnprint.click();
				Thread.sleep(2000);
				a.sendKeys(Keys.ENTER).build().perform();

				// Close Batch Report
				
				Thread.sleep(5000);
				wd.findElement(By.xpath("//div[@id='me']//img[@id='Img121']")).click();
				Thread.sleep(2000);
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				Thread.sleep(4000);
		*/
				// CLick On Challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();

				// Click Save Button for challan
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
				Thread.sleep(2000);
				
				// Click Print Button for challan
				
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
				int cellcount=(int) sheet.getRow(i).getCell(0).getNumericCellValue();
				
				//For Old challn Format
				if(cellcount<=102881)
				{
					Thread.sleep(1000);
					wd.navigate().back();
					Thread.sleep(2000);
				}
				
				// For new Challan Format
				else 
				{

					wd.executeScript("window.print();");
					wd.navigate().back();
					Thread.sleep(2000);
					wd.navigate().back();
					Thread.sleep(2000);
				}
				
				cell = sheet.getRow(i).createCell(3);
				cell.setCellValue("PASS");

				FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\C_273_Print_01.xlsx");
				wb.write(outputStream);
			
			} catch (Exception e) 
			{
				//Write Into excel
				
				cell = sheet.getRow(i).createCell(3);
				cell.setCellValue("Fail");
				FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\C_273_Print_01.xlsx");
				wb.write(outputStream);
				
				//For restart Loop Again
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
