package settings.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import settings.model.OrdersCredentials;
import settings.utils.RequestSettings;

import static io.restassured.RestAssured.given;

public class OrderBasicRequests {

    public static ValidatableResponse create(OrdersCredentials credentials) {
        return RestAssured.given()
                .spec(RequestSettings.getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    public static ValidatableResponse delete(int track) {
        return RestAssured.given()
                .spec(RequestSettings.getBaseSpec())
                .body("{\"track\":\"" + track + "\"}")
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then();

    }

    public static Response getOrdersLimit(int limit) {
        return given()
                .spec(RequestSettings.getBaseSpec())
                .when()
                .get("/api/v1/orders?limit=" + limit);
    }
}
