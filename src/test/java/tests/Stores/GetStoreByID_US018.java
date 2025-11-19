package tests.Stores;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;

public class GetStoreByID_US018 {

    @Test
    public void TC_US018_001_getStoreByValidID() {

        int storeId = 2090;

        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/stores/" + storeId);

        response.prettyPrint();

        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    public void TC_US018_002_getStoreWithInvalidID() {

        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/stores/99999");

        response.prettyPrint();

        Assert.assertEquals(404, response.statusCode());
    }
}

