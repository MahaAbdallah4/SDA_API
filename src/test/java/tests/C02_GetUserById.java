package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojos.UserPojo;
import utilities.ApiUtilities;
import utilities.UserId;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class C02_GetUserById {

    @Test
    public void getUserById() {

        Response response = given(ApiUtilities.spec())
                .get("/users/" + UserId.id);

        UserPojo actual = response.as(UserPojo.class);

        assertEquals(response.statusCode(), 200);
        assertEquals(actual.getId(), UserId.id);
    }
}
