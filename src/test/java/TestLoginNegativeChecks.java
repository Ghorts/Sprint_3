import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.api.settings.client.CourierClient;
import test.api.settings.client.ScooterRegisterCourier;
import test.api.settings.model.CourierLoginCredentials;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;

public class TestLoginNegativeChecks {
    ArrayList<String> loginPass;
    int courierId;
    CourierClient requests;
    CourierLoginCredentials credentials;

    @Before
    public void setUp() {
        loginPass = ScooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
    }

    @After
    public void tearDown() {
        CourierLoginCredentials trueCredentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        courierId = requests.loginJson(trueCredentials).extract().path("id");
        requests.delete(courierId).statusCode(200);
    }

    @Test
    @DisplayName("Лог-ин без поля логин возвращает ошибку")
    public void courierLoginWithoutLoginShowsError() {
        CourierClient.logInBody("{\"password\":\"" + loginPass.get(1) + "\"}")
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Лог-ин без поля пароль возвращает ошибку")
    public void courierLoginWithoutPasswordShowsError() {
        CourierClient.logInBody("{\"login\":\"" + loginPass.get(0) + "\"}")
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Лог-ин с неверным логином возвращает ошибку")
    public void courierLoginWithWrongLoginShowsError() {
        credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        CourierClient.logIn(credentials)
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Лог-ин с неверным паролем возвращает ошибку")
    public void courierLoginWithWrongPasswordShowsError() {
        credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(0));
        CourierClient.loginJson(credentials)
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Лог-ин с несуществующим логином возвращает ошибку")
    public void courierLoginWithNoneExistingLogin() {
        credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        CourierClient.logIn(credentials)
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
