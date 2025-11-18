package tests.Authentication;

import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserLogoutTests {

    @Test
    public void TC_US003_001() {
        Response response = given(ApiUtilities.spec()).post("/logout");
        response.prettyPrint();
    }
}