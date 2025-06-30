package Saba;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;


import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Delete_Batch extends Utility {
	
	
	public static  boolean isAlertPresent_New() {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
 }


	public static void main(String[] args) throws IOException, InterruptedException 
	{

		
		
		 
		FileInputStream fis = new FileInputStream("E:\\Manaci_Vijay\\Add_Batch\\Feb_Data_Add.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet2");
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;// sheet.getLastRowNum() - sheet.getFirstRowNum();

		// Web Driver setup
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		ChromeDriver wd = new ChromeDriver(options);
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Create Object
		Utility selUtil = new Utility();
		XSSFCell cell = null;

		// Login
		selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "mansiom", "mansiom#25", "3105251205");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "rbcrmc", "rbc#24", "213241237");
		// selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx",
		// "land23", "land@23", "20230401");
		Thread.sleep(2000);

		// Mouse action
		Actions a = new Actions(wd);

		WebElement Batch1 = wd.findElement(By.xpath("(//i[@class='md md-details'])[1]"));
		a.moveToElement(Batch1).release().build().perform();
		WebElement deleteBatch = wd.findElement(By.xpath("//a[@id='ctl00_Delete']"));
		deleteBatch.click();

		// For read and write data from excel
		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 15; i <=rowCount; i++) {
			try {
				// Enter Batch No
				WebElement txt_Batch = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_batchtxt']"));
				txt_Batch.clear();
				txt_Batch.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
				Thread.sleep(1000);

				
				
				//System.out.println(sheet.getRow(i).getCell(1).getStringCellValue());
				// Enter Batch Start Time
				WebElement getbatch = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_getbatchbtn']"));
				getbatch.click();
				
				// Select Customer
				WebElement btn_delete = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_gvBatch_ctl02_imgDelete']"));
				btn_delete.click();
				Thread.sleep(2000);
				String text = null;
				
				if(isAlertPresent(wd)==true)
				{
					 text= wd.switchTo().alert().getText();
					 Thread.sleep(2000);
					 wd.switchTo().alert().accept();
				}
				
				if(text.contains("Batch deleted successfully"))
				{
				cell = sheet.getRow(i).createCell(6);
				cell.setCellValue(text);
					FileOutputStream outputStream = new FileOutputStream("E:\\Manaci_Vijay\\Add_Batch\\Feb_Data_Add_01.xlsx");
					wb.write(outputStream);
				
				Thread.sleep(2000);
				System.out.println(i+": Done");
				}

			} catch (Exception e) 
			{
				//cell = sheet.getRow(i).createCell(6);
				//cell.setCellValue("FAIL");
				//FileOutputStream outputStream = new FileOutputStream("E:\\User\\Documents\\Vijay_Data\\Insert_02.xlsx");
				//wb.write(outputStream);
				wd.findElement(By.xpath("//input[@id='ctl00_cphBody_batchtxt']")).clear();
				continue;
			}

		}

		System.out.println("Thanks It's Done");
		wd.quit();

	
		
	}

}
