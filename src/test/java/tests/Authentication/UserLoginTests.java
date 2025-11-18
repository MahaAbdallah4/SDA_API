package tests.Authentication;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import utilities.ApiUtilities;

public class UserLoginTests {

    @BeforeClass
    public void setUp() {
        // Set the base URI for Rest Assured
        RestAssured.baseURI = "https://bazaarstores.com/api"; // Replace with your actual API URL
    }

    @Test
    public void TC_US002_001() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/login");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status code 200 (OK) for successful login. Actual Status: " + response.getStatusCode());

        // Check for token presence and store it
        String token = response.jsonPath().getString("authorisation.token");
        Assert.assertNotNull(token, "Token should be present in the response. Actual response: " + response.body().asString());
        ApiUtilities.setToken(token); // Store the token for future use
    }

    @Test
    public void TC_US002_002() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"wrongpassword\" }";
        Response response = ApiUtilities.spec().body(payload).post("/login");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 401, "Expected HTTP status code 401 (Unauthorized) for invalid credentials. Actual Status: " + response.getStatusCode());

        // Check for error message
        String errorMessage = response.jsonPath().getString("error");
        Assert.assertTrue(errorMessage.contains("Invalid credentials"),
                "Response should indicate invalid credentials. Actual response: " + response.body().asString());
    }

    @Test
    public void TC_US002_003() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/login");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status code 200 (OK). Actual Status: " + response.getStatusCode());

        // Here, we should check the token again if applicable
        String token = response.jsonPath().getString("authorisation.token");
        Assert.assertNotNull(token, "Token should be present in the response. Actual response: " + response.body().asString());
    }
}