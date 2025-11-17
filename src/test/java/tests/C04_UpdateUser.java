package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import utilities.UserId;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class C04_UpdateUser {

    @Test
    public void updateUser() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Jo");
        payload.put("email", "ususs@example.com");

        Response response = given(ApiUtilities.spec())
                .body(payload)
                .put("/users/" + UserId.id);

        response.then()
                .statusCode(200)
                .body("user.name", equalTo(payload.get("name")))
                .body("user.email", equalTo(payload.get("email")));
    }
}
