package tests.Authentication;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import com.github.javafaker.Faker;
import utilities.ConfigReader;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserRegistrationTests {

    private Faker faker;

    @Test
    public void TC_US001_001() {
        String payload = String.format("{ \"name\": \"John Doe\", \"email\": \"%s\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }", Faker.instance().internet().emailAddress());
        Response response = given(ApiUtilities.spec()).body(payload).post("/register");
        response.prettyPrint();
    }

    @Test
    public void TC_US001_002() {
        String payload = "{ \"name\": \"Jane Doe\", \"email\": \"existing@example.com\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/register");
        response.prettyPrint();
    }

    @Test
    public void TC_US001_003() {
        String payload = "{ \"name\": \"Jane Doe\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/register");
        response.prettyPrint();
    }

    @Test
    public void TC_US001_004() {
        String payload = "{ \"name\": \"Jane Doe\", \"email\": \"invalid_email\", \"password\": \"secure123\", \"password_confirmation\": \"secure123\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/register");
        response.prettyPrint();
    }
}