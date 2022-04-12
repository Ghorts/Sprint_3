import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.api.settings.model.OrdersCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static test.api.settings.client.OrderClient.deleteCourier;
import static test.api.settings.client.OrderClient.orderCreate;

@RunWith(Parameterized.class)
public class TestOrderCreate {
    static String[] black = {"BLACK"};
    static String[] grey = {"GREY"};
    static String[] blackGrey = {"BLACK", "GREY"};
    private final String[] color;
    int track;
    ValidatableResponse response;

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
        track = response.extract().path("track");
        deleteCourier(track);
    }

    @Test
    @DisplayName("Создание заказа со значениями цветов BLACK, GREY и BLACK, GREY успешно ")
    public void createOrderColorParametersSuccessful() {
        OrdersCredentials credentials = new OrdersCredentials("ninja", "saske", "village", "basic", "79876578654", 2, "2020-06-06", "comment", color);
        response = orderCreate(credentials)
                .statusCode(201)
                .and()
                .assertThat().body("track", is(not(0)));
    }
}