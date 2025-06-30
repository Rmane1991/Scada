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

public class Print_Challan extends Utility
{

	public static void main(String[] args) throws IOException, InterruptedException, AWTException 
	{
		FileInputStream fis = new FileInputStream("E:\\Eclipse_Excel\\Me_Print.xlsx");
		String Updatecement="Yes";

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet1");
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;

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
		
		//Utility selUtil = new Utility();
		XSSFCell cell = null;
		login_BMC(wd, "http://www.bmc-scada.online/app/default.aspx", "ME", "MRMA!@489");

		Thread.sleep(2000);
		
		wd.findElement(By.xpath("//*[@id='popup']/button")).click();
		Thread.sleep(1000);

		// Clickbatch
		
		wd.findElement(By.xpath("(//i[@class='fa fa-caret-down'])[3]")).click();
		Thread.sleep(1000);

		wd.findElement(By.xpath("//a[normalize-space()='Batch List']")).click();
		Thread.sleep(1000);

		// For read and write data from excel

		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 1; i <=rowCount; i++) {

			try {

				// Enter Batch No

				WebElement BatchFrom = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
				BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

				// Enter Batch No To

				WebElement BatchTo = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
				BatchTo.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

				// Click Search Button For Searching Challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				Thread.sleep(4000);

				// Printing Batch Report

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgPrint']")).click();
				Thread.sleep(2000);
				wd.executeScript("window.print();");
				Thread.sleep(1000);
				wd.navigate().back();
				Thread.sleep(2000);

				System.out.println(i + ": Batch Report print Done for batch no :-" + sheet.getRow(i).getCell(0).getRawValue());

				// Enter Batch No From
				WebElement BatchFrom1 = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchFrom']"));
				BatchFrom1.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

				// Enter Batch No To

				WebElement BatchTo1 = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtBatchTo']"));
				BatchTo1.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
				

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSearch']")).click();
				Thread.sleep(4000);
				

				// CLick On Challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_gvBatchList_ctl02_imgNoteC1']")).click();

				// Click Save Button for challan
				
				if(Updatecement=="Yes")
				{
					WebElement txt_Cement = wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_txtmincemqty']"));
					String Existing_Value = txt_Cement.getDomAttribute("value");
					if(Existing_Value.contains("445"))
					{
						System.out.println("All Ready 445 cement For Row No:"+i);
					}
					
					else
					{
						txt_Cement.clear();
						txt_Cement.sendKeys("445");
					}
					
				}
				
				
				
				
				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnSave']")).click();
				Thread.sleep(2000);

				// Click Print Button for challan

				wd.findElement(By.xpath("//input[@id='ctl00_ctpContent_btnPrint']")).click();
				Thread.sleep(3000);
				wd.executeScript("window.print();");
				Thread.sleep(2000);
				wd.navigate().back();
				Thread.sleep(2000);
				wd.navigate().back();
				Thread.sleep(2000);
				System.out.println(i + ": Challan Print Done for batch no :-" + sheet.getRow(i).getCell(0).getRawValue());
				cell = sheet.getRow(i).createCell(3);
				cell.setCellValue("PASS");

				FileOutputStream outputStream = new FileOutputStream("E:\\Eclipse_Excel\\Me_Print_01.xlsx");
				wb.write(outputStream);

			} 
			catch (Exception e) 
			{
				// Write Into excel
				cell = sheet.getRow(i).createCell(3);
				cell.setCellValue("Fail");
				FileOutputStream outputStream = new FileOutputStream("E:\\Eclipse_Excel\\Me_Print_02.xlsx");
				wb.write(outputStream);

				// For restart Loop Again
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
