package tests.Favorites;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utilities.ObjectMapperUtils.getJsonNode;

public class C10_AddToFavorites extends ApiUtilities {


    /*
    US10:
        As a customer,
        I want to save products to my favorites for future reference,
        So that I can track items I'm interested in without committing to purchase.
    */


    @Test
    public void addProductToFavorites() {

        //Prepare the payload
        JsonNode payload = getJsonNode("addToFavorites");

        //Send the request
        Response response = given(spec())
                .body(payload)
                .post("/favorites/create");

        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(200)
                .body("success", equalTo("Product added favorites successfully!"));
//                .body("message", equalTo("Product added favorites successfully!"));

    }

    @Test
    public void addProductAlreadyInFavorites() {

        //Prepare the payload
        JsonNode payload = getJsonNode("addToFavorites");

        //Send the request
        Response response = given(spec())
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/favorites/create");

        response.prettyPrint();

        //Do assertion
        response
                .then()
                .statusCode(400)
                .body("error", equalTo("Product is already in favorites."));
//                .body("message", equalTo("Product is already in favorites!"));

    }

}
