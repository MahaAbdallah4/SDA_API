package tests.Products;

import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ShoppingCartTests {


    @Test
    public void TC_US005_001() {
        String payload = "{ \"product_id\": 19, \"quantity\": 1 }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/cart/add");
        response.prettyPrint();
    }

    @Test
    public void TC_US005_002() {
        String payload = "{ \"product_id\": 9999, \"quantity\": 1 }";
        Response response = given(ApiUtilities.spec()).body(payload).post("/cart/add");
        response.prettyPrint();
    }
}