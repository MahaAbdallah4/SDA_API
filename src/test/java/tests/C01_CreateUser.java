package tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import utilities.UserId;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class C01_CreateUser {

    @Test
    public void prepareUserId() {

        Response response = given(ApiUtilities.spec())
                .get("/users");

        response.prettyPrint();

        assertEquals(response.statusCode(), 200);

        int id = response.jsonPath().getInt("[0].id");
        UserId.id = id;

        System.out.println("Dynamic User ID = " + id);
    }
}





