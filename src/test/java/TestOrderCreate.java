import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.api.settings.client.OrderBasicRequests;
import test.api.settings.model.OrdersCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class TestOrderCreate {
    static String[] black = {"BLACK"};
    static String[] grey = {"GREY"};
    static String[] blackGrey = {"BLACK", "GREY"};
    private final String[] color;
    int track;


    public TestOrderCreate(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {black},
                {grey},
                {blackGrey}
        };
    }

    @After
    public void tearDown() {
        deleteCourier(track);
    }

    @Test
    @DisplayName("Создание заказа со значениями цветов BLACK, GREY и BLACK, GREY успешно ")
    public void createOrderColorParametersSuccessful() {
        OrdersCredentials credentials = new OrdersCredentials("ninja", "saske", "village", "basic", "79876578654", 2, "2020-06-06", "comment", color);
        ValidatableResponse response = orderCreate(credentials);
        assertStatusCode(response, 201);
        assertBody(response);
        track = response.extract().path("track");
    }

    @Step("Отправляем запрос на создание заказа")
    public ValidatableResponse orderCreate(OrdersCredentials credentials) {
        return OrderBasicRequests.create(credentials);
    }

    @Step("Сравниваем код ответа")
    public void assertStatusCode(ValidatableResponse response, int code) {
        response.statusCode(code);
    }

    @Step("Проверяем тело ответа")
    public void assertBody(ValidatableResponse response) {
        response.assertThat().body("track", is(not(0)));
    }

    @Step("Удаление созданного заказа")
    public void deleteCourier(int track) {
        OrderBasicRequests.delete(track).statusCode(200);
    }
}