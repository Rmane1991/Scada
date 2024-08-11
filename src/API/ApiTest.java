package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class ApiTest {
    private static final Logger logger = LogManager.getLogger(ApiTest.class);

    @Test
    public void testResponseTime() {
        long startTime = System.currentTimeMillis();
        
        @SuppressWarnings("unused")
		Response response = RestAssured
            .given()
            .when()
            .get("https://restcountries.com/v3.1/name/USA");
        
        long endTime = System.currentTimeMillis();
        
        logger.info("Response time: " + (endTime - startTime) + " ms");
        System.out.println("Response time: " + (endTime - startTime) + " ms");
    }
}

