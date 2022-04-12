package test.api.settings.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import test.api.settings.model.OrdersCredentials;

import static io.restassured.RestAssured.given;

public class OrderClient extends ApiClient {

    public static ValidatableResponse create(OrdersCredentials credentials) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    public static ValidatableResponse delete(int track) {
        return RestAssured.given()
                .spec(getBaseSpec())
                .body("{\"track\":\"" + track + "\"}")
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then();
    }

    public static Response getOrdersLimit(int limit) {
        return given()
                .spec(getBaseSpec())
                .when()
                .get("/api/v1/orders?limit=" + limit);
    }

    @Step("Отправляем запрос на создание заказа")
    public static ValidatableResponse orderCreate(OrdersCredentials credentials) {
        return OrderClient.create(credentials);
    }

    @Step("Удаление созданного заказа")
    public static void deleteCourier(int track) {
        OrderClient.delete(track).statusCode(200);
    }

    @Step("Отправка запроса на получение списка заказов")
    public static Response getOrders(int limit) {
        return OrderClient.getOrdersLimit(limit);
    }
}
