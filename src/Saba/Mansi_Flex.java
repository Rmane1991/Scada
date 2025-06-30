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

public class Mansi_Flex extends Utility {



	public static void main(String[] args) throws IOException, InterruptedException 
	{
		

		FileInputStream fis = new FileInputStream("D:\\New_Host\\Feb_March__Data_Manci_1565_To_3227.xlsx");

		
		// Read Excel sheet
		XSSFWorkbook wb = new XSSFWorkbook(fis);
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
		Utility selUtil = new Utility();
		XSSFCell cell = null;
		
		// Login
		selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "mansiom", "mansiom#25", "3105251205");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "meflex", "meflex", "0375921468");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "rbcrmc", "rbc#24", "213241237");
		//selUtil.login_Flex(wd, "http://www.bmc-scada.online/flex/login.aspx", "land23", "land@23", "5471936802");
		Thread.sleep(2000);
		
		// Mouse action
		Actions a = new Actions(wd);

		// For read and write data from excel
		System.out.println("No of Record Found Into Excel :- " + rowCount);

		for (int i = 1462; i <= rowCount; i++)
			{
			try 
			{
				cell = sheet.getRow(i).createCell(6);
				
				//Enter Batch No From
 				WebElement BatchFrom = wd.findElement(By.id("ctl00_cphBody_txtBatchFrom"));
 				BatchFrom.clear();
				BatchFrom.sendKeys(sheet.getRow(i).getCell(0).getRawValue());
				
				//Enter Batch No To
				WebElement BatchTo = wd.findElement(By.id("ctl00_cphBody_txtBatchTo"));
				BatchTo.clear();
				BatchTo.sendKeys(sheet.getRow(i).getCell(1).getRawValue());
				wd.findElement(By.id("ctl00_cphBody_btnSearch")).click();
				Thread.sleep(1000);
				waitForLoaderToDisappear(wd);
				Thread.sleep(1000);
				
				// Check Batch No is Displayed or Not
				if(selUtil.isDisaplyed(By.xpath("//tr[@class='gridHeader']"),wd,5)==false)
				{
					cell.setCellValue("Batch Not found");
					FileOutputStream outputStream = new FileOutputStream("D:\\New_Host\\Feb_March__Data_Manci_1565_To_3227_01.xlsx");
					wb.write(outputStream);
					Thread.sleep(1000);
					System.out.println(i +":-" + sheet.getRow(i).getCell(0).getRawValue() + ": Batch Not found");
					continue;
				}
				
				//Click On Batch No
				Thread.sleep(3000);
				wd.findElement(By.id("ctl00_cphBody_gvBatchList_ctl02_lnkBatchNo")).click();
				if (selUtil.isDisaplyed(By.id("ctl00_cphBody_drpCustomer"), wd, 15) == true);
				waitForLoaderToDisappear(wd);	
				
				
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
				
				
				//Enter Vehicle number 
				WebElement myEle_Vehicle = wd.findElement(By.xpath("//select[@id='ctl00_cphBody_ddlTruckNo']"));
				selUtil.Dropdown(myEle_Vehicle, sheet.getRow(i).getCell(6).getStringCellValue());
				Thread.sleep(3000);
				
				
				
				//Enter Driver Name
				wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtPrintTruckDriver']")).clear();
				//if (selUtil.isInvisible(By.xpath("//img[@id='ctl00_cphBody_imgLoader']"), wd, 10) == true);
				Thread.sleep(2000);
				
				if(selUtil.isDisaplyed(By.xpath("/html/body/div[6]/div[7]/div/button"), wd, 5) ==true)
				{
					wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
				}
				
				WebElement txtdriver= wd.findElement(By.xpath("//input[@id='ctl00_cphBody_txtPrintTruckDriver']"));
				txtdriver.click();
				txtdriver.sendKeys(sheet.getRow(i).getCell(7).getStringCellValue());
				Thread.sleep(2000);
				waitForLoaderToDisappear(wd);
				*/
				
				//Enter Production Qty
				wd.findElement(By.id("ctl00_cphBody_txtPrintProductionQty")).clear();
				if (selUtil.isInvisible(By.xpath("//img[@id='ctl00_cphBody_imgLoader']"), wd, 20) == true);
				Thread.sleep(2500);
				WebElement btn_ok_Create_Row= wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button"));
				btn_ok_Create_Row.click();
				
				Thread.sleep(1500);
				waitForLoaderToDisappear(wd);
				
				//Create Raw
				WebElement P_Qty = wd.findElement(By.id("ctl00_cphBody_txtPrintProductionQty"));
				P_Qty.sendKeys(sheet.getRow(i).getCell(4).getRawValue());
				Thread.sleep(1500);
				a.click().sendKeys(Keys.ENTER).perform(); 
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				WebElement Lbl_ok_Create_Row= wd.findElement(By.xpath("/html/body/div[6]/p "));
				Thread.sleep(1000);
				if(Lbl_ok_Create_Row.getText().contains("same time already exist"))
				{
					cell.setCellValue("Time Problem");
					FileOutputStream outputStream = new FileOutputStream("D:\\New_Host\\Feb_March__Data_Manci_1565_To_3227_01.xlsx");
					wb.write(outputStream);
					wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
					Thread.sleep(1000);
					wd.findElement(By.id("ctl00_cphBody_btn_clear")).click();
					Thread.sleep(2000);
					continue;
				}
				
				Thread.sleep(2000);
				wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
				Thread.sleep(1500);
				
				//Enter Grade
				WebElement myEle_grade = wd.findElement(By.id("ctl00_cphBody_ddlReceipeCode"));
				Select dropdown_grade = new Select(myEle_grade);// For select Hardware Type
				dropdown_grade.selectByVisibleText(sheet.getRow(i).getCell(5).getStringCellValue());
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				
				if (selUtil.isDisaplyed(By.xpath("/html/body/div[6]/div[7]/div/button"), wd, 10) == true);
				wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
				waitForLoaderToDisappear(wd);
				Thread.sleep(1500);
				//Click Submit Button
				wd.findElement(By.id("ctl00_cphBody_btn_submit")).click();
				Thread.sleep(2500);
				waitForLoaderToDisappear(wd);
				
				
				if (selUtil.isDisaplyed(By.xpath("/html/body/div[6]/div[7]/div/button"), wd, 15) == true);
				
				WebElement print_msg = wd.findElement(By.xpath("/html/body/div[6]/p"));
				String text = print_msg.getText();
				
				
				if (text.contains("successfully")) 
				{
					cell.setCellValue("PASS");
				} 
				
				else
				{
					cell.setCellValue("Fail");
				}
				 
				FileOutputStream outputStream = new FileOutputStream("D:\\New_Host\\Feb_March__Data_Manci_1565_To_3227_01.xlsx");
				wb.write(outputStream);
				
				System.out.println(i +":-" + sheet.getRow(i).getCell(0).getRawValue() + ":" + text);
				Thread.sleep(3000);
				wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
				Thread.sleep(1000);
				wd.findElement(By.id("ctl00_cphBody_btn_clear")).click();
				Thread.sleep(2000);
			} 
			catch (Exception e) 
			{
				try 
				{
					wd.findElement(By.xpath("/html/body/div[6]/div[7]/div/button")).click();
					WebElement Batch= wd.findElement(By.xpath("//i[@class='md md-details']"));
					a.moveToElement(Batch).release().build().perform();
					wd.findElement(By.linkText("Batch List")).click();
					continue;
				}
				catch (Exception e1) 
				{
					WebElement Batch= wd.findElement(By.xpath("//i[@class='md md-details']"));
					a.moveToElement(Batch).release().build().perform();
					wd.findElement(By.linkText("Batch List")).click();
					continue;
				}
				
			}
			
			

		}
		
       System.out.println("Thanks It's Done");
		wd.quit();
        wb.close();
        fis.close();
	}


}
