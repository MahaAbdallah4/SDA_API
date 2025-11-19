package tests.Stores;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ApiUtilities;
import utilities.ObjectMapperUtils;
import utilities.StoreId;

import static io.restassured.RestAssured.given;

public class CreateStore_US019 {

    @Test
    public void TC_US019_001_createStoreSuccessfully() {


        var payload = ObjectMapperUtils.getJsonNode("create_store");


        ObjectMapperUtils.updateJsonNode(payload, "name", "AutoStore_" + System.currentTimeMillis());
        ObjectMapperUtils.updateJsonNode(payload, "description", "Best bookstore in town");
        ObjectMapperUtils.updateJsonNode(payload, "location", "Riyadh");
        ObjectMapperUtils.updateJsonNode(payload, "admin_id", 376);


        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .post("/stores/create");

        response.prettyPrint();


        Assert.assertEquals(201, response.statusCode());


        int storeId = response.jsonPath().getInt("product.id");
        StoreId.id = storeId;

        System.out.println("Dynamic Store ID = " + storeId);
    }
}
