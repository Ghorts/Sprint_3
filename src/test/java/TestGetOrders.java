
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import testSettings.FullOrder;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static testSettings.RequestSettings.getBaseSpec;

public class TestGetOrders {
    @Test
    @DisplayName("Запрос возвращает список заказов в теле ответа")
    public void getOrdersLimit10ReturnListOfOrders() {
        FullOrder response = given()
                .spec(getBaseSpec())
                .when()
                .get("/api/v1/orders?limit=10").body().as(FullOrder.class);
        assertEquals(response.getOrders().isEmpty(), false);

    }
}
