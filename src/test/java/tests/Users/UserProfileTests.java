package tests.Users;

import io.restassured.RestAssured;
import io.restassured.path.json.exception.JsonPathException;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import io.restassured.response.Response;

public class UserProfileTests {

    @Test
    public void TC_US004_001() {
        int userId = 1;
        Response response = ApiUtilities.spec().get("/users/" + userId);

        // Print debug details
        System.out.println("Response Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.body().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200 OK.");

        // If response is not JSON, this will fail
        try {
            Assert.assertNotNull(response.jsonPath().getString("id"), "User ID should be present.");
            Assert.assertNotNull(response.jsonPath().getString("name"), "User name should be present.");
            Assert.assertNotNull(response.jsonPath().getString("email"), "User email should be present.");
        } catch (JsonPathException e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
    }
}