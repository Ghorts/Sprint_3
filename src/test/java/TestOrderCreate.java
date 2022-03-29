import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import testSettings.OrderBasicRequests;
import testSettings.OrdersCredentials;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestOrderCreate {
    OrderBasicRequests request = new OrderBasicRequests();
    int track;
    private final String[] color;
    private final int expected;
    static String[] black = {"BLACK"};
    static String[] grey = {"GREY"};
    static String[] blackGrey = {"BLACK", "GREY"};


    public TestOrderCreate(String[] color, int expected) {
        this.color = color;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][] {
                {black, 201},
                {grey, 201},
                {blackGrey, 201}
        };
    }

    @After
    public void tearDown() {
        OrderBasicRequests requests = new OrderBasicRequests();
        request.delete(track).statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа со значениями цветов BLACK, GREY и BLACK, GREY успешно ")
    public void createOrderColorParametersSuccessful() {
        OrdersCredentials credentials = new OrdersCredentials("ninja", "saske", "village", "basic", "79876578654", 2, "2020-06-06", "comment", color);
        ValidatableResponse response = request.create(credentials);
        int actual = response.extract().statusCode();
        assertEquals(expected, actual);
        response.assertThat().body("track", is(not(0)));
        track = response.extract().path("track");
    }
}