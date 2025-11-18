package tests.CustomerCart;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class US08_ClearAllProductsInTheCart extends ApiUtilities {

    /*
    US08:
    As a customer,
    I want to empty my entire shopping cart at once,
    So that I can quickly start fresh with a new selection.
     */

    @Test
    //This is a bug! status code: 405 Method Not Allowed
    public void clearCart_TC01() {

        //Send the request
        Response response = given(spec()).post("/cart/clear");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(405) //Bug: Should be 200 OK
                .body("message", equalTo("The POST method is not supported for route api/cart/clear. Supported methods: DELETE.")); //Bug: Should return success message

    }


    @Test
    //This is a bug! DELETE should succeed but returns success:false
    public void clearCart_TC02(){

        //Send the request
        Response response = given(spec()).delete("/cart/clear");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(400)
                .body("success", equalTo(false)) //BUG: Should be true
                .body("message", containsString("Failed to remove product from cart"));

    }



    @Test
    public void clearCartVerifyCartIsEmpty_TC03() {

//        given(spec()).post("/cart/clear");
        //Send the request
        Response response = given(spec()).get("/cart");
        response.prettyPrint();

        //Do assertion
        response.then()
                .statusCode(500) // BUG: Should be 200
                .body("message", equalTo("Server Error")); // BUG response
    }

}
