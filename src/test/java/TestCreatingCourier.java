import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import test.api.settings.client.CourierBasicRequests;

import static org.junit.Assert.assertEquals;

public class TestCreatingCourier {
    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);

    @Test
    @DisplayName("Создание курьера без поля логин выдаёт ошибку")
    public void courierCreationWithoutLoginShowsError() {
        ValidatableResponse createResponse = postCourier("{\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}");
        assertStatusCode(createResponse, 400);
        assertBody(createResponse);
    }

    @Test
    @DisplayName("Создание курьера без поля пароль выдаёт ошибку")
    public void courierCreationWithoutPasswordShowsError() {
        ValidatableResponse createResponse = postCourier("{\"login\":\"" + courierLogin + "\"," + "\"firstName\":\"" + courierFirstName + "\"}");
        assertStatusCode(createResponse, 400);
        assertBody(createResponse);
    }

    @Test
    @DisplayName("Создание курьера без поля имя выдаёт ошибку")
    public void courierCreationFirstNameIsNullShowsError() {
        ValidatableResponse createResponse = postCourier("{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"}");
        assertStatusCode(createResponse, 400);
        assertBody(createResponse);
    }

    @Step("Отправка запроса на создание курьера")
    public ValidatableResponse postCourier(String body) {
        return CourierBasicRequests.createBody(body);
    }

    @Step("Сравнить код ответа")
    public void assertStatusCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Сравниваем тело ответа")
    public void assertBody(ValidatableResponse createResponse) {
        String responseText = createResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", responseText);
    }
}
