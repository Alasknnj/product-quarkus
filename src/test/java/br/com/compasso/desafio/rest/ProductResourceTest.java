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
    private static final String VALID_PRODUCT = MOCK_JSON_PATH + "valid-product.json";

    @Inject
    JsonBuilder builder;

    // create successfully
    @Test
    void shouldCreateProduct() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_CREATED)
            .body("$", hasKey("id"))
            .body("name", is(dto.getName()))
            .body("description", is(dto.getDescription()))
            .body("price", is(dto.getPrice().floatValue()));
    }
    // error on missing name
    @Test
    void shouldThrowErrorOnCreateProductBlankName() throws IOException {
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
    void shouldThrowErrorOnCreateProductBlankDescription() throws IOException {
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
    void shouldThrowErrorOnCreateProductBlankPrice() throws IOException {
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
    void shouldThrowErrorOnCreateProductPriceNegative() throws IOException {
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
    void shouldThrowErrorOnCreateProductPriceZero() throws IOException {
        ProductDTO dto = builder.fromJsonFile(VALID_PRODUCT, ProductDTO.class);
        dto.setPrice(BigDecimal.valueOf(0));
        given().contentType(ContentType.JSON)
            .body(builder.toJson(dto))
            .post(ENDPOINT_CREATE)
            .then().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("status_code", is(HttpStatus.SC_BAD_REQUEST))
            .body("message", containsString("price must be positive"));
    }

    // update successfully
    // error on id not found
    // error on missing name
    // error on missing description
    // error on missing price
    // error on negative price
    // error on zero price

    // get by id successfully
    // error on id not found

    // get all products successfully
    // get no products with empty database

    // get products with query (q, min, max)

    // delete successfully
    // error on id not found

    // error on json mappings
}
