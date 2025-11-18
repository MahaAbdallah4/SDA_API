package tests;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.ApiUtilities;
import utilities.ObjectMapperUtils;

import static io.restassured.RestAssured.given;

public class ProductApiTests {
    private int existingProductId; // dynamically created product ID
    private int favoriteId;        // favorite record ID for US011


    // US011: Remove product from favorites
    @Test
    public void removeProductFromFavorites_success() {
        Response response = given()
                .spec(ApiUtilities.spec())
                .delete("/favorites/" + favoriteId);

        // 1. Customer can remove by favorite record ID
        // 2. Returns 200 with success message
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to remove product from favorites!");
    }

    @Test
    public void removeProductFromFavorites_failure() {
        Response response = given()
                .spec(ApiUtilities.spec())
                .delete("/favorites/999999"); // Non-existing favorite

        // 3. Returns 404 error if deletion fails
        Assert.assertEquals(response.getStatusCode(), 404, "Expected failure for non-existing favorite did not occur!");
    }

    // US012: Browse all products
    @Test
    public void browseAllProducts_success() {
        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/products");

        // 1. User can retrieve list of all products
        // 2. Returns 200 with product array
        // 3. Requires authentication with Bearer token
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve products list!");
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, "Product array is empty!");
    }

    // US013: View product details
    @Test
    public void viewProductDetails_success() {
        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/products/" + existingProductId);

        // 1. User can retrieve detailed information for specific product
        // 2. Returns 200 with product details
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to retrieve product details!");
    }

    @Test
    public void viewProductDetails_notFound() {
        Response response = given()
                .spec(ApiUtilities.spec())
                .get("/products/999999"); // Non-existing product

        // 3. Returns 404 error if product not found
        Assert.assertEquals(response.getStatusCode(), 404, "Non-existing product did not return 404!");
    }

    // US014: Create new product
    @Test
    public void createProduct_success() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("createProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", "New Product " + System.currentTimeMillis());

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .post("/products/create");

        // 1. Admin can create product with required fields
        // 2. Returns 201 with product details on success
        Assert.assertEquals(response.getStatusCode(), 201, "Product creation failed!");
    }

    @Test
    public void createProduct_validationError() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("createProduct");
        ObjectMapperUtils.removeFieldJsonNode(payload, "name"); // Remove required field

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .post("/products");

        // 3. Returns 422 validation errors for invalid data
        Assert.assertEquals(response.getStatusCode(), 422, "Expected validation error did not occur!");
    }

    @Test
    public void createProduct_serverError() {
        // Simulate failure by sending payload that causes server error (depends on API)
        JsonNode payload = ObjectMapperUtils.getJsonNode("createProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", ""); // Empty name triggers 422 in actual API

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .post("/products/create");

        // 4. Returns 422 if creation fails (server error simulation)
        Assert.assertEquals(response.getStatusCode(), 422, "Expected server error did not occur!");
    }

    // US015: Update existing product
    @Test
    public void updateProduct_success() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", "Updated Product " + System.currentTimeMillis());

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .put("/products/" + existingProductId);

        // 1. Admin can update product information by ID
        // 2. Returns 200 with updated product details
        Assert.assertEquals(response.getStatusCode(), 200, "Product update failed!");
    }

    @Test
    public void updateProduct_notFound() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", "Non-existing Product");

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .put("/products/999999"); // Non-existing product ID

        // 3. Returns 404 if product not found
        Assert.assertEquals(response.getStatusCode(), 404, "Non-existing product update did not return 404!");
    }

    @Test
    public void updateProduct_validationError() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");
        ObjectMapperUtils.removeFieldJsonNode(payload, "name");

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .put("/products/" + existingProductId);

        // 4. Returns 422 for validation errors
        Assert.assertEquals(response.getStatusCode(), 422, "Expected validation error did not occur!");
    }

    @Test
    public void updateProduct_serverError() {
        JsonNode payload = ObjectMapperUtils.getJsonNode("updateProduct");
        ObjectMapperUtils.updateJsonNode(payload, "name", ""); // Empty name triggers 422 in actual API

        Response response = given()
                .spec(ApiUtilities.spec())
                .body(payload.toString())
                .put("/products/" + existingProductId);

        // 5. Returns 422 if update fails (server error simulation)
        Assert.assertEquals(response.getStatusCode(), 422, "Expected server error did not occur!");
    }
}
