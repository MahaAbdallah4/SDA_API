
package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import utilities.UserId;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class C05_DeleteUser {

    @Test
    public void deleteUser() {

        Response response = given(ApiUtilities.spec())
                .delete("/users/" + UserId.id);

        assertEquals(response.statusCode(), 200);
    }
}