package tests.Favorites;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static utilities.ApiUtilities.spec;

public class US09_GetFavoriteProducts {

    /*
    US09:
        As a customer,
        I want to save products to my wishlist for future reference,
        So that I can track items I'm interested in without committing to purchase.
     */


    @Test
    public void getFavoriteProducts() {

        //Send the request
        Response response = given(spec()).get("/favorites");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200)
                .body("$", not(empty()));

    }

    @Test
    public void validateFavoriteRecordFields(){

        //Send the request
        Response response = given(spec()).get("/favorites");
        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200)
                .body(
                        "[0].id", notNullValue(),
                        "[0].user_id", notNullValue(),
                        "[0].product_id", notNullValue(),
                        "[0].created_at", notNullValue(),
                        "[0].updated_at", notNullValue()
                );

    }

}
