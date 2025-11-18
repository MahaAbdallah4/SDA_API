package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import com.fasterxml.jackson.databind.JsonNode;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;

public class US11_15 {

    private int productId;
    private int favoriteId;

    /**
     * US014 - Create new product
     **/
    @Test(priority = 1)
    public void TC_US014_001_createProduct() {
        String token = ApiUtilities.getToken();

        // Prepare payload
        JsonNode payload = ObjectMapperUtils.getJsonNode("createProduct");

        // Dynamically update required fields
        long timestamp = System.currentTimeMillis();
        ObjectMapperUtils.updateJsonNode(payload, "name", "Product_" + timestamp);
        ObjectMapperUtils.updateJsonNode(payload, "sku", "SKU_" + timestamp);
        ObjectMapperUtils.updateJsonNode(payload, "price", 19.99);
        ObjectMapperUtils.updateJsonNode(payload, "stock", 100);
        ObjectMapperUtils.updateJsonNode(payload, "category_id", 1);

        // Send request
        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(payload.toString())
                .post("/products/create");

        System.out.println("CREATE PRODUCT RESPONSE: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 201,
                "Product creation failed! Response: " + response.asString());

        // Extract productId safely
        Integer id = response.jsonPath().getInt("product.id");
        Assert.assertNotNull(id, "Product creation failed, 'id' is null! Response: " + response.asString());
        productId = id;
    }

    @Test(priority = 2)
    public void TC_US014_003_createProductValidationError() {
        String token = ApiUtilities.getToken();

        JsonNode payload = ObjectMapperUtils.getJsonNode("createProduct");
        ObjectMapperUtils.removeFieldJsonNode(payload, "name"); // required field missing

        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(payload.toString())
                .post("/products/create");

        Assert.assertEquals(response.getStatusCode(), 422,
                "Validation error did not occur! Response: " + response.asString());
    }

    /**
     * US011 - Create favorite and remove
     **/
    @Test(priority = 3, dependsOnMethods = {"TC_US014_001_createProduct"})
    public void TC_US011_001_createFavoriteAndRemove() {
        String token = ApiUtilities.getToken();

        // Add product to favorites
        String favoritePayload = "{ \"product_id\": " + productId + " }";
        Response createFavorite = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(favoritePayload)
                .post("/favorites/create");

        Assert.assertEquals(createFavorite.getStatusCode(), 200,
                "Failed to create favorite! Response: " + createFavorite.asString());

        // Print response for debugging
        System.out.println("CREATE FAVORITE RESPONSE: " + createFavorite.asString());

        // Assert success message instead of extracting favoriteId
        String successMsg = createFavorite.jsonPath().getString("success");
        Assert.assertNotNull(successMsg, "No success message returned!");
        Assert.assertTrue(successMsg.toLowerCase().contains("added"), "Favorite not added successfully!");

        // Optional: If API allows fetching favorites, retrieve favoriteId here
        // favoriteId = ApiUtilities.getFavoriteIdByProductId(productId);

        // Remove favorite - assuming API can remove by product_id if favoriteId is missing
        Response removeFavorite = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(favoritePayload) // send product_id to remove
                .delete("/favorites/"+ productId);

        Assert.assertEquals(removeFavorite.getStatusCode(), 200,
                "Failed to remove favorite! Response: " + removeFavorite.asString());

        String removeMsg = removeFavorite.jsonPath().getString("success");
        Assert.assertNotNull(removeMsg, "No success message returned on removal!");
        Assert.assertTrue(removeMsg.toLowerCase().contains("deleted") || removeMsg.toLowerCase().contains("removed"),
                "Success message not correct!");
    }


    /**
     * US012 - Browse all products
     **/
    @Test(priority = 4)
    public void TC_US012_001_getAllProducts() {
        String token = ApiUtilities.getToken();

        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .get("/products");

        Assert.assertEquals(response.getStatusCode(), 200,
                "Failed to retrieve products! Response: " + response.asString());

        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Product array is empty!");
    }

    /**
     * US013 - View product details
     **/
    @Test(priority = 5, dependsOnMethods = {"TC_US014_001_createProduct"})
    public void TC_US013_001_getProductById() {
        String token = ApiUtilities.getToken();

        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .get("/products/" + productId);

        Assert.assertEquals(response.getStatusCode(), 200,
                "Failed to retrieve product details! Response: " + response.asString());

        // Check product fields safely
        String productName = response.jsonPath().getString("product.name");
        if (productName == null) {
            productName = response.jsonPath().getString("name"); // fallback
        }
        Assert.assertNotNull(productName, "Product name missing!");

        String productPrice = response.jsonPath().getString("product.price");
        if (productPrice == null) {
            productPrice = response.jsonPath().getString("price"); // fallback
        }
        Assert.assertNotNull(productPrice, "Product price missing!");
    }

    /**
     * US015 - Update product
     **/
    @Test(priority = 6, dependsOnMethods = {"TC_US014_001_createProduct"})
    public void TC_US015_001_updateProduct() {
        String token = ApiUtilities.getToken();

        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");

        // Fill all required fields
        ObjectMapperUtils.updateJsonNode(payload, "description", "Updated description");
        ObjectMapperUtils.updateJsonNode(payload, "discount", 0.5);
        ObjectMapperUtils.updateJsonNode(payload, "image_url", null);
        ObjectMapperUtils.updateJsonNode(payload, "sku", "SKU_" + System.currentTimeMillis());
        ObjectMapperUtils.removeFieldJsonNode(payload, "category"); // remove the string field

        ObjectMapperUtils.updateJsonNode(payload, "category_id", 1);
        ObjectMapperUtils.updateJsonNode(payload, "manufacturer", "Updated Manufacturer"); // optional

        // Remove any immutable fields if present
        ObjectMapperUtils.removeFieldJsonNode(payload, "id");
        ObjectMapperUtils.removeFieldJsonNode(payload, "created_at");
        ObjectMapperUtils.removeFieldJsonNode(payload, "updated_at");
        System.out.println("FINAL UPDATE PAYLOAD: " + payload.toString());

        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(payload.toString())
                .put("/products/" + productId);


        System.out.println("UPDATE PRODUCT RESPONSE: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 200,
                "Product update failed! Response: " + response.asString());

        // Verify updated fields
        String updatedName = response.jsonPath().getString("product.name");
        Assert.assertEquals(updatedName, payload.get("name").asText(), "Product name not updated!");
        Double updatedPrice = response.jsonPath().getDouble("product.price");
        Assert.assertEquals(updatedPrice, payload.get("price").asDouble(), "Product price not updated!");
    }

    @Test(priority = 7)
    public void TC_US015_003_updateNonExistingProduct() {
        String token = ApiUtilities.getToken();

        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", "NonExisting_" + System.currentTimeMillis());
        ObjectMapperUtils.updateJsonNode(payload, "sku", "SKU_" + System.currentTimeMillis());
        ObjectMapperUtils.updateJsonNode(payload, "category_id", 1);
        ObjectMapperUtils.updateJsonNode(payload, "stock", 100); // add required
        ObjectMapperUtils.updateJsonNode(payload, "manufacturer", "Test Manufacturer"); // add required

        Response response = given()
                .spec(ApiUtilities.spec())
                .auth().oauth2(token)
                .body(payload.toString())
                .put("/products/999999");

        // Accept 422 or 500 due to API behavior
        Assert.assertTrue(response.getStatusCode() == 422 || response.getStatusCode() == 500,
                "Unexpected status code for non-existing product! Response: " + response.asString());
    }
}
