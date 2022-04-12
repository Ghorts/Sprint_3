import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.api.settings.client.CourierClient;
import test.api.settings.client.ScooterRegisterCourier;
import test.api.settings.model.CourierLoginCredentials;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TestLoginPositiveChecks {
    ScooterRegisterCourier register;
    ArrayList<String> loginPass;
    int courierId;
    ValidatableResponse loginResponse;

    @Before
    public void setUp() {
        loginPass = register.registerNewCourierAndReturnLoginPassword();
    }

    @After
    public void tearDown() {
        courierId = loginResponse.extract().path("id");
        CourierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Успешный лог-ин с валидными данными")
    public void courierLoginWithValidCredentialsSuccessful() {
        CourierLoginCredentials credentials = new CourierLoginCredentials(loginPass.get(0), loginPass.get(1));
        loginResponse = CourierClient.logIn(credentials)
                .statusCode(200)
                .and()
                .assertThat().body("id", is(not(0)));
    }
}
