import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static testSettings.RequestSettings.getBaseSpec;

public class TestCreatingCourier {
    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);

    @Test
    @DisplayName("Создание курьера без поля логин выдаёт ошибку")
    public void courierCreationWithoutLoginShowsError() {
        ValidatableResponse createResponse = given()
                .spec(getBaseSpec())
                .body("{\"password\":\"" + courierPassword + "\","
                        + "\"firstName\":\"" + courierFirstName + "\"}")
                .when()
                .post("/api/v1/courier")
                .then();
        createResponse.statusCode(400);
        String responseText = createResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", responseText);
    }

    @Test
    @DisplayName("Создание курьера без поля пароль выдаёт ошибку")
    public void courierCreationWithoutPasswordShowsError() {
        ValidatableResponse createResponse = given()
                .spec(getBaseSpec())
                .body("{\"login\":\"" + courierLogin + "\","
                        + "\"firstName\":\"" + courierFirstName + "\"}")
                .when()
                .post("/api/v1/courier")
                .then();
        createResponse.statusCode(400);
        String responseText = createResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", responseText);
    }

    @Test
    @DisplayName("Создание курьера без поля имя выдаёт ошибку")
    public void courierCreationFirstNameIsNullSuccessful() {
        ValidatableResponse createResponse = given()
                .spec(getBaseSpec())
                .body("{\"login\":\"" + courierLogin + "\","
                        + "\"password\":\"" + courierPassword + "\"}")
                .when()
                .post("/api/v1/courier")
                .then();
        createResponse.statusCode(400);
        String responseText = createResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", responseText);
    }
}
