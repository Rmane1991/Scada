package Saba;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;
import method.Utility;



public class Insert_From_Excel_Flex extends Utility
{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		

		FileInputStream fis = new FileInputStream("E:\\Manaci_Vijay\\Feb_March__Data_Manci_1565_To_3227.xlsx");

		
		XSSFWorkbook wb = new XSSFWorkbook(fis);
        // Read Excel sheet
		//XSSFSheet sheet = wb.getSheet("Flex");
		XSSFSheet sheet = wb.getSheet("Sheet1");
		int rowCount = sheet.getPhysicalNumberOfRows()-1;// sheet.getLastRowNum() - sheet.getFirstRowNum();

		// Web Driver setup
		WebDriverManager.chromedriver().setup();
		
		//ChromeOptions options = new ChromeOptions();
		//options.addArguments("--remote-allow-origins=*");
		WebDriver wd = new ChromeDriver();
		wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		((ChromeDriver) wd).executeScript("document.body.style.zoom='75%'");
		
		// Create Object
		//Utility selUtil = new Utility();
		XSSFCell cell = null;
		
		// Login
		login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "mansiom", "mansiom#25", "3105251205");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "meflex", "meflex", "0375921468");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "rbcrmc", "rbc#24", "213241237");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "land23", "land@23", "5471936802");
		Thread.sleep(2000);
		
		
		
		
		// Mouse action
		Actions a = new Actions(wd);

		// For read and write data from excel
		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 1398; i <= rowCount; i++) //for (int i = rowCount; i >= 1; i--)
			{
			try 
			{
				//Enter Batch No From
 				WebElement BatchFrom = wd.findElement(By.id("ctl00_cphBody_txtBatchFrom"));
				BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
				
				//Enter Batch No To
				WebElement BatchTo = wd.findElement(By.id("ctl00_cphBody_txtBatchTo"));
				//String aString=sheet.getRow(i).getCell(1).getRawValue();
				BatchTo.sendKeys(sheet.getRow(i).getCell(1).getRawValue());
				wd.findElement(By.id("ctl00_cphBody_btnSearch")).click();
				
				waitForLoaderToDisappear(wd);
				
				//Click On Batch No
				Thread.sleep(3000);
				wd.findElement(By.id("ctl00_cphBody_gvBatchList_ctl02_lnkBatchNo")).click();
				if (isDisaplyed(By.id("ctl00_cphBody_drpCustomer"), wd, 15) == true);
				waitForLoaderToDisappear(wd);	
				
				
				//wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtPrintTruckDriver']")).clear();
				
				
				/*
				//Enter Customer Name
				WebElement myEle_Cust = wd.findElement(By.id("ctl00_cphBody_drpCustomer"));
				Thread.sleep(2000);
				selUtil.Dropdown(myEle_Cust, sheet.getRow(i).getCell(2).getStringCellValue());
				Thread.sleep(3000);
				
				
				// Enter Site Name
				WebElement myEle_Site = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_drpsite']"));
				selUtil.Dropdown(myEle_Site, sheet.getRow(i).getCell(3).getStringCellValue());
				Thread.sleep(3000);
				
				/*
				//Enter Vehicle number 
				WebElement myEle_Vehicle = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_ddlTruckNo']"));
				selUtil.Dropdown(myEle_Vehicle, sheet.getRow(i).getCell(6).getStringCellValue());
				Thread.sleep(3000);
				
				*/
				/*
				//Enter Driver Name
				wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtPrintTruckDriver']")).clear();
				//if (selUtil.isInvisible(By.xpath("//img[@id='ctl00_cphBody_imgLoader']"), wd, 10) == true);
				Thread.sleep(2000);
				
				if(selUtil.isDisaplyed(By.xpath("//button[contains(text(),'OK')]"), wd, 5) ==true)
				{
					wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
				}
				
				WebElement txtdriver= wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtPrintTruckDriver']"));
				txtdriver.click();
				txtdriver.sendKeys(sheet.getRow(i).getCell(7).getStringCellValue());
				Thread.sleep(2000);
				waitForLoaderToDisappear(wd);
				*/
				//WebElement Str=wd.findElement(By.xpath("//span[@id='ctl00_cphBody_lblPrintBatcherName']"));
				//Str.click();
				
				
				
				//Enter Production Qty
				wd.findElement(By.id("ctl00_cphBody_txtPrintProductionQty")).clear();
				if (isInvisible(By.xpath("//img[@id='ctl00_cphBody_imgLoader']"), wd, 20) == true);
				Thread.sleep(2500);
				wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
				Thread.sleep(1500);
				waitForLoaderToDisappear(wd);
				
				//Create Raw
				WebElement P_Qty = wd.findElement(By.id("ctl00_cphBody_txtPrintProductionQty"));
				P_Qty.sendKeys(sheet.getRow(i).getCell(4).getRawValue());
				Thread.sleep(1500);
				a.click().sendKeys(Keys.ENTER).perform(); 
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				//if (selUtil.isDisaplyed(By.xpath("//button[contains(text(),'OK')]")
				//div[@class='sa-confirm-button-container']
				if (isDisaplyed(By.xpath("//div[@class='sa-confirm-button-container']"), wd, 25) == true);
				
				//if (selUtil.isDisaplyed(By.xpath("//div[@class='sweet-alert showSweetAlert visible']//button[contains(text(),'OK')]"), wd, 15000) == true);
				Thread.sleep(2000);
				wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
				Thread.sleep(1500);
				
				//Enter Grade
				WebElement myEle_grade = wd.findElement(By.id("ctl00_cphBody_ddlReceipeCode"));
				Select dropdown_grade = new Select(myEle_grade);// For select Hardware Type
				dropdown_grade.selectByVisibleText(sheet.getRow(i).getCell(5).getStringCellValue());
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				
				// if (selUtil.isDisaplyed(By.xpath("//button[contains(text(),'OK')]"), wd,
				if (isDisaplyed(By.xpath("//div[@class='sweet-alert showSweetAlert visible']//button[contains(text(),'OK')]"), wd, 10000) == true);
				wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				//Click Submit Button
				wd.findElement(By.id("ctl00_cphBody_btn_submit")).click();
				Thread.sleep(2500);
				waitForLoaderToDisappear(wd);
				//if (selUtil.isDisaplyed(By.xpath("//p[contains(text(),'Batch created successfully.')]"), wd, 15) == true);
				if (isDisaplyed(By.xpath("//p[contains(text(),'Batch Updated successfully.')]"), wd, 15) == true);
				

				cell = sheet.getRow(i).createCell(6);

				//WebElement print_msg = wd.findElement(By.xpath("//p[contains(text(),'Batch created successfully.')]"));
				WebElement print_msg = wd.findElement(By.xpath("//p[contains(text(),'Batch Updated successfully.')]"));
				String text = print_msg.getText();

				if (print_msg.isDisplayed()) 
				{
					cell.setCellValue("PASS");
				} 
				 
				FileOutputStream outputStream = new FileOutputStream("E:\\Manaci_Vijay\\Feb_March__Data_Manci_1565_To_3227_01.xlsx");
				wb.write(outputStream);
				
				System.out.println(i +":-" + sheet.getRow(i).getCell(0).getRawValue() + ":" + text);
				Thread.sleep(3000);
				wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();

				wd.findElement(By.id("ctl00_cphBody_btn_clear")).click();
				Thread.sleep(2000);
				//Thread.sleep(60000);
			} 
			catch (Exception e) 
			{
				
				cell = sheet.getRow(i).createCell(6);
				cell.setCellValue("FAIL");
				FileOutputStream outputStream = new FileOutputStream("E:\\Manaci_Vijay\\Feb_March__Data_Manci_1565_To_3227_02.xlsx");
				wb.write(outputStream);
				
				Thread.sleep(3000);
				
				try 
				{	
					//try {

						if (wd.findElement(By.xpath("//p[contains(text(),'Batch Not updated')]")).isDisplayed() == true) 
						{
							Thread.sleep(1000);
							wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
							Thread.sleep(1000);
						}
						//} 
				/*	catch (Exception e1) 
					{
						if (wd.findElement(By.xpath("//p[contains(text(),'Batch Updated successfully.')]")).isDisplayed() == true) {
							Thread.sleep(1000);
							cell.setCellValue("Batch Updated");
							wd.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
							Thread.sleep(1000);
						}
					}*/

				}
				catch (Exception e1)
				{
					cell.setCellValue("Batch Not Found");
					System.out.println(i +":-" + sheet.getRow(i).getCell(0).getRawValue() + ":Batch Not found");
				}
				
				WebElement Batch= wd.findElement(By.xpath("//i[@class='md md-details']"));
				a.moveToElement(Batch).release().build().perform();
				wd.findElement(By.linkText("Batch List")).click();
				continue;
			}
			
			

		}
		
       System.out.println("Thanks It's Done");
		wd.quit();
        wb.close();
        fis.close();
	}

}
