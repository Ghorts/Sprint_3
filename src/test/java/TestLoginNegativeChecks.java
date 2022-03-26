import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testSettings.CourierBasicRequests;
import testSettings.CourierLoginCredentials;
import testSettings.scooterRegisterCourier;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static testSettings.RequestSettings.getBaseSpec;

public class TestLoginNegativeChecks {
    scooterRegisterCourier register = new scooterRegisterCourier();
    ArrayList<String> loginPass;
    int courierId;
    CourierBasicRequests requests;


    @Before
    public void setUp() {
        loginPass = register.registerNewCourierAndReturnLoginPassword();
    }

    @After
    public void tearDown() {
        //Удаление созданного курьера
        CourierLoginCredentials trueCredentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        ValidatableResponse loginResponse = requests.loginJson(trueCredentials);
        courierId = loginResponse.extract().path("id");
        requests.delete(courierId);
    }

    @Test
    @DisplayName("Лог-ин без поля логин возвращает ошибку")
    public void courierLoginWithoutLoginShowsError() {
        System.out.println(loginPass.get(1));
        given()
                .spec(getBaseSpec())
                .body("{\"password\":\"" + loginPass.get(1) + "\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Лог-ин без поля пароль возвращает ошибку")
    public void courierLoginWithoutPasswordShowsError() {
        given()
                .spec(getBaseSpec())
                .body("{\"login\":\"" + loginPass.get(0) + "\"}")
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Лог-ин с неверным логином возвращает ошибку")
    public void courierLoginWithWrongLoginShowsError() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        requests.loginJson(credentials).statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Лог-ин с неверным паролем возвращает ошибку")
    public void courierLoginWithWrongPasswordShowsError() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(0));
        requests.loginJson(credentials).statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Лог-ин с несуществующим логином возвращает ошибку")
    public void courierLoginWithNoneExistingLogin() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(1), loginPass.get(1));
        requests.loginJson(credentials).statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }
}
