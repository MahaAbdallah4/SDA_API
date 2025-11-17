package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class C01_CreateUser {

    @Test
    public void createUser() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Johh");
        payload.put("email", "user" + System.currentTimeMillis() + "@example.com");
        payload.put("password", "password");

        Response response = given(ApiUtilities.spec())
                .body(payload)
                .post("/users/create");

        response.then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo(payload.get("name")))
                .body("email", equalTo(payload.get("email")));
    }
}

/*
 * NOTE:
 * The POST /api/users/create endpoint is failing with 500 Internal Server Error.
 * Reason: SMTP authentication failed when sending the verification email.
 * This is a backend issue, not a client-side issue.
 * Other endpoints work correctly (GET, PUT, DELETE).
 */
