import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import testSettings.CourierBasicRequests;


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
        CourierBasicRequests.create(courierLogin, courierPassword, courierFirstName).statusCode(201).and().assertThat().body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Создание курьера с одинаковыми данными возвращает ошибку")
    public void createCourierWithSameCredentialsShowsError() {
        CourierBasicRequests.create(courierLogin, courierPassword, courierFirstName).statusCode(201);
        ValidatableResponse createAnotherResponse = CourierBasicRequests.create(courierLogin, courierPassword, courierFirstName).statusCode(409);
        String responseText = createAnotherResponse.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", responseText);
    }

    @Test
    @DisplayName("Создание курьера с сущетсвующим логином возвращает ошибку")
    public void createCourierWithExistingLoginShowsError() {
        CourierBasicRequests.create(courierLogin, courierPassword, courierFirstName).statusCode(201);
        ValidatableResponse createResponse = CourierBasicRequests.create(courierLogin, "password", "name").statusCode(409);
        String responseText = createResponse.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", responseText);
    }


}
