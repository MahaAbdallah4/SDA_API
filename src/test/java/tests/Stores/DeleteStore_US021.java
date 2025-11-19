package tests.Stores;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ApiUtilities;

import static io.restassured.RestAssured.given;

public class DeleteStore_US021 {

    @Test
    public void TC_US021_001_verifyAdminCanDeleteStore() {

        int storeId = 2045;

        Response response = given()
                .spec(ApiUtilities.spec())
                .delete("/stores/" + storeId);


        response.prettyPrint();

        Assert.assertEquals(200, response.statusCode());
    }

}

