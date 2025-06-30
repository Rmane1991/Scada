package Saba;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasicAuthExample {
    public static void main(String[] args) {
        // Set the path to your WebDriver executable
       // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
    	WebDriverManager.chromedriver().setup();
        // Create an instance of the WebDriver
        WebDriver driver = new ChromeDriver();

        // Website for testing basic auth
        String username = "admin";  // Replace with actual username
        String password = "admin";  // Replace with actual password
        String baseUrl = "https://the-internet.herokuapp.com/basic_auth";

        // Construct the URL with credentials
        String urlWithCredentials = "https://" + username + ":" + password + "@" + baseUrl.replace("https://", "");

        // Navigate to the website with embedded credentials
        driver.get(urlWithCredentials);

        // Perform further actions or validations as needed

        // Close the browser
        driver.quit();
    }
}

