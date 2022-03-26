package testSettings;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierBasicRequests extends RequestSettings {


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
}
