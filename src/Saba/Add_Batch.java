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
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;

public class Add_Batch  extends Utility {

	public static void main(String[] args) throws IOException, InterruptedException 
	{

		FileInputStream fis = new FileInputStream("E:\\Manaci_Vijay\\Add_Batch\\Feb_Data_Add.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		// Read Excel sheet
		XSSFSheet sheet = wb.getSheet("Sheet3");
		int rowCount = sheet.getPhysicalNumberOfRows() - 1;// sheet.getLastRowNum() - sheet.getFirstRowNum();

		// Web Driver setup
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		ChromeDriver wd = new ChromeDriver(options);
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		// Create Object
		//Utility selUtil = new Utility();
		XSSFCell cell = null;

		// Login
		login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "mansiom", "mansiom#25", "3105251205");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "rbcrmc", "rbc#24", "213241237");
		// selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx",
		// "land23", "land@23", "20230401");
		Thread.sleep(2000);

		// Mouse action
		Actions a = new Actions(wd);

		WebElement Batch1 = wd.findElement(By.xpath("(//i[@class='md md-details'])[1]"));
		a.moveToElement(Batch1).release().build().perform();
		WebElement btnaddbatch = wd.findElement(By.xpath("//a[@id='ctl00_A18']"));
		btnaddbatch.click();

		// For read and write data from excel
		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 270; i <=rowCount; i++) {
			try {
				// Enter Batch No
				WebElement Batchno = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtBATCHNO']"));
				Batchno.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
				Thread.sleep(1000);

				//System.out.println(sheet.getRow(i).getCell(1).getStringCellValue());
				// Enter Batch Start Time
				WebElement BatchStartTime = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtBATCHSTARTTIME']"));
				BatchStartTime.sendKeys(sheet.getRow(i).getCell(1).getStringCellValue());
				Thread.sleep(1000);

				// Select Customer
				WebElement Cust_Name = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpDownCustomer']"));
				Dropdown(Cust_Name, sheet.getRow(i).getCell(2).getStringCellValue());
				Thread.sleep(1000);

				// Select Site
				WebElement Name_Name = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpDownSite']"));
				Dropdown(Name_Name, sheet.getRow(i).getCell(3).getStringCellValue());
				//Select dropdown = new Select(Name_Name);// For select Hardware Type
				//dropdown.selectByIndex(1);
				
				//selUtil.Dropdown(Name_Name, sheet.getRow(i).getCell(3).getStringCellValue());
				Thread.sleep(1000);
				
				//For Receipe
				WebElement receipe = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpDownRecipe']"));
				Select dropdown = new Select(receipe);// For select Hardware Type
				dropdown.selectByIndex(2);
				Thread.sleep(1000);
				
				// Select Vehicle
				WebElement Vehicle_No = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpDownVehicleNo']"));
				Dropdown(Vehicle_No, sheet.getRow(i).getCell(4).getStringCellValue());
				Thread.sleep(1000);
				
				// Select Driver
				WebElement driver_name = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_droptruckdriver']"));
				//driver_name.clear();
				driver_name.sendKeys(sheet.getRow(i).getCell(5).getStringCellValue());
				Thread.sleep(1000);

				// Click On create Button
				WebElement btncreate = wd.findElement(By.xpath("//input[@id='ctl00_cphBody_btnCreate']"));
				btncreate.click();

				Thread.sleep(2000);

				// WebElement lblmsg=wd.findElement(By.xpath("//div[@class='alert
				// alert-success']"));

				cell = sheet.getRow(i).createCell(6);

				WebElement print_msg = wd.findElement(By.xpath("//div[@class='alert alert-success']"));
				String text = print_msg.getText();

				//if (text.contains("Success  : Batch created sucessfully")) {
					cell.setCellValue(text);
					FileOutputStream outputStream = new FileOutputStream("E:\\Manaci_Vijay\\Add_Batch\\Feb_Data_Add_01.xlsx");
					wb.write(outputStream);
				//}
					
					//div[@class='alert alert-danger']
					
					
				
				Thread.sleep(2000);
				System.out.println(i+": Done");
				// Thread.sleep(60000);
			} catch (Exception e) {
				//cell = sheet.getRow(i).createCell(6);
				//cell.setCellValue("FAIL");
				//FileOutputStream outputStream = new FileOutputStream("E:\\User\\Documents\\Vijay_Data\\Insert_02.xlsx");
				//wb.write(outputStream);
				wd.findElement(By.xpath("//input[@id='ctl00_cphBody_btnclear']")).click();
				continue;
			}

		}

		System.out.println("Thanks It's Done");
		wd.quit();

	}

}
