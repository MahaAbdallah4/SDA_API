package tests.Stores;


import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ApiUtilities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class GetAllStores_US017 {

    @Test
    public void TC_US017_001_getAllStores() {

        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/stores");

        response.prettyPrint();

        Assert.assertEquals(200, response.statusCode());

        List<Map<String, Object>> stores = response.jsonPath().getList("$");
        Assert.assertTrue("Stores list shoud not be empty", stores.size()>0);
    }
}

