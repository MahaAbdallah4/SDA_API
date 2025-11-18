package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiUtilities {
    public static final String BASE_URL = "https://bazaarstores.com/api";

    private static String token= null;

    public static RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("Authorization", "Bearer " + getToken())
                .setContentType(ContentType.JSON)
                .build();
    }


    private static String getToken() {
        if (token == null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", ConfigReader.getAdminEmail());
            payload.put("password", ConfigReader.getDefaultPassword());

            Response response = given()
                    .body(payload)
                    .contentType(ContentType.JSON)
                    .post(BASE_URL + "/login"); // make sure login endpoint is correct

            token = response.jsonPath().getString("authorisation.token"); // adjust path if needed
        }
        return token;
    }

    // Reset token if needed
    public static void clearToken() {
        token = null;
    }
}
