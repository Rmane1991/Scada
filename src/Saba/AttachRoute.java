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

public class AttachRoute {

	public static void main(String[] args) throws InterruptedException, IOException {
		FileInputStream fis = new FileInputStream("D:\\Eclipse_Excel\\Sample_Route.xlsx");

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
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		// Create Object
		Utility selUtil = new Utility();
		XSSFCell cell = null;

		// Login
		selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "meflex", "meflex", "1936587042");

		// Mouse action
		Actions a = new Actions(wd);
		Thread.sleep(2000);

		// For read and write data from excel
		System.out.println("No of Record Found Into Excel :- " + rowCount);

		// Clickbatch
		wd.findElement(By.xpath("//i[@class='md md-details']")).click();
		Thread.sleep(1000);

		wd.findElement(By.xpath("(//a[normalize-space()='MCGM Batch'])[1]")).click();
		Thread.sleep(1000);

		for (int i = 1; i <= rowCount; i++) {

			// Enter Batch No From
			WebElement BatchFrom = wd.findElement(By.id("ctl00_cphBody_txtBatchFrom"));
			BatchFrom.clear();
			BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());

			// Enter Batch No To
			WebElement BatchTo = wd.findElement(By.id("ctl00_cphBody_txtBatchTo"));
			BatchTo.clear();
			BatchTo.sendKeys(sheet.getRow(i).getCell(1).getRawValue());
			Thread.sleep(1000);
			
			//Click ON search button 
			wd.findElement(By.xpath("//input[@id='ctl00_cphBody_btnSearch']")).click();
			Thread.sleep(3000);
			
			// Click Attch route
			wd.findElement(By.xpath("//input[@id='ctl00_cphBody_gvBatchList_ctl02_imgRply']")).click();
			Thread.sleep(3000);
			
			//Set Route Name
			WebElement route = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpRoute']"));
			selUtil.Dropdown(route, sheet.getRow(i).getCell(2).getStringCellValue());
			Thread.sleep(1000);

			// Click on Map
			wd.findElement(By.xpath("//input[@id='ctl00_cphBody_imgReplay']")).click();
			Thread.sleep(4000);
			WebElement btnupdate = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_btnupdate']"));
			a.moveToElement(btnupdate).release().build().perform();
			btnupdate.click();
			Thread.sleep(3000);

			cell = sheet.getRow(i).createCell(3);
			WebElement return_msg = wd.findElement(By.xpath("//p[normalize-space()='Trip updated successfully.']"));
			String Abc = return_msg.getText();

			if (Abc.contains("Trip updated successfully.")) 
			{
				cell.setCellValue("PASS");
			}

			FileOutputStream outputStream = new FileOutputStream("D:\\Eclipse_Excel\\Sample_Route_01.xlsx");
			wb.write(outputStream);
			Thread.sleep(3000);
			wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();

			Thread.sleep(70000);
		}
		
		System.out.println("Its Done");
		wd.close();
	}

}
