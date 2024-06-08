package Saba;

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

public class Send_To_MCGM {

	public static void main(String[] args) throws InterruptedException, IOException {

		FileInputStream fis = new FileInputStream("D:\\Eclipse_Excel\\Ad_02_02_2023.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);

		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet1");

		// Web Driver setup
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		ChromeDriver wd = new ChromeDriver(options);

		// WebDriver wd = new ChromeDriver();
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

		// No of record present into Excel
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;

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
				Thread.sleep(3000);
				wd.findElement(By.xpath("//input[@ID='ctl00_ctpContent_gvBatchList_ctl02_cbxAddBatch']")).click();
				Thread.sleep(1000);
				
				//Click on Send to MCGM Button
				wd.findElement(By.xpath("//input[@ID='ctl00_ctpContent_btnMcgm']")).click();
				Thread.sleep(1000);
				
				//Message Print For sending MCGM
				WebElement return_Msg = wd.findElement(By.xpath("//div[@class='alert alert-success']"));
				Thread.sleep(1000);

				String msg = return_Msg.getText();

				cell = sheet.getRow(i).createCell(2);

				if (msg.contains("Batch set as MCGM sucessfully")) 
				{
					cell.setCellValue("PASS");
				}
				else
				{
					cell.setCellValue("FAIL");
				}

				//Write Reply to excel 
				FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\Ad_02_02_2023_01.xlsx");
				wb.write(outputStream);

			} catch (Exception e) 
			{
				cell = sheet.getRow(i).createCell(6);
				cell.setCellValue("FAIL");
				FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\Ad_02_02_2023_01.xlsx");
				wb.write(outputStream);
				continue;
			}

		}

		System.out.println("Its Done");
		wd.close();
	}

}
