package tests.Authentication;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import com.github.javafaker.Faker;

public class UserRegistrationTests {

    private Faker faker;

    @BeforeClass
    public void setUp() {
        // Set the base URI for Rest Assured
        RestAssured.baseURI = "https://bazaarstores.com/api"; // Replace with your API URL
        faker = new Faker(); // Initialize Faker
    }

    @Test
    public void TC_US001_001() {
        String email = faker.internet().emailAddress(); // Generate a random email
        String payload = "{ \"name\": \"John Doe\", \"email\": \"" + email + "\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/register");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 201, "Expected HTTP status code 201 (Created).");
        Assert.assertTrue(response.body().asString().contains("User created successfully"),
                "Expected success message did not appear in the response.");

        // Check if user details are present
        Assert.assertNotNull(response.jsonPath().getString("user.name"), "User name should be present.");
        Assert.assertNotNull(response.jsonPath().getString("user.email"), "User email should be present.");
    }

    @Test
    public void TC_US001_002() {
        String payload = "{ \"name\": \"Jane Doe\", \"email\": \"existing@example.com\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/register");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 422, "Expected HTTP status code 422 (Unprocessable Entity).");
        Assert.assertTrue(response.body().asString().contains("The email has already been taken."),
                "Expected error message did not appear in the response: " + response.body().asString());
    }

    @Test
    public void TC_US001_003() {
        String payload = "{ \"name\": \"Jane Doe\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/register");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 422, "Expected HTTP status code 422 (Unprocessable Entity).");
        Assert.assertTrue(response.body().asString().contains("The email field is required."),
                "Expected missing fields message did not appear in the response: " + response.body().asString());
    }

    @Test
    public void TC_US001_004() {
        String payload = "{ \"name\": \"Jane Doe\", \"email\": \"invalid_email\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = ApiUtilities.spec().body(payload).post("/register");

        System.out.println("Response Body: " + response.body().asString()); // Debug Output
        Assert.assertEquals(response.getStatusCode(), 422, "Expected HTTP status code 422 (Unprocessable Entity).");
        Assert.assertTrue(response.body().asString().contains("The email field must be a valid email address."),
                "Expected invalid email format message did not appear in the response: " + response.body().asString());
    }
}