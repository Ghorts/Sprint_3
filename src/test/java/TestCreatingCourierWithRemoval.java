import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import test.api.settings.client.CourierClient;

import static org.hamcrest.Matchers.equalTo;

public class TestCreatingCourierWithRemoval {
    int id;
    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);

    @After
    public void logIn() {
        id = CourierClient.login(courierLogin, courierPassword).extract().path("id");
        CourierClient.delete(id).statusCode(200);
    }

    @Test
    @DisplayName("Создание курьера с валидными данными успешно")
    public void createNewCourierWithValidCredentialsSuccessful() {
        CourierClient.createCourier(courierLogin, courierPassword, courierFirstName)
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера с одинаковыми данными возвращает ошибку")
    public void createCourierWithSameCredentialsShowsError() {
        CourierClient.createCourier(courierLogin, courierPassword, courierFirstName)
                .statusCode(201);
        CourierClient.createCourier(courierLogin, courierPassword, courierFirstName)
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера с сущетсвующим логином возвращает ошибку")
    public void createCourierWithExistingLoginShowsError() {
        CourierClient.createCourier(courierLogin, courierPassword, courierFirstName)
                .statusCode(201);
        CourierClient.createCourier(courierLogin, "password", "name")
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
