package method;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

public class Utility

{

	public static WebDriver wd;
	
public static  WebDriver  startBrowser (String browsename,String URL ) 
	
	{
		if (browsename.equalsIgnoreCase("chrome"))
		{
			WebDriverManager.chromedriver().setup();
			ChromeOptions options= new ChromeOptions();
			options.setAcceptInsecureCerts(true);
			options.addArguments("--force-device-scale-factor=0.8");
			 wd = new ChromeDriver(options);
		new WebDriverWait(wd, Duration.ofSeconds(20));
			
		}
		wd.manage().window().maximize();
		new WebDriverWait(wd, Duration.ofSeconds(30));
		wd.get(URL);
		return wd;
		
	}
	
	

public static void waitForLoaderToDisappear(WebDriver driver) 
{
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40)); // Set timeout as required
    wait.until(driver1 -> {
        JavascriptExecutor js = (JavascriptExecutor) driver1;
        String script = "return document.getElementById('ctl00_cphBody_upLoader').style.display === 'none';";
        return (Boolean) js.executeScript(script);
    });
}

	
	public static boolean isDisaplyed(By Locator, WebDriver wd, long tm)
	{
		boolean isDisplayed = false;

		try {

			WebDriverWait wt = new WebDriverWait(wd, Duration.ofSeconds(tm));
			wt.until(ExpectedConditions.visibilityOfElementLocated(Locator));
			isDisplayed = true;
		} catch (Exception e)

		{

			//e.printStackTrace();

		}

		return isDisplayed;

	}

	
	
	
	 public static void acceptAlertIfPresent() {
	        try {
	            Alert alert = wd.switchTo().alert();
	            System.out.println("Alert found: " + alert.getText());
	            alert.accept();
	            System.out.println("Alert accepted.");
	        } catch (NoAlertPresentException e) {
	            System.out.println("No alert present.");
	        }
	    }
	
	 
	 
	 public static boolean isAlertPresent(WebDriver wd1) {
	        try {
	        	wd1.switchTo().alert();
	            return true;
	        } catch (NoAlertPresentException e) {
	            return false;
	        }
	 }
	
	public static boolean isInvisible(By Locator, WebDriver wd, long tm)
	{
		boolean isDisplayed = false;

		try {

			WebDriverWait wt = new WebDriverWait(wd, Duration.ofSeconds(tm));
			wt.until(ExpectedConditions.invisibilityOfElementLocated(Locator));
			isDisplayed = true;
		} catch (Exception e)

		{

			//e.printStackTrace();

		}

		return isDisplayed;

	}
	
	
	
	public void hideElement(String xpath)
	{
	   
	}
	
	
	
	
	
	
	
	
	public void Dropdown(WebElement cat, String visible)
	{
		// WebElement myEleDp = wd.findElement(By.id(cat));
		Select dropdown = new Select(cat);// For select Hardware Type
		dropdown.selectByVisibleText(visible);

	}

	public static void login_Flex(WebDriver wd, String url, String User, String Pass, String refcode) 
	{
		wd.manage().window().maximize();
		wd.get(url);
		wd.findElement(By.id("txtLoginId")).sendKeys(User);
		wd.findElement(By.id("txtPassword")).sendKeys(Pass);
		wd.findElement(By.id("txtRefCode")).sendKeys(refcode);
		wd.findElement(By.id("btnSubmit")).click();
		System.out.println("Login Done");

	}

	public void print(WebDriver wd, String id, String cat) 
	{
		WebElement print_msg = wd.findElement(By.id(id));
		String text = print_msg.getText();
		System.out.println(cat + " " + text);
	}

	public static void login_BMC(WebDriver wd, String url, String User, String Pass) throws InterruptedException 
	{
		wd.manage().window().maximize();
		wd.get(url);
		wd.findElement(By.id("txtLoginId")).sendKeys(User);
		wd.findElement(By.id("txtPassword")).sendKeys(Pass);
		wd.findElement(By.id("btnSubmit")).click();
		Thread.sleep(2000);
		wd.findElement(By.xpath("//a[@id='dtlstModules_ctl00_lblModuleName']")).click();
		Thread.sleep(2000);
		
		System.out.println("Login Done");

	}
	
}
