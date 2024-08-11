package API;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Launch {

	public static void main(String[] args) 
	{
		 WebDriverManager.chromedriver().setup();

	        // Create an instance of ChromeDriver
	        WebDriver driver = new ChromeDriver();

	        // Open the specified URL
	        driver.get("https://www.google.com");

	        // Maximize the browser window
	        driver.manage().window().maximize();

	        // Close the browser
	        driver.quit();
	}

}
