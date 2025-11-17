package tests.CustomerCart;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class US07_DeleteProductFromCart extends ApiUtilities {

        /*
        US07:
        As a customer,
        I want to remove a specific product from my cart,
        So that I can adjust my purchase selections before checkout.
         */

    int productId = 398;
    int invalidProductId = 99999;

    @Test
    public void deleteProductFromCart() {

        //Send the request
        Response response = given(spec()).delete("/cart/" + productId);
        response.prettyPrint();


        //Do assertion
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("message", equalTo("Product removed from cart successfully"));

    }


    @Test
    // This is a bug! API should return 400 for invalid product ID, but returns success true with 200 OK
    public void deleteProductFromCartInvalidId() {

        //Send the request
        Response response = given(spec()).delete("/cart/" + invalidProductId);
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200) //Bug: Should be 400
                .body("success", equalTo(true)) //Bug: Should be false
                .body("message", equalTo("Product removed from cart successfully"));
    }

}
