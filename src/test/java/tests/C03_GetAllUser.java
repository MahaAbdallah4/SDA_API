
package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class C03_GetAllUser {

    @Test
    public void getAllUsers() {

        Response response = given(ApiUtilities.spec())
                .get("/users");
        response.prettyPrint();
        assertEquals(response.statusCode(), 200);
    }
}