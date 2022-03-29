import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import test.api.settings.client.OrderBasicRequests;
import test.api.settings.model.FullOrder;

import static org.junit.Assert.assertEquals;

public class TestGetOrders {
    @Test
    @DisplayName("Запрос возвращает список заказов в теле ответа")
    public void getOrdersLimit10ReturnListOfOrders() {
        FullOrder response = getOrders(10).body().as(FullOrder.class);
        assertBody(response);

    }

    @Step("Отправка запроса на получение списка заказов")
    public Response getOrders(int limit) {
        return OrderBasicRequests.getOrdersLimit(limit);
    }

    @Step("Проверка тела ответа на наличие заказов")
    public void assertBody(FullOrder response) {
        assertEquals(response.getOrders().isEmpty(), false);
    }
}


