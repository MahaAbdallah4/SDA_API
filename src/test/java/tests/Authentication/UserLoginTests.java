package tests.Authentication;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;

public class UserLoginTests {

    @Test
    public void TC_US002_001() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"secure123\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/login");
        response.prettyPrint();
    }

    @Test
    public void TC_US002_002() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"wrongpassword\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/login");
        response.prettyPrint();
    }

    @Test
    public void TC_US002_003() {
        String payload = "{ \"email\": \"valid@example.com\", \"password\": \"secure123\" }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/login");
        response.prettyPrint();
    }
}