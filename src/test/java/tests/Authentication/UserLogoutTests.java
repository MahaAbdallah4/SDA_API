package tests.Authentication;

import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import io.restassured.response.Response;

public class UserLogoutTests {

    @Test
    public void TC_US003_001() {
        // Ensure the token is set and valid before attempting to log out
        String token = ApiUtilities.getToken(); // Use a method to retrieve the current token
        Assert.assertNotNull(token, "No valid token found. User must be logged in to log out.");

        // Provide debugging output to verify the token
        System.out.println("Using token for logout: " + token);

        // Making the logout request with the token in the Authorization header
        Response response = ApiUtilities.spec()
                .header("Authorization", "Bearer " + token)
                .post("/logout");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP status code 200 (OK) for logout. Actual Status: " + response.getStatusCode());
        Assert.assertTrue(response.body().asString().contains("Successfully logged out"), "Response should contain the message: Successfully logged out.");

        // Optionally clear the token
        ApiUtilities.setToken(null);
    }
}