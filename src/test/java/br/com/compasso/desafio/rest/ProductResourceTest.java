package br.com.compasso.desafio.rest;

import br.com.compasso.desafio.TestCointainerPostgreSQL;
import br.com.compasso.desafio.rest.dto.ProductDTO;
import br.com.compasso.desafio.rest.resource.ProductResource;
import br.com.compasso.desafio.service.json.JsonBuilder;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(ProductResource.class)
@QuarkusTestResource(TestCointainerPostgreSQL.class)
class ProductResourceTest {
    private static final String ENDPOINT_CREATE = "";
    private static final String ENDPOINT_UPDATE = "%s";
    private static final String ENDPOINT_GET_ID = "%s";
    private static final String ENDPOINT_GET_ALL = "";
    private static final String ENDPOINT_GET_SEARCH = "search";
    private static final String ENDPOINT_DELETE = "%s";

    private static final String MOCK_JSON_PATH = "src/test/resources/mock-json/";
    private static final String PRODUCT_TEST_LOAD = MOCK_JSON_PATH + "test-load.json";
    private static final String VALID_PRODUCT = MOCK_JSON_PATH + "valid-product.json";
    private static final String VALID_PRODUCT_UPDATE = MOCK_JSON_PATH + "valid-product-update.json";
    private static final String INVALID_PRODUCT = MOCK_JSON_PATH + "invalid-product.json";

    private static final String PRODUCT_TEST_LOAD_ID = "e7442b1a-077b-4e44-b671-cb6c79238d71";
    private static final String PRODUCT_UPDATABLE_ID = "08290ab4-aa35-4dee-9a80-e777189db255";
    private static final String PRODUCT_NONEXISTENT_ID = "77bab5b5-f9d4-4bb3-89ec-893a1f596bd3";

    private static final String PRODUCT_NAME_QUERY = "produto tipo";
    private static final String PRODUCT_DESC_QUERY = "qualidade";
    private static final String PRODUCT_NAME_QUERY_A = "Tipo A";

    private static final BigDecimal PRODUCT_MIN_PRICE_QUERY = BigDecimal.valueOf(2.20);
    private static final BigDecimal PRODUCT_MAX_PRICE_QUERY = BigDecimal.valueOf(6.20);

    @Inject
    JsonBuilder builder;

    // create successfully
    // delete successfully
    @Test
    void shouldCreateProduct() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        String id = given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_CREATED)
            .body("$", hasKey("id"))
            .body("name", is(dto.getName()))
            .body("description", is(dto.getDescription()))
            .body("price", is(dto.getPrice().floatValue()))
            .extract().path("id");

        given().delete(String.format(ENDPOINT_DELETE, id))
            .then().statusCode(HttpStatus.SC_OK);
    }

    // error on missing name
    @Test
    void shouldFailOnCreateProductBlankName() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setName(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("name is mandatory"));
    }

    // error on missing description
    @Test
    void shouldFailOnCreateProductBlankDescription() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setDescription(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("description is mandatory"));
    }

    // error on missing price
    @Test
    void shouldFailOnCreateProductBlankPrice() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price is mandatory"));
    }

    // error on negative price
    @Test
    void shouldFailOnCreateProductPriceNegative() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(BigDecimal.valueOf(-10));
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST).log().all()
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price must be positive"));
    }

    // error on zero price
    @Test
    void shouldFailOnCreateProductPriceZero() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(BigDecimal.ZERO);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price must be positive"));
    }

    // update successfully
    @Test
    void shouldUpdateProduct() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT_UPDATE, ProductDTO.class);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_UPDATABLE_ID))
            .then().statusCode(HttpStatus.SC_OK)
            .body("id", is(PRODUCT_UPDATABLE_ID))
            .body("name", is(dto.getName()))
            .body("description", is(dto.getDescription()))
            .body("price", is(dto.getPrice().floatValue()));
    }

    // error on id not found
    @Test
    void shouldFailOnProductUpdateNonexisting() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT_UPDATE, ProductDTO.class);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_NONEXISTENT_ID))
            .then().statusCode(HttpStatus.SC_NOT_FOUND)
            .body("status_code", is(HttpStatus.SC_NOT_FOUND))
            .body("message", containsString("he product with id " + PRODUCT_NONEXISTENT_ID + " was not found"));
    }

    // error on missing name
    @Test
    void shouldFailOnProductUpdateBlankName() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setName(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("name is mandatory"));
    }

    // error on missing description
    @Test
    void shouldFailOnProductUpdateBlankDescription() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setDescription(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("description is mandatory"));
    }

    // error on missing price
    @Test
    void shouldFailOnProductUpdateBlankprice() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(null);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price is mandatory"));
    }
    // error on negative price
    @Test
    void shouldFailOnProductUpdateNegativePrice() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(BigDecimal.valueOf(-10));
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price must be positive"));
    }

    // error on zero price
    @Test
    void shouldFailOnProductUpdateZeroPrice() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(BigDecimal.ZERO);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price must be positive"));
    }

    // get by id successfully
    @Test
    void shouldGetProductById() throws IOException {
        ProductDTO dto = builder.fromJsonFile(PRODUCT_TEST_LOAD, ProductDTO.class);

        given().get(String.format(ENDPOINT_GET_ID, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_OK)
            .body("id", is(PRODUCT_TEST_LOAD_ID))
            .body("name", is(dto.getName()))
            .body("description", is(dto.getDescription()))
            .body("price", is(dto.getPrice().floatValue()));
    }

    // error on id not found
    @Test
    void shouldFailOnGetProductByIdNotFound() {
        given().get(String.format(ENDPOINT_GET_ID, PRODUCT_NONEXISTENT_ID))
            .then().statusCode(HttpStatus.SC_NOT_FOUND)
            .body("status_code", is(HttpStatus.SC_NOT_FOUND))
            .body("message", containsString("The product with id " + PRODUCT_NONEXISTENT_ID + " was not found"));
    }

    // get all products successfully
    @Test
    void shouldGetAllProducts() {
        given().get(ENDPOINT_GET_ALL)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(8));
    }

    // get products with query (q, min, max)
    @Test
    void shouldSearchProductsNoParam() {
        given().get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(8));
    }

    @Test
    void shouldSearchProductsQ() {
        given().queryParam("q", PRODUCT_NAME_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(6))
            .body("name", everyItem(containsString("Produto Tipo")));

        given().queryParam("q", PRODUCT_DESC_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(6))
            .body("description", everyItem(containsString("Qualidade")));
    }

    @Test
    void shouldSearchProductsMinPrice() {
        given().queryParam("min_price", PRODUCT_MIN_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(4))
            .body("price", everyItem(greaterThanOrEqualTo(PRODUCT_MIN_PRICE_QUERY.floatValue())));
    }

    @Test
    void shouldSearchProductsMaxPrice() {
        given().queryParam("max_price", PRODUCT_MAX_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(6))
            .body("price", everyItem(lessThanOrEqualTo(PRODUCT_MAX_PRICE_QUERY.floatValue())));
    }

    @Test
    void shouldSearchProductsQMinPrice() {
        given().queryParam("q", PRODUCT_NAME_QUERY_A)
            .queryParam("min_price", PRODUCT_MIN_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(2))
            .body("name", everyItem(containsString(PRODUCT_NAME_QUERY_A)))
            .body("price", everyItem(greaterThanOrEqualTo(PRODUCT_MIN_PRICE_QUERY.floatValue())));
    }

    @Test
    void shouldSearchProductsQMaxPrice() {
        given().queryParam("q", PRODUCT_NAME_QUERY_A)
            .queryParam("max_price", PRODUCT_MAX_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(2))
            .body("name", everyItem(containsString(PRODUCT_NAME_QUERY_A)))
            .body("price", everyItem(lessThanOrEqualTo(PRODUCT_MAX_PRICE_QUERY.floatValue())));
    }

    @Test
    void shouldSearchProductsQMinPriceMaxPrice() {
        given().queryParam("q", PRODUCT_NAME_QUERY_A)
            .queryParam("min_price", PRODUCT_MIN_PRICE_QUERY)
            .queryParam("max_price", PRODUCT_MAX_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("size()", is(1))
            .body("name", everyItem(containsString(PRODUCT_NAME_QUERY_A)))
            .body("price", everyItem(greaterThanOrEqualTo(PRODUCT_MIN_PRICE_QUERY.floatValue())))
            .body("price", everyItem(lessThanOrEqualTo(PRODUCT_MAX_PRICE_QUERY.floatValue())));
    }

    @Test
    void shouldSearchProductsMinPriceMaxPrice() {
        given().queryParam("min_price", PRODUCT_MIN_PRICE_QUERY)
            .queryParam("max_price", PRODUCT_MAX_PRICE_QUERY)
            .get(ENDPOINT_GET_SEARCH)
            .then().statusCode(HttpStatus.SC_OK)
            .body("price", everyItem(greaterThanOrEqualTo(PRODUCT_MIN_PRICE_QUERY.floatValue())))
            .body("price", everyItem(lessThanOrEqualTo(PRODUCT_MAX_PRICE_QUERY.floatValue())));
    }

    // error on id not found
    @Test
    void shouldFailOnDeleteProductByIdNotFound() {
        given().delete(String.format(ENDPOINT_DELETE, PRODUCT_NONEXISTENT_ID))
            .then().statusCode(HttpStatus.SC_NOT_FOUND)
            .body("status_code", is(HttpStatus.SC_NOT_FOUND))
            .body("message", containsString("The product with id " + PRODUCT_NONEXISTENT_ID + " was not found"));
    }

    @Test
    void shouldFailCreateProductInvalidPrice() throws IOException {
        given().contentType(ContentType.JSON)
            .body(Files.readString(Paths.get(INVALID_PRODUCT)))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("The value ABC.DE is not valid for type " + BigDecimal.class));
    }

    @Test
    void shouldFailUpdateProductInvalidPrice() throws IOException{
        given().contentType(ContentType.JSON)
            .body(Files.readString(Paths.get(INVALID_PRODUCT)))
            .put(String.format(ENDPOINT_UPDATE, PRODUCT_TEST_LOAD_ID))
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("The value ABC.DE is not valid for type " + BigDecimal.class));
    }
}
