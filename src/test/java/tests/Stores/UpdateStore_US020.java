package tests.Stores;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import utilities.ApiUtilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static io.restassured.RestAssured.given;

public class UpdateStore_US020 {

    @Test
    public void TC_US020_001_updateStoreWithValidData() throws Exception {

        int storeId = 2085;


        ObjectMapper mapper = new ObjectMapper();


        String jsonString = "{"
                + "\"name\":\"new store\","
                + "\"description\":\"store aaaa\","
                + "\"location\":\"tabuk\","
                + "\"admin_id\":377,"
                + "\"admin_d\":352"
                + "}";

        JsonNode payload = mapper.readTree(jsonString);


        ((ObjectNode) payload).put("name", "Updated_" + System.currentTimeMillis());


        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .put("/stores/" + storeId);

        response.prettyPrint();

        Assert.assertEquals(200, response.statusCode());
    }
}
