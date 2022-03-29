import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.api.settings.client.CourierBasicRequests;
import test.api.settings.client.ScooterRegisterCourier;
import test.api.settings.model.CourierLoginCredentials;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TestLoginPositiveChecks {
    ScooterRegisterCourier register = new ScooterRegisterCourier();
    ArrayList<String> loginPass;
    int courierId;
    CourierBasicRequests requests;

    @Before
    public void setUp() {
        createCourier();
    }

    @After
    public void tearDown() {
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Успешный лог-ин с валидными данными")
    public void courierLoginWithValidCredentialsSuccessful() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        ValidatableResponse loginResponse = logIn(credentials);
        assertStatusCode(loginResponse, 200);
        assertBody(loginResponse);
        courierId = loginResponse.extract().path("id");

    }

    @Step("Регистрация курьера")
    public void createCourier() {
        loginPass = register.registerNewCourierAndReturnLoginPassword();
    }

    @Step("Удаление созданного курьера")
    public void deleteCourier(int courierId) {
        CourierBasicRequests.delete(courierId).statusCode(200);
    }

    @Step("Отправка запроса на логин")
    public ValidatableResponse logIn(CourierLoginCredentials credentials) {
        return requests.loginJson(credentials);
    }

    @Step("Сравниваем код ответа")
    public void assertStatusCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Проверяем тело ответа")
    public void assertBody(ValidatableResponse response) {
        response.assertThat().body("id", is(not(0)));
    }
}
