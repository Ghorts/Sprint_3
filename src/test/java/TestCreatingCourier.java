import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import test.api.settings.client.CourierClient;

import static org.hamcrest.Matchers.equalTo;

public class TestCreatingCourier {
    String courierLogin = RandomStringUtils.randomAlphabetic(10);
    String courierPassword = RandomStringUtils.randomAlphabetic(10);
    String courierFirstName = RandomStringUtils.randomAlphabetic(10);

    @Test
    @DisplayName("Создание курьера без поля логин выдаёт ошибку")
    public void courierCreationWithoutLoginShowsError() {
        CourierClient.postCourier("{\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}")
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без поля пароль выдаёт ошибку")
    public void courierCreationWithoutPasswordShowsError() {
        CourierClient.postCourier("{\"login\":\"" + courierLogin + "\"," + "\"firstName\":\"" + courierFirstName + "\"}")
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без поля имя выдаёт ошибку")
    public void courierCreationFirstNameIsNullShowsError() {
        CourierClient.postCourier("{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"}")
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }
}
