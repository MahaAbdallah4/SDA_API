package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiUtilities {
    private static String token;

    public static String getToken() {
        if (token == null) {
            Map<String, String> payload = new HashMap<>();
            payload.put("email", ConfigReader.getAdminEmail());
            payload.put("password", ConfigReader.getDefaultPassword());

            Response response = given()
                    .body(payload)
                    .contentType(ContentType.JSON)
                    .post(ConfigReader.getApiBaseUrl() + "/login");

            if (response.getStatusCode() != 200) {
                System.out.println("Failed to authenticate: " + response.getStatusCode() + " - " + response.asString());
                throw new RuntimeException("Failed to get token: " + response.getBody().asString());
            }

            token = response.jsonPath().getString("authorisation.token");
        }
        return token;
    }

    public static void setToken(String authToken) {
        token = authToken;
    }

    public static RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + getToken())
                .build();
    }
}
