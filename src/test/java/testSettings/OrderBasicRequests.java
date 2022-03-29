package testSettings;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static testSettings.RequestSettings.getBaseSpec;

public class OrderBasicRequests {

    public static ValidatableResponse create(OrdersCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    public static ValidatableResponse delete(int track) {
        return given()
                .spec(getBaseSpec())
                .body("{\"track\":\"" + track + "\"}")
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then();

    }
}
