package test.api.settings.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import test.api.settings.model.CourierLoginCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends ApiClient {

    public static ValidatableResponse login(String courierLogin, String courierPassword) {
        return given()
                .spec(getBaseSpec())
                .body("{\"login\":\"" + courierLogin + "\","
                        + "\"password\":\"" + courierPassword + "\"}")
                .when()
                .post("/api/v1/courier/login")
                .then();

    }

    public static ValidatableResponse create(String courierLogin, String courierPassword, String courierFirstName) {
        return given()
                .spec(getBaseSpec())
                .body("{\"login\":\"" + courierLogin + "\","
                        + "\"password\":\"" + courierPassword + "\","
                        + "\"firstName\":\"" + courierFirstName + "\"}")
                .when()
                .post("/api/v1/courier")
                .then();

    }

    public static ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete("/api/v1/courier/" + id)
                .then();

    }

    public static ValidatableResponse loginJson(CourierLoginCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }

    public static ValidatableResponse loginBody(String body) {
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post("/api/v1/courier/login")
                .then();

    }

    public static ValidatableResponse createBody(String body) {
        return given().spec(getBaseSpec()).body(body).when().post("/api/v1/courier/").then();
    }

    @Step("Отправка запроса на создание курьера")
    public static ValidatableResponse postCourier(String body) {
        return createBody(body);
    }

    @Step("Отправка запроса на логин (с некорректным телом)")
    public static ValidatableResponse logInBody(String body) {
        return loginBody(body);
    }

    @Step("Отправка запроса на логин")
    public static ValidatableResponse logIn(CourierLoginCredentials credentials) {
        return loginJson(credentials);
    }

    @Step("Удаление созданного курьера")
    public static void deleteCourier(int courierId) {
        CourierClient.delete(courierId).statusCode(200);
    }

    @Step("Отправляем запрос на создание курьера")
    public static ValidatableResponse createCourier(String login, String password, String firstName) {
        return CourierClient.create(login, password, firstName);
    }
}

