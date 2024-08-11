package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApiTestFramework {
	private static final Logger logger = LogManager.getLogger(ApiTestFramework.class);

	@Test(dataProvider = "countryData")
	public void testCountryDetails(String countryCode, int expectedStatusCode, String expectedName) 
	{
		logger.info("Sending request for country code: " + countryCode);

		Response response = RestAssured.given().when().get("https://restcountries.com/v3.1/name/" + countryCode);

		logger.info("Response received: " + response.asString());

		Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");

		if (expectedStatusCode == 200) {
			String actualName = response.jsonPath().getString("name.common");
			Assert.assertEquals(actualName, expectedName, "Country name mismatch");
		} else {

			String responseBody = response.getBody().asString();
			Assert.assertTrue(responseBody.contains("Not Found"), "Response body does not contain 'Not Found' message");
			Assert.assertFalse(responseBody.isEmpty(), "Response body should not be empty for 404 errors");

		}
	}

	@DataProvider(name = "countryData")
	public Object[][] countryData() {
		return new Object[][] { { "USA", 200, "[United States]" }, // Valid country code
				{ "XYZ", 404, null } // Invalid country code {"status":404,"message":"Not Found"}
		};
	}
}
