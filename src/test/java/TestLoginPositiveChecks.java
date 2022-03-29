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
import static org.hamcrest.Matchers.*;
import static testSettings.RequestSettings.getBaseSpec;

public class TestLoginPositiveChecks {
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
        requests.delete(courierId);
    }

    @Test
    @DisplayName("Успешный лог-ин с валидными данными")
    public void courierLoginWithValidCredentialsSuccessful() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        ValidatableResponse loginResponse = requests.loginJson(credentials).statusCode(200).and().body("id", is(not(0)));
        courierId = loginResponse.extract().path("id");

    }

}
