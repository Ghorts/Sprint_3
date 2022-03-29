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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class TestLoginNegativeChecks {
    ScooterRegisterCourier register = new ScooterRegisterCourier();
    ArrayList<String> loginPass;
    int courierId;
    CourierBasicRequests requests;
    CourierLoginCredentials credentials;


    @Before
    public void setUp() {
        createCourier();
    }

    @After
    public void tearDown() {
        deleteCourier();
    }

    @Test
    @DisplayName("Лог-ин без поля логин возвращает ошибку")
    public void courierLoginWithoutLoginShowsError() {
        ValidatableResponse response = logInBody("{\"password\":\"" + loginPass.get(1) + "\"}");
        assertStatusCode(response, 400);
        ;
        assertBody(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Лог-ин без поля пароль возвращает ошибку")
    public void courierLoginWithoutPasswordShowsError() {
        ValidatableResponse response = logInBody("{\"login\":\"" + loginPass.get(0) + "\"}");
        assertStatusCode(response, 400);
        assertBody(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Лог-ин с неверным логином возвращает ошибку")
    public void courierLoginWithWrongLoginShowsError() {
        credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        ValidatableResponse response = logIn(credentials);
        assertStatusCode(response, 404);
        assertBody(response, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Лог-ин с неверным паролем возвращает ошибку")
    public void courierLoginWithWrongPasswordShowsError() {
        credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(0));

        CourierBasicRequests.loginJson(credentials).statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Лог-ин с несуществующим логином возвращает ошибку")
    public void courierLoginWithNoneExistingLogin() {
        credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        ValidatableResponse response = logIn(credentials);
        assertStatusCode(response, 404);
        assertBody(response, "Учетная запись не найдена");
    }

    @Step("Отправка запроса на логин (с некорректным телом)")
    public ValidatableResponse logInBody(String body) {
        return CourierBasicRequests.loginBody(body);
    }

    @Step("Отправка запроса на логин")
    public ValidatableResponse logIn(CourierLoginCredentials credentials) {
        return requests.loginJson(credentials);
    }

    @Step("Сравниваем код ответа")
    public void assertStatusCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Сравниваем тело ответа")
    public void assertBody(ValidatableResponse createResponse, String message) {
        String responseText = createResponse.extract().path("message");
        assertEquals(message, responseText);
    }

    @Step("Регистрация курьера")
    public void createCourier() {
        loginPass = register.registerNewCourierAndReturnLoginPassword();
    }

    @Step("Удаление созданного курьера")
    public void deleteCourier() {
        CourierLoginCredentials trueCredentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        courierId = requests.loginJson(trueCredentials).extract().path("id");
        requests.delete(courierId).statusCode(200);
    }
}
