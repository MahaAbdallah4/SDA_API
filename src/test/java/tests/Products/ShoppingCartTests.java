package tests.Products;

import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import io.restassured.response.Response;

public class ShoppingCartTests {

    @BeforeClass
    public void setup() {
        String token = ApiUtilities.getToken();
        ApiUtilities.setToken(token);
    }

    @Test
    public void TC_US005_001() {
        String payload = "{ \"product_id\": 123, \"quantity\": 1 }";
        RequestSpecification requestSpec = ApiUtilities.spec().body(payload);
        Response response = requestSpec.post("/api/cart/add");
        System.out.println("Request Body: " + payload);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected status code 200 but got " + response.getStatusCode());
    }

    @Test
    public void TC_US005_002() {
        String payload = "{ \"product_id\": 999, \"quantity\": 1 }";
        RequestSpecification requestSpec = ApiUtilities.spec().body(payload);
        Response response = requestSpec.post("/api/cart/add");
        System.out.println("Request Body: " + payload);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());
        Assert.assertEquals(response.getStatusCode(), 400,
                "Expected status code 400 but got " + response.getStatusCode());
        Assert.assertTrue(response.body().asString().contains("product cannot be added"),
                "Response should indicate that the product cannot be added.");
    }
}