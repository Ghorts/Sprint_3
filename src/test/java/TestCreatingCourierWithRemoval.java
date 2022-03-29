import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import test.api.settings.client.CourierBasicRequests;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class TestCreatingCourierWithRemoval {
    int id;
    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);
    ValidatableResponse response;

    @After
    public void logIn() {
        response = CourierBasicRequests.login(courierLogin, courierPassword);
        id = response.extract().path("id");
        CourierBasicRequests.delete(id).statusCode(200);
    }

    @Test
    @DisplayName("Создание курьера с валидными данными успешно")
    public void createNewCourierWithValidCredentialsSuccessful() {
        ValidatableResponse response = createCourier(courierLogin, courierPassword, courierFirstName).assertThat().body("ok", equalTo(true));
        assertStatusCode(response, 201);

    }

    @Test
    @DisplayName("Создание курьера с одинаковыми данными возвращает ошибку")
    public void createCourierWithSameCredentialsShowsError() {
        ValidatableResponse response = createCourier(courierLogin, courierPassword, courierFirstName);
        assertStatusCode(response, 201);
        ValidatableResponse createAnotherCourier = createCourier(courierLogin, courierPassword, courierFirstName);
        assertStatusCode(createAnotherCourier, 409);
        assertBody(createAnotherCourier);
    }

    @Test
    @DisplayName("Создание курьера с сущетсвующим логином возвращает ошибку")
    public void createCourierWithExistingLoginShowsError() {
        ValidatableResponse response = createCourier(courierLogin, courierPassword, courierFirstName);
        assertStatusCode(response, 201);
        ValidatableResponse createResponse = createCourier(courierLogin, "password", "name");
        assertStatusCode(createResponse, 409);
        assertBody(createResponse);
    }


    @Step("Отправляем запрос на создание курьера")
    public ValidatableResponse createCourier(String login, String password, String firstName) {
        return CourierBasicRequests.create(login, password, firstName);
    }

    @Step("Сравниваем код ответа")
    public void assertStatusCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Сравниваем тело ответа")
    public void assertBody(ValidatableResponse createResponse) {
        String responseText = createResponse.extract().path("message");
        assertEquals("Создание курьера с сущетсвующим логином выполнено без ошибки", "Этот логин уже используется. Попробуйте другой.", responseText);
    }
}
