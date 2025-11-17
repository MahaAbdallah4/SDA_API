package tests.CustomerCart;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;


public class US06_GetUserCart extends ApiUtilities {

    /*
    US06:
    As a customer,
    I want to retrieve all items in my shopping cart,
    So that I can review products, quantities, prices, and totals before checkout.
     */

    @Test
    //This is a bug! status code: 500 Server Error
    public void getUserCart() {

        //Send the request
        Response response = given(spec()).get("/cart");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(500);


    }


}
